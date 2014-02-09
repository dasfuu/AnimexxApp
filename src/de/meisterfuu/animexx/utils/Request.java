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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.json.JSONArray;
import org.json.JSONObject;

import de.meisterfuu.animexx.Constants;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import android.util.Log;
import android.widget.Toast;

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
		HttpGet request = new HttpGet(url);
		//Log.i("Animexx", "Requesting URL : " + url);
		return SignSend(request);
	}

	
	public static String sendGCMID(String id, String collapse) throws Exception {
		String url = "https://ws.animexx.de/json/cloud2device/registration_id_set/?api=2";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("registration_id", id));
		nameValuePairs.add(new BasicNameValuePair("collapse_by_type", collapse));
		request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		Log.i("Animexx", "Requesting URL : " + url);
		return SignSend(request);
	}

	public static String setGCMEvents() throws Exception {
		String url = "https://ws.animexx.de/json/cloud2device/set_active_events/?api=2";
		HttpPost request = new HttpPost(url);

		// String s="dummy=dummy";
		// s += "&events[]=XXEventENS";
		// s += "&events[]=XXEventGeburtstag";
		// s += "&events[]=XXEventGaestebuch";

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
		nameValuePairs.add(new BasicNameValuePair("events[]", "XXEventENS"));
		nameValuePairs.add(new BasicNameValuePair("events[]", "XXEventGeburtstag"));
		nameValuePairs.add(new BasicNameValuePair("events[]", "XXEventGaestebuch"));
		nameValuePairs.add(new BasicNameValuePair("events[]", "XXEventRPGPosting"));
		request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		// StringEntity se = new StringEntity(s);
		// se.setContentType("application/x-www-form-urlencoded");

		// request.setEntity(se);

		Log.i("Animexx", "Requesting URL : " + url);
		return SignSend(request);
	}


	public static String deleteGCMID(String id) throws Exception {
		String url = "https://ws.animexx.de/json/cloud2device/registration_id_del/?api=2";
		HttpPost request = new HttpPost(url);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		nameValuePairs.add(new BasicNameValuePair("registration_id", id));
		request.setEntity(new UrlEncodedFormEntity(nameValuePairs));

		Log.i("Animexx", "Requesting URL : " + url);
		return SignSend(request);
	}






	public static boolean checkpush(Context con) {
		try {
			String jsonOutput = doHTTPGetRequest("https://ws.animexx.de/json/cloud2device/registration_id_get/?api=2");
			String jsonResponse = new JSONObject(jsonOutput).getJSONObject("return").getJSONArray("registration_ids").getString(0);
			Log.i("GCM-Server", jsonResponse);

			//GCMRegistrar.checkDevice(con);
			//GCMRegistrar.checkManifest(con);
			final String regId = "";//GCMRegistrar.getRegistrationId(con);
			Log.i("GCM-Device", regId);

			if (jsonResponse.equals(regId)) {
				Log.i("GCM", "Active");
				return true;
			} else {
				Log.i("GCM", "Inactive");
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public static String SignSend(HttpRequestBase pRequest) throws Exception {
		OAuthConsumer consumer = getConsumer();
		DefaultHttpClient httpclient = new DefaultHttpClient();		
		httpclient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, "Android App " + Constants.VERSION);
		
		consumer.sign(pRequest);		
		Log.i("Animexx", "Request : " + pRequest.getURI());		
		HttpResponse response = httpclient.execute(pRequest);
		Log.i("Animexx", "Statusline : " + response.getStatusLine());
		
		InputStream data = response.getEntity().getContent();
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(data));
		String responeLine;
		StringBuilder responseBuilder = new StringBuilder();
		while ((responeLine = bufferedReader.readLine()) != null) {
			responseBuilder.append(responeLine );
		}
		
		String erg = responseBuilder.toString();
		Log.i("Animexx", "Response : " +  erg.substring(0, Math.min(60, erg.length()-1)));

		return erg;
	}

	// HttpPost request = new HttpPost(url);
	// HttpParameters para = new HttpParameters();
	// para.put("msg", msg)
}
