package de.meisterfuu.animexx.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.xmpp.XMPPRoosterFragment;

/**
 * Created by Furuha on 25.12.2014.
 */
public class XMPPNotificationManager extends BaseNotificationManager {

    public XMPPNotificationManager(Context pContext) {
        super(pContext);
    }

    @Override
    public String getNotificationTag() {
        return "XMPP_NOTIFICATION";
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
    public PendingIntent getInboxIntent(BaseNotification lastNotification, Context pContext) {
        return XMPPRoosterFragment.getPendingIntent(pContext);
    }

    @Override
    public String getInboxTicker(BaseNotification lastNotification, Context pContext) {
        return lastNotification.getTicker();
    }

    @Override
    public int getInboxIcon(BaseNotification lastNotification, Context pContext) {
        return R.drawable.ic_stat_xmpp;
    }

    @Override
    public Bitmap getInboxPicture(BaseNotification lastNotification, Context pContext) {
        return null;
    }

}
