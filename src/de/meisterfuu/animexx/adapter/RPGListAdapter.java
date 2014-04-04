package de.meisterfuu.animexx.adapter;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.rpg.RPGDetailActivity;
import de.meisterfuu.animexx.objects.RPGObject;
import de.meisterfuu.animexx.utils.Helper;
import de.meisterfuu.animexx.utils.imageloader.ImageDownloaderCustom;
import de.meisterfuu.animexx.utils.imageloader.ImageLoaderCustom;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class RPGListAdapter extends BaseAdapter {
	
	List<RPGObject> mItems;
	Activity mContext;
	ImageDownloaderCustom ImageLoader = new ImageDownloaderCustom("forenavatar");
	ImageLoaderCustom ImageLoaderProfile = new ImageLoaderCustom("profilbild");
	
	public RPGListAdapter(List<RPGObject> pList, Activity pContext){
		this.mItems = pList;
		this.mContext = pContext;
	}
	
	boolean mLoading;
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}


	
	static class ViewHolder {
		public TextView Title, Count, Date, Topic, Detail;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}
	
	

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	@Override
	public RPGObject getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {		
		return mItems.get(position).getId();
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = mContext.getLayoutInflater();
			rowView = inflater.inflate(R.layout.listitem_rpg, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.Title = (TextView) rowView.findViewById(R.id.rpg_list_item_title);
			viewHolder.Date = (TextView) rowView.findViewById(R.id.rpg_list_item_lastpost);
			viewHolder.Count = (TextView) rowView.findViewById(R.id.rpg_list_item_postcount);
			viewHolder.Topic = (TextView) rowView.findViewById(R.id.rpg_list_item_topic);
			viewHolder.Detail = (TextView) rowView.findViewById(R.id.rpg_list_item_detail);
			
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		final RPGObject RPG = mItems.get(position);
		
		//Init
		
		//Subject
		holder.Title.setText(RPG.getName());
		holder.Date.setText("Letzter Post "+Helper.TimestampToString(Helper.toTimestamp(RPG.getLastPostDate(), "yyyy-MM-dd hh:mm:ss"), false));
		holder.Count.setText(RPG.getPostCount()+" Posts");
		holder.Topic.setText(RPG.getTopicName());
		
		holder.Detail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RPGDetailActivity.getInstance(mContext, RPG.getId());		
			}
		});
		
		return rowView;
	}

	public void addAll(ArrayList<RPGObject> list) {
		mItems.addAll(list);
		this.notifyDataSetChanged();		
	}
	
	


}
