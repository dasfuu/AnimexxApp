package de.meisterfuu.animexx;

import java.util.Random;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

/**
 * Helper class for showing and canceling debug notifications.
 * <p>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
public class DebugNotification {
	/**
	 * The unique identifier for this type of notification.
	 */
	private static final String NOTIFICATION_TAG = "Debug";


	public static void notify(final Context context,
	                          final String pString, final int id) {
		notify(context, pString, id, false);
	}

	public static void notify(final Context context,
			final String pString, final int id, boolean force) {

		if(!force) {
			if (!Debug.SHOW_DEBUG_NOTIFICATION) {
				return;
			}
		}

		final Resources res = context.getResources();

		// This image is used as the notification's large icon (thumbnail).
		// TODO: Remove this if your notification has no relevant thumbnail.
		final Bitmap picture = BitmapFactory.decodeResource(res,
				R.drawable.test1);

		final String ticker = pString;
		final String title = pString;
		final String text = pString;

		final NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context)

				// Set required fields, including the small icon, the
				// notification title, and text.
				.setSmallIcon(R.drawable.ic_stat_debug).setContentTitle(title)
				.setContentText(text)

				// All fields below this line are optional.

				// Use a default priority (recognized on devices running Android
				// 4.1 or later)
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)

				// Provide a large icon, shown with the notification in the
				// notification drawer on devices running Android 3.0 or later.
				.setLargeIcon(picture);

				// Set ticker text (preview) information for this notification.
//				.setTicker(ticker);

				// If this notification relates to a past or upcoming event, you
				// should set the relevant time information using the setWhen
				// method below. If this call is omitted, the notification's
				// timestamp will by set to the time at which it was shown.
				// TODO: Call setWhen if this notification relates to a past or
				// upcoming event. The sole argument to this method should be
				// the notification timestamp in milliseconds.
				// .setWhen(...)


		if(force){
			String sound_uri = PreferenceManager.getDefaultSharedPreferences(context).getString("notifications_new_message_ringtone", null);

			if(sound_uri != null){
				builder.setSound(Uri.parse(sound_uri));
			} else {
				builder.setDefaults(Notification.DEFAULT_SOUND);
			}

			builder.setVibrate(new long[]{0,250,100,250});

			builder.setLights(R.color.bg_red, 1000, 600);
		}
		notify(context, builder.build(), id);
	}

	@TargetApi(Build.VERSION_CODES.ECLAIR)
	private static void notify(final Context context,
			final Notification notification, int id) {
		final NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Random r = new Random();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			nm.notify(NOTIFICATION_TAG, id, notification);
		} else {
			nm.notify(id, notification);
		}
	}

	/**
	 * Cancels any notifications of this type previously shown using
	 * {@link #notify(Context, String, int)}.
	 */
	@TargetApi(Build.VERSION_CODES.ECLAIR)
	public static void cancel(final Context context, int id) {
		final NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			nm.cancel(NOTIFICATION_TAG, id);
		} else {
			nm.cancel(id);
		}
	}
}