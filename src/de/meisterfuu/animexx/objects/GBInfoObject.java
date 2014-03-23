package de.meisterfuu.animexx.objects;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;


public class GBInfoObject {

//	- freigabe_lesen, boolean / hat der Aktuelle Benutzer Leserechte?
	@SerializedName("freigabe_lesen")
	boolean readable;
	
//	- freigabe_schreiben, boolean / hat der Aktuelle Benutzer Schreibrechte?
	@SerializedName("freigabe_schreiben")
	boolean writeable;
	
//	- gb_anzahl: int / Anzahl der bisherigen GB-Eintr�ge
	@SerializedName("gb_anzahl")
	int entryCount;
	
//	- kann_avatar: boolean / k�nnen Avatare verwendet werden? (Abh�ngig vom Tofu des GB-Inhabers, des aktuellen Benutzers sowie den GB-Einstellungen) 
	@SerializedName("kann_avatar")
	boolean avatarUseable;
	
//	- avatare: array()   
	@SerializedName("avatare")
	ArrayList<GBAvatar> avatars;
	
	static class GBAvatar{
//		 - id / int
		@SerializedName("id")
		long id;
		
//		 - width / int
		@SerializedName("width")
		int width;
		
//		 - height / int
		@SerializedName("height")
		int height;
		
//		 - url / string / nur wenige Minuten g�ltig
		@SerializedName("url")
		String url;
		
	}

	public boolean isReadable() {
		return readable;
	}

	public void setReadable(boolean readable) {
		this.readable = readable;
	}

	public boolean isWriteable() {
		return writeable;
	}

	public void setWriteable(boolean writeable) {
		this.writeable = writeable;
	}

	public int getEntryCount() {
		return entryCount;
	}

	public void setEntryCount(int entryCount) {
		this.entryCount = entryCount;
	}

	public boolean isAvatarUseable() {
		return avatarUseable;
	}

	public void setAvatarUseable(boolean avatarUseable) {
		this.avatarUseable = avatarUseable;
	}

	public ArrayList<GBAvatar> getAvatars() {
		return avatars;
	}

	public void setAvatars(ArrayList<GBAvatar> avatars) {
		this.avatars = avatars;
	}
			
}
