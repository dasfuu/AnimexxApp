package de.meisterfuu.animexx.activitys.events;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseFragment;
import de.meisterfuu.animexx.objects.event.EventObject;
import de.meisterfuu.animexx.utils.imageloader.PicassoDownloader;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SingleEventFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SingleEventFragment extends AnimexxBaseFragment {

    public static SingleEventFragment newInstance() {
        SingleEventFragment fragment = new SingleEventFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    EventObject mEvent;
    //private GoogleMap mMap;

    FrameLayout mHeader;
    ImageView mLogo;
    TextView mStart, mEnd, mAddress, mCount, mAnimexxStatus, mSection;

    public SingleEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void parentFinishedLoading(int pParentId) {
        loadEvent();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_single_event, container, false);

        mHeader = (FrameLayout) view.findViewById(R.id.activity_event_single_header);

        mLogo = (ImageView) view.findViewById(R.id.activity_event_single_user_avatar);

        mStart = (TextView) view.findViewById(R.id.activity_event_single_start_text);
        mEnd = (TextView) view.findViewById(R.id.activity_event_single_end_text);
        mAddress = (TextView) view.findViewById(R.id.activity_event_single_address_text);
        mAnimexxStatus = (TextView) view.findViewById(R.id.activity_event_single_animexx_text);
        mCount = (TextView) view.findViewById(R.id.activity_event_single_atcount_text);
        mSection = (TextView) view.findViewById(R.id.activity_event_single_type_text);

        mHeader.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(((SingleEventActivity)getActivity()).getEvent() != null){
            loadEvent();
        }
    }

    private void loadEvent(){
        mEvent = ((SingleEventActivity)getActivity()).getEvent();
        if (mEvent.getLogo() != null && !mEvent.getLogo().isEmpty()) {
            PicassoDownloader.getAvatarPicasso(getActivity()).load(mEvent.getLogo()).stableKey(PicassoDownloader.createEventLogoKey(mEvent.getId())).into(mLogo);
        } else {
            mLogo.setVisibility(View.GONE);
        }


        mStart.setText("Vom: " + mEvent.getStartDate());
        mEnd.setText("Bis: " + mEvent.getEndDate());
        mAddress.setText(mEvent.getAddress());
        mCount.setText(mEvent.getAttendees() + " Teilnehmer");
        mAnimexxStatus.setText(mEvent.getAnimexxString());
        mSection.setText(mEvent.getSections().getName());

        mHeader.setVisibility(View.VISIBLE);

        //showPages();

        //showMap();
    }


//    private void showMap() {
//        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.activity_event_single_map_fragment)).getMap();
//        Log.i("MAP", "mMap = " + mMap);
//        if (mMap != null) {
//            // The Map is verified. It is now safe to manipulate the map.
//            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//            CameraUpdate postion = CameraUpdateFactory.newLatLngZoom(new LatLng(mEvent.getLatitude(), mEvent.getLongitude()), 15);
//            mMap.moveCamera(postion);
//            mMap.addMarker(new MarkerOptions()
//                    .position(new LatLng(mEvent.getLatitude(), mEvent.getLongitude()))
//                    .draggable(false)
//                    .title(mEvent.getName()));
//            mMap.getUiSettings().setAllGesturesEnabled(false);
//            mMap.getUiSettings().setZoomControlsEnabled(true);
//            mMapContainer.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private void showPages() {
//
//        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        for (EventDescriptionObject page : mEvent.getPages()) {
//            LinearLayout view = (LinearLayout) inflater.inflate(R.layout.activity_single_event_desc_item, null);
//            TextView tx = (TextView) view.findViewById(R.id.activity_single_event_desc_item_title);
//            if (!page.getPageName().isEmpty()) {
//                tx.setText(page.getPageName());
//            } else {
//                tx.setText("Allgemein");
//            }
//            final EventDescriptionObject fpage = page;
//            view.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    //SingleEventDescriptionFragment.getInstance(getActivity(), fpage.getPageName(), mEvent.getId(), fpage.getId());
//                }
//            });
//            mPagesList.addView(view);
//        }
//        mPages.setVisibility(View.VISIBLE);
//    }

}
