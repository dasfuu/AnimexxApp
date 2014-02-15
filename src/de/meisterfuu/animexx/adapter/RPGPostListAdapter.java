package de.meisterfuu.animexx.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.objects.ENSObject;
import de.meisterfuu.animexx.objects.RPGObject;
import de.meisterfuu.animexx.objects.RPGPostObject;
import de.meisterfuu.animexx.objects.UserObject;
import de.meisterfuu.animexx.utils.imageloader.ImageDownloaderCustom;
import de.meisterfuu.animexx.utils.imageloader.ImageLoaderCustom;
import de.meisterfuu.animexx.utils.imageloader.ImageSaveObject;
import android.app.Activity;
import android.graphics.PorterDuff;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class RPGPostListAdapter extends BaseAdapter {
	
	List<RPGPostObject> mItems;
	Activity mContext;
	long mRPGID;
	ImageDownloaderCustom ImageLoader = new ImageDownloaderCustom("rpgavatar");
	
	public RPGPostListAdapter(List<RPGPostObject> pList, Activity pContext, long pRPGID){
		this.mItems = pList;
		this.mContext = pContext;
		this.mRPGID = pRPGID;
	}
	
	boolean mLoading;
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}


	
	static class ViewHolder {
		public TextView Title, Subtitle, Post;
		public ImageView Avatar;
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
	public RPGPostObject getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {		
		return mItems.get(position).getPos();
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
			rowView = inflater.inflate(R.layout.listitem_rpg_post, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.Title = (TextView) rowView.findViewById(R.id.rpgpost_item_title);
			viewHolder.Subtitle = (TextView) rowView.findViewById(R.id.rpgpost_item_subtitle);
			viewHolder.Post = (TextView) rowView.findViewById(R.id.rpgpost_item_post);
			viewHolder.Avatar = (ImageView) rowView.findViewById(R.id.rpgpost_item_avatar);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		final RPGPostObject RPG = mItems.get(position);
		
		
		//Subject
		if(RPG.getAuthor() != null){
			holder.Title.setText(RPG.getCharacterName() + "(" + RPG.getAuthor().getUsername() + ")");
		} else {
			holder.Title.setText(RPG.getCharacterName());
		}
		holder.Subtitle.setText(RPG.getDate());
		holder.Post.setText(Html.fromHtml(RPG.getPost()));
		
		if(RPG.getAvatarURL() != null){
			holder.Avatar.setVisibility(View.VISIBLE);
			ImageLoader.download(new ImageSaveObject(RPG.getAvatarURL(), mRPGID+"_"+RPG.getCharacterID()+"_"+RPG.getAvatarID()), holder.Avatar, false);
		} else {
			holder.Avatar.setVisibility(View.GONE);
		}
		
		return rowView;
	}

	public void addAll(ArrayList<RPGPostObject> list) {
		mItems.addAll(list);
		this.notifyDataSetChanged();		
	}
	
	


}
