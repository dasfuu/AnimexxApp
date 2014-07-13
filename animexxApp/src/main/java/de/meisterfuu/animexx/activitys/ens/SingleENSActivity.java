package de.meisterfuu.animexx.activitys.ens;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.data.ens.ENSApi;
import de.meisterfuu.animexx.objects.ENSDraftObject;
import de.meisterfuu.animexx.objects.ENSObject;
import de.meisterfuu.animexx.objects.UserObject;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.Helper;
import de.meisterfuu.animexx.utils.imageloader.ImageDownloaderCustom;
import de.meisterfuu.animexx.utils.imageloader.ImageLoaderCustom;
import de.meisterfuu.animexx.utils.imageloader.ImageSaveObject;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class SingleENSActivity extends Activity {

	long mID;
	ENSObject mENS;
	
	WebView mWebView;
	TextView mSubject, mUserLabel, mUser, mDate, mMessage;
	ImageView mAvatar;
	FrameLayout mHeader, mBody;
	ENSApi mAPI;
	Boolean mLoaded = false;
	
	ImageDownloaderCustom ImageLoader = new ImageDownloaderCustom("forenavatar");
	ImageLoaderCustom ImageLoaderProfile = new ImageLoaderCustom("profilbild");
	
	SimpleDateFormat sdf = new SimpleDateFormat("'Datum: 'HH:mm dd.MM.yyyy", Locale.getDefault());

	
	public static void getInstance(Context pContext,long pID){
		Intent i = new Intent().setClass(pContext, SingleENSActivity.class);
	     Bundle args = new Bundle();
	     args.putLong("id", pID);
	     i.putExtras(args);
	     pContext.startActivity(i);
	}
	
	public static PendingIntent getPendingIntent(Context pContext,long pID){    
	     Intent intent = new Intent(pContext, SingleENSActivity.class);
	     Bundle args = new Bundle();
	     args.putLong("id", pID);
	     intent.putExtras(args);
	     return PendingIntent.getActivity(pContext, (new Random()).nextInt(), intent, PendingIntent.FLAG_ONE_SHOT);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_ens);
		
		this.getActionBar().setDisplayHomeAsUpEnabled(true);
		
		
		mSubject = (TextView)this.findViewById(R.id.activity_ens_single_subject);
		mUserLabel = (TextView)this.findViewById(R.id.activity_ens_single_user_label);
		mUser = (TextView)this.findViewById(R.id.activity_ens_single_user);
		mDate = (TextView)this.findViewById(R.id.activity_ens_single_date);
		mWebView = (WebView)this.findViewById(R.id.activity_ens_single_webview);
		mMessage = (TextView)this.findViewById(R.id.activity_ens_single_message);
		mAvatar = (ImageView)this.findViewById(R.id.activity_ens_single_user_avatar);
		
		mHeader = (FrameLayout) this.findViewById(R.id.activity_ens_single_header);
		mBody = (FrameLayout) this.findViewById(R.id.activity_ens_single_body);
		
		mHeader.setVisibility(View.GONE);
		mBody.setVisibility(View.GONE);
		
		//this.findViewById(R.id.activity_ens_single_base);
		
		
		mWebView.setVisibility(View.GONE);
		
		
		Bundle extras = this.getIntent().getExtras();
		mID = extras.getLong("id");
		

		mAPI = new ENSApi(this);
		mAPI.getENS(mID, new APICallback() {
			
			@Override
			public void onCallback(APIException pError, Object pObject) {
				mENS = (ENSObject) pObject;	
				
				mSubject.setText(mENS.getSubject());
				SingleENSActivity.this.getActionBar().setTitle(mENS.getSubject());
				
				mDate.setText(sdf.format(mENS.getDateObject()));
				UserObject target= new UserObject();
				if(mENS.getAn_ordner() > 0) {
					target = mENS.getVon();
					if(target == null) target = new UserObject();
					mUserLabel.setText("Von:");
					mUser.setText(target.getUsername());
				} else {
					if(mENS.getAn().size() > 0){
						target = mENS.getAn().get(0);
					}
					if(target == null) target = new UserObject();
					mUserLabel.setText("An:");
					mUser.setText(target.getUsername());
				}
				
				if(ImageLoaderProfile.exists(new ImageSaveObject("", target.getId()+""), SingleENSActivity.this)){
					ImageLoaderProfile.download(new ImageSaveObject("", target.getId()+""), mAvatar);			
				} else {
					if(target.getAvatar() != null){
						ImageSaveObject image = new ImageSaveObject(target.getAvatar().getUrl(), target.getId()+"");
						System.out.println(target.getAvatar().getUrl());
						ImageLoader.download(image, mAvatar);
					} else {
						mAvatar.setVisibility(View.GONE);
					}
				}
				
				mMessage.setText(Html.fromHtml(getFullMessage()));
				
				
				mHeader.setVisibility(View.VISIBLE);
				mBody.setVisibility(View.VISIBLE);
				
				
				
				mLoaded = true;

				mAPI.clearNotification();
				//mWebView.loadDataWithBaseURL("fake://fake.de", getFullMessage(), "text/html", "UTF-8", null);				
			}
		});
		
	}


	protected String getFullMessage() {
		if(!mENS.getSignature().isEmpty()){
			return mENS.getMessage()+"<br><br>---------------<br>"+mENS.getSignature();
		} else {
			return mENS.getMessage();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.single_ens, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){ 
		case R.id.answer:
			if(mLoaded) answer();
			break;
		case R.id.answer_quote:
			if(mLoaded) answerQuote();
			break;
		case R.id.forward:
			if(mLoaded) forward();
			break;
		}
		
		
		return super.onOptionsItemSelected(item);
	}
	
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mAPI.close();
	}

	private void forward() {
		final ENSDraftObject draft = new ENSDraftObject();
		draft.setMessage(mENS.getMessage_raw());
		ArrayList<Long> recip = new ArrayList<Long>();
		recip.add(mENS.getVon().getId());
		ArrayList<String> recip_name = new ArrayList<String>();
		recip_name.add(mENS.getVon().getUsername());
		draft.setRecipients(recip);
		draft.setRecipients_name(recip_name);
		draft.setSubject(Helper.BetreffRe(mENS.getSubject()));
		draft.setReferenceID(mENS.getId());
		draft.setReferenceType("forward");
		mAPI.saveENSDraft(draft, new APICallback() {
			
			@Override
			public void onCallback(APIException pError, Object pObject) {
				NewENSActivity.getInstance(SingleENSActivity.this, draft);
			}
		});
	}

	private void answer() {
		final ENSDraftObject draft = new ENSDraftObject();
		draft.setMessage("");
		ArrayList<Long> recip = new ArrayList<Long>();
		recip.add(mENS.getVon().getId());
		ArrayList<String> recip_name = new ArrayList<String>();
		recip_name.add(mENS.getVon().getUsername());
		draft.setRecipients(recip);
		draft.setRecipients_name(recip_name);
		draft.setSubject(Helper.BetreffRe(mENS.getSubject()));
		draft.setReferenceID(mENS.getId());
		draft.setReferenceType("reply");
		mAPI.saveENSDraft(draft, new APICallback() {
			
			@Override
			public void onCallback(APIException pError, Object pObject) {
				NewENSActivity.getInstance(SingleENSActivity.this, draft);
			}
		});
	}
	
	private void answerQuote() {
		final ENSDraftObject draft = new ENSDraftObject();
		draft.setMessage(mENS.getMessage_raw());
		ArrayList<Long> recip = new ArrayList<Long>();
		recip.add(mENS.getVon().getId());
		ArrayList<String> recip_name = new ArrayList<String>();
		recip_name.add(mENS.getVon().getUsername());
		draft.setRecipients(recip);
		draft.setRecipients_name(recip_name);
		draft.setSubject(Helper.BetreffRe(mENS.getSubject()));
		draft.setReferenceID(mENS.getId());
		draft.setReferenceType("reply");
		mAPI.saveENSDraft(draft, new APICallback() {
			
			@Override
			public void onCallback(APIException pError, Object pObject) {
				NewENSActivity.getInstance(SingleENSActivity.this, draft);
			}
		});
	}
	
	

}
