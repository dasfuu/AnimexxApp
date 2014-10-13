package de.meisterfuu.animexx.api;

import de.meisterfuu.animexx.api.broker.UserBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.UserObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
		UserBroker uAPI = new UserBroker(mContext);
		uAPI.getMe(new Callback<ReturnObject<UserObject>>() {
			@Override

			public void success(final ReturnObject<UserObject> t, final Response response) {
				SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
				Editor edit = pref.edit();

				edit.putLong("self_userid", t.getObj().getId());
				edit.putString("self_username", t.getObj().getUsername());

				edit.commit();


				mUsername = pref.getString("self_username", null);
				mUserID = pref.getLong("self_userid", -1L);
			}

			@Override
			public void failure(final RetrofitError error) {

			}
		});
		

	}
		
	public boolean isLoggedIn() {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
		String oauth_token = pref.getString("OAUTH_REFRESHTOKEN", "");
		return (!oauth_token.isEmpty());
	}
	
	public String getUsername(){
		return this.mUsername;
	}
	
	public long getUserID(){
		return this.mUserID;
	}



}
