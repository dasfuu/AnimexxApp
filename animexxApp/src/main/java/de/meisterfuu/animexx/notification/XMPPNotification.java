package de.meisterfuu.animexx.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.utils.imageloader.BitmapLoaderCustom;
import de.meisterfuu.animexx.xmpp.XMPPRoosterFragment;

/**
 * Created by Furuha on 25.12.2014.
 */
public class XMPPNotification extends BaseNotification {

    private String pTitle;
    private String pUserName;
    private long pUserId;

    public XMPPNotification() {
        this.pTitle = "Neue Nachricht";
        this.pUserName = "Unbekannt";
        this.pUserId = 0;
    }

    public XMPPNotification(String pMessage, String pFrom, long pUserId) {
        this.pTitle = pMessage;
        this.pUserName = pFrom;
        this.pUserId = pUserId;
    }

    @Override
    public long getCollapseID() {
        return pUserId;
    }

    @Override
    public String getTitle() {
        if (pUserName.contains("animexx")) {
            return pUserName.split("@")[0];
        } else {
            return pUserName;
        }
    }

    @Override
    public String getText() {
        return pTitle;
    }

    @Override
    public String getTicker() {
        return pTitle;
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_stat_xmpp;
    }

    @Override
    public Bitmap getPicture(Context pContext) {
        return BitmapLoaderCustom.getUserBitmap(pUserId+"", pContext);
    }

    @Override
    public PendingIntent getIntent(Context pContext) {
        return XMPPRoosterFragment.getPendingIntent(pContext);
    }

    @Override
    public boolean vibrate(Context pContext) {
        return PreferenceManager.getDefaultSharedPreferences(pContext).getBoolean("notifications_new_xmpp_message_vibrate", true);
    }

    @Override
    public String getSoundUri(Context pContext) {
        return PreferenceManager.getDefaultSharedPreferences(pContext).getString("notifications_new_xmpp_message_ringtone", null);
    }

    @Override
    public int getLightColor(Context pContext) {
        return R.color.animexx_blue;
    }

    @Override
    public int getColor() {
        return R.color.animexx_blue;
    }

    @Override
    public SpannableStringBuilder getMultiTextLine() {
        final SpannableStringBuilder spann = new SpannableStringBuilder();
        spann.append(getTitle());
        spann.setSpan(new StyleSpan(Typeface.BOLD), 0, spann.length(), 0);
        spann.append("   " + pTitle);
        return spann;
    }

    @Override
    public SpannableStringBuilder getCollapsedMultiTextLine() {
        final SpannableStringBuilder spann = new SpannableStringBuilder();
        spann.append("    ");
        spann.setSpan(new StyleSpan(Typeface.BOLD), 0, spann.length(), 0);
        spann.append("   " + pTitle);
        return spann;
    }


}
