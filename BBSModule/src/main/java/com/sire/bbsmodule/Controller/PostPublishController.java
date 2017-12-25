package com.sire.bbsmodule.Controller;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.sire.bbsmodule.Pojo.EditData;
import com.sire.bbsmodule.R;
import com.sire.bbsmodule.Utils.ScreenUtils;
import com.sire.bbsmodule.Utils.StringUtils;
import com.sire.bbsmodule.ViewModel.BBSViewModel;
import com.sire.bbsmodule.Views.EmojiView.EmojiEditText;
import com.sire.bbsmodule.Views.EmojiView.EmojiPopup;
import com.sire.bbsmodule.Views.RichEditor.BackListenEditText;
import com.sire.bbsmodule.Views.RichEditor.RichTextEditor;
import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.Executors.AppExecutors;
import com.sire.corelibrary.Networking.Response.JsonResponse;
import com.sire.corelibrary.Networking.dataBound.DataResource;
import com.sire.corelibrary.Utils.APPUtils;
import com.sire.corelibrary.Utils.DialogUtils;
import com.sire.corelibrary.Utils.FileBuilder;
import com.sire.corelibrary.Utils.FileUtils;
import com.sire.corelibrary.Utils.PhotoPickUtils;
import com.sire.corelibrary.Utils.SPUtils;
import com.sire.corelibrary.Utils.ToastUtils;
import com.sire.corelibrary.View.ProgressHUD;
import com.sire.corelibrary.View.ToastSuccess;
import com.yalantis.ucrop.UCrop;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static android.os.Environment.DIRECTORY_PICTURES;
import static com.sire.bbsmodule.ViewModel.BBSViewModel.DRAFT;
import static com.sire.corelibrary.Permission.PermissionHandler.REQUECT_CODE_BASIC_PERMISSIONS;
import static com.sire.corelibrary.Utils.PhotoPickUtils.CODE_CAMERA;
import static com.sire.corelibrary.Utils.PhotoPickUtils.CODE_PHOTO;
import static com.sire.corelibrary.Utils.PhotoPickUtils.getPath;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/12/14
 * Author:Sire
 * Description:
 * ==================================================
 */

public class PostPublishController extends SireController implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, BackListenEditText.BackListener, View.OnLayoutChangeListener, RichTextEditor.OnScrollEditEvent {

    @Inject
    ViewModelProvider.Factory factory;
    private LinearLayout ll;
    private RichTextEditor editor;
    private String currentPictureName = "current";
    private EmojiPopup emojiPopup;
    private ImageButton ibKeyboard;
    private boolean isPanShow;
    private BBSViewModel bbsViewModel;
    @Inject
    AppExecutors appExecutors;
    private Disposable disposable;
    private boolean postSubmitFinsh = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bbsViewModel = ViewModelProviders.of(this, factory).get(BBSViewModel.class);

        setContentView(R.layout.controller_post_pubilish);
        initView();
    }

    private void initView() {
        ll = findViewById(R.id.ll);
        ll.addOnLayoutChangeListener(this);
        editor = findViewById(R.id.richEditor);
        editor.setOnScrollEditEvent(this);
        ibKeyboard = findViewById(R.id.ib_keyboard);
        ibKeyboard.setOnClickListener(this);
        findViewById(R.id.ib_photo).setOnClickListener(this);
        findViewById(R.id.ib_camera).setOnClickListener(this);
        findViewById(R.id.ib_topic).setOnClickListener(this);
        setActionBarEnabled(findViewById(R.id.toolbar));
        setUpEmojiPopup();
        checkDraft();
    }

    private void checkDraft() {
        List<EditData> draft = bbsViewModel.getDraft(this);
        editor.fillDraft(draft);
    }

    private void setUpEmojiPopup() {
        emojiPopup = EmojiPopup.Builder.fromRootView(findViewById(R.id.ll_container))
                .build((EmojiEditText) editor.getLastFocusEdit());
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CODE_CAMERA) {
                File file = FileBuilder.create()
                        .withFileType(FileBuilder.FileType.DATA)
                        .withfileTypeDirectoryName(DIRECTORY_PICTURES)
                        .withFileName(currentPictureName)
                        .build(this);
                showImage(file);
            } else if (requestCode == CODE_PHOTO) {
                Uri uri = data.getData();
                if (uri != null) {
                    File file = new File(getPath(PostPublishController.this, uri));
                    currentPictureName = file.getName();
                    showImage(file);
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                Uri resultUri = UCrop.getOutput(data);
                if (resultUri != null) {
                    File file = new File(getPath(PostPublishController.this, resultUri));
                    replaceImage(file);
                }
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Throwable cropError = UCrop.getError(data);
            Timber.e(cropError);
        }

    }

    public void onPublish(View view) {
        List<EditData> postData = editor.buildEditData(false,"127.0.0.1");
        if (checkPostData(postData)) {
            bbsViewModel.publishPost(postData).observe(this, new Observer<DataResource<JsonResponse>>() {
                @Override
                public void onChanged(@Nullable DataResource<JsonResponse> jsonResponseDataResource) {
                    switch (jsonResponseDataResource.status) {
                        case LOADING:
                            ProgressHUD.showDialog(PostPublishController.this);
                            break;
                        case SUCCESS:
                            publishResult();
                            break;
                        case ERROR:
                            ProgressHUD.close();
                            ToastUtils.showToast(PostPublishController.this,jsonResponseDataResource.message);
                            break;
                        default:
                            break;
                    }
                }
            });
        }
    }

    private void publishResult() {
        File cachePictureDirectory = FileBuilder.create()
                .withFileType(FileBuilder.FileType.DATA)
                .withfileTypeDirectoryName(DIRECTORY_PICTURES)
                .buildDir(PostPublishController.this);
        disposable = Flowable.just(cachePictureDirectory)
                .doOnNext(file -> {
//                    FileUtils.deleteFile(file, true);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(file -> {
                    ProgressHUD.close();
                    ToastSuccess.showDialog(PostPublishController.this, () -> {
                        postSubmitFinsh = true;
                        onBackPressed();
                    });
                }, throwable -> {
                }, () -> ProgressHUD.close());
    }


    private boolean checkPostData(List<EditData> editData) {
        String message = "";
        if (TextUtils.isEmpty(editData.get(0).inputStr)) {
            message = "请输入标题";
        }
        if (TextUtils.isEmpty(editData.get(1).inputStr) || TextUtils.isEmpty(editData.get(1).imagePath)) {
            message = "请写入内容";
        }
        if (TextUtils.isEmpty(message)) {
            ToastUtils.showToast(this, message);
            return false;
        }
        return true;
    }

    private void showImage(File file) {
        if (file.exists()) {
            editor.insertImage(file.getAbsolutePath());
        } else {
            ToastUtils.showToast(PostPublishController.this, "文件不存在");
        }
    }

    private void replaceImage(File file) {
        if (file.exists()) {
            editor.replaceImage(file.getAbsolutePath());
        } else {
            ToastUtils.showToast(PostPublishController.this, "文件不存在");
        }
    }



    @PermissionGrant(REQUECT_CODE_BASIC_PERMISSIONS)
    public void requestPermisssionSuccess() {
        PhotoPickUtils.takePicture(this, CODE_CAMERA, currentPictureName = StringUtils.getPhotoFileName());
    }


    @PermissionDenied(REQUECT_CODE_BASIC_PERMISSIONS)
    public void requestPermisssionFailed() {
        DialogUtils.showDialog(this, getResources().getString(R.string.permission_attention), (dialogInterface, i) -> {
            Intent appDetailSettingIntent = APPUtils.getAppDetailSettingIntent(PostPublishController.this);
            startActivity(appDetailSettingIntent);
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ib_camera) {
            requestNeedPermissions(new String[]{Manifest.permission.CAMERA});
        } else if (view.getId() == R.id.ib_photo) {
            PhotoPickUtils.takePicture(this, CODE_PHOTO, "");
        } else if (view.getId() == R.id.ib_keyboard) {
            if (isPanShow) {
                emojiPopup.toggle();
                setKeyboardImage(!emojiPopup.isShowing());
            } else {
                ScreenUtils.showKeyBoard(editor.getLastFocusEdit());
            }
        } else if (view.getId() == R.id.ib_topic) {

        }
    }

    @Override
    public boolean onBackSoftInputDismiss() {
        setKeyboardImage(false);
        return false;
    }

    @Override
    public void onBackPressed() {
        if (emojiPopup != null && emojiPopup.isShowing()) {
            emojiPopup.dismiss();
        } else {
            if(postSubmitFinsh){
                bbsViewModel.clearDraft(this);
                super.onBackPressed();
            }else {
                List<EditData> editDatas = editor.buildEditData(true, "");
                if(editDatas.size()==0){
                    super.onBackPressed();
                }else {
                    DialogUtils.showDialog(this,"保存","不保存", "是否保存草稿?", (dialog, which) -> {
                        dialog.dismiss();
                        bbsViewModel.clearDraft(this);
                        PostPublishController.super.onBackPressed();
                    }, (dialog, which) -> {
                        dialog.dismiss();
                        bbsViewModel.saveDraft(PostPublishController.this, editDatas);
                        PostPublishController.super.onBackPressed();
                    });
                }

            }
        }
    }


    private void setPanShow(boolean show) {
        isPanShow = show;
        setKeyboardImage(show);
    }

    private void setKeyboardImage(boolean showEmoji) {
        ibKeyboard.setImageResource(showEmoji ? R.drawable.svg_emoji : R.drawable.svg_keyboard);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //软键盘隐藏
        if (top - oldTop > 700) {
            setPanShow(false);
        }
        //软键盘弹出
        if (oldTop - top > 700) {
            setPanShow(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ll != null) {
            ll.removeOnLayoutChangeListener(this);
        }
        if(disposable!=null&&!disposable.isDisposed()){
            disposable.dispose();
            disposable = null;
        }
    }

    @Override
    public void onEditChange(EditText currentEditText) {
        emojiPopup.setEmojiEditText((EmojiEditText) currentEditText);
    }

    @Override
    public void onPhotoEdit(String imageUrl) {
        photoEditPrepare(imageUrl);
    }

    /**
     * 图片编辑准备
     *
     * @param imageUrl
     */
    private void photoEditPrepare(String imageUrl) {
        File editFile = new File(imageUrl);
        File file = FileBuilder.create()
                .withFileType(FileBuilder.FileType.DATA)
                .withfileTypeDirectoryName(DIRECTORY_PICTURES)
                .withFileName("edit"+editFile.getName())
                .build(this);
        Uri destinationUrl = Uri.fromFile(file);
        Uri sourceUrl = Uri.fromFile(editFile);
        UCrop.Options options = new UCrop.Options();
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        options.setFreeStyleCropEnabled(true);
        options.setActiveWidgetColor(getResources().getColor(R.color.colorPrimary));
        options.setCompressionFormat(Bitmap.CompressFormat.WEBP);
        UCrop.of(sourceUrl, destinationUrl)
                .useSourceImageAspectRatio()
                .withOptions(options)
                .start(this);

    }



}
