package de.meisterfuu.animexx.objects;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;

public class UserSearchResultObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@SerializedName("username")
	@DatabaseField
	String username;
	@SerializedName("id")
	@DatabaseField(id = true)
	long id;
	@SerializedName("foto_url")
	String avatar;
	@SerializedName("steckbrief_frei")
	boolean profileShared;
	
	public UserSearchResultObject(){
		this.username = "Abgemeldet";
		this.id = -1;
		this.avatar = null;
		this.profileShared = false;
	}
	
	public boolean isRegistered(){
		return (this.id >= 0);
	}

	
	public String getUsername() {
		return username;
	}

	
	public void setUsername(String username) {
		this.username = username;
	}

	
	public long getId() {
		return id;
	}

	
	public void setId(long id) {
		this.id = id;
	}

	
	public String getAvatar() {
		return avatar;
	}

	
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	@Override
	public String toString(){
		return this.getUsername();		
	}
	
	
}
