package de.meisterfuu.animexx.adapter;

import android.app.Activity;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.objects.rpg.RPGObject;
import de.meisterfuu.animexx.utils.imageloader.PicassoDownloader;
import de.meisterfuu.animexx.utils.views.TableDataView;
import de.meisterfuu.animexx.utils.views.UserViewBig;


public class RPGCharacterListAdapter extends BaseAdapter {

    List<RPGObject.PlayerObject> mItems;
    Activity mContext;

    public RPGCharacterListAdapter(List<RPGObject.PlayerObject> pList, Activity pContext) {
        this.mItems = pList;
        this.mContext = pContext;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    static class ViewHolder {
        public TextView Title, Subtitle, Status;
        public UserViewBig User;
        public TableDataView Table;
        public ImageView Image;
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
            rowView = inflater.inflate(R.layout.listitem_rpg_character, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.Title = (TextView) rowView.findViewById(R.id.activity_rpchara_title);
            viewHolder.Subtitle = (TextView) rowView.findViewById(R.id.activity_rpchara_subtitle);
            viewHolder.Status = (TextView) rowView.findViewById(R.id.activity_rpchara_status);
            viewHolder.User = (UserViewBig) rowView.findViewById(R.id.activity_rpchara_user);
            viewHolder.Table = (TableDataView) rowView.findViewById(R.id.activity_rpchara_table);
            viewHolder.Image = (ImageView) rowView.findViewById(R.id.activity_rpchara_img);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        final RPGObject.PlayerObject chara = mItems.get(position);

        holder.Title.setText(chara.getCharacterName());

        if(chara.getUser() != null){
            holder.User.setVisibility(View.VISIBLE);
            holder.User.setUser(chara.getUser());
            holder.Status.setVisibility(View.GONE);
        } else {
            holder.User.setVisibility(View.GONE);
            holder.Status.setVisibility(View.VISIBLE);
            holder.Status.setText("Offen");
        }

        if(chara.getMainPictureUrl() != null){
            PicassoDownloader.getPicasso(mContext).load(chara.getMainPictureUrl()).stableKey(PicassoDownloader.createRPGCharaMainImageKey(chara.getRpgId(), chara.getId())).into(holder.Image);
        } else {
            holder.Image.setImageResource(R.drawable.ic_contact_picture);
        }

        if(chara.getDescription() != null){
            holder.Subtitle.setText(Html.fromHtml(chara.getDescription()));
        } else {
            holder.Subtitle.setText("");
        }

        holder.Table.clear();
        if(chara.getProperties() != null) {
            for (ArrayList<String> row : chara.getProperties()) {
                try {
                    holder.Table.add(new TableDataView.TextTableDataEntity(row.get(0), row.get(1)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return rowView;
    }

    public void addAll(List<RPGObject.PlayerObject> list) {
        mItems.addAll(list);
        this.notifyDataSetChanged();
    }


}
