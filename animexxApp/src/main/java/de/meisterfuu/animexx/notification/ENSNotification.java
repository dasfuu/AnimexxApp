package de.meisterfuu.animexx.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.ens.SingleENSActivity;
import de.meisterfuu.animexx.activitys.rpg.RPGListFragment;
import de.meisterfuu.animexx.utils.imageloader.BitmapLoaderCustom;

/**
 * Created by Furuha on 25.12.2014.
 */
public class ENSNotification extends BaseNotification {

    private String pTitle;
    private long pId;
    private String pUserName;
    private long pUserId;

    public ENSNotification() {
        this.pTitle = "Neue ENS";
        this.pId = 0;
        this.pUserName = "Unbekannt";
        this.pUserId = 0;
    }

    public ENSNotification(String pTitle, long pId, String pUserName, long pUserId) {
        this.pTitle = pTitle;
        this.pId = pId;
        this.pUserName = pUserName;
        this.pUserId = pUserId;
    }

    @Override
    public long getCollapseID() {
        return pId;
    }

    @Override
    public String getTitle() {
        return pUserName;
    }

    @Override
    public String getText() {
        return pTitle;
    }

    @Override
    public String getTicker() {
        return "Neuer ENS von " + pUserName;
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_stat_ens;
    }

    @Override
    public Bitmap getPicture(Context pContext) {
        return BitmapLoaderCustom.getUserBitmap(pUserId + "", pContext);
    }

    @Override
    public PendingIntent getIntent(Context pContext) {
        return SingleENSActivity.getPendingIntent(pContext, pId);
    }

    @Override
    public boolean vibrate(Context pContext) {
        return PreferenceManager.getDefaultSharedPreferences(pContext).getBoolean("notifications_new_message_vibrate", true);
    }

    @Override
    public String getSoundUri(Context pContext) {
        return PreferenceManager.getDefaultSharedPreferences(pContext).getString("notifications_new_message_ringtone", null);
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
        spann.append(pUserName);
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
