package de.meisterfuu.animexx.activitys.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import de.meisterfuu.animexx.R;


public class SettingsActivity extends PreferenceActivity {

    public static void getInstance(Context pContext) {
        Intent i = new Intent().setClass(pContext, SettingsActivity.class);
        pContext.startActivity(i);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }


}
