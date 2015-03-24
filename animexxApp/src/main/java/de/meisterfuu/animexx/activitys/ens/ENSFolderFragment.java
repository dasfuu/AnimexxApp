package de.meisterfuu.animexx.activitys.ens;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.main.MainActivity;
import de.meisterfuu.animexx.adapter.ENSFolderAdapter;
import de.meisterfuu.animexx.api.broker.ENSBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.ens.ENSObject;
import de.meisterfuu.animexx.utils.views.FeedbackListView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ENSFolderFragment extends Fragment implements AbsListView.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, ENSFolderAdapter.NearEndListener {

    ENSBroker mAPI;
    boolean mInitiated = false;
    int mNextPage = 0;
    long mFolderID = 1;
    String mType = ENSBroker.TYPE_INBOX;
    ArrayList<ENSObject> mList;
    ENSFolderAdapter mAdapter;
    int mPrevTotalItemCount;

    static ENSFolderAdapter saveAdapter;
    static int saveItemCount, saveNextPage;
    static ArrayList<ENSObject> saveList;
    static int saveScrollstate;
    private FeedbackListView mListView;
    private SwipeRefreshLayout mSwipeLayout;
    private FloatingActionButton mFloatButton;

    public static ENSFolderFragment getInstance(long pFolderID, String pType) {
        ENSFolderFragment result = new ENSFolderFragment();
        Bundle args = new Bundle();
        args.putLong("mFolderID", pFolderID);
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
        mListView = (FeedbackListView) view.findViewById(android.R.id.list);
        mFloatButton = (FloatingActionButton) view.findViewById(R.id.ens_float_new);

        mFloatButton.setOnClickListener(this);
        mFloatButton.attachToListView(mListView);

        mSwipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setColorSchemeResources(R.color.animexx_blue,
                R.color.white);
        mSwipeLayout.setEnabled(false);

        return view;
    }


    private void init() {
        this.mType = this.getArguments().getString("mType");
        this.mFolderID = this.getArguments().getLong("mFolderID");
        mInitiated = false;
        mNextPage = 0;
        mList = new ArrayList<ENSObject>();
        mAdapter = new ENSFolderAdapter(mList, ENSFolderFragment.this.getActivity());
        mAdapter.setNearEndListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        //this.getListView().setDivider(null);

        getNextPage();
    }

    private void initOld() {
        this.mType = this.getArguments().getString("mType");
        this.mFolderID = this.getArguments().getLong("mFolderID");
        mInitiated = false;
        mNextPage = ENSFolderFragment.saveNextPage;
        mList = ENSFolderFragment.saveList;
        mAdapter = ENSFolderFragment.saveAdapter;
        mAdapter.setNearEndListener(this);
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

    boolean loading = false;
    @Override
    public void onRefresh() {
        if(!loading){
            loading = true;
            mAPI.getENSList(0, mFolderID, mType, new Callback<ReturnObject<List<ENSObject>>>() {
                @Override
                public void success(final ReturnObject<List<ENSObject>> t, final Response response) {
                    loading = false;
                    mSwipeLayout.setRefreshing(false);
                    List<ENSObject> list = t.getObj();
                    mPrevTotalItemCount = list.size();
                    Collections.sort(list);
                    mAdapter.clear();
                    mAdapter.addAll(list);
                    if(mAdapter.getCount() == 0){
                        mListView.showError("Keine ENS");
                    }
                    mNextPage = 1;
                }

                @Override
                public void failure(final RetrofitError error) {
                    loading = false;
                    mSwipeLayout.setRefreshing(false);
                    if(mAdapter.getCount() == 0){
                        mListView.showError("Es ist ein Fehler aufgetreten");
                    }
                }
            });
        }
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
                mPrevTotalItemCount = list.size();
                Collections.sort(list);
                mAdapter.stopLoadingAnimation();
                mAdapter.addAll(list);
                if(mAdapter.getCount() == 0){
                    mListView.showError("Keine ENS");
                }
                mSwipeLayout.setEnabled(true);
            }

            @Override
            public void failure(final RetrofitError error) {
                mAdapter.stopLoadingAnimation();
                if(mAdapter.getCount() == 0){
                    mListView.showError("Es ist ein Fehler aufgetreten");
                }
                mSwipeLayout.setEnabled(true);
            }
        });
    }

//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        if (view.getAdapter() != null && ((firstVisibleItem + visibleItemCount) >= totalItemCount) && totalItemCount != mPrevTotalItemCount) {
//            mPrevTotalItemCount = totalItemCount;
//            if (!mAdapter.isLoadingAnimation()) getNextPage();
//        }
//    }
//
//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        //Stub
//    }

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


    @Override
    public void onClick(View v) {
        NewENSActivity.getInstanceBlank(this.getActivity());
    }

    @Override
    public void nearsEnd(int elementsLeft) {
//        Log.e("ENS", "Left: "+elementsLeft+" mPrevTotalItemCount: "+mPrevTotalItemCount);
        if(elementsLeft <= 5){
            if (mPrevTotalItemCount == 20) {
                if (!mAdapter.isLoadingAnimation()) getNextPage();
            }
        }
    }
}
