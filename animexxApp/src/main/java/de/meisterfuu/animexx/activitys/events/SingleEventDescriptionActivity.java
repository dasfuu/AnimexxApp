package de.meisterfuu.animexx.activitys.events;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.R.id;
import de.meisterfuu.animexx.R.layout;
import de.meisterfuu.animexx.R.menu;
import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.data.events.EventApi;
import de.meisterfuu.animexx.objects.EventDescriptionObject;
import de.meisterfuu.animexx.utils.APIException;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.webkit.WebView;

public class SingleEventDescriptionActivity extends Activity {
	
	public static void getInstance(Context pContext,String pTitle, long pEventID, long pPageID){
		Intent i = new Intent().setClass(pContext, SingleEventDescriptionActivity.class);
	     Bundle args = new Bundle();
	     args.putString("title", pTitle);
	     args.putLong("eventid", pEventID);
	     args.putLong("id", pPageID);
	     i.putExtras(args);
	     pContext.startActivity(i);
	}

	private long mID;
	private String mTitle;
	private WebView mWebView;
	private EventApi mApi;
	private long mEventID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_event_description);
		
		if(this.getIntent().hasExtra("id")){
			Bundle extras = this.getIntent().getExtras();
			mID = extras.getLong("id");
			mEventID = extras.getLong("eventid");
			mTitle = extras.getString("title");
			mApi = new EventApi(this);
		} else {
			finish();
			return;
		}
		
		this.getActionBar().setTitle(mTitle);
		mWebView = (WebView) this.findViewById(R.id.activity_event_desc_html);

		mApi.getEventDescription(new APICallback() {
			
			@Override
			public void onCallback(APIException pError, Object pObject) {
				EventDescriptionObject obj = (EventDescriptionObject) pObject;
				mWebView.loadDataWithBaseURL("https://animexx.de", obj.getHtml(), "text/html", "UTF-8", null);	
			}
		}, mEventID, mID);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.single_event_description, menu);
		return true;
	}

}
