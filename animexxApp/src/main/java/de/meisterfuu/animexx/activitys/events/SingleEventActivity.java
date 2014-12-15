package de.meisterfuu.animexx.activitys.events;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import com.squareup.otto.Subscribe;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseActivityAB;
import de.meisterfuu.animexx.adapter.SectionsPagerAdapter;
import de.meisterfuu.animexx.api.ApiEvent;
import de.meisterfuu.animexx.api.FinishedLoadingEvent;
import de.meisterfuu.animexx.api.broker.EventBroker;
import de.meisterfuu.animexx.objects.event.EventDescriptionObject;
import de.meisterfuu.animexx.objects.event.EventObject;
import icepick.Icicle;

public class SingleEventActivity extends AnimexxBaseActivityAB {

    @Icicle
    EventObject mEvent;
    @Icicle
    long mID;
    @Icicle
    int idcount;

    private EventBroker mAPI;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    public void onCreate() {
        setContentView(R.layout.activity_single_event);
        // Show the Up button in the action bar.
        mAPI = new EventBroker(this);

        SingleEventActivity.this.getSupportActionBar().setTitle("");

        if (this.getIntent().hasExtra("id")) {
            Bundle extras = this.getIntent().getExtras();
            mID = extras.getLong("id");
        } else {
            finish();
            return;
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        mSectionsPagerAdapter.addFragment(new SectionsPagerAdapter.FragmentHolder("Allgemein", "EventMain", SingleEventFragment.newInstance(), idcount++));
        mSectionsPagerAdapter.notifyDataSetChanged();

    }

    @Override
    public void onCreateWithSavedInstanceState(Bundle savedInstanceState) {
        super.onCreateWithSavedInstanceState(savedInstanceState);
    }

    @Override
    public void onCreateWithoutSavedInstanceState() {
        super.onCreateWithoutSavedInstanceState();
        mAPI.getEvent(mID, this.getCallerID());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        load();
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void retrieveEvent(ApiEvent.EventEvent pEvent) {
        if(mEvent == pEvent.getObj()){
            return;
        }
        mEvent = pEvent.getObj();
        load();
    }

    private void load(){
        this.setTitle(mEvent.getName());
        mSectionsPagerAdapter.addFragmentBeginning(new SectionsPagerAdapter.FragmentHolder("News", "page_news", SingleEventFragmentWeblog.newInstance(mEvent.getId()), idcount++));
        //mSectionsPagerAdapter.addFragment(new SectionsPagerAdapter.FragmentHolder("Programm", "page_program_cal", SingleEventFragmentProgram.newInstance(mEvent.getId(), mEvent.getStartTS().getTime(), mEvent.getDays()), idcount++));
        mSectionsPagerAdapter.addFragment(new SectionsPagerAdapter.FragmentHolder("Programm", "page_program", SingleEventFragmentProgramList.newInstance(mEvent.getId()), idcount++));
        mSectionsPagerAdapter.addFragment(new SectionsPagerAdapter.FragmentHolder("Besucher", "page_attender", SingleEventFragmentAttenderList.newInstance(mEvent.getId()), idcount++));
        for(EventDescriptionObject page: mEvent.getPages()){
            mSectionsPagerAdapter.addFragment(new SectionsPagerAdapter.FragmentHolder(page.getPageName(), "page_"+page.getPageName(), SingleEventDescriptionFragment.newInstance(mEvent.getId(),page.getId()), idcount++));
        }
        mSectionsPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(1, false);
        this.getEventBus().getOtto().post(new FinishedLoadingEvent(this.getCallerID()));
    }

    public EventObject getEvent(){
        return mEvent;
    }

    public static void getInstance(Context pContext, long pID) {
        Intent i = new Intent().setClass(pContext, SingleEventActivity.class);
        Bundle args = new Bundle();
        args.putLong("id", pID);
        i.putExtras(args);
        pContext.startActivity(i);
    }

    public static PendingIntent getPendingIntent(Context pContext, long pID) {
        Intent intent = new Intent(pContext, SingleEventActivity.class);
        Bundle args = new Bundle();
        args.putLong("id", pID);
        intent.putExtras(args);
        return PendingIntent.getActivity(pContext, 0, intent, 0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.single_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
