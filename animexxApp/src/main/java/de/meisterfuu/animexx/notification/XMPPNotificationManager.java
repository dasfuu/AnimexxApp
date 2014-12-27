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
import de.meisterfuu.animexx.xmpp.XMPPRoosterFragment;

/**
 * Created by Furuha on 25.12.2014.
 */
public class XMPPNotificationManager extends BaseNotificationManager<XMPPNotification> {

    public XMPPNotificationManager(Context pContext) {
        super(pContext);
    }

    @Override
    public String getNotificationTag() {
        return "XMPP_NOTIFICATION";
    }

    @Override
    public Type getType() {
        return new TypeToken<ArrayList<XMPPNotification>>() {}.getType();
    }

    @Override
    public PendingIntent getDismissIntent(Context pContext) {
        Intent intent = new Intent(pContext, NotificationDismissedReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(pContext.getApplicationContext(), 0, intent, 0);
        return pendingIntent;
    }

    @Override
    public String getInboxTitle(int count, Context pContext) {
        return count + " neue Nachrichten";
    }

    @Override
    public String getInboxSummary(int count, Context pContext) {
        return "+ "+count+" weitere";
    }

    @Override
    public PendingIntent getInboxIntent(XMPPNotification lastNotification, Context pContext) {
        return XMPPRoosterFragment.getPendingIntent(pContext);
    }

    @Override
    public String getInboxTicker(XMPPNotification lastNotification, Context pContext) {
        return lastNotification.getTicker();
    }

    @Override
    public int getInboxIcon(XMPPNotification lastNotification, Context pContext) {
        return R.drawable.ic_stat_xmpp;
    }

    @Override
    public Bitmap getInboxPicture(XMPPNotification lastNotification, Context pContext) {
        return null;
    }

    public static class NotificationDismissedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context pContext, Intent pIntent) {
            XMPPNotificationManager manager = new XMPPNotificationManager(pContext);
            manager.clearNotifications();
        }
    }

}
