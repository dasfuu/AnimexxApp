package de.meisterfuu.animexx.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.api.ens.ENSApi;
import de.meisterfuu.animexx.objects.ENSFolderObject;

public class ENSFolderSpinnerAdapter extends BaseAdapter {
	
	List<ENSFolderObject> mItems;
	Activity mContext;
	
	public ENSFolderSpinnerAdapter(List<ENSFolderObject> pList, Activity pContext){
		this.mItems = pList;
		this.mContext = pContext;
	}
	

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}


	
	static class ViewHolder {

		public TextView Title;
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
	public ENSFolderObject getItem(int position) {
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
			rowView = inflater.inflate(R.layout.listitem_ens_folder, null);
			ViewHolder viewHolder = new ViewHolder();
			viewHolder.Title = (TextView) rowView.findViewById(R.id.ens_folder_title);
			viewHolder.Icon = (ImageView) rowView.findViewById(R.id.ens_folder_icon);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		final ENSFolderObject Folder = mItems.get(position);
		
		//Subject
		holder.Title.setText(Folder.getName());
		
		if(Folder.getType().equals(ENSApi.TYPE_INBOX)) {
			holder.Icon.setImageResource(R.drawable.ens_folder_in);
		} else {
			holder.Icon.setImageResource(R.drawable.ens_folder_out);
		}
		
		//holder.Icon.getDrawable().setColorFilter(mContext.getResources().getColor(R.color.animexx_blue), PorterDuff.Mode.MULTIPLY );
		
		return rowView;
	}

	public void addAll(ArrayList<ENSFolderObject> list) {
		mItems.addAll(list);
		this.notifyDataSetChanged();		
	}
	
	


}