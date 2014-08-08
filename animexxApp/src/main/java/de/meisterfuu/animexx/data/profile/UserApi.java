package de.meisterfuu.animexx.data.profile;



import java.lang.reflect.Type;
import java.util.ArrayList;

import de.meisterfuu.animexx.objects.ProfileObject;
import oauth.signpost.OAuth;

import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.objects.UserObject;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.PostBodyFactory;
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
	 * @param pUserID
	 * @param pCallback
	 */
	public void getProfile(final long pUserID, final APICallback<ProfileObject> pCallback){
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				APIException error = null;
				ProfileObject temp = null;
				try {
					temp = getProfilefromWeb(pUserID, true);
				} catch (APIException e) {
					error = e;
				}
				
				final ProfileObject retu = temp;
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
	
	
	/**
	 * @param pSearchString
	 * @param pCallback (ArrayList<UserSearchResultObject>)
	 */
	public void searchUserByName(final String pSearchString, final APICallback pCallback) {
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				APIException error = null;
				ArrayList<UserObject> temp = null;
				try {
					temp = searchUserByNameWeb(pSearchString);
				} catch (APIException e) {
					error = e;
				}
				
				final ArrayList<UserObject> retu = temp;
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
	 * @param pUsernameList
	 * @param pCallback (ArrayList<UserSearchResultObject>)
	 */
	public void getIDs(final ArrayList<String> pUsernameList, final APICallback pCallback) {
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				APIException error = null;
				ArrayList<UserObject> temp = null;
				try {
					temp = getIDsFromWeb(pUsernameList);
				} catch (APIException e) {
					error = e;
				}
				
				final ArrayList<UserObject> retu = temp;
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
	 * @param pUsernameList
	 * @return 
	 */
	public ArrayList<UserObject> NTgetIDs(final ArrayList<String> pUsernameList) {

				ArrayList<UserObject> temp = null;
				try {
					temp = getIDsFromWeb(pUsernameList);
				} catch (APIException e) {
				}				
				return temp;
	}
	
	/**
	 * @param pSearchString
	 * @return 
	 */
	public ArrayList<UserObject> NTsearchUserByName(final String pSearchString) {

				ArrayList<UserObject> temp = null;
				try {
					temp = searchUserByNameWeb(pSearchString);
				} catch (APIException e) {
				}				
				return temp;
	}
	
	
	//Web-Api Access
		
	private ArrayList<UserObject> searchUserByNameWeb(String pUsername) throws APIException {
		try {
			
			String result = Request.doHTTPGetRequest("https://ws.animexx.de/json/mitglieder/username_autocomplete/?api=2&str="+OAuth.percentEncode(pUsername),mContext);
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){				 
					Type collectionType = new TypeToken<ArrayList<UserObject>>(){}.getType();
					ArrayList<UserObject> list = gson.fromJson(resultObj.getString("return"), collectionType);	
					return list;
			} else {
					throw new APIException("Error", APIException.OTHER);
			}		
		} catch (Exception e) {
			e.printStackTrace();
			throw new APIException("Request Failed", APIException.REQUEST_FAILED);
		}	
	}
	
	private ArrayList<UserObject> getIDsFromWeb(ArrayList<String> pUsername) throws APIException {
		try {
			String url = "https://ws.animexx.de/json/mitglieder/usernames2ids/?api=2&get_user_avatar=true";
			
			
			PostBodyFactory factory = new PostBodyFactory();
			for (String name: pUsername){
				factory.putValue("usernames[]", name);
			}

			String result = Request.SignSendScribePost(url, factory, mContext);
			
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){				 
					Type collectionType = new TypeToken<ArrayList<UserObject>>(){}.getType();
					ArrayList<UserObject> list = gson.fromJson(resultObj.getJSONObject("return").getString("ids"), collectionType);	
					return list;
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
			
			String result = Request.doHTTPGetRequest("https://ws.animexx.de/json/mitglieder/ich/?api=2", mContext);
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

	private ProfileObject getProfilefromWeb(long pUserID, boolean pAllPictures) throws APIException{

		try {
			int allPictures = 0;
			if(pAllPictures){
				allPictures = 1;
			}
			
			String result = Request.doHTTPGetRequest("https://ws.animexx.de/json/mitglieder/steckbrief/?api=2&user_id="+ pUserID +"&allefotos="+ allPictures +"&mit_selbstbeschreibung=1&mit_boxen_infos=1&mit_statistiken=1&mit_gb=1&img_max_x=800&img_max_y=800&img_quality=90&img_format=jpg", mContext);
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){
				return gson.fromJson(resultObj.getString("return"), ProfileObject.class);
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
