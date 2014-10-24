package de.meisterfuu.animexx.objects.profile;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Meisterfuu on 05.10.2014.
 */
public class GBListObject {

	@SerializedName("eintraege")
	ArrayList<GBEntryObject> entries;

	public ArrayList<GBEntryObject> getEntries() {
		return entries;
	}

	public void setEntries(final ArrayList<GBEntryObject> pEntries) {
		entries = pEntries;
	}
}
