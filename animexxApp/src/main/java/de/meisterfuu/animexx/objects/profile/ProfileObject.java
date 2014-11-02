package de.meisterfuu.animexx.objects.profile;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ProfileObject {

//	 id: int / die User-ID des Mitglieds (immer identisch zum user_id-Parameter)
	@SerializedName("id")
	long id;
	
//	- username: string
	@SerializedName("username")
	String username;
	
//	- freigabe_steckbrief: boolean / ist der Steckbrief zugänglich?
	@SerializedName("freigabe_steckbrief")
	boolean profileShared;
	
//	Folgende Rückgabewerte sind nur gesetzt, wenn freigabe_steckbrief == true; "null" falls das entsprechende Datum nicht freigegeben ist
	
//	- nachname: string
	@SerializedName("nachname")
	String lastname;
	
//	- vorname: string
	@SerializedName("vorname")
	String surename;
	
//	- alter: int
	@SerializedName("alter")
	int age;
	
//	- ort: string
	@SerializedName("ort")
	String city;
	
//	- plz: string
	@SerializedName("plz")
	String zip;
	
//	- strasse: string
	@SerializedName("strasse")
	String street;
	
//	- land: string / [ISO 3166 ALPHA-2]
	@SerializedName("land")
	String country;
	
//	- email: string / leerer String, falls nicht bekannt
	@SerializedName("email")
	String eMail;
	
//	- homepage: string / OBSOLET
	

//	- geburtstag: date / YYYY-MM-DD
	@SerializedName("geburtstag")
	String birthday;
		
//	- foto_url: string / URL / Wichtig: die URL ist nur für einige Minuten gültig! (Nur wenn "allefotos" nicht gesetzt oder == 0)
	@SerializedName("foto_url")
	String picture;
	
//	- fotos: Array / Ein Array an zweiteiligen assoziativen Arrays mit jeweils einem "url"-, "url_gross" (URL oder null) und "kommentar"-Feld. Ist nur gesetzt, wenn allefotos==1 
	@SerializedName("fotos")
	ArrayList<ProfilePictureObject> pictures;
	
	public static class ProfilePictureObject{
		@SerializedName("url")
		String url;
			
		@SerializedName("url_gross")
		String urlBig;

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getUrlBig() {
			return urlBig;
		}

		public String getGoodUrl() {
			if(getUrlBig() != null && !getUrlBig().isEmpty()) return getUrlBig();
			return getUrl();
		}

		public void setUrlBig(String urlBig) {
			this.urlBig = urlBig;
		}	
		
	}
	
//	- kontaktdaten: Array / Ein Array an zweiteiligen assoziativen Arrays mit jeweils einem "name"- und "wert"-Feld, z.B. "name" = Homepage, "wert" = http://www.animexx.de/.
	@SerializedName("kontaktdaten")
	ArrayList<ProfileContactEntry> contactData;

	public static class ProfileContactEntry{
		@SerializedName("typ")
		String name;
			
		@SerializedName("wert")
		String value;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
		
	}

//	- boxen: Array / Ein Array bestehend aus Objekten mit folgenden Eigenschaften:
	@SerializedName("boxen")
	ArrayList<ProfileBoxObject> boxes;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isProfileShared() {
		return profileShared;
	}

	public void setProfileShared(boolean profileShared) {
		this.profileShared = profileShared;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getSurename() {
		return surename;
	}

	public void setSurename(String surename) {
		this.surename = surename;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public ArrayList<ProfilePictureObject> getPictures() {
		return pictures;
	}

	public void setPictures(ArrayList<ProfilePictureObject> pictures) {
		this.pictures = pictures;
	}

	public ArrayList<ProfileContactEntry> getContactData() {
		return contactData;
	}

	public void setContactData(ArrayList<ProfileContactEntry> contactData) {
		this.contactData = contactData;
	}

	public ArrayList<ProfileBoxObject> getBoxes() {
		return boxes;
	}

	public void setBoxes(final ArrayList<ProfileBoxObject> pBoxes) {
		boxes = pBoxes;
	}
}
