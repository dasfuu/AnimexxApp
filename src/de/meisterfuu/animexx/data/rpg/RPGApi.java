package de.meisterfuu.animexx.data.rpg;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.data.ens.ENSApi;
import de.meisterfuu.animexx.data.ens.ENSDatabase;
import de.meisterfuu.animexx.objects.ENSFolderObject;
import de.meisterfuu.animexx.objects.RPGObject;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.Request;

public class RPGApi {
	
	Context mContext;
	Gson gson;
	
	//Init
	
	public RPGApi(Context pContext){
		this.mContext = pContext;
		GsonBuilder b = new GsonBuilder();
		gson = b.create();
	}
	

	//Public Methods
	
	/**
	 * @param pCallback
	 */
	public void getFolderList(final APICallback pCallback){
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				APIException error = null;				
				ArrayList<RPGObject> list = new ArrayList<RPGObject>();
				
				try {
					list = getRPGListfromWeb();
				} catch (APIException e) {
					error = e;
				}			
				
				final ArrayList<RPGObject> retu = list;
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
	
	private ArrayList<RPGObject> getRPGListfromWeb() throws APIException {
		// TODO Auto-generated method stub
		try {
			String result = Request.doHTTPGetRequest("https://ws.animexx.de/json/rpg/meine_rpgs/?api=2&alles=1");
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){
				Type collectionType = new TypeToken<ArrayList<RPGObject>>(){}.getType();
				ArrayList<RPGObject> list = gson.fromJson(resultObj.getString("return"), collectionType);
	
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
		
