package de.meisterfuu.animexx.objects.home;

import com.google.gson.annotations.SerializedName;

import de.meisterfuu.animexx.objects.UserObject;

/**
 * Created by Furuha on 09.10.2014.
 */
public class HomeCommentObject {


	//	- id: int: Die ID des Kommentars.
	@SerializedName("id")
	private long id;
	//	- mitglied: User-Object: Der Benutzer, von dem der Kommentar geschrieben wurde.
	@SerializedName("mitglied")
	private UserObject author;
	//	- datum_server: MySQL-Datum / ME(S)Z
	@SerializedName("datum_server")
	private String dateServer;
	//	- datum_utc: MySQL-Datum / UTC
	@SerializedName("datum_utc")
	private String dateUtc;
	//	- text: string: Der Text des Kommentars
	@SerializedName("text")
	private String text;


	public long getId() {
		return id;
	}

	public void setId(final long pId) {
		id = pId;
	}

	public UserObject getAuthor() {
		return author;
	}

	public void setAuthor(final UserObject pAuthor) {
		author = pAuthor;
	}

	public String getDateServer() {
		return dateServer;
	}

	public void setDateServer(final String pDateServer) {
		dateServer = pDateServer;
	}

	public String getDateUtc() {
		return dateUtc;
	}

	public void setDateUtc(final String pDateUtc) {
		dateUtc = pDateUtc;
	}

	public String getText() {
		return text;
	}

	public void setText(final String pText) {
		text = pText;
	}
}
