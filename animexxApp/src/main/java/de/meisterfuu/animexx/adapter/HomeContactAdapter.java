package de.meisterfuu.animexx.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.objects.home.ContactHomeObject;
import de.meisterfuu.animexx.utils.Helper;
import de.meisterfuu.animexx.utils.imageloader.ImageDownloaderCustom;
import de.meisterfuu.animexx.utils.views.UserViewBig;


public class HomeContactAdapter extends BaseAdapter {

    List<ContactHomeObject> mItems;
    Activity mContext;
    ImageDownloaderCustom ImageLoader = new ImageDownloaderCustom("home_contact");
    private Picasso mPicasso;

    public HomeContactAdapter(List<ContactHomeObject> pList, Activity pContext) {
        this.mItems = pList;
        this.mContext = pContext;
        mPicasso = Picasso.with(mContext);
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
        public TextView Title, btLeft, btRight, Date;
        public ImageView Image;
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
            viewHolder.btLeft = (TextView) rowView.findViewById(R.id.home_list_item_button1);
            viewHolder.btRight = (TextView) rowView.findViewById(R.id.home_list_item_button2);
            viewHolder.Image = (ImageView) rowView.findViewById(R.id.home_list_item_image);
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

//			System.out.println(obj.getBigImageURL());

//			holder.Image.getLayoutParams().height = obj.getBigImageHeight();
//			ImageLoader.download(new ImageSaveObject(obj.getBigImageURL(), obj.getItemID()), holder.Image);


//		} else {
//
//		}

        holder.btLeft.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

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
