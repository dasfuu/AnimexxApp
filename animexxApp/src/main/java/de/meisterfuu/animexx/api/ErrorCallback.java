package de.meisterfuu.animexx.api;

import de.meisterfuu.animexx.api.web.ReturnObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Furuha on 27.06.2015.
 */
public abstract class ErrorCallback<X> implements Callback<ReturnObject<X>> {


    @Override
    public void success(ReturnObject<X> xReturnObject, Response response) {
        if(xReturnObject.isSuccess()){
            onSuccess(xReturnObject, response);
        } else {
            onFailure(null, xReturnObject);
        }
    }

    public abstract void onSuccess(ReturnObject<X> xReturnObject, Response response);

    @Override
    public void failure(RetrofitError error) {
        onFailure(error, null);
    }

    public abstract void onFailure(RetrofitError error, ReturnObject<X> xReturnObject);
}
