package de.meisterfuu.animexx.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.SpannableStringBuilder;

/**
 * Created by Furuha on 23.12.2014.
 */
public abstract class BaseNotification {

    public abstract long getCollapseID();
    public abstract String getTitle();
    public abstract String getText();
    public abstract String getTicker();
    public abstract int getIcon();
    public abstract Bitmap getPicture(Context pContex);

    public abstract PendingIntent getIntent(Context pContex);
    public abstract boolean vibrate(Context pContext);
    public abstract String getSoundUri(Context pContext);
    public abstract int getLightColor(Context pContext);
    public abstract int getColor();

    public abstract SpannableStringBuilder getMultiTextLine();
    public abstract SpannableStringBuilder getCollapsedMultiTextLine();

}
