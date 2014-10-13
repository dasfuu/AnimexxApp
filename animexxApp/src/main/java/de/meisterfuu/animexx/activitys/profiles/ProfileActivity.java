package de.meisterfuu.animexx.activitys.profiles;

import java.util.Locale;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.support.v13.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.otto.Subscribe;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseActivity;
import de.meisterfuu.animexx.api.ApiEvent;
import de.meisterfuu.animexx.api.broker.UserBroker;

public class ProfileActivity extends AnimexxBaseActivity{

	long mUserID;
	String mUserName;
	private UserBroker mApi;

	ProfileFragment profileFrag;
	GuestbookListFragment gbFrag;

	public static void getInstance(Context pContext, long pUserID){
		Intent i = new Intent().setClass(pContext, ProfileActivity.class);
		Bundle args = new Bundle();
		args.putLong("id", pUserID);
		i.putExtras(args);
		pContext.startActivity(i);
	}

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

	    Bundle extras = this.getIntent().getExtras();
	    mUserID = extras.getLong("id");
	    mUserName = "Steckbrief";

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

	    mApi = new UserBroker(this);

	    profileFrag = ProfileFragment.newInstance(mUserID);
	    gbFrag = GuestbookListFragment.newInstance(mUserID);
	    mApi.getProfile(mUserID, this.getCallerID());
    }

	@SuppressWarnings("unused")
	@Subscribe
	public void retrieveProfile(ApiEvent.ProfileEvent pEvent) {
		mUserName = pEvent.getObj().getUsername();
		this.getActionBar().setTitle(mUserName);
		profileFrag.onCallback(null, pEvent.getObj());
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	/**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
	        switch (position) {
		        case 0:
			        return profileFrag;
		        case 1:
			        return gbFrag;
		        default:
			        return ProfileFragment.newInstance(mUserID);
	        }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return "Steckbrief";
                case 1:
                    return "Gästebuch";
            }
            return null;
        }
    }



}
