package de.meisterfuu.animexx.activitys.main;

import de.meisterfuu.animexx.Constants;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.api.web.WebAPI;
import de.meisterfuu.animexx.api.web.oauth.AccessToken;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class RequestTokenActivity extends Activity {

	private TextView LadeMessage;
	private WebAPI mApi;
	private AccessToken mToken;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mApi = new WebAPI(RequestTokenActivity.this);
		setContentView(R.layout.login_animation);
		LadeMessage = (TextView) findViewById(R.id.login_status);

		LadeMessage.setText("Starte Login...");

		getRequestToken();
	}



	private void getRequestToken() {
		LadeMessage.setText("Browser starten...");
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.OAUTH_URL));

		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP
				| Intent.FLAG_ACTIVITY_NO_HISTORY
				| Intent.FLAG_FROM_BACKGROUND);

		RequestTokenActivity.this.startActivity(intent);
	}

	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		final Uri uri = intent.getData();
		if (uri != null	&& uri.getScheme().equals(Constants.OAUTH_CALLBACK_SCHEME)) {
			Log.i("OAuth", "Callback received : " + uri);
			Log.i("OAuth", "Retrieving Access Token");
			getAccessToken(uri);
		}
	}


	private void getAccessToken(Uri uri) {
		final String code = uri.getQueryParameter("code").trim();

		mApi.getOAuthApi().getAccessToken(Constants.CLIENT_ID, Constants.CLIENT_SECRET, code, "authorization_code", new Callback<AccessToken>() {
			@Override
			public void success(final AccessToken token, final Response response) {
				mToken = token;
				AccessToken.saveToken(mToken, RequestTokenActivity.this);
				RequestTokenActivity.this.startActivity(new Intent(RequestTokenActivity.this, LoginActivity.class));
				Log.i("OAuth", "Access Token Retrieved");
				LadeMessage.setText("Login abgeschlossen! :)");
				RequestTokenActivity.this.finish();
			}

			@Override
			public void failure(final RetrofitError error) {
				LadeMessage.setText("Fehler beim verarbeiten des AccessTokens :(");
				Log.e("OAuth", "Access Token Retrieval Error", error);
			}
		});
	}

}
