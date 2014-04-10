package de.meisterfuu.animexx.activitys.main;

import java.util.ArrayList;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.ens.ENSFolderFragment;
import de.meisterfuu.animexx.activitys.ens.NewENSActivity;
import de.meisterfuu.animexx.activitys.events.EventListFragment;
import de.meisterfuu.animexx.activitys.home.HomeObjectFragment;
import de.meisterfuu.animexx.activitys.rpg.RPGListFragment;
import de.meisterfuu.animexx.adapter.ENSFolderSpinnerAdapter;
import de.meisterfuu.animexx.adapter.MainDrawerAdapter;
import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.data.Self;
import de.meisterfuu.animexx.data.ens.ENSApi;
import de.meisterfuu.animexx.objects.ENSFolderObject;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.Helper;
import de.meisterfuu.animexx.utils.Request;
import de.meisterfuu.animexx.xmpp.EmptyRoosterFragment;
import de.meisterfuu.animexx.xmpp.XMPPRoosterFragment;
import de.meisterfuu.animexx.xmpp.XMPPService;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends Activity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private MainDrawerAdapter mAdapter;
	private String mSelected = "ENS";
	private int mLastPosition = -1;
	private long mDesign;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mAdapter = new MainDrawerAdapter(this);

		Request.config = PreferenceManager.getDefaultSharedPreferences(this);
		
		// Set shadow
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		mDrawerList.setAdapter(mAdapter);
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle
		mDrawerToggle = new ActionBarDrawerToggle(
			this, /* host Activity */
			mDrawerLayout, /* DrawerLayout object */
			R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
			R.string.drawer_open, /* "open drawer" description for accessibility */
			R.string.drawer_close /* "close drawer" description for accessibility */
			) {

			public void onDrawerClosed(View view) {
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}


			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		mDesign = 1;
		if(this.getIntent().hasExtra("LANDING") && this.getIntent().getStringExtra("LANDING").equals("CHAT")){
			selectItem(3);
		} else {
			if(mLastPosition != -1){
				selectItem(mLastPosition);
			} else {
				selectItem(0);
			}
		}
	}


	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if(intent.hasExtra("LANDING") && intent.getStringExtra("LANDING").equals("CHAT")){
			selectItem(3);
		} else {
			selectItem(mLastPosition);
		}
	}


	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}


	@Override
	protected void onResume() {
		super.onResume();
		Request.config = PreferenceManager.getDefaultSharedPreferences(this);
		System.out.println("RESUME");
		selectItem(mLastPosition);
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("PAUSE");
	}


	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		System.out.println("RESTART");
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		System.out.println("STOP");
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		inflateMenu(menu);
		return super.onCreateOptionsMenu(menu);
	}


	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		if (drawerOpen) {
			//menu.clear();
		}
		inflateMenu(menu);
		
		if (mSelected.equals("ENS")) {
			// MenuInflater inflater = getMenuInflater();
			// inflater.inflate(R.menu.main, menu);
		} 

		return super.onPrepareOptionsMenu(menu);
	}
	
	public Menu inflateMenu(Menu menu){
		menu.clear();
		if (mSelected.equals("ENS")) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.main_ens, menu);
		} else if (mSelected.equals("RPG")) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.main_rpg, menu);
		} else if (mSelected.equals("EVENT")) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.main_rpg, menu);
		} else if (mSelected.equals("EVENT")) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.main_rpg, menu);
		} else if (mSelected.equals("XMPP")) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.main_xmpp, menu);
	        View v = (View) menu.findItem(R.id.menu_xmpp_switch).getActionView();
			final Switch sw = ((Switch)v.findViewById(R.id.ac_switch_actionbar));			
			sw.setChecked(PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getBoolean("xmpp_status", false));
			sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putBoolean("xmpp_status", isChecked).commit();
					if(isChecked){
						String password = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("xmpp_password", null);
						Intent intent = new Intent(MainActivity.this, XMPPService.class);
						MainActivity.this.startService(intent);
						selectChat();	
					} else {
						Intent intent = new Intent(MainActivity.this, XMPPService.class);
						MainActivity.this.stopService(intent);
						selectChat();
					}
				}
			});
		}

		return menu;
		
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}


		switch (item.getItemId()) {
		case R.id.menu_settings:
			SettingsActivity.getInstance(this);
			return true;
		case R.id.menu_new_ens:
			NewENSActivity.getInstanceBlank(this);
			return true;
		case R.id.menu_xmpp_switch:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerLayout.closeDrawer(mDrawerList);

			selectItem(position);
		}
	}


	private void selectItem(int pPosition) {
		if(mLastPosition == pPosition) return;
		mLastPosition = pPosition;
		if (pPosition == 0) {
			mDesign = 1;
			selectENS();
		} else if(pPosition == 1) {
			selectRPG();
		} else if(pPosition == 2) {
			selectEvent();
		} else if(pPosition == 3) {
			selectChat();
		} else if(pPosition == 4){
			selectHome();
		} else if(pPosition == 5){
			SettingsActivity.getInstance(this);
		}
	}


	private void selectChat() {
		mSelected = "XMPP";
		this.setTitle("Chat");
		Fragment fragment;
		boolean chat = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("xmpp_status", false);
		if(chat){
			fragment = XMPPRoosterFragment.getInstance();
		} else {
			fragment = EmptyRoosterFragment.getInstance();
		}

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "Rooster").commit();
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		invalidateOptionsMenu();
	}


	private void selectENS() {
		mSelected = "ENS";
		this.setTitle("");
//		Fragment fragment = ENSFolderFragment.getInstance(1, ENSApi.TYPE_INBOX, mDesign);
//		FragmentManager fragmentManager = getFragmentManager();
//		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "1_"+ENSApi.TYPE_INBOX).commit();
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		new ENSApi(MainActivity.this).getFolderList(new APICallback() {

			@Override
			public void onCallback(APIException pError, Object pObject) {

				@SuppressWarnings("unchecked")
				final ENSFolderSpinnerAdapter spinnerAdapter = new ENSFolderSpinnerAdapter((ArrayList<ENSFolderObject>) pObject, MainActivity.this);
				OnNavigationListener listener = new OnNavigationListener() {

					@Override
					public boolean onNavigationItemSelected(int itemPosition, long itemId) {
						ENSFolderObject folder = spinnerAdapter.getItem(itemPosition);
						Fragment fragment = ENSFolderFragment.getInstance(folder.getId(), folder.getType(), mDesign);
						FragmentManager fragmentManager = getFragmentManager();
						fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, folder.getId()+"_"+folder.getType()).commit();
						return true;
					}
				};

				MainActivity.this.getActionBar().setListNavigationCallbacks(spinnerAdapter, listener);
			}

		});

		invalidateOptionsMenu();
	}

	private void selectRPG() {
		mSelected = "RPG";
		this.setTitle("RPG");
		Fragment fragment = RPGListFragment.getInstance();
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "RPGList").commit();
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		invalidateOptionsMenu();
	}

	private void selectEvent() {
		mSelected = "EVENT";
		this.setTitle("Events");
		Fragment fragment = EventListFragment.getInstance();
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "EventList").commit();
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		invalidateOptionsMenu();
	}
	

	private void selectHome() {
		mSelected = "HOME";
		this.setTitle("Home");
		Fragment fragment = HomeObjectFragment.getInstance();
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "Home").commit();
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		invalidateOptionsMenu();
	}
	
	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		System.out.println("POSTCREATE");
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}


	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

}
