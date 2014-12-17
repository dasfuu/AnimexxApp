package de.meisterfuu.animexx.activitys.rpg;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.adapter.ProfileFanAdapter;
import de.meisterfuu.animexx.adapter.RPGCharacterListAdapter;
import de.meisterfuu.animexx.api.broker.UserBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.profile.ProfileBoxObject;
import de.meisterfuu.animexx.objects.rpg.RPGObject;
import de.meisterfuu.animexx.utils.views.FeedbackListView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RPGDetailCharaListFragment extends Fragment {


    private FeedbackListView mListView;
    private RPGCharacterListAdapter mAdapter;
    private List<RPGObject.PlayerObject> mList;
    private RPGObject mRPG;

    public static Fragment getInstance() {
        RPGDetailCharaListFragment fragment = new RPGDetailCharaListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_page_fan, container, false);


        mListView = (FeedbackListView) v.findViewById(android.R.id.list);
        mList = new ArrayList<>();
        mAdapter = new RPGCharacterListAdapter(mList, this.getActivity());
        mListView.setAdapter(mAdapter);
        load();

        return v;

    }

    private void load() {
        mRPG = ((RPGDetailActivity)getActivity()).getRPG();
        mAdapter.addAll(mRPG.getPlayer());
    }
}
