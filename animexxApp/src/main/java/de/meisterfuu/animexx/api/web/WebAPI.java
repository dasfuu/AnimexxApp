package de.meisterfuu.animexx.api.web;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;

import de.meisterfuu.animexx.api.web.oauth.InterceptorSigner;
import de.meisterfuu.animexx.api.web.oauth.OAuthInterface;
import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;

/**
 * Created by Furuha on 28.09.2014.
 */
public class WebAPI implements ErrorHandler {

	private static WebApiInterface sRApi;
	private OAuthInterface mROauthApi;

	public WebAPI(Context pContext){

		if(sRApi ==  null) {
			RestAdapter.Builder builder = new RestAdapter.Builder()
					.setEndpoint(WebApiInterface.API_ENDPOINT)
					.setClient(new OkClient(new OkHttpClient()))
					.setErrorHandler(this)
					.setRequestInterceptor(new InterceptorSigner(pContext.getApplicationContext()));

			RestAdapter adapter = builder.build();

			sRApi = adapter.create(WebApiInterface.class);
		}
	}

	public WebApiInterface getApi(){
		return sRApi;
	}

	public OAuthInterface getOAuthApi(){
		if(mROauthApi == null){
			RestAdapter.Builder builder = new RestAdapter.Builder()
					.setEndpoint(OAuthInterface.TOKEN_ENDPOINT)
					.setClient(new OkClient(new OkHttpClient()))
					.setLogLevel(RestAdapter.LogLevel.FULL);


			RestAdapter adapter = builder.build();

			mROauthApi = adapter.create(OAuthInterface.class);
		}

		return mROauthApi;
	}

	@Override
	public Throwable handleError(final RetrofitError cause) {

		if(cause.getResponse().getStatus() == 401){
			//Refresh Token
		}

		return cause;
	}
}
