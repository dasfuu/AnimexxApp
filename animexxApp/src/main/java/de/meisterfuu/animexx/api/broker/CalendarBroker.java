package de.meisterfuu.animexx.api.broker;

import android.content.Context;

import de.meisterfuu.animexx.api.Self;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.SingleValueObjects;
import retrofit.Callback;

public class CalendarBroker extends BasicWebBroker {


    //Init

    public CalendarBroker(Context pContext) {
        super(pContext);

    }


    //Public Methods


    /**
     * @param pCallback
     */
    public void getEventList(final String pDay, final Callback<ReturnObject<SingleValueObjects.CalListObject>> pCallback) {
        getWebApi().getApi().getPrivateCalendar(Self.getInstance(getContext()).getUserID(), pDay, "month", 0, 1, 0, pCallback);
    }

    /**
     * No threads
     */
    public SingleValueObjects.CalListObject getEventListNT(final String pDay) {
        return getWebApi().getApi().getPrivateCalendar(Self.getInstance(getContext()).getUserID(), pDay, "month", 0, 1, 0).getObj();
    }


}

