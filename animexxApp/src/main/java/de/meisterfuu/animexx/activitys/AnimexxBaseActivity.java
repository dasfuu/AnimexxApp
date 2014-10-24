package de.meisterfuu.animexx.activitys;

import android.app.Activity;
import android.os.Bundle;

import com.squareup.otto.DeadEvent;
import com.squareup.otto.Subscribe;

import de.meisterfuu.animexx.api.ApiEvent;
import de.meisterfuu.animexx.api.EventBus;
import de.meisterfuu.animexx.api.xmpp.StatusEvent;
import icepick.Icepick;
import icepick.Icicle;

/**
 * Created by Meisterfuu on 26.09.2014.
 */
public class AnimexxBaseActivity extends Activity {

	EventBus mBus;
	@Icicle int mCallerID = -1;

	private Object mBaseActivityListener = new Object() {

		@SuppressWarnings("unused")
		@Subscribe
		public void onLoginEvent(StatusEvent pEvent) {

		}

		@SuppressWarnings("unused")
		@Subscribe
		public void onApiCall(ApiEvent.ApiProxyEvent pProxyEvent) {
			if (pProxyEvent.getEvent().getCallerID() == mCallerID) {
				// Send the real event off to the real handler.
				mBus.getOtto().post(pProxyEvent.getEvent());
			} else {
				// Send the event back to Api so it can retry it for the
				// intended Activity.
				mBus.getOtto().post(new DeadEvent(mBus.getOtto(), pProxyEvent.getEvent()));
			}
		}
	};

	public int getCallerID(){
		return mCallerID;
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mBus = EventBus.getBus();
		mBus.getOtto().register(this);
		mBus.getOtto().register(mBaseActivityListener);

		if(savedInstanceState == null){
			mCallerID = mBus.getNewCallerID();
		} else {
			Icepick.restoreInstanceState(this, savedInstanceState);
			mBus.retryDeadEvents(mCallerID);
		}

	}

	@Override public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Icepick.saveInstanceState(this, outState);
	}

	@Override
	public void onResume() {
		super.onResume();

		mBus.getOtto().register(this);
		mBus.getOtto().register(mBaseActivityListener);

	}

	@Override
	protected void onPostResume() {
		super.onPostResume();

		/**
		 * This covers missed events caused by dialogs or other views causing the
		 * Activity's onPause method to be called which unregisters the Activity
		 * as well as returning to an already running Activity via the back button.
		 */
		mBus.retryDeadEvents(mCallerID);
	}

	@Override
	public void onPause() {
		super.onPause();

		mBus.getOtto().unregister(this);
		mBus.getOtto().unregister(mBaseActivityListener);
	}
}
