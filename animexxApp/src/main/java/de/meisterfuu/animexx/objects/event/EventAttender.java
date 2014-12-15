package de.meisterfuu.animexx.objects.event;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Furuha on 15.12.2014.
 */
public class EventAttender {


    //    - id: int
    @SerializedName("id")
    private long id;

    //    - username: string
    @SerializedName("username")
    private String username;

    //    - steckbrief_freigabe: boolean
    @SerializedName("steckbrief_freigabe")
    private boolean profileShared;

    //    - kommentar: string
    @SerializedName("kommentar")
    private String comment;

    //    - status: int: 0 = Teilahme Sicher, 1 = Teilnahme unsicher
    public static final int STATUS_SURE = 0;
    public static final int STATUS_UNSURE = 1;
    @SerializedName("status")
    private int status;

    //    - datum_server/datum_utc: MySQL-Datum / ME(S)Z u. UTC: Die Uhrzeit, zu der sich der Benutzer für dieses Event eingetragen hat.
    @SerializedName("datum_server")
    private String dateServer;

    @SerializedName("datum_utc")
    private String dateUtc;

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDateServer() {
        return dateServer;
    }

    public void setDateServer(String dateServer) {
        this.dateServer = dateServer;
    }

    public String getDateUtc() {
        return dateUtc;
    }

    public void setDateUtc(String dateUtc) {
        this.dateUtc = dateUtc;
    }
}
