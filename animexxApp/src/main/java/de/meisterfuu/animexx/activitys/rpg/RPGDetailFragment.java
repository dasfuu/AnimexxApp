package de.meisterfuu.animexx.activitys.rpg;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseActivityAB;
import de.meisterfuu.animexx.activitys.AnimexxBaseFragment;
import de.meisterfuu.animexx.api.broker.RPGBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.rpg.RPGObject;
import de.meisterfuu.animexx.utils.views.TableDataView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RPGDetailFragment extends AnimexxBaseFragment implements Callback<ReturnObject<RPGObject>> {

    private long mRPGID;
    private RPGBroker mAPI;
    private RPGObject mRPG;
    private TableDataView mTableView;
    private TextView mTitle, mSubtitle;

    public static RPGDetailFragment newInstance(Context pContext, long pID) {
        RPGDetailFragment fragment = new RPGDetailFragment();
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
        mRPGID = this.getArguments().getLong("id", -1);
        mAPI = new RPGBroker(this.getActivity());

        mRPG = ((RPGDetailActivity)getActivity()).getRPG();

        mTitle.setText(mRPG.getName());
        mSubtitle.setText(mRPG.getTopicName());

//        mTableView.add(new TableDataView.TableDataEntity(mRPG.getPlayerCount()+" Charaktere", R.drawable.ens_flags_forwarded_blue));
        mTableView.add(new TableDataView.TableDataEntity(mRPG.getPostCount()+" Beiträge", R.drawable.ens_flags_forwarded_blue));
        if(mRPG.isTofu()){
            mTableView.add(new TableDataView.TableDataEntity("Tofu", R.drawable.ens_flags_forwarded_blue));
        }
        if(mRPG.isAdult()){
            mTableView.add(new TableDataView.TableDataEntity("Für Erwachsene", R.drawable.ens_flags_forwarded_blue));
        }
        return view;
    }


    @Override
    public void success(ReturnObject<RPGObject> rpgObjectReturnObject, Response response) {

    }

    @Override
    public void failure(RetrofitError error) {

    }
}
