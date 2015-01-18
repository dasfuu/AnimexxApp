package de.meisterfuu.animexx.api.xmpp;

import de.meisterfuu.animexx.xmpp.SmackConnection;

/**
 * Created by Furuha on 27.12.2014.
 */
public class StatsuChangeEvent {

    public SmackConnection.ConnectionState status;

    public StatsuChangeEvent(SmackConnection.ConnectionState status) {
        this.status = status;
    }
}
