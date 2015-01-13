package de.meisterfuu.animexx.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.objects.UserObject;
import de.meisterfuu.animexx.objects.profile.ProfileBoxObject;
import de.meisterfuu.animexx.utils.imageloader.PicassoDownloader;
import de.meisterfuu.animexx.utils.views.RoundedImageView;


public class ContactAdapter extends BaseAdapter {

    List<UserObject> mItems;
    Activity mContext;

    public ContactAdapter(List<UserObject> pList, Activity pContext) {
        this.mItems = pList;
        this.mContext = pContext;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    static class ViewHolder {
        public TextView Title;
        public RoundedImageView Image;
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
    public UserObject getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
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

            rowView = inflater.inflate(R.layout.listitem_contact, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.Title = (TextView) rowView.findViewById(R.id.listitem_contact_title);
            viewHolder.Image = (RoundedImageView) rowView.findViewById(R.id.listitem_contact_image);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        final UserObject user = mItems.get(position);

        holder.Title.setText(user.getUsername());

        if (user.getAvatar() != null) {
            PicassoDownloader.getAvatarPicasso(mContext).load(user.getAvatar().getUrl()).error(R.drawable.ic_contact_picture).stableKey(PicassoDownloader.createAvatarKey(user.getId())).into(holder.Image);
        } else {
            holder.Image.setImageResource(R.drawable.ic_contact_picture);
        }

        return rowView;
    }

    public void addAll(List<UserObject> list) {
        mItems.addAll(list);
        this.notifyDataSetChanged();
    }


}
