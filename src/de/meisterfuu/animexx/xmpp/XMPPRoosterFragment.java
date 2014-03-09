package de.meisterfuu.animexx.xmpp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.jivesoftware.smack.RosterEntry;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.R.layout;
import de.meisterfuu.animexx.R.menu;
import de.meisterfuu.animexx.activitys.ens.SingleENSActivity;
import de.meisterfuu.animexx.activitys.rpg.RPGListFragment;
import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.app.ListFragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class XMPPRoosterFragment extends ListFragment {

	ArrayList<String> list;
	ArrayAdapter<String> adapter;
	BroadcastReceiver receiver;
	
	
	public static XMPPRoosterFragment getInstance(){
		XMPPRoosterFragment result = new XMPPRoosterFragment();
//	     Bundle args = new Bundle();
//	     args.putLong("mFolderID", pFolderID);
//	     args.putString("mType", pType);
//	     result.setArguments(args);
		return result;
	}
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		list = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1, list);
		setListAdapter(adapter);
	}
	
	
	
	@Override
	public void onPause() {
		super.onPause();
		this.getActivity().unregisterReceiver(receiver);
	}



	@Override
	public void onResume() {
		super.onResume();
		
		
		
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (action.equals(XMPPService.NEW_ROOSTER)) {
					try{
						Collection<RosterEntry> entries = XMPPService.getInstace().getRoster();
						list.clear();
	//					list.add("lastCreate "+new Date(XMPPService.lastCreate).toString());
	//					list.add("lastLogin "+new Date(XMPPService.lastLogin).toString());
	//					list.add("lastStart "+new Date(XMPPService.lastStart).toString());
						for (RosterEntry r : entries) {
							list.add(r.getUser());
						}
						adapter.notifyDataSetChanged();
					} catch (Exception e){
						e.printStackTrace();
					}
				}
			}
		};
		
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(XMPPService.NEW_ROOSTER);
		this.getActivity().registerReceiver(receiver, filter);
		
		try{
			Collection<RosterEntry> entries = XMPPService.getInstace().getRoster();
			list.clear();
	//		list.add("lastCreate "+new Date(XMPPService.lastCreate).toString());
	//		list.add("lastLogin "+new Date(XMPPService.lastLogin).toString());
	//		list.add("lastStart "+new Date(XMPPService.lastStart).toString());
			for (RosterEntry r : entries) {
				list.add(r.getUser());
			}
			adapter.notifyDataSetChanged();
		} catch (Exception e){
			e.printStackTrace();
		}
	}




	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		XMPPChatActivity.getInstance(this.getActivity(), list.get(position));
	}



	public static PendingIntent getPendingIntent(Context pContext) {
	     Intent intent = new Intent(pContext, XMPPRoosterFragment.class);
	     return PendingIntent.getActivity(pContext, 0, intent, 0);
	}

}
