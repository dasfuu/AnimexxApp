package de.meisterfuu.animexx.objects.rpg;

import com.google.gson.annotations.SerializedName;

import de.meisterfuu.animexx.objects.UserObject;

public class RPGPostObject {

//		  "text_html":"Test",
	@SerializedName("text_html")
	String post;
	
//		  "datum_server":"2011-12-20 10:20:29",
//		  "datum_utc":"2011-12-20 09:20:29",
	@SerializedName("datum_utc")
	String date;
	
//		  "charakter_id":5431679,
	@SerializedName("charakter_id")
	long characterID;
	
//		  "charakter_name":"Tester",
	@SerializedName("charakter_name")
	String characterName;
	
//		  "mitglied":{ User-Objekt
//		    "id":2,
//		    "username":"cato",
//		    "steckbrief_freigabe":true
//		  },
	@SerializedName("mitglied")
UserObject author;
	
//		  "aktion":0,  0 = keine Aktion (Standard), 1 = Aktion (wird kursiv dargestellt)
	@SerializedName("aktion")
	int action;
	
//		  "intime":1, 0 = Nicht In-Time (wird grau dargestellt), 1 = In-time (Standard)
	@SerializedName("intime")
	int inTime;
	
//		  "avatar_url":"https:\/\/media.animexx.de\/rpgs\/charaktere\/85\/425985\/5786089_1450451.jpg", URL oder null
	@SerializedName("avatar_url")
	String avatarURL;
	
//		  "avatar_id":0,
	@SerializedName("avatar_id")
	long avatarID;
	
//		  "pos":1,
	@SerializedName("pos")
	long pos;
	
//		  "metadaten":[
//		    [
//		      "Ort",
//		      "Irgendo"
//		    ],
//		    [
//		      "Zeit",
//		      "Irgendwann"
//		    ],
//		    [
//		      "Test-Angabe",
//		      "Test 1"
//		    ]
//		  ]
//	@SerializedName("metadaten")
//	String metadata;
	
	public RPGPostObject(){
		
	}

//	public String getMetadata() {
//		return metadata;
//	}
//
//	public void setMetadata(String metadata) {
//		this.metadata = metadata;
//	}

	public int getAction() {
		return action;
	}

	public int getInTime() {
		return inTime;
	}

	public String getPost() {
		return post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public long getCharacterID() {
		return characterID;
	}

	public void setCharacterID(long characterID) {
		this.characterID = characterID;
	}

	public String getCharacterName() {
		return characterName;
	}

	public void setCharacterName(String characterName) {
		this.characterName = characterName;
	}

	public UserObject getAuthor() {
		return author;
	}

	public void setAuthor(UserObject author) {
		this.author = author;
	}

	public boolean isAction() {
		return (action == 1);
	}

	public void setAction(int action) {
		this.action = action;
	}

	public boolean isInTime() {
		return (inTime == 1);
	}

	public void setInTime(int inTime) {
		this.inTime = inTime;
	}

	public String getAvatarURL() {
		return avatarURL;
	}

	public void setAvatarURL(String avatarURL) {
		this.avatarURL = avatarURL;
	}

	public long getAvatarID() {
		return avatarID;
	}

	public void setAvatarID(long avatarID) {
		this.avatarID = avatarID;
	}

	public long getPos() {
		return pos;
	}

	public void setPos(long pos) {
		this.pos = pos;
	}
	
		
	
}
