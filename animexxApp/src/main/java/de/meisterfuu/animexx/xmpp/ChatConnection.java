package de.meisterfuu.animexx.xmpp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ChatMessageListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.ping.PingFailedListener;
import org.jivesoftware.smackx.ping.PingManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import de.meisterfuu.animexx.Debug;
import de.meisterfuu.animexx.DebugNotification;
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
import de.meisterfuu.animexx.utils.imageloader.PicassoDownloader;

public class ChatConnection implements ChatMessageListener, ChatManagerListener, RosterListener, PingFailedListener, ConnectionListener, PacketListener {

    XMPPTCPConnection mConnection;
    XMPPApi mApi;
    public static final String TAG = "XMPP";
    private Context mApplicationContext;
    protected boolean connectionState;
    private PingManager mPingManager;
    private String ressource;
    private ConnectionManager mManager;

    public ChatConnection(Context pContext, ConnectionManager pManager) {
        Log.i(TAG, "ChatConnection Constructor called");
        mApplicationContext = pContext.getApplicationContext();
        mApi = new XMPPApi(mApplicationContext);
        mManager = pManager;

        connectionState = false;

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
                EventBus.getBus().getOtto().register(ChatConnection.this);
            }
        });

    }

    public boolean connect() {
        if (mApi == null) mApi = new XMPPApi(mApplicationContext);

        Log.i(TAG, "ChatConnection.connect() called");
        try {

            //Get Username
            String username = Self.getInstance(mApplicationContext).getUsername();
            if (username == null) {
                return false;
            }

            //Password?
            String password = PreferenceManager.getDefaultSharedPreferences(mApplicationContext).getString("xmpp_password", null);
            if (password == null) {
                password = mApi.NTgetNewChatAuth();
                PreferenceManager.getDefaultSharedPreferences(mApplicationContext).edit().putString("xmpp_password", password).apply();
            }
//				username = "flo2";
//				password = "123";
            login(username, password);
            newRoster();
        } catch (Exception e) {
            e.printStackTrace();
            Helper.sendStacTrace(e, mApplicationContext);
            return false;
        }
        return true;
    }

    public void disconnect() {
        Log.i(TAG, ".disconnect() called");
        try {
            getConnection().disconnect();
        } catch (NotConnectedException e) {

        }
        mConnection = null;
        connectionState = false;
        mApi.close();
        mApi = null;

        Intent intent = new Intent(XMPPService.NEW_ROOSTER);
        intent.setPackage(mApplicationContext.getPackageName());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        }
        mApplicationContext.sendBroadcast(intent);
    }

    public boolean shouldConnect() {
        return PreferenceManager.getDefaultSharedPreferences(mApplicationContext).getBoolean("xmpp_status", false);
    }

    private void login(final String userName, final String password) throws Exception {

        //Create TCPConnection
        if (getConnection() == null) {
            XMPPTCPConnectionConfiguration.XMPPTCPConnectionConfigurationBuilder builder = XMPPTCPConnectionConfiguration.builder();
            builder.setHost("jabber.animexx.de");
            builder.setServiceName("jabber.animexx.de");
            builder.setResource(ressource);
            builder.setUsernameAndPassword(userName, password);
            builder.setRosterLoadedAtLogin(true);

            Log.i(TAG, "mConnection = new TCPConnection(config) called");
            mConnection = new XMPPTCPConnection(builder.build());
            connectionState = true;
        }

        //Create Pingmanager
        initKeepAlive();

        //Set ConnectionListener here to catch initial connect();
        getConnection().addConnectionListener(this);

        //Connect and login(if not already)
        Log.i(TAG, "TCPConnection.connect() called");
        getConnection().connect();
        if (!getConnection().isAuthenticated()) {
            Log.i(TAG, ".login() called");
            try {
                getConnection().login();
            } catch (XMPPException e) {
                PreferenceManager.getDefaultSharedPreferences(mApplicationContext).edit().putString("xmpp_password", null).apply();
                postStatusEvent(false);
                throw e;
            }
        }


        //Set listener
        this.setChatListener();
        this.setRosterListener();

        initCarbonManager();

    }


    public void initCarbonManager() {
//        try {
//            if (CarbonManager.getInstanceFor(getConnection()).isSupportedByServer()) {
//                CarbonManager.getInstanceFor(getConnection()).enableCarbons();
//            }
//        } catch (XMPPException e) {
//            e.printStackTrace();
//        } catch (SmackException e) {
//            e.printStackTrace();
//        }
    }

    private void initKeepAlive() {
        PingManager.setDefaultPingInterval(10);
        mPingManager = PingManager.getInstanceFor(getConnection());
        mPingManager.registerPingFailedListener(this);
    }

    public void ping() {
        try {
            mPingManager.pingMyServer();
        } catch (SmackException e) {
            postStatusEvent(false);
            connectionState = false;
            e.printStackTrace();
        }
    }

    public XMPPTCPConnection getConnection() {
        return mConnection;
    }

    public boolean isConnected() {
        ping();
        return (getConnection().isConnected() && connectionState && !getConnection().isSocketClosed());
    }

    public void sendMessage(String message, String to) throws NotConnectedException, XMPPException {
        System.out.println("this:" + this);
        System.out.println("to:" + to);
        System.out.println("message:" + message);
        Chat chat = ChatManager.getInstanceFor(getConnection()).createChat(to, this);
        try {
            chat.sendMessage(message);
        } catch (NotConnectedException e) {
            connectionState = false;
            e.printStackTrace();
            Helper.sendStacTrace(e, mApplicationContext);
            throw e;
        } catch (XMPPException e) {
            e.printStackTrace();
            Helper.sendStacTrace(e, mApplicationContext);
            throw e;
        }
    }


    private void setChatListener() {
        ChatManager.getInstanceFor(getConnection()).addChatListener(this);
    }

    private void setRosterListener() {
        getConnection().getRoster().addRosterListener(this);
    }

    @Override
    public void chatCreated(Chat chat, boolean arg1) {
        chat.addMessageListener(this);
    }

    private String getBareJID(String from) {
        String[] res = from.split("/");
        return res[0].toLowerCase();
    }

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

        if (message.getType().equals(Type.chat) || message.getType().equals(Type.normal)) {
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


        boolean online = getConnection().getRoster().getPresence(pNewPresence.getFrom()).isAvailable();
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
        for (RosterEntry obj : getConnection().getRoster().getEntries()) {
            Log.i(TAG, "newRoster: " + obj.getName() + " " + getConnection().getRoster().getPresence(obj.getUser()).isAvailable());
            if (obj.getUser().contains("@jabber.animexx.de")) {
                names.add(obj.getName());
            }
        }

        List<UserObject> user_list = (new UserBroker(mApplicationContext).NTgetIDs(names));

        for (RosterEntry obj : getConnection().getRoster().getEntries()) {

            XMPPRoosterObject temp = new XMPPRoosterObject();
            temp.setJid(obj.getUser());
            boolean online = getConnection().getRoster().getPresence(obj.getUser()).isAvailable();
            boolean away = getConnection().getRoster().getPresence(obj.getUser()).isAway();
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
                            PicassoDownloader.getAvatarPicasso(mApplicationContext).load(temp.getLastAvatarURL()).stableKey(PicassoDownloader.createAvatarKey(temp.getAnimexxID())).into(new Target() {
                                @Override
                                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {

                                }

                                @Override
                                public void onBitmapFailed(Drawable drawable) {

                                }

                                @Override
                                public void onPrepareLoad(Drawable drawable) {

                                }
                            });
                        }
                    }
                }

            mApi.insertSingleRoosterToDB(temp);
        }


//        Intent intent = new Intent(XMPPService.NEW_ROOSTER);
//        intent.setPackage(mApplicationContext.getPackageName());
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
//            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
//        }
//        mApplicationContext.sendBroadcast(intent);

        postToEventBus(new RoosterEvent());
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
                } catch (XMPPException e) {
                    postToEventBus(new SendMessageReturnEvent(false));
                } catch (NotConnectedException e) {
                    postToEventBus(new SendMessageReturnEvent(false));
                }
            }
        });
    }

    @Override
    public void pingFailed() {
        connectionState = false;
        mManager.scheduleCheck();
        Log.i(TAG, ".pingFailed() called");
        postStatusEvent(false);
        DebugNotification.notify(mApplicationContext, "XMPP Ping Failed", 865);
    }

    @Override
    public void reconnectionSuccessful() {
        Log.i(TAG, "ConnectionListener.reconnectionSuccessful() called");
        postStatusEvent(true);
        initKeepAlive();
        initCarbonManager();
    }

    @Override
    public void reconnectionFailed(Exception arg0) {
    }

    @Override
    public void reconnectingIn(int seconds) {
    }

    @Override
    public void connectionClosedOnError(Exception arg0) {
        connectionState = false;
        mManager.scheduleCheck();
        postStatusEvent(false);
        Log.i(TAG, "ConnectionListener.connectionClosedOnError() called");
    }

    @Override
    public void connectionClosed() {
        connectionState = false;
        mManager.scheduleCheck();
        postStatusEvent(false);
        Log.i(TAG, "ConnectionListener.connectionClosed() called");
    }

    @Override
    public void authenticated(XMPPConnection arg0) {
        Log.i(TAG, "ConnectionListener.authenticated() called");
        postStatusEvent(true);
    }

    @Override
    public void connected(XMPPConnection arg0) {
        connectionState = true;
        Log.i(TAG, "ConnectionListener.connected() called");
        postStatusEvent(true);
        initKeepAlive();
        initCarbonManager();
    }


    @Override
    public void processPacket(final Packet pPacket) throws NotConnectedException {

    }

    public void postStatusEvent(final boolean online){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                EventBus.getBus().getOtto().post(new StatsuChangeEvent(online));
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
}
