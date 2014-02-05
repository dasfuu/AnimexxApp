package de.meisterfuu.animexx.activitys.home;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.R.layout;
import de.meisterfuu.animexx.dummy.DummyContent;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks} interface.
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
	private ListAdapter mAdapter;


	// TODO: Rename and change types of parameters
	public static HomeObjectFragment newInstance() {
		HomeObjectFragment fragment = new HomeObjectFragment();
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
		mAdapter = new ArrayAdapter<DummyContent.DummyItem>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_homeobject, container, false);

		// Set the adapter
		mListView = (AbsListView) view.findViewById(android.R.id.list);
		((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

		// Set OnItemClickListener so we can be notified on item clicks
		mListView.setOnItemClickListener(this);

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
	public void setEmptyText(CharSequence emptyText) {
		View emptyView = mListView.getEmptyView();

		if (emptyText instanceof TextView) {
			((TextView) emptyView).setText(emptyText);
		}
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p>
	 * See the Android Training lesson <a href= "http://developer.android.com/training/basics/fragments/communicating.html" >Communicating with Other
	 * Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {

		// TODO: Update argument type and name
		public void onFragmentInteraction(String id);
	}

}
