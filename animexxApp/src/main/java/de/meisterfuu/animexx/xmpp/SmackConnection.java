package de.meisterfuu.animexx.xmpp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.squareup.otto.Subscribe;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ChatMessageListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.ping.PingFailedListener;
import org.jivesoftware.smackx.ping.PingManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import de.meisterfuu.animexx.Debug;
import de.meisterfuu.animexx.api.EventBus;
import de.meisterfuu.animexx.api.Self;
import de.meisterfuu.animexx.api.broker.UserBroker;
import de.meisterfuu.animexx.api.xmpp.ChatEvent;
import de.meisterfuu.animexx.api.xmpp.RoosterEvent;
import de.meisterfuu.animexx.api.xmpp.SendMessageEvent;
import de.meisterfuu.animexx.api.xmpp.SendMessageReturnEvent;
import de.meisterfuu.animexx.api.xmpp.StatsuChangeEvent;
import de.meisterfuu.animexx.api.xmpp.XMPPApi;
import de.meisterfuu.animexx.notification.XMPPNotification;
import de.meisterfuu.animexx.notification.XMPPNotificationManager;
import de.meisterfuu.animexx.objects.UserObject;
import de.meisterfuu.animexx.objects.xmpp.XMPPMessageObject;
import de.meisterfuu.animexx.objects.xmpp.XMPPRoosterObject;
import de.meisterfuu.animexx.utils.Helper;
import de.meisterfuu.animexx.utils.imageloader.DummyTarget;
import de.meisterfuu.animexx.utils.imageloader.PicassoDownloader;


/**
 * Created by Furuha on 27.12.2014.
 */
public class SmackConnection implements ConnectionListener, ChatManagerListener, RosterListener, ChatMessageListener, PingFailedListener, PacketListener {


    public static enum ConnectionState {
        CONNECTED, CONNECTING, RECONNECTING, DISCONNECTED;
    }
    public static ConnectionState sConnectionState;

    public static ConnectionState getStatus(){
        return sConnectionState;
    }

    private static final String TAG = "SMACK";
    private final Context mApplicationContext;
    private String mPassword;
    private String mUsername;
    private final String ressource;

    private XMPPTCPConnection mConnection;
    private XMPPApi mApi;

    public SmackConnection(Context pContext) {
        Log.i(TAG, "ChatConnection()");

        mApplicationContext = pContext.getApplicationContext();

        mApi = new XMPPApi(mApplicationContext);

        sConnectionState = ConnectionState.DISCONNECTED;

        SmackConfiguration.DEBUG_ENABLED = Debug.XMPP_DEBUG_ENABLE;
        SmackConfiguration.setDefaultPacketReplyTimeout(30000);
        ReconnectionManager.setEnabledPerDefault(false);

        Random r = new Random();
        int res_attach = r.nextInt();
        ressource = "AndroidApp_2_"+res_attach;

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                EventBus.getBus().getOtto().register(SmackConnection.this);
            }
        });
    }

    public void connect() throws IOException, XMPPException, SmackException {
        Log.i(TAG, "connect()");


        //Get Username
        mUsername = Self.getInstance(mApplicationContext).getUsername();

        //Password?
        mPassword = PreferenceManager.getDefaultSharedPreferences(mApplicationContext).getString("xmpp_password", null);
        if (mPassword == null) {
            mPassword = mApi.NTgetNewChatAuth();
            PreferenceManager.getDefaultSharedPreferences(mApplicationContext).edit().putString("xmpp_password", mPassword).apply();
        }


        XMPPTCPConnectionConfiguration.XMPPTCPConnectionConfigurationBuilder builder = XMPPTCPConnectionConfiguration.builder();
        builder.setHost("jabber.animexx.de");
        builder.setServiceName("jabber.animexx.de");
        builder.setResource(ressource);
        builder.setUsernameAndPassword(mUsername, mPassword);
        builder.setRosterLoadedAtLogin(true);


        mConnection = new XMPPTCPConnection(builder.build());

        //Set ConnectionListener here to catch initial connect();
        mConnection.addConnectionListener(this);

        mConnection.connect();
        mConnection.login();

        PingManager.setDefaultPingInterval(600); //Ping every 10 minutes
        PingManager pingManager = PingManager.getInstanceFor(mConnection);
        pingManager.registerPingFailedListener(this);

        ChatManager.getInstanceFor(mConnection).addChatListener(this);
        mConnection.getRoster().addRosterListener(this);

    }

    public void disconnect() {
        Log.i(TAG, "disconnect()");
        try {
            if(mConnection != null){
                mConnection.disconnect();
            }
        } catch (SmackException.NotConnectedException e) {
            sConnectionState = ConnectionState.DISCONNECTED;
            e.printStackTrace();
        }

        mConnection = null;

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                EventBus.getBus().getOtto().unregister(SmackConnection.this);
            }
        });
    }

    public boolean shouldConnect() {
        return PreferenceManager.getDefaultSharedPreferences(mApplicationContext).getBoolean("xmpp_status", false);
    }

    private String getBareJID(String from) {
        String[] res = from.split("/");
        return res[0].toLowerCase();
    }

    public void postStatusEvent(final ConnectionState status){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                EventBus.getBus().getOtto().post(new StatsuChangeEvent(status));
            }
        });
    }

    public void postToEventBus(final Object event){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                EventBus.getBus().getOtto().post(event);
            }
        });
    }

    @Subscribe
    public void onSendNewMessage(SendMessageEvent pEvent){
        final SendMessageEvent event = pEvent;
        ConnectionManager.getInstance().getmTHandler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    sendMessage(event.getMessage(), event.getToJid());
                    postToEventBus(new SendMessageReturnEvent(true));
                } catch (XMPPException | SmackException.NotConnectedException e) {
                    postToEventBus(new SendMessageReturnEvent(false));
                }
            }
        });
    }

    public void sendMessage(String message, String to) throws SmackException.NotConnectedException, XMPPException {
        System.out.println("this:" + this);
        System.out.println("to:" + to);
        System.out.println("message:" + message);
        Chat chat = ChatManager.getInstanceFor(mConnection).createChat(to, this);
        try {
            chat.sendMessage(message);
        } catch (SmackException.NotConnectedException e) {
            sConnectionState = ConnectionState.DISCONNECTED;
            e.printStackTrace();
            Helper.sendStacTrace(e, mApplicationContext);
            throw e;
        } catch (XMPPException e) {
            e.printStackTrace();
            Helper.sendStacTrace(e, mApplicationContext);
            throw e;
        }
    }

    //ChatListener

    @Override
    public void chatCreated(Chat chat, boolean createdLocally) {
        Log.i(TAG, "chatCreated()");
        chat.addMessageListener(this);
    }

    //MessageListener

    @Override
    public void processMessage(Chat chat, final Message pMessage) {
        Message message = pMessage;
        boolean carbonCopied = false;
        String fromJID;
        boolean directionIN = true;
//        CarbonExtension c = CarbonManager.getCarbon(pMessage);
//        if (c != null) {
//            carbonCopied = true;
//            Forwarded fwd = c.getForwarded();
//            message = (Message) fwd.getForwardedPacket();
//
//            if (c.getDirection() == CarbonExtension.Direction.sent) {
//                fromJID = getBareJID(message.getTo());
//                directionIN = false;
//            } else {
//                fromJID = getBareJID(message.getFrom());
//            }
//        } else {
        fromJID = getBareJID(message.getFrom());
//        }

        if (message.getType().equals(Message.Type.chat) || message.getType().equals(Message.Type.normal)) {
            if (message.getBody() != null && !chat.getParticipant().startsWith("animexx")) {

                ChatEvent event = new ChatEvent();
                event.setMessage(message.getBody());
                event.setJid(fromJID);
                event.setDirection(directionIN);
                event.setTime(System.currentTimeMillis());
                postToEventBus(event);

                Long id = mApi.getSingleRoosterFromDB(fromJID).getAnimexxID();

                if (carbonCopied) {
                    //No Notification
                } else {
                    XMPPNotificationManager manager = new XMPPNotificationManager(mApplicationContext);
                    manager.addNotification(new XMPPNotification(message.getBody(), fromJID, id));
                    manager.show();
                }


                XMPPMessageObject msg = new XMPPMessageObject();
                msg.setDate(System.currentTimeMillis());
                msg.setTopicJID(fromJID);
                msg.setBody(message.getBody());
                if (directionIN) {
                    msg.setMe(false);
                } else {
                    msg.setMe(true);
                }
                mApi.insertMessageToDB(msg);
            }
        }
    }

    //ConnectionListener

    @Override
    public void connected(XMPPConnection connection) {
        sConnectionState = ConnectionState.CONNECTED;
        Log.i(TAG, "connected()");
        postStatusEvent(sConnectionState);
    }

    @Override
    public void authenticated(XMPPConnection connection) {
        sConnectionState = ConnectionState.CONNECTED;
        Log.i(TAG, "authenticated()");
        postStatusEvent(sConnectionState);
    }

    @Override
    public void connectionClosed() {
        sConnectionState = ConnectionState.DISCONNECTED;
        Log.i(TAG, "connectionClosed()");
        postStatusEvent(sConnectionState);
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        sConnectionState = ConnectionState.DISCONNECTED;
        Log.i(TAG, "connectionClosedOnError()");
        postStatusEvent(sConnectionState);
    }

    @Override
    public void reconnectingIn(int seconds) {
        sConnectionState = ConnectionState.RECONNECTING;
        Log.i(TAG, "reconnectingIn()");
        postStatusEvent(sConnectionState);
    }

    @Override
    public void reconnectionSuccessful() {
        sConnectionState = ConnectionState.CONNECTED;
        Log.i(TAG, "reconnectionSuccessful()");
        postStatusEvent(sConnectionState);
    }

    @Override
    public void reconnectionFailed(Exception e) {
        sConnectionState = ConnectionState.DISCONNECTED;
        Log.i(TAG, "reconnectionFailed()");
        postStatusEvent(sConnectionState);
    }

    //RosterListener

    @Override
    public void entriesAdded(Collection<String> arg0) {
        for (String s : arg0) {
            Log.i(TAG, "entriesAdded: " + s);
        }
        newRoster();
    }

    @Override
    public void entriesDeleted(Collection<String> arg0) {
        for (String name : arg0) {
            mApi.deleteSingleRoosterFromDB(name);
        }
        postToEventBus(new RoosterEvent());
    }

    @Override
    public void entriesUpdated(Collection<String> arg0) {
        for (String s : arg0) {
            Log.i(TAG, "entriesUpdated: " + s);
        }
        newRoster();
    }

    @Override
    public void presenceChanged(Presence pNewPresence) {

        Log.i(TAG, "presenceChanged: " + pNewPresence.getFrom());

        XMPPRoosterObject temp = mApi.NTgetSingleRooster(pNewPresence.getFrom().split("/")[0]);
        if (temp == null) {
            newRoster();
            return;
        }


        boolean online = mConnection.getRoster().getPresence(pNewPresence.getFrom()).isAvailable();
        boolean away = pNewPresence.isAway();

        if (online && !away) {
            temp.setStatus(XMPPRoosterObject.STATUS_ONLINE);
        } else if (!online) {
            temp.setStatus(XMPPRoosterObject.STATUS_OFFLINE);
        } else if (online && away) {
            temp.setStatus(XMPPRoosterObject.STATUS_AWAY);
        }

        mApi.insertSingleRoosterToDB(temp);

        postToEventBus(new RoosterEvent());
    }

    private void newRoster() {

        ArrayList<String> names = new ArrayList<String>();
        for (RosterEntry obj : mConnection.getRoster().getEntries()) {
            Log.i(TAG, "newRoster: " + obj.getName() + " " + mConnection.getRoster().getPresence(obj.getUser()).isAvailable());
            if (obj.getUser().contains("@jabber.animexx.de")) {
                names.add(obj.getName());
            }
        }

        List<UserObject> user_list = (new UserBroker(mApplicationContext).NTgetIDs(names));

        Handler mainHandler = new Handler(Looper.getMainLooper());

        for (RosterEntry obj : mConnection.getRoster().getEntries()) {

            XMPPRoosterObject temp = new XMPPRoosterObject();
            temp.setJid(obj.getUser());
            boolean online = mConnection.getRoster().getPresence(obj.getUser()).isAvailable();
            boolean away = mConnection.getRoster().getPresence(obj.getUser()).isAway();
            if (online && !away) {
                temp.setStatus(XMPPRoosterObject.STATUS_ONLINE);
            } else if (!online) {
                temp.setStatus(XMPPRoosterObject.STATUS_OFFLINE);
            } else if (online && away) {
                temp.setStatus(XMPPRoosterObject.STATUS_AWAY);
            }
            //Beta.notifyOnline(temp, mApplicationContext);

            if (user_list != null)
                for (UserObject user_obj : user_list) {
//                    Log.e(TAG, user_obj.getUsername() + " == " + obj.getName());
                    if (user_obj.getUsername().equalsIgnoreCase(obj.getName())) {
                        temp.setAnimexxID(user_obj.getId());
                        if (user_obj.getAvatar() != null) {
                            temp.setLastAvatarURL(user_obj.getAvatar().getUrl());
                            final XMPPRoosterObject tempMainThread = temp;
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    PicassoDownloader.getAvatarPicasso(mApplicationContext).load(tempMainThread.getLastAvatarURL()).stableKey(PicassoDownloader.createAvatarKey(tempMainThread.getAnimexxID())).into(new DummyTarget());
                                }
                            });
                        }
                    }
                }

            mApi.insertSingleRoosterToDB(temp);
        }

        postToEventBus(new RoosterEvent());
    }

    //PingFailedListener

    @Override
    public void pingFailed() {
        Log.i(TAG, "pingFailed()");
    }

    @Override
    public void processPacket(final Packet pPacket) throws SmackException.NotConnectedException {

    }


}