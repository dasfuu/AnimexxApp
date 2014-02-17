package de.meisterfuu.animexx.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.meisterfuu.animexx.Constants;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.data.events.EventApi;
import de.meisterfuu.animexx.objects.EventObject;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.Request;

import android.accounts.Account;
import android.accounts.OperationCanceledException;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.BaseColumns;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.text.format.Time;
import android.util.Log;

public class CalSyncService extends Service {
	
	private static SyncAdapterImpl sSyncAdapter = null;
	   private static String CALENDAR_COLUMN_NAME = "animexx_motion_cal";
		public static final String TAG = "Animexx Event Cal";
	
	public CalSyncService() {
		super();
	}

	private static class SyncAdapterImpl extends AbstractThreadedSyncAdapter {
        private Context mContext;

        public SyncAdapterImpl(Context context) {
            super(context, true);
            mContext = context;
        }

        @Override
        public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
            try {
            	CalSyncService.performSync(mContext, account, extras, authority, provider, syncResult);
            } catch (OperationCanceledException e) {
//                Log.e(Constants.TAG, "OperationCanceledException", e);
            }
        }
    }
	
	@Override
    public IBinder onBind(Intent intent) {
        IBinder ret = null;
        ret = getSyncAdapter().getSyncAdapterBinder();
        return ret;
    }
	
	private SyncAdapterImpl getSyncAdapter() {
        if (sSyncAdapter == null)
            sSyncAdapter = new SyncAdapterImpl(this);
        return sSyncAdapter;
    }

	public static void performSync(Context mContext, Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) throws OperationCanceledException {
		performSync(mContext);
	}
	
	public static Uri getAdapterUri(Uri uri) {
        return uri.buildUpon().appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(Calendars.ACCOUNT_NAME, Constants.ACCOUNT_NAME)
                .appendQueryParameter(Calendars.ACCOUNT_TYPE, Constants.ACCOUNT_TYPE).build();
    }
	
	/**
     * Gets calendar id, when no calendar is present, create one!
     *
     * @param context
     * @return
     */
    private static long getCalendar(Context context) {
        Log.d("CalSyncDemo", "getCalendar Method...");

        ContentResolver contentResolver = context.getContentResolver();

        // Find the calendar if we've got one
        Uri calenderUri = getAdapterUri(Calendars.CONTENT_URI);

        Cursor cursor = contentResolver.query(calenderUri, new String[]{BaseColumns._ID},
                Calendars.ACCOUNT_NAME + " = ? AND " + Calendars.ACCOUNT_TYPE + " = ?",
                new String[]{Constants.ACCOUNT_NAME, Constants.ACCOUNT_TYPE}, null);

        try {
            if (cursor != null && cursor.moveToNext()) {
                return cursor.getLong(0);
            } else {
                ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();

                ContentProviderOperation.Builder builder = ContentProviderOperation
                        .newInsert(calenderUri);
                builder.withValue(Calendars.ACCOUNT_NAME, Constants.ACCOUNT_NAME);
                builder.withValue(Calendars.ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
                builder.withValue(Calendars.NAME, CALENDAR_COLUMN_NAME);
                builder.withValue(Calendars.CALENDAR_DISPLAY_NAME, "Animexx (Events)");
                builder.withValue(Calendars.CALENDAR_COLOR, Color.parseColor("#5858FF"));

                //builder.withValue(Calendars.CALENDAR_ACCESS_LEVEL, Calendars.CAL_ACCESS_EDITOR);
                builder.withValue(Calendars.CALENDAR_ACCESS_LEVEL, Calendars.CAL_ACCESS_READ);
 
                builder.withValue(Calendars.OWNER_ACCOUNT, Constants.ACCOUNT_NAME);
                builder.withValue(Calendars.SYNC_EVENTS, 1);
                builder.withValue(Calendars.VISIBLE, 1);
                operationList.add(builder.build());
                try {
                    contentResolver.applyBatch(CalendarContract.AUTHORITY, operationList);
                } catch (Exception e) {
                    Log.e(TAG, "getCalendar() failed", e);
                    return -1;
                }
                return getCalendar(context);
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
    }
    
    /**
     * Get a new ContentProviderOperation to insert a event
     *
     * @param context
     * @param calendarId
     * @param eventDate
     * @param year       The event is inserted for this year
     * @param title
     * @return
     */
    private static ContentProviderOperation insertEvent(Context context, long calendarId, EventObject event) {
        ContentProviderOperation.Builder builder;

        builder = ContentProviderOperation.newInsert(getAdapterUri(Events.CONTENT_URI));

        // Calendar cal = Calendar.getInstance();
        // cal.setTimeInMillis(event.getFromDate());

        /*
         * Allday events have to be set in UTC!
         * 
         * Without UTC it results in: CalendarProvider2 W insertInTransaction: allDay is true but
         * sec, min, hour were not 0.
         * http://stackoverflow.com/questions/3440172/getting-exception-when
         * -inserting-events-in-android-calendar
         */
        //cal.setTimeZone(TimeZone.getTimeZone("UTC"));
        // cal.setTimeZone(TimeZone.getTimeZone(Time.getCurrentTimezone()));

        /*
         * Define over entire day.s
         * 
         * Note: ALL_DAY is enough on original Android calendar, but some calendar apps (Business
         * Calendar) do not display the event if time between dtstart and dtend is 0
         */
        long dtstart = event.getStartTS().getTime();
        long dtend = event.getEndTS().getTime()+1;

        builder.withValue(Events.CALENDAR_ID, calendarId);
        
        builder.withValue(Events.ALL_DAY, true);
        builder.withValue(Events.DTSTART, dtstart);
        builder.withValue(Events.DTEND, dtend);
        builder.withValue(Events.EVENT_TIMEZONE, Time.getCurrentTimezone());

        builder.withValue(Events.TITLE, event.getName());
        builder.withValue(Events.DESCRIPTION, event.getName());
        
        builder.withValue(Events.STATUS, Events.STATUS_CONFIRMED);
        //builder.withValue(Events.HAS_ALARM, true);
        
        /*
         * Enable reminders for this event
         */
        //builder.withValue(Events.HAS_ALARM, 1);
        builder.withValue(Events.AVAILABILITY, Events.AVAILABILITY_BUSY);


        return builder.build();
    }
    
    private static void insertSingleEvent(Context context, long calendarId, EventObject event) {
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        
        Calendar sc = Calendar.getInstance();
        Calendar ec = Calendar.getInstance();
        
        sc.setTime(event.getStartTS());
        sc.set(Calendar.MINUTE, 0);
        sc.set(Calendar.HOUR, 0);
        ec.setTime(event.getEndTS());
        ec.set(Calendar.MINUTE, 59);
        ec.set(Calendar.HOUR, 23);
        
        long dtstart = sc.getTimeInMillis();
        long dtend = ec.getTimeInMillis();
        

        values.put(Events.CALENDAR_ID, calendarId);        
        values.put(Events.ALL_DAY, true);
        values.put(Events.DTSTART, dtstart);
        values.put(Events.DTEND, dtend);
        values.put(Events.EVENT_TIMEZONE, Time.getCurrentTimezone());

//        values.put(Events., value)
        
        values.put(Events.TITLE, event.getName());
        values.put(Events.DESCRIPTION, event.getName());
        
        values.put(Events.STATUS, Events.STATUS_CONFIRMED);
        values.put(Events.HAS_ALARM, true);

        Uri uri = cr.insert(Events.CONTENT_URI, values);
//        long eventID = Long.parseLong(uri.getLastPathSegment());
//        
//        ContentValues reminders = new ContentValues();
//        reminders.put(Reminders.EVENT_ID, eventID);
//        reminders.put(Reminders.METHOD, Reminders.METHOD_ALERT);
//        reminders.put(Reminders.MINUTES, 1);
//
//        cr.insert(Reminders.CONTENT_URI, reminders);


        
  
    }
    
    public static void performSync(Context context) {
        Log.d(TAG, "Starting sync...");
        Request.config = PreferenceManager.getDefaultSharedPreferences(context);

        ContentResolver contentResolver = context.getContentResolver();

        if (contentResolver == null) {
            Log.e(TAG, "Unable to get content resolver!");
            return;
        }

        long calendarId = getCalendar(context);
        if (calendarId == -1) {
            Log.e("CalendarSyncAdapter", "Unable to create calendar");
            return;
        }

        // Sync flow:
        // 1. Clear events table for this account completely
        // 2. Get birthdays from contacts
        // 3. Create events and reminders for each birthday

        // empty table
        int delEventsRows = contentResolver.delete(getAdapterUri(Events.CONTENT_URI), Events.CALENDAR_ID + " = ?", new String[]{String.valueOf(calendarId)});

        
        
        EventApi API = new EventApi(context);
        ArrayList<EventObject> events = new ArrayList<EventObject>();
		try {
	        Log.d(TAG, "Getting Events from Animexx");
			events = API.getEventListNT(EventApi.LIST_PARTICIPATING);
		} catch (APIException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        ArrayList<ContentProviderOperation> operationList = new ArrayList<ContentProviderOperation>();

  
        for(EventObject event: events){
        	//operationList.add(insertEvent(context, calendarId, event));
        	insertSingleEvent(context, calendarId, event);
        }
        
        //WICHTIG ADD MAXIMAL IN 200er SCHRITTEN

        /* Create events */
        if (operationList.size() > 0) {
            try {
                Log.d(TAG, "Start applying the batch...");
                contentResolver.applyBatch(CalendarContract.AUTHORITY, operationList);
                Log.d(TAG, "Applying the batch was successful!");
            } catch (Exception e) {
                Log.e(TAG, "Applying batch error!", e);
            }
        }
    }
}

