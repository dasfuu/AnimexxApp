package de.meisterfuu.animexx.activitys.events;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.api.broker.EventBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.event.EventDescriptionObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SingleEventDescriptionActivity extends Activity {

    public static void getInstance(Context pContext, String pTitle, long pEventID, long pPageID) {
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
    private EventBroker mApi;
    private long mEventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_event_description);

        if (this.getIntent().hasExtra("id")) {
            Bundle extras = this.getIntent().getExtras();
            mID = extras.getLong("id");
            mEventID = extras.getLong("eventid");
            mTitle = extras.getString("title");
            mApi = new EventBroker(this);
        } else {
            finish();
            return;
        }

        this.getActionBar().setTitle(mTitle);
        mWebView = (WebView) this.findViewById(R.id.activity_event_desc_html);

        mApi.getEventDescription(mID, mEventID, new Callback<ReturnObject<EventDescriptionObject>>() {
            @Override
            public void success(final ReturnObject<EventDescriptionObject> t, final Response response) {
                EventDescriptionObject obj = t.getObj();
                mWebView.loadDataWithBaseURL("https://animexx.de", obj.getHtml(), "text/html", "UTF-8", null);
            }

            @Override
            public void failure(final RetrofitError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.single_event_description, menu);
        return true;
    }

}
