package de.meisterfuu.animexx.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.api.broker.ENSBroker;
import de.meisterfuu.animexx.objects.ens.ENSFolderObject;

public class ChatStatusSpinnerAdapter extends BaseAdapter {

    List<String> mItems;
    Activity mContext;

    public ChatStatusSpinnerAdapter(Activity pContext) {
        ArrayList<String> pList = new ArrayList<>();
        pList.add("Online");
        pList.add("Offline");
        this.mItems = pList;
        this.mContext = pContext;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    static class ViewHolder {
        public TextView Title;
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
    public String getItem(int position) {
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
            rowView = inflater.inflate(R.layout.listitem_chatstatus_spinner, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.Title = (TextView) rowView.findViewById(R.id.text1);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.Title.setText(getItem(position));

        return rowView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            rowView = inflater.inflate(R.layout.listitem_chatstatus_spinner_dialog, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.Title = (TextView) rowView.findViewById(R.id.text1);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        holder.Title.setText(getItem(position));

        return rowView;
    }

    public void addAll(ArrayList<String> list) {
        mItems.addAll(list);
        this.notifyDataSetChanged();
    }


}