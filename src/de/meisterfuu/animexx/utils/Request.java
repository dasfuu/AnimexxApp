package de.meisterfuu.animexx.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;

import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.DefaultApi10a;
import org.scribe.builder.api.DefaultApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import de.meisterfuu.animexx.Constants;
import de.meisterfuu.animexx.Debug;
import de.meisterfuu.animexx.data.ens.ENSApi;
import android.content.Context;
import android.content.SharedPreferences;

import android.util.Log;

public class Request {

	public static SharedPreferences config;

	public static OAuthConsumer getConsumer() {
		String token = config.getString(OAuth.OAUTH_TOKEN, "");
		String secret = config.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		OAuthConsumer consumer = new CommonsHttpOAuthConsumer(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
		consumer.setTokenWithSecret(token, secret);
		return consumer;
	}


	public static String doHTTPGetRequest(String url) throws Exception {
		return SignSendScribeGet(url);
//		return SignSend(new HttpGet(url));
	}

	public static String SignSend(HttpRequestBase pRequest) throws Exception {
		OAuthConsumer consumer = getConsumer();
		DefaultHttpClient httpclient = new DefaultHttpClient();		
		httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Android App " + Constants.VERSION);
		consumer.sign(pRequest);		
		if(!Debug.SILENT_NETWORK)Log.i("Animexx", "Request : " + pRequest.getURI());		
		HttpResponse response = httpclient.execute(pRequest);
		if(!Debug.SILENT_NETWORK)Log.i("Animexx", "Statusline : " + response.getStatusLine());
		
		InputStream data = response.getEntity().getContent();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(data));
		String responeLine;
		StringBuilder responseBuilder = new StringBuilder();
		while ((responeLine = bufferedReader.readLine()) != null) {
			responseBuilder.append(responeLine );
		}
		
		String erg = responseBuilder.toString();
		if(!Debug.SILENT_NETWORK)Log.i("Animexx", "Response : " +  erg.substring(0, Math.min(Debug.NETWORK_ANSWER_LOG_LENGTH, erg.length()-1)));

		return erg;
	}
	
	private static String SignSendScribeGet(String url) throws Exception {
		
		OAuthService service = new ServiceBuilder()
        .provider(AnimexxApi.class)
        .apiKey(Constants.CONSUMER_KEY)
        .apiSecret(Constants.CONSUMER_SECRET)
        .build();
		
		String token = config.getString(OAuth.OAUTH_TOKEN, "");
		String secret = config.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		
		OAuthRequest request = new OAuthRequest(Verb.GET, url);

		Token accessToken = new Token(token, secret);
		service.signRequest(accessToken, request); 
		
		if(!Debug.SILENT_NETWORK)Log.i("Animexx", "Request : " + request.toString());
		if(!Debug.SILENT_NETWORK)Log.i("Animexx", "Request URL: " + request.getUrl());		
		Response response = request.send();
		if(!Debug.SILENT_NETWORK)Log.i("Animexx", "Statusline : " + response.getCode());
		
		String erg = response.getBody();
		if(!Debug.SILENT_NETWORK)Log.i("Animexx", "Response : " +  erg.substring(0, Math.min(Debug.NETWORK_ANSWER_LOG_LENGTH, erg.length()-1)));
		return erg;
	}
	
	public static String SignSendScribeGet(String url, Context pContext) throws Exception {
		
		OAuthService service = new ServiceBuilder()
        .provider(AnimexxApi.class)
        .apiKey(Constants.CONSUMER_KEY)
        .apiSecret(Constants.CONSUMER_SECRET)
        .build();
		
		String token = config.getString(OAuth.OAUTH_TOKEN, "");
		String secret = config.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		
		OAuthRequest request = new OAuthRequest(Verb.GET, url);

		Token accessToken = new Token(token, secret);
		service.signRequest(accessToken, request); 
		
		if(!Debug.SILENT_NETWORK)Log.i("Animexx", "Request : " + request.toString());
		if(!Debug.SILENT_NETWORK)Log.i("Animexx", "Request URL: " + request.getUrl());		
		Response response = request.send();
		if(!Debug.SILENT_NETWORK)Log.i("Animexx", "Statusline : " + response.getCode());
		
		String erg = response.getBody();
		ENSApi.sendENSDEBUG(erg, "Rqeust", pContext);
		if(!Debug.SILENT_NETWORK)Log.i("Animexx", "Response : " +  erg.substring(0, Math.min(Debug.NETWORK_ANSWER_LOG_LENGTH, erg.length()-1)));
		return erg;
	}
	
	public static String SignSendScribePost(String url, PostBodyFactory factorybody, Context pContext) throws Exception {
		
		OAuthService service = new ServiceBuilder()
        .provider(new AnimexxApi())
        .apiKey(Constants.CONSUMER_KEY)
        .apiSecret(Constants.CONSUMER_SECRET)
        .debug()
        .build();
		
		String token = config.getString(OAuth.OAUTH_TOKEN, "");
		String secret = config.getString(OAuth.OAUTH_TOKEN_SECRET, "");
		
		OAuthRequest request = new OAuthRequest(Verb.POST, url);
		
		request.addHeader("Content-Type", "application/x-www-form-urlencoded");
		factorybody.build(request);

		Token accessToken = new Token(token, secret);
		service.signRequest(accessToken, request); // the access token from step 4

		if(!Debug.SILENT_NETWORK)Log.i("Animexx", "Request : " + request.toString());
		if(!Debug.SILENT_NETWORK)Log.i("Animexx", "Request URL: " + request.getUrl());		
		Response response = request.send();
		if(!Debug.SILENT_NETWORK)Log.i("Animexx", "Statusline : " + response.getCode());
		
		String erg = response.getBody();
		if(!Debug.SILENT_NETWORK)Log.i("Animexx", "Response : " +  erg.substring(0, Math.min(Debug.NETWORK_ANSWER_LOG_LENGTH, erg.length()-1)));
//		ENSApi.sendENSDEBUG(erg, "Rqeust", pContext);
		return erg;
	}
	
	public static class AnimexxApi extends DefaultApi10a {

		@Override
		public String getAccessTokenEndpoint() {
			return Constants.ACCESS_URL;
		}

		@Override
		public String getAuthorizationUrl(Token arg0) {
			return Constants.AUTHORIZE_URL+"?oauth_token="+arg0.getToken();
		}

		@Override
		public String getRequestTokenEndpoint() {
			return Constants.REQUEST_URL;
		}
		
	}
}
