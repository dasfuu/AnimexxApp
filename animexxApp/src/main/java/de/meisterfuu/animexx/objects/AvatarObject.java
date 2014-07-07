package de.meisterfuu.animexx.objects;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;


public class AvatarObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SerializedName("height")
	int height;
	@SerializedName("width")
	int width;
	@SerializedName("url")
	String url;

	public AvatarObject(){
	}

	
	public int getHeight() {
		return height;
	}

	
	public void setHeight(int height) {
		this.height = height;
	}

	
	public int getWidth() {
		return width;
	}

	
	public void setWidth(int width) {
		this.width = width;
	}

	
	public String getUrl() {
		return url;
	}

	
	public void setUrl(String url) {
		this.url = url;
	}
	
}

