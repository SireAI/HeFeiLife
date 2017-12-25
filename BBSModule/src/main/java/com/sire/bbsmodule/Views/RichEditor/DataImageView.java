package com.sire.bbsmodule.Views.RichEditor;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

/**
 * 这只是一个简单的ImageView，可以存放Bitmap和Path等信息
 * 
 * @author xmuSistone
 * 
 */
public class DataImageView extends AppCompatImageView {
	
	private String absolutePath;
	private int bitmapWidth;
	private int bitmapHeight;

	public int getBitmapWidth() {
		return bitmapWidth;
	}

	public void setBitmapWidth(int bitmapWidth) {
		this.bitmapWidth = bitmapWidth;
	}

	public int getBitmapHeight() {
		return bitmapHeight;
	}

	public void setBitmapHeight(int bitmapHeight) {
		this.bitmapHeight = bitmapHeight;
	}

	public DataImageView(Context context) {
		this(context, null);
	}

	public DataImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DataImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}


}
