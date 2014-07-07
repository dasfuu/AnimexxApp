package de.meisterfuu.animexx.objects;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class UserObject implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@SerializedName("username")
	@DatabaseField
	String username;
	@SerializedName("id")
	@DatabaseField(id = true)
	long id;
	@SerializedName("avatar")
	AvatarObject avatar;
	@SerializedName("steckbrief_freigabe")
	boolean profileShared;
	
	public UserObject(){
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

	
	public AvatarObject getAvatar() {
		return avatar;
	}

	
	public void setAvatar(AvatarObject avatar) {
		this.avatar = avatar;
	}
	
	@Override
	public String toString(){
		return this.getUsername();		
	}
	
	
}
