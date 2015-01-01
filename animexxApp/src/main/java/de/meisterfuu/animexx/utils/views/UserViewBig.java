package de.meisterfuu.animexx.utils.views;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.profiles.ProfileActivity;
import de.meisterfuu.animexx.objects.UserObject;
import de.meisterfuu.animexx.utils.ColorGenerator;
import de.meisterfuu.animexx.utils.imageloader.ImageDownloaderCustom;
import de.meisterfuu.animexx.utils.imageloader.ImageSaveObject;
import de.meisterfuu.animexx.utils.imageloader.PicassoDownloader;

/**
 * TODO: document your custom view class.
 */
public class UserViewBig extends FrameLayout {

    private Context mContext;
    private LayoutInflater mInflater;
    private LinearLayout mLayout;
    private RoundedImageView mImage;
    private TextView mText;
    private UserObject mUser;
    private static Picasso sPicasso;

    public UserViewBig(Context context) {
        super(context);
        init(context);
    }

    public UserViewBig(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public UserViewBig(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(final Context pContext) {
        this.mContext = pContext;
        if(sPicasso == null){
            try {
                sPicasso = new Picasso.Builder(mContext).downloader(new PicassoDownloader(mContext, "forenavatar")).build();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayout = (LinearLayout) mInflater.inflate(R.layout.view_user_big, null);
        mImage = (RoundedImageView) mLayout.findViewById(R.id.view_user_big_image);
        mText = (TextView) mLayout.findViewById(R.id.view_user_big_text);
        this.addView(mLayout);
    }

    public void setUser(UserObject pUser) {
        mUser = pUser;
        TextDrawable drawable = TextDrawable.builder().buildRound(mUser.getUsername().substring(0,1), ColorGenerator.DEFAULT.getColor(mUser.getId()));
        if (mUser.getAvatar() != null) {
            sPicasso.load(mUser.getAvatar().getUrl()).noFade().error(R.drawable.ic_contact_picture).stableKey(mUser.getId() + "").into(mImage);
        } else {
            mImage.setImageResource(R.drawable.ic_contact_picture);
        }
        mText.setText(mUser.getUsername());
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity.getInstance(mContext, mUser.getId());
            }
        });
    }

    public UserObject getUser() {
        return mUser;
    }

}
