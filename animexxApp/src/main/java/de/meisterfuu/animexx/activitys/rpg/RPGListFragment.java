package de.meisterfuu.animexx.activitys.rpg;

import android.app.ListFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseFragment;
import de.meisterfuu.animexx.activitys.main.MainActivity;
import de.meisterfuu.animexx.adapter.RPGListAdapter;
import de.meisterfuu.animexx.api.broker.RPGBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.rpg.RPGObject;
import de.meisterfuu.animexx.utils.views.FeedbackListView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class RPGListFragment extends AnimexxBaseFragment implements AdapterView.OnItemClickListener {

    RPGBroker mAPI;
    ArrayList<RPGObject> mList;
    RPGListAdapter mAdapter;
    SharedPreferences config;
    private FeedbackListView mListView;

    public static RPGListFragment getInstance() {
        RPGListFragment result = new RPGListFragment();
//	     Bundle args = new Bundle();
//	     args.putLong("mFolderID", pFolderID);
//	     args.putString("mType", pType);
//	     result.setArguments(args);
        return result;
    }

    public static PendingIntent getPendingIntent(Context pContext) {
        Intent intent = new Intent(pContext, MainActivity.class);
        intent.putExtra("LANDING", "RPG");
        return PendingIntent.getActivity(pContext, 0, intent, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rpg_list, container, false);
        mListView = (FeedbackListView) view.findViewById(android.R.id.list);mListView.setOnItemClickListener(this);
        return view;
    }


    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onResume() {

        config = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        mAPI = new RPGBroker(this.getActivity());
        init();

        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        mList = new ArrayList<RPGObject>();
        mAdapter = new RPGListAdapter(mList, RPGListFragment.this.getActivity());
        mListView.setAdapter(mAdapter);
        loadRPG();
    }


    private void loadRPG() {
        mListView.showLoading();
        mAPI.getRPGList(new Callback<ReturnObject<List<RPGObject>>>() {
            @Override
            public void success(ReturnObject<List<RPGObject>> listReturnObject, Response response) {
                mListView.showList();
                List<RPGObject> list = listReturnObject.getObj();
                list.removeAll(Collections.singleton(null));
                Collections.sort(list);
                mAdapter.addAll(list);
                if(mAdapter.getCount() == 0){
                    mListView.showError("Keine RPGs");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();
                mListView.showError("Es ist ein Fehler aufgetreten");
            }
        });

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RPGPostListActivity.getInstance(this.getActivity(), mAdapter.getItem(position).getId());
    }
}
