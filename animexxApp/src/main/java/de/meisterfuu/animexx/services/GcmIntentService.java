package de.meisterfuu.animexx.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.HashMap;
import java.util.Map;

import de.meisterfuu.animexx.Debug;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.main.MainActivity;
import de.meisterfuu.animexx.api.Self;
import de.meisterfuu.animexx.notification.ENSNotification;
import de.meisterfuu.animexx.notification.ENSNotificationManager;
import de.meisterfuu.animexx.notification.GBNotification;
import de.meisterfuu.animexx.notification.GBNotificationManager;
import de.meisterfuu.animexx.notification.RPGNotification;
import de.meisterfuu.animexx.notification.RPGNotificationManager;
import de.meisterfuu.animexx.receiver.GcmBroadcastReceiver;

public class GcmIntentService extends IntentService {

    public static HashMap<Integer, Long> doubleMap = new HashMap<>();

    public static HashMap<Integer, Long> getDoubleMap(){
        return doubleMap;
    }


    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "GcmIntentService";
    private NotificationManager mNotificationManager;

    public static final String NEW_POST = "de.meisterfuu.animexx.new";

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

        boolean notify = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notifications_new_message", true);
        boolean notifyRPG = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notifications_new_rpg_message", true);
        boolean notifyENS = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notifications_new_ens_message", true);
        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle

			/*
             * Filter messages based on message type. Since it is likely that GCM
			 * will be extended in the future with new message types, just ignore
			 * any message types you're not interested in, or that you don't
			 * recognize.
			 */

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
//                sendNotification("Send error: " + extras.toString());

            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
//                sendNotification("Deleted messages on server: " + extras.toString());

                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                String url = extras.getString("link");
                if(url != null && !url.isEmpty()){
                    int hash = url.hashCode();
                    int hash2 = (""+url).hashCode();
                    Log.d("GCM", "Hash: "+hash+" "+hash2);
                    for(Map.Entry<Integer, Long> entry: GcmIntentService.getDoubleMap().entrySet()){
                        if( System.currentTimeMillis() - entry.getValue() < 30000){
                            GcmIntentService.getDoubleMap().remove(entry.getKey());
                        }
                    }
                    if(GcmIntentService.getDoubleMap().containsKey(hash)){
                        return;
                    }
                    GcmIntentService.getDoubleMap().put(hash, System.currentTimeMillis());
                }

                Log.d("GCM", "Type: "+extras.getString("type"));
                if (extras.getString("type").equalsIgnoreCase("XXEventENS")) {
                    if(notifyENS){
                        ENSNotificationManager manager = new ENSNotificationManager(this);
                        manager.addNotification(new ENSNotification(extras.getString("title"), Long.parseLong(extras.getString("id")), extras.getString("from_username"),  Long.parseLong(extras.getString("from_id"))));
                        manager.show();
                    }
                } else if (extras.getString("type").equalsIgnoreCase("XXEventRPGPosting")) {
                    if(notifyRPG) {
                        if (Long.parseLong(extras.getString("from_id")) != Self.getInstance(this).getUserID() && RPGNotificationManager.currentRPG != Long.parseLong(extras.getString("id"))) {
                            RPGNotificationManager manager = new RPGNotificationManager(this);
                            manager.addNotification(new RPGNotification(extras.getString("title"), Long.parseLong(extras.getString("id")), extras.getString("from_username"), Long.parseLong(extras.getString("from_id"))));
                            manager.show();
                        }
                        Intent i = new Intent(GcmIntentService.NEW_POST);
                        i.setPackage(this.getPackageName());
                        this.sendBroadcast(intent);
                    }
                } else if (extras.getString("type").equalsIgnoreCase("XXEventGaestebuch")) {
                    if(notify) {
                        GBNotificationManager manager = new GBNotificationManager(this);
                        manager.addNotification(new GBNotification(extras.getString("title"), Long.parseLong(extras.getString("id")), extras.getString("from_username"), Long.parseLong(extras.getString("from_id"))));
                        manager.show();
                    }
                } else {
                    // Post notification of unknown received message.
                    if (Debug.SHOW_DEBUG_NOTIFICATION) sendNotification("Received: " + extras.toString());
                    if (!Debug.SILENT_NETWORK) Log.i(TAG, "Received: " + extras.toString());
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
