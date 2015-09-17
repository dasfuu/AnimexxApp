package de.meisterfuu.animexx.api.broker;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.j256.ormlite.android.apptools.OpenHelperManager;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.meisterfuu.animexx.api.ApiEvent;
import de.meisterfuu.animexx.api.DatabaseHelper;
import de.meisterfuu.animexx.api.ErrorCallback;
import de.meisterfuu.animexx.api.EventBus;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.aidb.EANResultObject;
import de.meisterfuu.animexx.objects.aidb.MangaDbObject;
import de.meisterfuu.animexx.objects.aidb.MangaObject;
import de.meisterfuu.animexx.objects.aidb.MangaSeriesObject;
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
     * @param pCallerID
     */
    public void getMyMangaList(final int pCallerID) {
        getWebApi().getApi().getMangaOwned(new ErrorCallback<JsonObject>() {

            @Override
            public void onSuccess(ReturnObject<JsonObject> jsonObjectReturnObject, Response response) {
                EventBus.getBus().postProxyEvent(new ApiEvent.MyMangaEvent().setObj(parseMyMangaList(jsonObjectReturnObject.getObj())).setCallerID(pCallerID));
            }


            @Override
            public void onFailure(RetrofitError error, ReturnObject<JsonObject> jsonObjectReturnObject) {
                EventBus.getBus().postProxyEvent(new ApiEvent.ErrorEvent().setObj(null).setCallerID(pCallerID));
            }
        });
    }

    private List<MyMangaObject> parseMyMangaList(JsonObject obj){
        Set<Map.Entry<String, JsonElement>> entries = obj.entrySet();
        Gson gson = new Gson();
        List<MyMangaObject> manga = new ArrayList<MyMangaObject>();
        for(Map.Entry<String, JsonElement> entry: entries){
            manga.add(gson.fromJson(entry.getValue(), MyMangaObject.class));
        }
        return manga;
    }

    public void getMangaDetails(final long mangaId, final int pCallerID) {

        getWebApi().getApi().getMangaDetail(mangaId, new ErrorCallback<MangaObject>() {
            @Override
            public void onSuccess(ReturnObject<MangaObject> jsonElementReturnObject, Response response) {
                MangaObject obj = jsonElementReturnObject.getObj();

                EventBus.getBus().postProxyEvent(new ApiEvent.MangaEvent().setObj(obj).setCallerID(pCallerID));
            }

            @Override
            public void onFailure(RetrofitError error, ReturnObject<MangaObject> jsonObjectReturnObject) {
                EventBus.getBus().postProxyEvent(new ApiEvent.ErrorEvent().setObj(null).setCallerID(pCallerID));
            }

        });

    }

    public void resolveEAN(final long pEAN, final int pCallerID) {

        getWebApi().getApi().eanResolve(pEAN, new ErrorCallback<EANResultObject>() {
            @Override
            public void onSuccess(ReturnObject<EANResultObject> jsonElementReturnObject, Response response) {
                EANResultObject result = jsonElementReturnObject.getObj();

                EventBus.getBus().postProxyEvent(new ApiEvent.EANResultEvent().setObj(result).setCallerID(pCallerID));
            }

            @Override
            public void onFailure(RetrofitError error, ReturnObject<EANResultObject> jsonObjectReturnObject) {
                EventBus.getBus().postProxyEvent(new ApiEvent.ErrorEvent().setObj(null).setCallerID(pCallerID));
            }

        });

    }

    public void addManga(final List<Long> pMangaIds, final int pCallerID) {

        getWebApi().getApi().addManga(pMangaIds, new ErrorCallback<JsonObject>() {
            @Override
            public void onSuccess(ReturnObject<JsonObject> jsonElementReturnObject, Response response) {
                EventBus.getBus().postProxyEvent(new ApiEvent.MangaAddedEvent().setObj(pMangaIds).setCallerID(pCallerID));
            }

            @Override
            public void onFailure(RetrofitError error, ReturnObject<JsonObject> jsonObjectReturnObject) {
                EventBus.getBus().postProxyEvent(new ApiEvent.ErrorEvent().setObj(null).setCallerID(pCallerID));
            }

        });

    }

    public void addManga(final List<Long> pMangaIds) {

        getWebApi().getApi().addManga(pMangaIds, new ErrorCallback<JsonObject>() {
            @Override
            public void onSuccess(ReturnObject<JsonObject> jsonElementReturnObject, Response response) {
                EventBus.getBus().getOtto().post(new ApiEvent.MangaAddedEvent().setObj(pMangaIds));
            }

            @Override
            public void onFailure(RetrofitError error, ReturnObject<JsonObject> jsonObjectReturnObject) {
                EventBus.getBus().getOtto().post(new ApiEvent.ErrorEvent().setObj(null));
            }

        });
    }

    public void removeManga(final List<Long> pMangaIds, final int pCallerID) {

        getWebApi().getApi().removeManga(pMangaIds, new ErrorCallback<JsonObject>() {
            @Override
            public void onSuccess(ReturnObject<JsonObject> jsonElementReturnObject, Response response) {
                EventBus.getBus().postProxyEvent(new ApiEvent.MangaDeletedEvent().setObj(pMangaIds).setCallerID(pCallerID));
            }

            @Override
            public void onFailure(RetrofitError error, ReturnObject<JsonObject> jsonObjectReturnObject) {
                EventBus.getBus().postProxyEvent(new ApiEvent.ErrorEvent().setObj(null).setCallerID(pCallerID));
            }

        });
    }


    public void removeManga(final List<Long> pMangaIds) {

        getWebApi().getApi().removeManga(pMangaIds, new ErrorCallback<JsonObject>() {
            @Override
            public void onSuccess(ReturnObject<JsonObject> jsonElementReturnObject, Response response) {
                EventBus.getBus().getOtto().post(new ApiEvent.MangaDeletedEvent().setObj(pMangaIds));
            }

            @Override
            public void onFailure(RetrofitError error, ReturnObject<JsonObject> jsonObjectReturnObject) {
                EventBus.getBus().getOtto().post(new ApiEvent.ErrorEvent().setObj(null));
            }

        });
    }

    public void getMangaSeriesDetails(final long mangaId, final int pCallerID) {

        getWebApi().getApi().getMangaSeriesDetail(mangaId, new ErrorCallback<JsonObject>() {
            @Override
            public void onSuccess(ReturnObject<JsonObject> jsonElementReturnObject, Response response) {
                EventBus.getBus().postProxyEvent(new ApiEvent.MangaSeriesEvent().setObj(parseMangaSeriesDetails(jsonElementReturnObject.getObj(), mangaId)).setCallerID(pCallerID));
            }

            @Override
            public void onFailure(RetrofitError error, ReturnObject<JsonObject> jsonObjectReturnObject) {
                EventBus.getBus().postProxyEvent(new ApiEvent.ErrorEvent().setObj(null).setCallerID(pCallerID));
            }

        });

    }

    private MangaSeriesObject parseMangaSeriesDetails(JsonObject obj, long mangaId){
        Gson gson = new Gson();
        MangaSeriesObject result = gson.fromJson(obj.get("" + mangaId), MangaSeriesObject.class);
        return result;
    }

    public void getAbos(final Callback<List<MyMangaObject>> pCallback) {
        getWebApi().getApi().getAbos(new Callback<ReturnObject<JsonObject>>() {
            @Override
            public void success(ReturnObject<JsonObject> jsonObjectReturnObject, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public List<MangaDbObject> getVolumes(long pSeriesId) {
        List<MangaDbObject> result = getHelper().getMangaDbDataDao().queryForEq("seriedId", pSeriesId);
        return result;
    }


    private DatabaseHelper databaseHelper = null;

    public void close() {
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }

    private DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper =
                    OpenHelperManager.getHelper(getContext(), DatabaseHelper.class);
        }
        return databaseHelper;
    }

    public void fetch(long pSeriesId){
        MangaSeriesObject series = parseMangaSeriesDetails(getWebApi().getApi().getMangaSeriesDetail(pSeriesId).getObj(), pSeriesId);
        List<MyMangaObject> myManga = parseMyMangaList(getWebApi().getApi().getMangaOwned().getObj());

        HashSet<Long> inPossession = new HashSet<>();
        for(MyMangaObject manga: myManga){
            if(manga.getMangaId() == pSeriesId){
                MyMangaObject mMyManga = manga;
                for(MyMangaObject.Volume myVolume: mMyManga.getVolumes()){
                    inPossession.add(myVolume.getMangaId());
                }
                break;
            }
        }

        MangaDbObject temp;
        for(MangaSeriesObject.Volume volume: series.getVolumes()){
            temp = new MangaDbObject();
            temp.setName("Band "+volume.getVolume());
            if(volume.getName() != null){
                temp.setName(volume.getName());
            }
            temp.setSeriedId(pSeriesId);
            temp.setSeriesName(series.getName());
            temp.setInPossession(false);
            temp.setVolume(volume.getVolume());
            temp.setMangaId(volume.getMangaId());

            if(inPossession.contains(temp.getMangaId())){
                temp.setInPossession(true);
            }

            getHelper().getMangaDbDataDao().createOrUpdate(temp);
        }



    }
}

