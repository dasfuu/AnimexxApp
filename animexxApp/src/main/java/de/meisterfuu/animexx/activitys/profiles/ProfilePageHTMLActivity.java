package de.meisterfuu.animexx.activitys.profiles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.api.APICallback;
import de.meisterfuu.animexx.api.broker.UserBroker;
import de.meisterfuu.animexx.objects.event.EventDescriptionObject;
import de.meisterfuu.animexx.utils.APIException;

public class ProfilePageHTMLActivity extends Activity {

	private UserBroker mApi;
	private String mBoxID;
	private long mID;
	private String mTitle;
	private WebView mWebView;

	public static void getInstance(Context pContext, long pUserID, String pBoxID, String pTitle){
		Intent i = new Intent().setClass(pContext, ProfilePageHTMLActivity.class);
		Bundle args = new Bundle();
		args.putLong("id", pUserID);
		args.putString("boxid", pBoxID);
		args.putString("title", pTitle);
		i.putExtras(args);
		pContext.startActivity(i);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_event_description);

		if(this.getIntent().hasExtra("id")){
			Bundle extras = this.getIntent().getExtras();
			mID = extras.getLong("id");
			mBoxID = extras.getString("boxid");
			mTitle = extras.getString("title");
			mApi = new UserBroker(this);
		} else {
			finish();
			return;
		}

		this.getActionBar().setTitle(mTitle);
		mWebView = (WebView) this.findViewById(R.id.activity_profile_page_html_html);

		mApi.getProfileBox(mBoxID, mID, new APICallback<Object>() {

			@Override
			public void onCallback(APIException pError, Object pObject) {
				EventDescriptionObject obj = (EventDescriptionObject) pObject;
				mWebView.loadDataWithBaseURL("https://animexx.de", obj.getHtml(), "text/html", "UTF-8", null);
			}
		});

	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_page_html, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
