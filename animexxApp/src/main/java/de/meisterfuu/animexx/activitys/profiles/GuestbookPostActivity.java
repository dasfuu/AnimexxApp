package de.meisterfuu.animexx.activitys.profiles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseActivityAB;
import de.meisterfuu.animexx.adapter.GBAvatarSpinnerAdapter;
import de.meisterfuu.animexx.api.broker.GBBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.profile.GBDraftObject;
import de.meisterfuu.animexx.objects.profile.GBInfoObject;
import de.meisterfuu.animexx.objects.UserObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GuestbookPostActivity extends AnimexxBaseActivityAB implements Callback<ReturnObject<GBInfoObject>>, AdapterView.OnItemSelectedListener {


	public static void getInstance(Context pContext, long pID){
		Intent i = new Intent().setClass(pContext, GuestbookPostActivity.class);
		Bundle args = new Bundle();
		args.putLong("id", pID);
		i.putExtras(args);
		pContext.startActivity(i);
	}


	EditText mText;
	Spinner mAvatarSpinner;
    GBAvatarSpinnerAdapter mAdapter;

	private GBBroker mApi;
	private long mUserID;
	private long mAvatarID;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gbpost_new);

		mAvatarID = -1;


		mText = (EditText) this.findViewById(R.id.activity_gbpost_new_text);
		mAvatarSpinner = (Spinner) this.findViewById(R.id.activity_gbpost_new_avatar);
        mAvatarSpinner.setOnItemSelectedListener(this);
    }

	@Override
	public void onResume() {
        super.onResume();
		Bundle extras = this.getIntent().getExtras();
		mUserID = extras.getLong("id");

		mApi = new GBBroker(this);
		mApi.getGBInfo(mUserID, this);
	}


	@Override
	public void success(final ReturnObject<GBInfoObject> t, final Response response) {
        mAdapter = new GBAvatarSpinnerAdapter(t.getObj().getAvatars(), this);
        mAvatarSpinner.setAdapter(mAdapter);
	}

	@Override
	public void failure(final RetrofitError error) {

	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.guestbook_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_post) {
	        send();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

	private void send() {

		GBDraftObject draft = new GBDraftObject();
		draft.setText(mText.getText().toString());
		UserObject user = new UserObject();
		user.setId(mUserID);
		draft.setRecipient(user);
		draft.setAvatar(mAvatarID);

        mApi.postGBEntry(draft, new Callback<ReturnObject<Boolean>>() {
            @Override
            public void success(ReturnObject<Boolean> booleanReturnObject, Response response) {
                   finish();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(GuestbookPostActivity.this, "Eintrag konnte nicht gesendet werden.", Toast.LENGTH_SHORT).show();
            }
        });
	}

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        mAvatarID = mAdapter.getItem(position).getId();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        mAvatarID = -1;
    }
}
