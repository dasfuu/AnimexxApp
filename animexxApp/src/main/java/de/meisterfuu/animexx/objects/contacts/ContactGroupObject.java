package de.meisterfuu.animexx.objects.contacts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Furuha on 09.10.2014.
 */
public class ContactGroupObject {

	@SerializedName("id")
	private long id;
	@SerializedName("name")
	private String name;
	@SerializedName("anzahl")
	private long count;

	public long getId() {
		return id;
	}

	public void setId(final long pId) {
		id = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(final String pName) {
		name = pName;
	}

	public long getCount() {
		return count;
	}

	public void setCount(final long pCount) {
		count = pCount;
	}
}
