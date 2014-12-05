package de.meisterfuu.animexx.activitys.profiles;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.api.broker.UserBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.profile.ProfileBoxObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProfilePageHTMLFragment extends Fragment {

    private UserBroker mApi;
    private String mBoxID;
    private long mID;
    private WebView mWebView;

    public static Fragment getInstance(Context pContext, long pUserID, String pBoxID) {
        ProfilePageHTMLFragment fragment = new ProfilePageHTMLFragment();
        Bundle args = new Bundle();
        args.putLong("id", pUserID);
        args.putString("boxid", pBoxID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile_page_html, container, false);

        if (getArguments() != null) {
            mID = getArguments().getLong("id");
            mBoxID = getArguments().getString("boxid");
        }

        mApi = new UserBroker(this.getActivity());

        mWebView = (WebView) v.findViewById(R.id.fragment_profile_page_html_html);

        load();

        return v;

    }

    private void load() {

        mApi.getProfileBox(mBoxID, mID, new Callback<ReturnObject<ProfileBoxObject>>() {
            @Override
            public void success(ReturnObject<ProfileBoxObject> o, Response response) {
                ProfileBoxObject obj = (ProfileBoxObject) o.getObj();
                mWebView.loadDataWithBaseURL("https://animexx.de", obj.getHtml(), "text/html", "UTF-8", null);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

}
