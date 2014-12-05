package de.meisterfuu.animexx.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.objects.rpg.RPGPostObject;
import de.meisterfuu.animexx.utils.Helper;
import de.meisterfuu.animexx.utils.imageloader.ImageDownloaderCustom;
import de.meisterfuu.animexx.utils.imageloader.ImageSaveObject;


public class RPGPostListAdapter extends BaseAdapter {

    List<RPGPostObject> mItems;
    Activity mContext;
    long mRPGID;
    ImageDownloaderCustom ImageLoader = new ImageDownloaderCustom("rpgavatar");

    public RPGPostListAdapter(List<RPGPostObject> pList, Activity pContext, long pRPGID) {
        this.mItems = pList;
        this.mContext = pContext;
        this.mRPGID = pRPGID;
    }

    boolean mLoading;

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    static class ViewHolder {
        public TextView Title, Subtitle, Post;
        public ImageView Avatar;
        public LinearLayout Container;
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
    public RPGPostObject getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position).getPos();
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
            rowView = inflater.inflate(R.layout.listitem_rpg_post, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.Title = (TextView) rowView.findViewById(R.id.rpgpost_item_title);
            viewHolder.Subtitle = (TextView) rowView.findViewById(R.id.rpgpost_item_subtitle);
            viewHolder.Post = (TextView) rowView.findViewById(R.id.rpgpost_item_post);
            viewHolder.Avatar = (ImageView) rowView.findViewById(R.id.rpgpost_item_avatar);
            viewHolder.Container = (LinearLayout) rowView.findViewById(R.id.rpgpost_container);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        final RPGPostObject RPG = mItems.get(position);

        //Color
        if (position % 2 == 0) {
            holder.Container.setBackgroundColor(Color.parseColor("#E8E8FF"));
        } else {
            holder.Container.setBackgroundColor(Color.WHITE);
        }


        //Subject
        if (RPG.getAuthor() != null) {
            holder.Title.setText(RPG.getCharacterName() + " (" + RPG.getAuthor().getUsername() + ")");
        } else {
            holder.Title.setText(RPG.getCharacterName());
        }
        holder.Subtitle.setText(Helper.TimestampToString(Helper.toTimestamp(RPG.getDate(), "yyyy-MM-dd hh:mm:ss"), false));

        if (RPG.isAction()) {
            holder.Post.setTypeface(null, Typeface.ITALIC);
        } else {
            holder.Post.setTypeface(null, Typeface.NORMAL);
        }
        if (!RPG.isInTime()) {
            holder.Post.setTextColor(Color.GRAY);
        } else {
            holder.Post.setTextColor(Color.BLACK);
        }

        holder.Post.setText(Html.fromHtml(RPG.getPost()));

        if (RPG.getAvatarURL() != null) {
            holder.Avatar.setVisibility(View.VISIBLE);
            ImageLoader.download(new ImageSaveObject(RPG.getAvatarURL(), mRPGID + "_" + RPG.getCharacterID() + "_" + RPG.getAvatarID()), holder.Avatar, false);
        } else {
            holder.Avatar.setVisibility(View.GONE);
        }

        return rowView;
    }

    public void addAll(List<RPGPostObject> list) {
        mItems.addAll(list);
        this.notifyDataSetChanged();
    }


}
