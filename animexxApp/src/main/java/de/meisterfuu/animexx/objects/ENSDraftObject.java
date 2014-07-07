package de.meisterfuu.animexx.objects;

import java.io.Serializable;
import java.util.ArrayList;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class ENSDraftObject implements Serializable {
	
//	- betreff, string / benötigt
//	- text, string / benötigt
//	- sig, string / benötigt
//	- an_users, int-Array / benötigt
//	- referenz_typ, string / optional / "reply" oder "forward", muss zusammen mit referenz_id angegeben werden.
//	- referenz_id, int / optional / Die ID der ENS, auf die sich diese bezieht. Muss zusammen mit referenz_id angegeben werden.
	
	@DatabaseField(generatedId = true)
	long id;
	
	@DatabaseField
	String subject;
	
	@DatabaseField
	String message;
	
	@DatabaseField
	String signature;
	
	@DatabaseField(dataType = DataType.SERIALIZABLE)
	ArrayList<Long> recipients;
	
	@DatabaseField(dataType = DataType.SERIALIZABLE)
	ArrayList<String> recipients_name;
	
	@DatabaseField
	String referenceType;
	
	@DatabaseField
	long referenceID;
	
	public ENSDraftObject(){
		
	}

	public long getID() {
		return id;
	}

	
	public void setID(long id) {
		this.id = id;
	}
	
	public String getSubject() {
		return subject;
	}

	
	public void setSubject(String subject) {
		this.subject = subject;
	}

	
	public String getMessage() {
		return message;
	}

	
	public void setMessage(String message) {
		this.message = message;
	}

	
	public String getSignature() {
		return signature;
	}

	
	public void setSignature(String signature) {
		this.signature = signature;
	}

	
	public ArrayList<Long> getRecipients() {
		return recipients;
	}

	
	public void setRecipients(ArrayList<Long> recipients) {
		this.recipients = recipients;
	}

	
	public String getReferenceType() {
		return referenceType;
	}

	
	public void setReferenceType(String referenceType) {
		this.referenceType = referenceType;
	}

	
	public long getReferenceID() {
		return referenceID;
	}

	
	public void setReferenceID(long referenceID) {
		this.referenceID = referenceID;
	}

	public ArrayList<String> getRecipients_name() {
		return recipients_name;
	}

	public void setRecipients_name(ArrayList<String> recipients_name) {
		this.recipients_name = recipients_name;
	}

	
}
