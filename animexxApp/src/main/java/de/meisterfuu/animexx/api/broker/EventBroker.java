package de.meisterfuu.animexx.api.broker;

import android.content.Context;

import java.util.List;

import de.meisterfuu.animexx.api.ApiEvent;
import de.meisterfuu.animexx.api.EventBus;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.event.EventDescriptionObject;
import de.meisterfuu.animexx.objects.event.EventObject;
import de.meisterfuu.animexx.objects.profile.ProfileObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class EventBroker extends BasicWebBroker {


    //Init

    public EventBroker(Context pContext) {
        super(pContext);
    }


    //Public Methods

    /**
     * @param pCallback
     */
    public void getEventList(final Callback<ReturnObject<List<EventObject>>> pCallback) {
        getWebApi().getApi().getEventsAttending(pCallback);
    }

    /**
     * No threads
     */
    public List<EventObject> getEventListNT() throws RetrofitError {
        return getWebApi().getApi().getEventsAttending().getObj();
    }

    public void getEvent(final long pID, final int pCallerID) {
        getWebApi().getApi().getEvent(pID, new Callback<ReturnObject<EventObject>>() {
            @Override
            public void success(final ReturnObject<EventObject> t, final Response response) {
                EventBus.getBus().getOtto().post(new ApiEvent.EventEvent().setObj(t.getObj()).setCallerID(pCallerID));
            }

            @Override
            public void failure(final RetrofitError error) {
            }
        });
    }

    /**
     * @param pCallback
     */
    public void getEventDescription(final long pPageID, final long pEventID, final Callback<ReturnObject<EventDescriptionObject>> pCallback) {
        getWebApi().getApi().getEventDescription(pEventID, pPageID, pCallback);
    }


}

