package de.meisterfuu.animexx.services.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import de.meisterfuu.animexx.api.broker.GCMBroker;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class RegisterGCMIntentService extends IntentService {

    public static void register(Context context) {
        Intent intent = new Intent(context, RegisterGCMIntentService.class);
        context.startService(intent);
    }

    public RegisterGCMIntentService() {
        super("RegisterGCMIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        GCMBroker broker = new GCMBroker(this);
        if(!prefs.getBoolean("gcm_init", false)){
            if(broker.registerGCM()){
                prefs.edit().putBoolean("gcm_init", true).apply();
            } else {
                prefs.edit().putBoolean("gcm_init", false).apply();
                //Todo: Retry?
            }
        }
        broker.activateGCMEvents();
    }


}
