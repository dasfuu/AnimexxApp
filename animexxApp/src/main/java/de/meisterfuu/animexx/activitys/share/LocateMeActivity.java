package de.meisterfuu.animexx.activitys.share;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.objects.LocateMeObject;

public class LocateMeActivity extends Activity {


    SimpleDateFormat sdf = new SimpleDateFormat("'Datum: 'HH:mm dd.MM.yyyy", Locale.getDefault());

    LocateMeObject mLocObj;
    private GoogleMap mMap;

    FrameLayout mHeader, mMapContainer;
    ImageView mLogo;
    TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate_me);

        // Show the Up button in the action bar.
        mHeader = (FrameLayout) this.findViewById(R.id.activity_locateme_single_header);
        mMapContainer = (FrameLayout) this.findViewById(R.id.activity_locateme_single_map);


        mLogo = (ImageView) this.findViewById(R.id.activity_locateme_single_user_avatar);

        mText = (TextView) this.findViewById(R.id.activity_locateme_single_user_text);

        mHeader.setVisibility(View.GONE);
        mMapContainer.setVisibility(View.GONE);

        LocateMeActivity.this.getActionBar().setTitle("");

        if (this.getIntent().hasExtra("obj")) {
            Bundle extras = this.getIntent().getExtras();
            mLocObj = new Gson().fromJson(extras.getString("obj"), LocateMeObject.class);
        } else {
            finish();
            return;
        }


        Picasso.with(this).load(mLocObj.getImageURLPlace()).into(mLogo);


        mText.setText(mLocObj.getText());

        mHeader.setVisibility(View.VISIBLE);


        showMap();


        setupActionBar();
    }

    protected void showMap() {
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.activity_locateme_single_map_fragment)).getMap();
        if (mMap != null) {
            // The Map is verified. It is now safe to manipulate the map.
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            CameraUpdate postion = CameraUpdateFactory.newLatLngZoom(new LatLng(mLocObj.getLatitude(), mLocObj.getLongitude()), 15);
            mMap.moveCamera(postion);
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(mLocObj.getLatitude(), mLocObj.getLongitude()))
                    .draggable(false)
                    .title(mLocObj.getUsername()));
            mMap.getUiSettings().setAllGesturesEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMapContainer.setVisibility(View.VISIBLE);
        }
    }

    public static void getInstance(Context pContext, LocateMeObject pObj) {
        Intent i = new Intent().setClass(pContext, LocateMeActivity.class);
        Bundle args = new Bundle();
        args.putString("obj", new Gson().toJson(pObj));
        i.putExtras(args);
        pContext.startActivity(i);
    }

    public static PendingIntent getPendingIntent(Context pContext, LocateMeObject pObj) {
        Intent intent = new Intent(pContext, LocateMeActivity.class);
        Bundle args = new Bundle();
        args.putString("obj", new Gson().toJson(pObj));
        intent.putExtras(args);
        return PendingIntent.getActivity(pContext, 0, intent, 0);
    }

    /**
     * Set up the {@link android.app.ActionBar}.
     */
    private void setupActionBar() {

        getActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.locate_me, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
