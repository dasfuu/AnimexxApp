package de.meisterfuu.animexx.xmpp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateUtils;
import android.util.Log;

import de.meisterfuu.animexx.Debug;
import de.meisterfuu.animexx.DebugNotification;
import de.meisterfuu.animexx.utils.Helper;

import static de.meisterfuu.animexx.xmpp.SmackConnection.ConnectionState;

public class ConnectionManager {

    private static ConnectionManager mThis;

    private XMPPService mService;
    private SmackConnection mConnection;

    private boolean mActive = false;

    private Thread mThread;

    protected Handler mTHandler;

    private BroadcastReceiver mNetRec;

    public static final String TAG = "RECONMANAGER";

    public ConnectionManager(XMPPService service) {
        mService = service;
        mThis = this;
        mNetRec = new BroadcastReceiver() {

            @Override
            public void onReceive(final Context context, final Intent intent) {

                if (intent.getExtras() != null) {
                    final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    final NetworkInfo net = connectivityManager.getActiveNetworkInfo();

                    if (net != null && net.isConnected()) {
                        Log.i(TAG, "Network " + net.getTypeName() + " connected");
                        tryConnect();
                    } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                        Log.d(TAG, "Lost connectivity");
                    }
                }

            }
        };


        mService.registerReceiver(mNetRec, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public Handler getmTHandler() {
        return mTHandler;
    }

    public static ConnectionManager getInstance() {
        return mThis;
    }

    public void start() {
        if (!mActive) {
            mActive = true;
            step = 0;

            // Create ConnectionThread Loop
            if (mThread == null || !mThread.isAlive()) {
                mThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        mTHandler = new Handler();
                        tryConnect();
                        Looper.loop();
                    }

                });
                mThread.start();
            }
        }
    }

    public void stop() {
        mActive = false;
        mThis = null;
        mService.unregisterReceiver(mNetRec);
        mTHandler.post(new Runnable() {

            @Override
            public void run() {
                mConnection.disconnect();
            }
        });
    }

    private int step = 0;
    private int[] time = new int[]{1, 3, 5, 5, 10, 10, 10, 10, 30, 60};

    public void tryConnect() {

        // Check in ConnectionThread
        if (mTHandler != null) {
            mTHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (connect()) {
                        //reset step counter
                        step = 0;
                    } else {
                        sheduleNextConnect();
                        //increase step counter
                        step++;
                    }

                }
            });
        } else {
            Log.d(TAG, "Chat mTHandler is NULL");
        }

    }

    public void sheduleNextConnect() {

        //prevent step counter getting out of bounds
        if (step >= time.length) {
            step = time.length - 1;
        }

        //schedule the next alarm
        Intent alarm = new Intent(mService, AlarmReceiver.class);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            alarm.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(mService, 0, alarm, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) mService.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (time[step] * DateUtils.SECOND_IN_MILLIS), pendingIntent);

    }

    private boolean connect() {
        try {
            if (!mActive) {
                // return if stopped
                return true;
            }

            if (mConnection == null) {
                // (re)create connection
                mConnection = new SmackConnection(mService);
            }

            //Check network
            final ConnectivityManager connectivityManager = (ConnectivityManager) mService.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo net = connectivityManager.getActiveNetworkInfo();

            //No network -> return true here to stop try reconnecting
            if(net == null || !net.isAvailable()){
                return true;
            }

            // connection is not ok -> reconnect
            if (SmackConnection.getStatus().equals(ConnectionState.DISCONNECTED) && mConnection.shouldConnect()) {
                Log.i(TAG, "SmackConnection.connect() called in ConnectionManager");
                mConnection.connect();
            }

            // check if reconnect was succesfull
            if (!SmackConnection.getStatus().equals(ConnectionState.DISCONNECTED) && mConnection.shouldConnect()) {
                return true;
            }

            //it wasn't...
            return false;

        // Something bad happened
        } catch (Exception e) {
            e.printStackTrace();
            Helper.sendStacTrace(e, mService);
        }

        return false;
    }

    static public class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                // Recon
                ConnectionManager.getInstance().tryConnect();
            } catch (Exception e) {
                e.printStackTrace();
                Helper.sendStacTrace(e, context);
            }

        }
    }


}
