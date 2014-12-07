package de.meisterfuu.animexx.activitys.events;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseFragment;
import de.meisterfuu.animexx.api.broker.EventBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.event.EventDescriptionObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Use the {@link de.meisterfuu.animexx.activitys.events.SingleEventDescriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleEventDescriptionFragment extends AnimexxBaseFragment {

    public static SingleEventDescriptionFragment newInstance(long pEventId, long pPageId) {
        SingleEventDescriptionFragment fragment = new SingleEventDescriptionFragment();
        Bundle args = new Bundle();
        args.putLong("pageId", pPageId);
        args.putLong("eventId", pEventId);
        fragment.setArguments(args);
        return fragment;
    }

    private long mEventId;
    private long mPageId;
    private WebView mWebView;

    public SingleEventDescriptionFragment() {
        // Required empty public constructor
    }

    @Override
    public void parentFinishedLoading(int pParentId) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_single_event_description, container, false);

        mWebView = (WebView) view.findViewById(R.id.activity_event_desc_html);

        if (getArguments() != null) {
            mPageId = getArguments().getLong("pageId");
            mEventId = getArguments().getLong("eventId");
        }

        EventBroker mApi = new EventBroker(this.getActivity());
        mApi.getEventDescription(mPageId, mEventId, new Callback<ReturnObject<EventDescriptionObject>>() {
            @Override
            public void success(final ReturnObject<EventDescriptionObject> t, final Response response) {
                EventDescriptionObject obj = t.getObj();
                if(obj != null){
                    mWebView.loadDataWithBaseURL("https://animexx.de", obj.getHtml(), "text/html", "UTF-8", null);
                }
            }

            @Override
            public void failure(final RetrofitError error) {

            }
        });

        return view;
    }


}
