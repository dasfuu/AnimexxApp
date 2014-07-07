package de.meisterfuu.animexx.services;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import de.meisterfuu.animexx.Debug;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.main.MainActivity;
import de.meisterfuu.animexx.notification.ENSCollapseNotification;
import de.meisterfuu.animexx.notification.ENSNotification;
import de.meisterfuu.animexx.notification.RPGPostNotification;
import de.meisterfuu.animexx.receiver.GcmBroadcastReceiver;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GcmIntentService extends IntentService {

	public static final int NOTIFICATION_ID = 1;
	private static final String TAG = "GcmIntentService";
	private NotificationManager mNotificationManager;
	NotificationCompat.Builder builder;


	public GcmIntentService() {
		super("GcmIntentService");
	}


	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {  // has effect of unparcelling Bundle

			/*
			 * Filter messages based on message type. Since it is likely that GCM
			 * will be extended in the future with new message types, just ignore
			 * any message types you're not interested in, or that you don't
			 * recognize.
			 */
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
				
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
				sendNotification("Deleted messages on server: " + extras.toString());

			// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				if(extras.getString("type").equalsIgnoreCase("XXEventENS")){
						ENSCollapseNotification.notify(this, extras.getString("title"), extras.getString("from_username"), extras.getString("from_id"),  extras.getString("id"), extras.getString("from"));
				} else if(extras.getString("type").equalsIgnoreCase("XXEventRPGPosting")) {
					RPGPostNotification.notify(this, extras.getString("title"), extras.getString("from_username"), extras.getString("from_id"),  extras.getString("id"), extras.getString("from"), 1);
				} else {
					// Post notification of unknown received message.
					if(Debug.SHOW_DEBUG_NOTIFICATION)sendNotification("Received: " + extras.toString());
					if(!Debug.SILENT_NETWORK)Log.i(TAG, "Received: " + extras.toString());
				}


			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);
	}


	// Put the message into a notification and post it.
	// This is just one simple example of what you might choose to do with
	// a GCM message.
	private void sendNotification(String msg) {

		mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.ic_launcher).setContentTitle("GCM Notification")
				.setStyle(new NotificationCompat.BigTextStyle().bigText(msg)).setContentText(msg);

		mBuilder.setContentIntent(contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}
}
