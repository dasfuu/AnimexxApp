package de.meisterfuu.animexx.activitys.main;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.contacts.ContactFragment;
import de.meisterfuu.animexx.activitys.ens.ENSFolderFragment;
import de.meisterfuu.animexx.activitys.ens.FeedbackActivity;
import de.meisterfuu.animexx.activitys.ens.NewENSActivity;
import de.meisterfuu.animexx.activitys.events.EventListFragment;
import de.meisterfuu.animexx.activitys.home.HomeObjectFragment;
import de.meisterfuu.animexx.activitys.profiles.ProfileActivity;
import de.meisterfuu.animexx.activitys.rpg.RPGListFragment;
import de.meisterfuu.animexx.activitys.share.ImagePickerActivity;
import de.meisterfuu.animexx.activitys.share.WebDavImageActivity;
import de.meisterfuu.animexx.adapter.ChatStatusSpinnerAdapter;
import de.meisterfuu.animexx.adapter.ENSFolderSpinnerAdapter;
import de.meisterfuu.animexx.adapter.MainDrawerAdapter;
import de.meisterfuu.animexx.api.Self;
import de.meisterfuu.animexx.api.broker.ENSBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.DrawerObject;
import de.meisterfuu.animexx.objects.ens.ENSFolderObject;
import de.meisterfuu.animexx.utils.imageloader.PicassoDownloader;
import de.meisterfuu.animexx.xmpp.XMPPRoosterFragment;
import de.meisterfuu.animexx.xmpp.XMPPService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private MainDrawerAdapter mAdapter;
    private String mLastCode = "";

    private Spinner mENSSpinner;
    private Spinner mChatSpinner;
    private Spinner currentSpinner;
    private Toolbar mToolbar;
    private RelativeLayout profileHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mAdapter = new MainDrawerAdapter(this);


        // Set shadow
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        LayoutInflater inflater = (LayoutInflater)getSystemService(this.LAYOUT_INFLATER_SERVICE);
        profileHeader = (RelativeLayout) inflater.inflate(R.layout.drawer_header_profile, null);
        ImageView headerImage = (ImageView)profileHeader.findViewById(R.id.headerImage);
        TextView headerText = (TextView)profileHeader.findViewById(R.id.headerText);
        String name = Self.getInstance(this).getUsername();
        if(name != null && !name.isEmpty()){
            headerText.setText(Self.getInstance(this).getUsername());
        } else {
            headerText.setText("Ich");
        }

        PicassoDownloader.getAvatarPicasso(this)
                .load(PicassoDownloader.getAvatarURI(PicassoDownloader.createAvatarKey(Self.getInstance(this).getUserID()), this))
                .stableKey(PicassoDownloader.createAvatarKey(Self.getInstance(this).getUserID()))
                .placeholder(R.drawable.ic_contact_picture)
                .error(R.drawable.ic_contact_picture)
                .into(headerImage);

        profileHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProfile();
            }
        });
        mDrawerList.addHeaderView(profileHeader);

        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // Handle Toolbar

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setSubtitleTextColor(Color.WHITE);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle
        mDrawerToggle = new ActionBarDrawerToggle(
                this, /* host Activity */
                mDrawerLayout, /* DrawerLayout object */
                mToolbar,//R.drawable.ic_drawer, /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open, /* "open drawer" description for accessibility */
                R.string.drawer_close /* "close drawer" description for accessibility */
        ) {

            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }


            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (this.getIntent().hasExtra("LANDING")) {
            selectItem(this.getIntent().getStringExtra("LANDING")); //Todo: Test: Split into multiple extras
        } else {
            if (!mLastCode.isEmpty()) {
                selectItem(mLastCode);
            } else {
                String landing = PreferenceManager.getDefaultSharedPreferences(this).getString("landing_page", "Home");
                switch (landing){
                    case "Home":
                        selectItem("HOME");
                        break;
                    case "ENS":
                        selectItem("ENS");
                        break;
                    case "RPG":
                        selectItem("RPG");
                        break;
                    case "Chat":
                        selectItem("CHAT");
                        break;
                    case "Events":
                        selectItem("EVENT");
                        break;
                    case "Kontakte":
                        selectItem("CONTACTS");
                        break;
                }
//                selectItem("HOME");
            }
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (this.getIntent().hasExtra("LANDING")) {
            selectItem(this.getIntent().getStringExtra("LANDING"));
        } else {
            selectItem(mLastCode);
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);
    }


    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("RESUME");
        if (PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getBoolean("xmpp_status", false)) {
            Intent intent = new Intent(MainActivity.this, XMPPService.class);
            MainActivity.this.startService(intent);
        }
        selectItem(mLastCode);
    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        System.out.println("PAUSE");
    }


    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        System.out.println("RESTART");
    }


    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        System.out.println("STOP");
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        inflateMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }


    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        if (drawerOpen) {
            //menu.clear();
        }
        inflateMenu(menu);

        return super.onPrepareOptionsMenu(menu);
    }

    public Menu inflateMenu(Menu menu) {
        menu.clear();

        if (mLastCode.equals("ENS")) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main_ens, menu);
        } else if (mLastCode.equals("RPG")) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main_rpg, menu);
        } else if (mLastCode.equals("XMPP")) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.main_xmpp, menu);
        }

        return menu;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerLayout.closeDrawer(mDrawerList);
            DrawerObject obj = (DrawerObject) parent.getAdapter().getItem(position);
            selectItem(obj.getCode());
        }
    }



    private void selectItem(String pCode) {
        if (mLastCode.equals(pCode)) return;

        if (!pCode.equals("SETTINGS") && !pCode.equals("FEEDBACK") && !pCode.equals("FILES")) {
            mLastCode = pCode;
        }

        if (pCode.equals("ENS")) {
            selectENS();
        } else if (pCode.equals("RPG")) {
            selectRPG();
        } else if (pCode.equals("EVENT")) {
            selectEvent();
        } else if (pCode.equals("CHAT")) {
            selectChat();
        } else if (pCode.equals("HOME")) {
            selectHome();
        } else if (pCode.equals("CONTACTS")) {
            selectContacts();
        } else if (pCode.equals("SETTINGS")) {
            SettingsActivity.getInstance(this);
        } else if (pCode.equals("FEEDBACK")) {
            FeedbackActivity.getInstance(this);
        } else if (pCode.equals("FILES")) {
            WebDavImageActivity.getInstance(this, 3);
        }

    }

    private void selectChat() {
        this.setTitle("Chat");
        resetToolbar();
        loadFragment(XMPPRoosterFragment.getInstance(), "Rooster");
        invalidateOptionsMenu();

        mChatSpinner = new Spinner(mToolbar.getContext(), Spinner.MODE_DROPDOWN);

        ChatStatusSpinnerAdapter spinnerArrayAdapter = new ChatStatusSpinnerAdapter(this);
        mChatSpinner.setAdapter(spinnerArrayAdapter);

        boolean status = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getBoolean("xmpp_status", false);
        mChatSpinner.setSelection(status ? 0 : 1);

        mChatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putBoolean("xmpp_status", true).apply();
                    Intent intentStop = new Intent(MainActivity.this, XMPPService.class);
                    MainActivity.this.startService(intentStop);
                } else {
                    PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putBoolean("xmpp_status", false).apply();
                    Intent intentStart = new Intent(MainActivity.this, XMPPService.class);
                    MainActivity.this.stopService(intentStart);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        setSpinner(mChatSpinner);
    }


    private void selectENS() {

        loadFragment(ENSFolderFragment.getInstance(1, ENSBroker.TYPE_INBOX), "1_"+ENSBroker.TYPE_INBOX);
        invalidateOptionsMenu();

        new ENSBroker(MainActivity.this).getFolderList(new Callback<ReturnObject<ENSFolderObject.ENSFolderObjectContainer>>() {

            @Override
            public void success(ReturnObject<ENSFolderObject.ENSFolderObjectContainer> t, Response response) {
                final ENSFolderSpinnerAdapter spinnerAdapter = new ENSFolderSpinnerAdapter(t.getObj().getAll(), MainActivity.this);
                AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int itemPosition, long id) {
                        ENSFolderObject folder = spinnerAdapter.getItem(itemPosition);
                        Fragment fragment = ENSFolderFragment.getInstance(folder.getId(), folder.getType());
                        loadFragment(fragment, folder.getId()+ "_" + folder.getType());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                };

                mENSSpinner = new Spinner(mToolbar.getContext(), Spinner.MODE_DROPDOWN);
                mENSSpinner.setAdapter(spinnerAdapter);
                mENSSpinner.setOnItemSelectedListener(listener);
                setSpinner(mENSSpinner);
            }

            @Override
            public void failure(final RetrofitError error) {

            }
        });

    }

    private void selectContacts() {
        this.setTitle("Kontakte");
        resetToolbar();
        loadFragment(ContactFragment.getInstance(), "Kontakte");
        invalidateOptionsMenu();
    }

    private void selectRPG() {
        this.setTitle("RPG");
        resetToolbar();
        loadFragment(RPGListFragment.getInstance(), "RPG");
        invalidateOptionsMenu();
    }

    private void selectProfile() {
        ProfileActivity.getInstance(this, Self.getInstance(this).getUserID());
    }

    private void selectEvent() {
        this.setTitle("Events");
        resetToolbar();
        loadFragment(EventListFragment.getInstance(), "EventList");
        invalidateOptionsMenu();
    }


    private void selectHome() {
        this.setTitle("Home");
        resetToolbar();
        loadFragment(HomeObjectFragment.getInstance(), "Home");
        invalidateOptionsMenu();
    }


    public void loadFragment(Fragment pFragment, String pTag){
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, pFragment, pTag).commit();
    }

    private void resetToolbar() {
        if(currentSpinner != null){
            mToolbar.removeView(currentSpinner);
            currentSpinner = null;
        }
    }

    private void setSpinner(Spinner pSpinner) {
        if(currentSpinner != null){
            mToolbar.removeView(currentSpinner);
        }
        currentSpinner = pSpinner;
        this.setTitle("");
        mToolbar.addView(currentSpinner);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        System.out.println("POSTCREATE");
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

}
