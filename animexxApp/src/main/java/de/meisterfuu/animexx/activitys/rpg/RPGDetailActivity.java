package de.meisterfuu.animexx.activitys.rpg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.otto.Subscribe;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseActivityAB;
import de.meisterfuu.animexx.activitys.profiles.GuestbookListFragment;
import de.meisterfuu.animexx.activitys.profiles.ProfileFragment;
import de.meisterfuu.animexx.activitys.profiles.ProfilePageEventFragment;
import de.meisterfuu.animexx.activitys.profiles.ProfilePageFanFragment;
import de.meisterfuu.animexx.activitys.profiles.ProfilePageHTMLFragment;
import de.meisterfuu.animexx.activitys.profiles.ProfilePageTableFragment;
import de.meisterfuu.animexx.adapter.SectionsPagerAdapter;
import de.meisterfuu.animexx.api.ApiEvent;
import de.meisterfuu.animexx.api.broker.RPGBroker;
import de.meisterfuu.animexx.api.broker.UserBroker;
import de.meisterfuu.animexx.objects.profile.ProfileBoxObject;
import de.meisterfuu.animexx.objects.profile.ProfileObject;
import de.meisterfuu.animexx.objects.rpg.RPGObject;

public class RPGDetailActivity extends AnimexxBaseActivityAB {

    long mRPGId;
    RPGObject mRPG;
    private RPGBroker mApi;


    public static void getInstance(Context pContext, long mRPGId) {
        Intent i = new Intent().setClass(pContext, RPGDetailActivity.class);
        Bundle args = new Bundle();
        args.putLong("id", mRPGId);
        i.putExtras(args);
        pContext.startActivity(i);
    }

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v13.app.FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v13.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link android.support.v4.view.ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;


    private long idcount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle extras = this.getIntent().getExtras();
        mRPGId = extras.getLong("id");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        //mViewPager.setOffscreenPageLimit(30);
        mApi = new RPGBroker(this);
        mApi.getRPG(mRPGId, this.getCallerID());
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void retrieveProfile(ApiEvent.RPGEvent pEvent) {
        mRPG = pEvent.getObj();

        this.getSupportActionBar().setTitle(mRPG.getName());

        mSectionsPagerAdapter.addFragment(new SectionsPagerAdapter.FragmentHolder("Charaktere", "charas", RPGDetailCharaListFragment.getInstance(), idcount++));
        mSectionsPagerAdapter.addFragment(new SectionsPagerAdapter.FragmentHolder("Allgemein", "main", RPGDetailFragment.newInstance(this, mRPGId), idcount++));


        for(int i = 0; i < mRPG.getDescriptionPages().size(); i++){
            RPGObject.DescriptionPageObject page = mRPG.getDescriptionPages().get(i);
            mSectionsPagerAdapter.addFragment(new SectionsPagerAdapter.FragmentHolder(page.getName(), "customPage"+i, RPGDetailPageFragment.newInstance(mRPGId, i), idcount++));
        }

        mSectionsPagerAdapter.notifyDataSetChanged();

        mViewPager.setCurrentItem(1, false);
    }


    public RPGObject getRPG() {
        return mRPG;
    }

}
