package de.meisterfuu.animexx.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import java.util.List;

/**
 * Created by Furuha on 19.12.2014.
 */
public abstract class BaseNotificationManager <X extends BaseNotification> {

    public List<X> getNotifications(){
        return null;
    }

    public boolean addNotification(){
        return true;
    }

    public boolean removeNotification(int id){
        return true;
    }

    public boolean removeAllNotification(){
        return true;
    }

    public boolean clearNotifications(Context pContext){
        boolean removed = removeAllNotification();
        cancel(pContext);
        return removed;
    }

    public abstract String getNotificationTag();

    private void notify(final Context context, final Notification notification) {
        final NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(getNotificationTag(), 0, notification);
    }

    public void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(getNotificationTag(), 0);
    }

    public abstract String getInboxTitle(int count, Context pContext);
    public abstract String getInboxSummary(int count, Context pContext);
    public abstract PendingIntent getInboxIntent(X lastNotification, Context pContext);
    public abstract String getInboxTicker(X lastNotification, Context pContext);
    public abstract int getInboxIcon(X lastNotification, Context pContext);
    public abstract Bitmap getInboxPicture(X lastNotification, Context pContext);

    public void show(Context pContext){
        List<X> list = getNotifications();

        if (list.size() == 1) {
                createNotification(list.get(0), pContext);
        } else {

            //Create inbox notification
            NotificationCompat.InboxStyle inbox = new NotificationCompat.InboxStyle();

            for (int i = 0; (i < list.size()) && (i < 5); i++) {
                inbox.addLine(list.get(i).getMultiTextLine());
            }

            inbox.setBigContentTitle(getInboxTitle(list.size(), pContext));

            if (list.size() > 5) {
                inbox.setSummaryText(getInboxSummary(list.size() - 5, pContext));
            } else {
                inbox.setSummaryText("");
            }

            createInboxNotification(list.get(0), inbox, list.size(), pContext);
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
        Bitmap picture = notification.getPicture();
        if (picture != null) builder.setLargeIcon(picture);

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

        notify(pContext, builder.build());
    }

    private void createInboxNotification(X notification, NotificationCompat.InboxStyle inbox, int count, Context pContext) {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(pContext);


        // Set required fields, including the small icon, the
        // notification title, and text.
        builder.setSmallIcon(getInboxIcon(notification, pContext));
        builder.setContentTitle(notification.getTitle());
        builder.setContentText(notification.getText());

        // Use a default priority (recognized on devices running Android
        // 4.1 or later)
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // Set ticker text (preview) information for this notification.
        builder.setTicker(getInboxTicker(notification, pContext));

        // Show a number. This is useful when stacking notifications of
        // a single type.
        builder.setNumber(count);

        // Set the pending intent to be initiated when the user touches
        // the notification.
        builder.setContentIntent(getInboxIntent(notification, pContext));

        // Automatically dismiss the notification when it is touched.
        builder.setAutoCancel(true);

        //Tint color
        builder.setColor(notification.getColor());

        builder.setStyle(inbox);

        // Provide a large icon, shown with the notification in the
        // notification drawer on devices running Android 3.0 or later.
        Bitmap picture = getInboxPicture(notification, pContext);
        if (picture != null) builder.setLargeIcon(picture);

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

        notify(pContext, builder.build());
    }
}
