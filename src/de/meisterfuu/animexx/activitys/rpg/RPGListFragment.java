package de.meisterfuu.animexx.activitys.rpg;

import java.util.ArrayList;
import java.util.Collections;

import de.meisterfuu.animexx.adapter.ENSFolderAdapter;
import de.meisterfuu.animexx.adapter.RPGListAdapter;
import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.data.ens.ENSApi;
import de.meisterfuu.animexx.data.rpg.RPGApi;
import de.meisterfuu.animexx.objects.ENSObject;
import de.meisterfuu.animexx.objects.RPGObject;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.Request;
import android.app.ListFragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;


public class RPGListFragment extends ListFragment  {

	RPGApi mAPI;
	ArrayList<RPGObject> mList;
	RPGListAdapter mAdapter;
	
	public static RPGListFragment getInstance(){
		RPGListFragment result = new RPGListFragment();
//	     Bundle args = new Bundle();
//	     args.putLong("mFolderID", pFolderID);
//	     args.putString("mType", pType);
//	     result.setArguments(args);
		return result;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		RPGDetailActivity.getInstance(this.getActivity(), mAdapter.getItem(position).getId());		
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		Request.config = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
		mAPI = new RPGApi(this.getActivity());
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
		loadRPG();		
	}

	
	private void loadRPG(){
				
		mAPI.getFolderList(new APICallback(){

			@SuppressWarnings("unchecked")
			@Override
			public void onCallback(APIException pError, Object pObject) {
				ArrayList<RPGObject> list = (ArrayList<RPGObject>) pObject;
				mAdapter.addAll(list);			
			}
		});

	}


	
}
