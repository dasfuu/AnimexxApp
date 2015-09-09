package de.meisterfuu.animexx.activitys.main;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.api.DatabaseHelper;
import de.meisterfuu.animexx.api.web.WebAPI;
import de.meisterfuu.animexx.xmpp.XMPPService;


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

    public static class MyPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            Preference onReset = findPreference("pref_reset");
            onReset.setOnPreferenceClickListener(this);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {

            //Shutdown XMPP
            Intent intentStopChat = new Intent(getActivity(), XMPPService.class);
            getActivity().stopService(intentStopChat);

            //Free WebAPI Memory
            WebAPI.clear();

            //Clear DB
            DatabaseHelper dbhelper = new DatabaseHelper(getActivity());
            dbhelper.deleteDatabase(getActivity());

            //Clear Preferences
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            prefs.edit().clear().commit();

            //Start Login Activity
            Intent intentStart = new Intent(getActivity(), LoginActivity.class);
            getActivity().startActivity(intentStart);

            //Finish Preferences
            getActivity().finish();

            return true;
        }
    }
}


