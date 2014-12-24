package de.meisterfuu.animexx.notification;

import android.app.Notification;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.SpannableStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Furuha on 19.12.2014.
 */
public class BaseNotificationManager <X extends BaseNotification> {

    public List<X> getNotifications(){
        return null;
    }

    public boolean addNotification(){
        return true;
    }

    public boolean removeNotification(int id){
        return true;
    }

    public boolean clearNotifications(){
        return true;
    }

    public void show(Context pContext){
        List<X> list = getNotifications();

        if (list.size() == 1) {
                createNotification(list.get(0), pContext);
        } else {
            ArrayList<SpannableStringBuilder> lines = new ArrayList<SpannableStringBuilder>();
            for (int i = list.size() - 1; i >= 0; i--) {
                lines.add(list.get(i).getMultiTextLine());
            }
            //Create inbox notification
        }

    }

    private void createNotification(X notification, Context pContext) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(pContext);


        // Set required fields, including the small icon, the
        // notification title, and text.
        builder.setSmallIcon(notification.getIcon());
        builder.setContentTitle(notification.getTitle());
        builder.setContentText(notification.getText());

        // Use a default priority (recognized on devices running Android
        // 4.1 or later)
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Set ticker text (preview) information for this notification.
        builder.setTicker(notification.getTicker());

        // Show a number. This is useful when stacking notifications of
        // a single type.
        builder.setNumber(1);

        // Set the pending intent to be initiated when the user touches
        // the notification.
        builder.setContentIntent(notification.getIntent());

        // Automatically dismiss the notification when it is touched.
        builder.setAutoCancel(true);

        //Tint color
        builder.setColor(notification.getColor());

        // Provide a large icon, shown with the notification in the
        // notification drawer on devices running Android 3.0 or later.
        if (notification.getPicture() != null) builder.setLargeIcon(notification.getPicture());

        String sound_uri = notification.getSoundUri();
        Boolean vibrate = notification.vibrate();

        if (sound_uri != null) {
            builder.setSound(Uri.parse(sound_uri));
        } else {
            builder.setDefaults(Notification.DEFAULT_SOUND);
        }
        if (vibrate) {
            builder.setVibrate(new long[]{0, 250, 100, 250});
        }

        builder.setLights(notification.getLightColor(), 1000, 600);
    }

}
