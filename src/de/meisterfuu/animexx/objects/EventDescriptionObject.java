package de.meisterfuu.animexx.objects;

import com.google.gson.annotations.SerializedName;

public class EventDescriptionObject{
	
//    id: int: ID der Beschreibungsseite
	@SerializedName("id")
	long id;
	
//    sprache: string: ISO-Code der Sprache (meistens "de")
	@SerializedName("sprache")
	String language;
	
//    seite: int: Nummer der Seite innerhalb der Sprache; nur zum Sortieren wichtig, das beschreibungsseiten-array ist aber auch bereits nach diesem Wert vorsortiert.
	@SerializedName("seite")
	int pageNumber;
	
//    seitenname: string: Name der Seite
	@SerializedName("seitenname")
	String pageName;
	
//    html: int: 1, falls die Seite HTML-formatiert ist, ansonsten BBCode.
	@SerializedName("html")
	String isHtml;
	
	@SerializedName("text_html")
	String html;
	
//    status: int: 0, falls sichtbar. 1, falls unsichtbar (unsichtbare Seiten tauchen nur bei Events auf, bei denen der aktuelle Nutzer Admin-Rechte hat)
	@SerializedName("status")
	int status;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getIsHtml() {
		return isHtml;
	}

	public void setIsHtml(String isHtml) {
		this.isHtml = isHtml;
	}
	
	
}
