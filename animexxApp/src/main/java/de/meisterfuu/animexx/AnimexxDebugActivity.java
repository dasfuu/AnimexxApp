package de.meisterfuu.animexx;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;

import java.util.List;

import de.meisterfuu.animexx.api.xmpp.XMPPApi;
import de.meisterfuu.animexx.objects.xmpp.XMPPMessageObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AnimexxDebugActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_animexx_debug);
        XMPPApi mApi = new XMPPApi(this);
        mApi.getOfflineHistory(null, new Callback<List<XMPPMessageObject>>() {
            @Override
            public void success(List<XMPPMessageObject> xmppMessageObjects, Response response) {
                List<XMPPMessageObject> list = xmppMessageObjects;
                setListAdapter(new ArrayAdapter<XMPPMessageObject>(AnimexxDebugActivity.this, android.R.layout.simple_list_item_1, list));
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.animexx_debug, menu);
        return true;
    }

}
