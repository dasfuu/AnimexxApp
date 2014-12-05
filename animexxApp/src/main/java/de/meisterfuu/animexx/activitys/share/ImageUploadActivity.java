package de.meisterfuu.animexx.activitys.share;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import de.meisterfuu.animexx.R;

public class ImageUploadActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_upload);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.image_upload, menu);
        return true;
    }

}
