package de.meisterfuu.animexx.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.objects.ENSObject;
import de.meisterfuu.animexx.objects.RPGObject;
import de.meisterfuu.animexx.objects.UserObject;
import de.meisterfuu.animexx.utils.imageloader.ImageDownloaderCustom;
import de.meisterfuu.animexx.utils.imageloader.ImageLoaderCustom;
import de.meisterfuu.animexx.utils.imageloader.ImageSaveObject;
import android.app.Activity;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
		public TextView Title, Subtitle, Color;
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
			viewHolder.Subtitle = (TextView) rowView.findViewById(R.id.rpg_list_item_subtitle);
			viewHolder.Color = (TextView) rowView.findViewById(R.id.rpg_list_item_color);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		final RPGObject RPG = mItems.get(position);
		
		//Init
		holder.Color.setBackgroundResource(R.color.animexx_blue);
		
		//Subject
		holder.Title.setText(RPG.getName());
		holder.Subtitle.setText("Letzter Post: "+RPG.getLastPostDate());

		
		return rowView;
	}

	public void addAll(ArrayList<RPGObject> list) {
		mItems.addAll(list);
		this.notifyDataSetChanged();		
	}
	
	


}
