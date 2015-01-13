package de.meisterfuu.animexx.activitys.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;

import de.meisterfuu.animexx.R;


public class SettingsActivity extends ActionBarActivity {

    public static void getInstance(Context pContext) {
        Intent i = new Intent().setClass(pContext, SettingsActivity.class);
        pContext.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class MyPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}


