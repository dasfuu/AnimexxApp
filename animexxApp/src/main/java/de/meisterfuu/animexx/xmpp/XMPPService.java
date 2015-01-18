package de.meisterfuu.animexx.xmpp;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.api.EventBus;
import de.meisterfuu.animexx.api.xmpp.StatsuChangeEvent;

import static de.meisterfuu.animexx.xmpp.SmackConnection.ConnectionState;

public class XMPPService extends Service {

    public static final String TAG = "XMPP";

    private ConnectionState oldEvent = ConnectionState.DISCONNECTED;

    @Override
    public void onCreate() {
        super.onCreate();
        Notification notification = getNotificationOffline();
        this.startForeground(42, notification);
        EventBus.getBus().getOtto().register(this);
    }

    @Subscribe
    public void onStatusChange(StatsuChangeEvent event){
        if(event.status == oldEvent){
            return;
        } 
        
        oldEvent = event.status;
        
        if(!event.status.equals(ConnectionState.DISCONNECTED) && !event.status.equals(ConnectionState.ERROR)){
            this.startForeground(42, getNotificationOnline());
        } else {
            this.startForeground(42, getNotificationOffline());
        }

        if(event.status.equals(ConnectionState.ERROR)){
            ConnectionManager.getInstance().tryConnect();
        }
    }

    @Produce
    public StatsuChangeEvent produceStatus(){
        return new StatsuChangeEvent(oldEvent);
    }

    private Notification getNotificationOnline() {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Notification notification = builder
                .setOngoing(true)
                .setTicker("Animexxenger")
                .setContentTitle("Animexxenger")
                .setContentText("Online")
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_stat_chat_online)
                .setColor(this.getResources().getColor(R.color.animexx_blue))
                .setContentIntent(XMPPRoosterFragment.getPendingIntent(this))
                .build();
        return notification;
    }

    private Notification getNotificationOffline() {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Notification notification = builder
                .setOngoing(true)
                .setTicker("Animexxenger")
                .setContentTitle("Animexxenger")
                .setContentText("Offline")
                .setPriority(NotificationCompat.PRIORITY_MIN)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_stat_chat_offline)
                .setColor(this.getResources().getColor(R.color.animexx_blue))
                .setContentIntent(XMPPRoosterFragment.getPendingIntent(this))
                .build();
        return notification;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (ConnectionManager.getInstance() == null) {
            ConnectionManager mReconManager = new ConnectionManager(XMPPService.this);
            mReconManager.start();
        }

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (ConnectionManager.getInstance() != null) ConnectionManager.getInstance().stop();
        EventBus.getBus().getOtto().unregister(this);
        stopForeground(true);
    }


}
