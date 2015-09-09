package de.meisterfuu.animexx.activitys.main;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.Collections;

import de.meisterfuu.animexx.Constants;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.api.Self;
import de.meisterfuu.animexx.api.broker.GCMBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.SingleValueObjects;
import de.meisterfuu.animexx.services.gcm.RegisterGCMIntentService;
import de.meisterfuu.animexx.xmpp.XMPPService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends Activity implements OnClickListener {

    GCMBroker gcm;
    SharedPreferences config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gcm = new GCMBroker(this);
        config = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        if (init()) return;
        setContentView(R.layout.activity_login);

        ImageView img_login = (ImageView) findViewById(R.id.activity_login_image);
        Button btn_login = (Button) findViewById(R.id.activity_login_button);
        btn_login.setOnClickListener(this);
        img_login.setOnClickListener(this);
    }

    private boolean init() {
        boolean result = false;

        if (config.getBoolean("first", true)) {
            config.edit().clear().commit();
            config.edit().putBoolean("first", false).commit();
        }

        //Logged in?
        if (Self.getInstance(this).isLoggedIn()) {
            //Yes? set result TRUE
            result = true;

            //Create Account
            AccountManager manager = AccountManager.get(this);
            final Account acc = new Account(Constants.ACCOUNT_NAME, Constants.ACCOUNT_TYPE);
            if (manager.addAccountExplicitly(acc, null, null)) {
                // Only change sync settings if it did not exist, yet
                ContentResolver.setIsSyncable(acc, Constants.CONTENT_CALENDAR_AUTHORITY, 1);
                ContentResolver.setSyncAutomatically(acc, Constants.CONTENT_CALENDAR_AUTHORITY, true);
                // Sync daily by default
                ContentResolver.addPeriodicSync(acc, Constants.CONTENT_CALENDAR_AUTHORITY, new Bundle(), 24 * 60 * 60);
            }

            //Yes? Start MainActivity
            startActivity(new Intent().setClass(this, MainActivity.class));

            boolean chat = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("xmpp_status", false);
            if (chat) {
                Intent intent = new Intent(this, XMPPService.class);
                //this.startService(intent);
            } else {
                Intent intent = new Intent(this, XMPPService.class);
                this.stopService(intent);
            }


            //Own User data known?
            if (Self.getInstance(this).getUserID() == -1L) {
                //No? Get it!
                Self.getInstance(this).fetchSelf();
            }

            //Check for gcm
            this.initGCM();

            //Finish this activity
            LoginActivity.this.finish();
        }


        return result;
    }

    private void initGCM() {
        //Check for gcm ids and print and delete all
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for(String s: gcm.getActiveIds().getIds()){
//                        Log.e("GCM IDs", s);
//                }
//                gcm.deleteID(gcm.getActiveIds().getIds());
//            }
//        }).start();

        RegisterGCMIntentService.register(this);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent().setClass(v.getContext(), RequestTokenActivity.class));
        LoginActivity.this.finish();
    }


}
