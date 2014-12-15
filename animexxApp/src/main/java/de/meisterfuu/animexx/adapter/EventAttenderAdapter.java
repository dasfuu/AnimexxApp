package de.meisterfuu.animexx.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.objects.event.EventAttender;
import de.meisterfuu.animexx.objects.event.EventRoomProgramObject;


public class EventAttenderAdapter extends BaseAdapter {

    List<EventAttender> mItems;
    Activity mContext;

    public EventAttenderAdapter(List<EventAttender> pList, Activity pContext) {
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
    public EventAttender getItem(int position) {
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

            rowView = inflater.inflate(R.layout.listitem_event_attender, null);


            ViewHolder viewHolder = new ViewHolder();
            viewHolder.Title = (TextView) rowView.findViewById(R.id.event_attender_list_item_title);
            viewHolder.Subtitle = (TextView) rowView.findViewById(R.id.event_attender_list_item_subtitle);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        final EventAttender Event = mItems.get(position);

        //Init
        //holder.Color.setBackgroundResource(R.color.animexx_blue);


        String title = Event.getUsername();
        if(Event.getComment() != null && !Event.getComment().isEmpty()){
            title += "\n"+Event.getComment();
        }
        holder.Title.setText(title);
        if(Event.getStatus() == EventAttender.STATUS_SURE){
            holder.Subtitle.setText("");
        } else {
            holder.Subtitle.setText("Unsicher");
        }



        return rowView;
    }

    public void addAll(List<EventAttender> list) {
        mItems.addAll(list);
        this.notifyDataSetChanged();
    }

    public void addAll(EventAttender obj) {
        mItems.add(obj);
    }


}
