package de.meisterfuu.animexx.api.broker;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.aidb.MangaObject;
import de.meisterfuu.animexx.objects.aidb.MyMangaObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MangaBroker extends BasicWebBroker {


    //Init

    public MangaBroker(Context pContext) {
        super(pContext);
    }


    //Public Methods

    /**
     * @param pCallback
     */
    public void getMyMangaList(final Callback<List<MyMangaObject>> pCallback) {

        getWebApi().getApi().getMangaOwned(new Callback<ReturnObject<JsonElement>>() {
            @Override
            public void success(ReturnObject<JsonElement> jsonElementReturnObject, Response response) {
                JsonObject obj = jsonElementReturnObject.getObj().getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();
                Gson gson = new Gson();
                List<MyMangaObject> manga = new ArrayList<MyMangaObject>();
                for(Map.Entry<String, JsonElement> entry: entries){
                    manga.add(gson.fromJson(entry.getValue(), MyMangaObject.class));
                }

                pCallback.success(manga, response);
            }

            @Override
            public void failure(RetrofitError error) {
                pCallback.failure(error);
            }
        });

    }

    /**
     * @param pCallback
     */
    public void getMangaDetails(final long mangaId, final Callback<MangaObject> pCallback) {

        getWebApi().getApi().getMangaOwned(new Callback<ReturnObject<JsonElement>>() {
            @Override
            public void success(ReturnObject<JsonElement> jsonElementReturnObject, Response response) {
                JsonObject obj = jsonElementReturnObject.getObj().getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();
                Gson gson = new Gson();

                pCallback.success(gson.fromJson(obj.get(mangaId+""), MangaObject.class), response);
            }

            @Override
            public void failure(RetrofitError error) {
                pCallback.failure(error);
            }
        });

    }

}

