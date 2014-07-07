package de.meisterfuu.animexx.activitys.rpg;

import java.util.ArrayList;

import org.apache.harmony.javax.security.auth.Refreshable;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.ens.NewENSActivity;
import de.meisterfuu.animexx.activitys.main.MainActivity;
import de.meisterfuu.animexx.activitys.main.SettingsActivity;
import de.meisterfuu.animexx.adapter.ChatAdapter;
import de.meisterfuu.animexx.adapter.RPGAvatarSpinnerAdapter;
import de.meisterfuu.animexx.adapter.RPGPostListAdapter;
import de.meisterfuu.animexx.adapter.RPGSpinnerAdapter;
import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.data.rpg.RPGApi;
import de.meisterfuu.animexx.notification.RPGPostNotification;
import de.meisterfuu.animexx.notification.XMPPNotification;
import de.meisterfuu.animexx.objects.RPGDraftObject;
import de.meisterfuu.animexx.objects.RPGObject;
import de.meisterfuu.animexx.objects.RPGPostObject;
import de.meisterfuu.animexx.utils.APIException;
import de.meisterfuu.animexx.utils.Request;
import de.meisterfuu.animexx.xmpp.XMPPService;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.ListActivity;
import android.app.ActionBar.OnNavigationListener;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class RPGPostListActivity extends ListActivity implements PanelSlideListener {
	
	public static void getInstance(Context pContext, long pID){
		Intent i = new Intent().setClass(pContext, RPGPostListActivity.class);
	     Bundle args = new Bundle();
	     args.putLong("id", pID);
	     i.putExtras(args);
	     pContext.startActivity(i);
	}
	
	RPGApi mAPI;
	ArrayList<RPGPostObject> mList;
	RPGPostListAdapter mAdapter;
	RPGObject mRPG;
	long mRPGID;
	long mCharaID;
	
	SlidingUpPanelLayout mSlidingLayout;
	EditText mEditPost;
	CheckBox mCBAction, mCBInTime;
	TextView mToggleLabel;
	private BroadcastReceiver mReceiver;
	private Spinner mSpinnerAvatar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rpgpost_list);
		
		mSlidingLayout = (SlidingUpPanelLayout) this.findViewById(R.id.activity_rpgpost_list_slide);
		mToggleLabel = (TextView) this.findViewById(R.id.activity_rpgpost_new_toggle);
		mCBAction = (CheckBox) this.findViewById(R.id.activity_rpgpost_new_action);
		mCBInTime = (CheckBox) this.findViewById(R.id.activity_rpgpost_new_intime);
		mSpinnerAvatar = (Spinner) this.findViewById(R.id.activity_rpgpost_new_avatar);
		mEditPost = (EditText) this.findViewById(R.id.activity_rpgpost_new_text);
		
		mSlidingLayout.setPanelSlideListener(this);
		mAPI = new RPGApi(this);	
		this.getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		RPGPostListActivity.this.getActionBar().setDisplayHomeAsUpEnabled(true);
		init();
	}


	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	}
	
	boolean paused = false;
	
	@Override
	public void onPause() {
		unregisterReceiver(mReceiver);
		RPGPostNotification.filter = null;
		paused = true;
		super.onPause();
	}

	@Override
	public void onResume() {
		Request.config = PreferenceManager.getDefaultSharedPreferences(this);
		mAPI = new RPGApi(this);	

		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (action.equals(RPGPostNotification.NEW_POST)) {	
					refreshList();
				}
			}
		};
		
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(RPGPostNotification.NEW_POST);
		registerReceiver(mReceiver, filter);
		
		RPGPostNotification.filter = mRPGID+"";
		
		if(paused){
			refreshList();
		}
		
		super.onResume();
	}
	
	private void init() {
		Bundle extras = this.getIntent().getExtras();
		mRPGID = extras.getLong("id");
		
		mList = new ArrayList<RPGPostObject>();
		mAdapter = new RPGPostListAdapter(mList, this, mRPGID);
		this.getListView().setAdapter(mAdapter);
		
		mAPI.getRPG(mRPGID, new APICallback(){

			@Override
			public void onCallback(APIException pError, Object pObject) {
				mRPG = (RPGObject) pObject;
				RPGPostListActivity.this.getActionBar().setTitle(mRPG.getName());				
				final RPGSpinnerAdapter spinnerAdapter = new RPGSpinnerAdapter(mRPG.getPlayer(), mRPGID, RPGPostListActivity.this);
				OnNavigationListener listener = new OnNavigationListener() {

					@Override
					public boolean onNavigationItemSelected(int itemPosition, long itemId) {
						mCharaID = itemId;
						initChara();
						return true;
					}

				};

				RPGPostListActivity.this.getActionBar().setListNavigationCallbacks(spinnerAdapter, listener);
				
				loadPosts();
			}
		});
	}

	int postCount = 0;
	
	private void loadPosts() {
		postCount = mRPG.getPostCount();
		
		mAPI.getRPGPostList(mRPGID, mRPG.getPostCount()-29, new APICallback(){

			@SuppressWarnings("unchecked")
			@Override
			public void onCallback(APIException pError, Object pObject) {
				ArrayList<RPGPostObject> list = (ArrayList<RPGPostObject>) pObject;
				mAdapter.addAll(list);		
			}
		});
		
	}
	
	public void refreshList() {
		mAPI.getRPGPostList(mRPGID, postCount+1, new APICallback(){

			@SuppressWarnings("unchecked")
			@Override
			public void onCallback(APIException pError, Object pObject) {
				ArrayList<RPGPostObject> list = (ArrayList<RPGPostObject>) pObject;
				postCount = postCount+list.size();
				mAdapter.addAll(list);		
				if(list.size() == 30){
					refreshList();
				}
			}
		});
	}


	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onPanelSlide(View panel, float slideOffset) {
		
	}

	@Override
	public void onPanelCollapsed(View panel) {
		mToggleLabel.setText("Zur�ck zum Post");
		this.getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		RPGPostListActivity.this.getActionBar().setDisplayShowTitleEnabled(true);
		invalidateOptionsMenu();
	}

	@Override
	public void onPanelExpanded(View panel) {
		mToggleLabel.setText("Zur�ck zum RPG");
		this.getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		RPGPostListActivity.this.getActionBar().setDisplayShowTitleEnabled(false);
		invalidateOptionsMenu();
	}
	
	private void initChara() {
		for(RPGObject.PlayerObject obj: mRPG.getPlayer()){
			if(obj.getId() == mCharaID){
				mSpinnerAvatar.setAdapter(new RPGAvatarSpinnerAdapter(obj, mRPG, this));
				if(mSpinnerAvatar.getAdapter().getCount() > 1){
					mSpinnerAvatar.setSelection(1);
				}
				break;
			}
		}
	}
	
	public void post() {
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setTitle("Sende...");
		RPGDraftObject draft = new RPGDraftObject();
		draft.setAvatarID(mSpinnerAvatar.getSelectedItemId());
		draft.setCharaID(mCharaID);
		if(mCBInTime.isChecked()){
			draft.setInTime(1);
		} else {
			draft.setInTime(0);
		}
		
		if(mCBAction.isChecked()){
			draft.setKursiv(1);
		} else {
			draft.setKursiv(0);
		}
		draft.setRpgID(mRPGID);
		draft.setText(mEditPost.getText().toString());
		dialog.show();
		mAPI.sendRPGDraft(draft, new APICallback() {
			
			@Override
			public void onCallback(APIException pError, Object pObject) {
				dialog.cancel();
				if(pError != null){
					return;
				}
				refreshList();
				mSlidingLayout.collapsePanel();
				mToggleLabel.setText("Neuer Post");
				mEditPost.setText("");
			}
		});
	}


	@Override
	public void onPanelAnchored(View panel) {
		
	}

    @Override
    public void onPanelHidden(View view) {

    }

    @Override
	public void onBackPressed() {
		if(mSlidingLayout.isPanelExpanded()){
			mSlidingLayout.collapsePanel();
		} else {
			super.onBackPressed();
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		inflateMenu(menu);
		return true;
	}


	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		inflateMenu(menu);

		return super.onPrepareOptionsMenu(menu);
	}
	
	public Menu inflateMenu(Menu menu){
		menu.clear();
		if (!mSlidingLayout.isPanelExpanded()) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.rpgpost_list, menu);
		} else {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.rpgpost_list_new, menu);
		} 

		return menu;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_post:
			post();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}




}
