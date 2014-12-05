package de.meisterfuu.animexx.activitys.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseActivityAB;
import de.meisterfuu.animexx.api.broker.HomeBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.home.ContactHomeObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NewMicroblogActivity extends AnimexxBaseActivityAB {


    public static void getInstance(Context pContext) {
        Intent i = new Intent().setClass(pContext, NewMicroblogActivity.class);
        pContext.startActivity(i);
    }


    EditText mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_microblog);
        mText = (EditText) findViewById(R.id.activity_new_microblog_edittext);
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
        broker.getWebApi().getApi().sendMicroblog(mText.getText().toString(), new Callback<ReturnObject<ContactHomeObject>>() {
            @Override
            public void success(ReturnObject<ContactHomeObject> contactHomeObjectReturnObject, Response response) {
                if (contactHomeObjectReturnObject.getObj() != null) {
                    finish();
                } else {
                    Toast.makeText(NewMicroblogActivity.this, "Microblog senden fehlgeschlagen.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(NewMicroblogActivity.this, "Microblog senden fehlgeschlagen.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
