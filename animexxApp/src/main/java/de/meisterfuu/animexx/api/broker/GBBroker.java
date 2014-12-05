package de.meisterfuu.animexx.api.broker;

import android.content.Context;

import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.profile.GBDraftObject;
import de.meisterfuu.animexx.objects.profile.GBInfoObject;
import de.meisterfuu.animexx.objects.profile.GBListObject;
import retrofit.Callback;

/**
 * Created by Meisterfuu on 14.07.2014.
 */
public class GBBroker extends BasicWebBroker {


    //Init

    public GBBroker(Context pContext) {
        super(pContext);
    }


    //Public Methods

    public void getGBList(final long pUserID, final int pPage, final Callback<ReturnObject<GBListObject>> pCallback) {
        getWebApi().getApi().getGBEntries(pUserID, pPage, pCallback);
    }

    public void getGBInfo(final long pUserID, final Callback<ReturnObject<GBInfoObject>> pCallback) {
        getWebApi().getApi().getGBInfo(pUserID, pCallback);
    }

    public void postGBEntry(final GBDraftObject pDraft, final Callback<ReturnObject<Boolean>> pCallback) {
        getWebApi().getApi().sendGBEntry(pDraft.getRecipient().getId(), pDraft.getText(), pDraft.getAvatar(), pCallback);
    }


}
