package de.meisterfuu.animexx.api.web;

import android.content.Context;
import android.util.Log;

import de.meisterfuu.animexx.Constants;
import de.meisterfuu.animexx.api.web.oauth.AccessToken;
import de.meisterfuu.animexx.api.web.oauth.RefreshToken;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Meisterfuu on 29.09.2014.
 */
public abstract class RefreshTokenCallback<T> implements Callback<T> {

	private final Context mContext;

	public RefreshTokenCallback(Context pContext){
		mContext = pContext;
	}

	@Override
	public void failure(final RetrofitError error) {
		if(error.getResponse().getStatus() == 401) {
			final WebAPI api = new WebAPI(mContext);
			AccessToken.clearToken(mContext);
			api.getOAuthApi().refreshAccessToken(Constants.CLIENT_ID,Constants.CLIENT_SECRET, AccessToken.getToken(mContext).getRefreshToken(),"refresh_token", new Callback<RefreshToken>() {
				@Override
				public void success(final RefreshToken t, final Response response) {
					retry();
				}

				@Override
				public void failure(final RetrofitError error) {
					Log.e("Api", "getProfile()", error);
				}
			});
		}
	}

	public abstract void retry();
}
