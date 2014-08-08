package de.meisterfuu.animexx.activitys.home;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.adapter.HomeContactAdapter;
import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.data.home.HomeApi;
import de.meisterfuu.animexx.objects.home.ContactHomeObject;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.Request;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView with a GridView.
 * <p />
 */
public class HomeObjectFragment extends Fragment implements AbsListView.OnItemClickListener {


	/**
	 * The fragment's ListView/GridView.
	 */
	private AbsListView mListView;

	/**
	 * The Adapter which will be used to populate the ListView/GridView with
	 * Views.
	 */
	private HomeContactAdapter mAdapter;

	private HomeApi mAPI;

	private ArrayList<ContactHomeObject> mList;


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
		Request.config = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
		View view = inflater.inflate(R.layout.fragment_homeobject, container, false);
		mListView = (AbsListView) view.findViewById(android.R.id.list);
		mAPI = new HomeApi(this.getActivity());
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
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void init(){
		// Set OnItemClickListener so we can be notified on item clicks
		mListView.setOnItemClickListener(this);
		
		mList = new ArrayList<ContactHomeObject>();
		mAdapter = new HomeContactAdapter(mList, HomeObjectFragment.this.getActivity());
		SwingBottomInAnimationAdapter swingAdapter = new SwingBottomInAnimationAdapter(mAdapter);
		swingAdapter.setAbsListView(mListView);
		mListView.setAdapter(swingAdapter);
//		mListView.setDivider(null);
		mListView.setPadding(15, 0, 15, 0);
		loadEntries();		
	}

	
	private void loadEntries(){
				
		mAPI.getContactWidgetList(HomeApi.LIST_ALL ,new APICallback(){

			@SuppressWarnings("unchecked")
			@Override
			public void onCallback(APIException pError, Object pObject) {
				ArrayList<ContactHomeObject> list = (ArrayList<ContactHomeObject>) pObject;
				ArrayList<ContactHomeObject> list_ = new ArrayList<ContactHomeObject>();
				for(ContactHomeObject obj: list){
					if(obj.isMultiItem() == false){
						list_.add(obj);
					} else {
						for(ContactHomeObject obj_: obj.getChildItems()){
								list_.add(obj_);
						}
					}
				}
				Collections.sort(list_);
				mAdapter.addAll(list_);			
			}
		});

	}

}
