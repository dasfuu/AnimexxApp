package de.meisterfuu.animexx.activitys.ens;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseActivityAB;
import de.meisterfuu.animexx.api.broker.ENSBroker;
import de.meisterfuu.animexx.objects.ens.ENSDraftObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class FeedbackActivity extends AnimexxBaseActivityAB {

    private EditText text;

    public static void getInstance(Context pContext) {
        Intent i = new Intent().setClass(pContext, FeedbackActivity.class);
        pContext.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        text = (EditText) this.findViewById(R.id.activity_feedback_text);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
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
            ENSDraftObject draft = new ENSDraftObject();

            draft.setMessage(text.getText().toString());

            ArrayList<Long> fuu = new ArrayList<Long>(1);
            fuu.add(586283L);
            draft.setRecipients(fuu);

            draft.setSubject("App-Feedback");

            PackageInfo pInfo = null;
            int version = 0;
            String versionName = "Wrong package";
            try {
                pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                version = pInfo.versionCode;
                versionName = pInfo.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

            draft.setSignature("Feedback "+versionName+" ("+version+")");

            new ENSBroker(this).sendENS(draft, new Callback<Long>() {
                @Override
                public void success(Long aLong, Response response) {
                    FeedbackActivity.this.finish();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(FeedbackActivity.this, "Fehler beim senden.", Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
