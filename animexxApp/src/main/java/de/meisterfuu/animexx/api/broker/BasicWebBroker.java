package de.meisterfuu.animexx.api.broker;

import android.content.Context;

import de.meisterfuu.animexx.api.web.WebAPI;

/**
 * Created by Meisterfuu on 13.10.2014.
 */
public abstract class BasicWebBroker {

    private final WebAPI mWebApi;
    private final Context mContext;

    public BasicWebBroker(Context pContext) {
        this.mContext = pContext.getApplicationContext();
        this.mWebApi = new WebAPI(mContext);
        this.getWebApi().refresh(mContext);
    }

    public WebAPI getWebApi() {
        return mWebApi;
    }

    public Context getContext() {
        return mContext;
    }

}
