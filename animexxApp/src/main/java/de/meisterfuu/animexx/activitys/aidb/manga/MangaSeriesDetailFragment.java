package de.meisterfuu.animexx.activitys.aidb.manga;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseFragment;
import de.meisterfuu.animexx.activitys.rpg.RPGDetailActivity;
import de.meisterfuu.animexx.api.Events;
import de.meisterfuu.animexx.api.broker.MangaBroker;
import de.meisterfuu.animexx.api.broker.RPGBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.aidb.MangaDbObject;
import de.meisterfuu.animexx.objects.aidb.MangaSeriesObject;
import de.meisterfuu.animexx.objects.rpg.RPGObject;
import de.meisterfuu.animexx.utils.views.TableDataView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MangaSeriesDetailFragment extends AnimexxBaseFragment {

    private long mId;
    private TableDataView mTableView;
    private TextView mTitle, mSubtitle;
    private MangaBroker mApi;
    private MangaSeriesObject mSeries;
    private int inPossession;
    private boolean fetched = false;

    public static MangaSeriesDetailFragment newInstance(long pID) {
        MangaSeriesDetailFragment fragment = new MangaSeriesDetailFragment();
        Bundle args = new Bundle();
        args.putLong("id", pID);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_rpgdetail, container, false);
        mTableView = (TableDataView) view.findViewById(R.id.activity_rpg_single_body_table);
        mTitle = (TextView) view.findViewById(R.id.activity_rpg_single_body_title);
        mSubtitle = (TextView) view.findViewById(R.id.activity_rpg_single_body_subtitle);
        mId = this.getArguments().getLong("id", -1);

        mApi = new MangaBroker(this.getActivity());
        mSeries = ((MangaSeriesActivity)getActivity()).getSeries();

        mTitle.setText(mSeries.getName());

        String subtitle = mSeries.getThemeName() +"\n\n";

        Set<String> author = new HashSet<>();

        if(mSeries.getPerson() != null){
            for(Map.Entry<String, JsonElement> entry: mSeries.getPerson().entrySet()){
                if(!entry.getValue().getAsJsonObject().entrySet().isEmpty()){
                    String person = entry.getValue().getAsJsonObject().entrySet().iterator().next().getValue().getAsString();
                    if(author.add(person)){
                        subtitle += person+"\n";
                    }
                }
            }
        }

        if(mSeries.getApIntro() != null && !mSeries.getApIntro().isEmpty()){
            subtitle += "\n"+mSeries.getApIntro().trim();
        }

        mSubtitle.setText(subtitle);

        populateTable();

        return view;
    }

    private void populateTable() {
        mTableView.clear();

        if(mSeries.getDtl_angeschlossen() == 1){
            mTableView.add(new TableDataView.TableDataEntity("Abgeschlossen", R.drawable.ens_flags_forwarded_blue));
        } else if(mSeries.getDtl_angeschlossen() == 0) {
            mTableView.add(new TableDataView.TableDataEntity("Fortlaufend", R.drawable.ens_flags_forwarded_blue));
        }
        if(!fetched){
            mTableView.add(new TableDataView.TableDataEntity("???" +"/"+ mSeries.getVolumeCount() + " Bände im Besitz", R.drawable.ens_flags_forwarded_blue));
        } else {
            mTableView.add(new TableDataView.TableDataEntity(inPossession +"/"+ mSeries.getVolumeCount() + " Bände im Besitz", R.drawable.ens_flags_forwarded_blue));
        }

    }

    @Subscribe
    public void onMangaSeriresFetched(Events.MangaSeriesFetched event){
        if(event.getSeriesId() == mId){
            load();
        }
    }

    private void load() {
        List<MangaDbObject> tempList = mApi.getVolumes(mId);
        for(MangaDbObject manga: tempList){
            if(manga.isInPossession()){
                inPossession++;
            }
        }
        fetched = true;
        populateTable();
    }

}
