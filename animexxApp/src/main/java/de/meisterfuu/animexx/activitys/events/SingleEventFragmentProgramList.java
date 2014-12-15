package de.meisterfuu.animexx.activitys.events;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseFragment;
import de.meisterfuu.animexx.adapter.RoomEventAdapter;
import de.meisterfuu.animexx.api.broker.EventBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.event.EventRoomProgramObject;
import de.meisterfuu.animexx.utils.Helper;
import de.meisterfuu.animexx.utils.views.FeedbackListView;
import de.meisterfuu.animexx.utils.views.WeekView;
import de.meisterfuu.animexx.utils.views.WeekViewEvent;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Use the {@link de.meisterfuu.animexx.activitys.events.SingleEventFragmentProgramList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleEventFragmentProgramList extends AnimexxBaseFragment implements Callback<ReturnObject<List<EventRoomProgramObject>>> {
    private static final String ID_PARAM = "mEventId";



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SingleEventFragmentNews.
     */
    public static SingleEventFragmentProgramList newInstance(long pEventId) {
        SingleEventFragmentProgramList fragment = new SingleEventFragmentProgramList();
        Bundle args = new Bundle();
        args.putLong(ID_PARAM, pEventId);
        fragment.setArguments(args);
        return fragment;
    }


    private FeedbackListView mListView;
    private List<EventRoomProgramObject> mList;
    private RoomEventAdapter mAdapter;
    private ArrayList<EventRoomProgramObject.EventProgramEntry> mAdapterList;

    private long mEventId;
    private EventBroker mEventApi;

    public SingleEventFragmentProgramList() {
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
        View view = inflater.inflate(R.layout.fragment_single_event_program_list, container, false);
        mListView = (FeedbackListView) view.findViewById(android.R.id.list);
        mAdapterList = new ArrayList<>();
        mAdapter = new RoomEventAdapter(mAdapterList, getActivity());
        mListView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mEventApi = new EventBroker(this.getActivity());
        mListView.showLoading();
        mEventApi.getEventProgram(mEventId, this);
    }

    @Override
    public void success(final ReturnObject<List<EventRoomProgramObject>> listReturnObject, Response response) {
        mListView.showList();
        mList = listReturnObject.getObj();
        ArrayList<EventRoomProgramObject.EventProgramEntry> list = new ArrayList<>();
        if(mList != null) {
            for (EventRoomProgramObject room : mList) {
                for (EventRoomProgramObject.EventProgramEntry entry : room.getEntries()) {
                    entry.setRoomName(room.getRoomName());
                    list.add(entry);
                }
            }
            Collections.sort(list);
            mAdapterList.clear();
            mAdapter.addAll(list);
            if(mAdapter.getCount() == 0){
                mListView.showError("Kein Programmplan vorhanden");
            }
        }
    }

    @Override
    public void failure(RetrofitError error) {
        mListView.showError("Es ist ein Fehler aufgetreten");
    }

}
