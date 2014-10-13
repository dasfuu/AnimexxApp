package de.meisterfuu.animexx.api.broker;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;


import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.meisterfuu.animexx.api.APICallback;
import de.meisterfuu.animexx.api.broker.BasicWebBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.event.EventDescriptionObject;
import de.meisterfuu.animexx.objects.event.EventObject;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.Request;
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
		
