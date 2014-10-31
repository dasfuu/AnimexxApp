package de.meisterfuu.animexx.api.broker;

import java.util.List;


import android.content.Context;

import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.event.EventDescriptionObject;
import de.meisterfuu.animexx.objects.event.EventObject;
import retrofit.Callback;
import retrofit.RetrofitError;

public class EventBroker extends BasicWebBroker {
	

	//Init
	
	public EventBroker(Context pContext){
		super(pContext);
	}
	

	//Public Methods

	/**
	 * @param pCallback
	 */
	public void getEventList(final Callback<ReturnObject<List<EventObject>>> pCallback){
		getWebApi().getApi().getEventsAttending(pCallback);
	}
	
	/**
	 * No threads
	 */
	public List<EventObject> getEventListNT() throws RetrofitError {
		return getWebApi().getApi().getEventsAttending().getObj();
	}
	
	/**
	 * @param pCallback
	 */
	public void getEvent(final long pID, final Callback<ReturnObject<EventObject>> pCallback){
		getWebApi().getApi().getEvent(pID, pCallback);
	}
	
	/**
	 * @param pCallback
	 */
	public void getEventDescription(final long pPageID, final long pEventID, final Callback<ReturnObject<EventDescriptionObject>> pCallback){
		getWebApi().getApi().getEventDescription(pEventID, pPageID, pCallback);
	}
	

	
	
	
	
}
		
