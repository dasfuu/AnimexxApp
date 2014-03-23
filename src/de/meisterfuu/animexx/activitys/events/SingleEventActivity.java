package de.meisterfuu.animexx.activitys.events;

import java.text.SimpleDateFormat;
import java.util.Locale;

import com.google.android.gms.drive.internal.s;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.R.layout;
import de.meisterfuu.animexx.R.menu;
import de.meisterfuu.animexx.activitys.ens.SingleENSActivity;
import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.data.events.EventApi;
import de.meisterfuu.animexx.objects.EventDescriptionObject;
import de.meisterfuu.animexx.objects.EventObject;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.imageloader.ImageDownloaderCustom;
import de.meisterfuu.animexx.utils.imageloader.ImageLoaderCustom;
import de.meisterfuu.animexx.utils.imageloader.ImageSaveObject;
import android.os.Bundle;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class SingleEventActivity extends Activity {

	
	ImageDownloaderCustom ImageLoader = new ImageDownloaderCustom("event_logo");
	
	SimpleDateFormat sdf = new SimpleDateFormat("'Datum: 'HH:mm dd.MM.yyyy", Locale.getDefault());

	EventObject mEvent;
	EventApi mAPI;
	long mID;
	
	FrameLayout mHeader, mPages, mBody;
	LinearLayout mPagesList;
	ImageView mLogo;
	TextView mStart,mEnd,mAddress,mCount, mAnimexxStatus;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_event);
		// Show the Up button in the action bar.
		mAPI = new EventApi(this);
		mHeader = (FrameLayout) this.findViewById(R.id.activity_event_single_header);
		mBody = (FrameLayout) this.findViewById(R.id.activity_event_single_body);
		mPages = (FrameLayout) this.findViewById(R.id.activity_event_single_pagelist);
		
		mPagesList  = (LinearLayout) this.findViewById(R.id.activity_event_single_pagelist_list);
		
		mLogo = (ImageView) this.findViewById(R.id.activity_event_single_user_avatar);
		
		mStart = (TextView) this.findViewById(R.id.activity_event_single_start_text);
		mEnd = (TextView) this.findViewById(R.id.activity_event_single_end_text);
		mAddress = (TextView) this.findViewById(R.id.activity_event_single_address_text);
		mAnimexxStatus = (TextView) this.findViewById(R.id.activity_event_single_animexx_text);
		mCount = (TextView) this.findViewById(R.id.activity_event_single_atcount_text);
		
		
		mHeader.setVisibility(View.GONE);
		mBody.setVisibility(View.GONE);
		mPages.setVisibility(View.GONE);
		
		SingleEventActivity.this.getActionBar().setTitle("");
		
		if(this.getIntent().hasExtra("id")){
			Bundle extras = this.getIntent().getExtras();
			mID = extras.getLong("id");
		} else {
			finish();
			return;
		}
		
		
		mAPI.getEvent(new APICallback() {
			
			@Override
			public void onCallback(APIException pError, Object pObject) {
				mEvent = (EventObject)pObject;
				if(mEvent.getLogo() != null && !mEvent.getLogo().isEmpty()){
					ImageLoader.download(new ImageSaveObject(mEvent.getLogo(), ""+mEvent.getId()), mLogo);
				} else {
					mLogo.setVisibility(View.GONE);
				}

				
				mStart.setText("Vom: "+mEvent.getStartDate());
				mEnd.setText("Bis: "+mEvent.getEndDate());
				mAddress.setText(mEvent.getAddress());
				mCount.setText(mEvent.getAttendees()+" Teilnehmer");
				mAnimexxStatus.setText(mEvent.getAnimexxString());
				SingleEventActivity.this.getActionBar().setTitle(mEvent.getName());
				
				mHeader.setVisibility(View.VISIBLE);
				
				showPages();
			}
			
		}, mID, EventApi.DETAIL_FULL);
		
		setupActionBar();
	}
	
	private void showPages() {

		LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for(EventDescriptionObject page: mEvent.getPages()){
			LinearLayout view = (LinearLayout) inflater.inflate(R.layout.activity_single_event_desc_item, null);
			TextView tx = (TextView) view.findViewById(R.id.activity_single_event_desc_item_title);
			tx.setText(page.getPageName());
			final EventDescriptionObject fpage = page;
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					SingleEventDescriptionActivity.getInstance(SingleEventActivity.this, fpage.getPageName(), mEvent.getId(), fpage.getId());
				}
			});
			mPagesList.addView(view);
		}
		mPages.setVisibility(View.VISIBLE);
	}

	public static void getInstance(Context pContext,long pID){
		Intent i = new Intent().setClass(pContext, SingleEventActivity.class);
	     Bundle args = new Bundle();
	     args.putLong("id", pID);
	     i.putExtras(args);
	     pContext.startActivity(i);
	}
	
	public static PendingIntent getPendingIntent(Context pContext,long pID){    
	     Intent intent = new Intent(pContext, SingleEventActivity.class);
	     Bundle args = new Bundle();
	     args.putLong("id", pID);
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
		getMenuInflater().inflate(R.menu.single_event, menu);
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
