package de.meisterfuu.animexx.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.objects.event.EventObject;
import de.meisterfuu.animexx.objects.profile.ProfileBoxObject;


public class ProfileEventAdapter extends BaseAdapter {

	List<ProfileBoxObject.EventBoxObject> mItems;
	Activity mContext;

	public ProfileEventAdapter(List<ProfileBoxObject.EventBoxObject> pList, Activity pContext){
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
	public ProfileBoxObject.EventBoxObject getItem(int position) {
		return mItems.get(position);
	}

	@Override
	public long getItemId(int position) {		
		return mItems.get(position).getEventId();
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
		final ProfileBoxObject.EventBoxObject Event = mItems.get(position);
		
		//Init
		//holder.Color.setBackgroundResource(R.color.animexx_blue);
		

		holder.Title.setText(Event.getName());	
		holder.Subtitle.setText(Event.getDateFrom()+" - "+Event.getDateTo());

		
		return rowView;
	}

	public void addAll(List<ProfileBoxObject.EventBoxObject> list) {
		mItems.addAll(list);
		this.notifyDataSetChanged();		
	}
	
	


}
