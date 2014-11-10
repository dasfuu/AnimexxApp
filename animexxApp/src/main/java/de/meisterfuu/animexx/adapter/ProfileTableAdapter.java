package de.meisterfuu.animexx.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.meisterfuu.animexx.R;


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

		public TextView Left, Right;
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
			
			rowView = inflater.inflate(R.layout.listitem_profile_table, null);


			ViewHolder viewHolder = new ViewHolder();
			viewHolder.Left = (TextView) rowView.findViewById(R.id.list_item_profile_table_left);
			viewHolder.Right = (TextView) rowView.findViewById(R.id.list_item_profile_table_right);
			rowView.setTag(viewHolder);
		}

		ViewHolder holder = (ViewHolder) rowView.getTag();
		final List<String> datum = mItems.get(position);
        String first = "";
        if(datum.size() > 0){
            first = datum.get(0);
        }

		//Init
		//holder.Color.setBackgroundResource(R.color.animexx_blue);
		
        StringBuilder sb = new StringBuilder();
        for(int i = 1; i < datum.size(); i++){
            sb.append(datum.get(i));
            if(i < (datum.size()-1))sb.append(System.getProperty("line.separator"));
        }
		holder.Left.setText(first);
		holder.Right.setText(sb.toString());

		
		return rowView;
	}

	public void addAll(List<List<String>> list) {
		mItems.addAll(list);
		this.notifyDataSetChanged();		
	}
	
	


}
