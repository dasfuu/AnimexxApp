package de.meisterfuu.animexx.api.profile;

import android.content.Context;
import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.api.APICallback;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.api.web.WebAPI;
import de.meisterfuu.animexx.objects.profile.GBDraftObject;
import de.meisterfuu.animexx.objects.profile.GBEntryObject;
import de.meisterfuu.animexx.objects.profile.GBInfoObject;
import de.meisterfuu.animexx.objects.profile.GBListObject;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.PostBodyFactory;
import de.meisterfuu.animexx.utils.Request;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Furuha on 14.07.2014.
 */
public class GBBroker {


	private final WebAPI mWebApi;
	private final Context mContext;



	//Init

	public GBBroker(Context pContext){
		this.mContext = pContext;
		this.mWebApi = new WebAPI(mContext);
	}


	//Public Methods

	public void getGBList(final long pUserID, final int pPage, final Callback<ReturnObject<GBListObject>> pCallback) {
		mWebApi.getApi().getGBEntries(pUserID,pPage, pCallback);
	}

	public void getGBInfo(final long pUserID, final Callback<ReturnObject<List<GBInfoObject>>> pCallback) {
		mWebApi.getApi().getGBInfo(pUserID, pCallback);
	}

	public void postGBEntry(final GBDraftObject pDraft, final Callback<ReturnObject<Boolean>> pCallback) {
		mWebApi.getApi().sendGBEntry(pDraft.getRecipient().getId(),pDraft.getText(), pDraft.getAvatar(), pCallback);
	}


}
