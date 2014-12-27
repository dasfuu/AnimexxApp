package de.meisterfuu.animexx.api.xmpp;

/**
 * Created by Meisterfuu on 26.09.2014.
 */
public class SendMessageEvent {

    public String toJid;
    public long toId;
    public String message;
    public long time;

    public String getToJid() {
        return toJid;
    }

    public void setToJid(String toJid) {
        this.toJid = toJid;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
