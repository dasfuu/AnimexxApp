package de.meisterfuu.animexx.objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.google.gson.annotations.SerializedName;

public class EventObject {

//	- id: int
	@SerializedName("id")
	long id;
	
//	- name: string
	@SerializedName("name")
	String name;
	
//	- datum_von, datum_bis: YYYY-MM-DD
	@SerializedName("datum_von")
	String startDate;
//	- datum_bis, datum_bis: YYYY-MM-DD
	@SerializedName("datum_bis")
	String endDate;
	
//	- plz, ort: string
	@SerializedName("plz")
	String plz;
	
	@SerializedName("ort")
	String city;
	
//	- land_iso, land_name: L�nderk�rzel nach ISO-639 + ausgeschriebener Name
	@SerializedName("land_iso")
	String countryCode;
	
//	@SerializedName("land_name")
	String countryName;
	
//	- bundesland_iso, bundesland_name: Bundesl�nderk�rzel nach ISO 3166-2 + ausgeschriebener Name. Nur bei DE/AT/CH
	@SerializedName("bundesland_iso")
	String stateCode;
	
//	@SerializedName("bundesland_name")
	String stateName;
	
//	- adresse, veranstalter, kontakt: Freitext-Felder
	@SerializedName("adresse")
	String address;
	@SerializedName("veranstalter")
	String organizer;
	@SerializedName("kontakt")
	String contact;

//	- logo: string: die URL des Logos. Leerer String, falls nicht vorhanden.
	@SerializedName("logo")
	String logo;
	
//	- animexx_ticket_url: string: Link zum Animexx-Ticket-System der Convention. Nicht gesetzt, falls nicht vorhanden.
	@SerializedName("animexx_ticket_url")
	String animexxTicketURL;
	
//	- abgesagt: int: 1, falls abgesagt.
	@SerializedName("abgesagt")
	int canceled;
	
//	- groesse, groesse_str: int / string: Erwartete Besucherzahl. Interner Wert + Ausgeschriebener String.
	@SerializedName("groesse")
	int size;
	@SerializedName("groesse_str")
	String sizeString;
	
//	- animexx, animexx_str: int / string: Animexx-Involvierung. Interner Wert + Ausgeschriebener String.
	@SerializedName("animexx")
	int animexx;
	@SerializedName("animexx_str")
	String animexxString;
	
//	- bereich: objekt: Assoziatives Array: {
//	      id: int: Bereichs-ID
//	      name_kurz, name, name_kurz_pl, name_pl: string: Name des Bereichs, jeweils Kurz- und Lang-, Singular- und Pluralform.
//	      beschreibung: string: Kurze Beschreibung des Bereichs
//	      ueberbereich: int: �berbereich (wird in der API nicht weiter verwendet)
	
	
//	- dabei_anzahl: int: Die Anzahl an angemeldeten Mitgliedern.
	@SerializedName("dabei_anzahl")
	int attendees;
	
//	- ueberevent: Event-Objekt / null: falls ein �berevent vorhanden ist, dann wird dieses hier direkt mitgeliefert. Achtung: theoretisch rekursiv. In der Praxis gibt es meist nur eine �berevent-Ebene.
	@SerializedName("ueberevent")
	EventObject parentEvent;
	
//	- unterevents: int: Anzahl der Unterevents.
	@SerializedName("unterevents")
	EventObject childEvents;
	
//	- reihe: Reihen-Objekt: wie in der Funktion /events/reihe/details/
	
	
//	- geo_laenge, geo_breite: float: Geo-Koordinaten des Treffpunkts.
	@SerializedName("geo_laenge")
	Float longitude;
	@SerializedName("geo_breite")
	Float latitude;
	
//	- berichte_anzahl, videos_anzahl, termine_anzahl: int: Zahl der zugeordneten Berichte / Videos / Termine.
	@SerializedName("termine_anzahl")
	int appointmentCount;
	
	
//	- history_zugriff, fanprodukte_policy: int: Wird derzeit von der API nicht unterst�tzt.
	
//	- highlight, int, 1, wenn das Event als Startseiten-Sticky markiert ist.
	@SerializedName("highlight")
	int highlight;
	
//	- status, int, Freischalt-Status. Werte > 0 hei�en, dass das Event noch nicht freigeschaltet ist. Es darf trotzdem angezeigt werden. 0 = freigeschaltet. Gel�schte Events sind �ber die API nicht abrufbar. 
	@SerializedName("status")
	int status;
	
//	- admins, array | User-Objekt, Die Admins dieses Event-Eintrags.
	@SerializedName("admins")
	ArrayList<UserObject> admins;
	
//	- beschreibungsseiten, array, Assoziatives Array: {
//	      id: int: ID der Beschreibungsseite
//	      sprache: string: ISO-Code der Sprache (meistens "de")
//	      seite: int: Nummer der Seite innerhalb der Sprache; nur zum Sortieren wichtig, das beschreibungsseiten-array ist aber auch bereits nach diesem Wert vorsortiert.
//	      seitenname: string: Name der Seite
//	      html: int: 1, falls die Seite HTML-formatiert ist, ansonsten BBCode.
//	      status: int: 0, falls sichtbar. 1, falls unsichtbar (unsichtbare Seiten tauchen nur bei Events auf, bei denen der aktuelle Nutzer Admin-Rechte hat)

//	Falls man eingeloggt ist:
//	- admin, int, 1, wenn der aktuelle Nutzer Admin des Event-Eintrags ist.
	@SerializedName("admin")
	int admin;
	
//	- dabei: int: 1, wenn man dabei ist. Die Werte weiter unten sind nur bei dabei=1 vorhanden.
	@SerializedName("dabei")
	int participation;
	
//	- dabei_kommentar: string: Kommentar zur Dabei-Meldung.
	@SerializedName("dabei_kommentar")
	int participationString;
	
//	- dabei_status, dabei_status_str: int / string: Sicher, dass man dabei ist? 1 = unsicher.
	@SerializedName("dabei_status")
	int participationStatus;
	@SerializedName("dabei_status_str")
	String participationStatusString;
	
//	- dabei_special_status, dabei_special_status_str: int / string: spezielle Funktion auf der Con (Helfer, Orga)? Derzeit nur 1/2 vergeben, kann aber mehr werden. Bitte den String verwenden.
	@SerializedName("dabei_special_status_str")
	String participationSpecial;
	
	public EventObject(){
		
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

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getPlz() {
		return plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOrganizer() {
		return organizer;
	}

	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getAnimexxTicketURL() {
		return animexxTicketURL;
	}

	public void setAnimexxTicketURL(String animexxTicketURL) {
		this.animexxTicketURL = animexxTicketURL;
	}

	public int getCanceled() {
		return canceled;
	}

	public void setCanceled(int canceled) {
		this.canceled = canceled;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getSizeString() {
		return sizeString;
	}

	public void setSizeString(String sizeString) {
		this.sizeString = sizeString;
	}

	public int getAnimexx() {
		return animexx;
	}

	public void setAnimexx(int animexx) {
		this.animexx = animexx;
	}

	public String getAnimexxString() {
		return animexxString;
	}

	public void setAnimexxString(String animexxString) {
		this.animexxString = animexxString;
	}

	public int getAttendees() {
		return attendees;
	}

	public void setAttendees(int attendees) {
		this.attendees = attendees;
	}

	public EventObject getParentEvent() {
		return parentEvent;
	}

	public void setParentEvent(EventObject parentEvent) {
		this.parentEvent = parentEvent;
	}

	public EventObject getChildEvents() {
		return childEvents;
	}

	public void setChildEvents(EventObject childEvents) {
		this.childEvents = childEvents;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public int getAppointmentCount() {
		return appointmentCount;
	}

	public void setAppointmentCount(int appointmentCount) {
		this.appointmentCount = appointmentCount;
	}

	public int getHighlight() {
		return highlight;
	}

	public void setHighlight(int highlight) {
		this.highlight = highlight;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public ArrayList<UserObject> getAdmins() {
		return admins;
	}

	public void setAdmins(ArrayList<UserObject> admins) {
		this.admins = admins;
	}

	public int getAdmin() {
		return admin;
	}

	public void setAdmin(int admin) {
		this.admin = admin;
	}

	public int getParticipation() {
		return participation;
	}

	public void setParticipation(int participation) {
		this.participation = participation;
	}

	public int getParticipationString() {
		return participationString;
	}

	public void setParticipationString(int participationString) {
		this.participationString = participationString;
	}

	public int getParticipationStatus() {
		return participationStatus;
	}

	public void setParticipationStatus(int participationStatus) {
		this.participationStatus = participationStatus;
	}

	public String getParticipationStatusString() {
		return participationStatusString;
	}

	public void setParticipationStatusString(String participationStatusString) {
		this.participationStatusString = participationStatusString;
	}

	public String getParticipationSpecial() {
		return participationSpecial;
	}

	public void setParticipationSpecial(String participationSpecial) {
		this.participationSpecial = participationSpecial;
	}
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.GERMANY);
	
	public Date getStartTS(){
		try {
			return sdf.parse(this.getStartDate());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Date getEndTS(){
		try {
			return sdf.parse(this.getEndDate());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
