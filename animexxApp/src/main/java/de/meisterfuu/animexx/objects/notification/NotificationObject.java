package de.meisterfuu.animexx.objects.notification;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Meisterfuu on 19.12.2014.
 */
public class NotificationObject {

    @DatabaseField(id = true)
    private long id;
    @DatabaseField
    private long time;
    @DatabaseField
    private int type;

    @DatabaseField
    private String fromUsername;
    @DatabaseField
    private long fromID;

    @DatabaseField
    private String subject;


    public long getId() {
        return id;
    }

    public void setId(final long pId) {
        id = pId;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(final String pFromUsername) {
        fromUsername = pFromUsername;
    }

    public long getFromID() {
        return fromID;
    }

    public void setFromID(final long pFromID) {
        fromID = pFromID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String pSubject) {
        subject = pSubject;
    }

    public long getTime() {
        return time;
    }

    public void setTime(final long pTime) {
        time = pTime;
    }
}
