package de.meisterfuu.animexx.notification;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.ens.ENSFolderFragment;

/**
 * Created by Furuha on 25.12.2014.
 */
public class ENSNotificationManager extends BaseNotificationManager<ENSNotification> {

    public ENSNotificationManager(Context pContext) {
        super(pContext);
    }

    @Override
    public String getNotificationTag() {
        return "ENS_NOTIFICATION";
    }

    @Override
    public Type getType() {
        return new TypeToken<ArrayList<ENSNotification>>() {}.getType();
    }

    @Override
    public PendingIntent getDismissIntent(Context pContext) {
        Intent intent = new Intent(pContext, NotificationDismissedReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(pContext.getApplicationContext(), 0, intent, 0);
        return pendingIntent;
    }

    @Override
    public String getInboxTitle(int count, Context pContext) {
        return count + " neue ENS";
    }

    @Override
    public String getInboxSummary(int count, Context pContext) {
        return "+ "+count+" weitere";
    }

    @Override
    public PendingIntent getInboxIntent(ENSNotification lastNotification, Context pContext) {
        return ENSFolderFragment.getPendingIntent(pContext);
    }

    @Override
    public String getInboxTicker(ENSNotification lastNotification, Context pContext) {
        return lastNotification.getTicker();
    }

    @Override
    public int getInboxIcon(ENSNotification lastNotification, Context pContext) {
        return R.drawable.ic_stat_ens;
    }

    @Override
    public Bitmap getInboxPicture(ENSNotification lastNotification, Context pContext) {
        return null;
    }

    public static class NotificationDismissedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context pContext, Intent pIntent) {
            ENSNotificationManager manager = new ENSNotificationManager(pContext);
            manager.clearNotifications();
        }
    }

}
