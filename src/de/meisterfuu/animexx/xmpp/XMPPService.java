package de.meisterfuu.animexx.xmpp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

import org.jivesoftware.smack.AndroidConnectionConfiguration;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Presence;

import de.meisterfuu.animexx.Debug;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.data.ens.ENSApi;
import de.meisterfuu.animexx.notification.XMPPNotification;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;

public class XMPPService extends Service implements ChatManagerListener,
		MessageListener, RosterListener {

	Handler mHandler, mTHandler;
	XMPPConnection mConnection;
	Thread mThread;
	static XMPPService mThis;
	ArrayList<String> rooster;
	static long lastLogin, lastStart, lastCreate;
	
	public static final String TAG = "XMPP";

	public static final String NEW_MESSAGE = "de.meisterfuu.animexx.xmpp.newmessage";
	public static final String SEND_MESSAGE = "de.meisterfuu.animexx.xmpp.sendmessage";
	public static final String SEND_MESSAGE_OK = "de.meisterfuu.animexx.xmpp.sendmessage.ok";
	public static final String SEND_MESSAGE_BAD = "de.meisterfuu.animexx.xmpp.sendmessage.bad";
	public static final String NEW_CHAT = "de.meisterfuu.animexx.xmpp.newchat";
	public static final String NEW_ROOSTER = "de.meisterfuu.animexx.xmpp.newrooster";
	

	public XMPPService() {

	}
	
	public static XMPPService getInstace(){
		return mThis;
	}
	
	public static final String BUNDLE_TO = "b_from";
	
	@Override
	public void onCreate() {
		super.onCreate();

		lastCreate = System.currentTimeMillis();
		mHandler = new Handler();
		mThis = this;
		
		rooster = new ArrayList<String>();
		
		SmackAndroid.init(this);
		// turn on the enhanced debugger
		XMPPConnection.DEBUG_ENABLED = true;
		setupNewMessageReceiver();
	}
	
	private Notification getNotification(String pTitle){
		final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);		
		Notification notification = builder
			.setOngoing(true)
			.setTicker("Animexxenger")
			.setContentTitle("Animexxenger")
			.setContentText("XMPP Aktiv")
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentIntent(XMPPRoosterFragment.getPendingIntent(this))
			.build();
		 return notification;
	}

	private void setupNewMessageReceiver() {
		final BroadcastReceiver receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (action.equals(SEND_MESSAGE)) {
					try {
						sendMessage(intent.getStringExtra(BUNDLE_MESSAGE_BODY), intent.getStringExtra(BUNDLE_TO));
						Intent i = new Intent(SEND_MESSAGE_OK);
						i.setPackage(XMPPService.this.getPackageName());	
						getApplicationContext().sendBroadcast(i);
					} catch (XMPPException e) {
						Intent i = new Intent(SEND_MESSAGE_BAD);
						i.setPackage(XMPPService.this.getPackageName());	
						getApplicationContext().sendBroadcast(i);
					}
				}
			}
		};
		
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(SEND_MESSAGE);
		registerReceiver(receiver, filter);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Notification notification = getNotification("");		
//		final NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//		nm.notify(42, notification);
		this.startForeground(42, notification);
		
		lastStart = System.currentTimeMillis();
		// Enter your login information here
		try {
			if (mConnection == null || !mConnection.isConnected()) {
				login(Debug.XMPP_USER, Debug.XMPP_PW);
	
			}
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mThis = null;
		disconnect();
		stopForeground(true);
	}

	public void login(final String userName, final String password)
			throws XMPPException {
		
		lastLogin = System.currentTimeMillis();
		if(mConnection == null){
			AndroidConnectionConfiguration config = new AndroidConnectionConfiguration("jabber.animexx.de");
			config.setReconnectionAllowed(true);
			mConnection = new XMPPConnection(config);
			if(Debug.XMPP_CONNECTION_LISTENER)mConnection.addConnectionListener(new ConnectionListener() {
				 
	            @Override
	            public void reconnectionSuccessful() {
	            	Date d = new Date();
	                ENSApi.sendENSDEBUG("reconnectionSuccessful at "+d.toString(), XMPPService.this);
	            }
	            
	            @Override
	            public void reconnectionFailed(Exception arg0) {
	            	Date d = new Date();
	                ENSApi.sendENSDEBUG("reconnectionFailed at "+d.toString(), XMPPService.this);
	            }
	 
	            @Override
	            public void reconnectingIn(int seconds) {
	            	Date d = new Date();
	                ENSApi.sendENSDEBUG("reconnectingIn "+ seconds +"s at "+d.toString(), XMPPService.this);
	            }
	            
	            @Override
	            public void connectionClosedOnError(Exception arg0) {
	            	Date d = new Date();
	                ENSApi.sendENSDEBUG("connectionClosedOnError at "+d.toString(), XMPPService.this);
	            }
	            
	            @Override
	            public void connectionClosed() {
	            	Date d = new Date();
	                ENSApi.sendENSDEBUG("connectionClosed at "+d.toString(), XMPPService.this);
	            }
	        });
		}

		if(mThread == null || !mThread.isAlive()){
			mThread = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Looper.prepare();
						mConnection.connect();
						mConnection.login(userName, password);
						mConnection.getChatManager().addChatListener(XMPPService.this);
						mConnection.getRoster().addRosterListener(XMPPService.this);
						mTHandler = new Handler();
						Looper.loop();
					} catch (XMPPException e) {
						e.printStackTrace();
					}
				}
	
			});
			mThread.start();
		} else {
			mTHandler.post(new Runnable() {
				
				@Override
				public void run() {
					try {
						mConnection.connect();
						mConnection.login(userName, password);
						mConnection.getChatManager().addChatListener(XMPPService.this);
						mConnection.getRoster().addRosterListener(XMPPService.this);
					} catch (XMPPException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}

	public void sendMessage(String message, String to) throws XMPPException {
		System.out.println("this:"+this);
		System.out.println("to:"+to);
		System.out.println("message:"+message);
		Chat chat = mConnection.getChatManager().createChat(to, this);
		chat.sendMessage(message);
	}

	public void disconnect() {
		mConnection.disconnect();
	}

	@Override
	public void chatCreated(Chat chat, boolean arg1) {
		chat.addMessageListener(this);
	}

	public static final String BUNDLE_FROM = "b_from";
	public static final String BUNDLE_TIME = "b_time";
	public static final String BUNDLE_MESSAGE_BODY = "b_body";

	@Override
	public void processMessage(Chat chat, Message message) {
		if(message.getType().equals(Type.chat) || message.getType().equals(Type.normal)) {
			if(message.getBody() != null && !chat.getParticipant().startsWith("animexx")) {
				Intent intent = new Intent(XMPPService.NEW_MESSAGE);
				intent.setPackage(this.getPackageName());
				intent.putExtra(BUNDLE_MESSAGE_BODY, message.getBody());
				intent.putExtra(BUNDLE_FROM, chat.getParticipant());
				intent.putExtra(BUNDLE_TIME, ""+System.currentTimeMillis());
				getApplicationContext().sendBroadcast(intent);
				XMPPNotification.notify(this, message.getBody(), chat.getParticipant().split("@")[0]);
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
		Intent intent = new Intent(XMPPService.NEW_ROOSTER);
		intent.setPackage(this.getPackageName());
		getApplicationContext().sendBroadcast(intent);
	}
	
	public Collection<RosterEntry> getRoster(){
		Roster roster = mConnection.getRoster();
		Collection<RosterEntry> entries = roster.getEntries();
		return Collections.unmodifiableCollection(entries);
	}
//	public void displayBuddyList() {
//	Roster roster = connection.getRoster();
//	Collection<RosterEntry> entries = roster.getEntries();
//	
//
//	print("\n\n" + roster.getEntryCount() + " buddy(ies):");
//	for (RosterEntry r : entries) {
//		print(r.getUser());
//	}
//}
}
