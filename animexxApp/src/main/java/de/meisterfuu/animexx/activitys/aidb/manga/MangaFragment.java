package de.meisterfuu.animexx.activitys.aidb.manga;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.melnykov.fab.FloatingActionButton;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseFragment;
import de.meisterfuu.animexx.activitys.profiles.ProfileActivity;
import de.meisterfuu.animexx.adapter.ContactAdapter;
import de.meisterfuu.animexx.api.ApiEvent;
import de.meisterfuu.animexx.api.broker.MangaBroker;
import de.meisterfuu.animexx.api.broker.UserBroker;
import de.meisterfuu.animexx.objects.UserObject;
import de.meisterfuu.animexx.objects.aidb.MyMangaObject;
import de.meisterfuu.animexx.utils.views.FeedbackListView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MangaFragment extends AnimexxBaseFragment implements AdapterView.OnItemClickListener, View.OnClickListener {


    private MangaBroker mApi;
    private FeedbackListView mListView;
    private FloatingActionButton mFloatButton;

    public static Fragment getInstance() {
        MangaFragment fragment = new MangaFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_manga_my, container, false);


        mApi = new MangaBroker(this.getActivity());

        mListView = (FeedbackListView) v.findViewById(android.R.id.list);
        mListView.setOnItemClickListener(this);

        mFloatButton = (FloatingActionButton) v.findViewById(R.id.manga_float_scan);

        mFloatButton.setOnClickListener(this);
        mFloatButton.attachToListView(mListView);

        load();

        return v;
    }

    private void load() {
        mListView.showLoading();

        MangaBroker mApi = new MangaBroker(this.getActivity());
        mApi.getMyMangaList(this.getCallerID());
    }

    @Subscribe
    public void receiveList(ApiEvent.MyMangaEvent event){
        mListView.setAdapter(new ArrayAdapter<MyMangaObject>(MangaFragment.this.getActivity(), android.R.layout.simple_list_item_1, event.getObj()));
        mListView.showList();
    }

    @Subscribe
    public void onError(ApiEvent.ErrorEvent event){
        mListView.showError("Fehler");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MangaSeriesActivity.getInstance(this.getActivity(), ((MyMangaObject)mListView.getAdapter().getItem(position)).getMangaId());
    }

    @Override
    public void onClick(View v) {
        MangaScanActivity.getInstance(this.getActivity());
    }
}
