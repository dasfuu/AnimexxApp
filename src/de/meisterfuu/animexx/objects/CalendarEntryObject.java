package de.meisterfuu.animexx.objects;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.google.gson.annotations.SerializedName;

public class CalendarEntryObject  {
	
	
//	 "id":"event_57383",
	@SerializedName("id")
	String id;
	
//     "titel":"Connichi 2013",
	@SerializedName("titel")
	String title;
	
//     "start":"2013-09-13 00:00:00",
	@SerializedName("start")
	String start;
	
//     "ende":"2013-09-15 23:59:59",
	@SerializedName("ende")
	String end;
	
//     "ganztag":false,
	@SerializedName("ganztag")
	boolean fullday;
	
//     "bearbeitbar":true,
	@SerializedName("bearbeitbar")
	boolean writeable;
	
//     "ort":"Kassel",
	@SerializedName("ort")
	String place;
	
//     "link":"\/events\/57383\/"
	@SerializedName("link")
	String url;
	
	
	public long internal_id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public boolean isFullday() {
		return fullday;
	}

	public void setFullday(boolean fullday) {
		this.fullday = fullday;
	}

	public boolean isWriteable() {
		return writeable;
	}

	public void setWriteable(boolean writeable) {
		this.writeable = writeable;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o);
	}
	
	public boolean equals(CalendarEntryObject o) {
		return this.getId().equals(o.getId());
	}

	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}

	@Override
	public String toString() {
		return this.getTitle();
	}

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMANY);
	
	public Date getStartTS(){
		try {
			sdf.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
			return sdf.parse(this.getStart());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Date getEndTS(){
		try {
			sdf.setTimeZone(TimeZone.getTimeZone("Europe/Berlin"));
			return sdf.parse(this.getEnd());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	

}
