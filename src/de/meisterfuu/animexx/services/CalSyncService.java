package de.meisterfuu.animexx.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.meisterfuu.animexx.Constants;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.data.events.EventApi;
import de.meisterfuu.animexx.objects.EventObject;
import de.meisterfuu.animexx.services.helper.EventCalHelper;
import de.meisterfuu.animexx.services.helper.PrivateCalHelper;
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
		public static final String TAG = "Animexx Cal";
	
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
	
	
    
    public static void performSync(Context pContext) {
        Log.d(TAG, "Starting sync...");
        EventCalHelper.sync(pContext);
        PrivateCalHelper.sync(pContext);
    }
}

