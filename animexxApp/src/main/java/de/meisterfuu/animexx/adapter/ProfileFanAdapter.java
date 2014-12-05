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


public class ProfileFanAdapter extends BaseAdapter {

    List<ProfileBoxObject.FanBoxObject> mItems;
    Activity mContext;

    public ProfileFanAdapter(List<ProfileBoxObject.FanBoxObject> pList, Activity pContext) {
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
    public ProfileBoxObject.FanBoxObject getItem(int position) {
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
        final ProfileBoxObject.FanBoxObject item = mItems.get(position);

        //Init
        //holder.Color.setBackgroundResource(R.color.animexx_blue);


        holder.Title.setText(item.getName());
        if (item.getSeriesType() != null) {
            holder.Subtitle.setText(item.getSeriesType());
        } else {
            holder.Subtitle.setText("Thema");
        }


        return rowView;
    }

    public void addAll(List<ProfileBoxObject.FanBoxObject> list) {
        mItems.addAll(list);
        this.notifyDataSetChanged();
    }


}
