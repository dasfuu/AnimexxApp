package de.meisterfuu.animexx.objects.home;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

import de.meisterfuu.animexx.objects.UserObject;

public class ContactHomeObject extends BasicHomeObject {
	
	//	- items: array: Ein Array, das mehrere Einzel-Objekte (siehe oben) enthält.
	@SerializedName("items")
	ArrayList<ContactHomeObject> childItems;
	
	//	- event_typ: string: der interne Name des Eventtyps. Einer von derzeit folgenden Werten:
//	@SerializedName("event_typ")
//	String eventType;

//	- kommentar: string / null: Kommentar des Benutzers, oder "null", falls es keinen Kommentar gibt. ACHTUNG: Kann manchmal aus abwärtskompatibilitätsgründen auch den Titel des Fanworks enthalten.
	@SerializedName("kommentar")
	String comment;
	
//	- kommentierbar: bool: Können andere Nutzer Kommentare zu diesem Item verfassen?
	@SerializedName("kommentierbar")
	boolean commentable;
	
//	- kommentare_anzahl: int: Anzahl der Kommentare zu diesem Item
	@SerializedName("kommentare_anzahl")
	int commentCount;
	
//	- kommentare_letzte: string: Datum (ME(S)Z) des letzten Kommentars)
	@SerializedName("kommentare_letzte")
	String lastCommentDate;
	
//	Falls event_typ === "mb" ist:
//	- empfohlen_von: null / User-Objekt-Array: Wenn der Wert nicht "null" ist, dann wurde diese Microblog-Nachricht von einem oder mehreren Bekannten empfohlen.
	@SerializedName("empfohlen_von")
	ArrayList<UserObject> recommendedBy;

	public ArrayList<ContactHomeObject> getChildItems() {
		return childItems;
	}

	public void setChildItems(ArrayList<ContactHomeObject> childItems) {
		this.childItems = childItems;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public boolean isCommentable() {
		return commentable;
	}

	public void setCommentable(boolean commentable) {
		this.commentable = commentable;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public String getLastCommentDate() {
		return lastCommentDate;
	}

	public void setLastCommentDate(String lastCommentDate) {
		this.lastCommentDate = lastCommentDate;
	}

	public ArrayList<UserObject> getRecommendedBy() {
		return recommendedBy;
	}

	public void setRecommendedBy(ArrayList<UserObject> recommendedBy) {
		this.recommendedBy = recommendedBy;
	}

	public String toString(){
		if(this.getEventType().equalsIgnoreCase("eventteilnahme")){
			return "nimmt am Event \""+this.getItemName()+"\" teil.";
		} else if(this.getEventType().equalsIgnoreCase("mb")){
			return this.getComment();
		} else if(this.getAuthor() != null && this.getEventType().contains("mag")){
			return "empfiehlt \""+this.getItemName()+"\" von "+this.getAuthor().getUsername()+".";
		} else if(this.getComment() != null && !this.getEventType().equals("sb")) {
            if(this.getItemName() != null) {
                return this.getItemName()+"\n"+this.getComment();//this.getAuthor().getUsername();
            } else {
                return this.getComment();//this.getAuthor().getUsername(););
            }
        } else if(this.getItemName() != null) {
			return this.getItemName();//this.getAuthor().getUsername();
		} else {
			return this.getEventName();//this.getVon().getUsername();
		}
	}
	
}
