package de.meisterfuu.animexx.activitys.share;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseActivityAB;
import de.meisterfuu.animexx.adapter.FileAdapter;
import de.meisterfuu.animexx.api.broker.FileBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.UploadedFile;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class WebDavImageActivity extends AnimexxBaseActivityAB implements Callback<ReturnObject<List<UploadedFile>>> {

    @InjectView(R.id.gridView)
    GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_dav_image);
        ButterKnife.inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        FileBroker broker = new FileBroker(this);
        broker.getFileList(this);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void success(ReturnObject<List<UploadedFile>> listReturnObject, Response response) {
        FileAdapter adapter = new FileAdapter(listReturnObject.getObj(), this);
        mGridView.setAdapter(adapter);
    }

    @Override
    public void failure(RetrofitError error) {

    }
}
