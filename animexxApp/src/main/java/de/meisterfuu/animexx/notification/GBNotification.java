package de.meisterfuu.animexx.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.profiles.ProfileActivity;
import de.meisterfuu.animexx.api.Self;
import de.meisterfuu.animexx.utils.imageloader.BitmapLoaderCustom;
import de.meisterfuu.animexx.xmpp.XMPPRoosterFragment;

/**
 * Created by Furuha on 25.12.2014.
 */
public class GBNotification extends BaseNotification {

    private String pTitle;
    private long pId;
    private String pUserName;
    private long pUserId;

    public GBNotification() {
        this.pTitle = "Neuer Gästebucheintrag";
        this.pUserName = "Unbekannt";
        this.pUserId = 0;
    }

    public GBNotification(String pTitle, long pId, String pUserName, long pUserId) {
        this.pTitle = pTitle;
        this.pId = pId;
        this.pUserName = pUserName;
        this.pUserId = pUserId;
    }

    @Override
    public long getCollapseID() {
        return pUserId;
    }

    @Override
    public String getTitle() {
            return pUserName;
    }

    @Override
    public String getText() {
        return "Neuer Gästebucheintrag von "+pTitle;
    }

    @Override
    public String getTicker() {
        return pTitle;
    }

    @Override
    public int getIcon() {
        return R.drawable.ic_gb;
    }

    @Override
    public Bitmap getPicture(Context pContext) {
        return BitmapLoaderCustom.getUserBitmap(pUserId+"", pContext);
    }

    @Override
    public PendingIntent getIntent(Context pContext) {
        return ProfileActivity.getPendingIntent(pContext, Self.getInstance(pContext).getUserID());
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
