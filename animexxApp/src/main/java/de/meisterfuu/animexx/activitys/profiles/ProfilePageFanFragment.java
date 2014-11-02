package de.meisterfuu.animexx.activitys.profiles;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.adapter.ProfileEventAdapter;
import de.meisterfuu.animexx.adapter.ProfileFanAdapter;
import de.meisterfuu.animexx.api.broker.UserBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.profile.ProfileBoxObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProfilePageFanFragment  extends Fragment {


    private UserBroker mApi;
    private String mBoxID;
    private long mID;
    private ListView mListView;
    private ProfileFanAdapter mAdapter;
    private List<ProfileBoxObject.FanBoxObject> mList;

    public static Fragment getInstance(Context pContext, long pUserID, String pBoxID){
        ProfilePageFanFragment fragment = new ProfilePageFanFragment();
        Bundle args = new Bundle();
        args.putLong("id", pUserID);
        args.putString("boxid", pBoxID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_page_fan, container, false);

        if (getArguments() != null) {
            mID = getArguments().getLong("id");
            mBoxID = getArguments().getString("boxid");
        }

        mApi = new UserBroker(this.getActivity());

        mListView = (ListView) v.findViewById(R.id.fragment_profile_page_fan_list);
        mList = new ArrayList<ProfileBoxObject.FanBoxObject>();
        mAdapter = new ProfileFanAdapter(mList, this.getActivity());
        mListView.setAdapter(mAdapter);
        load();

        return v;

    }

    private void load() {

        mApi.getProfileBox(mBoxID, mID, new Callback<ReturnObject<ProfileBoxObject>>() {
            @Override
            public void success(ReturnObject<ProfileBoxObject> o, Response response) {
                ProfileBoxObject obj = (ProfileBoxObject) o.getObj();
                mAdapter.addAll(obj.getFanlist());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }
}
