package de.meisterfuu.animexx.activitys.rpg;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseFragment;
import de.meisterfuu.animexx.objects.rpg.RPGObject;

public class RPGDetailPageFragment extends AnimexxBaseFragment  {

    private long mRPGID;
    private WebView mWebView;
    private RPGObject mRPG;
    private int mPageID;

    public static RPGDetailPageFragment newInstance(long pID, int pIndex) {
        RPGDetailPageFragment fragment = new RPGDetailPageFragment();
        Bundle args = new Bundle();
        args.putLong("id", pID);
        args.putInt("index", pIndex);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rpg_detail_page, container, false);
        mWebView = (WebView) view.findViewById(R.id.fragment_rpg_detail_page_html);

        mRPGID = this.getArguments().getLong("id", -1);
        mPageID = this.getArguments().getInt("index", -1);
        mRPG = ((RPGDetailActivity)getActivity()).getRPG();
        mWebView.loadDataWithBaseURL("https://animexx.de", mRPG.getDescriptionPages().get(mPageID).getHtml(), "text/html", "UTF-8", null);

        return view;
    }


}
