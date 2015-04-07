package de.meisterfuu.animexx.objects.profile;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meisterfuu on 02.11.2014.
 */
public class ProfileBoxObject {

    //Rückgabewert ("return"-Objekt)
    //- "typ": Typ der Box (siehe /mitglieder/steckbrief/)
    //- "id": die ID der Box
    //- "titel"
    //        - "anzahl"
    //        - "html" (bei "sb")
    //        - "daten" (bei "eig")
    //        - "events_zukunft" / "events_vergangen" (bei "conventions")
    //        - "favoriten"(bei "fanlisten")


//	- "typ": einer der folgenden Typen:
//			- "sb" = Selbstbeschreibung
//	        - "eig" = Tabellarische Daten
//	        - "conventions" = Besuchte Events
//	        - "fanlisten" = Fanlisten

    @SerializedName("typ")
    String type;

    public static final String TYPE_BESCHREIBUNG = "sb";
    public static final String TYPE_EIGENSCHAFTEN = "eig";
    public static final String TYPE_EVENTS = "conventions";
    public static final String TYPE_FAVS = "fanlisten";

    //	- "id": die ID der Box
    @SerializedName("id")
    String id;

    //	- "titel"
    @SerializedName("titel")
    String title;

    //	- "anzahl"
    @SerializedName("anzahl")
    String count;

    //	- "html"
    @SerializedName("html")
    String html;

    //        - "events_zukunft" / "events_vergangen" (bei "conventions")
    @SerializedName("events_zukunft")
    List<EventBoxObject> eventsFuture;

    @SerializedName("events_vergangen")
    List<EventBoxObject> eventsPast;

    //        - "favoriten"(bei "fanlisten")
    @SerializedName("favoriten")
    List<FanBoxObject> fanlist;

    @SerializedName("daten")
    List<List<String>> dataList;

    public String getType() {
        return type;
    }

    public void setType(final String pType) {
        type = pType;
    }

    public String getTitle() {
        if(title == null || title.isEmpty()){
            return "Seite";
        }
        return title;
    }

    public void setTitle(final String pTitle) {
        title = pTitle;
    }

    public String getCount() {
        return count;
    }

    public void setCount(final String pCount) {
        count = pCount;
    }

    public String getId() {
        return id;
    }

    public void setId(final String pId) {
        id = pId;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public List<List<String>> getDataList() {
        return dataList;
    }

    public void setDataList(List<List<String>> dataList) {
        this.dataList = dataList;
    }

    public List<EventBoxObject> getEventsFuture() {
        if(eventsFuture == null){
            return new ArrayList<>();
        }
        return eventsFuture;
    }

    public void setEventsFuture(List<EventBoxObject> eventsFuture) {
        this.eventsFuture = eventsFuture;
    }

    public List<EventBoxObject> getEventsPast() {
        if(eventsPast == null){
            return new ArrayList<>();
        }
        return eventsPast;
    }

    public void setEventsPast(List<EventBoxObject> eventsPast) {
        this.eventsPast = eventsPast;
    }

    public List<FanBoxObject> getFanlist() {
        if(fanlist == null){
            return new ArrayList<>();
        }
        return fanlist;
    }

    public void setFanlist(List<FanBoxObject> fanlist) {
        this.fanlist = fanlist;
    }

    public static class EventBoxObject {

        //"event_id":64781,
        @SerializedName("event_id")
        long eventId;

        //"name":"Connichi 2014",
        @SerializedName("name")
        String name;

        //"datum_von":"2014-09-12",
        @SerializedName("datum_von")
        String dateFrom;

        //"datum_bis":"2014-09-14"
        @SerializedName("datum_bis")
        String dateTo;


        public long getEventId() {
            return eventId;
        }

        public void setEventId(long eventId) {
            this.eventId = eventId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDateFrom() {
            return dateFrom;
        }

        public void setDateFrom(String dateFrom) {
            this.dateFrom = dateFrom;
        }

        public String getDateTo() {
            return dateTo;
        }

        public void setDateTo(String dateTo) {
            this.dateTo = dateTo;
        }
    }

    public static class FanBoxObject {

        @SerializedName("name")
        String name;

        @SerializedName("typ")
        String type;

        @SerializedName("id")
        long id;

        @SerializedName("link")
        String url;

        @SerializedName("serien_typ")
        String seriesType;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getSeriesType() {
            return seriesType;
        }

        public void setSeriesType(String seriesType) {
            this.seriesType = seriesType;
        }
    }
}
