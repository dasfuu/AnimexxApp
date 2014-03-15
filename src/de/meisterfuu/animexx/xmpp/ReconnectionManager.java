package de.meisterfuu.animexx.xmpp;

import android.os.Handler;


public class ReconnectionManager {

	
	private XMPPService mService;
	private ChatConnection mConnection;
	private boolean mActive = false;
	private Handler mHandler;
	private int count;
	private int step;
	private int[] time = new int[]{5, 5, 5, 5, 10, 10, 10, 10, 30};

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
		if(!mActive) return;
		if(mConnection == null){
			mService.stopSelf();
			return;
		}
		
		if(!mConnection.isConnected() && mConnection.shouldConnect()){
			if(count == time[step]){
				mConnection.connect();
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
		
		
		mHandler.postDelayed(new Runnable() {			
			@Override
			public void run() {
				 check(count);
			}
		}, 2000);		
		
	}
}
