package de.meisterfuu.animexx.xmpp;

import de.meisterfuu.animexx.R;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

public class XMPPService extends Service {

	public static final String TAG = "XMPP";

	public static final String NEW_MESSAGE = "de.meisterfuu.animexx.xmpp.newmessage";
	public static final String SEND_MESSAGE = "de.meisterfuu.animexx.xmpp.sendmessage";
	public static final String SEND_MESSAGE_OK = "de.meisterfuu.animexx.xmpp.sendmessage.ok";
	public static final String SEND_MESSAGE_BAD = "de.meisterfuu.animexx.xmpp.sendmessage.bad";
	public static final String NEW_CHAT = "de.meisterfuu.animexx.xmpp.newchat";
	public static final String NEW_ROOSTER = "de.meisterfuu.animexx.xmpp.newrooster";
	
	public static final String BUNDLE_FROM = "b_from";
	public static final String BUNDLE_DIRECTION = "b_direc";
	public static final String BUNDLE_TO = "b_to";
	public static final String BUNDLE_TIME = "b_time";
	public static final String BUNDLE_MESSAGE_BODY = "b_body";

	public static final String BUNDLE_DIRECTION_OUT = "OUT";
	public static final String BUNDLE_DIRECTION_IN = "IN";
		
	@Override
	public void onCreate() {
		super.onCreate();

		Notification notification = getNotification("");		
		this.startForeground(42, notification);
	}
	
	private Notification getNotification(String pTitle){
		final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);		
		Notification notification = builder
			.setOngoing(true)
			.setTicker("Animexxenger")
			.setContentTitle("Animexxenger")
			.setContentText("Online")
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentIntent(XMPPRoosterFragment.getPendingIntent(this))
			.build();
		 return notification;
	}


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if(ConnectionManager.getInstance() == null){
			ConnectionManager mReconManager = new ConnectionManager(XMPPService.this);
			mReconManager.start();
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
		if(ConnectionManager.getInstance() != null) ConnectionManager.getInstance().stop();
		stopForeground(true);
	}
	
	

}
