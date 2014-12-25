package de.meisterfuu.animexx.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.rpg.RPGListFragment;

/**
 * Created by Furuha on 25.12.2014.
 */
public class RPGNotificationManager extends BaseNotificationManager {

    public RPGNotificationManager(Context pContext) {
        super(pContext);
    }

    @Override
    public String getNotificationTag() {
        return "RPG_NOTIFICATION_NEW_POST";
    }

    @Override
    public String getInboxTitle(int count, Context pContext) {
        return count + " neue RPG Posts";
    }

    @Override
    public String getInboxSummary(int count, Context pContext) {
        return "+ "+count+" weitere";
    }

    @Override
    public PendingIntent getInboxIntent(BaseNotification lastNotification, Context pContext) {
        return RPGListFragment.getPendingIntent(pContext);
    }

    @Override
    public String getInboxTicker(BaseNotification lastNotification, Context pContext) {
        return lastNotification.getTicker();
    }

    @Override
    public int getInboxIcon(BaseNotification lastNotification, Context pContext) {
        return R.drawable.ic_stat_rpgpost;
    }

    @Override
    public Bitmap getInboxPicture(BaseNotification lastNotification, Context pContext) {
        return null;
    }

}
