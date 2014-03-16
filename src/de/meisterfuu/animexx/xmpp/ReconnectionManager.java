package de.meisterfuu.animexx.xmpp;

import java.util.Date;

import de.meisterfuu.animexx.DebugNotification;
import de.meisterfuu.animexx.utils.Helper;
import android.os.Handler;
import android.util.Log;


public class ReconnectionManager {

	
	private XMPPService mService;
	private ChatConnection mConnection;
	private boolean mActive = false;
	private Handler mHandler;
	private int count;
	private int step;
	private int[] time = new int[]{5, 5, 5, 5, 10, 10, 10, 10, 30};
	public static final String TAG = "RECONMANAGER";

	public ReconnectionManager(XMPPService service, ChatConnection connection){
		mService = service;
		mConnection = connection;
		mHandler = new Handler();
	}
	
	public void start(){
		if(!mActive){
			mActive = true;
			step = 0;
			check(1);
		}
	}
	
	public void stop(){
		mActive = false;
	}
	
	public void check(int i){
		final int j = i+1;
		if(j%60 == 0){
			DebugNotification.notify(mService, "ReconManager working", 433962);
		}
		
		try{
			if(!mActive) return;
			if(mConnection == null){
				mService.stopForeground(true);
				mService.stopSelf();
				return;
			}
			
			if(!mConnection.isConnected() && mConnection.shouldConnect()){
				if(count >= time[step]){
					Log.i(TAG , "ChatConnection.connect() called in ReconManager");
					DebugNotification.notify(mService, "ReconManager is active!\n "+(new Date()).toString(), 433961);
					boolean temp = mConnection.connect();
					Log.i(TAG , "ChatConnection.connect() result: "+temp);
					count++;
					count = 0;
					step++;
					if(step >= time.length) step = time.length-1;
				} else {
					count++;
				}
			} else {
				count = 0;
				step = 0;
			} 		
		} catch (Exception e){
			DebugNotification.notify(mService, "Exception in ReconManager", 433963);
			e.printStackTrace();
			Helper.sendStacTrace(e, mService);
		}
	
		
		mHandler.postDelayed(new Runnable() {			
			@Override
			public void run() {
				 check(j);
			}
		}, 2000);		
		
	}
}
