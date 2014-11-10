package de.meisterfuu.animexx.activitys.rpg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.meisterfuu.animexx.activitys.main.MainActivity;
import de.meisterfuu.animexx.adapter.RPGListAdapter;
import de.meisterfuu.animexx.api.broker.RPGBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.rpg.RPGObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.app.ListFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ListView;


public class RPGListFragment extends ListFragment  {

	RPGBroker mAPI;
	ArrayList<RPGObject> mList;
	RPGListAdapter mAdapter;
    SharedPreferences config;

	public static RPGListFragment getInstance(){
		RPGListFragment result = new RPGListFragment();
//	     Bundle args = new Bundle();
//	     args.putLong("mFolderID", pFolderID);
//	     args.putString("mType", pType);
//	     result.setArguments(args);
		return result;
	}

	public static PendingIntent getPendingIntent(Context pContext) {
		Intent intent = new Intent(pContext, MainActivity.class);
		intent.putExtra("LANDING", "RPG");
		return PendingIntent.getActivity(pContext, 0, intent, 0);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		RPGPostListActivity.getInstance(this.getActivity(), mAdapter.getItem(position).getId());
//		RPGPostListActivity.getInstance(this.getActivity(), mAdapter.getItem(position).getId());
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {

        config = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        mAPI = new RPGBroker(this.getActivity());
		init();
		
		super.onResume();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void init(){
		mList = new ArrayList<RPGObject>();
		mAdapter = new RPGListAdapter(mList, RPGListFragment.this.getActivity());
		RPGListFragment.this.setListAdapter(mAdapter);
		this.getListView().setDivider(null);
		this.getListView().setPadding(15, 0, 15, 0);
		loadRPG();		
	}

	
	private void loadRPG(){
				
		mAPI.getRPGList(new Callback<ReturnObject<List<RPGObject>>>() {
            @Override
            public void success(ReturnObject<List<RPGObject>> listReturnObject, Response response) {
                List<RPGObject> list = listReturnObject.getObj();
                list.removeAll(Collections.singleton(null));
                Collections.sort(list);
                mAdapter.addAll(list);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

	}


	
}
