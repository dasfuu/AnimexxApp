package de.meisterfuu.animexx.api.web.oauth;

import android.content.Context;

import retrofit.RequestInterceptor;

/**
 * Created by Meisterfuu on 28.09.2014.
 */
public class InterceptorSigner implements RequestInterceptor {

    AccessToken mToken;

    public InterceptorSigner(Context pContext) {
        refresh(pContext);
    }

    @Override
    public void intercept(final RequestFacade request) {
        request.addHeader("Accept", "application/json;versions=1");
        request.addHeader("Authorization", mToken.getTokenType() + " " + mToken.getAccessToken());
        request.addQueryParam("api", "3");
    }

    public void refresh(Context pContext) {
        mToken = AccessToken.getToken(pContext);
    }
}
