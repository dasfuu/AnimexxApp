package de.meisterfuu.animexx.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Meisterfuu on 05.10.2014.
 */
public class SingleValueObjects {

	public static class Empty {


	}

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

	public static class CalListObject {

		@SerializedName("events")
		List<CalendarEntryObject> events;

        public List<CalendarEntryObject> getEvents() {
            return events;
        }

        public void setEvents(List<CalendarEntryObject> events) {
            this.events = events;
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

	public static class GCMIdObject {

		@SerializedName("registration_ids")
		List<String> Ids;

		public List<String> getIds() {
			return Ids;
		}

		public void setIds(final List<String> pIds) {
			Ids = pIds;
		}
	}

}
