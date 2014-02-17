package de.meisterfuu.animexx.data;

import org.json.JSONException;
import org.json.JSONObject;

import de.meisterfuu.animexx.data.profile.UserApi;
import de.meisterfuu.animexx.utils.APIException;
import oauth.signpost.OAuth;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;


public class Self {
	
	private static Self mSelf;
	private Context mContext; 
	private String mUsername;
	private long mUserID;
	
	public Self(Context pContext) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(pContext);
		mUsername = pref.getString("self_username", null);
		mUserID = pref.getLong("self_userid", -1L);
		this.mContext = pContext;
	}

	public static Self getInstance(Context pContext){	
		if(mSelf == null){
			mSelf = new Self(pContext);
		} else {
			mSelf.mContext = pContext;
		}
		return mSelf;
	}
	
	public void fetchSelf(){
		UserApi uAPI = new UserApi(mContext);
		uAPI.getMe(new APICallback() {
			
			@Override
			public void onCallback(APIException pError, Object pObject) {
				JSONObject result = (JSONObject)pObject;
				Editor edit = PreferenceManager.getDefaultSharedPreferences(mContext).edit();
				try {
					edit.putLong("self_userid", result.getLong("id"));
					edit.putString("self_username", result.getString("username"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
				edit.commit();
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
				mUsername = pref.getString("self_username", null);
				mUserID = pref.getLong("self_userid", -1L);
			}
		});
		

	}
		
	public boolean isLoggedIn() {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
		String oauth_token = pref.getString(OAuth.OAUTH_TOKEN, null);
		return (oauth_token != null);
	}
	
	public String getUsername(){
		return this.mUsername;
	}
	
	public long getUserID(){
		return this.mUserID;
	}



}
