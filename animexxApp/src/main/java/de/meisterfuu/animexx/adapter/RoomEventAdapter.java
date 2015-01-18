package de.meisterfuu.animexx.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.objects.event.EventRoomProgramObject;
import de.meisterfuu.animexx.utils.Helper;


public class RoomEventAdapter extends BaseAdapter {

    List<EventRoomProgramObject.EventProgramEntry> mItems;
    Activity mContext;

    public RoomEventAdapter(List<EventRoomProgramObject.EventProgramEntry> pList, Activity pContext) {
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
    public EventRoomProgramObject.EventProgramEntry getItem(int position) {
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

            rowView = inflater.inflate(R.layout.listitem_room_event, null);


            ViewHolder viewHolder = new ViewHolder();
            viewHolder.Title = (TextView) rowView.findViewById(R.id.room_event_list_item_title);
            viewHolder.Subtitle = (TextView) rowView.findViewById(R.id.room_event_list_item_subtitle);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        final EventRoomProgramObject.EventProgramEntry Event = mItems.get(position);

        //Init
        //holder.Color.setBackgroundResource(R.color.animexx_blue);

        String start = Helper.toDateTimeString(Helper.toTimestamp(Event.getStartUtc()));
        String end = Helper.toDateTimeString(Helper.toTimestamp(Event.getEndUtc()));

        holder.Title.setText(Event.getName());
        holder.Subtitle.setText("Im " + Event.getRoomName() +"\n" + start + " - " + end);


        return rowView;
    }

    public void addAll(List<EventRoomProgramObject.EventProgramEntry> list) {
        mItems.addAll(list);
        this.notifyDataSetChanged();
    }

    public void addAll(EventRoomProgramObject.EventProgramEntry obj) {
        mItems.add(obj);
    }


}
