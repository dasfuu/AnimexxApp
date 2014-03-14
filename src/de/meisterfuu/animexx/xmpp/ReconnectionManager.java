package de.meisterfuu.animexx.xmpp;

import android.os.Handler;


public class ReconnectionManager {

	
	private XMPPService mService;
	private ChatConnection mConnection;
	private boolean mActive = false;
	private Handler mHandler;
	private int count;
	private int[] time = new int[]{15, 15, 30, 30, 60};

	public ReconnectionManager(XMPPService service, ChatConnection connection){
		mService = service;
		mConnection = connection;
		mHandler = new Handler();
	}
	
	public void start(){
		if(!mActive){
			mActive = true;
			check();
		}
	}
	
	public void stop(){
		mActive = false;
	}
	
	public void check(){
		if(!mActive) return;
		
		if(mConnection != null && !mConnection.isConnected() && mConnection.shouldConnect()){
			mConnection.connect();
			count++;
		} else if (mConnection != null){
			count = 0;
		} if(mConnection == null){
			mService.stopSelf();
			return;
		}
		if(count > 4) count = 4;
		int ti = time[count]*1000;
		mHandler.postDelayed(new Runnable() {			
			@Override
			public void run() {
				 check();
			}
		}, ti);		
		
	}
}
