package de.meisterfuu.animexx.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.data.Self;
import de.meisterfuu.animexx.objects.RPGObject;
import de.meisterfuu.animexx.objects.RPGObject.PlayerAvatarObject;
import de.meisterfuu.animexx.utils.imageloader.ImageDownloaderCustom;
import de.meisterfuu.animexx.utils.imageloader.ImageSaveObject;

public class RPGAvatarSpinnerAdapter extends BaseAdapter {
	
	List<RPGObject.PlayerAvatarObject> mItems;
	Activity mContext;
	long mUserID;
	ImageDownloaderCustom ImageLoader = new ImageDownloaderCustom("rpgavatar");
	RPGObject mRPG;
	RPGObject.PlayerObject mPlayer;
	
	public RPGAvatarSpinnerAdapter(RPGObject.PlayerObject pPlayer, RPGObject pRPG, Activity pContext){
		mRPG = pRPG;
		mPlayer = pPlayer;
		mUserID = Self.getInstance(pContext).getUserID();
		ArrayList<RPGObject.PlayerAvatarObject> arrayList = new ArrayList<RPGObject.PlayerAvatarObject>();
		
		//Empty "No Avatar" Object
		arrayList.add(new PlayerAvatarObject());
		
		//Avatar Object for this player character combination
		for(RPGObject.PlayerAvatarObject avatar: pPlayer.getAvatars()){
			arrayList.add(avatar);
		}
		
		this.mItems = arrayList;
		this.mContext = pContext;
	}
	

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}


	
	static class ViewHolder {

		public ImageView Icon;
	}

	@Override
	public int getCount() {
		return mItems.size();
	}
	

	@Override
	public boolean areAllItemsEnabled() {
		return true;
	}

	@Override
	public boolean isEnabled(int position) {
		return true;
	}

	@Override
	public RPGObject.PlayerAvatarObject getItem(int position) {
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
			rowView = inflater.inflate(R.layout.listitem_rpg_avatar, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.Icon = (ImageView) rowView.findViewById(R.id.rpgpost_avatar_item_avatar);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		final RPGObject.PlayerAvatarObject chara = mItems.get(position);
		
		holder.Icon.setVisibility(View.VISIBLE);
		
//		media.animexx.onlinewelten.com/rpgs/charaktere/85/RPG-Id/charakterID_avatarID.jpg 
//		85 ist RPG-ID Modulo 100
		
		ImageLoader.download(new ImageSaveObject("http://media.animexx.onlinewelten.com/rpgs/charaktere/"+mRPG.getId()%100+"/"+mRPG.getId()+"/"+mPlayer.getId()+"_"+chara.getId()+".jpg", mRPG.getId()+"_"+mPlayer.getId()+"_"+chara.getId()), holder.Icon);

		
		//holder.Icon.getDrawable().setColorFilter(mContext.getResources().getColor(R.color.animexx_blue), PorterDuff.Mode.MULTIPLY );
		
		return rowView;
	}

	
	


}