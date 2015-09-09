package de.meisterfuu.animexx.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import de.meisterfuu.animexx.api.EventBus;
import de.meisterfuu.animexx.api.Events;
import de.meisterfuu.animexx.api.broker.MangaBroker;

/**
 * Created by Furuha on 08.09.2015.
 */
public class MangaFetchService extends IntentService {

    public MangaFetchService() {
        super("MangaFetchService");
    }

    public static void startAction(Context context, long mSeriesId) {
        Intent intent = new Intent(context, MangaFetchService.class);
        intent.putExtra("seriesId", mSeriesId);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            long seriesId = intent.getLongExtra("seriesId", -1);
            fetch(seriesId);
        }
    }

    private void fetch(long seriesId) {
        Log.e("MangaFetchService", "ICH LEBE!!!!");
        MangaBroker mApi = new MangaBroker(this);
        mApi.fetch(seriesId);

        final Events.MangaSeriesFetched event = new Events.MangaSeriesFetched();
        event.setSeriesId(seriesId);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                EventBus.getBus().getOtto().post(event);
            }
        });
    }

}
