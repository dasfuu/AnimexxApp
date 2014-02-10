package de.meisterfuu.animexx.activitys.main;

import de.meisterfuu.animexx.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;



public class SettingsActivity extends PreferenceActivity {
	
	public static void getInstance(Context pContext){
		Intent i = new Intent().setClass(pContext, SettingsActivity.class);
	    pContext.startActivity(i);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
    
}
