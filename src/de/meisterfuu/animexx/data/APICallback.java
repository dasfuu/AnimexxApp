package de.meisterfuu.animexx.data;

import de.meisterfuu.animexx.utils.APIException;


public abstract class APICallback {
	
	public abstract void onCallback(APIException pError, Object pObject);

}
