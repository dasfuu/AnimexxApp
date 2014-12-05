package de.meisterfuu.animexx.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.ens.ENSFolderFragment;
import de.meisterfuu.animexx.activitys.ens.SingleENSActivity;
import de.meisterfuu.animexx.api.broker.ENSBroker;
import de.meisterfuu.animexx.objects.ens.ENSNotifyObject;
import de.meisterfuu.animexx.utils.imageloader.BitmapLoaderCustom;

/**
 * Helper class for showing and canceling ens
 * notifications.
 * <p/>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper class to create notifications in a backward-compatible way.
 */
public class ENSCollapseNotification {

    /**
     * The unique identifier for this type of notification.
     */
    private static final String NOTIFICATION_TAG = "ENS";


    public static void notify(final Context pContext, final String pTitle, final String pUserName, final String pUserId, final String pId, final String pFrom) {

        ENSBroker api = new ENSBroker(pContext);

        ENSNotifyObject obj = new ENSNotifyObject();
        obj.setFromID(Long.valueOf(pUserId));
        obj.setFromUsername(pUserName);
        obj.setId(Long.valueOf(pId));
        obj.setSubject(pTitle);
        obj.setTime(System.currentTimeMillis());

        api.addNotifications(obj);

        List<ENSNotifyObject> list = api.getNotifications();
        if (list.size() == 1) {
            notifyFirst(pContext, pTitle, pUserName, pUserId, pId, pFrom);
        } else {
            ArrayList<SpannableStringBuilder> lines = new ArrayList<SpannableStringBuilder>();
//			lines.add(obj.getFromUsername() + ": " + obj.getSubject());
            for (int i = list.size() - 1; i >= 0; i--) {

                final SpannableStringBuilder exampleItem = new SpannableStringBuilder();
                exampleItem.append(list.get(i).getFromUsername());
                exampleItem.setSpan(new ForegroundColorSpan(Color.WHITE), 0, exampleItem.length(), 0);
                exampleItem.append("   " + list.get(i).getSubject());

                lines.add(exampleItem);
            }
            notifyMultiple(pContext, pTitle, lines, lines.size());
        }

    }

    private static void notifyFirst(final Context pContext, final String pTitle, final String pUserName, final String pUserId, final String pId, final String pFrom) {


        // This image is used as the notification's large icon (thumbnail).
        final Bitmap picture = BitmapLoaderCustom.getUserBitmap(pUserId, pContext);

        final String ticker = "Neue ENS";
        final String title = pTitle;
        final String text = pUserName;

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(pContext)

                // Set required fields, including the small icon, the
                // notification title, and text.
                .setSmallIcon(R.drawable.ic_stat_ens).setContentTitle(title).setContentText(text)

                        // All fields below this line are optional.

                        // Use a default priority (recognized on devices running Android
                        // 4.1 or later)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

                        // Set ticker text (preview) information for this notification.
                .setTicker(ticker)

                        // Show a number. This is useful when stacking notifications of
                        // a single type.
                .setNumber(1)

                .setWhen(Long.valueOf(pFrom))

                        // Set the pending intent to be initiated when the user touches
                        // the notification.
                .setContentIntent(SingleENSActivity.getPendingIntent(pContext, Long.valueOf(pId)))

                        // Example additional actions for this notification. These will
                        // only show on devices running Android 4.1 or later, so you
                        // should ensure that the activity in this notification's
                        // content intent provides access to the same actions in
                        // another way.
//				.addAction(
//						R.drawable.ic_action_stat_share,
//						res.getString(R.string.action_share),
//						PendingIntent.getActivity(pContext, 0,
//								Intent.createChooser(new Intent(Intent.ACTION_SEND).setType("text/plain").putExtra(Intent.EXTRA_TEXT, "Dummy text"), "Dummy title"),
//								PendingIntent.FLAG_UPDATE_CURRENT))

//				.addAction(R.drawable.ic_action_stat_reply, res.getString(R.string.action_reply), null)

                        // Automatically dismiss the notification when it is touched.
                .setAutoCancel(true);

        // Provide a large icon, shown with the notification in the
        // notification drawer on devices running Android 3.0 or later.
        if (picture != null) builder.setLargeIcon(picture);

        Boolean loud = PreferenceManager.getDefaultSharedPreferences(pContext).getBoolean("notifications_new_message", true);
        String sound_uri = PreferenceManager.getDefaultSharedPreferences(pContext).getString("notifications_new_message_ringtone", null);
        Boolean vibrate = PreferenceManager.getDefaultSharedPreferences(pContext).getBoolean("notifications_new_message_vibrate", true);

        if (loud) {
            if (sound_uri != null) {
                builder.setSound(Uri.parse(sound_uri));
            } else {
                builder.setDefaults(Notification.DEFAULT_SOUND);
            }
            if (vibrate) {
                builder.setVibrate(new long[]{0, 250, 100, 250});
            }
            builder.setLights(R.color.animexx_blue, 1000, 600);

            notify(pContext, builder.build());
        }


    }

    private static void notifyMultiple(final Context pContext, final String pTicker, ArrayList<SpannableStringBuilder> lines, final int count) {

        final String ticker = pTicker;

        NotificationCompat.InboxStyle inbox = new NotificationCompat.InboxStyle();

        int i;
        for (i = 0; (i < lines.size()) && (i < 5); i++) {
            inbox.addLine(lines.get(i));
        }

        inbox.setBigContentTitle(count + " neue ENS");
        if (lines.size() > 5) {
            inbox.setSummaryText("+" + (lines.size() - 5) + " weitere");
        } else {
            inbox.setSummaryText("");
        }

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(pContext)
                .setContentTitle(count + " neue ENS")
                .setContentText(lines.get(0))
                .setTicker(ticker)
                .setSmallIcon(R.drawable.ic_stat_ens)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(ENSFolderFragment.getPendingIntent(pContext))
                .setAutoCancel(true)
                .setNumber(count)
                .setStyle(inbox);

        Intent intent = new Intent(pContext, ENSNotificationDismissedReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(pContext.getApplicationContext(), 0, intent, 0);
        builder.setDeleteIntent(pendingIntent);

        Boolean loud = PreferenceManager.getDefaultSharedPreferences(pContext).getBoolean("notifications_new_message", true);
        String sound_uri = PreferenceManager.getDefaultSharedPreferences(pContext).getString("notifications_new_message_ringtone", null);
        Boolean vibrate = PreferenceManager.getDefaultSharedPreferences(pContext).getBoolean("notifications_new_message_vibrate", true);

        if (loud) {
            if (sound_uri != null) {
                builder.setSound(Uri.parse(sound_uri));
            } else {
                builder.setDefaults(Notification.DEFAULT_SOUND);
            }
            if (vibrate) {
                builder.setVibrate(new long[]{0, 250, 100, 250});
            }
            builder.setLights(R.color.animexx_blue, 1000, 600);

            notify(pContext, builder.build());
        }


    }


    @TargetApi(Build.VERSION_CODES.ECLAIR)
    private static void notify(final Context context, final Notification notification) {
        System.out.println("ENS NOTIFY!");
        final NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(NOTIFICATION_TAG, 0, notification);
    }


    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void cancel(final Context context) {
        final NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nm.cancel(NOTIFICATION_TAG, 0);
    }


    public static class ENSNotificationDismissedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context pContext, Intent pIntent) {
            ENSBroker api = new ENSBroker(pContext);
            api.clearNotification();
        }
    }
}