package de.meisterfuu.animexx.activitys.profiles;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.objects.profile.ProfileObject;
import de.meisterfuu.animexx.utils.imageloader.ImageDownloaderCustom;
import de.meisterfuu.animexx.utils.imageloader.ImageSaveObject;
import de.meisterfuu.animexx.utils.imageloader.PicassoDownloader;
import de.meisterfuu.animexx.utils.views.FitImageView;
import de.meisterfuu.animexx.utils.views.TableDataView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {


    private static final String USER_ID = "mUserID";

    private long mUserID;

    private FrameLayout commonFrame, contactFrame;
    private TableDataView commonTable, contactTable;
    private ImageView profileImage;
    boolean created = false;

//	private UserApi mApi;

    private ImageDownloaderCustom mImageLoader;
    private ProfileObject mUser;
    private FrameLayout profileImageFrame;

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

        mImageLoader = new ImageDownloaderCustom("profilbild");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        profileImage = (ImageView) v.findViewById(R.id.activity_profile_single_image);
        profileImageFrame = (FrameLayout) v.findViewById(R.id.activity_profile_single_image_frame);

        commonFrame = (FrameLayout) v.findViewById(R.id.activity_profile_single_common);
        contactFrame = (FrameLayout) v.findViewById(R.id.activity_profile_single_contact);

        commonTable = (TableDataView) v.findViewById(R.id.activity_profile_single_common_table);
        contactTable = (TableDataView) v.findViewById(R.id.activity_profile_single_contact_table);

        v.setVisibility(View.INVISIBLE);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mUser != null) {
            profileImageFrame.setVisibility(View.VISIBLE);
            onCallback(mUser);
        } else {
            profileImageFrame.setVisibility(View.GONE);
            ProfileObject parentProfile = ((ProfileActivity) this.getActivity()).getProfile();
            if (parentProfile != null) {
                mUser = parentProfile;
                onCallback(mUser);
            }
        }
//		mApi.getProfile(mUserID, this);
    }

    public void onCallback(final ProfileObject pObject) {


        commonTable.clear();
        contactTable.clear();

        this.profileImageFrame.setVisibility(View.VISIBLE);
        if (!pObject.getPictures().isEmpty()) {
            Picasso picasso = new Picasso.Builder(this.getActivity()).downloader(new PicassoDownloader(this.getActivity(), "profilbild")).build();
            picasso.load(pObject.getPictures().get(0).getGoodUrl()).noFade().stableKey(mUserID + "_0").into(profileImage, new Callback() {
                @Override
                public void onSuccess() {
                    profileImageFrame.setVisibility(View.VISIBLE);
                }

                @Override
                public void onError() {
                    profileImageFrame.setVisibility(View.GONE);
                }
            });
            //mImageLoader.download(new ImageSaveObject(pObject.getPictures().get(0).getGoodUrl(), mUserID + "_0", false), profileImage);
        } else {
            this.profileImageFrame.setVisibility(View.GONE);
        }

        commonTable.clear();
        commonTable.add(new TableDataView.TableDataEntity(pObject.getUsername(), R.drawable.ic_action_ens_new));

        for (ProfileObject.ProfileContactEntry entry : pObject.getContactData()) {
            contactTable.add(new TableDataView.TableDataEntity(entry.getName() + ": " + entry.getValue(), -1));
        }
        if (pObject.getContactData().isEmpty()) {
            this.contactFrame.setVisibility(View.GONE);
        } else {
            this.contactFrame.setVisibility(View.VISIBLE);
        }


        this.getView().setVisibility(View.VISIBLE);
    }
}
