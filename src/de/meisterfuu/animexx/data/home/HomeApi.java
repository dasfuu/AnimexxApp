package de.meisterfuu.animexx.data.home;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.objects.EventObject;
import de.meisterfuu.animexx.objects.home.ContactHomeObject;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.Request;

public class HomeApi {
	Context mContext;
	Gson gson;
	
	//Init
	
	public HomeApi(Context pContext){
		this.mContext = pContext;
		GsonBuilder b = new GsonBuilder();
		gson = b.create();
	}
	

	//Public Methods
	
	public static final int LIST_ALL = 1;
	public static final int LIST_MICROBLOG = 2;
	
	
	/**
	 * @param pCallback
	 */
	public void getContactWidgetList(final int pList, final APICallback pCallback){
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				APIException error = null;				
				ArrayList<ContactHomeObject> list = new ArrayList<ContactHomeObject>();
				
				try {
					if(pList == LIST_ALL){
						list = getContactListfromWeb();
					} else if (pList == LIST_MICROBLOG){
						list = getMBListfromWeb();					
					}

				} catch (APIException e) {
					error = e;
				}			
				
				final ArrayList<ContactHomeObject> retu = list;
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
	
	private ArrayList<ContactHomeObject> getContactListfromWeb() throws APIException {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -7);
		long from = c.getTimeInMillis()/1000;
		try {
			String result = Request.doHTTPGetRequest("https://ws.animexx.de/json/persstart5/get_widget_data/?api=2&widget_id=kontakte&zeit_von="+from+"&img_max_x=800&img_max_y=800&img_quality=90&img_format=jpg", mContext);
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){
				Type collectionType = new TypeToken<ArrayList<ContactHomeObject>>(){}.getType();
				ArrayList<ContactHomeObject> list = gson.fromJson(resultObj.getString("return"), collectionType);
	
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
	
	private ArrayList<ContactHomeObject> getMBListfromWeb() throws APIException {
		// TODO Auto-generated method stub
		try {
			String result = Request.doHTTPGetRequest("https://ws.animexx.de/json/persstart5/get_widget_data/?api=2&widget_id=kontakte_blog&img_max_x=800&img_max_y=800&img_quality=90&img_format=jpg",mContext);
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){
				Type collectionType = new TypeToken<ArrayList<ContactHomeObject>>(){}.getType();
				ArrayList<ContactHomeObject> list = gson.fromJson(resultObj.getString("return"), collectionType);
	
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
