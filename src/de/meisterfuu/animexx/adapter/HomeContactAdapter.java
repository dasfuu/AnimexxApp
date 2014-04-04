package de.meisterfuu.animexx.adapter;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.objects.home.ContactHomeObject;
import de.meisterfuu.animexx.utils.imageloader.ImageDownloaderCustom;
import de.meisterfuu.animexx.utils.imageloader.ImageSaveObject;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class HomeContactAdapter extends BaseAdapter {
	
	List<ContactHomeObject> mItems;
	Activity mContext;
	ImageDownloaderCustom ImageLoader = new ImageDownloaderCustom("home_contact");
	
	public HomeContactAdapter(List<ContactHomeObject> pList, Activity pContext){
		this.mItems = pList;
		this.mContext = pContext;
	}
	
	boolean mLoading;
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
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
	public ContactHomeObject getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {		
		return mItems.get(position).getItemID();
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	static class ViewHolder {
		public TextView Title, Detail;
		public ImageView Image;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		
		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = mContext.getLayoutInflater();
			rowView = inflater.inflate(R.layout.listitem_home_contact, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.Title = (TextView) rowView.findViewById(R.id.home_list_item_title);
			viewHolder.Detail = (TextView) rowView.findViewById(R.id.home_list_item_detail);
			viewHolder.Image = (ImageView) rowView.findViewById(R.id.home_list_item_image);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		final ContactHomeObject obj = mItems.get(position);
		
		//Init
		
		//Subject
		if(obj.getItemName() != null){
			holder.Title.setText(obj.getItemName()+"\n"+obj.getVon().getUsername());
		} else {
			holder.Title.setText(obj.getEventName()+"\n"+obj.getVon().getUsername());
		}
		
		if(obj.getBigImageURL() != null){
			System.out.println(obj.getBigImageURL());
			ImageLoader.download(new ImageSaveObject(obj.getBigImageURL(), obj.getItemID()+""), holder.Image);
			holder.Image.setVisibility(View.VISIBLE);
		} else {
			holder.Image.setVisibility(View.GONE);
		}

		holder.Detail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

			}
		});
		
		return rowView;
	}

	public void addAll(ArrayList<ContactHomeObject> list) {
		mItems.addAll(list);
		this.notifyDataSetChanged();		
	}
	
	


}
