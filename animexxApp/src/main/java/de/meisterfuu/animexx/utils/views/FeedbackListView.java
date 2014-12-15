package de.meisterfuu.animexx.utils.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.meisterfuu.animexx.R;

/**
 * Created by Furuha on 15.12.2014.
 */
public class FeedbackListView extends ListView{

    public FeedbackListView(Context context) {
        super(context);
    }

    public FeedbackListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FeedbackListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("NewApi")
    public FeedbackListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private View mTargetView;
    private Context mContext;
    private LayoutInflater mInflater;
    private FrameLayout mContainer;
    private ArrayList<View> mDefaultViews;

    private View mLayoutLoadingContent;
    private View mLayoutError;

    private final String TAG_ERROR 	 =  "ERROR";
    private final String TAG_LOADING_CONTENT =  "LOADING_CONTENT";
    private final String TAG_LIST =  "LIST";


    private boolean inited = false;

    private void init() {
        if(inited){
            return;
        } else {
            inited = true;
        }
        this.mContext 		= this.getContext();
        this.mInflater 		= (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mTargetView 	= this;
        this.mContainer 	= new FrameLayout(mContext);
        this.mDefaultViews	= new ArrayList<>();

        setDefaultViews();

        AbsListView abslistview = (AbsListView)mTargetView;
        abslistview.setVisibility(View.GONE);
        ViewGroup parent = (ViewGroup) abslistview.getParent();
        if(mContainer!=null){
            parent.addView(mContainer);
            //abslistview.setEmptyView(mContainer);
        }else
            throw new IllegalArgumentException("mContainer is null !");
    }


    private void setDefaultViews(){

        mLayoutLoadingContent = initView(R.layout.loading_animation, TAG_LOADING_CONTENT);
        mLayoutError = initView(R.layout.error_view, TAG_ERROR);

        mDefaultViews.add(0,mLayoutError);
        mDefaultViews.add(1,mLayoutLoadingContent);

        // Hide all layouts at first initialization
        mLayoutError.setVisibility(View.GONE);
        mLayoutLoadingContent.setVisibility(View.GONE);

        // init Layout params
        FrameLayout.LayoutParams containerParams = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, Gravity.CENTER);
        // init new RelativeLayout Wrapper
        mContainer.setLayoutParams(containerParams);

        // Add default views
        mContainer.addView(mLayoutLoadingContent);
        mContainer.addView(mLayoutError);
    }

    private View initView(int layout, String tag){
        View view = mInflater.inflate(layout, null,false);

        view.setTag(tag);
        view.setVisibility(View.GONE);

        return view;
    }

    public void showLoading(){
        init();
        mTargetView.setVisibility(View.INVISIBLE);
        show(TAG_LOADING_CONTENT);
    }

    public void showList(){
        init();
        show(TAG_LIST);
        mTargetView.setVisibility(View.VISIBLE);
    }

    public void showError(String pMessage){
        init();
        ((TextView)mLayoutError.findViewById(R.id.error_view_text)).setText(pMessage);
        mTargetView.setVisibility(View.INVISIBLE);
        show(TAG_ERROR);
    }

    private void show(String tag){
        ArrayList<View> views =  new ArrayList<>(mDefaultViews);
        for(View view : views){
            if(view.getTag()!=null && view.getTag().toString().equals(tag)){
                view.setVisibility(View.VISIBLE);
            }else{
                view.setVisibility(View.GONE);
            }
        }
    }

}
