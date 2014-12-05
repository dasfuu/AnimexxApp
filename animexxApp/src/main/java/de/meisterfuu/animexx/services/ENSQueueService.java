package de.meisterfuu.animexx.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.List;

import de.meisterfuu.animexx.api.broker.ENSBroker;
import de.meisterfuu.animexx.objects.ens.ENSQueueObject;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ENSQueueService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "de.meisterfuu.animexx.action.GO";

    public static void startAction(Context context) {
        Intent intent = new Intent(context, ENSQueueService.class);
        intent.setAction(ACTION_FOO);
        context.startService(intent);
    }

    public static Intent getStartActionIntent(Context context) {
        Intent intent = new Intent(context, ENSQueueService.class);
        intent.setAction(ACTION_FOO);
        return intent;
    }

    public ENSQueueService() {
        super("ENSQueueService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                handleAction();
            }
        }
    }

    private void handleAction() {
        ENSBroker api = new ENSBroker(this);
        int error_count = 0;
        List<ENSQueueObject> queue = api.getFromQueue();
        if (queue.size() > 0) {
            ENSQueueObject draft = queue.get(0);
            try {
                api.NTsendENS(draft.getDraft());
                api.removeFromQueue(draft);
            } catch (Exception e) {
                e.printStackTrace();
                draft.setTries(draft.getTries() + 1);
                error_count++;
            }
        }

        if (error_count > 0 || queue.size() > 1) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.MINUTE, 1);
            Intent intent = ENSQueueService.getStartActionIntent(this);
            PendingIntent pintent = PendingIntent
                    .getService(this, 0, intent, 0);

            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarm.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pintent);
        }

        api.close();
    }

}
