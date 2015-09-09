package de.meisterfuu.animexx;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;

import java.util.List;

import de.meisterfuu.animexx.api.broker.MangaBroker;
import de.meisterfuu.animexx.api.xmpp.XMPPApi;
import de.meisterfuu.animexx.objects.aidb.MyMangaObject;
import de.meisterfuu.animexx.objects.xmpp.XMPPMessageObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AnimexxDebugActivity extends ListActivity {

    public static void getInstance(Context pContext) {
        Intent i = new Intent().setClass(pContext, AnimexxDebugActivity.class);
        pContext.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_animexx_debug);
        MangaBroker mApi = new MangaBroker(this);
        mApi.getAbos(new Callback<List<MyMangaObject>>() {
            @Override
            public void success(List<MyMangaObject> mangas, Response response) {
                List<MyMangaObject> list = mangas;
                setListAdapter(new ArrayAdapter<MyMangaObject>(AnimexxDebugActivity.this, android.R.layout.simple_list_item_1, list));
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.animexx_debug, menu);
        return true;
    }

}
