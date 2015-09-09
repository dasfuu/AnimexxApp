package de.meisterfuu.animexx.services.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import de.meisterfuu.animexx.Debug;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.main.MainActivity;
import de.meisterfuu.animexx.api.Self;
import de.meisterfuu.animexx.api.broker.GCMBroker;
import de.meisterfuu.animexx.notification.ENSNotification;
import de.meisterfuu.animexx.notification.ENSNotificationManager;
import de.meisterfuu.animexx.notification.GBNotification;
import de.meisterfuu.animexx.notification.GBNotificationManager;
import de.meisterfuu.animexx.notification.RPGNotification;
import de.meisterfuu.animexx.notification.RPGNotificationManager;

public class AnimexxGcmListenerService extends GcmListenerService {

//    public static ConcurrentHashMap<Integer, Long> doubleMap = new ConcurrentHashMap<>();
//    public static ConcurrentHashMap<Integer, Long> getDoubleMap(){
//        return doubleMap;
//    }


    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "GcmIntentService";
    private NotificationManager mNotificationManager;

    public static final String NEW_POST = "de.meisterfuu.animexx.new";


    @Override
    public void onMessageReceived(String from, Bundle data) {
//        super.onMessageReceived(from, data);
        Log.e("GCM", "GCM GCM GCM");

//        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
//        String messageType = gcm.getMessageType(intent);

        boolean notify = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notifications_new_message", true);
        boolean notifyRPG = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notifications_new_rpg_message", true);
        boolean notifyENS = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("notifications_new_ens_message", true);
        if (!data.isEmpty()) {  // has effect of unparcelling Bundle

            GCMBroker api = new GCMBroker(this);
            if(!api.compareRegistrationIdHash(data.getString("toReg"))){
                return;
            }
//                synchronized (doubleMap){
//                    String url = data.getString("link");
//                    if(url != null && !url.isEmpty()){
//                        int hash = url.hashCode();
//                        int hash2 = (""+url).hashCode();
//                        Log.d("GCM", "Hash: "+hash+" "+hash2);
//                        for(Map.Entry<Integer, Long> entry: GcmIntentService.getDoubleMap().entrySet()){
//                            if( System.currentTimeMillis() - entry.getValue() < 30000){
//                                GcmIntentService.getDoubleMap().remove(entry.getKey());
//                            }
//                        }
//                        if(GcmIntentService.getDoubleMap().containsKey(hash)){
//                            return;
//                        }
//                        GcmIntentService.getDoubleMap().put(hash, System.currentTimeMillis());
//                    }
//                }

            Log.d("GCM", "Type: " + data.getString("type"));
            if (data.getString("type").equalsIgnoreCase("XXEventENS")) {
                if(notifyENS){
                    ENSNotificationManager manager = new ENSNotificationManager(this);
                    manager.addNotification(new ENSNotification(data.getString("title"), Long.parseLong(data.getString("id")), data.getString("from_username"),  Long.parseLong(data.getString("from_id"))));
                    manager.show();
                }
            } else if (data.getString("type").equalsIgnoreCase("XXEventRPGPosting")) {
                if(notifyRPG) {
                    if (Long.parseLong(data.getString("from_id")) != Self.getInstance(this).getUserID() && RPGNotificationManager.currentRPG != Long.parseLong(data.getString("id"))) {
                        RPGNotificationManager manager = new RPGNotificationManager(this);
                        manager.addNotification(new RPGNotification(data.getString("title"), Long.parseLong(data.getString("id")), data.getString("from_username"), Long.parseLong(data.getString("from_id"))));
                        manager.show();
                    }
                    Intent i = new Intent(AnimexxGcmListenerService.NEW_POST);
                    i.setPackage(this.getPackageName());
                    this.sendBroadcast(i);
                }
            } else if (data.getString("type").equalsIgnoreCase("XXEventGaestebuch")) {
                if(notify) {
                    GBNotificationManager manager = new GBNotificationManager(this);
                    manager.addNotification(new GBNotification(data.getString("title"), Long.parseLong(data.getString("id")), data.getString("from_username"), Long.parseLong(data.getString("from_id"))));
                    manager.show();
                }
            } else {
                // Post notification of unknown received message.
                if (Debug.SHOW_DEBUG_NOTIFICATION) sendNotification("Received: " + data.toString());
                if (!Debug.SILENT_NETWORK) Log.i(TAG, "Received: " + data.toString());
            }


        }
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

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String msgId) {
        super.onMessageSent(msgId);
    }

    @Override
    public void onSendError(String msgId, String error) {
        super.onSendError(msgId, error);
    }


}
