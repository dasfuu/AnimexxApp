package de.meisterfuu.animexx.objects.ens;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Meisterfuu on 05.10.2014.
 */
public class ENSCheckRecipientsObject {

	@SerializedName("user_ids")
	ArrayList<Long> IDs;

	@SerializedName("errors")
	ArrayList<String> errors;

	@SerializedName("warnings")
	ArrayList<String> warnings;

	public ENSCheckRecipientsObject(){

	}

	public ArrayList<Long> getIDs() {
		return IDs;
	}

	public void setIDs(final ArrayList<Long> pIDs) {
		IDs = pIDs;
	}

	public ArrayList<String> getErrors() {
		return errors;
	}

	public void setErrors(final ArrayList<String> pErrors) {
		errors = pErrors;
	}

	public ArrayList<String> getWarnings() {
		return warnings;
	}

	public void setWarnings(final ArrayList<String> pWarnings) {
		warnings = pWarnings;
	}
}
