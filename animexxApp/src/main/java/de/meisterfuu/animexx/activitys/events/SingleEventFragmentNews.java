package de.meisterfuu.animexx.activitys.events;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseFragment;
import de.meisterfuu.animexx.adapter.WeblogEntryAdapter;
import de.meisterfuu.animexx.api.broker.EventBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.weblog.WeblogEntryObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SingleEventFragmentNews#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleEventFragmentNews extends AnimexxBaseFragment implements Callback<ReturnObject<List<WeblogEntryObject>>> {
    private static final String ID_PARAM = "mEventId";

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SingleEventFragmentNews.
     */
    public static SingleEventFragmentNews newInstance(long pEventId) {
        SingleEventFragmentNews fragment = new SingleEventFragmentNews();
        Bundle args = new Bundle();
        args.putLong(ID_PARAM, pEventId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ArrayList<WeblogEntryObject> mList;
    private WeblogEntryAdapter mWeblogEntryAdapter;

    private long mEventId;
    private EventBroker mEventApi;

    public SingleEventFragmentNews() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEventId = getArguments().getLong(ID_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_event_news, container, false);
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mEventApi = new EventBroker(this.getActivity());
        mEventApi.getEventNews(mEventId, this);
    }

    @Override
    public void success(ReturnObject<List<WeblogEntryObject>> listReturnObject, Response response) {
        mWeblogEntryAdapter = new WeblogEntryAdapter(listReturnObject.getObj(), this.getActivity());
        mListView.setAdapter(mWeblogEntryAdapter);
    }

    @Override
    public void failure(RetrofitError error) {

    }
}
