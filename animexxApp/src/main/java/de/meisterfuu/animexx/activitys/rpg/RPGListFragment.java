package de.meisterfuu.animexx.activitys.rpg;

import android.app.ListFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseFragment;
import de.meisterfuu.animexx.activitys.main.MainActivity;
import de.meisterfuu.animexx.adapter.RPGListAdapter;
import de.meisterfuu.animexx.api.broker.ENSBroker;
import de.meisterfuu.animexx.api.broker.RPGBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.rpg.RPGObject;
import de.meisterfuu.animexx.utils.views.FeedbackListView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class RPGListFragment extends AnimexxBaseFragment implements AdapterView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    RPGBroker mAPI;
    ArrayList<RPGObject> mList;
    RPGListAdapter mAdapter;
    SharedPreferences config;
    private FeedbackListView mListView;
    private SwipeRefreshLayout mSwipeLayout;

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
        mListView = (FeedbackListView) view.findViewById(android.R.id.list);
        mListView.setOnItemClickListener(this);
        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.animexx_blue,
                R.color.white);
        mSwipeLayout.setEnabled(false);
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
//                 try {
//                    ENSBroker.sendENSDEBUG(new Gson().toJson(listReturnObject), "RPG", getActivity());
//                    mListView.showError("Debug log gesendet");
//                    Toast.makeText(getActivity(), "Error 1", Toast.LENGTH_SHORT).show();
//                 } catch (Exception e) {
//                    e.printStackTrace();
//                 }
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

//                try {
//                    StringWriter errors = new StringWriter();
//                    error.printStackTrace(new PrintWriter(errors));
//                    ENSBroker.sendENSDEBUG(errors.toString() + "", "RPG", getActivity());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    BufferedReader reader = null;
//                    StringBuilder sb = new StringBuilder();
//                    reader = new BufferedReader(new InputStreamReader(error.getResponse().getBody().in()));
//
//                    String line;
//
//                    while ((line = reader.readLine()) != null) {
//                        sb.append(line);
//                    }
//                    String result = sb.toString();
//                    ENSBroker.sendENSDEBUG(result + "", "RPG", getActivity());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                Toast.makeText(getActivity(), "Error 2", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
                mListView.showError("Es ist ein Fehler aufgetreten");
            }
        });

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        RPGPostListActivity.getInstance(this.getActivity(), mAdapter.getItem(position).getId());
    }

    @Override
    public void onRefresh() {

    }
}
