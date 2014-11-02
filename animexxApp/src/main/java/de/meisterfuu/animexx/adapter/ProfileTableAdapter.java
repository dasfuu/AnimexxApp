package de.meisterfuu.animexx.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.objects.profile.ProfileBoxObject;


public class ProfileTableAdapter extends BaseAdapter {

	List<List<String>> mItems;
	Activity mContext;

	public ProfileTableAdapter(List<List<String>> pList, Activity pContext){
		this.mItems = pList;
		this.mContext = pContext;
	}
	

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}


	
	static class ViewHolder {

		public TextView Title, Subtitle;
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
	public List<String> getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {		
		return position;
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
			
			rowView = inflater.inflate(R.layout.listitem_event, null);


			ViewHolder viewHolder = new ViewHolder();
			viewHolder.Title = (TextView) rowView.findViewById(R.id.event_list_item_title);
			viewHolder.Subtitle = (TextView) rowView.findViewById(R.id.event_list_item_subtitle);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		final List<String> datum = mItems.get(position);
		
		//Init
		//holder.Color.setBackgroundResource(R.color.animexx_blue);
		
        StringBuilder sb = new StringBuilder();
        for(String row: datum){
            sb.append(row);
            sb.append(System.getProperty("line.separator"));
        }
		holder.Title.setText(sb.toString());
		holder.Subtitle.setVisibility(View.GONE);

		
		return rowView;
	}

	public void addAll(List<List<String>> list) {
		mItems.addAll(list);
		this.notifyDataSetChanged();		
	}
	
	


}
