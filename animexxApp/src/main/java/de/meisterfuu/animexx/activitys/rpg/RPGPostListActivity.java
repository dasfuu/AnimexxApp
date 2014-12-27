package de.meisterfuu.animexx.activitys.rpg;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseActivityAB;
import de.meisterfuu.animexx.adapter.RPGAvatarSpinnerAdapter;
import de.meisterfuu.animexx.adapter.RPGPostListAdapter;
import de.meisterfuu.animexx.adapter.RPGSpinnerAdapter;
import de.meisterfuu.animexx.api.broker.RPGBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.rpg.RPGDraftObject;
import de.meisterfuu.animexx.objects.rpg.RPGObject;
import de.meisterfuu.animexx.objects.rpg.RPGPostObject;
import de.meisterfuu.animexx.services.GcmIntentService;
import de.meisterfuu.animexx.utils.views.FeedbackListView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RPGPostListActivity extends AnimexxBaseActivityAB implements PanelSlideListener {

    public static void getInstance(Context pContext, long pID) {
        Intent i = new Intent().setClass(pContext, RPGPostListActivity.class);
        Bundle args = new Bundle();
        args.putLong("id", pID);
        i.putExtras(args);
        pContext.startActivity(i);
    }

    RPGBroker mAPI;
    ArrayList<RPGPostObject> mList;
    RPGPostListAdapter mAdapter;
    RPGObject mRPG;
    long mRPGID;
    long mCharaID;

    SlidingUpPanelLayout mSlidingLayout;
    EditText mEditPost;
    FeedbackListView mListView;
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
        mListView = (FeedbackListView) this.findViewById(android.R.id.list);

        mSlidingLayout.setPanelSlideListener(this);
        mAPI = new RPGBroker(this);
        init();
    }


    boolean paused = false;

    @Override
    public void onPause() {
        unregisterReceiver(mReceiver);
        paused = true;
        super.onPause();
    }

    @Override
    public void onResume() {
        mAPI = new RPGBroker(this);

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(GcmIntentService.NEW_POST)) {
                    refreshList();
                }
            }
        };


        IntentFilter filter = new IntentFilter();
        filter.addAction(GcmIntentService.NEW_POST);
        registerReceiver(mReceiver, filter);

        if (paused) {
            refreshList();
        }

        super.onResume();
    }

    private void init() {
        Bundle extras = this.getIntent().getExtras();
        mRPGID = extras.getLong("id");

        mList = new ArrayList<RPGPostObject>();
        mAdapter = new RPGPostListAdapter(mList, this, mRPGID);
        mListView.setAdapter(mAdapter);
        mListView.showLoading();
        mAPI.getRPG(mRPGID, new Callback<ReturnObject<RPGObject>>() {
            @Override
            public void success(ReturnObject<RPGObject> rpgObjectReturnObject, Response response) {
                mRPG = rpgObjectReturnObject.getObj();
                setTitle(mRPG.getName());
                final RPGSpinnerAdapter spinnerAdapter = new RPGSpinnerAdapter(mRPG.getPlayer(), mRPGID, RPGPostListActivity.this);
                ActionBar.OnNavigationListener listener = new ActionBar.OnNavigationListener() {

                    @Override
                    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                        mCharaID = itemId;
                        initChara();
                        return true;
                    }

                };

                RPGPostListActivity.this.getSupportActionBar().setListNavigationCallbacks(spinnerAdapter, listener);

                loadPosts();
            }

            @Override
            public void failure(RetrofitError error) {
                mListView.showError("Es ist ein Fehler aufgetreten");
            }
        });
    }

    int postCount = 0;

    private void loadPosts() {
        postCount = mRPG.getPostCount();

        mAPI.getRPGPostList(mRPGID, mRPG.getPostCount() - 29, new Callback<ReturnObject<List<RPGPostObject>>>() {
            @Override
            public void success(ReturnObject<List<RPGPostObject>> listReturnObject, Response response) {
                mListView.showList();
                List<RPGPostObject> list = listReturnObject.getObj();
                mAdapter.addAll(list);
                if(mAdapter.getCount() == 0){
                    mListView.showError("Keine Posts");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                mListView.showError("Es ist ein Fehler aufgetreten");
            }
        });

    }

    public void refreshList() {
        mAPI.getRPGPostList(mRPGID, postCount + 1, new Callback<ReturnObject<List<RPGPostObject>>>() {
            @Override
            public void success(ReturnObject<List<RPGPostObject>> listReturnObject, Response response) {
                List<RPGPostObject> list = listReturnObject.getObj();
                postCount = postCount + list.size();
                mAdapter.addAll(list);
                if (list.size() == 30) {
                    refreshList();
                }
            }

            @Override
            public void failure(RetrofitError error) {

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
        if(mEditPost.getText().toString().isEmpty()){
            mToggleLabel.setText("Neuer Post");
        } else {
            mToggleLabel.setText("Zurück zum Post");
        }

        this.getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        RPGPostListActivity.this.getSupportActionBar().setDisplayShowTitleEnabled(true);
        this.setTitle(mRPG.getName());
        invalidateOptionsMenu();
    }

    @Override
    public void onPanelExpanded(View panel) {
        mToggleLabel.setText("Zurück zum RPG");
        this.getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        RPGPostListActivity.this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        invalidateOptionsMenu();
    }

    private void initChara() {
        for (RPGObject.PlayerObject obj : mRPG.getPlayer()) {
            if (obj.getId() == mCharaID) {
                mSpinnerAvatar.setAdapter(new RPGAvatarSpinnerAdapter(obj, mRPG, this));
                if (mSpinnerAvatar.getAdapter().getCount() > 1) {
                    mSpinnerAvatar.setSelection(1);
                }
                break;
            }
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void post() {
        hideKeyboard();

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Sende...");
        RPGDraftObject draft = new RPGDraftObject();
        draft.setAvatarID(mSpinnerAvatar.getSelectedItemId());
        draft.setCharaID(mCharaID);
        if (mCBInTime.isChecked()) {
            draft.setInTime(1);
        } else {
            draft.setInTime(0);
        }

        if (mCBAction.isChecked()) {
            draft.setKursiv(1);
        } else {
            draft.setKursiv(0);
        }
        draft.setRpgID(mRPGID);
        draft.setText(mEditPost.getText().toString());
        dialog.show();
        mAPI.sendRPGDraft(draft, new Callback<ReturnObject<Integer>>() {
            @Override
            public void success(ReturnObject<Integer> integerReturnObject, Response response) {
                dialog.cancel();
                refreshList();
                mSlidingLayout.collapsePanel();
                mToggleLabel.setText("Neuer Post");
                mEditPost.setText("");
            }

            @Override
            public void failure(RetrofitError error) {
                dialog.cancel();
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
        if (mSlidingLayout.isPanelExpanded()) {
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

    public Menu inflateMenu(Menu menu) {
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
