package de.meisterfuu.animexx.activitys.profiles;



import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.data.profile.UserApi;
import de.meisterfuu.animexx.objects.ProfileObject;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.imageloader.ImageDownloaderCustom;
import de.meisterfuu.animexx.utils.imageloader.ImageSaveObject;
import de.meisterfuu.animexx.utils.views.TableDataView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ProfileFragment extends Fragment implements APICallback<ProfileObject> {


	private static final String USER_ID = "mUserID";

	private long mUserID;

	private FrameLayout commonFrame, contactFrame, boxFrame;
	private TableDataView commonTable, contactTable, boxTable;
	private ImageView profileImage;

	private UserApi mApi;

	private ImageDownloaderCustom mImageLoader;

	public static ProfileFragment newInstance(long pUserID) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putLong(USER_ID, pUserID);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
	        mUserID = getArguments().getLong(USER_ID);
        }

	    mApi = new UserApi(this.getActivity());
	    mImageLoader = new ImageDownloaderCustom("profilbild");

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
	    View v = inflater.inflate(R.layout.fragment_profile, container, false);

	    profileImage = (ImageView) v.findViewById(R.id.activity_profile_single_image);

	    commonFrame = (FrameLayout) v.findViewById(R.id.activity_profile_single_common);
	    contactFrame = (FrameLayout) v.findViewById(R.id.activity_profile_single_contact);
	    boxFrame = (FrameLayout) v.findViewById(R.id.activity_profile_single_pages);

	    commonTable = (TableDataView) v.findViewById(R.id.activity_profile_single_common_table);
	    contactTable = (TableDataView) v.findViewById(R.id.activity_profile_single_contact_table);
	    boxTable = (TableDataView) v.findViewById(R.id.activity_profile_single_pages_table);

        return v;
    }

	@Override
	public void onResume() {
		super.onResume();

		mApi.getProfile(mUserID, this);
	}

	@Override
	public void onCallback(final APIException pError, final ProfileObject pObject) {

		commonTable.clear();
		contactTable.clear();
		boxTable.clear();

		if(!pObject.getPictures().isEmpty()){
			mImageLoader.download(new ImageSaveObject(pObject.getPictures().get(0).getUrl(), mUserID+"_0"),profileImage);
			this.profileImage.setVisibility(View.VISIBLE);
		} else {
			this.profileImage.setVisibility(View.GONE);
		}

		commonTable.clear();
		commonTable.add(new TableDataView.TableDataEntity(pObject.getUsername(), R.drawable.ic_action_ens_new));

		for(ProfileObject.ProfileContactEntry entry: pObject.getContactData()){
			contactTable.add(new TableDataView.TableDataEntity(entry.getName()+": "+entry.getValue(), -1));
		}
		if(pObject.getContactData().isEmpty()){
			this.contactFrame.setVisibility(View.GONE);
		} else {
			this.contactFrame.setVisibility(View.VISIBLE);
		}

		for(ProfileObject.ProfileBoxEntry entry: pObject.getBoxes()){
			boxTable.add(new TableDataView.TableDataEntity(entry.getTitle(), R.drawable.ens_flags_forwarded_blue));
		}
		if(pObject.getBoxes().isEmpty()){
			this.boxFrame.setVisibility(View.GONE);
		} else {
			this.boxFrame.setVisibility(View.VISIBLE);
		}


	}
}
