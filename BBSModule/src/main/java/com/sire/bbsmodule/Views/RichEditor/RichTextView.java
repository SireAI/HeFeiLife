package com.sire.bbsmodule.Views.RichEditor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.sire.bbsmodule.Pojo.EditData;
import com.sire.bbsmodule.R;
import com.sire.bbsmodule.Utils.ScreenUtils;
import com.sire.bbsmodule.Views.EmojiView.EmojiTextView;
import com.sire.corelibrary.DI.Environment.GlideConfigure;
import com.sire.corelibrary.Utils.CommonUtils;

import java.net.URL;
import java.util.List;

/**
 * 这是一个富文本编辑器，给外部提供insertImage接口，添加的图片跟当前光标所在位置有关
 *
 * @author xmuSistone
 */
@SuppressLint({"NewApi", "InflateParams"})
public class RichTextView extends ScrollView {

    private int viewTagIndex = 1; // 新生的view都会打一个tag，对每个view来说，这个tag是唯一的。
    private LinearLayout allLayout; // 这个是所有子view的容器，scrollView内部的唯一一个ViewGroup
    private LayoutInflater inflater;


    public RichTextView(Context context) {
        this(context, null);
    }

    public RichTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);

        // 1. 初始化allLayout
        allLayout = new LinearLayout(context);
        allLayout.setOrientation(LinearLayout.VERTICAL);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        addView(allLayout, layoutParams);
    }


    private void addContentText(String lineData) {
        LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        TextView firstEdit = createTextView();
        firstEdit.setText(lineData);
        allLayout.addView(firstEdit, firstEditParam);
    }

    private void addTitleText(String title) {
        LinearLayout.LayoutParams titleEditParam = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        TextView titleEdit = createTextView();
        titleEdit.setTextColor(getResources().getColor(R.color.color_title_black));
        titleEdit.setTextSize(20);
        titleEdit.setText(title);
        allLayout.addView(titleEdit, titleEditParam);
    }


    /**
     * 生成文本输入框
     */
    private TextView createTextView() {
        EmojiTextView editText = (EmojiTextView) inflater.inflate(R.layout.view_commponent_textview_item,
                null);
        editText.setTag(viewTagIndex++);
        return editText;
    }

    /**
     * 生成图片View
     */
    private RelativeLayout createImageLayout() {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(
                R.layout.view_commonpent_textview_imageview, null);

        return layout;
    }


    /**
     * 在特定位置添加ImageView
     */
    private void addImageView(String imagePath) {
        final RelativeLayout imageLayout = createImageLayout();
        DataImageView imageView = (DataImageView) imageLayout
                .findViewById(R.id.edit_imageView);
        RequestOptions configure = GlideConfigure.getConfigure(DiskCacheStrategy.NONE);
        //图片适配屏幕尺寸，根据地址上的匡高和控件的宽高进行缩放
        double width = Double.valueOf(ScreenUtils.getScreenWidth((Activity) getContext()));
        width = width - 2*CommonUtils.dip2px(getContext(),getResources().getDimension(R.dimen.post_padding));
        double height = Double.valueOf(ScreenUtils.getScreenHeight((Activity) getContext())*3/5);
        try {
            String params = imagePath.substring(imagePath.indexOf("?") + 1);
            String[] split = params.split("&");
            double imageWidth = Double.valueOf(split[0].split("=")[1]);
            double imageHeight = Double.valueOf(split[1].split("=")[1]);
            height = width/imageWidth*imageHeight;
        }catch (Exception e){
        }
        if(!imagePath.startsWith("http")){
            imagePath = imagePath.substring(0,imagePath.indexOf("?"));
        }
        configure.override((int) width,(int) height);
        Glide.with(getContext()).load(imagePath).apply(configure).into(imageView);
        imageView.setAbsolutePath(imagePath);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.bottomMargin = 10;
        imageView.setLayoutParams(lp);
        allLayout.addView(imageLayout);

    }

    public Bitmap createScaleBitmap(Bitmap bitmap,int parentWidth){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int sampleSize = width > parentWidth ? width / parentWidth
                + 1 : 1;
        //放大為屏幕的1/2大小

        float scaleWidth = width/sampleSize;
        float scaleHeight = height/sampleSize;

        // 取得想要缩放的matrix參數
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的圖片
        Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,true);
        return newbm;
    }

    public void showEditData(List<EditData> datas) {
        if (datas == null || datas.size() == 0) {
            return;
        }
        for (int i = 0; i < datas.size(); i++) {
            EditData editData = datas.get(i);
            if (i == 0) {
                addTitleText(datas.get(i).inputStr);
            } else {
                if (!TextUtils.isEmpty(editData.inputStr)) {
                    addContentText(editData.inputStr);
                } else if (!TextUtils.isEmpty(editData.imagePath)) {
                    addImageView(editData.imagePath);
                }
            }
        }
    }

}
