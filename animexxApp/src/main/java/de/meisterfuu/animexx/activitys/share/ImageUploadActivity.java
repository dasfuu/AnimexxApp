package de.meisterfuu.animexx.activitys.share;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseActivityAB;
import de.meisterfuu.animexx.adapter.FileUploadedAdapter;
import de.meisterfuu.animexx.api.UploadProgressEvent;
import de.meisterfuu.animexx.objects.FileUploadReturnObject;
import de.meisterfuu.animexx.services.UploadService;
import de.meisterfuu.animexx.utils.views.FeedbackListView;

public class ImageUploadActivity extends AnimexxBaseActivityAB {




    public static void getInstance(Context pContext, ArrayList<String> files) {
        Intent i = new Intent().setClass(pContext, ImageUploadActivity.class);
        Bundle b = new Bundle();
        b.putStringArrayList("files", files);
        i.putExtras(b);
        pContext.startActivity(i);
    }

    private FeedbackListView mListview;
    private FileUploadedAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        mListview = (FeedbackListView) this.findViewById(R.id.listView);
        mAdapter = new FileUploadedAdapter(new ArrayList<FileUploadReturnObject>(), this);

        mListview.setAdapter(mAdapter);
        mListview.showLoading();

        Bundle b = this.getIntent().getExtras();
        ArrayList<String> files = b.getStringArrayList("files");
        UploadService.startAction(this, files, this.getCallerID());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_upload, menu);
        return true;
    }

    private boolean first = true;

    @Subscribe
    public void onItemUploaded(UploadProgressEvent event){
        if(first){
            mListview.showList();
            first = false;
        }
        mAdapter.add(event.getObj().getObj());
    }

}
