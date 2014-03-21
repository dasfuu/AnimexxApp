package de.meisterfuu.animexx.xmpp;

import de.meisterfuu.animexx.DebugNotification;
import de.meisterfuu.animexx.utils.Helper;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;


public class ConnectionManager {

	private static ConnectionManager mThis;
	
	private XMPPService mService;
	private ChatConnection mConnection;

	private boolean mActive = false;

	private Thread mThread;
	protected Handler mTHandler;
	private Handler mHandler;

	public static final String TAG = "RECONMANAGER";

	public ConnectionManager(XMPPService service){
		mService = service;
		mHandler = new Handler();
		mThis = this;
	}
	
	public static ConnectionManager getInstance(){
		return mThis;
	}
	
	public void start(){
		if(!mActive){
			mActive = true;
			step = count = 0;
			
			//Create ConnectionThread Loop
			if(mThread == null || !mThread.isAlive()){
				mThread = new Thread(new Runnable() {
					@Override
					public void run() {
							Looper.prepare();
							mTHandler = new Handler();							
							checkTick();
							Looper.loop();
					}
		
				});
				mThread.start();
			} 
		}
	}
	
	public void stop(){
		mActive = false;
		mThis = null;
		mTHandler.post(new Runnable() {
			
			@Override
			public void run() {
				mConnection.disconnect();
			}
		});
	}
	
	private void initConnection(){
		mConnection = new ChatConnection(mService);
		mConnection.connect();
	}
	
	private int count;
	private int step;
	private long calls;
	private long calls_count;
	private int[] time = new int[]{5, 5, 5, 5, 10, 10, 10, 10, 30};
	
	public void checkTick(){
		
		//Debug Notification every ~50s
		if((calls++)%5 == 0)DebugNotification.notify(mService, "ReconManager is alive "+(++calls_count), 433962);
		
		//Check in ConnectionThread
		mTHandler.post(new Runnable() {			
			@Override
			public void run() {
				if(mConnection != null) mConnection.ping();
				check();
			}
		});	
		

	    Intent alarm = new Intent(mService, AlarmReceiver.class);
	    PendingIntent pendingIntent = PendingIntent.getBroadcast(mService, 0, alarm, PendingIntent.FLAG_CANCEL_CURRENT);
	    AlarmManager alarmManager = (AlarmManager) mService.getSystemService(Context.ALARM_SERVICE);
	    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+10000, pendingIntent); 
		
	}

	private void check() {
		try{
			if(!mActive){
				//return if stopped
				return;
			}
			
			if(mConnection == null){
				//(re)create connection
				initConnection();
			}
			
			//connection is not ok
			if(!mConnection.isConnected() && mConnection.shouldConnect()){
				//Reconnect at this interval
//				if(count >= time[step]){
					Log.i(TAG , "ChatConnection.connect() called in ReconManager");
					DebugNotification.notify(mService, "ReconManager is working", 433961);
//					mConnection.disconnect();
					mConnection.connect();
					
//					count = 0;
//					step++;
//					if(step >= time.length) step = time.length-1;
//					
//					//Keep intervall small for testing
//					step = 0;
//					
//				//Skip this interval
//				} else {
//					count++;
//				}
//			
//			//connection is ok: Reset interval
//			} else {
//				count = 0;
//				step = 0;
			} 		
			
		//Something bad happened
		} catch (Exception e){
			DebugNotification.notify(mService, "Exception in ReconManager", 433963);
			e.printStackTrace();
			Helper.sendStacTrace(e, mService);
		}
	}
	
	static public class AlarmReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			ConnectionManager.getInstance().checkTick();			
		}

	}
}
