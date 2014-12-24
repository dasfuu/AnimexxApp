package de.meisterfuu.animexx.notification;

import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.text.Spannable;
import android.text.SpannableStringBuilder;

/**
 * Created by Furuha on 23.12.2014.
 */
public interface BaseNotification {

    public String getTitle();
    public String getText();
    public String getTicker();
    public String getTextLong();
    public int getIcon();
    public Bitmap getPicture();

    public PendingIntent getIntent();
    public boolean vibrate();
    public boolean sound();
    public String getSoundUri();
    public int getLightColor();
    public int getColor();

    public SpannableStringBuilder getMultiTextLine();


}
