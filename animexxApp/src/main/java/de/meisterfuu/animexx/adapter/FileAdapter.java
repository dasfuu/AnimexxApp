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
import de.meisterfuu.animexx.objects.FileUploadReturnObject;
import de.meisterfuu.animexx.objects.UploadedFile;
import de.meisterfuu.animexx.utils.imageloader.PicassoDownloader;
import de.meisterfuu.animexx.utils.views.RoundedImageView;


public class FileAdapter extends BaseAdapter {

    List<UploadedFile> mItems;
    Activity mContext;

    public FileAdapter(List<UploadedFile> pList, Activity pContext) {
        this.mItems = pList;
        this.mContext = pContext;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    static class ViewHolder {
        public ImageView Image;
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
    public UploadedFile getItem(int position) {
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

            rowView = inflater.inflate(R.layout.listitem_image, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.Image = (ImageView) rowView.findViewById(R.id.image);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        final UploadedFile obj = mItems.get(position);


        if (obj.getThumbnailUrl() != null) {
            holder.Image.setVisibility(View.VISIBLE);
//            Picasso.with(mContext).load(obj.getThumbnailUrl()).into(holder.Image);
            PicassoDownloader.getPicasso(mContext).load(obj.getThumbnailUrl()).stableKey(PicassoDownloader.createFileThumbnailKey(obj.getId())).into(holder.Image);
        } else {
            holder.Image.setVisibility(View.INVISIBLE);
        }

        return rowView;
    }

    public void addAll(List<UploadedFile> list) {
        mItems.addAll(list);
        this.notifyDataSetChanged();
    }

    public void add(UploadedFile item) {
        mItems.add(item);
        this.notifyDataSetChanged();
    }

}
