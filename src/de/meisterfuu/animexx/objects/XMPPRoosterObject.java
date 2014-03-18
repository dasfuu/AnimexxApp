package de.meisterfuu.animexx.objects;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class XMPPRoosterObject {

	@DatabaseField(id = true)
	String jid;

	@DatabaseField
	long animexxID;

	public static final int STATUS_OFFLINE = 0;
	public static final int STATUS_ONLINE = 1;
	public static final int STATUS_AWAY = 2;

	@DatabaseField
	int status;

	@DatabaseField
	String lastAvatarURL;
	
	public XMPPMessageObject latestMessage;

	public String getJid() {
		return jid;
	}

	public void setJid(String jid) {
		this.jid = jid;
	}

	public String getLastAvatarURL() {
		return lastAvatarURL;
	}

	public void setLastAvatarURL(String lastAvatarURL) {
		this.lastAvatarURL = lastAvatarURL;
	}

	public long getAnimexxID() {
		return animexxID;
	}

	public void setAnimexxID(long animexxID) {
		this.animexxID = animexxID;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return getJid().replace("@jabber.animexx.de", "");
	}

}
