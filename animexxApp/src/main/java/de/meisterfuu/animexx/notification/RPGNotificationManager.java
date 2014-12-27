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
import de.meisterfuu.animexx.activitys.rpg.RPGListFragment;

/**
 * Created by Furuha on 25.12.2014.
 */
public class RPGNotificationManager extends BaseNotificationManager<RPGNotification> {

    public RPGNotificationManager(Context pContext) {
        super(pContext);
    }

    @Override
    public String getNotificationTag() {
        return "RPG_NOTIFICATION_NEW_POST";
    }

    @Override
    public Type getType() {
        return new TypeToken<ArrayList<RPGNotification>>() {}.getType();
    }

    @Override
    public String getInboxTitle(int count, Context pContext) {
        return count + " neue RPG Posts";
    }

    @Override
    public PendingIntent getDismissIntent(Context pContext) {
        Intent intent = new Intent(pContext, NotificationDismissedReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(pContext.getApplicationContext(), 0, intent, 0);
        return pendingIntent;
    }

    @Override
    public String getInboxSummary(int count, Context pContext) {
        return "+ "+count+" weitere";
    }

    @Override
    public PendingIntent getInboxIntent(RPGNotification lastNotification, Context pContext) {
        return RPGListFragment.getPendingIntent(pContext);
    }

    @Override
    public String getInboxTicker(RPGNotification lastNotification, Context pContext) {
        return lastNotification.getTicker();
    }

    @Override
    public int getInboxIcon(RPGNotification lastNotification, Context pContext) {
        return R.drawable.ic_stat_rpgpost;
    }

    @Override
    public Bitmap getInboxPicture(RPGNotification lastNotification, Context pContext) {
        return null;
    }

    public static class NotificationDismissedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context pContext, Intent pIntent) {
            RPGNotificationManager manager = new RPGNotificationManager(pContext);
            manager.clearNotifications();
        }
    }
}
