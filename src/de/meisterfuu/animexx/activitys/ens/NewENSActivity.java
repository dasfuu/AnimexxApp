package de.meisterfuu.animexx.activitys.ens;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.data.ens.ENSApi;
import de.meisterfuu.animexx.objects.ENSDraftObject;
import de.meisterfuu.animexx.utils.APIException;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

public class NewENSActivity extends Activity {
	
	public static void getInstance(Context pContext, ENSDraftObject pDraft){
		Intent i = new Intent().setClass(pContext, NewENSActivity.class);
	     Bundle args = new Bundle();
	     	args.putLong("ENSDraftObject", pDraft.getID());
	     i.putExtras(args);
	     pContext.startActivity(i);
	}
	
	public static void getInstance(Context pContext){
		Intent i = new Intent().setClass(pContext, NewENSActivity.class);
	     pContext.startActivity(i);
	}
	
	
	long mDraftID;
	ENSApi mAPI;
	EditText mMessage, mSubject, mRecipient;
	FrameLayout mHeader, mBody;
	ENSDraftObject mENSDraft;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_ens);
		
		mMessage = (EditText) this.findViewById(R.id.activity_ens_new_message);
		mSubject = (EditText) this.findViewById(R.id.activity_ens_new_subject);
		mRecipient = (EditText) this.findViewById(R.id.activity_ens_new_user);
		
		mHeader = (FrameLayout) this.findViewById(R.id.activity_ens_new_header);
		mBody = (FrameLayout) this.findViewById(R.id.activity_ens_new_body);
		
		mHeader.setVisibility(View.GONE);
		mBody.setVisibility(View.GONE);

		Bundle extras = this.getIntent().getExtras();
		mDraftID = extras.getLong("ENSDraftObject");
		mAPI = new ENSApi(this);
		
		mAPI.getENSDraft(mDraftID, new APICallback() {
			
			@Override
			public void onCallback(APIException pError, Object pObject) {
				mENSDraft = (ENSDraftObject) pObject;
				
				mMessage.setText(mENSDraft.getMessage());
				mSubject.setText(mENSDraft.getSubject());
				mRecipient.setText(mENSDraft.getRecipients().get(0)+"");
				
				mHeader.setVisibility(View.VISIBLE);
				mBody.setVisibility(View.VISIBLE);
			}
		});

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mAPI.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_ens, menu);
		return true;
	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){ 
		case R.id.send:
			send();
			break;
		}
		
		
		return super.onOptionsItemSelected(item);
	}

	private void send() {
		mENSDraft.setMessage(mMessage.getText().toString());
		mAPI.saveENSDraft(mENSDraft, null);
		mAPI.sendENS(mENSDraft, new APICallback() {
			
			@Override
			public void onCallback(APIException pError, Object pObject) {
				if(pError == null){					

					mAPI.deleteENSDraft(mENSDraft, new APICallback() {						
						@Override
						public void onCallback(APIException pError, Object pObject) {

						}
					});
				}
			}
		});
		NewENSActivity.this.finish();
	}

}
