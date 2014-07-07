package de.meisterfuu.animexx.adapter;

import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.RequestTransformer;
import com.squareup.picasso.Request;
import com.squareup.picasso.Picasso.Builder;

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
	private Picasso mPicasso;
	
	public HomeContactAdapter(List<ContactHomeObject> pList, Activity pContext){
		this.mItems = pList;
		this.mContext = pContext;
		mPicasso = Picasso.with(mContext);
		mPicasso.setDebugging(false);
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


	public String getId(int position) {		
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
		
		//Text
		holder.Title.setText(obj.toString());
		
		if(obj.getBigImageURL() != null && !obj.getBigImageURL().equals("null")){
//			System.out.println(obj.getBigImageURL());
			holder.Image.setVisibility(View.VISIBLE);
			holder.Image.getLayoutParams().height = obj.getBigImageHeight();
//			ImageLoader.download(new ImageSaveObject(obj.getBigImageURL(), obj.getItemID()), holder.Image);

			mPicasso.load(obj.getBigImageURL()).into(holder.Image);
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

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	


}
