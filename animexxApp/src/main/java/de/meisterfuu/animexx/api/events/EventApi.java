package de.meisterfuu.animexx.api.events;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONObject;


import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.meisterfuu.animexx.api.APICallback;
import de.meisterfuu.animexx.objects.event.EventDescriptionObject;
import de.meisterfuu.animexx.objects.event.EventObject;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.Request;

public class EventApi {
	
	Context mContext;
	Gson gson;
	
	//Init
	
	public EventApi(Context pContext){
		this.mContext = pContext;
		GsonBuilder b = new GsonBuilder();
		gson = b.create();
	}
	

	//Public Methods
	
	public static final int LIST_PARTICIPATING = 1;
	public static final int LIST_HIGHLIGHT = 2;
	
	public static final int DETAIL_FULL = 1;
	public static final int DETAIL_BASIC = 2;
	
	/**
	 * @param pCallback
	 */
	public void getEventList(final APICallback pCallback, final int pList){
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				APIException error = null;				
				ArrayList<EventObject> list = new ArrayList<EventObject>();
				
				try {
					if(pList == LIST_PARTICIPATING){
						list = getEventListfromWeb();
					} else if (pList == LIST_HIGHLIGHT){
						list = getHighlightEventListfromWeb();					
					}

				} catch (APIException e) {
					error = e;
				}			
				
				final ArrayList<EventObject> retu = list;
				final APIException ferror = error;
				
				hand.post(new Runnable() {			
					public void run() {
						if(pCallback != null) pCallback.onCallback(ferror, retu);
					}
				});
			}
		}).start();
	}
	
	/**
	 * No threads
	 * @throws APIException 
	 */
	public ArrayList<EventObject> getEventListNT(final int pList) throws APIException {	
					if(pList == LIST_PARTICIPATING){
						return getEventListfromWeb();
					} else if (pList == LIST_HIGHLIGHT){
						return getHighlightEventListfromWeb();					
					}
					return null;
	}
	
	/**
	 * @param pCallback
	 */
	public void getEvent(final APICallback pCallback, final long pID, final int pDetailLevel){
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				APIException error = null;				
				EventObject list = null;
				
				try {
					if(pDetailLevel == DETAIL_FULL){
						list = getEventfromWeb(pID);
					} else if (pDetailLevel == DETAIL_BASIC){
						list = getBasicEventfromWeb(pID);					
					}

				} catch (APIException e) {
					error = e;
				}			
				
				final EventObject retu = list;
				final APIException ferror = error;
				
				hand.post(new Runnable() {			
					public void run() {
						if(pCallback != null) pCallback.onCallback(ferror, retu);
					}
				});
			}
		}).start();
	}
	
	/**
	 * @param pCallback
	 */
	public void getEventDescription(final APICallback pCallback, final long pEventID, final long pPageID){
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				APIException error = null;				
				EventDescriptionObject list = null;
				
				try {
						list = getEventDescriptionfromWeb(pEventID, pPageID);
				} catch (APIException e) {
					error = e;
				}			
				
				final EventDescriptionObject retu = list;
				final APIException ferror = error;
				
				hand.post(new Runnable() {			
					public void run() {
						if(pCallback != null) pCallback.onCallback(ferror, retu);
					}
				});
			}
		}).start();
	}
	
	//Web-Api Access
	
	private EventObject getEventfromWeb(long pID) throws APIException{
		// TODO Auto-generated method stub
		try {
			String result = Request.doHTTPGetRequest("https://ws.animexx.de/json/events/event/details/?api=2&id="+pID,mContext);
			//result = mContext.getResources().getString(R.string.testJSON);
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){
				//ENSApi.sendENSDEBUG("s", mContext);
				EventObject obj = gson.fromJson(resultObj.getJSONObject("return").toString(1), EventObject.class);
				//ENSObject[] x = gson.fromJson(resultObj.getString("return"), ENSObject[].class);

				return obj;
			} else {
				throw new APIException("Error", APIException.OTHER);
			}		
	
		} catch (Exception e) {
			e.printStackTrace();
			throw new APIException("Request Failed", APIException.REQUEST_FAILED);
		}	
	}
	
	private EventDescriptionObject getEventDescriptionfromWeb(long pEventID, long pPageID) throws APIException{
		// TODO Auto-generated method stub
		try {
			String result = Request.doHTTPGetRequest("https://ws.animexx.de/json/events/event/beschreibung_get/?api=2&event_id="+pEventID+"&beschreibung_id="+pPageID,mContext);
			//result = mContext.getResources().getString(R.string.testJSON);
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){
				//ENSApi.sendENSDEBUG("s", mContext);
				EventDescriptionObject obj = gson.fromJson(resultObj.getJSONObject("return").toString(1), EventDescriptionObject.class);
				//ENSObject[] x = gson.fromJson(resultObj.getString("return"), ENSObject[].class);

				return obj;
			} else {
				throw new APIException("Error", APIException.OTHER);
			}		
	
		} catch (Exception e) {
			e.printStackTrace();
			throw new APIException("Request Failed", APIException.REQUEST_FAILED);
		}	
	}
	
	private EventObject getBasicEventfromWeb(long pID) throws APIException{
		// TODO Auto-generated method stub
		try {
			String result = Request.doHTTPGetRequest("https://ws.animexx.de/json/events/event/grunddaten/?api=2&id="+pID,mContext);
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){
				EventObject obj = gson.fromJson(resultObj.getString("return"), EventObject.class);
	
				//ENSObject[] x = gson.fromJson(resultObj.getString("return"), ENSObject[].class);

				return obj;
			} else {
				throw new APIException("Error", APIException.OTHER);
			}		
	
		} catch (Exception e) {
			e.printStackTrace();
			throw new APIException("Request Failed", APIException.REQUEST_FAILED);
		}	
	}
	
	private ArrayList<EventObject> getEventListfromWeb() throws APIException {
		// TODO Auto-generated method stub
		try {
			String result = Request.doHTTPGetRequest("https://ws.animexx.de/json/events/event/dabei_events/?api=2&alles=1", mContext);
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){
				Type collectionType = new TypeToken<ArrayList<EventObject>>(){}.getType();
				ArrayList<EventObject> list = gson.fromJson(resultObj.getString("return"), collectionType);
	
				//ENSObject[] x = gson.fromJson(resultObj.getString("return"), ENSObject[].class);

				return list;
			} else {
				throw new APIException("Error", APIException.OTHER);
			}		
	
		} catch (Exception e) {
			e.printStackTrace();
			throw new APIException("Request Failed", APIException.REQUEST_FAILED);
		}	
	}
	
	private ArrayList<EventObject> getHighlightEventListfromWeb() throws APIException {
		// TODO Auto-generated method stub
		try {
			String result = Request.doHTTPGetRequest("https://ws.animexx.de/json/events/event/startseitenevents/?api=2&alles=1", mContext);
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){
				Type collectionType = new TypeToken<ArrayList<EventObject>>(){}.getType();
				ArrayList<EventObject> list = gson.fromJson(resultObj.getString("return"), collectionType);
	
				//ENSObject[] x = gson.fromJson(resultObj.getString("return"), ENSObject[].class);

				return list;
			} else {
				throw new APIException("Error", APIException.OTHER);
			}		
	
		} catch (Exception e) {
			e.printStackTrace();
			throw new APIException("Request Failed", APIException.REQUEST_FAILED);
		}	
	}
	
	
	
	
}
		
