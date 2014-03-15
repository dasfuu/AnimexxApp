package de.meisterfuu.animexx.xmpp;

import java.util.Collection;

import org.jivesoftware.smack.AndroidConnectionConfiguration;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Message.Type;

import de.meisterfuu.animexx.Debug;
import de.meisterfuu.animexx.data.Self;
import de.meisterfuu.animexx.data.xmpp.XMPPApi;
import de.meisterfuu.animexx.notification.XMPPNotification;
import de.meisterfuu.animexx.objects.XMPPRoosterObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.preference.PreferenceManager;

public class ChatConnection implements MessageListener, ChatManagerListener, RosterListener {

	XMPPConnection mConnection;
	XMPPApi mApi;
	public static final String TAG = "XMPP";
	private Context mApplicationContext;
	
	public ChatConnection(Context pContext){
		mApplicationContext = pContext.getApplicationContext();
		mApi = new XMPPApi(mApplicationContext);
		SmackAndroid.init(mApplicationContext);
		
		// turn on the enhanced debugger
		XMPPConnection.DEBUG_ENABLED = true;
		
		setupNewMessageReceiver();
	}
	
	public boolean connect(){
			try {
				//Get Username
				String username = Self.getInstance(mApplicationContext).getUsername();
				
				//Password?
				String password = PreferenceManager.getDefaultSharedPreferences(mApplicationContext).getString("xmpp_password", null);
				if(password == null){
					return false;
				}	
				if (mConnection == null || !mConnection.isConnected()) {
					login(username, password);	
				} else {
					return false;
				}
			} catch (XMPPException e) {
				e.printStackTrace();
				return false;
			}
			return true;
	}
	
	public void disconnect(){
		mConnection.disconnect();
		mApi.close();
	}
	
	public boolean isConnected(){
		return mConnection.isConnected();		
	}
	
	public boolean shouldConnect(){
		return PreferenceManager.getDefaultSharedPreferences(mApplicationContext).getBoolean("xmpp_status", false);
	}
	
	private void login(final String userName, final String password) throws XMPPException {
		
		if(mConnection == null){
			AndroidConnectionConfiguration config = new AndroidConnectionConfiguration("jabber.animexx.de");
			config.setReconnectionAllowed(true);
			mConnection = new XMPPConnection(config);

		}
		
		mConnection.connect();
		if(!mConnection.isAuthenticated())mConnection.login(userName, password);
		this.setChatListener();
		this.setRosterListener();
		
		if(Debug.XMPP_CONNECTION_LISTENER)mConnection.addConnectionListener(new ConnectionListener() {
			 
            @Override
            public void reconnectionSuccessful() {
            }
            
            @Override
            public void reconnectionFailed(Exception arg0) {
            }
 
            @Override
            public void reconnectingIn(int seconds) {
            }
            
            @Override
            public void connectionClosedOnError(Exception arg0) {
            }
            
            @Override
            public void connectionClosed() {
            }
            
        });
		
	}
	
	public XMPPConnection getConnection(){
		return mConnection;
	}
	
	
	public void sendMessage(String message, String to) throws XMPPException {
		System.out.println("this:"+this);
		System.out.println("to:"+to);
		System.out.println("message:"+message);
		Chat chat = mConnection.getChatManager().createChat(to, this);
		chat.sendMessage(message);
	}
	
	private void setChatListener(){
		getConnection().getChatManager().addChatListener(this);
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
				XMPPNotification.notify(mApplicationContext, message.getBody(), chat.getParticipant().split("@")[0]);
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
	public void presenceChanged(Presence arg0) {
		newRoster();
	}
	
	private void newRoster(){
		
		for(RosterEntry obj: mConnection.getRoster().getEntries()){
			XMPPRoosterObject temp = new XMPPRoosterObject();
			temp.setJid(obj.getUser());
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
					}
				}
			}
			
		};		
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(XMPPService.SEND_MESSAGE);
		mApplicationContext.registerReceiver(receiver, filter);
	}


}
