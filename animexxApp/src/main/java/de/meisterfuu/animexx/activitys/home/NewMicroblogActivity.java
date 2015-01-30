package de.meisterfuu.animexx.activitys.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseActivityAB;
import de.meisterfuu.animexx.activitys.share.ImagePickerActivity;
import de.meisterfuu.animexx.api.broker.HomeBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.FileUploadReturnObject;
import de.meisterfuu.animexx.objects.home.ContactHomeObject;
import de.meisterfuu.animexx.utils.imageloader.PicassoDownloader;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NewMicroblogActivity extends AnimexxBaseActivityAB implements View.OnClickListener, Callback<ReturnObject<ContactHomeObject>> {


    public static void getInstance(Context pContext) {
        Intent i = new Intent().setClass(pContext, NewMicroblogActivity.class);
        pContext.startActivity(i);
    }

    private EditText mText;
    private Button mButton;
    private ImageView mPicture;

    private long pictureID;
    private String pictureThumb;

    private boolean success = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_new_microblog);
        mText = (EditText) findViewById(R.id.activity_new_microblog_edittext);
        mButton = (Button) findViewById(R.id.activity_new_microblog_add_picture);
        mPicture = (ImageView) findViewById(R.id.activity_new_microblog_picture);

        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(pictureID == -1){
            ImagePickerActivity.getInstance(this, 1);
            finish();
        } else {
            PreferenceManager.getDefaultSharedPreferences(NewMicroblogActivity.this).edit()
                    .remove("mb_draft_url")
                    .remove("mb_draft_id")
                    .commit();
            pictureID = -1;
            pictureThumb = "";
            mPicture.setVisibility(View.GONE);
            mButton.setText(getResources().getText(R.string.patio_attach_picture));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(!success){
            PreferenceManager.getDefaultSharedPreferences(this).edit()
                    .putString("mb_draft_text", mText.getText().toString())
                    .putString("mb_draft_url", pictureThumb)
                    .putLong("mb_draft_id", pictureID)
                    .commit();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String text = PreferenceManager.getDefaultSharedPreferences(this).getString("mb_draft_text", "");
        pictureThumb = PreferenceManager.getDefaultSharedPreferences(this).getString("mb_draft_url", "");
        pictureID = PreferenceManager.getDefaultSharedPreferences(this).getLong("mb_draft_id", -1);
        mText.setText(text);

        if(pictureID != -1){
            mPicture.setVisibility(View.VISIBLE);
            PicassoDownloader.getPicasso(this).load(pictureThumb).into(mPicture);
            mButton.setText("Bild entfernen");
        } else {
            mPicture.setVisibility(View.GONE);
            mButton.setText(getResources().getText(R.string.patio_attach_picture));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_microblog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.send) {
            send();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void send() {
        HomeBroker broker = new HomeBroker(this);
        if(pictureID != -1){
            broker.getWebApi().getApi().sendMicroblog(mText.getText().toString(), pictureID, this);
        } else {
            broker.getWebApi().getApi().sendMicroblog(mText.getText().toString(), this);
        }
    }

    @Override
    public void success(ReturnObject<ContactHomeObject> contactHomeObjectReturnObject, Response response) {
        if (contactHomeObjectReturnObject.getObj() != null) {
            success = true;
            PreferenceManager.getDefaultSharedPreferences(NewMicroblogActivity.this).edit()
                    .remove("mb_draft_text")
                    .remove("mb_draft_url")
                    .remove("mb_draft_id")
                    .commit();
            finish();
        } else {
            Toast.makeText(NewMicroblogActivity.this, "Microblog senden fehlgeschlagen.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void failure(RetrofitError error) {
        Toast.makeText(NewMicroblogActivity.this, "Microblog senden fehlgeschlagen.", Toast.LENGTH_SHORT).show();
    }
}
