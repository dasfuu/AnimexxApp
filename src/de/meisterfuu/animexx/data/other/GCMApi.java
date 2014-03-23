package de.meisterfuu.animexx.data.other;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import oauth.signpost.OAuth;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.meisterfuu.animexx.KEYS;
import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.PostBodyFactory;
import de.meisterfuu.animexx.utils.Request;


public class GCMApi {
	

	Context mContext;
	Gson gson;
	ArrayList<String> EventCodes = new ArrayList<String>();
	
	String[] ev1 = new String[]{"XXEventENS", "XXEventGeburtstag", "XXEventGaestebuch"};
	String[] ev2 = new String[]{"XXEventVerrealkontaktet", "XXEventUsernameChange", "XXEventTwitterAnbindung", "XXEventSelbstbeschreibungErwaehnung", "XXEventEventsDabei"};
	String[] ev3 = new String[]{"XXEventSelbstbeschreibungAenderung", "XXEventSteckbriefVideoNeu", "XXEventMagDoujinshi", "XXEventMagWeblogeintrag", "XXEventMagEvent", "XXEventMagFanart", "XXEventMagBastelei", "XXEventMagFanfic", "XXEventMagFoto", "XXEventMagFotoreihe", "XXEventMagOekaki", "XXEventMagNews", "XXEventMagWettbewerb", "XXEventMagManga", "XXEventMagDVD", "XXEventMagArtbook", "XXEventMagCD", "XXEventMagGame", "XXEventMagAsia", "XXEventMagUmfrage", "XXEventMagCosplay", "XXEventMagJFashion", "XXEventMagForumThread", "XXEventMagForumPosting", "XXEventMagZirkelForumThread", "XXEventMagZirkelForumPosting", "XXEventMagEventVideo", "XXEventMagFanVideo", "XXEventMagWebshop", "XXEventMagAudiobook"};
	String[] ev4 = new String[]{"XXEventRPGAdminUebergabe", "XXEventRPGBewerbungAngenommen", "XXEventRPGBewerbungAbgelehnt", "XXEventRPGBewerbung", "XXEventRPGAbmeldung","XXEventRPGPosting"};
	
	
	//Init
	
	public GCMApi(Context pContext){
		this.mContext = pContext.getApplicationContext();
		GsonBuilder b = new GsonBuilder();
		gson = b.create();	
		EventCodes.addAll(Arrays.asList(ev1));
		EventCodes.addAll(Arrays.asList(ev2));
		EventCodes.addAll(Arrays.asList(ev3));
		EventCodes.addAll(Arrays.asList(ev4));
	}
	
	
	//Public Methods
	
	/**
	 * @param pCallback
	 */
	public void activateGCMEvents(final APICallback pCallback){		
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				APIException error = null;				

				try {
					activateGCMEventsWeb();
				} catch (APIException e) {
					error = e;
				}
				

				final APIException ferror = error;
				
				hand.post(new Runnable() {			
					public void run() {
						if(pCallback != null) pCallback.onCallback(ferror, null);
					}
				});
			}
		}).start();
	}
	
	/**
	 * @param pId
	 * @param pCallback
	 */
	public void setGCMID(final String pId, final APICallback pCallback){		
		final Handler hand = new Handler();		
		new Thread(new Runnable() {
			public void run() {
				APIException error = null;				

				try {
					sentGCMIDWeb(pId);
				} catch (APIException e) {
					error = e;
				}
				

				final APIException ferror = error;
				
				hand.post(new Runnable() {			
					public void run() {
						if(pCallback != null) pCallback.onCallback(ferror, null);
					}
				});
			}
		}).start();
	}
	
	public void registerGCM(final APICallback pCallback){	
		final Handler hand = new Handler();	
		final Thread t = new Thread(new Runnable() {

			public void run() {
				Looper.prepare();
				GoogleCloudMessaging gcm =  GoogleCloudMessaging.getInstance(mContext);
				try {
					String reg_id = gcm.register(KEYS.GCM_SENDER_ID);
					Log.e("Animexx GCM", "NEW ID: "+reg_id);
					saveGCM(reg_id);


					setGCMID(reg_id, new APICallback() {
						
						@Override
						public void onCallback(final APIException pError, final Object pObject) {
							hand.post(new Runnable() {			
								public void run() {
									if(pCallback != null) pCallback.onCallback(pError, pObject);
								}
							});
						}
					});

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Looper.loop();
			}
		});
		t.start();
	}
	
	/**
	 * @param context
	 */
	public String getRegistrationId(Context context) {
		    final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		    String registrationId = prefs.getString("gcm_reg_id", "");
		    
		    Log.e("Animexx GCM", "getRegistrationId()");
			Log.e("Animexx GCM", "GCM ID: "+registrationId);
		    if (registrationId.isEmpty()) {
				Log.e("Animexx GCM", "EMTPY ID");
		        return null;
		    }

		    int registeredVersion = prefs.getInt("gcm_app_ver", Integer.MIN_VALUE);
		    int currentVersion = getAppVersion(context);
		    if (registeredVersion != currentVersion) {
				Log.e("Animexx GCM", "WRONG VERSION");
		        return null;
		    }
		    return registrationId;
	}
	
	
	//Web-Api Access
	
	private void activateGCMEventsWeb()  throws APIException {
		try {
			String url = "https://ws.animexx.de/json/cloud2device/set_active_events/?api=2";

			PostBodyFactory factory = new PostBodyFactory();
			for (int i = 0; i < EventCodes.size(); i++){
				factory.putValue("events[]", EventCodes.get(i));
			}
					
			String result = Request.SignSendScribePost(url, factory);
			
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){
				
			} else {
				throw new APIException("Error", APIException.OTHER);
			}		
	
		} catch (Exception e) {
			e.printStackTrace();
			throw new APIException("Request Failed", APIException.REQUEST_FAILED);
		}	
	}
	
	private void sentGCMIDWeb(String id)  throws APIException {
		try {
			
			String url = "https://ws.animexx.de/json/cloud2device/registration_id_set/?api=2";
			
			PostBodyFactory factory = new PostBodyFactory();
			factory.putValue("registration_id", id);
			factory.putValue("collapse_by_type", "0");
					
			String result = Request.SignSendScribePost(url, factory);
			
			JSONObject resultObj = new JSONObject(result);
			if(resultObj.getBoolean("success")){
				
			} else {
				throw new APIException("Error", APIException.OTHER);
			}		
	
		} catch (Exception e) {
			e.printStackTrace();
			throw new APIException("Request Failed", APIException.REQUEST_FAILED);
		}	
	}
	
	
	//Persist Config
	
	private void saveGCM(String reg_id) {
		Log.e("Animexx GCM","Saving GCM ID: "+reg_id.hashCode());
		PreferenceManager.getDefaultSharedPreferences(mContext).edit().putString("gcm_reg_id", reg_id).commit();
		int appVersion = getAppVersion(mContext);
		PreferenceManager.getDefaultSharedPreferences(mContext).edit().putInt("gcm_app_ver", appVersion).commit();
	}
	
	private String getGCM() {
		return PreferenceManager.getDefaultSharedPreferences(mContext).getString("gcm_reg_id", null);
	}
		
	/**
	 * @return Application's version code from the {@code PackageManager}.
	 */
	private static int getAppVersion(Context context) {
	    try {
	        PackageInfo packageInfo = context.getPackageManager()
	                .getPackageInfo(context.getPackageName(), 0);
	        return packageInfo.versionCode;
	    } catch (NameNotFoundException e) {
	        // should never happen
	        throw new RuntimeException("Could not get package name: " + e);
	    }
	}



}

