package de.meisterfuu.animexx.activitys.share;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseActivityAB;
import de.meisterfuu.animexx.activitys.home.NewMicroblogActivity;
import de.meisterfuu.animexx.adapter.FileAdapter;
import de.meisterfuu.animexx.api.broker.FileBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.UploadedFile;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WebDavImageActivity extends AnimexxBaseActivityAB implements Callback<ReturnObject<List<UploadedFile>>>, AdapterView.OnItemClickListener, View.OnClickListener {

    @InjectView(R.id.gridView)
    GridView mGridView;
    private FileAdapter mAdapter;
    @InjectView(R.id.webdav_float_upload)
    FloatingActionButton mFloatButton;

    public static void getInstance(Context pContext, int mode) {
        Intent i = new Intent().setClass(pContext, WebDavImageActivity.class);
        Bundle args = new Bundle();
        args.putInt("mode", mode);
        i.putExtras(args);
        pContext.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_dav_image);
        ButterKnife.inject(this);
        mFloatButton.setOnClickListener(this);
        mFloatButton.attachToListView(mGridView);
    }

    @Override
    public void onResume() {
        super.onResume();
        FileBroker broker = new FileBroker(this);
        broker.getImageFileList(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web_dav_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void success(ReturnObject<List<UploadedFile>> listReturnObject, Response response) {
        mAdapter = new FileAdapter(listReturnObject.getObj(), this);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
    }

    @Override
    public void failure(RetrofitError error) {
        Toast.makeText(this, "Bilder konnten nicht abgerufen werden.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putString("mb_draft_url", mAdapter.getItem(position).getThumbnailUrl())
                .putLong("mb_draft_id", mAdapter.getItem(position).getId())
                .commit();

        NewMicroblogActivity.getInstance(this);
        finish();
    }

    @Override
    public void onClick(View v) {
        ImagePickerActivity.getInstance(this, 3);
    }
}
