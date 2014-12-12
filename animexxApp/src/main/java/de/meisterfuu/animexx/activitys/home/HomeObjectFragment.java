package de.meisterfuu.animexx.activitys.home;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.melnykov.fab.FloatingActionButton;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.adapter.HomeContactAdapter;
import de.meisterfuu.animexx.api.broker.HomeBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.home.ContactHomeObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView with a GridView.
 * <p/>
 */
public class HomeObjectFragment extends Fragment implements AbsListView.OnItemClickListener, View.OnClickListener {


    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private HomeContactAdapter mAdapter;
    private ArrayList<ContactHomeObject> mList;

    private HomeBroker mAPI;




    private FloatingActionButton mFloatButton;
    private boolean paused;

    public static Fragment getInstance() {
        Fragment fragment = new HomeObjectFragment();
        return fragment;
    }


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HomeObjectFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//		if (getArguments() != null) {
//			mParam1 = getArguments().getString(ARG_PARAM1);
//		}

        // TODO: Change Adapter to display your content
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homeobject_list, container, false);
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mFloatButton = (FloatingActionButton) view.findViewById(R.id.home_float_new);
        mAPI = new HomeBroker(this.getActivity());

        mFloatButton.setOnClickListener(this);
        mFloatButton.attachToListView(mListView);

        init();

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


    }


    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
//	public void setEmptyText(CharSequence emptyText) {
//		View emptyView = mListView.getEmptyView();
//
//		if (emptyText instanceof TextView) {
//			((TextView) emptyView).setText(emptyText);
//		}
//	}
    @Override
    public void onResume() {

//		mAPI = new HomeApi(this.getActivity());
//		init();

        super.onResume();

        if (paused) {
            paused = false;
            loadNew();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        mList = new ArrayList<ContactHomeObject>();
        mAdapter = new HomeContactAdapter(mList, HomeObjectFragment.this.getActivity());
        SwingBottomInAnimationAdapter swingAdapter = new SwingBottomInAnimationAdapter(mAdapter);
        swingAdapter.setAbsListView(mListView);
        mListView.setAdapter(swingAdapter);


        loadEntries();
    }

    @Override
    public void onPause() {
        super.onPause();
        paused = true;
    }

    private void loadNew() {
        mAPI.getContactWidgetList(new Callback<ReturnObject<List<ContactHomeObject>>>() {
            @Override
            public void success(ReturnObject<List<ContactHomeObject>> listReturnObject, Response response) {
                List<ContactHomeObject> list = listReturnObject.getObj();
                ArrayList<ContactHomeObject> list_ = new ArrayList<ContactHomeObject>();
                ContactHomeObject start = mAdapter.getItem(0);
                for (ContactHomeObject obj : list) {
                    if (obj.isMultiItem() == false) {
                        if (start.getServerTS() < obj.getServerTS()) list_.add(obj);
                    } else {
                        for (ContactHomeObject obj_ : obj.getChildItems()) {
                            if (start.getServerTS() < obj.getServerTS()) list_.add(obj_);
                        }
                    }
                }
                Collections.sort(list_);
                mAdapter.addAllBeginning(list_);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void loadEntries() {

        mAPI.getContactWidgetList(new Callback<ReturnObject<List<ContactHomeObject>>>() {
            @Override
            public void success(ReturnObject<List<ContactHomeObject>> listReturnObject, Response response) {
                List<ContactHomeObject> list = listReturnObject.getObj();
                ArrayList<ContactHomeObject> list_ = new ArrayList<ContactHomeObject>();
                for (ContactHomeObject obj : list) {
                    if (obj.isMultiItem() == false) {
                        list_.add(obj);
                    } else {
                        for (ContactHomeObject obj_ : obj.getChildItems()) {
                            list_.add(obj_);
                        }
                    }
                }
                Collections.sort(list_);
                mAdapter.addAll(list_);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        NewMicroblogActivity.getInstance(this.getActivity());
    }
}
