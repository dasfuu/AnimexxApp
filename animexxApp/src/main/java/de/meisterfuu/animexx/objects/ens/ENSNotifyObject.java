package de.meisterfuu.animexx.objects.ens;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Meisterfuu on 04.07.2014.
 */
public class ENSNotifyObject {

    @DatabaseField(id = true)
    private long id;

    @DatabaseField
    private String fromUsername;
    @DatabaseField
    private long fromID;
    @DatabaseField
    private String subject;
    @DatabaseField
    private long time;

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
