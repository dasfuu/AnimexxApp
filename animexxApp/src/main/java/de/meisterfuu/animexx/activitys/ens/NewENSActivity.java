package de.meisterfuu.animexx.activitys.ens;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.AnimexxBaseActivityAB;
import de.meisterfuu.animexx.api.broker.ENSBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.UserObject;
import de.meisterfuu.animexx.objects.ens.ENSCheckRecipientsObject;
import de.meisterfuu.animexx.objects.ens.ENSDraftObject;
import de.meisterfuu.animexx.utils.views.UsernameAutoCompleteTextView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NewENSActivity extends AnimexxBaseActivityAB {

    public static void getInstance(Context pContext, ENSDraftObject pDraft) {
        Intent i = new Intent().setClass(pContext, NewENSActivity.class);
        Bundle args = new Bundle();
        args.putLong("ENSDraftObject", pDraft.getID());
        i.putExtras(args);
        pContext.startActivity(i);
    }

    public static void getInstance(Context pContext, long pUserId, String pUsername) {
        Intent i = new Intent().setClass(pContext, NewENSActivity.class);
        Bundle args = new Bundle();
        args.putLong("UserID", pUserId);
        args.putString("UserName", pUsername);
        i.putExtras(args);
        pContext.startActivity(i);
    }

    public static void getInstanceBlank(final Context pContext) {
        getInstance(pContext);
    }

    public static void getInstance(Context pContext) {
        Intent i = new Intent().setClass(pContext, NewENSActivity.class);
        pContext.startActivity(i);
    }


    long mDraftID;
    ENSBroker mAPI;
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

        mAPI = new ENSBroker(this);

        Bundle extras = this.getIntent().getExtras();
        if(extras != null && extras.containsKey("ENSDraftObject")){
            mDraftID = extras.getLong("ENSDraftObject");
            loadDraft();
            return;
        } else if(extras != null && extras.containsKey("UserID")){
            long userid = extras.getLong("UserID");
            String username = extras.getString("UserName");
            mENSDraft = new ENSDraftObject();
            mENSDraft.setSubject("");
            mENSDraft.setReferenceType(null);
            mENSDraft.setSignature("");
            ArrayList<Long> recip = new ArrayList<Long>();
            recip.add(userid);
            ArrayList<String> recip_name = new ArrayList<String>();
            recip_name.add(username);
            mENSDraft.setRecipients(recip);
            mENSDraft.setRecipients_name(recip_name);
        } else {
            mENSDraft = new ENSDraftObject();
            mENSDraft.setSubject("");
            mENSDraft.setReferenceType(null);
            mENSDraft.setSignature("");
            ArrayList<Long> recip = new ArrayList<Long>();
            ArrayList<String> recip_name = new ArrayList<String>();
            mENSDraft.setRecipients(recip);
            mENSDraft.setRecipients_name(recip_name);
        }

        fillViews();

    }

    private void fillViews() {
        mMessage.setText(mENSDraft.getMessage());
        mSubject.setText(mENSDraft.getSubject());
        if (mENSDraft.getRecipients_name().size() > 0) {
            mSearch.setText(mENSDraft.getRecipients_name().get(0) + ", ");
        }


        mHeader.setVisibility(View.VISIBLE);
        mBody.setVisibility(View.VISIBLE);
    }

    private void loadDraft() {
        mAPI.getENSDraft(mDraftID, new Callback<ENSDraftObject>() {
            @Override
            public void success(ENSDraftObject ensDraftObject, Response response) {
                mENSDraft = ensDraftObject;

                fillViews();
            }

            @Override
            public void failure(RetrofitError error) {
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

        switch (item.getItemId()) {
            case R.id.send:
                send();
                break;
            case android.R.id.home:
                this.finish();
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
        for (String s : names) {
            s = s.trim();
            if (s.isEmpty()) continue;
            names_.add(s);
        }
        mAPI.checkUserName(names_, new Callback<ReturnObject<ENSCheckRecipientsObject>>() {
            @Override
            public void success(final ReturnObject<ENSCheckRecipientsObject> t, final Response response) {
                if (t.getObj().getErrors().size() > 0) {
                    mDialog.cancel();
                    Toast.makeText(NewENSActivity.this, Arrays.toString(t.getObj().getErrors().toArray()), Toast.LENGTH_SHORT).show();
                } else {
                    mENSDraft.setRecipients(t.getObj().getIDs());
                    mDialog.setTitle("Sende ENS");
                    send_();
                }
            }

            @Override
            public void failure(final RetrofitError error) {

            }
        });
    }

    public void send_() {
        mENSDraft.setMessage(mMessage.getText().toString());
        mENSDraft.setSubject(mSubject.getText().toString());
        mAPI.saveENSDraft(mENSDraft, null);
        mAPI.sendENS(mENSDraft, new Callback<Long>() {
            @Override
            public void success(Long aLong, Response response) {
                if (aLong > -1) {
                    mAPI.deleteENSDraft(mENSDraft, new Callback<Boolean>() {
                        @Override
                        public void success(Boolean aBoolean, Response response) {

                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        mDialog.cancel();
        NewENSActivity.this.finish();
    }

}
