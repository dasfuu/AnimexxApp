package de.meisterfuu.animexx.activitys.ens;

import java.util.ArrayList;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.api.APICallback;
import de.meisterfuu.animexx.api.ens.ENSApi;
import de.meisterfuu.animexx.objects.ens.ENSDraftObject;
import de.meisterfuu.animexx.objects.UserObject;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.views.UsernameAutoCompleteTextView;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

public class NewENSActivity extends Activity {
	
	public static void getInstance(Context pContext, ENSDraftObject pDraft){
		Intent i = new Intent().setClass(pContext, NewENSActivity.class);
	     Bundle args = new Bundle();
	     	args.putLong("ENSDraftObject", pDraft.getID());
	     i.putExtras(args);
	     pContext.startActivity(i);
	}
	
	public static void getInstanceBlank(final Context pContext){
		final ENSDraftObject draft = new ENSDraftObject();
		draft.setMessage("");
		ArrayList<Long> recip = new ArrayList<Long>();
		ArrayList<String> recip_name = new ArrayList<String>();
		draft.setRecipients(recip);
		draft.setRecipients_name(recip_name);
		draft.setSubject("");		
		draft.setReferenceType(null);
		draft.setSignature("");
		
		final ENSApi mAPI = new ENSApi(pContext);
		mAPI.saveENSDraft(draft, new APICallback() {
			
			@Override
			public void onCallback(APIException pError, Object pObject) {
				Intent i = new Intent().setClass(pContext, NewENSActivity.class);
			    Bundle args = new Bundle();
			    args.putLong("ENSDraftObject", draft.getID());
			    i.putExtras(args);
			    pContext.startActivity(i);
			    mAPI.close();
			}
		});
		

	}
	
	public static void getInstance(Context pContext){
		Intent i = new Intent().setClass(pContext, NewENSActivity.class);
	     pContext.startActivity(i);
	}
	
	
	long mDraftID;
	ENSApi mAPI;
	EditText mMessage, mSubject, mRecipient;
	UsernameAutoCompleteTextView mSearch;
	FrameLayout mHeader, mBody;
	ENSDraftObject mENSDraft;
	ArrayList<UserObject> mRecipients = new ArrayList<UserObject>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_ens);
		
		mMessage = (EditText) this.findViewById(R.id.activity_ens_new_message);
		mSubject = (EditText) this.findViewById(R.id.activity_ens_new_subject);
//		mRecipient = (EditText) this.findViewById(R.id.activity_ens_new_user);
		
		mSearch = (UsernameAutoCompleteTextView) this.findViewById(R.id.activity_ens_new_user_search);
		 
		mHeader = (FrameLayout) this.findViewById(R.id.activity_ens_new_header);
		mBody = (FrameLayout) this.findViewById(R.id.activity_ens_new_body);
		
		mSearch.init();
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
				if(mENSDraft.getRecipients_name().size()>0){
					mSearch.setText(mENSDraft.getRecipients_name().get(0)+", ");
				}

				
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

	ProgressDialog mDialog;

	private void send() {
		mDialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
		mDialog.setTitle("Prüfe Nutzernamen");
		String[] names = mSearch.getText().toString().split(",");
		ArrayList<String> names_ = new ArrayList<String>();
		for(String s: names){
			s = s.trim();
			if(s.isEmpty()) continue;
			names_.add(s);
		}
		mAPI.checkUserName(names_, new APICallback() {
			
			@Override
			public void onCallback(APIException pError, Object pObject) {
				ENSApi.anCheckObject ret = (ENSApi.anCheckObject)pObject;
				if(ret.errors.size()> 0){
					mDialog.cancel();
					Toast.makeText(NewENSActivity.this, ret.errors.toString(), Toast.LENGTH_SHORT).show();
				} else {
					mENSDraft.setRecipients(ret.IDs);
					mDialog.setTitle("Sende ENS");
					send_();
				}
			}
		});
	}
	
	public void send_(){
		mENSDraft.setMessage(mMessage.getText().toString());
		mENSDraft.setSubject(mSubject.getText().toString());
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
		mDialog.cancel();
		NewENSActivity.this.finish();
	}

}
