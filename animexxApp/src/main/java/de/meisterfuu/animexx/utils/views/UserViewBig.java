package de.meisterfuu.animexx.utils.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.meisterfuu.animexx.R;
import de.meisterfuu.animexx.activitys.profiles.ProfileActivity;
import de.meisterfuu.animexx.objects.UserObject;
import de.meisterfuu.animexx.utils.imageloader.ImageDownloaderCustom;
import de.meisterfuu.animexx.utils.imageloader.ImageSaveObject;

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
    ImageDownloaderCustom mImageLoader = new ImageDownloaderCustom("forenavatar");

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
        mInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayout = (LinearLayout) mInflater.inflate(R.layout.view_user_big, null);
        mImage = (RoundedImageView) mLayout.findViewById(R.id.view_user_big_image);
        mText = (TextView) mLayout.findViewById(R.id.view_user_big_text);
        this.addView(mLayout);
    }

    public void setUser(UserObject pUser) {
        mUser = pUser;
        if (mUser.getAvatar() != null) {
            ImageSaveObject image = new ImageSaveObject(mUser.getAvatar().getUrl(), mUser.getId() + "");
            mImageLoader.download(image, mImage);
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
