package de.meisterfuu.animexx.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.meisterfuu.animexx.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ChatAdapter extends BaseAdapter {

	ArrayList<ChatAdapter.Message> mList;
	private Activity mContext;
	private ListView mView;
	
	SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.GERMANY);
	
	public ChatAdapter(ArrayList<ChatAdapter.Message> list, Activity pContext, ListView pView){
		mList = list;
		mContext = pContext;
		mView = pView;
	}
	
	public void add(ChatAdapter.Message pMessage){
		mList.add(pMessage);
		this.notifyDataSetChanged();
		mView.smoothScrollToPositionFromTop(mList.size() - 1, 0);
	}
	
	public void addTop(ChatAdapter.Message pMessage){
		mList.add(0, pMessage);
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void clear() {
		mList.clear();
		this.notifyDataSetChanged();
	}

	static class ViewHolder{
		TextView body, time;
	}

	@Override
	public int getItemViewType(int position) {
		if(((ChatAdapter.Message) this.getItem(position)).isLeft()){
			return 0;
		} else {
			return 1;
		} 
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		ChatAdapter.Message temp = (ChatAdapter.Message) this.getItem(position);
		
		if (rowView == null) {
			LayoutInflater inflater = mContext.getLayoutInflater();
			
			if(temp.isLeft()){
				rowView = inflater.inflate(R.layout.listitem_chat_left, null);
			} else {
				rowView = inflater.inflate(R.layout.listitem_chat_right, null);
			} 
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.body = (TextView) rowView.findViewById(R.id.chat_message_text);
			viewHolder.time = (TextView) rowView.findViewById(R.id.chat_message_time);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		
		holder.body.setText(temp.getBody());
		holder.time.setText(sdf.format(new Date(temp.getTime())));
		
		return rowView;
	}
	
	public static class Message{
		
		String body;
		long time;
		String avatarURL;
		boolean left;
		
		public Message(){
			
		}
		
		public boolean isLeft() {
			return left;
		}

		public void setLeft(boolean left) {
			this.left = left;
		}

		public String getBody() {
			return body;
		}
		public void setBody(String body) {
			this.body = body;
		}
		public long getTime() {
			return time;
		}
		public void setTime(long time) {
			this.time = time;
		}
		public String getAvatarURL() {
			return avatarURL;
		}
		public void setAvatarURL(String avatarURL) {
			this.avatarURL = avatarURL;
		}
		
		
		
	}

}
