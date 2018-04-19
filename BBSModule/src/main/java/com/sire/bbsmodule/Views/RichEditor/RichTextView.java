package com.sire.bbsmodule.Views.RichEditor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.sire.bbsmodule.Pojo.EditData;
import com.sire.bbsmodule.R;
import com.sire.corelibrary.Utils.ScreenUtils;
import com.sire.bbsmodule.Utils.StringUtils;
import com.sire.corelibrary.View.EmojiView.EmojiTextView;
import com.sire.corelibrary.DI.Environment.GlideConfigure;
import com.sire.corelibrary.Utils.CommonUtils;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * 这是一个富文本编辑器，给外部提供insertImage接口，添加的图片跟当前光标所在位置有关
 *
 * @author sire
 */
@SuppressLint({"NewApi", "InflateParams"})
public class RichTextView extends LinearLayout {

    private int viewTagIndex = 1; // 新生的view都会打一个tag，对每个view来说，这个tag是唯一的。
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
        setOrientation(VERTICAL);
    }



    private void addContentText(String lineData) {
        LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        TextView firstEdit = createTextView();
        firstEdit.setText(lineData);
        addView(firstEdit, firstEditParam);
    }

    private void addTitleText(String title) {
        LinearLayout.LayoutParams titleEditParam = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        TextView titleEdit = createTextView();
        titleEdit.setTextColor(getResources().getColor(R.color.color_title_black));
        titleEdit.setTextSize(20);
        titleEdit.setText(title);
        addView(titleEdit, titleEditParam);
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
        RequestOptions configure = GlideConfigure.getConfigure(DiskCacheStrategy.AUTOMATIC);
        //图片适配屏幕尺寸，根据地址上的匡高和控件的宽高进行缩放
        double width = Double.valueOf(ScreenUtils.getScreenWidth((Activity) getContext()));
        width = width - 2*CommonUtils.dip2px(getContext(),getResources().getDimension(R.dimen.post_padding));
        double height = Double.valueOf(ScreenUtils.getScreenHeight((Activity) getContext())*3/5);
        try {
            Map<String, String> params = StringUtils.cutUrlParams2Map(imagePath);
            double imageWidth = Double.valueOf(params.get("width"));
            double imageHeight = Double.valueOf(params.get("height"));
            height = imageHeight;
            width = imageWidth;
        }catch (Exception e){
            Timber.e(e);
        }
        configure.override((int) (width+0.5),(int) (height+0.5));
        Glide.with(getContext()).load(imagePath).apply(configure).into(imageView);
        imageView.setAbsolutePath(imagePath);
        addView(imageLayout);

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
    public void onDestroy() {
            for (int i = 0; i < getChildCount(); i++) {
                if(getChildAt(i) instanceof BackListenEditText){
                    ((BackListenEditText) getChildAt(i)).onDestroy();
                }
            }

    }

}
