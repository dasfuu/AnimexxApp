package de.meisterfuu.animexx.activitys.contacts;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseFragment;
import de.meisterfuu.animexx.activitys.profiles.ProfileActivity;
import de.meisterfuu.animexx.adapter.ContactAdapter;
import de.meisterfuu.animexx.api.ApiEvent;
import de.meisterfuu.animexx.api.broker.UserBroker;
import de.meisterfuu.animexx.objects.UserObject;
import de.meisterfuu.animexx.utils.views.FeedbackListView;

public class ContactFragment extends AnimexxBaseFragment implements AdapterView.OnItemClickListener {


    private UserBroker mApi;
    private long mID;
    private FeedbackListView mListView;
    private ContactAdapter mAdapter;
    private List<UserObject> mList;

    public static Fragment getInstance() {
        return getInstance(-1);
    }

    public static Fragment getInstance(long pGroudpId) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putLong("id", pGroudpId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_page_fan, container, false);

        if (getArguments() != null) {
            mID = getArguments().getLong("id");
        }

        mApi = new UserBroker(this.getActivity());

        mListView = (FeedbackListView) v.findViewById(android.R.id.list);
        mList = new ArrayList<UserObject>();
        mAdapter = new ContactAdapter(mList, this.getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        load();

        return v;
    }

    private void load() {
        mListView.showLoading();
        mApi.getContacts(this.getCallerID());
    }

    @Subscribe
    public void receiveUser(ApiEvent.UserListEvent event){
        mAdapter.addAll(event.getObj());
        mListView.showList();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ProfileActivity.getInstance(this.getActivity(), id);
    }
}
