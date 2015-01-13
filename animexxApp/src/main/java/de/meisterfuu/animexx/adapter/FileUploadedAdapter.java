package de.meisterfuu.animexx.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.objects.FileUploadReturnObject;
import de.meisterfuu.animexx.objects.UserObject;
import de.meisterfuu.animexx.utils.imageloader.PicassoDownloader;
import de.meisterfuu.animexx.utils.views.RoundedImageView;


public class FileUploadedAdapter extends BaseAdapter {

    List<FileUploadReturnObject> mItems;
    Activity mContext;

    public FileUploadedAdapter(List<FileUploadReturnObject> pList, Activity pContext) {
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
    public FileUploadReturnObject getItem(int position) {
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

            rowView = inflater.inflate(R.layout.listitem_fileupload, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.Title = (TextView) rowView.findViewById(R.id.listitem_file_title);
            viewHolder.Image = (RoundedImageView) rowView.findViewById(R.id.listitem_file_image);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        final FileUploadReturnObject obj = mItems.get(position);

        holder.Title.setText(obj.getFilename());


        if (obj.getUrlThumb() != null) {
            holder.Image.setVisibility(View.VISIBLE);
            PicassoDownloader.getPicasso(mContext).load(obj.getUrlThumb()).stableKey(PicassoDownloader.createFileThumbnailKey(obj.getId())).into(holder.Image);
        } else {
            holder.Image.setVisibility(View.GONE);
        }

        return rowView;
    }

    public void addAll(List<FileUploadReturnObject> list) {
        mItems.addAll(list);
        this.notifyDataSetChanged();
    }

    public void add(FileUploadReturnObject item) {
        mItems.add(item);
        this.notifyDataSetChanged();
    }

}
