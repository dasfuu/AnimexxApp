package de.meisterfuu.animexx.api.web;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Furuha on 28.09.2014.
 */
public class ReturnObject<T> {

	@SerializedName("return")
	private T obj;

	public T getObj() {
		return obj;
	}

	public void setObj(final T pObj) {
		obj = pObj;
	}
}
