package de.meisterfuu.animexx.xmpp;

import de.meisterfuu.animexx.DebugNotification;
import de.meisterfuu.animexx.utils.Helper;
import android.os.Handler;
import android.util.Log;


public class ConnectionManager {

	
	private XMPPService mService;
	private ChatConnection mConnection;
	private Handler mHandler;
	private boolean mActive = false;

	public static final String TAG = "RECONMANAGER";

	public ConnectionManager(XMPPService service){
		mService = service;
		mHandler = new Handler();
		initConnection();
		start();
	}
	
	public void start(){
		if(!mActive){
			mActive = true;
			step = count = 0;
			check();
		}
	}
	
	public void stop(){
		mActive = false;
		mConnection.disconnect();
	}
	
	private void initConnection(){
		mConnection = new ChatConnection(mService);
		mConnection.connect();
	}
	
	private int count;
	private int step;
	private long calls;
	private int[] time = new int[]{5, 5, 5, 5, 10, 10, 10, 10, 30};
	
	public void check(){
		
		//Debug Notification every ~10s
		if((++calls)%5==0) DebugNotification.notify(mService, "ReconManager is alive "+calls, 433962);
		
		try{
			if(!mActive){
				//return if stopped
				return;
			}
			
			if(mConnection == null){
				//recreate connection
				initConnection();
			}
			
			//connection is not ok
			if(!mConnection.isConnected() && mConnection.shouldConnect()){
				//Reconnect at this interval
				if(count >= time[step]){
					Log.i(TAG , "ChatConnection.connect() called in ReconManager");
					DebugNotification.notify(mService, "ReconManager is working", 433961);
					mConnection.connect();
					
					count = 0;
					step++;
					if(step >= time.length) step = time.length-1;
					
					//Keep intervall small for testing
					step = 0;
					
				//Skip this interval
				} else {
					count++;
				}
			
			//connection is ok: Reset interval
			} else {
				count = 0;
				step = 0;
			} 		
			
		//Something bad happened
		} catch (Exception e){
			DebugNotification.notify(mService, "Exception in ReconManager", 433963);
			e.printStackTrace();
			Helper.sendStacTrace(e, mService);
		}
	
		//Recursiv call
		mHandler.postDelayed(new Runnable() {			
			@Override
			public void run() {
				 check();
			}
		}, 2000);		
		
	}
}
