package de.meisterfuu.animexx.data;

import de.meisterfuu.animexx.utils.APIException;


public abstract interface APICallback<T> {
	
	public abstract void onCallback(APIException pError, T pObject);

}
