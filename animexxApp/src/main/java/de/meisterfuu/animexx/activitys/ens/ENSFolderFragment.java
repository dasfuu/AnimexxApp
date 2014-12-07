package de.meisterfuu.animexx.activitys.ens;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.main.MainActivity;
import de.meisterfuu.animexx.adapter.ENSFolderAdapter;
import de.meisterfuu.animexx.api.broker.ENSBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.ens.ENSObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ENSFolderFragment extends Fragment implements AbsListView.OnItemClickListener, OnScrollListener {

    ENSBroker mAPI;
    boolean mInitiated = false;
    int mNextPage = 0;
    long mFolderID = 1;
    String mType = ENSBroker.TYPE_INBOX;
    ArrayList<ENSObject> mList;
    ENSFolderAdapter mAdapter;
    int mPrevTotalItemCount;
    private long mDesign;

    static ENSFolderAdapter saveAdapter;
    static int saveItemCount, saveNextPage;
    static ArrayList<ENSObject> saveList;
    static int saveScrollstate;
    private ListView mListView;

    public static ENSFolderFragment getInstance(long pFolderID, String pType, long pDesign) {
        ENSFolderFragment result = new ENSFolderFragment();
        Bundle args = new Bundle();
        args.putLong("mFolderID", pFolderID);
        args.putLong("mDesign", pDesign);
        args.putString("mType", pType);
        result.setArguments(args);
        return result;
    }


    public static PendingIntent getPendingIntent(Context pContext) {
        Intent intent = new Intent(pContext, MainActivity.class);
        intent.putExtra("LANDING", "ENS");
        return PendingIntent.getActivity(pContext, 0, intent, 0);
    }


    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onResume() {

        mAPI = new ENSBroker(this.getActivity());
        mAPI.clearNotification();

        if (ENSFolderFragment.saveAdapter == null) {
            init();
        } else {
            initOld();
        }

        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mAPI!=null){
            mAPI.close();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ens_list, container, false);
        mListView = (ListView) view.findViewById(android.R.id.list);

        return view;
    }


    private void init() {
        this.mType = this.getArguments().getString("mType");
        this.mFolderID = this.getArguments().getLong("mFolderID");
        this.mDesign = this.getArguments().getLong("mDesign");
        mInitiated = false;
        mNextPage = 0;
        this.getListView().setOnScrollListener(this);
        mList = new ArrayList<ENSObject>();
        mAdapter = new ENSFolderAdapter(mList, ENSFolderFragment.this.getActivity(), mDesign);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        //this.getListView().setDivider(null);

        getNextPage();
    }

    private void initOld() {
        this.mType = this.getArguments().getString("mType");
        this.mFolderID = this.getArguments().getLong("mFolderID");
        this.mDesign = this.getArguments().getLong("mDesign");
        mInitiated = false;
        mNextPage = ENSFolderFragment.saveNextPage;
        this.getListView().setOnScrollListener(this);
        mList = ENSFolderFragment.saveList;
        mAdapter = ENSFolderFragment.saveAdapter;
        mPrevTotalItemCount = ENSFolderFragment.saveItemCount;
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        this.getListView().setSelectionFromTop(ENSFolderFragment.saveScrollstate, 0);

        //this.getListView().setDivider(null);

        ENSFolderFragment.saveScrollstate = 0;
        ENSFolderFragment.saveNextPage = 0;
        ENSFolderFragment.saveList = null;
        ENSFolderFragment.saveAdapter = null;
        ENSFolderFragment.saveItemCount = 0;
    }

    private void getNextPage() {
        getPage(mNextPage++);
    }

    private void getPage(int pPage) {

        mAdapter.startLoadingAnimation();

        mAPI.getENSList(pPage, mFolderID, mType, new Callback<ReturnObject<List<ENSObject>>>() {
            @Override
            public void success(final ReturnObject<List<ENSObject>> t, final Response response) {
                List<ENSObject> list = t.getObj();
                Collections.sort(list);
                mAdapter.stopLoadingAnimation();
                mAdapter.addAll(list);
            }

            @Override
            public void failure(final RetrofitError error) {
                mAdapter.stopLoadingAnimation();
            }
        });
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (view.getAdapter() != null && ((firstVisibleItem + visibleItemCount) >= totalItemCount) && totalItemCount != mPrevTotalItemCount) {
            mPrevTotalItemCount = totalItemCount;
            if (!mAdapter.isLoadingAnimation()) getNextPage();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ENSFolderFragment.saveScrollstate = this.getListView().getFirstVisiblePosition();
        ENSFolderFragment.saveNextPage = mNextPage;
        ENSFolderFragment.saveList = mList;
        ENSFolderFragment.saveAdapter = mAdapter;
        ENSFolderFragment.saveItemCount = mPrevTotalItemCount;

        SingleENSActivity.getInstance(this.getActivity(), mAdapter.getItem(position).getId());
    }

    public ListView getListView() {
        return mListView;
    }
}
