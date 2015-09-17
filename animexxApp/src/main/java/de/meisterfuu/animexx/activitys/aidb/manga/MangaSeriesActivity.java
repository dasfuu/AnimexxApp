package de.meisterfuu.animexx.activitys.aidb.manga;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.otto.Subscribe;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseActivityAB;
import de.meisterfuu.animexx.adapter.SectionsPagerAdapter;
import de.meisterfuu.animexx.api.ApiEvent;
import de.meisterfuu.animexx.api.EventBus;
import de.meisterfuu.animexx.api.broker.MangaBroker;
import de.meisterfuu.animexx.objects.aidb.MangaSeriesObject;
import de.meisterfuu.animexx.objects.aidb.MyMangaObject;
import de.meisterfuu.animexx.services.MangaFetchService;

public class MangaSeriesActivity extends AnimexxBaseActivityAB {


    public static void getInstance(Context pContext, long pMangaId) {
        Intent i = new Intent().setClass(pContext, MangaSeriesActivity.class);
        Bundle args = new Bundle();
        args.putLong("id", pMangaId);
        i.putExtras(args);
        pContext.startActivity(i);
    }

    private long mID;

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    private long idcount = 0;
    private MangaBroker mApi;
    private MangaSeriesObject mSeries;
    private MyMangaObject mMyManga;

    private MangaVolumeListFragment ownFragment;
    private MangaVolumeListFragment missingFragment;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_series);

        Bundle extras = this.getIntent().getExtras();
        mID = extras.getLong("id");
        MangaFetchService.startAction(this, mID);
        handler = new Handler();

        mApi = new MangaBroker(this.getApplicationContext());

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mApi.getMangaSeriesDetails(mID, this.getCallerID());
//        mApi.getMyMangaList(this.getCallerID());
    }

    @Subscribe
    public void onReceiveSeries(ApiEvent.MangaSeriesEvent event){
        mSeries = event.getObj();
        this.setTitle(mSeries.getName());

        //Create Detail Tab
        mSectionsPagerAdapter.addFragment(new SectionsPagerAdapter.FragmentHolder("Im Besitz", "inPossession", MangaSeriesDetailFragment.newInstance(mID), idcount++));

        //Create Volumes Tabs

        ownFragment = MangaVolumeListFragment.newInstance(mID, MangaVolumeListFragment.MODE_OWN);
        mSectionsPagerAdapter.addFragment(new SectionsPagerAdapter.FragmentHolder("Im Besitz", "inPossession", ownFragment, idcount++));
        missingFragment = MangaVolumeListFragment.newInstance(mID, MangaVolumeListFragment.MODE_MISSING);
        mSectionsPagerAdapter.addFragment(new SectionsPagerAdapter.FragmentHolder("Fehlend", "missing", missingFragment, idcount++));
        mSectionsPagerAdapter.addFragment(new SectionsPagerAdapter.FragmentHolder("Alle", "all", MangaVolumeListFragment.newInstance(mID, MangaVolumeListFragment.MODE_ALL), idcount++));

        mSectionsPagerAdapter.notifyDataSetChanged();

//        mViewPager.setCurrentItem(1, false);

    }

    public long lastUpdate;

    public void onChange(){
        lastUpdate = System.currentTimeMillis();
        handler.postDelayed(new DelayedDismiss(), 5000);
    }

    @Subscribe
    public void onDelete(ApiEvent.MangaDeletedEvent event){
        //EventBus.getBus().getOtto().post(new ApiEvent.MangaUpdateEvent());
        onChange();
    }

    @Subscribe
    public void onAdd(ApiEvent.MangaAddedEvent event){
        //EventBus.getBus().getOtto().post(new ApiEvent.MangaUpdateEvent());
        onChange();
    }

    private class DelayedDismiss implements Runnable {

        long time;

        public DelayedDismiss(){
            time = System.currentTimeMillis();
        }

        @Override
        public void run() {
            if(time >= lastUpdate){
                MangaFetchService.startAction(MangaSeriesActivity.this, mID);
            } else {
                Log.d("MangaFetchService", "Update dissmissed");
            }
        }
    }

//    @Subscribe
//    public void onReceiveMyManga(ApiEvent.MyMangaEvent event){
//        for(MyMangaObject manga: event.getObj()){
//            if(manga.getMangaId() == mID){
//                mMyManga = manga;
//                Toast.makeText(this, "MANGA GEFUNDEN", Toast.LENGTH_SHORT).show();
//                break;
//            }
//        }
//
//        if(mMyManga != null){
//            mMyManga.getVolumes();
//
//
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manga_series, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public MangaSeriesObject getSeries() {
        return mSeries;
    }
}
