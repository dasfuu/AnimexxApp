package de.meisterfuu.animexx.api.broker;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.api.ApiEvent;
import de.meisterfuu.animexx.api.EventBus;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.SingleValueObjects;
import de.meisterfuu.animexx.objects.UserObject;
import de.meisterfuu.animexx.objects.profile.ProfileBoxObject;
import de.meisterfuu.animexx.objects.profile.ProfileObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class UserBroker extends BasicWebBroker {


    //Init

    public UserBroker(Context pContext) {
        super(pContext);
    }


    //Public Methods

    /**
     * @param pUserID
     */
    public void getProfile(final long pUserID, final int pCallerID) {
        getWebApi().getApi().getProfile(pUserID, 1, 1, 1, 1, 1, new Callback<ReturnObject<ProfileObject>>() {
            @Override
            public void success(final ReturnObject<ProfileObject> t, final Response response) {
//				ApiEvent.ProfileEvent event = ;
                EventBus.getBus().getOtto().post(new ApiEvent.ApiProxyEvent(new ApiEvent.ProfileEvent().setObj(t.getObj()).setCallerID(pCallerID)));
            }

            @Override
            public void failure(final RetrofitError error) {
//				EventBus.getBus().getOtto().post(new EventBus.ApiEvent<ProfileObject>(t.getObj(), pCallerID));
            }
        });
    }


    public void getProfileBox(final String pBoxID, final long pID, final Callback<ReturnObject<ProfileBoxObject>> pCallback) {
        getWebApi().getApi().getProfileBox(pID, pBoxID, pCallback);
    }


    /**
     * @param pCallback
     */
    public void getMe(final Callback<ReturnObject<UserObject>> pCallback) {
        getWebApi().getApi().getMe(pCallback);
    }


    /**
     * @param pSearchString
     * @param pCallback     (ArrayList<UserSearchResultObject>)
     */
    public void searchUserByName(final String pSearchString, final Callback<ReturnObject<List<UserObject>>> pCallback) {
        getWebApi().getApi().searchUser(pSearchString, pCallback);
    }

    /**
     * @param pUsernameList
     * @param pCallback     (ArrayList<UserSearchResultObject>)
     */
    public void getIDs(final ArrayList<String> pUsernameList, final Callback<ReturnObject<SingleValueObjects.UserListObject>> pCallback) {
        getWebApi().getApi().getUserId(pUsernameList, pCallback);
    }

    /**
     * @param pUsernameList
     * @return
     */
    public List<UserObject> NTgetIDs(final ArrayList<String> pUsernameList) {
        List<UserObject> temp;
        try {
            temp = getWebApi().getApi().getUserId(pUsernameList).getObj().getUser();
        } catch (RetrofitError e) {
            temp = new ArrayList<UserObject>();
        }
        return temp;
    }

    /**
     * @param pSearchString
     * @return
     */
    public List<UserObject> NTsearchUserByName(final String pSearchString) {
        List<UserObject> temp;
        try {
            temp = getWebApi().getApi().searchUser(pSearchString).getObj();
        } catch (RetrofitError e) {
            temp = new ArrayList<UserObject>();
        }
        return temp;
    }


}
