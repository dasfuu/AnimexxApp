package de.meisterfuu.animexx.data.profile;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.objects.ENSObject;
import de.meisterfuu.animexx.objects.GBEntryObject;
import de.meisterfuu.animexx.objects.UserObject;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.PostBodyFactory;
import de.meisterfuu.animexx.utils.Request;
import oauth.signpost.OAuth;

/**
 * Created by Furuha on 14.07.2014.
 */
public class GBApi {



	Context mContext;
	Gson gson;



	//Init

	public GBApi(Context pContext){
		this.mContext = pContext;
		GsonBuilder b = new GsonBuilder();
		gson = b.create();
	}


	//Public Methods

	/**
	 * @param pUserID
	 * @param pPage
	 * @param pCallback (ArrayList<GBEntryObject>)
	 */
	public void getGBList(final long pUserID, final int pPage, final APICallback<ArrayList<GBEntryObject>> pCallback) {
		final Handler hand = new Handler();
		new Thread(new Runnable() {
			public void run() {
				APIException error = null;
				ArrayList<GBEntryObject> temp = null;
				try {
					temp = getGBListfromWeb(pPage, pUserID);
				} catch (APIException e) {
					error = e;
				}

				final ArrayList<GBEntryObject> retu = temp;
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

	private ArrayList<GBEntryObject> getGBListfromWeb(int pPage, long pUserID)  throws APIException{
		try {

			String result = Request.doHTTPGetRequest("https://ws.animexx.de/json/mitglieder/gaestebuch_lesen/?user_id=" + pUserID + "&text_format=both&seite=" + pPage + "&api=2&anzahl=30", mContext);
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){
				Type collectionType = new TypeToken<ArrayList<GBEntryObject>>(){}.getType();
				ArrayList<GBEntryObject> list = gson.fromJson(resultObj.getJSONObject("return").getString("eintraege"), collectionType);
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


	//DB Access
}
