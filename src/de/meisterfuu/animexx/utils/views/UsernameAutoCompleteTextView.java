package de.meisterfuu.animexx.utils.views;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.data.profile.UserApi;
import de.meisterfuu.animexx.objects.UserObject;
import de.meisterfuu.animexx.utils.APIException;

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
    private UserApi mApi;
	
	public void init() {

		mApi = new UserApi(mContext);
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
            		
          		    mApi.SearchUserByName(last, new APICallback() {					
						@SuppressWarnings("unchecked")
						@Override
						public void onCallback(APIException pError, Object pObject) {
							mUser = (ArrayList<UserObject>)pObject;
							//mAdapter = new ArrayAdapter<UserObject>(mContext, android.R.layout.simple_list_item_1 , mUser);
							mAdapter.clear();
							mAdapter.addAll(mUser);
							mAdapter.notifyDataSetChanged();
							System.out.println(mAdapter.getCount());
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
