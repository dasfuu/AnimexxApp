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
import de.meisterfuu.animexx.activitys.profiles.ProfileActivity;
import de.meisterfuu.animexx.activitys.rpg.RPGListFragment;
import de.meisterfuu.animexx.api.Self;

/**
 * Created by Furuha on 25.12.2014.
 */
public class GBNotificationManager extends BaseNotificationManager<GBNotification> {

    public GBNotificationManager(Context pContext) {
        super(pContext);
    }

    @Override
    public String getNotificationTag() {
        return "GB_NOTIFICATION";
    }

    @Override
    public Type getType() {
        return new TypeToken<ArrayList<GBNotification>>() {}.getType();
    }

    @Override
    public String getInboxTitle(int count, Context pContext) {
        return count + " neue Gästebuch Einträge";
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
    public PendingIntent getInboxIntent(GBNotification lastNotification, Context pContext) {
        return ProfileActivity.getPendingIntent(pContext, Self.getInstance(pContext).getUserID());
    }

    @Override
    public String getInboxTicker(GBNotification lastNotification, Context pContext) {
        return lastNotification.getTicker();
    }

    @Override
    public int getInboxIcon(GBNotification lastNotification, Context pContext) {
        return R.drawable.ic_gb;
    }

    @Override
    public Bitmap getInboxPicture(GBNotification lastNotification, Context pContext) {
        return null;
    }

    public static class NotificationDismissedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context pContext, Intent pIntent) {
            GBNotificationManager manager = new GBNotificationManager(pContext);
            manager.clearNotifications();
        }
    }
}
