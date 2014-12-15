package de.meisterfuu.animexx.xmpp;

import android.app.ListFragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.meisterfuu.animexx.activitys.main.MainActivity;
import de.meisterfuu.animexx.adapter.XMPPRoosterAdapter;
import de.meisterfuu.animexx.api.xmpp.XMPPApi;
import de.meisterfuu.animexx.objects.xmpp.XMPPRoosterObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class XMPPRoosterFragment extends ListFragment  {

    ArrayList<XMPPRoosterObject> list;
    XMPPRoosterAdapter adapter;
    BroadcastReceiver receiver;
    XMPPApi mApi;


    public static XMPPRoosterFragment getInstance() {
        XMPPRoosterFragment result = new XMPPRoosterFragment();
//	     Bundle args = new Bundle();
//	     args.putLong("mFolderID", pFolderID);
//	     args.putString("mType", pType);
//	     result.setArguments(args);
        return result;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = new ArrayList<XMPPRoosterObject>();
        adapter = new XMPPRoosterAdapter(list, this.getActivity());
        setListAdapter(adapter);
    }


    @Override
    public void onPause() {
        super.onPause();
        mApi.close();
        mApi = null;
        this.getActivity().unregisterReceiver(receiver);
    }


    @Override
    public void onResume() {
        super.onResume();
        mApi = new XMPPApi(this.getActivity());


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(XMPPService.NEW_ROOSTER) || action.equals(XMPPService.NEW_MESSAGE)) {
                    getRooster();
                }
            }
        };


        IntentFilter filter = new IntentFilter();
        filter.addAction(XMPPService.NEW_ROOSTER);
        this.getActivity().registerReceiver(receiver, filter);
        getRooster();

        IntentFilter filter2 = new IntentFilter();
        filter.addAction(XMPPService.NEW_MESSAGE);
        this.getActivity().registerReceiver(receiver, filter2);
    }


    private void getRooster() {
        mApi.getRooster(new Callback<List<XMPPRoosterObject>>() {
            @Override
            public void success(List<XMPPRoosterObject> xmppRoosterObjects, Response response) {
                final List<XMPPRoosterObject> temp = xmppRoosterObjects;
                Collections.sort(temp);
                list.clear();
                list.addAll(temp);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        }, false);


    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        XMPPChatActivity.getInstance(this.getActivity(), list.get(position).getJid());
    }


    public static PendingIntent getPendingIntent(Context pContext) {
        Intent intent = new Intent(pContext, MainActivity.class);
        intent.putExtra("LANDING", "CHAT");
        return PendingIntent.getActivity(pContext, 0, intent, 0);
    }

}
