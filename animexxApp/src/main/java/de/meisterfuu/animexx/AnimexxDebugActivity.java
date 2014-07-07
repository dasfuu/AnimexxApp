package de.meisterfuu.animexx;

import java.util.ArrayList;

import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.data.xmpp.XMPPApi;
import de.meisterfuu.animexx.objects.XMPPMessageObject;
import de.meisterfuu.animexx.utils.APIException;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class AnimexxDebugActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_animexx_debug);
		XMPPApi mApi = new XMPPApi(this);
		mApi.getOfflineHistory(null, new APICallback() {
			
			@Override
			public void onCallback(APIException pError, Object pObject) {
				ArrayList<XMPPMessageObject> list = (ArrayList<XMPPMessageObject>) pObject;
				setListAdapter(new ArrayAdapter<XMPPMessageObject>(AnimexxDebugActivity.this, android.R.layout.simple_list_item_1, list));
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
