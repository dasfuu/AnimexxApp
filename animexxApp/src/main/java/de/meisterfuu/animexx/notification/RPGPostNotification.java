package de.meisterfuu.animexx.notification;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.R.drawable;
import de.meisterfuu.animexx.activitys.rpg.RPGListFragment;
import de.meisterfuu.animexx.data.Self;
import de.meisterfuu.animexx.utils.imageloader.BitmapLoaderCustom;
import de.meisterfuu.animexx.xmpp.XMPPService;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.drive.internal.p;

/**
 * Helper class for showing and canceling rpgpost notifications.
 * <p>
 * This class makes heavy use of the {@link NotificationCompat.Builder} helper
 * class to create notifications in a backward-compatible way.
 */
public class RPGPostNotification {
	
	public static String filter;
	
	/**
	 * The unique identifier for this type of notification.
	 */
	private static final String NOTIFICATION_TAG = "RPGPost";

	public static final String NEW_POST = "de.meisterfuu.animexx.new";

	/**
	 * Shows the notification, or updates a previously shown notification of
	 * this type, with the given parameters.
	 * <p>
	 * TODO: Customize this method's arguments to present relevant content in
	 * the notification.
	 * <p>
	 * TODO: Customize the contents of this method to tweak the behavior and
	 * presentation of rpgpost notifications. Make sure to follow the <a
	 * href="https://developer.android.com/design/patterns/notifications.html">
	 * Notification design guidelines</a> when doing so.
	 * 
	 * @see #cancel(Context)
	 */
	public static void notify(final Context pContext, final String pTitle, final String pUserName, final String pUserId, final String pId, final String pFrom, final int pNumber) {
		if(filter != null && pId.equalsIgnoreCase(filter)){
			
			Intent intent = new Intent(RPGPostNotification.NEW_POST);
			intent.setPackage(pContext.getPackageName());
			pContext.sendBroadcast(intent);
			
			return;
		}
		
		if(pUserId.equals(""+Self.getInstance(pContext).getUserID())){
			return;
		}
		

		// This image is used as the notification's large icon (thumbnail).
		// TODO: Remove this if your notification has no relevant thumbnail.
		final Bitmap picture = BitmapLoaderCustom.getUserBitmap(pUserId, pContext);

		final String ticker = "Neuer RPG Eintrag von "+pUserName;
		final String title = pUserName;
		final String text = pTitle;

		final NotificationCompat.Builder builder = new NotificationCompat.Builder(pContext)


				// Set required fields, including the small icon, the
				// notification title, and text.
				.setSmallIcon(R.drawable.ic_stat_rpgpost)
				.setContentTitle(title)
				.setContentText(text)

				// All fields below this line are optional.

				// Use a default priority (recognized on devices running Android
				// 4.1 or later)
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)

				// Provide a large icon, shown with the notification in the
				// notification drawer on devices running Android 3.0 or later.
				//.setLargeIcon(picture)

				// Set ticker text (preview) information for this notification.
				.setTicker(ticker)

				// Show a number. This is useful when stacking notifications of
				// a single type.
			    //.setNumber(number)

				// If this notification relates to a past or upcoming event, you
				// should set the relevant time information using the setWhen
				// method below. If this call is omitted, the notification's
				// timestamp will by set to the time at which it was shown.
				// TODO: Call setWhen if this notification relates to a past or
				// upcoming event. The sole argument to this method should be
				// the notification timestamp in milliseconds.
				// .setWhen(...)

				// Set the pending intent to be initiated when the user touches
				// the notification.
				.setContentIntent(RPGListFragment.getPendingIntent(pContext))

				// Example additional actions for this notification. These will
				// only show on devices running Android 4.1 or later, so you
				// should ensure that the activity in this notification's
				// content intent provides access to the same actions in
				// another way.
//				.addAction(
//						R.drawable.ic_action_stat_share,
//						res.getString(R.string.action_share),
//						PendingIntent.getActivity(context, 0, Intent
//								.createChooser(
//										new Intent(Intent.ACTION_SEND).setType(
//												"text/plain")
//												.putExtra(Intent.EXTRA_TEXT,
//														"Dummy text"),
//										"Dummy title"),
//								PendingIntent.FLAG_UPDATE_CURRENT))
//				.addAction(R.drawable.ic_action_stat_reply,
//						res.getString(R.string.action_reply), null)

				// Automatically dismiss the notification when it is touched.
				.setAutoCancel(true);

		if(picture != null)builder.setLargeIcon(picture);
		
		Boolean loud = PreferenceManager.getDefaultSharedPreferences(pContext).getBoolean("notifications_new_message", true);
		String sound_uri = PreferenceManager.getDefaultSharedPreferences(pContext).getString("notifications_new_message_ringtone", null);
		Boolean vibrate = PreferenceManager.getDefaultSharedPreferences(pContext).getBoolean("notifications_new_message_vibrate", true);
		
		if(loud){
			if(sound_uri != null){
				builder.setSound(Uri.parse(sound_uri));
			} else {
				builder.setDefaults(Notification.DEFAULT_SOUND);
			}
			if(vibrate){
				builder.setVibrate(new long[]{0,150,100,150});
			}
			builder.setLights(R.color.animexx_blue, 1000, 600);
			
			notify(pContext, pId.hashCode(),builder.build());
		}
		

	}

	@TargetApi(Build.VERSION_CODES.ECLAIR)
	private static void notify(final Context context, int pID, final Notification notification) {
		final NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify(NOTIFICATION_TAG, pID, notification);
	}

	/**
	 * Cancels any notifications of this type previously shown using
	 * {@link #notify(Context, String, int)}.
	 */
	@TargetApi(Build.VERSION_CODES.ECLAIR)
	public static void cancel(final Context context, long pID) {
		final NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(NOTIFICATION_TAG, (""+pID).hashCode());
	}
}