package de.meisterfuu.animexx.api.broker;

import android.content.Context;

import java.util.Calendar;
import java.util.List;

import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.home.ContactHomeObject;
import retrofit.Callback;

/**
 * Created by meisterfuu on 28.10.2014.
 */
public class HomeBroker extends BasicWebBroker {

    //Init

    public HomeBroker(Context pContext) {
        super(pContext);
    }

    //Public Methods

    /**
     * @param pCallback
     */
    public void getContactWidgetList(final Callback<ReturnObject<List<ContactHomeObject>>> pCallback) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -10);
        long from = c.getTimeInMillis() / 1000;
        getWebApi().getApi().getContactWidget(from, pCallback);
    }

    /**
     * @param pCallback
     */
    public void getContactMicroblogList(final Callback<ReturnObject<List<ContactHomeObject>>> pCallback) {
        getWebApi().getApi().getMicroblogs(pCallback);
    }

}
