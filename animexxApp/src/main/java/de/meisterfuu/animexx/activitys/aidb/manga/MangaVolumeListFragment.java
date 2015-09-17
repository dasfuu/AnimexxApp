package de.meisterfuu.animexx.activitys.aidb.manga;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.squareup.otto.Subscribe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseFragment;
import de.meisterfuu.animexx.api.ApiEvent;
import de.meisterfuu.animexx.api.EventBus;
import de.meisterfuu.animexx.api.Events;
import de.meisterfuu.animexx.api.broker.MangaBroker;
import de.meisterfuu.animexx.objects.aidb.MangaDbObject;
import de.meisterfuu.animexx.objects.aidb.MyMangaObject;
import de.meisterfuu.animexx.services.MangaFetchService;
import de.meisterfuu.animexx.utils.views.FeedbackListView;

public class MangaVolumeListFragment extends AnimexxBaseFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static final int MODE_ALL = 1;
    public static final int MODE_MISSING = 2;
    public static final int MODE_OWN = 3;

    private MangaBroker mApi;
    private FeedbackListView mListView;

    private int mMode;
    private long mId;
    List<MangaDbObject> list;

    public static MangaVolumeListFragment newInstance(long pMangaSeriesId, int pMode) {
        if(pMode < 1 || pMode > 3){
            pMode = MODE_ALL;
        }
        MangaVolumeListFragment fragment = new MangaVolumeListFragment();
        Bundle args = new Bundle();
        args.putLong("id", pMangaSeriesId);
        args.putInt("mode", pMode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_manga_volumes, container, false);

        this.mMode = this.getArguments().getInt("mode");
        this.mId = this.getArguments().getLong("id");

        mApi = new MangaBroker(this.getActivity());

        mListView = (FeedbackListView) v.findViewById(android.R.id.list);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
        load(false);

        return v;
    }

    @Subscribe
    public void onMangaSeriresFetched(Events.MangaSeriesFetched event){
        if(event.getSeriesId() == mId){
            load(true);
        }
    }

    private void load(boolean silent) {
        if(!silent){
            mListView.showLoading();
        }
        List<MangaDbObject> tempList = mApi.getVolumes(mId);
        list = new ArrayList<>();

        if(mMode == MODE_ALL){
            list.addAll(tempList);
        } else if (mMode == MODE_MISSING){
            for(MangaDbObject manga: tempList){
                if(!manga.isInPossession()){
                    list.add(manga);
                }
            }
        } else if(mMode == MODE_OWN){
            for(MangaDbObject manga: tempList){
                if(manga.isInPossession()){
                    list.add(manga);
                }
            }
        }

        mListView.setAdapter(new ArrayAdapter<MangaDbObject>(MangaVolumeListFragment.this.getActivity(), android.R.layout.simple_list_item_1, list));
        mListView.showList();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        editVolumeDialog(list.get(position).toString(), list.get(position).getMangaId(), position);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        editVolumeDialog(list.get(position).toString(), list.get(position).getMangaId(), position);
        return true;
    }


    private void editVolumeDialog(String title, final Long id, final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());

        if(mMode == MODE_ALL){
            return;
        } else if (mMode == MODE_MISSING){
            builder.setTitle("Hinzufügen");
            builder.setMessage("Möchstest du den Manga '"+ title +"' deiner Sammlung hinzufügen?");
        } else if(mMode == MODE_OWN){
            builder.setTitle("Entfernen");
            builder.setMessage("Möchstest du den Manga '"+ title +"' aus deiner Sammlung entfernen?");
        }

        builder.setPositiveButton("Ja", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (mMode == MODE_MISSING){
                    ArrayList<Long> listtemp = new ArrayList<>();
                    listtemp.add(id);
                    mApi.addManga(listtemp);
                    list.remove(position);
                    ((ArrayAdapter<MangaDbObject>) mListView.getAdapter()).notifyDataSetChanged();
                } else if(mMode == MODE_OWN){
                    ArrayList<Long> listtemp = new ArrayList<>();
                    listtemp.add(id);
                    mApi.removeManga(listtemp);
                    list.remove(position);
                    ((ArrayAdapter<MangaDbObject>) mListView.getAdapter()).notifyDataSetChanged();
                }
                dialog.dismiss();
            }

        });

        builder.setNegativeButton("Nein", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void refresh() {
        load(true);
    }

}
