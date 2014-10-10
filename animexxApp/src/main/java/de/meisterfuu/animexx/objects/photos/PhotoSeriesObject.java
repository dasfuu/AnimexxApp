package de.meisterfuu.animexx.objects.photos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import de.meisterfuu.animexx.objects.UserObject;

public class PhotoSeriesObject {

	@SerializedName("id")
	private long id;
	@SerializedName("titel")
	private String title;
	@SerializedName("datum_utc")
	private UserObject author;
	@SerializedName("datum_server")
	private String dateServer;
	@SerializedName("datum_utc")
	private String dateUtc;
	@SerializedName("datum_server_ts")
	private long dateServerTs;
	@SerializedName("adult")
	private boolean adult;
	@SerializedName("hat_kommentare")
	private boolean comments;
	@SerializedName("item_image")
	private String itemImage;
	@SerializedName("empfohlen")
	private boolean recommended;
	@SerializedName("kommentare_display")
	private long commentDisplay;
	@SerializedName("kommentare_anz")
	private long commentCount;
	@SerializedName("kommentare_anz_antworten")
	private long commentAnswerCount;
	@SerializedName("kann_kommentieren")
	private boolean commentAllowed;
	@SerializedName("kann_kommentare_beantworten")
	private boolean answeringAllowed;
	@SerializedName("tags")
	private List<Object> tags;
	@SerializedName("type")
	private String type;
	@SerializedName("anz_fotos_gesamt")
	private long photoCount;
	@SerializedName("datum_utc")
	private List<Event> events;
	@SerializedName("datum_utc")
	private List<List<List<PhotoObject>>> photos;

	public long getId() {
		return id;
	}

	public void setId(final long pId) {
		id = pId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String pTitle) {
		title = pTitle;
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

	public long getDateServerTs() {
		return dateServerTs;
	}

	public void setDateServerTs(final long pDateServerTs) {
		dateServerTs = pDateServerTs;
	}

	public boolean isAdult() {
		return adult;
	}

	public void setAdult(final boolean pAdult) {
		adult = pAdult;
	}

	public boolean isComments() {
		return comments;
	}

	public void setComments(final boolean pComments) {
		comments = pComments;
	}

	public String getItemImage() {
		return itemImage;
	}

	public void setItemImage(final String pItemImage) {
		itemImage = pItemImage;
	}

	public boolean isRecommended() {
		return recommended;
	}

	public void setRecommended(final boolean pRecommended) {
		recommended = pRecommended;
	}

	public long getCommentDisplay() {
		return commentDisplay;
	}

	public void setCommentDisplay(final long pCommentDisplay) {
		commentDisplay = pCommentDisplay;
	}

	public long getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(final long pCommentCount) {
		commentCount = pCommentCount;
	}

	public long getCommentAnswerCount() {
		return commentAnswerCount;
	}

	public void setCommentAnswerCount(final long pCommentAnswerCount) {
		commentAnswerCount = pCommentAnswerCount;
	}

	public boolean isCommentAllowed() {
		return commentAllowed;
	}

	public void setCommentAllowed(final boolean pCommentAllowed) {
		commentAllowed = pCommentAllowed;
	}

	public boolean isAnsweringAllowed() {
		return answeringAllowed;
	}

	public void setAnsweringAllowed(final boolean pAnsweringAllowed) {
		answeringAllowed = pAnsweringAllowed;
	}

	public List<Object> getTags() {
		return tags;
	}

	public void setTags(final List<Object> pTags) {
		tags = pTags;
	}

	public String getType() {
		return type;
	}

	public void setType(final String pType) {
		type = pType;
	}

	public long getPhotoCount() {
		return photoCount;
	}

	public void setPhotoCount(final long pPhotoCount) {
		photoCount = pPhotoCount;
	}

	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(final List<Event> pEvents) {
		events = pEvents;
	}

	public List<List<List<PhotoObject>>> getPhotos() {
		return photos;
	}

	public void setPhotos(final List<List<List<PhotoObject>>> pPhotos) {
		photos = pPhotos;
	}

	public class Event {

		@SerializedName("name")
		private String name;

		@SerializedName("datum_von")
		private String dateFrom;

		@SerializedName("datum_bis")
		private String dateTo;

		@SerializedName("ort")
		private String place;

		public String getName() {
			return name;
		}

		public void setName(final String pName) {
			name = pName;
		}

		public String getDateFrom() {
			return dateFrom;
		}

		public void setDateFrom(final String pDateFrom) {
			dateFrom = pDateFrom;
		}

		public String getDateTo() {
			return dateTo;
		}

		public void setDateTo(final String pDateTo) {
			dateTo = pDateTo;
		}

		public String getPlace() {
			return place;
		}

		public void setPlace(final String pPlace) {
			place = pPlace;
		}
	}

	public static class PhotoSeriesListObject {

		@SerializedName("offset")
		private int offset;
		@SerializedName("anzahl_gesamt")
		private long photoCount;
		@SerializedName("reihen")
		private List<PhotoSeriesObject> series;

		public int getOffset() {
			return offset;
		}

		public void setOffset(final int pOffset) {
			offset = pOffset;
		}

		public long getPhotoCount() {
			return photoCount;
		}

		public void setPhotoCount(final long pPhotoCount) {
			photoCount = pPhotoCount;
		}

		public List<PhotoSeriesObject> getSeries() {
			return series;
		}

		public void setSeries(final List<PhotoSeriesObject> pSeries) {
			series = pSeries;
		}
	}

}