package de.meisterfuu.animexx.services;

import android.accounts.Account;
import android.accounts.OperationCanceledException;
import android.app.Service;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import de.meisterfuu.animexx.services.helper.EventCalHelper;
import de.meisterfuu.animexx.services.helper.PrivateCalHelper;

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

