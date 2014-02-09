package de.meisterfuu.animexx.utils.views;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.data.APICallback;
import de.meisterfuu.animexx.data.profile.UserApi;
import de.meisterfuu.animexx.objects.UserSearchResultObject;
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
		init(context);
	}

	public UsernameAutoCompleteTextView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public UsernameAutoCompleteTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	Context mContext;
	
	private List<UserSearchResultObject> mUser;	
    public ArrayAdapter<UserSearchResultObject> mAdapter;
    private UserApi mApi;
	
	private void init(Context context) {
		mContext = context;
		mApi = new UserApi(mContext);
		mUser = new ArrayList<UserSearchResultObject>();
		
		this.setOnItemClickListener(this);
		mAdapter = new ArrayAdapter<UserSearchResultObject>(mContext.getApplicationContext(), android.R.layout.simple_dropdown_item_1line, mUser);
		this.setAdapter(mAdapter);
		
		this.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable editable) {
                // TODO Auto-generated method stub

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                // TODO Auto-generated method stub

            }

            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                    
                	mApi.SearchUserByName(s.toString(), new APICallback() {
					
					@SuppressWarnings("unchecked")
					@Override
					public void onCallback(APIException pError, Object pObject) {
						mUser = (ArrayList<UserSearchResultObject>)pObject;
						
						mAdapter = new ArrayAdapter<UserSearchResultObject>(mContext.getApplicationContext(), android.R.layout.simple_dropdown_item_1line, mUser);
						UsernameAutoCompleteTextView.this.setAdapter(mAdapter);
						mAdapter.notifyDataSetChanged();
					}
				});
                
            }

        });
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		UserSearchResultObject user = mAdapter.getItem(position);
		this.setText("");
		Toast.makeText(mContext, user.getUsername()+" "+user.getId(), Toast.LENGTH_SHORT).show();
	}

}
