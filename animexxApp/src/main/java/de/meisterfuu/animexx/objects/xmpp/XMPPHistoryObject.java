package de.meisterfuu.animexx.objects.xmpp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

import de.meisterfuu.animexx.api.Self;

public class XMPPHistoryObject {

	
	@SerializedName("from")
	long fromID;
	
	@SerializedName("text")
	String body;
	
	@SerializedName("datum_utc")
	String date;
	
	@DatabaseField
	String fromJID;
	

	public long getFromID() {
		return fromID;
	}

	public void setFromID(long fromID) {
		this.fromID = fromID;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getFromJID() {
		return fromJID;
	}

	public void setFromJID(String fromJID) {
		this.fromJID = fromJID;
	}
	
	public boolean isMe(Context pContext){
		if(fromID == Self.getInstance(pContext).getUserID()) {
			return true;
		} else {
			return false;
		}
	}
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY);
	
	public Date getTimeObj(){
		try {
			sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
			return sdf.parse(this.getDate());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}

	}
}
