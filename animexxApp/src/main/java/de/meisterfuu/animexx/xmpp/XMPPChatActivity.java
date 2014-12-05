package de.meisterfuu.animexx.xmpp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseActivityAB;
import de.meisterfuu.animexx.adapter.ChatAdapter;
import de.meisterfuu.animexx.api.broker.UserBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.api.xmpp.XMPPApi;
import de.meisterfuu.animexx.notification.XMPPNotification;
import de.meisterfuu.animexx.objects.UserObject;
import de.meisterfuu.animexx.objects.xmpp.XMPPHistoryObject;
import de.meisterfuu.animexx.objects.xmpp.XMPPMessageObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class XMPPChatActivity extends AnimexxBaseActivityAB {

    Handler h;
    ListView lv;
    EditText mNewMessageTx;
    ImageView mNewMessageBt;
    ChatAdapter adapter;
    ArrayList<ChatAdapter.Message> list;
    String mjabberName;
    BroadcastReceiver mReceiver;

    XMPPApi mApi;

    public static void getInstance(Context pContext, String jabberName) {
        Intent i = new Intent().setClass(pContext, XMPPChatActivity.class);
        Bundle args = new Bundle();
        args.putString("name", jabberName);
        i.putExtras(args);
        pContext.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmpptest);
        mNewMessageTx = (EditText) this.findViewById(R.id.xmpp_test_send);
        mNewMessageBt = (ImageView) this.findViewById(R.id.xmpp_test_send_bt);
        lv = (ListView) this.findViewById(R.id.xmpp_test_list);

        Bundle extras = this.getIntent().getExtras();
        mjabberName = extras.getString("name");
        this.getSupportActionBar().setTitle(mjabberName.replace("@jabber.animexx.de", ""));

        list = new ArrayList<ChatAdapter.Message>();
        adapter = new ChatAdapter(list, this, lv);
        lv.setDivider(null);
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(adapter);
        swingBottomInAnimationAdapter.setInitialDelayMillis(100);
        swingBottomInAnimationAdapter.setAbsListView(lv);
        lv.setAdapter(swingBottomInAnimationAdapter);

        mApi = new XMPPApi(this);

//		Intent intent = new Intent(this, XMPPService.class);
//		startService(intent);


        mNewMessageBt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                XMPPApi.sendMessage(mjabberName, mNewMessageTx.getText().toString(), XMPPChatActivity.this);

                ChatAdapter.Message temp = new ChatAdapter.Message();
                temp.setBody(mNewMessageTx.getText().toString());
                temp.setTime(System.currentTimeMillis());
                temp.setLeft(false);
                adapter.add(temp);

                mNewMessageTx.setText("");

                XMPPMessageObject msg = new XMPPMessageObject();
                msg.setDate(System.currentTimeMillis());
                msg.setTopicJID(mjabberName);
                msg.setBody(temp.getBody());
                msg.setMe(true);
                mApi.insertMessageToDB(msg);
            }
        });

    }

    public void showHistory() {

        if (mjabberName.split("@")[1].equalsIgnoreCase("jabber.animexx.de")) {
            (new UserBroker(this)).searchUserByName(mjabberName.split("@")[0], new Callback<ReturnObject<List<UserObject>>>() {


                @Override
                public void success(ReturnObject<List<UserObject>> listReturnObject, Response response) {
                    List<UserObject> temp = listReturnObject.getObj();
                    mApi.getOnlineHistory(temp.get(0).getId(), new Callback<ReturnObject<List<XMPPHistoryObject>>>() {
                        @Override
                        public void success(ReturnObject<List<XMPPHistoryObject>> listReturnObject, Response response) {
                            List<XMPPHistoryObject> temp = listReturnObject.getObj();
                            adapter.clear();
                            for (XMPPHistoryObject obj : temp) {
                                ChatAdapter.Message msg = new ChatAdapter.Message();
                                msg.setBody(obj.getBody());
                                msg.setTime(obj.getTimeObj().getTime());
                                msg.setLeft(!obj.isMe(XMPPChatActivity.this));
                                adapter.addTop(msg);
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        } else {
            mApi.getOfflineHistory(mjabberName, new Callback<List<XMPPMessageObject>>() {
                @Override
                public void success(List<XMPPMessageObject> xmppMessageObjects, Response response) {
                    List<XMPPMessageObject> temp = xmppMessageObjects;
                    adapter.clear();
                    for (XMPPMessageObject obj : temp) {
                        ChatAdapter.Message msg = new ChatAdapter.Message();
                        msg.setBody(obj.getBody());
                        msg.setTime(obj.getDate());
                        msg.setLeft(!obj.isMe());
                        adapter.add(msg);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mApi == null) mApi = new XMPPApi(this);
        XMPPNotification.d_from = mjabberName;

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(XMPPService.NEW_MESSAGE)) {
                    String from = intent.getStringExtra(XMPPService.BUNDLE_FROM);
                    String time = intent.getStringExtra(XMPPService.BUNDLE_TIME);
                    String message = intent.getStringExtra(XMPPService.BUNDLE_MESSAGE_BODY);
                    String direction = intent.getStringExtra(XMPPService.BUNDLE_DIRECTION);
                    if (!from.split("/")[0].equalsIgnoreCase(XMPPNotification.d_from)) {
                        return;
                    }
                    ChatAdapter.Message temp = new ChatAdapter.Message();
                    temp.setBody(message);
                    temp.setTime(System.currentTimeMillis());
                    temp.setLeft(direction.equals(XMPPService.BUNDLE_DIRECTION_IN));
                    adapter.add(temp);
                }
            }
        };


        showHistory();

        IntentFilter filter = new IntentFilter();
        filter.addAction(XMPPService.NEW_MESSAGE);
        registerReceiver(mReceiver, filter);


    }

    @Override
    public void onPause() {
        super.onPause();
        mApi.close();
        mApi = null;
        XMPPNotification.d_from = null;
        this.unregisterReceiver(mReceiver);
    }


}
