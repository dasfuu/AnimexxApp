package de.meisterfuu.animexx.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.profiles.ProfileActivity;
import de.meisterfuu.animexx.api.broker.GBBroker;
import de.meisterfuu.animexx.api.web.ReturnObject;
import de.meisterfuu.animexx.objects.UserObject;
import de.meisterfuu.animexx.objects.profile.GBEntryObject;
import de.meisterfuu.animexx.objects.profile.GBListObject;
import de.meisterfuu.animexx.utils.Helper;
import de.meisterfuu.animexx.utils.imageloader.PicassoDownloader;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class GBListAdapter extends BaseAdapter implements Callback<ReturnObject<GBListObject>> {

    List<GBEntryObject> mItems;
    Activity mContext;
    long mGBID;
    GBBroker mApi;
    int page;
    private boolean mEnd = false;
    private boolean mLoading = false;

    public GBListAdapter(Activity pContext, long pGBID) {
        this.mItems = new ArrayList<GBEntryObject>();
        this.mContext = pContext;
        this.mGBID = pGBID;
        mApi = new GBBroker(pContext);
        mApi.getGBList(mGBID, page++, this);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void success(final ReturnObject<GBListObject> t, final Response response) {
        mLoading = false;
        if (t.getObj() == null) {
            mEnd = true;
            return;
        }
        if (t.getObj().getEntries().size() < 30) {
            mEnd = true;
        }
        this.addAll(t.getObj().getEntries());
    }

    @Override
    public void failure(final RetrofitError error) {
        mLoading = false;
    }


    static class ViewHolder {
        public TextView Title, Subtitle, Post;
        public ImageView Avatar;
        public LinearLayout Container, Header;
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
    public GBEntryObject getItem(int position) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (this.getCount() - position < 3 && !mLoading) {
            if (!mEnd) {
                mLoading = true;
                mApi.getGBList(mGBID, page++, this);
            }
        }

        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = mContext.getLayoutInflater();
            rowView = inflater.inflate(R.layout.listitem_gb, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.Title = (TextView) rowView.findViewById(R.id.gbentry_item_title);
            viewHolder.Subtitle = (TextView) rowView.findViewById(R.id.gbentry_item_subtitle);
            viewHolder.Post = (TextView) rowView.findViewById(R.id.gbentry_item_post);
            viewHolder.Avatar = (ImageView) rowView.findViewById(R.id.gbentry_item_avatar);
            viewHolder.Container = (LinearLayout) rowView.findViewById(R.id.gbentry_container);
            viewHolder.Header = (LinearLayout) rowView.findViewById(R.id.gbentry_item_header);
            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        final GBEntryObject gb_entry = mItems.get(position);

        holder.Header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View pView) {
                ProfileActivity.getInstance(mContext, mItems.get(position).getAuthor().getId());
            }
        });

        //Color
        if (position % 2 == 0) {
            holder.Container.setBackgroundColor(Color.parseColor("#E8E8FF"));
        } else {
            holder.Container.setBackgroundColor(Color.WHITE);
        }


        //Subject
        if (gb_entry.getAuthor() != null) {
            holder.Title.setText(gb_entry.getAuthor().getUsername());
        } else {
            holder.Title.setText(new UserObject().getUsername());
        }
        holder.Subtitle.setText(Helper.TimestampToString(Helper.toTimestamp(gb_entry.getDate(), "yyyy-MM-dd hh:mm:ss"), false));


        holder.Post.setText(Html.fromHtml(gb_entry.getHtml()));

        if (gb_entry.getAvatarURL() != null) {
            holder.Avatar.setVisibility(View.VISIBLE);
            PicassoDownloader.getPicasso(mContext).load(gb_entry.getAvatarURL()).stableKey(PicassoDownloader.createGBAvatarKey(gb_entry.getAvatarURL().hashCode())).into(holder.Avatar);
        } else {
            holder.Avatar.setVisibility(View.GONE);
        }

        return rowView;
    }

    public void addAll(ArrayList<GBEntryObject> list) {
        mItems.addAll(list);
        this.notifyDataSetChanged();
    }


}
