package de.meisterfuu.animexx.activitys.events;

import java.util.ArrayList;

import de.meisterfuu.animexx.adapter.EventAdapter;
import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.data.events.EventApi;
import de.meisterfuu.animexx.objects.EventObject;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.Request;
import android.app.ListFragment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;


public class EventListFragment extends ListFragment  {

	EventApi mAPI;
	ArrayList<EventObject> mList;
	EventAdapter mAdapter;
	
	public static EventListFragment getInstance(){
		EventListFragment result = new EventListFragment();
//	     Bundle args = new Bundle();
//	     args.putLong("mFolderID", pFolderID);
//	     args.putString("mType", pType);
//	     result.setArguments(args);
		return result;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		SingleEventActivity.getInstance(this.getActivity(), id);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		Request.config = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
		mAPI = new EventApi(this.getActivity());
		init();
		
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void init(){
		mList = new ArrayList<EventObject>();
		mAdapter = new EventAdapter(mList, EventListFragment.this.getActivity());
		this.setListAdapter(mAdapter);
		this.getListView().setDivider(null);
		this.getListView().setPadding(15, 0, 15, 0);
		loadEvents();		
	}

	
	private void loadEvents(){
				
		mAPI.getEventList(new APICallback(){

			@SuppressWarnings("unchecked")
			@Override
			public void onCallback(APIException pError, Object pObject) {
				ArrayList<EventObject> list = (ArrayList<EventObject>) pObject;
				mAdapter.addAll(list);			
			}
		}, EventApi.LIST_PARTICIPATING);
		


	}


	
}
