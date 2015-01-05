package de.meisterfuu.animexx.activitys.share;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.api.web.WebAPI;
import de.meisterfuu.animexx.objects.FileUploadReturnObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

import static de.meisterfuu.animexx.utils.imageloader.BitmapLoaderCustom.getTestPictureFile;

public class ImageUploadActivity extends Activity {



    public static void getInstance(Context pContext) {
        Intent i = new Intent().setClass(pContext, ImageUploadActivity.class);
        pContext.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
        WebAPI api = new WebAPI(this);
        TypedFile file = new TypedFile("image/png", getTestPictureFile(this));
        api.getApi().uploadFile("/testfile.png", file, new Callback<ReturnObject<FileUploadReturnObject>>() {
            @Override
            public void success(ReturnObject<FileUploadReturnObject> r, Response response) {
                ((TextView)findViewById(R.id.textView)).setText(r.getObj().toString());
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_upload, menu);
        return true;
    }

}
