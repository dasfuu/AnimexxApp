package de.meisterfuu.animexx.activitys.profiles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.data.profile.GBApi;
import de.meisterfuu.animexx.objects.GBDraftObject;
import de.meisterfuu.animexx.objects.GBInfoObject;
import de.meisterfuu.animexx.objects.UserObject;
import de.meisterfuu.animexx.utils.APIException;

public class GuestbookPostActivity extends Activity implements APICallback<GBInfoObject> {


	public static void getInstance(Context pContext, long pID){
		Intent i = new Intent().setClass(pContext, GuestbookPostActivity.class);
		Bundle args = new Bundle();
		args.putLong("id", pID);
		i.putExtras(args);
		pContext.startActivity(i);
	}


	EditText mText;
	Spinner mAvatarSpinner;

	private GBApi mApi;
	private long mUserID;
	private int mAvatarID;


	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gbpost_new);

		mAvatarID = -1;



		mText = (EditText) this.findViewById(R.id.activity_gbpost_new_text);
		mAvatarSpinner = (Spinner) this.findViewById(R.id.activity_gbpost_new_avatar);

    }

	@Override
	public void onResume() {
		Bundle extras = this.getIntent().getExtras();
		mUserID = extras.getLong("id");

		mApi = new GBApi(this);
		mApi.getGBInfo(mUserID, this);
	}

	@Override
	public void onCallback(final APIException pError, final GBInfoObject pObject) {
			if(pError != null){

			} else {
				this.finish();
			}
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

	}


}
