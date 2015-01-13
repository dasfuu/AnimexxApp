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
import de.meisterfuu.animexx.api.Self;
import de.meisterfuu.animexx.objects.rpg.RPGObject;

public class RPGSpinnerAdapter extends BaseAdapter {

    List<RPGObject.PlayerObject> mItems;
    Activity mContext;
    long mUserID;
    long mRPGID;

    public RPGSpinnerAdapter(ArrayList<RPGObject.PlayerObject> pList, long pID, Activity pContext) {
        mRPGID = pID;
        mUserID = Self.getInstance(pContext).getUserID();
        ArrayList<RPGObject.PlayerObject> arrayList = new ArrayList<RPGObject.PlayerObject>();
        for (RPGObject.PlayerObject chara : pList) {
            if (chara.getUser() == null) continue;
            if (chara.getUser().getId() == mUserID) arrayList.add(chara);
        }
        this.mItems = arrayList;
        this.mContext = pContext;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    static class ViewHolder {

        public TextView Title;
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
    public RPGObject.PlayerObject getItem(int position) {
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
            rowView = inflater.inflate(R.layout.listitem_ens_folder, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.Title = (TextView) rowView.findViewById(R.id.ens_folder_title);
            viewHolder.Icon = (ImageView) rowView.findViewById(R.id.ens_folder_icon);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        final RPGObject.PlayerObject chara = mItems.get(position);

        //Subject
        holder.Title.setText(chara.getCharacterName());
        holder.Icon.setVisibility(View.GONE);
        //ImageLoader.download(RPG.getAvatarURL(), mRPGID+"_"+chara.get+"_"+RPG.getAvatarID()), imageView, clickable)


        //holder.Icon.getDrawable().setColorFilter(mContext.getResources().getColor(R.color.animexx_blue), PorterDuff.Mode.MULTIPLY );

        return rowView;
    }

    public void addAll(ArrayList<RPGObject.PlayerObject> pList) {

        ArrayList<RPGObject.PlayerObject> arrayList = new ArrayList<RPGObject.PlayerObject>();
        for (RPGObject.PlayerObject chara : pList) {
            if (chara.getUser() == null) continue;
            if (chara.getUser().getId() == mUserID) {
                arrayList.add(chara);
            }
        }
        mItems.addAll(arrayList);
        this.notifyDataSetChanged();
    }


}