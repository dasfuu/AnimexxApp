package de.meisterfuu.animexx.data.calendar;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.data.Self;
import de.meisterfuu.animexx.objects.CalendarEntryObject;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.Request;

public class CalendarApi {
	Context mContext;
	Gson gson;
	
	//Init
	
	public CalendarApi(Context pContext){
		this.mContext = pContext;
		GsonBuilder b = new GsonBuilder();
		gson = b.create();
	}
	

	//Public Methods
	
	public static final int LIST_PRIVATE = 1;
	public static final int LIST_BIRTHDAY = 2;
	public static final int LIST_ZIRKEL= 2;
	
	
	/**
	 * @param pCallback
	 */
	public void getEventList(final APICallback pCallback, final String pDay){
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				APIException error = null;				
				ArrayList<CalendarEntryObject> list = new ArrayList<CalendarEntryObject>();
				
				try {
//					if(pList == LIST_PARTICIPATING){
						list = getEntryListfromWeb(pDay);
//					} else if (pList == LIST_HIGHLIGHT){
//						list = getHighlightEventListfromWeb();					
//					}

				} catch (APIException e) {
					error = e;
				}			
				
				final ArrayList<CalendarEntryObject> retu = list;
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
	public ArrayList<CalendarEntryObject> getEventListNT(final String pDay) throws APIException {	
//					if(pList == LIST_PARTICIPATING){
						return getEntryListfromWeb(pDay);
//					} else if (pList == LIST_HIGHLIGHT){
//						return getHighlightEventListfromWeb();					
//					}
//					return null;
	}
	
	//Web-Api Access
	
	
	private ArrayList<CalendarEntryObject> getEntryListfromWeb(String pDay) throws APIException {
		try {
			String result = Request.doHTTPGetRequest("https://ws.animexx.de/json/kalender/list/?api=2&zuord_typ=user_private&zuord_id="+Self.getInstance(mContext).getUserID()+"&zeitraum=month&show_gebs=1&tag="+pDay);
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){
				Type collectionType = new TypeToken<ArrayList<CalendarEntryObject>>(){}.getType();
				ArrayList<CalendarEntryObject> list = gson.fromJson(resultObj.getJSONObject("return").getString("events"), collectionType);
	
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
		
