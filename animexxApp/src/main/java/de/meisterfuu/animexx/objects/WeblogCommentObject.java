package de.meisterfuu.animexx.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeblogCommentObject {

	@SerializedName("id")
	private long id;
	@SerializedName("seite")
	private long page;
	@SerializedName("betreff")
	private String subject;
	@SerializedName("beitraege")
	private long entries;
	@SerializedName("postings")
	private List<Posting> postings;

	public long getId() {
		return id;
	}

	public void setId(final long pId) {
		id = pId;
	}

	public long getPage() {
		return page;
	}

	public void setPage(final long pPage) {
		page = pPage;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(final String pSubject) {
		subject = pSubject;
	}

	public long getEntries() {
		return entries;
	}

	public void setEntries(final long pEntries) {
		entries = pEntries;
	}

	public List<Posting> getPostings() {
		return postings;
	}

	public void setPostings(final List<Posting> pPostings) {
		postings = pPostings;
	}

	public static class Posting {

		@SerializedName("id")
		private long id;
		@SerializedName("adult")
		private long adult;
		@SerializedName("userObject")
		private UserObject user;
		@SerializedName("betreff")
		private String subject;
		@SerializedName("text")
		private String text;
		@SerializedName("signatur")
		private String signature;
		@SerializedName("datumServer")
		private String dateServer;
		@SerializedName("datumUtc")
		private String dateUtc;
		@SerializedName("smiley")
		private long smiley;
		@SerializedName("avatar")
		private String avatar;
		@SerializedName("changed")
		private String changed;

		public long getId() {
			return id;
		}

		public void setId(final long pId) {
			id = pId;
		}

		public long getAdult() {
			return adult;
		}

		public void setAdult(final long pAdult) {
			adult = pAdult;
		}

		public UserObject getUser() {
			return user;
		}

		public void setUser(final UserObject pUser) {
			user = pUser;
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(final String pSubject) {
			subject = pSubject;
		}

		public String getText() {
			return text;
		}

		public void setText(final String pText) {
			text = pText;
		}

		public String getSignature() {
			return signature;
		}

		public void setSignature(final String pSignature) {
			signature = pSignature;
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

		public long getSmiley() {
			return smiley;
		}

		public void setSmiley(final long pSmiley) {
			smiley = pSmiley;
		}

		public String getAvatar() {
			return avatar;
		}

		public void setAvatar(final String pAvatar) {
			avatar = pAvatar;
		}

		public String getChanged() {
			return changed;
		}

		public void setChanged(final String pChanged) {
			changed = pChanged;
		}
	}
}