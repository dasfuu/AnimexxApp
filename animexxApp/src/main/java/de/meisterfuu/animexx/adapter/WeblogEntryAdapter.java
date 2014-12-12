package de.meisterfuu.animexx.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.objects.weblog.WeblogEntryObject;
import de.meisterfuu.animexx.utils.views.UserViewBig;


public class WeblogEntryAdapter extends BaseAdapter {

    List<WeblogEntryObject> mItems;
    Activity mContext;

    public WeblogEntryAdapter(List<WeblogEntryObject> pList, Activity pContext) {
        this.mItems = pList;
        this.mContext = pContext;
    }

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
    public WeblogEntryObject getItem(int position) {
        return mItems.get(position);
    }


    public String getId(int position) {
        return mItems.get(position).getId()+"";
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
        public TextView Title, btLeft, btRight, Date;
        public TextView Text;
        public UserViewBig User;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            rowView = inflater.inflate(R.layout.listitem_weblog_entry, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.Title = (TextView) rowView.findViewById(R.id.weblog_list_item_title);
            viewHolder.Date = (TextView) rowView.findViewById(R.id.weblog_list_item_date);
            viewHolder.btLeft = (TextView) rowView.findViewById(R.id.weblog_list_item_button1);
            viewHolder.btRight = (TextView) rowView.findViewById(R.id.weblog_list_item_button2);
            viewHolder.Text = (TextView) rowView.findViewById(R.id.weblog_list_item_text);
            viewHolder.User = (UserViewBig) rowView.findViewById(R.id.weblog_list_item_author);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        final WeblogEntryObject obj = mItems.get(position);

        //Init

        //Text
        holder.Title.setText(obj.getTitle());
        holder.Date.setText(obj.getDatum());
        if(obj.getTextLong() != null && !obj.getTextLong().isEmpty()){
            holder.Text.setText(Html.fromHtml(obj.getTextLong()));
        } else {
            holder.Text.setText(Html.fromHtml(obj.getTextShort()));
        }

        holder.User.setVisibility(View.GONE);

        if (obj.getAuthor() != null) {
            holder.User.setVisibility(View.VISIBLE);
            holder.User.setUser(obj.getAuthor());
        }

        holder.btLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        return rowView;
    }

    public void addAll(List<WeblogEntryObject> list) {
        mItems.addAll(list);
        this.notifyDataSetChanged();
    }

    public void addAllBeginning(List<WeblogEntryObject> list) {
        mItems.addAll(0, list);
        this.notifyDataSetChanged();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


}
