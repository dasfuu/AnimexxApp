package de.meisterfuu.animexx.activitys.events;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseFragment;
import de.meisterfuu.animexx.adapter.EventAttenderAdapter;
import de.meisterfuu.animexx.adapter.RoomEventAdapter;
import de.meisterfuu.animexx.api.broker.EventBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.event.EventAttender;
import de.meisterfuu.animexx.objects.event.EventRoomProgramObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Use the {@link de.meisterfuu.animexx.activitys.events.SingleEventFragmentAttenderList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleEventFragmentAttenderList extends AnimexxBaseFragment implements Callback<ReturnObject<List<EventAttender>>> {
    private static final String ID_PARAM = "mEventId";



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SingleEventFragmentNews.
     */
    public static SingleEventFragmentAttenderList newInstance(long pEventId) {
        SingleEventFragmentAttenderList fragment = new SingleEventFragmentAttenderList();
        Bundle args = new Bundle();
        args.putLong(ID_PARAM, pEventId);
        fragment.setArguments(args);
        return fragment;
    }


    private ListView mListView;
    private List<EventAttender> mList;
    private EventAttenderAdapter mAdapter;

    private long mEventId;
    private EventBroker mEventApi;

    public SingleEventFragmentAttenderList() {
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
        View view = inflater.inflate(R.layout.fragment_single_event_attender_list, container, false);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mList = new ArrayList<>();
        mAdapter = new EventAttenderAdapter(mList, getActivity());
        mListView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mEventApi = new EventBroker(this.getActivity());
        mEventApi.getEventAttender(mEventId, this);
    }

    @Override
    public void success(final ReturnObject<List<EventAttender>> listReturnObject, Response response) {
        mAdapter.addAll(listReturnObject.getObj());
    }

    @Override
    public void failure(RetrofitError error) {

    }

}
