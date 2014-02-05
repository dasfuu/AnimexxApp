package de.meisterfuu.animexx.data.profile;



import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.objects.ENSObject;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.Request;


public class UserApi {
	
	
	Context mContext;
	Gson gson;
	
	
	
	//Init
	
	public UserApi(Context pContext){
		this.mContext = pContext;
		GsonBuilder b = new GsonBuilder();
		gson = b.create();
	}
	
	
	//Public Methods
	
	/**
	 * @param pCallback
	 */
	public void getMe(final APICallback pCallback){
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				APIException error = null;
				JSONObject temp = null;
				try {
					temp = getMefromWeb();
				} catch (APIException e) {
					error = e;
				}
				
				final JSONObject retu = temp;
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
	
	private ENSObject getUserfromWeb(long pID) throws APIException{

		try {
			String result = Request.doHTTPGetRequest("https://ws.animexx.de/json/ens/ens_open/?ens_id=" + pID + "&text_format=html&api=2");
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){
				ENSObject ENS = gson.fromJson(resultObj.getString("return"), ENSObject.class);
				return ENS;
			} else {
				throw new APIException("Error", APIException.OTHER);
			}		
	
		} catch (Exception e) {
			e.printStackTrace();
			throw new APIException("Request Failed", APIException.REQUEST_FAILED);
		}	

	}
	
	private JSONObject getMefromWeb() throws APIException{

		try {
			
			String result = Request.doHTTPGetRequest("https://ws.animexx.de/json/mitglieder/ich/?api=2");
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){
				return resultObj.getJSONObject("return");
			} else {
				throw new APIException("Error", APIException.OTHER);
			}		
	
		} catch (Exception e) {
			e.printStackTrace();
			throw new APIException("Request Failed", APIException.REQUEST_FAILED);
		}	

	}
		

	//DB Access

	


}