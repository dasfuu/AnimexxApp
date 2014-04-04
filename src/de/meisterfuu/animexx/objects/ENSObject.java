package de.meisterfuu.animexx.objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable
public class ENSObject implements Comparable<ENSObject> {

//	- id, int, identisch zum ens_id-Parameter
//	- von, User-Objekt
//	- an, Array[User-Objekte]
//	- betreff, string
//	- text_raw, string (gesetzt wenn text_format=="raw" oder text_format=="both")
//	- text_html, string (gesetzt wenn text_format=="html" oder text_format=="both")
//	- sig_raw, string (Signatur), (gesetzt wenn text_format=="raw" oder text_format=="both")
//	- sig_html, string (Signatur), (gesetzt wenn text_format=="html" oder text_format=="both")
//	- datum_server, MySQL-Datum / ME(S)Z
//	- datum_utc, MySQL-Datum / UTC
//	- typ, int; 1 == Standard-ENS, 2 == Systembenachrichtigung
//	- html, boolean, (true: HTML-Formatiert; kommt selten vor, muss nicht unbedingt implementiert werden; false: Der Animexx-BB-Code)
//	- konversation, int/null; Die ID des zugehörigen Gesprächsverlaufs, sofern einer existiert (ansonsten: null).
//	- referenz, int/null; Die ID der ENS, auf die sich diese bezieht (Antwort auf; Weitergeleitet von). Wenn kein Bezug oder die Bezugs-ENS nicht zu öffnen ist (gelöscht, Weiterleitung): null.
//	- an/von_ordner, int / Der Ordner, in dem die ENS bei einem selbst liegt. Entweder an_ordner oder von_ordner ist gesetzt. (Bei /ordner_ens_liste/ immer identisch zum ordner_id-Parameter)
//	- an_gelesen, bool / Nur gesetzt, wenn man die ENS selbst geschrieben hat (ordner_typ == "von"). Zeigt an, ob das Gegenüber die ENS gelesen hat oder nicht (bzw. sie nachträglich wieder auf "ungelesen" gestetzt hat).
//	- an_flags, int / Nur gesetzt, wenn die ENS an einen selbst ging (ordner_typ == "an"). Siehe/ordner_ens_liste/. 
	
	@DatabaseField(id = true)
	@SerializedName("id")
	long id;
	
	@DatabaseField(dataType = DataType.SERIALIZABLE)
	@SerializedName("von")
	UserObject von;
	
	@DatabaseField(dataType = DataType.SERIALIZABLE)
	@SerializedName("an")
	ArrayList<UserObject> an;
	
	@DatabaseField
	@SerializedName("betreff")
	String subject;
	
	@DatabaseField
	@SerializedName("text_html")
	String message;
	
	@DatabaseField
	@SerializedName("text_raw")
	String message_raw;
	
	@DatabaseField
	@SerializedName("sig_html")
	String signature;
	
	@DatabaseField
	@SerializedName("datum_utc")
	String date;
	
	@DatabaseField
	@SerializedName("typ")
	int type;
	
	@DatabaseField
	@SerializedName("konversation")
	long topicID;
	
	@DatabaseField
	@SerializedName("referenz")
	long reference;
	
	@DatabaseField
	@SerializedName("von_ordner")
	long von_ordner;
	
	@DatabaseField
	@SerializedName("an_ordner")
	long an_ordner;
	
	int folder;
	
	@DatabaseField
	@SerializedName("an_gelesen")
	boolean outboxRead;
	
	@DatabaseField
	@SerializedName("an_flags")
	int flags;
	
	public ENSObject(){
		
	}

	
	
	public long getId() {
		return id;
	}

	
	public void setId(long id) {
		this.id = id;
	}

	
	public UserObject getVon() {
		return von;
	}

	
	public void setVon(UserObject von) {
		this.von = von;
	}

	
	public ArrayList<UserObject> getAn() {
		return an;
	}

	
	public void setAn(ArrayList<UserObject> an) {
		this.an = an;
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

	
	public String getDate() {
		return date;
	}
	
	public Date getDateObject(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY);
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			return sdf.parse(getDate());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public void setDate(String date) {
		this.date = date;
	}

	
	public int getType() {
		return type;
	}

	
	public void setType(int type) {
		this.type = type;
	}

	
	public long getTopicID() {
		return topicID;
	}

	
	public void setTopicID(long topicID) {
		this.topicID = topicID;
	}

	
	public long getReference() {
		return reference;
	}

	
	public void setReference(long reference) {
		this.reference = reference;
	}

	
	public long getVon_ordner() {
		return von_ordner;
	}

	
	public void setVon_ordner(long von_ordner) {
		this.von_ordner = von_ordner;
	}

	
	public long getAn_ordner() {
		return an_ordner;
	}

	
	public void setAn_ordner(long an_ordner) {
		this.an_ordner = an_ordner;
	}

	
	public int getFolder() {
		return folder;
	}

	
	public void setFolder(int folder) {
		this.folder = folder;
	}

	
	public boolean isOutboxRead() {
		return outboxRead;
	}

	
	public void setOutboxRead(boolean outboxRead) {
		this.outboxRead = outboxRead;
	}

	
	public int getFlags() {
		return flags;
	}

	
	public void setFlags(int flags) {
		this.flags = flags;
	}



	
	public String getMessage_raw() {
		return message_raw;
	}



	
	public void setMessage_raw(String message_raw) {
		this.message_raw = message_raw;
	}



	@Override
	public int compareTo(ENSObject another) {
		return ((Long)another.getId()).compareTo(this.getId());

	}
	
	public boolean isRead(){
		String flag = Integer.toBinaryString(getFlags());
		if (flag.length() >= 2 && flag.charAt(flag.length() - 2) == '1')
			return true;
		else
			return false;
	}
	
	public boolean isAnswered() {
		String flag = Integer.toBinaryString(getFlags());
		if (flag.length() >= 3 && flag.charAt(flag.length() - 3) == '1')
			return true;
		else
			return false;
	}
	
	public boolean isForwarded() {
		String flag = Integer.toBinaryString(getFlags());
		if (flag.length() >= 4 && flag.charAt(flag.length() - 4) == '1')
			return true;
		else
			return false;
	}
	
	
	
}
