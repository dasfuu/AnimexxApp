package de.meisterfuu.animexx.objects.photos;

import com.google.gson.annotations.SerializedName;

import de.meisterfuu.animexx.objects.UserObject;

/**
 * Created by Furuha on 08.10.2014.
 */
public class PhotoObject {

	@SerializedName("id")
	private long id;
	@SerializedName("groesse_x")
	private long sizeX;
	@SerializedName("groesse_y")
	private long sizeY;
	@SerializedName("datum_server")
	private String dateServer;
	@SerializedName("datum_utc")
	private String dateUtc;
	@SerializedName("thumbnail")
	private String thumbnailUrl;
	@SerializedName("image")
	private String imageUrl;
	@SerializedName("anzahl_animexxler")
	private long personCount;
	@SerializedName("anzahl_kommentare")
	private long commentCount;
	@SerializedName("urheber")
	private UserObject author;
	@SerializedName("kommentar")
	private String description;

	public long getId() {
		return id;
	}

	public void setId(final long pId) {
		id = pId;
	}

	public long getSizeX() {
		return sizeX;
	}

	public void setSizeX(final long pSizeX) {
		sizeX = pSizeX;
	}

	public long getSizeY() {
		return sizeY;
	}

	public void setSizeY(final long pSizeY) {
		sizeY = pSizeY;
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

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(final String pThumbnailUrl) {
		thumbnailUrl = pThumbnailUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(final String pImageUrl) {
		imageUrl = pImageUrl;
	}

	public long getPersonCount() {
		return personCount;
	}

	public void setPersonCount(final long pPersonCount) {
		personCount = pPersonCount;
	}

	public long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(final long pCommentCount) {
		commentCount = pCommentCount;
	}

	public UserObject getAuthor() {
		return author;
	}

	public void setAuthor(final UserObject pAuthor) {
		author = pAuthor;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String pDescription) {
		description = pDescription;
	}
}
