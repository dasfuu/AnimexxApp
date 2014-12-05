package de.meisterfuu.animexx.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.objects.profile.GBInfoObject;

public class GBAvatarSpinnerAdapter extends BaseAdapter {

    List<GBInfoObject.GBAvatar> mItems;
    Activity mContext;
    long mUserID;

    public GBAvatarSpinnerAdapter(List<GBInfoObject.GBAvatar> mItems, Activity pContext) {
        this.mItems = mItems;
        this.mContext = pContext;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    static class ViewHolder {

        public ImageView Icon;
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
    public GBInfoObject.GBAvatar getItem(int position) {
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
            rowView = inflater.inflate(R.layout.listitem_rpg_avatar, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.Icon = (ImageView) rowView.findViewById(R.id.rpgpost_avatar_item_avatar);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        final GBInfoObject.GBAvatar avatar = mItems.get(position);

        holder.Icon.setVisibility(View.VISIBLE);

//		media.animexx.onlinewelten.com/rpgs/charaktere/85/RPG-Id/charakterID_avatarID.jpg 
//		85 ist RPG-ID Modulo 100

        Picasso.with(mContext).load(avatar.getUrl()).stableKey("gbavatar_" + avatar.getId()).into(holder.Icon);
        //ImageLoader.download(new ImageSaveObject("http://media.animexx.onlinewelten.com/rpgs/charaktere/"+mRPG.getId()%100+"/"+mRPG.getId()+"/"+mPlayer.getId()+"_"+chara.getId()+".jpg", mRPG.getId()+"_"+mPlayer.getId()+"_"+chara.getId()), holder.Icon);


        //holder.Icon.getDrawable().setColorFilter(mContext.getResources().getColor(R.color.animexx_blue), PorterDuff.Mode.MULTIPLY );

        return rowView;
    }


}