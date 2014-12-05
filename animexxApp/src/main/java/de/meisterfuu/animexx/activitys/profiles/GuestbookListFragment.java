package de.meisterfuu.animexx.activitys.profiles;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.melnykov.fab.FloatingActionButton;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.adapter.GBListAdapter;

public class GuestbookListFragment extends Fragment implements View.OnClickListener {

    private static final String USER_ID = "mUserID";

    private long mUserID;
    private GBListAdapter mAdapter;
    private FloatingActionButton mFloatButton;
    private ListView mListView;

    public static GuestbookListFragment newInstance(long pUserID) {
        GuestbookListFragment fragment = new GuestbookListFragment();
        Bundle args = new Bundle();
        args.putLong(USER_ID, pUserID);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public GuestbookListFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gb_list, container, false);
        mListView = (ListView) view.findViewById(android.R.id.list);
        mFloatButton = (FloatingActionButton) view.findViewById(R.id.gb_float_new);
        mFloatButton.setOnClickListener(this);
        mFloatButton.attachToListView(mListView);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            mUserID = getArguments().getLong(USER_ID);
        }

        mAdapter = new GBListAdapter(this.getActivity(), mUserID);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View v) {
        GuestbookPostActivity.getInstance(this.getActivity(), mUserID);
    }
}
