package de.meisterfuu.animexx.activitys.main;

import java.util.ArrayList;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.ens.ENSFolderFragment;
import de.meisterfuu.animexx.adapter.MainDrawerAdapter;
import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.data.ens.ENSApi;
import de.meisterfuu.animexx.objects.ENSFolderObject;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.Request;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends Activity {

	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	private MainDrawerAdapter mAdapter;
	private String mSelected = "ENS";
	private int mLastPosition = -1;


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

		if (savedInstanceState == null) {

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
		System.out.println("RESUME");
		
		selectItem(0);
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		System.out.println("PAUS");
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
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}


	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		if (drawerOpen) {
			//menu.clear();
		}
		if (mSelected.equals("ENS")) {
			// MenuInflater inflater = getMenuInflater();
			// inflater.inflate(R.menu.main, menu);
		}

		return super.onPrepareOptionsMenu(menu);
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		switch (item.getItemId()) {
		case 2:
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
		if (pPosition == 0) {
			selectENS();
		}
		mLastPosition = pPosition;
	}


	private void selectENS() {
		mSelected = "ENS";
		this.setTitle("ENS");
		Fragment fragment = ENSFolderFragment.getInstance(1, ENSApi.TYPE_INBOX);
		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.content_frame, fragment, "1_"+ENSApi.TYPE_INBOX).commit();
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		new ENSApi(MainActivity.this).getFolderList(new APICallback() {

			@Override
			public void onCallback(APIException pError, Object pObject) {

				@SuppressWarnings("unchecked")
				final ArrayAdapter<ENSFolderObject> spinnerAdapter = new ArrayAdapter<ENSFolderObject>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, (ArrayList<ENSFolderObject>) pObject);
				OnNavigationListener listener = new OnNavigationListener() {

					@Override
					public boolean onNavigationItemSelected(int itemPosition, long itemId) {
						ENSFolderObject folder = spinnerAdapter.getItem(itemPosition);
						Fragment fragment = ENSFolderFragment.getInstance(folder.getId(), folder.getType());
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