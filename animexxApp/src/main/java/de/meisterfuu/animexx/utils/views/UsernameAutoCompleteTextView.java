package de.meisterfuu.animexx.utils.views;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.api.broker.UserBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.UserObject;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

public class UsernameAutoCompleteTextView extends MultiAutoCompleteTextView implements OnItemClickListener {

	public UsernameAutoCompleteTextView(Context context) {
		super(context);
		mContext = context;
	}

	public UsernameAutoCompleteTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
	}

	public UsernameAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}
	
	Context mContext;
	
	private List<UserObject> mUser;	
    public ArrayAdapter<UserObject> mAdapter;
    private UserBroker mApi;
	private int seq = 0;


	public void init() {

		mApi = new UserBroker(mContext);
		mUser = new ArrayList<UserObject>();

		
		//this.setOnItemClickListener(this);
		this.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		this.setThreshold(1);
		mAdapter = new ArrayAdapter<UserObject>(mContext, android.R.layout.simple_list_item_1, mUser);
		this.setAdapter(mAdapter);
		
		this.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable editable) {
                // TODO Auto-generated method stub

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            	
					String[] t = UsernameAutoCompleteTextView.this.getText().toString().split(",");
					String last = "";
					if(t.length > 0) last = t[t.length-1].trim();
            		if(last.length() == 0) return;

	                seq++;
	                final int current_seq = seq;
            		
          		    mApi.searchUserByName(last, new Callback<ReturnObject<List<UserObject>>>() {
		                @Override
		                public void success(final ReturnObject<List<UserObject>> t, final Response response) {
			                if(current_seq != seq){
				                return;
			                }
			                mUser =  t.getObj();
			                //mAdapter = new ArrayAdapter<UserObject>(mContext, android.R.layout.simple_list_item_1 , mUser);
			                mAdapter.clear();
			                mAdapter.addAll(mUser);
			                mAdapter.notifyDataSetChanged();
			                System.out.println(mAdapter.getCount());
		                }

		                @Override
		                public void failure(final RetrofitError error) {

		                }
	                });
            }

        });
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		UserObject user = mAdapter.getItem(position);
		this.setText("");
		Toast.makeText(mContext, user.getUsername()+" "+user.getId(), Toast.LENGTH_SHORT).show();
	}

}
