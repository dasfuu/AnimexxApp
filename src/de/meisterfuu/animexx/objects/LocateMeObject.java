package de.meisterfuu.animexx.objects;

public class LocateMeObject {
	
	String text;
	String username;
	long ts;
	
	double longitude;
	double latitude;
	
	String imageURLMe;
	String imageURLPlace;
	
	public LocateMeObject(){
		
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getTs() {
		return ts;
	}

	public void setTs(long ts) {
		this.ts = ts;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getImageURLMe() {
		return imageURLMe;
	}

	public void setImageURLMe(String imageURLMe) {
		this.imageURLMe = imageURLMe;
	}

	public String getImageURLPlace() {
		return imageURLPlace;
	}

	public void setImageURLPlace(String imageURLPlace) {
		this.imageURLPlace = imageURLPlace;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	
	
	
	

}
