package de.meisterfuu.animexx.objects.xmpp;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class XMPPRoosterObject implements Comparable<XMPPRoosterObject>{

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

	@Override
	public int compareTo(XMPPRoosterObject another) {
		//Both offline
		if(this.getStatus() == XMPPRoosterObject.STATUS_OFFLINE && another.getStatus() == XMPPRoosterObject.STATUS_OFFLINE) {
			//Timecheck if both offline
			if(latestMessage == null && another.latestMessage == null) return 0;
			if(latestMessage == null) return 1;
			if(another.latestMessage == null) return -1;
			if(this.latestMessage.getDate() > another.latestMessage.getDate()){
				return -1;
			} else if(this.latestMessage.getDate() == another.latestMessage.getDate()){
				return 0;
			} else {
				return 1;
			}
		}
		//This offline
		if(this.getStatus() == XMPPRoosterObject.STATUS_OFFLINE){
			return 1;
		}
		//Other offline
		if(another.getStatus() == XMPPRoosterObject.STATUS_OFFLINE){
			return -1;
		}

		//Timecheck if both online
		if(latestMessage == null && another.latestMessage == null) return 0;
		if(latestMessage == null) return 1;
		if(another.latestMessage == null) return -1;
		if(this.latestMessage.getDate() >= another.latestMessage.getDate()){
			return -1;
		} else if(this.latestMessage.getDate() == another.latestMessage.getDate()){
			return 0;
		} else {
			return 1;
		}
	}

}
