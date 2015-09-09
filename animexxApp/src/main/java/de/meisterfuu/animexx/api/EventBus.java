package de.meisterfuu.animexx.api;

import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.DeadEvent;
import com.squareup.otto.Subscribe;

import java.util.LinkedList;

import de.meisterfuu.animexx.BuildConfig;

/**
 * Created by Meisterfuu on 26.09.2014.
 */
public class EventBus {

    private static EventBus sBus = new EventBus();
    private static int sCallerIDCounter = 0;

    public static EventBus getBus() {
        return sBus;
    }

    private Bus mOtto;
    private LinkedList<ApiEvent<?>> mEventQueue;

    private EventBus() {
        mEventQueue = new LinkedList<ApiEvent<?>>();
        mOtto = new Bus();
        mOtto.register(new Object() {

            @Subscribe
            public void onDeadEvent(DeadEvent deadEvent) {
                Object event = deadEvent.event;

                if (BuildConfig.DEBUG) {
                    Log.i("OTTO", "onDeadEvent: " + event.getClass().getName());
                }

                if (event instanceof ApiEvent<?>) {
                    addDeadApiEvent((ApiEvent<?>) event);
                } else {
                    if (event instanceof ApiEvent.ApiProxyEvent) {
                        addDeadApiEvent(((ApiEvent.ApiProxyEvent) event).getEvent());
                    }
                }
            }

        });
    }

    private void addDeadApiEvent(final ApiEvent<?> pEvent) {
        synchronized (mEventQueue) {
            mEventQueue.add(pEvent);
        }
    }

    public void retryDeadEvents(int pCallerID) {
        synchronized (mEventQueue) {
            if (mEventQueue.isEmpty()) {
                return;
            }

            LinkedList<ApiEvent<?>> deadApiEvents = mEventQueue;
            mEventQueue = new LinkedList<ApiEvent<?>>();
            int callerID = pCallerID;

            // Iterate over all the dead events, firing off each one.  If it fails,
            // it is recaught by the @Subscribe onDeadEvent, and added back to the list.
            for (ApiEvent<?> apiEvent : deadApiEvents) {
                // Fire the event If the activityids match, otherwise add it back
                // to the list of dead events so we can try it again later.
                if (callerID == apiEvent.getCallerID()) {
                    if (BuildConfig.DEBUG) {
                        Log.i("OTTO", "Retrying dead event: " +
                                apiEvent.getClass().getName());
                    }

                    getOtto().post(apiEvent);
                } else {
                    if (BuildConfig.DEBUG) {
                        Log.i("OTTO", "Adding dead event: " + apiEvent.getClass().toString());
                    }

                    mEventQueue.add(apiEvent);
                }
            }

            if (BuildConfig.DEBUG && mEventQueue.size() > 0) {
                Log.i("OTTO", "Skipped " + mEventQueue.size() + " dead events");
            }
        }
    }

    public void postProxyEvent(ApiEvent<?> event){
        getOtto().post(new ApiEvent.ApiProxyEvent(event));
    }

    public Bus getOtto() {
        return mOtto;
    }

    public int getNewCallerID() {
        return ++sCallerIDCounter;
    }


}
