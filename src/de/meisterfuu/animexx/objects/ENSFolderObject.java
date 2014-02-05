package de.meisterfuu.animexx.objects;

import com.google.gson.annotations.SerializedName;


public class ENSFolderObject {

	@SerializedName("ordner_id")
	long id;	
	
	String type;	
	
	@SerializedName("name")
	String name;
	
	@SerializedName("gesamt")
	int count;
	
	@SerializedName("ungelesen")
	int unread;
	
	public ENSFolderObject(){
		
	}
	
	
	@Override
	public String toString() {
		return this.getName();
	}


	public long getId() {
		return id;
	}

	
	public void setId(long id) {
		this.id = id;
	}

	
	public String getName() {
		return name;
	}

	
	public void setName(String name) {
		this.name = name;
	}

	
	public int getCount() {
		return count;
	}

	
	public void setCount(int count) {
		this.count = count;
	}

	
	public int getUnread() {
		return unread;
	}

	
	public void setUnread(int unread) {
		this.unread = unread;
	}


	public String getType() {
		return type;
	}
	
	
	public void setType(String type) {
		this.type = type;
	}
	
}
