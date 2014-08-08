package de.meisterfuu.animexx.utils.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import de.meisterfuu.animexx.R;

public class TableDataView extends LinearLayout {

	ArrayList<TableDataEntity> mData;
	private Context mContext;
	LayoutInflater mInflater;

	public TableDataView(final Context context) {
		super(context);
		init(context);
	}

	public TableDataView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public TableDataView(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(final Context pContext){
		this.mContext = pContext;
		this.setOrientation(LinearLayout.VERTICAL);
		mData = new ArrayList<TableDataEntity>();
		mInflater = (LayoutInflater)   getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void add(final TableDataEntity pEntry){
		mData.add(pEntry);
		this.addView(pEntry.getView(mInflater));
	}

	public void remove(int index){
		this.removeView(mData.get(index).getView(mInflater));
		mData.remove(index);
	}

	public void clear(){
		this.removeAllViews();
		mData.clear();
	}

	public static class TableDataEntity {

		private Spannable text;
		private int icon_rid;
		private LinearLayout vContainer;
		private TextView vText;
		private ImageView vImage;

		public Spannable getText() {
			return text;
		}

		public void setText(final Spannable pText) {
			text = pText;
		}

		public void setText(final String pText) {
			this.setText(new SpannableString(pText));
		}

		public int getIcon_rid() {
			return icon_rid;
		}

		public void setIcon_rid(final int pIcon_rid) {
			icon_rid = pIcon_rid;
		}

		public LinearLayout getView(final LayoutInflater pInflater) {
			if(vContainer ==  null){
				vContainer = (LinearLayout) pInflater.inflate(R.layout.table_data_entity, null);
				vText = (TextView) vContainer.findViewById(R.id.table_item_text);
				vText.setText(text);
				vImage = (ImageView) vContainer.findViewById(R.id.table_item_image);
				vImage.setImageResource(icon_rid);
			}
			return vContainer;
		}

		public TableDataEntity(final Spannable pText, final int pIcon_rid) {
			text = pText;
			icon_rid = pIcon_rid;
		}

		public TableDataEntity(final String pText, final int pIcon_rid) {
			this(new SpannableString(pText), pIcon_rid);
		}
	}
}
