package de.meisterfuu.animexx.xmpp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;


import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.TCPConnection;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smackx.ping.PingFailedListener;
import org.jivesoftware.smackx.ping.PingManager;

import de.meisterfuu.animexx.Debug;
import de.meisterfuu.animexx.DebugNotification;
import de.meisterfuu.animexx.data.Self;
import de.meisterfuu.animexx.data.profile.UserApi;
import de.meisterfuu.animexx.data.xmpp.XMPPApi;
import de.meisterfuu.animexx.notification.XMPPNotification;
import de.meisterfuu.animexx.objects.UserObject;
import de.meisterfuu.animexx.objects.XMPPMessageObject;
import de.meisterfuu.animexx.objects.XMPPRoosterObject;
import de.meisterfuu.animexx.utils.Helper;
import de.meisterfuu.animexx.utils.imageloader.ImageSaveObject;
import de.meisterfuu.animexx.utils.imageloader.ImageSaverCustom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.preference.PreferenceManager;
import android.util.Log;

public class ChatConnection implements MessageListener, ChatManagerListener, RosterListener, PingFailedListener, ConnectionListener {

	TCPConnection mConnection;
	XMPPApi mApi;
	public static final String TAG = "XMPP";
	private Context mApplicationContext;
	protected boolean connectionState;
	private PingManager mPingManager;
	private int res_attach;
	private String ressource;
	
	public ChatConnection(Context pContext){
		Log.i(TAG, "ChatConnection Constructor called");
		mApplicationContext = pContext.getApplicationContext();
		mApi = new XMPPApi(mApplicationContext);
		
    	connectionState = false;
		SmackAndroid.init(mApplicationContext);
		
		TCPConnection.DEBUG_ENABLED = Debug.XMPP_DEBUG_ENABLE;
		SmackConfiguration.setDefaultPacketReplyTimeout(30000);
		
		Random r = new Random();
		res_attach = r.nextInt();
		ressource = "AndroidApp_"+res_attach;
		
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
					return false;
				}	
				
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
		getConnection().disconnect();
		mConnection = null;
		connectionState = false;
		mApi.close();
		mApi = null;
	}
	
	public boolean shouldConnect(){
		return PreferenceManager.getDefaultSharedPreferences(mApplicationContext).getBoolean("xmpp_status", false);
	}
	
	private void login(final String userName, final String password) throws Exception {
		
		//Create TCPConnection
		if(getConnection() == null){
			ConnectionConfiguration config = new ConnectionConfiguration("jabber.animexx.de");
			
			config.setReconnectionAllowed(true);
			Log.i(TAG, "mConnection = new TCPConnection(config) called");
			mConnection = new TCPConnection(config);
			connectionState = true;			
		}
		
		//Create Pingmanager
		initKeepAlive();

		//Connect and login(if not already)
		Log.i(TAG, "TCPConnection.connect() called");
		getConnection().connect();
		if(!getConnection().isAuthenticated()){
			Log.i(TAG, ".login() called");
			getConnection().login(userName, password, ressource);
		}
		
		//Set listener
		this.setChatListener();
		this.setRosterListener();		
		getConnection().addConnectionListener(this);
		
	}
	
	private void initKeepAlive() {
		PingManager.setDefaultPingInterval(10);
		mPingManager = PingManager.getInstanceFor(getConnection());
		mPingManager.registerPingFailedListener(this);
		mPingManager.setPingInterval(10);
	}
	
	public void ping(){
		try {
			mPingManager.pingMyServer();
		} catch (SmackException e) {
			connectionState = false;
			e.printStackTrace();
		}
	}

	public TCPConnection getConnection(){
		return mConnection;
	}
	
	public boolean isConnected(){		
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

	@Override
	public void processMessage(Chat chat, Message message) {
		if(message.getType().equals(Type.chat) || message.getType().equals(Type.normal)) {
			if(message.getBody() != null && !chat.getParticipant().startsWith("animexx")) {
				Intent intent = new Intent(XMPPService.NEW_MESSAGE);
				intent.setPackage(mApplicationContext.getPackageName());
				intent.putExtra(XMPPService.BUNDLE_MESSAGE_BODY, message.getBody());
				intent.putExtra(XMPPService.BUNDLE_FROM, chat.getParticipant());
				intent.putExtra(XMPPService.BUNDLE_TIME, ""+System.currentTimeMillis());
				mApplicationContext.sendBroadcast(intent);
				
				System.out.println("new message: "+XMPPNotification.d_from+" "+chat.getParticipant());
				if(XMPPNotification.d_from != null && XMPPNotification.d_from.equalsIgnoreCase(chat.getParticipant().split("/")[0])){
				} else {
					XMPPNotification.notify(mApplicationContext, message.getBody(), chat.getParticipant().split("@")[0]);
				}
	
				
				XMPPMessageObject msg = new XMPPMessageObject();
				msg.setDate(System.currentTimeMillis());
				msg.setTopicJID(chat.getParticipant().split("/")[0]);
				msg.setBody(message.getBody());
				msg.setMe(false);
				mApi.insertMessageToDB(msg);
			}
		}
	}

	@Override
	public void entriesAdded(Collection<String> arg0) {
		newRoster();
	}

	@Override
	public void entriesDeleted(Collection<String> arg0) {
		newRoster();
	}

	@Override
	public void entriesUpdated(Collection<String> arg0) {
		newRoster();
	}

	@Override
	public void presenceChanged(Presence pNewPresence) {
		
		XMPPRoosterObject temp = mApi.NTgetSingleRooster(pNewPresence.getFrom().split("/")[0]);
		if(temp == null){
			newRoster();
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
		mApi.insertSingleRoosterToDB(temp);
		
		Intent intent = new Intent(XMPPService.NEW_ROOSTER);
		intent.setPackage(mApplicationContext.getPackageName());
		mApplicationContext.sendBroadcast(intent);
	}
	
	private void newRoster(){
		ImageSaverCustom loader = new ImageSaverCustom("forenavatar");
		
		ArrayList<String> names = new ArrayList<String>();
		for(RosterEntry obj: getConnection().getRoster().getEntries()){	
			names.add(obj.getName());			
		}		
		
		ArrayList<UserObject> user_list = (new UserApi(mApplicationContext).NTgetIDs(names));
		
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
			
			if(user_list != null)
			for(UserObject user_obj: user_list){	
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
   	Log.i(TAG, "ConnectionListener.connectionClosedOnError() called");
   }
   
   @Override
   public void connectionClosed() {
   	connectionState = false;
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
	}


}
