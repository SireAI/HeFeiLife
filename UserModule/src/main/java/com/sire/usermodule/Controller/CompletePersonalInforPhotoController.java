package com.sire.usermodule.Controller;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sire.corelibrary.Controller.Segue;
import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.Executors.AppExecutors;
import com.sire.corelibrary.Networking.dataBound.DataResource;
import com.sire.corelibrary.Utils.APPUtils;
import com.sire.corelibrary.Utils.DialogUtils;
import com.sire.corelibrary.Utils.FileBuilder;
import com.sire.corelibrary.Utils.FileUtils;
import com.sire.corelibrary.Utils.ImageUtil;
import com.sire.corelibrary.Utils.PhotoPickUtils;
import com.sire.corelibrary.Utils.SnackbarUtils;
import com.sire.corelibrary.View.ProgressHUD;
import com.sire.corelibrary.View.ToastSuccess;
import com.sire.usermodule.R;
import com.sire.usermodule.View.ActionSheet;
import com.sire.usermodule.ViewModel.UserViewModel;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import java.io.File;
import java.util.Date;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

import static android.os.Environment.DIRECTORY_PICTURES;
import static com.sire.corelibrary.Controller.Segue.FOR_RESULT_REQUEST_CODE;
import static com.sire.corelibrary.Permission.PermissionHandler.REQUECT_CODE_BASIC_PERMISSIONS;
import static com.sire.corelibrary.Utils.PhotoPickUtils.CODE_CAMERA;
import static com.sire.corelibrary.Utils.PhotoPickUtils.CODE_PHOTO;
import static com.sire.corelibrary.Utils.PhotoPickUtils.getPath;
import static com.sire.usermodule.Constant.Constant.LOGIN_REQUEST_CODE;


/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/18
 * Author:Sire
 * Description:
 * ==================================================
 */

public class CompletePersonalInforPhotoController extends SireController implements TextWatcher, ActionSheet.ActionSheetListener, TextView.OnEditorActionListener {
    UserViewModel userViewModel;
    @Inject
    ViewModelProvider.Factory factory;
    @Inject
    AppExecutors appExecutors;
    private boolean pictureReady = false;
    private boolean nickNameReady = false;
    private Button btnNext;
    private String pictureName = "userpicture.png";
    private ImageView ivUserPicture;
    private CoordinatorLayout coordinatorLayout;
    private TextInputLayout tilNickname;
    private EditText tieNickName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller_complete_personalinfor_photo);
        setSupportActionBar(findViewById(R.id.toolbar));
        userViewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);

        tieNickName = findViewById(R.id.tie_nickname);
        coordinatorLayout = findViewById(R.id.cl_photo);
        tilNickname = findViewById(R.id.til_nickname);
        btnNext = findViewById(R.id.btn_next);
        ivUserPicture = findViewById(R.id.iv_user_picture);
        tieNickName.addTextChangedListener(this);
        btnNext.setEnabled(pictureReady && nickNameReady);
        tieNickName.setOnEditorActionListener(this);
    }

    public void onSkip(View view) {
        segueToCompletePersonalInforSexController();
    }

    @PermissionGrant(REQUECT_CODE_BASIC_PERMISSIONS)
    public void requestPermisssionSuccess() {
        setTheme(R.style.ActionSheet);
        ActionSheet.createBuilder(this, getSupportFragmentManager())
                .setOtherButtonTitles(getResources().getString(R.string.take_photo), getResources().getString(R.string.photo_pick))
                .setCancelableOnTouchOutside(true)
                .setListener(this)
                .show();
    }


    @PermissionDenied(REQUECT_CODE_BASIC_PERMISSIONS)
    public void requestPermisssionFailed() {
        DialogUtils.showDialog(this, getResources().getString(R.string.permission_attention), (dialogInterface, i) -> {
            dialogInterface.dismiss();
            Intent appDetailSettingIntent = APPUtils.getAppDetailSettingIntent(CompletePersonalInforPhotoController.this);
            startActivity(appDetailSettingIntent);
        });
    }


    public void onNext(View view) {
        String nickName = tieNickName.getText().toString();

        userViewModel.updateNickname(nickName, userViewModel.getUserId()).observe(this, dataResource -> {
            switch (dataResource.status) {
                case LOADING:
                    ProgressHUD.showDialog(CompletePersonalInforPhotoController.this);
                    break;
                case SUCCESS:
                    ProgressHUD.close();
                    segueToCompletePersonalInforSexController();
                    break;
                case ERROR:
                    ProgressHUD.close();
                    if (getResources().getString(R.string.name_already_exist).equals(dataResource.message)) {
                        tilNickname.setError(getResources().getString(R.string.name_already_exist));
                    } else {
                        SnackbarUtils.basicSnackBar(coordinatorLayout, dataResource.message, CompletePersonalInforPhotoController.this);
                    }
                    break;
                default:
                    break;
            }
        });
    }

    private void segueToCompletePersonalInforSexController() {
        Intent intent = new Intent(this, CompletePersonalInforSexController.class);
        intent.putExtra(FOR_RESULT_REQUEST_CODE, LOGIN_REQUEST_CODE);
        segueForResult(Segue.SegueType.PUSH, intent);
    }

    public void onPicture(View view) {
        requestNeedPermissions(new String[]{Manifest.permission.CAMERA});
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence nickName, int i, int i1, int i2) {
        tilNickname.setError("");
        nickNameReady = nickName.length() >= 2;
        btnNext.setEnabled(pictureReady && nickNameReady);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CODE_CAMERA) {
                File file = FileBuilder.create()
                        .withFileType(FileBuilder.FileType.DATA)
                        .withfileTypeDirectoryName(DIRECTORY_PICTURES)
                        .withFileName(pictureName)
                        .build(this);
                if (file.exists()) {
                    compressImageAndUpload(file,CODE_CAMERA);
                } else {
                    SnackbarUtils.basicSnackBar(coordinatorLayout, getResources().getString(R.string.file_not_exist), this);
                }
            } else if (requestCode == CODE_PHOTO) {
                Uri uri = data.getData();
                if (uri != null) {
                    File file = new File(getPath(CompletePersonalInforPhotoController.this, uri));
                    if (file.exists()) {
                        compressImageAndUpload(file,CODE_PHOTO);
                    } else {
                        SnackbarUtils.basicSnackBar(coordinatorLayout, getResources().getString(R.string.file_not_exist), this);
                    }
                }
            }
        }else if(requestCode == LOGIN_REQUEST_CODE && resultCode == LOGIN_REQUEST_CODE){
            setResult(LOGIN_REQUEST_CODE);
            finish();
        }

    }

    private void compressImageAndUpload(File file,int action) {
        Flowable.just(file).map(originalFile -> {
            byte[] bytes = ImageUtil.compressBitmapToBytes(originalFile.getAbsolutePath(),600,600,100, Bitmap.CompressFormat.WEBP);
            String webpName = originalFile.getName().substring(0, originalFile.getName().indexOf(".")) + ".webp";
            File webpFile = new File(originalFile.getParentFile(), webpName);
            FileUtils.bytesToFile(bytes,webpFile.getAbsolutePath());
            if(action == CODE_CAMERA){
                file.delete();
            }
            return webpFile;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(webpFile -> upoadHeadImage(webpFile));
    }

    private void setImageWithUri(File file) {
        Uri uri = Uri.fromFile(file);
        ivUserPicture.setImageURI(uri);
    }


    private void upoadHeadImage(File file) {
        String userId = userViewModel.getUserId();
        if (TextUtils.isEmpty(userId)) {
            SnackbarUtils.basicSnackBar(coordinatorLayout, getResources().getString(R.string.relogin), this);
            return;
        }
        userViewModel.uploadHeadImage(file, userId).observe(this, dataResource -> {
            switch (dataResource.status) {
                case LOADING:
                    ProgressHUD.showDialog(CompletePersonalInforPhotoController.this);
                    break;
                case SUCCESS:
                    updateUserAvatar(dataResource.data.getMessage(), file);
                    break;
                case ERROR:
                    ProgressHUD.close();
                    SnackbarUtils.basicSnackBar(coordinatorLayout, dataResource.message, CompletePersonalInforPhotoController.this);
                    break;
                default:
                    break;
            }
        });

    }

    private void updateUI(File file) {
        setImageWithUri(file);
        updateNextBtnState();
    }

    private void updateUserAvatar(String avartarUrl, File file) {
        userViewModel.updateUserAvatar(avartarUrl).observe(this, new Observer<DataResource>() {
            @Override
            public void onChanged(@Nullable DataResource dataResource) {
                switch (dataResource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        ProgressHUD.close();
                        updateUI(file);
                        ToastSuccess.showDialog(CompletePersonalInforPhotoController.this, getResources().getString(R.string.upload_success));
                        break;
                    case ERROR:
                        ProgressHUD.close();
                        SnackbarUtils.basicSnackBar(coordinatorLayout, dataResource.message, CompletePersonalInforPhotoController.this);
                        break;
                    default:
                        break;
                }
            }
        });

    }


    private void updateNextBtnState() {
        pictureReady = true;
        btnNext.setEnabled(pictureReady && nickNameReady);
    }

    @Override
    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
    }

    @Override
    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
        if (index == 0) {
            pictureName = System.currentTimeMillis() +".png";
            PhotoPickUtils.takePicture(this, CODE_CAMERA, pictureName);
        } else if (index == 1) {
            PhotoPickUtils.takePicture(this, CODE_PHOTO, "");
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == 6 || id == EditorInfo.IME_NULL && btnNext.isEnabled()) {
            onNext(textView);
            return true;
        }
        return false;
    }
}
