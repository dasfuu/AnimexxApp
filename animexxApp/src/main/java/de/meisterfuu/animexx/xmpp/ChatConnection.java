package de.meisterfuu.animexx.xmpp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;


import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smackx.carbons.CarbonManager;
import org.jivesoftware.smackx.carbons.packet.CarbonExtension;
import org.jivesoftware.smackx.forward.Forwarded;
import org.jivesoftware.smackx.ping.PingFailedListener;
import org.jivesoftware.smackx.ping.PingManager;

import de.meisterfuu.animexx.Beta;
import de.meisterfuu.animexx.Debug;
import de.meisterfuu.animexx.DebugNotification;
import de.meisterfuu.animexx.api.Self;
import de.meisterfuu.animexx.api.broker.UserBroker;
import de.meisterfuu.animexx.api.xmpp.XMPPApi;
import de.meisterfuu.animexx.notification.XMPPNotification;
import de.meisterfuu.animexx.objects.UserObject;
import de.meisterfuu.animexx.objects.xmpp.XMPPMessageObject;
import de.meisterfuu.animexx.objects.xmpp.XMPPRoosterObject;
import de.meisterfuu.animexx.utils.Helper;
import de.meisterfuu.animexx.utils.imageloader.ImageSaveObject;
import de.meisterfuu.animexx.utils.imageloader.ImageSaverCustom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.preference.PreferenceManager;
import android.util.Log;

public class ChatConnection implements MessageListener, ChatManagerListener, RosterListener, PingFailedListener, ConnectionListener, PacketListener {

	XMPPTCPConnection mConnection;
	XMPPApi mApi;
	public static final String TAG = "XMPP";
	private Context mApplicationContext;
	protected boolean connectionState;
	private PingManager mPingManager;
	private int res_attach;
	private String ressource;
	private ConnectionManager mManager;
	
	public ChatConnection(Context pContext, ConnectionManager pManager){
		Log.i(TAG, "ChatConnection Constructor called");
		mApplicationContext = pContext.getApplicationContext();
		mApi = new XMPPApi(mApplicationContext);
		mManager = pManager;
		
    	connectionState = false;
		SmackAndroid.init(mApplicationContext);

        SmackConfiguration.DEBUG_ENABLED = Debug.XMPP_DEBUG_ENABLE;
		SmackConfiguration.setDefaultPacketReplyTimeout(30000);
		
		Random r = new Random();
		res_attach = r.nextInt();
		ressource = "AndroidApp_2";//+res_attach;
		
		setupNewMessageReceiver();
	}
	
	public boolean connect(){
		if(mApi == null) mApi = new XMPPApi(mApplicationContext);
		
    	Log.i(TAG, "ChatConnection.connect() called");
			try {
				
				//Get Username
				String username = Self.getInstance(mApplicationContext).getUsername();
				if(username == null){
					return false;
				}	
				
				//Password?
				String password = PreferenceManager.getDefaultSharedPreferences(mApplicationContext).getString("xmpp_password", null);
				if(password == null){
					password = mApi.NTgetNewChatAuth();
					PreferenceManager.getDefaultSharedPreferences(mApplicationContext).edit().putString("xmpp_password", password).apply();
				}	
//				username = "flo2";
//				password = "123";
				login(username, password);		
				newRoster();
			} catch (Exception e) {
				e.printStackTrace();
				Helper.sendStacTrace(e, mApplicationContext);
				return false;
			}
			return true;
	}
	
	public void disconnect(){
    	Log.i(TAG, ".disconnect() called");
        try {
            getConnection().disconnect();
        } catch (NotConnectedException e) {

        }
        mConnection = null;
		connectionState = false;
		mApi.close();
		mApi = null;

		Intent intent = new Intent(XMPPService.NEW_ROOSTER);
		intent.setPackage(mApplicationContext.getPackageName());
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
		}
		mApplicationContext.sendBroadcast(intent);
	}
	
	public boolean shouldConnect(){
		return PreferenceManager.getDefaultSharedPreferences(mApplicationContext).getBoolean("xmpp_status", false);
	}
	
	private void login(final String userName, final String password) throws Exception {

		//Create TCPConnection
		if(getConnection() == null){
			ConnectionConfiguration config = new ConnectionConfiguration("jabber.animexx.de");

//			ConnectionConfiguration config = new ConnectionConfiguration("192.168.1.116");
//			config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

			config.setRosterLoadedAtLogin(true);

			config.setReconnectionAllowed(false);
			Log.i(TAG, "mConnection = new TCPConnection(config) called");
			mConnection = new XMPPTCPConnection(config);
			connectionState = true;			
		}
		
		//Create Pingmanager
		initKeepAlive();

		//Connect and login(if not already)
		Log.i(TAG, "TCPConnection.connect() called");
		getConnection().connect();
		if(!getConnection().isAuthenticated()){
			Log.i(TAG, ".login() called");
			try{
				getConnection().login(userName, password, ressource);
			} catch (XMPPException e){
				PreferenceManager.getDefaultSharedPreferences(mApplicationContext).edit().putString("xmpp_password", null).apply();
				throw e;
			}
		}



		//Set listener
		this.setChatListener();
		this.setRosterListener();
		getConnection().addConnectionListener(this);

		initCarbonManager();
		
	}


	public void initCarbonManager() {
		try {
			if(CarbonManager.getInstanceFor(getConnection()).isSupportedByServer()){
				CarbonManager.getInstanceFor(getConnection()).enableCarbons();
			}
		} catch (XMPPException e) {
			e.printStackTrace();
		} catch (SmackException e) {
			e.printStackTrace();
		}
	}

	private void initKeepAlive() {
		PingManager.setDefaultPingInterval(10);
		mPingManager = PingManager.getInstanceFor(getConnection());
		mPingManager.registerPingFailedListener(this);
	}
	
	public void ping(){
		try {
			mPingManager.pingMyServer();
		} catch (SmackException e) {
			connectionState = false;
			e.printStackTrace();
		}
	}

	public XMPPTCPConnection getConnection(){
		return mConnection;
	}
	
	public boolean isConnected(){		
		ping();
		return (getConnection().isConnected() && connectionState && !getConnection().isSocketClosed());
	}
	
	public void sendMessage(String message, String to) throws NotConnectedException, XMPPException {
		System.out.println("this:"+this);
		System.out.println("to:"+to);
		System.out.println("message:"+message);
		Chat chat = ChatManager.getInstanceFor(getConnection()).createChat(to, this);	
		try {
			chat.sendMessage(message);
		} catch (NotConnectedException e) {
			connectionState = false;
			e.printStackTrace();
			Helper.sendStacTrace(e, mApplicationContext);
			throw e;
		} catch (XMPPException e) {
			e.printStackTrace();
			Helper.sendStacTrace(e, mApplicationContext);
			throw e;
		}
	}


	private void setChatListener(){
		ChatManager.getInstanceFor(getConnection()).addChatListener(this);
	}
	
	private void setRosterListener(){
		getConnection().getRoster().addRosterListener(this);
	}
	
	@Override
	public void chatCreated(Chat chat, boolean arg1) {
		chat.addMessageListener(this);
	}

	private String getBareJID(String from) {
		String[] res = from.split("/");
		return res[0].toLowerCase();
	}

	@Override
	public void processMessage(Chat chat, final Message pMessage) {
		Message message = pMessage;
		boolean carbonCopied = false;
		String fromJID;
		String direction = XMPPService.BUNDLE_DIRECTION_IN;
		CarbonExtension c = CarbonManager.getCarbon(pMessage);
		if(c != null){
			carbonCopied = true;
			Forwarded fwd = c.getForwarded();
			message = (Message)fwd.getForwardedPacket();

			if (c.getDirection() == CarbonExtension.Direction.sent) {
				fromJID = getBareJID(message.getTo());
				direction = XMPPService.BUNDLE_DIRECTION_OUT;
			} else {
				fromJID = getBareJID(message.getFrom());
			}
		} else {
			fromJID = getBareJID(message.getFrom());
		}
		
		if(message.getType().equals(Type.chat) || message.getType().equals(Type.normal)) {
			if(message.getBody() != null && !chat.getParticipant().startsWith("animexx")) {
				Intent intent = new Intent(XMPPService.NEW_MESSAGE);
				intent.setPackage(mApplicationContext.getPackageName());
				intent.putExtra(XMPPService.BUNDLE_MESSAGE_BODY, message.getBody());
				intent.putExtra(XMPPService.BUNDLE_FROM, fromJID);
				intent.putExtra(XMPPService.BUNDLE_DIRECTION, direction);
				intent.putExtra(XMPPService.BUNDLE_TIME, ""+System.currentTimeMillis());
			    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
				    intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
			    }
				mApplicationContext.sendBroadcast(intent);

				Long id = mApi.getSingleRoosterFromDB(fromJID).getAnimexxID();

				System.out.println("new message: "+XMPPNotification.d_from+" "+chat.getParticipant());
				if(carbonCopied || (XMPPNotification.d_from != null && XMPPNotification.d_from.equalsIgnoreCase(fromJID))){
					//No Notification
				} else {
					XMPPNotification.notify(mApplicationContext, message.getBody(), fromJID, ""+id);
				}
	
				
				XMPPMessageObject msg = new XMPPMessageObject();
				msg.setDate(System.currentTimeMillis());
				msg.setTopicJID(fromJID);
				msg.setBody(message.getBody());
				if(direction.equals(XMPPService.BUNDLE_DIRECTION_IN)){
					msg.setMe(false);
				} else {
					msg.setMe(true);
				}
				mApi.insertMessageToDB(msg);
			}
		}
	}

	@Override
	public void entriesAdded(Collection<String> arg0) {
		for (String s: arg0){
			Log.i(TAG, "entriesAdded: "+ s);
		}
		newRoster();
	}

	@Override
	public void entriesDeleted(Collection<String> arg0) {
        for (String name: arg0){
            mApi.deleteSingleRoosterFromDB(name);
        }

        Intent intent = new Intent(XMPPService.NEW_ROOSTER);
        intent.setPackage(mApplicationContext.getPackageName());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        }
        mApplicationContext.sendBroadcast(intent);
	}

	@Override
	public void entriesUpdated(Collection<String> arg0) {
		for (String s: arg0){
			Log.i(TAG, "entriesUpdated: "+ s);
		}

        newRoster();
	}

	@Override
	public void presenceChanged(Presence pNewPresence) {

		Log.i(TAG, "presenceChanged: "+pNewPresence.getFrom());

		XMPPRoosterObject temp = mApi.NTgetSingleRooster(pNewPresence.getFrom().split("/")[0]);
		if(temp == null){
			newRoster();
			return;
		}


		boolean online = getConnection().getRoster().getPresence(pNewPresence.getFrom()).isAvailable();
		boolean away = pNewPresence.isAway();
		
		if(online && !away){
			temp.setStatus(XMPPRoosterObject.STATUS_ONLINE);
		} else if (!online){
			temp.setStatus(XMPPRoosterObject.STATUS_OFFLINE);
		} else if (online && away){
			temp.setStatus(XMPPRoosterObject.STATUS_AWAY);
		}

		Beta.notifyOnline(temp, mApplicationContext);

		mApi.insertSingleRoosterToDB(temp);
		
		Intent intent = new Intent(XMPPService.NEW_ROOSTER);
		intent.setPackage(mApplicationContext.getPackageName());
	    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
		    intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
	    }
		mApplicationContext.sendBroadcast(intent);
	}
	
	private void newRoster(){
		ImageSaverCustom loader = new ImageSaverCustom("forenavatar");
		
		ArrayList<String> names = new ArrayList<String>();
		for(RosterEntry obj: getConnection().getRoster().getEntries()){
			Log.i(TAG, "newRoster: "+obj.getName() +" "+getConnection().getRoster().getPresence(obj.getUser()).isAvailable());
            if(obj.getUser().contains("@jabber.animexx.de")){
                names.add(obj.getName());
            }
		}		
		
		List<UserObject> user_list = (new UserBroker(mApplicationContext).NTgetIDs(names));
		
		for(RosterEntry obj: getConnection().getRoster().getEntries()){
			
			XMPPRoosterObject temp = new XMPPRoosterObject();
			temp.setJid(obj.getUser());
			boolean online = getConnection().getRoster().getPresence(obj.getUser()).isAvailable();
			boolean away = getConnection().getRoster().getPresence(obj.getUser()).isAway();
			if(online && !away){
				temp.setStatus(XMPPRoosterObject.STATUS_ONLINE);
			} else if (!online){
				temp.setStatus(XMPPRoosterObject.STATUS_OFFLINE);
			} else if (online && away){
				temp.setStatus(XMPPRoosterObject.STATUS_AWAY);
			}
			Beta.notifyOnline(temp, mApplicationContext);

			if(user_list != null)
			for(UserObject user_obj: user_list){
                Log.e(TAG, user_obj.getUsername() + " == "+obj.getName());
				if(user_obj.getUsername().equalsIgnoreCase(obj.getName())){
					temp.setAnimexxID(user_obj.getId());
					if(user_obj.getAvatar() != null){
						temp.setLastAvatarURL(user_obj.getAvatar().getUrl());
						loader.download(new ImageSaveObject(temp.getLastAvatarURL(), temp.getAnimexxID()+""), mApplicationContext);
					}
				}
			}	

			mApi.insertSingleRoosterToDB(temp);
		}
		
		
		Intent intent = new Intent(XMPPService.NEW_ROOSTER);
		intent.setPackage(mApplicationContext.getPackageName());
	    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
		    intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
	    }
		mApplicationContext.sendBroadcast(intent);
	}
	
	private void setupNewMessageReceiver() {
		final BroadcastReceiver receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (action.equals(XMPPService.SEND_MESSAGE)) {
					try {
						sendMessage(intent.getStringExtra(XMPPService.BUNDLE_MESSAGE_BODY), intent.getStringExtra(XMPPService.BUNDLE_TO));
						Intent i = new Intent(XMPPService.SEND_MESSAGE_OK);
						i.setPackage(mApplicationContext.getPackageName());	
						mApplicationContext.sendBroadcast(i);
					} catch (XMPPException e) {
						
						Intent i = new Intent(XMPPService.SEND_MESSAGE_BAD);
						i.setPackage(mApplicationContext.getPackageName());	
						mApplicationContext.sendBroadcast(i);
					} catch (NotConnectedException e) {
						Intent i = new Intent(XMPPService.SEND_MESSAGE_BAD);
						i.setPackage(mApplicationContext.getPackageName());	
						mApplicationContext.sendBroadcast(i);
					}
				}
			}
			
		};		
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(XMPPService.SEND_MESSAGE);
		mApplicationContext.registerReceiver(receiver, filter);
	}

	@Override
	public void pingFailed() {
		connectionState = false;
	   	Log.i(TAG, ".pingFailed() called");
		DebugNotification.notify(mApplicationContext, "XMPP Ping Failed", 865);
	}

   @Override
   public void reconnectionSuccessful() {
	   	Log.i(TAG, "ConnectionListener.reconnectionSuccessful() called");
	   	initKeepAlive();
	    initCarbonManager();
   }
   
   @Override
   public void reconnectionFailed(Exception arg0) {
   }

   @Override
   public void reconnectingIn(int seconds) {
   }
   
   @Override
   public void connectionClosedOnError(Exception arg0) {
	   	connectionState = false;
   		mManager.scheduleCheck();
	   	Log.i(TAG, "ConnectionListener.connectionClosedOnError() called");
   }
   
   @Override
   public void connectionClosed() {
   		connectionState = false;
   		mManager.scheduleCheck();
		Log.i(TAG, "ConnectionListener.connectionClosed() called");
   }

	@Override
	public void authenticated(XMPPConnection arg0) {
		Log.i(TAG, "ConnectionListener.authenticated() called");				
	}

	@Override
	public void connected(XMPPConnection arg0) {
		connectionState = true;
		Log.i(TAG, "ConnectionListener.connected() called");
	   	initKeepAlive();
		initCarbonManager();
	}


	@Override
	public void processPacket(final Packet pPacket) throws NotConnectedException {

	}
}
