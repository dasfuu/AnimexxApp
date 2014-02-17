package de.meisterfuu.animexx.adapter;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.objects.ENSObject;
import de.meisterfuu.animexx.objects.UserObject;
import de.meisterfuu.animexx.utils.imageloader.ImageDownloaderCustom;
import de.meisterfuu.animexx.utils.imageloader.ImageLoaderCustom;
import de.meisterfuu.animexx.utils.imageloader.ImageSaveObject;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ENSFolderAdapter extends BaseAdapter {
	
	List<ENSObject> mItems;
	Activity mContext;
	long mDesign;
	ImageDownloaderCustom ImageLoader = new ImageDownloaderCustom("forenavatar");
	ImageLoaderCustom ImageLoaderProfile = new ImageLoaderCustom("profilbild");
	
	public ENSFolderAdapter(List<ENSObject> pList, Activity pContext, long pDesign){
		this.mItems = pList;
		this.mDesign = pDesign;
		this.mContext = pContext;
	}
	
	boolean mLoading;
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	public void startLoadingAnimation(){
		mLoading = true;
		this.notifyDataSetChanged();
	}
	
	public void stopLoadingAnimation(){
		mLoading = false;
		this.notifyDataSetChanged();
	}
	
	public boolean isLoadingAnimation(){
		return mLoading;
	}
	
	static class ViewHolder {

		public TextView Title, Subtitle, Color;
		public ImageView Avatar, Flag_Answered, Flag_Forwarded;
	}

	@Override
	public int getCount() {
		if(isLoadingAnimation()){
			return mItems.size()+1;
		}
		return mItems.size();
	}
	
	

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		if(isLoadingAnimation() && (position == getCount()-1)){
			return false;
		}
		return true;
	}

	@Override
	public ENSObject getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {		
		if(isLoadingAnimation() && (position == getCount()-1)){
			return System.currentTimeMillis();
		}
		return mItems.get(position).getId();
	}

	@Override
	public int getItemViewType(int position) {
		if(isLoadingAnimation() && (position == getCount()-1)){
			return 1;
		}
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(getItemViewType(position) == 1){
			LayoutInflater inflater = mContext.getLayoutInflater();
			return inflater.inflate(R.layout.listitem_loading, null);			
		}
		
		
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = mContext.getLayoutInflater();
			
			if(mDesign == 1){
				rowView = inflater.inflate(R.layout.listitem_ens, null);
			} else if (mDesign == 2) {
				rowView = inflater.inflate(R.layout.listitem_ens_2, null);
			} else if (mDesign == 3) {
				rowView = inflater.inflate(R.layout.listitem_ens_3, null);
			} else if (mDesign == 4) {
				rowView = inflater.inflate(R.layout.listitem_ens_4, null);
			}

			ViewHolder viewHolder = new ViewHolder();
			viewHolder.Title = (TextView) rowView.findViewById(R.id.ens_list_item_title);
			viewHolder.Subtitle = (TextView) rowView.findViewById(R.id.ens_list_item_subtitle);
			viewHolder.Color = (TextView) rowView.findViewById(R.id.ens_list_item_color);
			viewHolder.Avatar = (ImageView) rowView.findViewById(R.id.ens_list_item_avatar);
			viewHolder.Flag_Answered = (ImageView) rowView.findViewById(R.id.ens_flag_answered);
			viewHolder.Flag_Forwarded = (ImageView) rowView.findViewById(R.id.ens_flag_forwarded);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		final ENSObject ENS = mItems.get(position);
		
		//Init
		holder.Color.setBackgroundResource(R.color.animexx_blue);
		holder.Flag_Answered.getDrawable().setColorFilter(null);
		holder.Flag_Forwarded.getDrawable().setColorFilter(null);
		
		//Subject
		holder.Title.setText(ENS.getSubject());
		
		//Username / Avatar
		UserObject target= new UserObject();
		if(ENS.getAn_ordner() > 0) {
			target = ENS.getVon();
			if(target == null) target = new UserObject();
			holder.Subtitle.setText("Von "+target.getUsername());
			if(ENS.isRead()){
				holder.Color.setBackgroundResource(R.color.bg_blue2);
			}
		} else {
			if(ENS.getAn().size() > 0){
				target = ENS.getAn().get(0);
			}
			if(target == null) target = new UserObject();
			holder.Subtitle.setText("An "+target.getUsername());
			if(ENS.isOutboxRead()){
				holder.Color.setBackgroundResource(R.color.bg_blue2);
			}
		}

		//Flags
		if(ENS.isAnswered()){
			holder.Flag_Answered.setImageResource(R.drawable.ens_flags_answered_blue);
		} else {
			holder.Flag_Answered.setImageResource(R.drawable.ens_flags_answered);
		}
		
		if(ENS.isForwarded()){
			holder.Flag_Forwarded.setImageResource(R.drawable.ens_flags_forwarded_blue);
		} else {
			holder.Flag_Forwarded.setImageResource(R.drawable.ens_flags_forwarded);
		}
		
		//Sys ENS
		if(ENS.getType() == 2){
			
		}
		

		//holder.Avatar.getDrawable().set//.setColorFilter(mContext.getResources().getColor(R.color.animexx_blue), PorterDuff.Mode. );
		
		//Avatar
		if(ImageLoaderProfile.exists(new ImageSaveObject("", target.getId()+""), mContext)){
			holder.Avatar.setVisibility(View.VISIBLE);
			ImageLoader.download(new ImageSaveObject("", target.getId()+""), holder.Avatar);			
		} else {
			if(target.getAvatar() != null){
				holder.Avatar.setVisibility(View.VISIBLE);
				ImageSaveObject image = new ImageSaveObject(target.getAvatar().getUrl(), target.getId()+"");
				System.out.println(target.getAvatar().getUrl());
				ImageLoader.download(image, holder.Avatar);
			} else {
				//holder.Avatar.setVisibility(View.GONE);
				holder.Avatar.setImageResource(R.drawable.ic_contact_picture);
			}
		}
		

		
		
		return rowView;
	}

	public void addAll(ArrayList<ENSObject> list) {
		mItems.addAll(list);
		this.notifyDataSetChanged();		
	}
	
	


}
