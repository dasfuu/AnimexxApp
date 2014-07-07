package de.meisterfuu.animexx.objects;

import com.google.gson.annotations.SerializedName;

public class GBEntryObject {
	
//	- id, int (bigint)
	@SerializedName("id")
	long id;
	
//	- von, User-Objekt
	@SerializedName("von")
	UserObject author;
	
//	- datum_server, MySQL-Datum / ME(S)Z
//	- datum_utc, MySQL-Datum / UTC
	@SerializedName("datum_utc")
	String date;
	
//	- text_raw, string (gesetzt wenn text_format=="raw" oder text_format=="both")
	@SerializedName("text_raw")
	String raw;
	
//	- text_html, string (gesetzt wenn text_format=="html" oder text_format=="both")
	@SerializedName("text_html")
	String html;
	
//	- widmung, null / int / falls der GB-Eintrag eine Widmung enthält, wird dieser Wert gesetzt. Die API bietet derzeit noch keine Möglichkeit, die Widmungen dann auch tatsächlich abzurufen.
	
//	- avatar, null / string / falls der GB-Eintrag einen Avatar enthält, wird hier die URL zum Avatar mitgeliefert. Die URL ist nur einige Stunden gültig.
	@SerializedName("avatar")
	String avatarURL;

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public UserObject getAuthor() {
		return author;
	}

	public void setAuthor(UserObject author) {
		this.author = author;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getRaw() {
		return raw;
	}

	public void setRaw(String raw) {
		this.raw = raw;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public String getAvatarURL() {
		return avatarURL;
	}

	public void setAvatarURL(String avatarURL) {
		this.avatarURL = avatarURL;
	}
	
	
	
}
