package de.meisterfuu.animexx.activitys.share;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseActivityAB;
import de.meisterfuu.animexx.utils.patio.Patio;

public class ImagePickerActivity extends AnimexxBaseActivityAB implements Patio.PatioCallbacks {

    public static void getInstance(Context pContext) {
        Intent i = new Intent().setClass(pContext, ImagePickerActivity.class);
        pContext.startActivity(i);
    }


    public static final int REQUEST_CODE_TAKE_PICTURE = 1000;
    public static final int REQUEST_CODE_ATTACH_PICTURE = 2000;

    private Patio mPatio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);
        mPatio = (Patio) findViewById(R.id.patio);
        mPatio.setCallbacksListener(this);
    }

    @Override
    public void onTakePictureClick() {
        Intent intent = mPatio.getTakePictureIntent();
        startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
    }

    @Override
    public void onAddPictureClick() {
        Intent intent = mPatio.getAttachPictureIntent();
        startActivityForResult(intent, REQUEST_CODE_ATTACH_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == AnimexxBaseActivityAB.RESULT_OK && requestCode == REQUEST_CODE_ATTACH_PICTURE) {
            mPatio.handleAttachPictureResult(data);
        }
        if(resultCode == AnimexxBaseActivityAB.RESULT_OK && requestCode == REQUEST_CODE_TAKE_PICTURE) {
            mPatio.handleTakePictureResult(data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_picker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_finished) {
            ImageUploadActivity.getInstance(this, mPatio.getThumbnailsPaths());
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
