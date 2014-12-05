package de.meisterfuu.animexx.objects.xmpp;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class XMPPMessageObject implements Comparable {

    @DatabaseField(generatedId = true)
    long id;

    @DatabaseField
    long fromID;

    @DatabaseField
    String body;

    @DatabaseField
    long date;

    @DatabaseField
    String topicJID;

    @DatabaseField
    boolean me;


    public boolean isMe() {
        return me;
    }

    public void setMe(boolean me) {
        this.me = me;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getFromID() {
        return fromID;
    }

    public void setFromID(long fromID) {
        this.fromID = fromID;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTopicJID() {
        return topicJID;
    }

    public void setTopicJID(String fromJID) {
        this.topicJID = fromJID;
    }

    @Override
    public int compareTo(Object another) {
        return 0;
    }

    @Override
    public String toString() {
        return topicJID;
    }


}
