package de.meisterfuu.animexx.objects.home;

import com.google.gson.annotations.SerializedName;

import de.meisterfuu.animexx.objects.UserObject;

public class BasicHomeObject implements Comparable<BasicHomeObject> {

//	- multi_item: bool: bei einzelnen Objekten immer "false" bei Sammel Objekten immer "true"
	@SerializedName("multi_item")
	boolean multiItem;
	
//		Einzelnes Objekt:
//		- item_id: string / null: die ID des Items, wird z.B. beim Kommentieren benötigt. "null" bei Multi-Items.
	@SerializedName("item_id")
	String itemID;
	
//		- event_typ: string: der interne Name des Eventtyps. Die genaue Liste der möglichen Typen steht jeweils weiter unten.
	@SerializedName("event_typ")
	String eventType;
	
//		- event_typ_name: string: menschenlesbarer Name des Eventtyps, z.B. "Selbstbeschreibungsänderung", "Geburtstag" ...
	@SerializedName("event_typ_name")
	String eventName;
	
//		- date_server, date_server_ts, date_utc: Datum/Zeit, wann der Eintrag angelegt wurde
	@SerializedName("date_utc")
	String date;
	
	@SerializedName("date_server_ts")
	long serverTS;
	
//		- von: User-Objekt: Von wem das Ereignis ausgelöst wurde
	@SerializedName("von")
	UserObject von;
	
//		- link: string: relative URL, meist auf das zugehörige Fanwork
	@SerializedName("link")
	String relativeURL;
	
//		- item_image: string / null: Absolute URL einer Abbildung, oder "null", falls keine vorhanden ist
	@SerializedName("item_image")
	String imageURL;
	
//		- item_image_big: string / null: Absolute URL einer Abbildung, oder "null", falls keines vorhanden ist. Wird nur zurückgeliefert, wenn beim Aufruf img_max_x, img_max_y, img_quality und img_format angegeben wird.
	@SerializedName("item_image_big")
	String bigImageURL;
	
//		- item_name: string / null: Name des Objekts/Fanworks, oder "null", falls keines vorhanden ist.
	@SerializedName("item_name")
	String itemName;
	
//		- item_author: User-Object: Der Benutzer, von dem das verlinkte Fanwork stammt.
	@SerializedName("item_author")
	UserObject author;
	
//		- fanwork_class: int: Fanwork-Typ (nur gesetzt, falls es eine Fanwork-Zuordnung gibt).
	@SerializedName("fanwork_class")
	int fanworkClass;
	
//		- fanwork_id: int: Fanwork-ID (nur gesetzt, falls es eine Fanwork-Zuordnung gibt).
	@SerializedName("fanwork_id")
	long fanworkID;
	
//		- fanwork_kommentierbar: bool: Ist das Fanwork kommentierbar? (nur gesetzt, falls es eine Fanwork-Zuordnung gibt).
	@SerializedName("fanwork_kommentierbar")
	boolean fanworkCommentable;
	
//		Sammel-Objekt:
//		- grouped_by: int: Anhand welchen Kriteriums die Objekte gesammelt sind. 1 = Alle Objekte stammen vom selben Kontakt und sind vom selben Typ (Kontakt xyz hat 5 Fanarts am Stück empfohlen). 2 = Verschiedene Kontakte haben das selbe Objekt empfohlen.
	@SerializedName("grouped_by")
	int groupedBy;

	
	public boolean isMultiItem() {
		return multiItem;
	}

	public void setMultiItem(boolean multiItem) {
		this.multiItem = multiItem;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public UserObject getVon() {
		return von;
	}

	public void setVon(UserObject von) {
		this.von = von;
	}

	public String getRelativeURL() {
		return relativeURL;
	}

	public void setRelativeURL(String relativeURL) {
		this.relativeURL = relativeURL;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getBigImageURL() {
		return bigImageURL;
	}

	public void setBigImageURL(String bigImageURL) {
		this.bigImageURL = bigImageURL;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public UserObject getAuthor() {
		return author;
	}

	public void setAuthor(UserObject author) {
		this.author = author;
	}

	public int getFanworkClass() {
		return fanworkClass;
	}

	public void setFanworkClass(int fanworkClass) {
		this.fanworkClass = fanworkClass;
	}

	public long getFanworkID() {
		return fanworkID;
	}

	public void setFanworkID(long fanworkID) {
		this.fanworkID = fanworkID;
	}

	public boolean isFanworkCommentable() {
		return fanworkCommentable;
	}

	public void setFanworkCommentable(boolean fanworkCommentable) {
		this.fanworkCommentable = fanworkCommentable;
	}

	public int getGroupedBy() {
		return groupedBy;
	}

	public void setGroupedBy(int groupedBy) {
		this.groupedBy = groupedBy;
	}

	@Override
	public int compareTo(BasicHomeObject another) {
		return ((Long)this.serverTS).compareTo(another.serverTS)*-1;
	}
		
	
	
}
