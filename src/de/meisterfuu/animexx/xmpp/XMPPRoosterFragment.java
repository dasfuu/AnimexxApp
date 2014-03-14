package de.meisterfuu.animexx.xmpp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.jivesoftware.smack.RosterEntry;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.R.layout;
import de.meisterfuu.animexx.R.menu;
import de.meisterfuu.animexx.activitys.ens.SingleENSActivity;
import de.meisterfuu.animexx.activitys.main.MainActivity;
import de.meisterfuu.animexx.activitys.rpg.RPGListFragment;
import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.data.xmpp.XMPPApi;
import de.meisterfuu.animexx.objects.XMPPRoosterObject;
import de.meisterfuu.animexx.utils.APIException;
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

	ArrayList<XMPPRoosterObject> list;
	ArrayAdapter<XMPPRoosterObject> adapter;
	BroadcastReceiver receiver;
	XMPPApi mApi;
	
	
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
		
		list = new ArrayList<XMPPRoosterObject>();
		adapter = new ArrayAdapter<XMPPRoosterObject>(this.getActivity(),android.R.layout.simple_list_item_1, list);
		setListAdapter(adapter);
	}
	
	
	
	@Override
	public void onPause() {
		super.onPause();
		mApi.close();
		this.getActivity().unregisterReceiver(receiver);
	}



	@Override
	public void onResume() {
		super.onResume();
		mApi = new XMPPApi(this.getActivity());
		
		
		receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (action.equals(XMPPService.NEW_ROOSTER)) {
					getRooster();
				}
			}
		};
		
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(XMPPService.NEW_ROOSTER);
		this.getActivity().registerReceiver(receiver, filter);
		getRooster();
	}

	
	private void getRooster(){
		mApi.getRooster(new APICallback() {
			
			@Override
			public void onCallback(APIException pError, Object pObject) {
				ArrayList<XMPPRoosterObject> temp = (ArrayList<XMPPRoosterObject>) pObject;
				list.clear();
				list.addAll(temp);
				adapter.notifyDataSetChanged();
			}
			
		});
	}



	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		XMPPChatActivity.getInstance(this.getActivity(), list.get(position).getJid());
	}



	public static PendingIntent getPendingIntent(Context pContext) {
	     Intent intent = new Intent(pContext, MainActivity.class);
	     intent.putExtra("LANDING", "CHAT");
	     return PendingIntent.getActivity(pContext, 0, intent, 0);
	}

}
