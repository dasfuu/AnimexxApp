package de.meisterfuu.animexx.activitys.main;



import de.meisterfuu.animexx.Constants;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.data.Self;
import de.meisterfuu.animexx.data.other.GCMApi;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.Request;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class LoginActivity extends Activity implements OnClickListener {
	
	GCMApi gcm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gcm = new GCMApi(this);
		Request.config = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		if(init()) return;
		setContentView(R.layout.activity_login);
		
		ImageView img_login = (ImageView)findViewById(R.id.activity_login_image);
		Button btn_login = (Button)findViewById(R.id.activity_login_button);
		btn_login.setOnClickListener(this);
		img_login.setOnClickListener(this);
	}

	private boolean init() {
		boolean result = false;
		
		//Logged in?
		if(Self.getInstance(this).isLoggedIn()){
			//Yes? set result TRUE
			result = true;
			
			//Create Account
	        AccountManager manager = AccountManager.get(this);
	        final Account acc = new Account(Constants.ACCOUNT_NAME, Constants.ACCOUNT_TYPE);
	        if (manager.addAccountExplicitly(acc, null, null)) {
	            // Only change sync settings if it did not exist, yet
	            ContentResolver.setIsSyncable(acc, Constants.CONTENT_CALENDAR_AUTHORITY, 1);
	            ContentResolver.setSyncAutomatically(acc, Constants.CONTENT_CALENDAR_AUTHORITY, true);
	            // Sync daily by default
	            ContentResolver.addPeriodicSync(acc, Constants.CONTENT_CALENDAR_AUTHORITY, new Bundle(), 24 * 60 * 60);
	        }
			
			//Yes? Start MainActivity
			startActivity(new Intent().setClass(this, MainActivity.class));
			
			//Own User data known?
			if(Self.getInstance(this).getUserID() == -1L){
				//No? Get it!
				Self.getInstance(this).fetchSelf();
			} 
			
			//Check for gcm
			this.initGCM();
			
			//Finish this activity
			LoginActivity.this.finish();
		}	

		
		return result;
	}
	
	private void initGCM(){
		//Check for gcm id
		if(gcm.getRegistrationId(this) == null){
			Log.e("Animexx Init", "GCM REG ID IS NULL");
			//No? Get one and activate.
			gcm.registerGCM(new APICallback() {
				
				@Override
				public void onCallback(APIException pError, Object pObject) {
				
					//Activate Events after registration					
					gcm.activateGCMEvents(new APICallback() {						
						@Override
						public void onCallback(APIException pError, Object pObject) {
							//nothing to do here
						}
					});
				}
			});
		} 
	}

	@Override
	public void onClick(View v) {
		startActivity(new Intent().setClass(v.getContext(), RequestTokenActivity.class));
		LoginActivity.this.finish();
	}




}
