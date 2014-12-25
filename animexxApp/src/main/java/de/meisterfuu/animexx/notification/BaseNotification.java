package de.meisterfuu.animexx.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.SpannableStringBuilder;

/**
 * Created by Furuha on 23.12.2014.
 */
public interface BaseNotification {

    public String getTitle();
    public String getText();
    public String getTicker();
    public int getIcon();
    public Bitmap getPicture(Context pContex);

    public PendingIntent getIntent(Context pContex);
    public boolean vibrate(Context pContext);
    public String getSoundUri(Context pContext);
    public int getLightColor(Context pContext);
    public int getColor();

    public SpannableStringBuilder getMultiTextLine();


}
