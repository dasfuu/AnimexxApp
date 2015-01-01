package de.meisterfuu.animexx.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.objects.home.ContactHomeObject;
import de.meisterfuu.animexx.utils.Helper;
import de.meisterfuu.animexx.utils.imageloader.ImageDownloaderCustom;
import de.meisterfuu.animexx.utils.imageloader.PicassoDownloader;
import de.meisterfuu.animexx.utils.views.UserViewBig;


public class HomeContactAdapter extends BaseAdapter {

    List<ContactHomeObject> mItems;
    Activity mContext;
    ImageDownloaderCustom ImageLoader = new ImageDownloaderCustom("home_contact");
    private Picasso mPicasso;

    public HomeContactAdapter(List<ContactHomeObject> pList, Activity pContext) {
        this.mItems = pList;
        this.mContext = pContext;
        mPicasso = new Picasso.Builder(pContext).downloader(new PicassoDownloader(pContext, "home_contacts")).build();
        mPicasso.setIndicatorsEnabled(false);
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
        public TextView Title, btComments, btWeb, Date;
        public ImageView Image, IcComments, IcWeb;
        public UserViewBig User;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            rowView = inflater.inflate(R.layout.listitem_home_contact, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.Title = (TextView) rowView.findViewById(R.id.home_list_item_title);
            viewHolder.Date = (TextView) rowView.findViewById(R.id.home_list_item_date);
            viewHolder.btComments = (TextView) rowView.findViewById(R.id.home_list_item_button_comments);
            viewHolder.btWeb = (TextView) rowView.findViewById(R.id.home_list_item_button_web);
            viewHolder.Image = (ImageView) rowView.findViewById(R.id.home_list_item_image);
            viewHolder.IcComments = (ImageView) rowView.findViewById(R.id.home_list_item_ic_comments);
            viewHolder.IcWeb = (ImageView) rowView.findViewById(R.id.home_list_item_ic_web);
            viewHolder.User = (UserViewBig) rowView.findViewById(R.id.home_list_item_author);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        final ContactHomeObject obj = mItems.get(position);

        //Init

        //Text
        holder.Title.setText(obj.toString());
        holder.Date.setText(Helper.TimestampToString(obj.getServerTS() * 1000, false));


        holder.Image.setVisibility(View.GONE);

        if (obj.getBigImageURL() != null) {
            holder.Image.setVisibility(View.VISIBLE);
            int height = obj.getBigImageHeight();
            if (height == 0) {
                height = 300;
            }
            holder.Image.getLayoutParams().height = height;
            mPicasso.load(obj.getBigImageURL()).stableKey("home_contact_" + obj.getItemID()).into(holder.Image);
        }

        holder.User.setVisibility(View.GONE);

        if (obj.getVon() != null) {
            holder.User.setVisibility(View.VISIBLE);
            holder.User.setUser(obj.getVon());
        }

        if(obj.isCommentable()){
            holder.btComments.setText(obj.getCommentCount()+" Kommentare");
            holder.btComments.setVisibility(View.VISIBLE);
            holder.IcComments.setVisibility(View.VISIBLE);
            OnClickListener commentsClick = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Comments", Toast.LENGTH_SHORT).show();
                }
            };
            holder.btComments.setOnClickListener(commentsClick);
            holder.IcComments.setOnClickListener(commentsClick);
        } else {
            holder.btComments.setVisibility(View.INVISIBLE);
            holder.IcComments.setVisibility(View.INVISIBLE);
        }

        holder.btWeb.setText("Web");
        OnClickListener webClick = new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        };
        holder.btWeb.setOnClickListener(webClick);
        holder.IcWeb.setOnClickListener(webClick);


        return rowView;
    }

    public void addAll(ArrayList<ContactHomeObject> list) {
        mItems.addAll(list);
        this.notifyDataSetChanged();
    }

    public void addAllBeginning(ArrayList<ContactHomeObject> list) {
        mItems.addAll(0, list);
        this.notifyDataSetChanged();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


}
