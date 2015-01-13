package de.meisterfuu.animexx.xmpp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseFragment;
import de.meisterfuu.animexx.activitys.main.MainActivity;
import de.meisterfuu.animexx.adapter.XMPPRoosterAdapter;
import de.meisterfuu.animexx.api.xmpp.ChatEvent;
import de.meisterfuu.animexx.api.xmpp.RoosterEvent;
import de.meisterfuu.animexx.api.xmpp.StatsuChangeEvent;
import de.meisterfuu.animexx.api.xmpp.XMPPApi;
import de.meisterfuu.animexx.objects.xmpp.XMPPRoosterObject;
import de.meisterfuu.animexx.utils.views.FeedbackListView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class XMPPRoosterFragment extends AnimexxBaseFragment implements AdapterView.OnItemClickListener {

    ArrayList<XMPPRoosterObject> list;
    XMPPRoosterAdapter adapter;
    XMPPApi mApi;

    @InjectView(android.R.id.list)
    FeedbackListView mListView;
    @InjectView(R.id.statusFrame)
    FrameLayout mStatusFrame;
    @InjectView(R.id.statusText)
    TextView mStatusText;


    public static XMPPRoosterFragment getInstance() {
        XMPPRoosterFragment result = new XMPPRoosterFragment();
//	     Bundle args = new Bundle();
//	     args.putLong("mFolderID", pFolderID);
//	     args.putString("mType", pType);
//	     result.setArguments(args);
        return result;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_xmpp_rooster, container, false);
        ButterKnife.inject(this, view);

        mListView.setOnItemClickListener(this);

        list = new ArrayList<XMPPRoosterObject>();
        adapter = new XMPPRoosterAdapter(list, this.getActivity());
        mListView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        mApi.close();
        mApi = null;
    }


    @Override
    public void onResume() {
        super.onResume();
        mApi = new XMPPApi(this.getActivity());
        getRooster();
    }

    @Subscribe
    public void onStatusChange(StatsuChangeEvent event){

        Handler handler = new Handler();
        boolean shouldOnline = PreferenceManager.getDefaultSharedPreferences(this.getActivity()).getBoolean("xmpp_status", false);
        boolean display = event.online || (!shouldOnline);

        if(display){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mStatusFrame.setVisibility(View.GONE);
                }
            });
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    mStatusFrame.setVisibility(View.VISIBLE);
                }
            });
        }
    }


    @Subscribe
    public void onNewMessageReturn(RoosterEvent event){
        getRooster();
    }


    @Subscribe
    public void onNewMessageReturn(ChatEvent event){
        getRooster();
    }

    private void getRooster() {
        mApi.getRooster(new Callback<List<XMPPRoosterObject>>() {
            @Override
            public void success(List<XMPPRoosterObject> xmppRoosterObjects, Response response) {
                if(xmppRoosterObjects == null){
                    return;
                }
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

    public static PendingIntent getPendingIntent(Context pContext) {
        Intent intent = new Intent(pContext, MainActivity.class);
        intent.putExtra("LANDING", "CHAT");
        return PendingIntent.getActivity(pContext, 0, intent, 0);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        XMPPChatActivity.getInstance(this.getActivity(), list.get(position).getJid());
    }
}
