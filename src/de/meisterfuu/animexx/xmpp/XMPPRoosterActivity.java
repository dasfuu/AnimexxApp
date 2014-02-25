package de.meisterfuu.animexx.xmpp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.jivesoftware.smack.RosterEntry;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.R.layout;
import de.meisterfuu.animexx.R.menu;
import de.meisterfuu.animexx.activitys.ens.SingleENSActivity;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class XMPPRoosterActivity extends ListActivity {

	ListView lv;
	ArrayList<String> list;
	ArrayAdapter<String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_xmpprooster);
		lv = this.getListView();
		list = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
		lv.setAdapter(adapter);
		
		Intent intent = new Intent(this, XMPPService.class);
		startService(intent);
		

	 
		final BroadcastReceiver receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (action.equals(XMPPService.NEW_ROOSTER)) {
					Collection<RosterEntry> entries = XMPPService.getInstace().getRoster();
					list.clear();
					list.add("lastCreate "+new Date(XMPPService.lastCreate).toString());
					list.add("lastLogin "+new Date(XMPPService.lastLogin).toString());
					list.add("lastStart "+new Date(XMPPService.lastStart).toString());
					for (RosterEntry r : entries) {
						list.add(r.getUser());
					}
					adapter.notifyDataSetChanged();
				}
			}
		};
		
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(XMPPService.NEW_ROOSTER);
		registerReceiver(receiver, filter);
		
	}
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
		try{
		Collection<RosterEntry> entries = XMPPService.getInstace().getRoster();
		list.clear();
		list.add("lastCreate "+new Date(XMPPService.lastCreate).toString());
		list.add("lastLogin "+new Date(XMPPService.lastLogin).toString());
		list.add("lastStart "+new Date(XMPPService.lastStart).toString());
		for (RosterEntry r : entries) {
			list.add(r.getUser());
		}
		adapter.notifyDataSetChanged();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.xmpprooster, menu);
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		XMPPTestActivity.getInstance(this, list.get(position));
	}



	public static PendingIntent getPendingIntent(Context pContext) {
	     Intent intent = new Intent(pContext, XMPPRoosterActivity.class);
	     return PendingIntent.getActivity(pContext, 0, intent, 0);
	}

}
