package de.meisterfuu.animexx.activitys.aidb.manga;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.otto.Subscribe;

import java.util.Arrays;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseActivityAB;
import de.meisterfuu.animexx.api.ApiEvent;
import de.meisterfuu.animexx.api.broker.MangaBroker;

public class MangaScanActivity extends AnimexxBaseActivityAB implements View.OnClickListener {



    public static void getInstance(Context pContext) {
        Intent i = new Intent().setClass(pContext, MangaScanActivity.class);
        pContext.startActivity(i);
    }

    MangaBroker mApi;
    ImageView button;
    TextView status;
    private String lastManga;
    private boolean scanning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_scan);

        mApi = new MangaBroker(this);

        button = (ImageView) findViewById(R.id.activity_manga_scan_button);
        status = (TextView) findViewById(R.id.activity_manga_scan_laststatus_text);
        status.setText("");
        setTitle("Manga hinzufügen");

        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        scan();
    }

    public void scan(){
        if(scanning){
            return;
        }
        IntentIntegrator scanIntent = new IntentIntegrator(this);
        scanIntent.initiateScan(IntentIntegrator.PRODUCT_CODE_TYPES);
        scanIntent.setTitle("Manga hinzufügen");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null && scanResult.getContents() != null) {
            try{
                mApi.resolveEAN(Long.parseLong(scanResult.getContents()), this.getCallerID());
                scanning = true;
            } catch (NumberFormatException e){
//                this.finish();
            }
        } else {
//            this.finish();
        }
    }

    @Subscribe
    public void onEANResult(ApiEvent.EANResultEvent event){
        if(event.getObj().getType().equalsIgnoreCase("manga")){
            lastManga = event.getObj().getName();
            mApi.addManga(Arrays.asList(event.getObj().getId()), this.getCallerID());
        } else {
            scanning = false;
            status.setText("Es wurde leider kein passender Band gefunden.");
        }
    }

    @Subscribe
    public void onMangaAdded(ApiEvent.MangaAddedEvent event){
        scanning = false;
        status.setText(lastManga + " zur Sammlung hinzugefuegt.");
        lastManga = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manga_scan, menu);
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


}
