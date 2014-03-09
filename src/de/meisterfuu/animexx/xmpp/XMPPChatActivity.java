package de.meisterfuu.animexx.xmpp;

import java.util.ArrayList;
import java.util.Collection;

import org.jivesoftware.smack.AndroidConnectionConfiguration;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.R.layout;
import de.meisterfuu.animexx.R.menu;
import de.meisterfuu.animexx.activitys.ens.SingleENSActivity;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class XMPPChatActivity extends Activity  {

	Handler h;
	ListView lv;
	EditText mNewMessageTx;
	Button mNewMessageBt;
	ArrayAdapter<String> adapter;
	ArrayList<String> list;
	String mjabberName;
	
	public static void getInstance(Context pContext,String jabberName){
		Intent i = new Intent().setClass(pContext, XMPPChatActivity.class);
	     Bundle args = new Bundle();
	     args.putString("name", jabberName);
	     i.putExtras(args);
	     pContext.startActivity(i);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xmpptest);
		mNewMessageTx = (EditText) this.findViewById(R.id.xmpp_test_send);
		mNewMessageBt = (Button) this.findViewById(R.id.xmpp_test_send_bt);
		lv = (ListView) this.findViewById(R.id.xmpp_test_list);
		list = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
		lv.setAdapter(adapter);
		
		Bundle extras = this.getIntent().getExtras();
		mjabberName = extras.getString("name");
		
//		Intent intent = new Intent(this, XMPPService.class);
//		startService(intent);
	 
		final BroadcastReceiver receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if (action.equals(XMPPService.NEW_MESSAGE)) {
					String from = intent.getStringExtra(XMPPService.BUNDLE_FROM);
					String time = intent.getStringExtra(XMPPService.BUNDLE_TIME);
					String message = intent.getStringExtra(XMPPService.BUNDLE_MESSAGE_BODY);
					list.add(from+":\n"+message);
					adapter.notifyDataSetChanged();
				}
			}
		};
		
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(XMPPService.NEW_MESSAGE);
		registerReceiver(receiver, filter);
		
		mNewMessageBt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(XMPPService.SEND_MESSAGE);
				intent.setPackage(XMPPChatActivity.this.getPackageName());
				intent.putExtra(XMPPService.BUNDLE_MESSAGE_BODY, mNewMessageTx.getText().toString());
				intent.putExtra(XMPPService.BUNDLE_TO, mjabberName);
				getApplicationContext().sendBroadcast(intent);
				list.add("Me:\n"+mNewMessageTx.getText().toString());
				mNewMessageTx.setText("");
			}
		});
	    
	}

	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.xmpptest, menu);
		return true;
	}

	

//	public void displayBuddyList() {
//		Roster roster = connection.getRoster();
//		Collection<RosterEntry> entries = roster.getEntries();
//		
//
//		print("\n\n" + roster.getEntryCount() + " buddy(ies):");
//		for (RosterEntry r : entries) {
//			print(r.getUser());
//		}
//	}


}
