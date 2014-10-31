package de.meisterfuu.animexx.api.broker;

import java.util.List;

import android.content.Context;

import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.rpg.RPGDraftObject;
import de.meisterfuu.animexx.objects.rpg.RPGObject;
import de.meisterfuu.animexx.objects.rpg.RPGPostObject;
import retrofit.Callback;

public class RPGBroker extends BasicWebBroker{


    public RPGBroker(Context pContext) {
        super(pContext);
    }

	//Public Methods
	
	/**
     * @param pCallback
     */
	public void getRPGList(final Callback<ReturnObject<List<RPGObject>>> pCallback){
        getWebApi().getApi().getRPGList(1,0,30, pCallback);
	}
	
	/**
     * @param pCallback
     */
	public void getRPG(final long pID, final Callback<ReturnObject<RPGObject>> pCallback){
        getWebApi().getApi().getRPG(pID, pCallback);
	}
	
	/**
	 * @param pCallback
	 */
	public void sendRPGDraft(final RPGDraftObject pDraft, final Callback<ReturnObject<Integer>> pCallback){

        if(pDraft.getAvatarID() >= 0){
            getWebApi().getApi().sendRPGPosting(pDraft.getRpgID(), pDraft.getText(), pDraft.getCharaID(), pDraft.getKursiv(), pDraft.getInTime(), pDraft.getAvatarID(), pCallback);
        } else {
            getWebApi().getApi().sendRPGPosting(pDraft.getRpgID(), pDraft.getText(), pDraft.getCharaID(), pDraft.getKursiv(), pDraft.getInTime(), pCallback);
        }

	}
	
	/**
     * @param pCallback
     */
	public void getRPGPostList(final long pID, final long pFromPos, final Callback<ReturnObject<List<RPGPostObject>>> pCallback){
        getWebApi().getApi().getRPGPostings(pID, pFromPos, 30, pCallback);
	}

	
}
		
