package de.meisterfuu.animexx.api.broker;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.List;

import de.meisterfuu.animexx.api.ApiEvent;
import de.meisterfuu.animexx.api.EventBus;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.rpg.RPGDraftObject;
import de.meisterfuu.animexx.objects.rpg.RPGObject;
import de.meisterfuu.animexx.objects.rpg.RPGPostObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RPGBroker extends BasicWebBroker {


    public RPGBroker(Context pContext) {
        super(pContext);
    }

    //Public Methods

    /**
     * @param pCallback
     */
    public void getRPGList(final Callback<ReturnObject<List<RPGObject>>> pCallback) {
        boolean finished = PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean("rpg_show_finished", true);
        getWebApi().getApi().getRPGList(finished ? 1 : 0, 0, 30, pCallback);
    }

    /**
     */
    public void getRPG(final long pID, final int pCallerID) {
        getWebApi().getApi().getRPG(pID, new Callback<ReturnObject<RPGObject>>() {
            @Override
            public void success(ReturnObject<RPGObject> rpgObjectReturnObject, Response response) {
                EventBus.getBus().getOtto().post(new ApiEvent.ApiProxyEvent(new ApiEvent.RPGEvent().setObj(rpgObjectReturnObject.getObj()).setCallerID(pCallerID)));
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    /**
     */
    public void getRPG(final long pID, final Callback<ReturnObject<RPGObject>> pCallback) {
        getWebApi().getApi().getRPG(pID, pCallback);
    }

    /**
     * @param pCallback
     */
    public void sendRPGDraft(final RPGDraftObject pDraft, final Callback<ReturnObject<Integer>> pCallback) {

        if (pDraft.getAvatarID() >= 0) {
            getWebApi().getApi().sendRPGPosting(pDraft.getRpgID(), pDraft.getText(), pDraft.getCharaID(), pDraft.getKursiv(), pDraft.getInTime(), pDraft.getAvatarID(), pCallback);
        } else {
            getWebApi().getApi().sendRPGPosting(pDraft.getRpgID(), pDraft.getText(), pDraft.getCharaID(), pDraft.getKursiv(), pDraft.getInTime(), pCallback);
        }

    }

    /**
     * @param pCallback
     */
    public void getRPGPostList(final long pID, final long pFromPos, final Callback<ReturnObject<List<RPGPostObject>>> pCallback) {
        getWebApi().getApi().getRPGPostings(pID, pFromPos, 30, pCallback);
    }


}

