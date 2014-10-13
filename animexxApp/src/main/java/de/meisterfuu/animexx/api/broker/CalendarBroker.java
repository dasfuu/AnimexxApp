package de.meisterfuu.animexx.api.broker;

import java.util.List;

import android.content.Context;

import de.meisterfuu.animexx.api.Self;
import de.meisterfuu.animexx.api.broker.BasicWebBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.CalendarEntryObject;
import de.meisterfuu.animexx.utils.APIException;
import retrofit.Callback;

public class CalendarBroker extends BasicWebBroker {

	
	//Init
	
	public CalendarBroker(Context pContext){
		super(pContext);

	}
	

	//Public Methods


	/**
	 * @param pCallback
	 */
	public void getEventList(final String pDay, final Callback<ReturnObject<List<CalendarEntryObject>>> pCallback){
		getWebApi().getApi().getPrivateCalendar(Self.getInstance(getContext()).getUserID(),pDay,"month",0,1,0, pCallback);
	}
	
	/**
	 * No threads
	 * @throws APIException 
	 */
	public List<CalendarEntryObject> getEventListNT(final String pDay) throws APIException {
		return getWebApi().getApi().getPrivateCalendar(Self.getInstance(getContext()).getUserID(),pDay,"month",0,1,0).getObj();
	}

	
}
		
