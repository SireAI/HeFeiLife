package com.sire.bbsmodule.Views.RichEditor;

import android.animation.LayoutTransition;
import android.animation.LayoutTransition.TransitionListener;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.sire.bbsmodule.Pojo.EditData;
import com.sire.bbsmodule.R;
import com.sire.bbsmodule.Utils.ImageUtil;
import com.sire.bbsmodule.Utils.ScreenUtils;
import com.sire.bbsmodule.Utils.StringUtils;
import com.sire.corelibrary.DI.Environment.GlideConfigure;
import com.sire.corelibrary.Executors.AppExecutors;
import com.sire.corelibrary.Utils.CommonUtils;
import com.sire.corelibrary.Utils.DialogUtils;
import com.sire.corelibrary.Utils.FileBuilder;
import com.sire.corelibrary.Utils.FileProvider7;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.DIRECTORY_PICTURES;

/**
 * 这是一个富文本编辑器，给外部提供insertImage接口，添加的图片跟当前光标所在位置有关
 *
 * @author xmuSistone
 */
@SuppressLint({"NewApi", "InflateParams"})
public class RichTextEditor extends ScrollView {
    private static final int EDIT_PADDING = 10; // edittext常规padding是10dp
    private static final int EDIT_FIRST_PADDING_TOP = 10; // 第一个EditText的paddingTop值

    private int viewTagIndex = 1; // 新生的view都会打一个tag，对每个view来说，这个tag是唯一的。
    private LinearLayout allLayout; // 这个是所有子view的容器，scrollView内部的唯一一个ViewGroup
    private LayoutInflater inflater;
    private OnKeyListener keyListener; // 所有EditText的软键盘监听器
    private OnClickListener btnListener; // 图片右上角红叉按钮监听器
    private OnFocusChangeListener focusListener; // 所有EditText的焦点监听listener
    private OnScrollEditEvent onScrollEditEvent; // 所有EditText的焦点监听listener
    private EditText lastFocusEdit; // 最近被聚焦的EditText
    private LayoutTransition mTransitioner; // 只在图片View添加或remove时，触发transition动画
    private int disappearingImageIndex = 0;
    private DataImageView currentEditImageView;

    public RichTextEditor(Context context) {
        this(context, null);
    }

    public RichTextEditor(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RichTextEditor(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflater = LayoutInflater.from(context);

        // 1. 初始化allLayout
        allLayout = new LinearLayout(context);
        allLayout.setOrientation(LinearLayout.VERTICAL);
        setupLayoutTransitions();
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        addView(allLayout, layoutParams);

        // 2. 初始化键盘退格监听
        // 主要用来处理点击回删按钮时，view的一些列合并操作
        keyListener = new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                    EditText edit = (EditText) v;
                    onBackspacePress(edit);
                }
                return false;
            }
        };

        // 3. 图片叉掉处理
        btnListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.image_close) {
                    RelativeLayout parentView = (RelativeLayout) v.getParent();
                    DialogUtils.showDialog(getContext(), "要删除这张照片么？", (dialog, which) -> {
                        dialog.dismiss();
                        onImageCloseClick(parentView);
                    });
                } else if (v.getId() == R.id.image_edit) {
                    if (onScrollEditEvent != null) {
                        RelativeLayout parentView = (RelativeLayout) v.getParent();
                        currentEditImageView = parentView.findViewById(R.id.edit_imageView);
                        onScrollEditEvent.onPhotoEdit(currentEditImageView.getAbsolutePath());
                    }
                }

            }
        };

        focusListener = new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    changeLastFocusEdit((EditText) v);
                }
            }
        };

        addTitleText();
//        addExtraInforView();

        addContentText("写内容~");

    }

    private void addExtraInforView() {
        LinearLayout.LayoutParams lineParam = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        lineParam.gravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
        TextView extraInfor = new TextView(getContext());
        extraInfor.setTextColor(getResources().getColor(R.color.color_title_caption));
        extraInfor.setTextSize(12);
        extraInfor.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        extraInfor.setPadding(0, 10, 30, 10);
        extraInfor.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.svg_gps), null, null, null);
        extraInfor.setText("合肥高新区");
        allLayout.addView(extraInfor, lineParam);
    }

    private void addContentText(String hint) {
        LinearLayout.LayoutParams firstEditParam = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        EditText firstEdit = createEditText(hint);
        allLayout.addView(firstEdit, firstEditParam);
        changeLastFocusEdit(firstEdit);
    }

    private void addTitleText() {
        LinearLayout.LayoutParams titleEditParam = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        EditText titleEdit = createEditText("请输入标题");
        titleEdit.setTextColor(getResources().getColor(R.color.color_title_black));
        titleEdit.setTextSize(18);
        titleEdit.setSingleLine(true);
        allLayout.addView(titleEdit, titleEditParam);
    }

    public void setOnScrollEditEvent(OnScrollEditEvent onScrollEditEvent) {
        this.onScrollEditEvent = onScrollEditEvent;
    }

    /**
     * 处理软键盘backSpace回退事件
     *
     * @param editTxt 光标所在的文本输入框
     */
    private void onBackspacePress(EditText editTxt) {
        int startSelection = editTxt.getSelectionStart();
        // 只有在光标已经顶到文本输入框的最前方，在判定是否删除之前的图片，或两个View合并
        if (startSelection == 0) {
            int editIndex = allLayout.indexOfChild(editTxt);
            View preView = allLayout.getChildAt(editIndex - 1); // 如果editIndex-1<0,
            // 则返回的是null
            if (null != preView) {
                if (preView instanceof RelativeLayout) {
                    // 光标EditText的上一个view对应的是图片
                    onImageCloseClick(preView);
                } else if (preView instanceof EditText && editIndex!=1) {
                    // 光标EditText的上一个view对应的还是文本框EditText
                    String str1 = editTxt.getText().toString();
                    EditText preEdit = (EditText) preView;
                    String str2 = preEdit.getText().toString();

                    // 合并文本view时，不需要transition动画
                    allLayout.setLayoutTransition(null);
                    allLayout.removeView(editTxt);
                    allLayout.setLayoutTransition(mTransitioner); // 恢复transition动画

                    // 文本合并
                    preEdit.setText(str2 + str1);
                    preEdit.requestFocus();
                    preEdit.setSelection(str2.length(), str2.length());
                    changeLastFocusEdit(preEdit);
                }
            }
        }
    }

    /**
     * 处理图片叉掉的点击事件
     *
     * @param view 整个image对应的relativeLayout view
     * @type 删除类型 0代表backspace删除 1代表按红叉按钮删除
     */
    private void onImageCloseClick(View view) {
        if (!mTransitioner.isRunning()) {
            allLayout.removeView(view);
        }
    }

    /**
     * 生成文本输入框
     */
    private EditText createEditText(String hint) {
        BackListenEditText editText = (BackListenEditText) inflater.inflate(R.layout.view_commponent_edit_item,
                null);
        editText.setOnKeyListener(keyListener);
        editText.setBackListener((BackListenEditText.BackListener) inflater.getContext());
        editText.setTag(viewTagIndex++);
        editText.setHint(hint);
        editText.setOnFocusChangeListener(focusListener);
        return editText;
    }

    /**
     * 生成图片View
     */
    private RelativeLayout createImageLayout() {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(
                R.layout.view_commonpent_edit_imageview, null);
        layout.setTag(viewTagIndex++);
        View closeView = layout.findViewById(R.id.image_close);
        View editView = layout.findViewById(R.id.image_edit);
        closeView.setTag(layout.getTag());
        closeView.setOnClickListener(btnListener);
        editView.setOnClickListener(btnListener);
        return layout;
    }

    /**
     * 根据绝对路径添加view,添加水印重新生成图片
     *
     * @param imagePath
     */
    public void insertImage(String imagePath) {
        File editFile = new File(imagePath);
        File destinatinFile = FileBuilder.create()
                .withFileType(FileBuilder.FileType.DATA)
                .withfileTypeDirectoryName(DIRECTORY_PICTURES)
                .withFileName("watermark"+ StringUtils.getPhotoFileName())
                .build(getContext());
        Bitmap watermarkBitmap = createWaterMark(imagePath,destinatinFile);
        insertImage(watermarkBitmap, destinatinFile.getAbsolutePath());
    }

    public void replaceImage(String imagePath) {
        if (currentEditImageView != null) {
            Bitmap source = getScaledBitmap(imagePath, getWidth());
            setImageBitmap(source, imagePath, currentEditImageView);
            currentEditImageView = null;
        }
    }

    @Nullable
    private Bitmap createWaterMark(String imagePath, File destinatinFile) {
        Bitmap source = getScaledBitmap(imagePath, getWidth());
        Bitmap waterBitmap = ImageUtil.getBitmapFromDrawable(getContext(), R.drawable.logo_white);
        Bitmap watermarkBitmap = ImageUtil.createWaterMaskLeftBottom(getContext(), source, waterBitmap, 5, 5);
        Bitmap watermarkBitmapText = ImageUtil.drawTextToLeftBottom(getContext(), watermarkBitmap, getResources().getString(R.string.app_name), 14, getResources().getColor(R.color.main_bg_1_text), 35, 10);
        new AppExecutors().diskIO().execute(() -> ImageUtil.saveBitmaplocal(destinatinFile.getAbsolutePath(), watermarkBitmapText));
        return watermarkBitmapText;
    }

    /**
     * 插入一张图片
     */
    private void insertImage(Bitmap bitmap, String imagePath) {
        String lastEditStr = lastFocusEdit.getText().toString();
        int cursorIndex = lastFocusEdit.getSelectionStart();
        String editStr1 = lastEditStr.substring(0, cursorIndex).trim();
        int lastEditIndex = allLayout.indexOfChild(lastFocusEdit);

        if (lastEditIndex == 0) {
            lastEditIndex = 1;
        }
        if (lastEditStr.length() == 0 || editStr1.length() == 0) {
            // 如果EditText为空，或者光标已经顶在了editText的最前面，则直接插入图片，并且EditText下移即可
            addImageViewAtIndex(lastEditIndex, bitmap, imagePath,true);
        } else {
            // 如果EditText非空且光标不在最顶端，则需要添加新的imageView和EditText
            lastFocusEdit.setText(editStr1);
            String editStr2 = lastEditStr.substring(cursorIndex).trim();
            EditText addedText = null;
            if (allLayout.getChildCount() - 1 == lastEditIndex
                    || editStr2.length() > 0) {
                addedText = addEditTextAtIndex(lastEditIndex + 1, editStr2);
            }

            addImageViewAtIndex(lastEditIndex + 1, bitmap, imagePath,true);
            if (addedText == null) {
                lastFocusEdit.requestFocus();
                lastFocusEdit.setSelection(editStr1.length(), editStr1.length());
            } else {
                addedText.requestFocus();
                addedText.setSelection(editStr2.length(), editStr2.length());
            }
        }
        hideKeyBoard();
    }

    public EditText getLastFocusEdit() {
        return lastFocusEdit;
    }

    private void changeLastFocusEdit(EditText newEdit) {
        this.lastFocusEdit = newEdit;
        if (onScrollEditEvent != null) {
            onScrollEditEvent.onEditChange(lastFocusEdit);
        }
    }

    /**
     * 隐藏小键盘
     */
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(lastFocusEdit.getWindowToken(), 0);
    }

    /**
     * 在特定位置插入EditText
     *
     * @param index   位置
     * @param editStr EditText显示的文字
     */
    private EditText addEditTextAtIndex(final int index, String editStr) {
        EditText editText2 = createEditText("");
        editText2.setText(editStr);

        // 请注意此处，EditText添加、或删除不触动Transition动画
        allLayout.setLayoutTransition(null);
        allLayout.addView(editText2, index);
        allLayout.setLayoutTransition(mTransitioner); // remove之后恢复transition动画
        return editText2;
    }

    /**
     * 在特定位置添加ImageView
     */
    private void addImageViewAtIndex( int index, Bitmap bmp,
                                     String imagePath,boolean addDelay) {
        final RelativeLayout imageLayout = createImageLayout();
        DataImageView imageView = (DataImageView) imageLayout
                .findViewById(R.id.edit_imageView);
        setImageBitmap(bmp, imagePath, imageView);
        // onActivityResult无法触发动画，此处post处理
//        allLayout.addView(imageLayout, index);
        int imageindex = index;
        if(addDelay){
            allLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    allLayout.addView(imageLayout, imageindex);
                }
            }, 200);
        }else {
            allLayout.addView(imageLayout, imageindex);
        }

    }

    private void setImageBitmap(Bitmap bmp, String imagePath, DataImageView imageView) {
        imageView.setImageBitmap(bmp);
        imageView.setAbsolutePath(imagePath);
        imageView.setBitmapWidth(bmp.getWidth());
        imageView.setBitmapHeight(bmp.getHeight());
        // 调整imageView的高度
        double width = Double.valueOf(ScreenUtils.getScreenWidth((Activity) getContext()));
        width = width - 2* CommonUtils.dip2px(getContext(),6);
         double   imageHeight = width * Double.valueOf(bmp.getHeight()) / Double.valueOf(bmp.getWidth());

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, (int) imageHeight);
        imageView.setLayoutParams(lp);
    }

    /**
     * 根据view的宽度，动态缩放bitmap尺寸
     *
     * @param width view的宽度
     */
    private Bitmap getScaledBitmap(String filePath, int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        int sampleSize = options.outWidth > width ? options.outWidth / width
                + 1 : 1;
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 初始化transition动画
     */
    private void setupLayoutTransitions() {
        mTransitioner = new LayoutTransition();
        allLayout.setLayoutTransition(mTransitioner);
        mTransitioner.addTransitionListener(new TransitionListener() {

            @Override
            public void startTransition(LayoutTransition transition,
                                        ViewGroup container, View view, int transitionType) {

            }

            @Override
            public void endTransition(LayoutTransition transition,
                                      ViewGroup container, View view, int transitionType) {
                if (!transition.isRunning()
                        && transitionType == LayoutTransition.CHANGE_DISAPPEARING) {
                    // transition动画结束，合并EditText
                    // mergeEditText();
                }
            }
        });
        mTransitioner.setDuration(300);
    }

    /**
     * 图片删除的时候，如果上下方都是EditText，则合并处理
     */
    private void mergeEditText() {
        View preView = allLayout.getChildAt(disappearingImageIndex - 1);
        View nextView = allLayout.getChildAt(disappearingImageIndex);
        if (preView != null && preView instanceof EditText && null != nextView
                && nextView instanceof EditText) {
            Log.d("LeiTest", "合并EditText");
            EditText preEdit = (EditText) preView;
            EditText nextEdit = (EditText) nextView;
            String str1 = preEdit.getText().toString();
            String str2 = nextEdit.getText().toString();
            String mergeText = "";
            if (str2.length() > 0) {
                mergeText = str1 + "\n" + str2;
            } else {
                mergeText = str1;
            }

            allLayout.setLayoutTransition(null);
            allLayout.removeView(nextEdit);
            preEdit.setText(mergeText);
            preEdit.requestFocus();
            preEdit.setSelection(str1.length(), str1.length());
            allLayout.setLayoutTransition(mTransitioner);
        }
    }

    /**
     * 聚合编辑数据
     * @param localData  如果是本地数据，则图片
     * @return
     */
    public List<EditData> buildEditData(boolean localData,String imageServerUrl) {
        List<EditData> dataList = new ArrayList<EditData>();
        int num = allLayout.getChildCount();
        for (int index = 0; index < num; index++) {
            View itemView = allLayout.getChildAt(index);
            EditData itemData = new EditData();
            if (itemView instanceof EditText ) {
                EditText item = (EditText) itemView;
                if(TextUtils.isEmpty(item.getText().toString())){
                    continue;
                }
                itemData.inputStr = item.getText().toString();
            } else if (itemView instanceof RelativeLayout) {
                DataImageView item = itemView
                        .findViewById(R.id.edit_imageView);
                if(TextUtils.isEmpty(item.getAbsolutePath())){
                    continue;
                }
                if(localData){
                    itemData.imagePath = item.getAbsolutePath();
                }else {
                    itemData.imagePath = imageServerUrl+"?"+"width="+item.getBitmapWidth()+"&height="+item.getBitmapHeight();
                }
            }
            dataList.add(itemData);
        }

        return dataList;
    }

    public void fillDraft(List<EditData> draft) {
        if(draft == null || draft.size() == 0){
            return;
        }
        for (int i = 0; i < draft.size(); i++) {
            EditData editData = draft.get(i);
            if(i==0){
                EditText titleText = (EditText) allLayout.getChildAt(0);
                titleText.setText(editData.inputStr);
            }else if(i==1){
                EditText firstText = (EditText) allLayout.getChildAt(1);
                firstText.setText(editData.inputStr);
            }else {
                if (!TextUtils.isEmpty(editData.inputStr)) {
                    lastFocusEdit =  addEditTextAtIndex(i,editData.inputStr);
                } else if (!TextUtils.isEmpty(editData.imagePath)) {
                    Bitmap bitmap = BitmapFactory.decodeFile(editData.imagePath);
                    if(bitmap!=null) {
                        addImageViewAtIndex(i,bitmap,editData.imagePath,false);
                    }
                }
            }
        }
        if(!TextUtils.isEmpty(draft.get(draft.size()-1).imagePath)){
            addContentText("");
        }
        lastFocusEdit.requestFocus();
    }



    public interface OnScrollEditEvent {
        void onEditChange(EditText currentEditText);

        void onPhotoEdit(String imageUrl);
    }


}
