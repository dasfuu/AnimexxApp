package de.meisterfuu.animexx.activitys.profiles;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.otto.Subscribe;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseActivityAB;
import de.meisterfuu.animexx.adapter.SectionsPagerAdapter;
import de.meisterfuu.animexx.api.ApiEvent;
import de.meisterfuu.animexx.api.broker.UserBroker;
import de.meisterfuu.animexx.objects.profile.ProfileBoxObject;
import de.meisterfuu.animexx.objects.profile.ProfileObject;

public class ProfileActivity extends AnimexxBaseActivityAB {

    long mUserID;
    String mUserName;
    private UserBroker mApi;

    ProfileFragment profileFrag;
    GuestbookListFragment gbFrag;
    ProfileObject mProfile;

    public static void getInstance(Context pContext, long pUserID) {
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


    private long idcount = 0;

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
        //mViewPager.setOffscreenPageLimit(30);
        mApi = new UserBroker(this);
        profileFrag = ProfileFragment.newInstance(mUserID);
        mSectionsPagerAdapter.addFragment(new SectionsPagerAdapter.FragmentHolder("Steckbrief", "MAIN", profileFrag, idcount++));
        mSectionsPagerAdapter.notifyDataSetChanged();
        mApi.getProfile(mUserID, this.getCallerID());
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void retrieveProfile(ApiEvent.ProfileEvent pEvent) {
        mUserName = pEvent.getObj().getUsername();
        mProfile = pEvent.getObj();

        this.getSupportActionBar().setTitle(mUserName);

        if (!pEvent.getObj().isProfileShared()) {


        }

        profileFrag.onCallback(pEvent.getObj());
        gbFrag = GuestbookListFragment.newInstance(mUserID);
        mSectionsPagerAdapter.addFragmentBeginning(new SectionsPagerAdapter.FragmentHolder("Gästebuch", "GB", gbFrag, idcount++));
        mSectionsPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(1);

        for (ProfileBoxObject entry : pEvent.getObj().getBoxes()) {
            if (entry.getType().equals(ProfileBoxObject.TYPE_BESCHREIBUNG)) {
                mSectionsPagerAdapter.addFragment(new SectionsPagerAdapter.FragmentHolder(entry.getTitle(), "box_sb", ProfilePageHTMLFragment.getInstance(this, mUserID, entry.getId()), idcount++));
            } else if (entry.getType().equals(ProfileBoxObject.TYPE_EVENTS)) {
                mSectionsPagerAdapter.addFragment(new SectionsPagerAdapter.FragmentHolder(entry.getTitle(), "box_event", ProfilePageEventFragment.getInstance(this, mUserID, entry.getId()), idcount++));
            } else if (entry.getType().equals(ProfileBoxObject.TYPE_EIGENSCHAFTEN)) {
                mSectionsPagerAdapter.addFragment(new SectionsPagerAdapter.FragmentHolder(entry.getTitle(), "box_eig", ProfilePageTableFragment.getInstance(this, mUserID, entry.getId()), idcount++));
            } else if (entry.getType().equals(ProfileBoxObject.TYPE_FAVS)) {
                mSectionsPagerAdapter.addFragment(new SectionsPagerAdapter.FragmentHolder(entry.getTitle(), "box_fan", ProfilePageFanFragment.getInstance(this, mUserID, entry.getId()), idcount++));
            }
        }
        mSectionsPagerAdapter.notifyDataSetChanged();
    }

    public long getUserId() {
        return mUserID;
    }

    public ProfileObject getProfile() {
        return mProfile;
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


}
