package de.meisterfuu.animexx.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Furuha on 05.10.2014.
 */
public class SingleValueObjects {

	public static class ENSSignatureObject {

		@SerializedName("sig")
		String signature;

		public String getSignature() {
			return signature;
		}

		public void setSignature(final String pSignature) {
			signature = pSignature;
		}
	}

	public static class ENSUnreadObject {

		@SerializedName("ungelesen")
		int unread;

		public int getUnread() {
			return unread;
		}

		public void setUnread(final int pUnread) {
			unread = pUnread;
		}
	}

	public static class ENSSendIDObject {

		@SerializedName("id")
		int id;

		public int getId() {
			return id;
		}

		public void setId(final int pID) {
			id = pID;
		}
	}

	public static class XMPPAuthObject {

		@SerializedName("chat_auth")
		String token;

		public String getToken() {
			return token;
		}

		public void setToken(final String pToken) {
			token = pToken;
		}
	}

}
