package de.meisterfuu.animexx.activitys.events;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.adapter.EventAdapter;
import de.meisterfuu.animexx.api.broker.EventBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.event.EventObject;
import de.meisterfuu.animexx.utils.Request;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.app.ListFragment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;


public class EventListFragment extends ListFragment  {

	EventBroker mAPI;
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
		mAPI = new EventBroker(this.getActivity());
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
				
		mAPI.getEventList(new Callback<ReturnObject<List<EventObject>>>() {
			@Override
			public void success(final ReturnObject<List<EventObject>> t, final Response response) {
				mAdapter.addAll(t.getObj());
			}

			@Override
			public void failure(final RetrofitError error) {

			}
		});



	}


	
}
