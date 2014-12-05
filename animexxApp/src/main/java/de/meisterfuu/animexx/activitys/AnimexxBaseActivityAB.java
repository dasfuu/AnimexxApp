package de.meisterfuu.animexx.activitys;

import android.graphics.Color;
import android.support.v7.widget.Toolbar;

import de.meisterfuu.animexx.R;

/**
 * Created by Meisterfuu on 26.09.2014.
 */
public class AnimexxBaseActivityAB extends AnimexxBaseActivity {


    Toolbar mToolbar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        // Handle Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setSubtitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    public void setTitle(String pTitle) {
        mToolbar.setTitle(pTitle);
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }


}
