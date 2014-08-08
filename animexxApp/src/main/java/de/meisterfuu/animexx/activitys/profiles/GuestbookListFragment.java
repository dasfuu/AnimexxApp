package de.meisterfuu.animexx.activitys.profiles;

import android.os.Bundle;
import android.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import de.meisterfuu.animexx.adapter.GBListAdapter;

public class GuestbookListFragment extends ListFragment {

    private static final String USER_ID = "mUserID";

    private long mUserID;
	private GBListAdapter mAdapter;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mUserID = getArguments().getLong(USER_ID);
        }

	    mAdapter = new GBListAdapter(this.getActivity(), mUserID);
        setListAdapter(mAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }



}
