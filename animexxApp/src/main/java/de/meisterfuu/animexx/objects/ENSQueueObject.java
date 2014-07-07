package de.meisterfuu.animexx.objects;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

public class ENSQueueObject {
	
	@DatabaseField(generatedId = true)
	long id;
	
	@DatabaseField
	String subject;
	
	@DatabaseField
	int tries;
	
	@DatabaseField(dataType = DataType.SERIALIZABLE)
	@SerializedName("draft")
	ENSDraftObject draft;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public ENSDraftObject getDraft() {
		return draft;
	}

	public void setDraft(ENSDraftObject draft) {
		this.draft = draft;
	}

	public int getTries() {
		return tries;
	}

	public void setTries(int tries) {
		this.tries = tries;
	}
	
	

}
