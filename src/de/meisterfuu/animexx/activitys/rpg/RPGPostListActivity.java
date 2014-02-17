package de.meisterfuu.animexx.activitys.rpg;

import java.util.ArrayList;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.adapter.RPGPostListAdapter;
import de.meisterfuu.animexx.adapter.RPGSpinnerAdapter;
import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.data.rpg.RPGApi;
import de.meisterfuu.animexx.objects.RPGObject;
import de.meisterfuu.animexx.objects.RPGPostObject;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.Request;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ActionBar.OnNavigationListener;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class RPGPostListActivity extends ListActivity {
	
	public static void getInstance(Context pContext, long pID){
		Intent i = new Intent().setClass(pContext, RPGPostListActivity.class);
	     Bundle args = new Bundle();
	     args.putLong("id", pID);
	     i.putExtras(args);
	     pContext.startActivity(i);
	}
	
	RPGApi mAPI;
	ArrayList<RPGPostObject> mList;
	RPGPostListAdapter mAdapter;
	RPGObject mRPG;
	long mRPGID;
	long mCharaID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_rpgpost_list);
		mAPI = new RPGApi(this);	
		this.getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rpgpost_list, menu);
		return true;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		Request.config = PreferenceManager.getDefaultSharedPreferences(this);
		mAPI = new RPGApi(this);	
		super.onResume();
	}
	
	private void init() {
		Bundle extras = this.getIntent().getExtras();
		mRPGID = extras.getLong("id");
		
		mList = new ArrayList<RPGPostObject>();
		mAdapter = new RPGPostListAdapter(mList, this, mRPGID);
		this.getListView().setAdapter(mAdapter);
		
		mAPI.getRPG(mRPGID, new APICallback(){

			@Override
			public void onCallback(APIException pError, Object pObject) {
				mRPG = (RPGObject) pObject;
				RPGPostListActivity.this.getActionBar().setTitle(mRPG.getName());				
				final RPGSpinnerAdapter spinnerAdapter = new RPGSpinnerAdapter(mRPG.getPlayer(), mRPGID, RPGPostListActivity.this);
				OnNavigationListener listener = new OnNavigationListener() {

					@Override
					public boolean onNavigationItemSelected(int itemPosition, long itemId) {
						mCharaID = itemId;
						return true;
					}
				};

				RPGPostListActivity.this.getActionBar().setListNavigationCallbacks(spinnerAdapter, listener);
				
				loadPosts();
			}
		});
	}

	private void loadPosts() {
		mAPI.getRPGPostList(mRPGID, mRPG.getPostCount()-29, new APICallback(){

			@SuppressWarnings("unchecked")
			@Override
			public void onCallback(APIException pError, Object pObject) {
				ArrayList<RPGPostObject> list = (ArrayList<RPGPostObject>) pObject;
				mAdapter.addAll(list);		
			}
		});
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

}
