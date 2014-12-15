package de.meisterfuu.animexx.activitys.events;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseFragment;
import de.meisterfuu.animexx.adapter.EventAdapter;
import de.meisterfuu.animexx.api.broker.EventBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.event.EventObject;
import de.meisterfuu.animexx.utils.views.FeedbackListView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class EventListFragment extends AnimexxBaseFragment implements AdapterView.OnItemClickListener {

    EventBroker mAPI;
    ArrayList<EventObject> mList;
    EventAdapter mAdapter;
    private FeedbackListView mListView;

    public static EventListFragment getInstance() {
        EventListFragment result = new EventListFragment();
//	     Bundle args = new Bundle();
//	     args.putLong("mFolderID", pFolderID);
//	     args.putString("mType", pType);
//	     result.setArguments(args);
        return result;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        mListView = (FeedbackListView) view.findViewById(android.R.id.list);
        mListView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public void onResume() {
        mAPI = new EventBroker(this.getActivity());
        init();

        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        mList = new ArrayList<EventObject>();
        mAdapter = new EventAdapter(mList, EventListFragment.this.getActivity());
        mListView.setAdapter(mAdapter);
        loadEvents();
    }


    private void loadEvents() {
        mListView.showLoading();
        mAPI.getEventList(new Callback<ReturnObject<List<EventObject>>>() {
            @Override
            public void success(final ReturnObject<List<EventObject>> t, final Response response) {
                mListView.showList();
                mAdapter.addAll(t.getObj());
                if(mAdapter.getCount() == 0){
                    mListView.showError("Keine Events");
                }
            }

            @Override
            public void failure(final RetrofitError error) {
                mListView.showError("Es ist ein Fehler aufgetreten");
            }
        });


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SingleEventActivity.getInstance(this.getActivity(), id);
    }
}
