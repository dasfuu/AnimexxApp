package de.meisterfuu.animexx.activitys.events;


import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseFragment;
import de.meisterfuu.animexx.adapter.WeblogEntryAdapter;
import de.meisterfuu.animexx.api.broker.EventBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.event.EventRoomProgramObject;
import de.meisterfuu.animexx.objects.weblog.WeblogEntryObject;
import de.meisterfuu.animexx.utils.Helper;
import de.meisterfuu.animexx.utils.views.WeekView;
import de.meisterfuu.animexx.utils.views.WeekViewEvent;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Use the {@link de.meisterfuu.animexx.activitys.events.SingleEventFragmentProgram#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleEventFragmentProgram extends AnimexxBaseFragment implements Callback<ReturnObject<List<EventRoomProgramObject>>>, WeekView.MonthChangeListener {
    private static final String ID_PARAM = "mEventId";
    private static final String START_PARAM = "mStart";
    private static final String DAYS_PARAM = "mDays";



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SingleEventFragmentNews.
     */
    public static SingleEventFragmentProgram newInstance(long pEventId, long pStart, long pDays) {
        SingleEventFragmentProgram fragment = new SingleEventFragmentProgram();
        Bundle args = new Bundle();
        args.putLong(ID_PARAM, pEventId);
        args.putLong(START_PARAM, pStart);
        args.putLong(DAYS_PARAM, pDays);
        fragment.setArguments(args);
        return fragment;
    }


    private WeekView mWeekView;
    private List<EventRoomProgramObject> mList;

    private long mEventId;
    private EventBroker mEventApi;
    private long mStartTime;
    private long mDays;

    public SingleEventFragmentProgram() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mEventId = getArguments().getLong(ID_PARAM);
            mStartTime = getArguments().getLong(START_PARAM);
            mDays = getArguments().getLong(DAYS_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_event_program, container, false);
        mWeekView = (WeekView) view.findViewById(R.id.weekView);
        mWeekView.setMonthChangeListener(this);
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("UTC+0"));
        cal.setTimeInMillis(mStartTime);
        //cal.set(2014,Calendar.SEPTEMBER,12);

        mWeekView.setNumberOfVisibleDays(3);
        mWeekView.goToDate(cal);
        mWeekView.goToHour(7);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mEventApi = new EventBroker(this.getActivity());
        mEventApi.getEventProgram(mEventId, this);
    }

    @Override
    public void success(final ReturnObject<List<EventRoomProgramObject>> listReturnObject, Response response) {
        mList = listReturnObject.getObj();
        mWeekView.notifyDatasetChanged();
    }

    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

    @Override
    public void failure(RetrofitError error) {

    }

    @Override
    public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<>();
        if(mList != null) {
            for (EventRoomProgramObject room : mList) {
                for (EventRoomProgramObject.EventProgramEntry entry : room.getEntries()) {

                    WeekViewEvent event = new WeekViewEvent();
                    event.setName(entry.getName() + " \n" + room.getRoomName());
                    event.setId(entry.getId());

                    Calendar cal = Calendar.getInstance();
                    cal.setTimeZone(TimeZone.getTimeZone("UTC+0"));
                    cal.setTimeInMillis(Helper.toTimestamp(entry.getStartUtc()));
                    event.setStartTime(cal);

                    cal = Calendar.getInstance();
                    cal.setTimeZone(TimeZone.getTimeZone("UTC+0"));
                    cal.setTimeInMillis(Helper.toTimestamp(entry.getStartUtc()));
                    event.setEndTime(cal);

                    event.setColor(getResources().getColor(R.color.bg_purple));
                    if (event.getStartTime().get(Calendar.YEAR) == newYear && event.getStartTime().get(Calendar.MONTH) == newMonth) {
                        events.add(event);
                    }
                }
            }
        }
        return events;
    }
}
