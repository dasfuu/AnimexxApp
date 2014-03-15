package de.meisterfuu.animexx.xmpp;

import java.util.ArrayList;


import de.meisterfuu.animexx.R;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;

public class XMPPService extends Service {

	Handler mHandler, mTHandler;
	Thread mThread;
	static XMPPService mThis;
	
	ArrayList<String> rooster;

	private ReconnectionManager mReconManager;
	private ChatConnection mConnection;
	
	public static final String TAG = "XMPP";

	public static final String NEW_MESSAGE = "de.meisterfuu.animexx.xmpp.newmessage";
	public static final String SEND_MESSAGE = "de.meisterfuu.animexx.xmpp.sendmessage";
	public static final String SEND_MESSAGE_OK = "de.meisterfuu.animexx.xmpp.sendmessage.ok";
	public static final String SEND_MESSAGE_BAD = "de.meisterfuu.animexx.xmpp.sendmessage.bad";
	public static final String NEW_CHAT = "de.meisterfuu.animexx.xmpp.newchat";
	public static final String NEW_ROOSTER = "de.meisterfuu.animexx.xmpp.newrooster";
	
	public static final String BUNDLE_FROM = "b_from";
	public static final String BUNDLE_TO = "b_from";
	public static final String BUNDLE_TIME = "b_time";
	public static final String BUNDLE_MESSAGE_BODY = "b_body";
	
	
	public static XMPPService getInstance(){
		return mThis;
	}
	
	public ChatConnection getConnection(){
		return mConnection;
	}
	
	
	@Override
	public void onCreate() {
		super.onCreate();

		mHandler = new Handler();
		mThis = this;

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


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		Notification notification = getNotification("");		
		this.startForeground(42, notification);
		

		
		if(mThread == null || !mThread.isAlive()){
			mThread = new Thread(new Runnable() {
				@Override
				public void run() {
						Looper.prepare();
						mTHandler = new Handler();
						mConnection = new ChatConnection(XMPPService.this);
						mConnection.connect();
						mReconManager = new ReconnectionManager(XMPPService.this, mConnection);
						mReconManager.start();
						Looper.loop();
				}
	
			});
			mThread.start();
		} 
		
		
		return Service.START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mThis = null;
		mTHandler.post(new Runnable() {
			
			@Override
			public void run() {
				mReconManager.stop();
				mConnection.disconnect();

			}
		});
		stopForeground(true);
	}
	
}
