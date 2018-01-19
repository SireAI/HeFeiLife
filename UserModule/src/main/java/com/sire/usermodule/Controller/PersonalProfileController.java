package com.sire.usermodule.Controller;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.luck.picture.lib.tools.ScreenUtils;
import com.sire.corelibrary.Bug.CleanLeakUtils;
import com.sire.corelibrary.Controller.Segue;
import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.DI.Environment.GlideConfigure;
import com.sire.corelibrary.Networking.dataBound.DataResource;
import com.sire.corelibrary.Utils.APPUtils;
import com.sire.corelibrary.Utils.DialogUtils;
import com.sire.corelibrary.Utils.FileUtils;
import com.sire.corelibrary.Utils.ImageUtil;
import com.sire.corelibrary.Utils.PhotoPickUtils;
import com.sire.corelibrary.Utils.ToastUtils;
import com.sire.corelibrary.View.PopOperation;
import com.sire.corelibrary.View.ProgressHUD;
import com.sire.corelibrary.View.ToastSuccess;
import com.sire.usermodule.R;
import com.sire.usermodule.View.datetimepicker.DatePickerFragmentDialog;
import com.sire.usermodule.View.datetimepicker.DateTimeBuilder;
import com.sire.usermodule.View.datetimepicker.callback.DatePickerCallback;
import com.sire.usermodule.ViewModel.UserViewModel;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.sire.corelibrary.Controller.Segue.FOR_RESULT_REQUEST_CODE;
import static com.sire.usermodule.Constant.Constant.COLLEGE;
import static com.sire.usermodule.Constant.Constant.INSTRUCTION;
import static com.sire.usermodule.Constant.Constant.NEW_NICK_NAME;
import static com.sire.usermodule.Constant.Constant.OLD_COLLEGE;
import static com.sire.usermodule.Constant.Constant.OLD_NICK_NAME;
import static com.sire.usermodule.Constant.Constant.OLD_SIGN;

/**
 * ==================================================
 * All Right Reserved
 * Date:2018/01/11
 * Author:Sire
 * Description:
 * ==================================================
 */

public class PersonalProfileController extends SireController implements View.OnClickListener, DatePickerCallback {

    public static final int NICK_NAME_UPDATE = 106;
    public static final int COLLEGE_CODE = 107;
    public static final String YYYY_年_MM_月_DD_日 = "yyyy年MM月dd日";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final int SIGN_CODE = 108;

    @Inject
    UserViewModel userViewModel;
    private ImageView ivUserPicture;
    private ImageView ivhomeImg;
    private ImageView currentImage;
    /**
     * 标记是否更新过用户信息
     */
    private boolean isUserInforUpdated = false;
    private TextView tvNickName;
    private TextView tvSex;
    private TextView tvBirthday;
    private TextView tvUniversity;
    private TextView tvSign;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller_personal_profile);
        findViewById(R.id.rl_head_image).setOnClickListener(this);
        findViewById(R.id.rl_home_image).setOnClickListener(this);
        findViewById(R.id.rl_nick_name).setOnClickListener(this);
        findViewById(R.id.rl_sex).setOnClickListener(this);
        findViewById(R.id.rl_birthday).setOnClickListener(this);
        findViewById(R.id.rl_college).setOnClickListener(this);
        findViewById(R.id.rl_sign).setOnClickListener(this);
        tvNickName = findViewById(R.id.tv_nick_name);
        tvNickName.setText(userViewModel.getUserName());
        tvSex = findViewById(R.id.tv_sex);
        String sex = userViewModel.getSex();
        tvSex.setText(TextUtils.isEmpty(sex) ? "未填写" : sex);
        tvUniversity = findViewById(R.id.tv_university);
        String college = userViewModel.getCollege();
        tvUniversity.setText(TextUtils.isEmpty(college) ? "未填写" : college);
        ivUserPicture = findViewById(R.id.iv_user_picture);
        ivhomeImg = findViewById(R.id.iv_home_img);
        tvBirthday = findViewById(R.id.tv_birthday);
        String birthday = userViewModel.getBirthday();
        birthday = formatDate(birthday);
        tvBirthday.setText(TextUtils.isEmpty(birthday) ? "未设置" : birthday);
        String instruction = userViewModel.getInstruction();
        tvSign = findViewById(R.id.tv_sign);
        tvSign.setText(TextUtils.isEmpty(instruction) ? "还没有签名" : instruction);
        Glide.with(this).load(userViewModel.getUserImage()).apply(GlideConfigure.getConfigure(DiskCacheStrategy.AUTOMATIC)).into(ivUserPicture);
        Glide.with(this).load(userViewModel.getHomePageImage()).apply(GlideConfigure.getConfigure(DiskCacheStrategy.AUTOMATIC)).into(ivhomeImg);
        setActionBarEnabled(findViewById(R.id.toolbar));
    }

    private String formatDate(String birthday) {
        if (!TextUtils.isEmpty(birthday)) {

            SimpleDateFormat dateFormatParse = new SimpleDateFormat(YYYY_MM_DD);
            SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_年_MM_月_DD_日);
            try {
                birthday = dateFormat.format(dateFormatParse.parse(birthday));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return birthday;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.rl_home_image || v.getId() == R.id.rl_head_image) {
            requestPermission(v.getId());
        } else if (v.getId() == R.id.rl_nick_name) {
            segueToNickNameUpdateController();
        } else if (v.getId() == R.id.rl_sex) {
            PopOperation popOperation = new PopOperation();
            popOperation.showDialog(this, (index, item) -> {
                popOperation.dismiss();
                updateSex(item);
            }, "男", "女");
        } else if (v.getId() == R.id.rl_birthday) {
            DateTimeBuilder dateTimeBuilder = DateTimeBuilder.newInstance()
                    .withTheme(R.style.PickersTheme);
            String birthday = tvBirthday.getText().toString();
            if (!TextUtils.isEmpty(birthday)) {
                SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_年_MM_月_DD_日);
                try {
                    Date date = dateFormat.parse(birthday);
                    dateTimeBuilder.withSelectedDate(date.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            DatePickerFragmentDialog.newInstance(dateTimeBuilder).show(getSupportFragmentManager(), "DatePickerFragmentDialog");

        } else if (v.getId() == R.id.rl_college) {
            segueToCollegeUpdateController();
        } else if (v.getId() == R.id.rl_sign) {
            segueToPersonalSignController();
        }
    }

    private void segueToPersonalSignController() {
        Intent intent = new Intent(this, PersonalSignController.class);
        intent.putExtra(FOR_RESULT_REQUEST_CODE, SIGN_CODE);
        String sign = tvSign.getText().toString();

        if (!"还没有签名".equals(sign)) {
            intent.putExtra(OLD_SIGN, sign);
        }
        segueForResult(Segue.SegueType.MODAL, intent);
    }

    private void segueToCollegeUpdateController() {
        Intent intent = new Intent(this, CollegeUpdateController.class);
        intent.putExtra(FOR_RESULT_REQUEST_CODE, COLLEGE_CODE);
        String college = tvUniversity.getText().toString();

        if (!"未填写".equals(college)) {
            intent.putExtra(OLD_COLLEGE, college);
        }
        segueForResult(Segue.SegueType.MODAL, intent);
    }


    private void updateSex(String sex) {
        userViewModel.updateSex(sex, userViewModel.getUserId()).observe(this, dataResource -> {
            switch (dataResource.status) {
                case LOADING:
                    ProgressHUD.showDialog(PersonalProfileController.this);
                    break;
                case SUCCESS:
                    ProgressHUD.close();
                    ToastSuccess.showDialog(this, "成功");
                    tvSex.setText(sex);
                    isUserInforUpdated = true;
                    break;
                case ERROR:
                    ProgressHUD.close();
                    ToastUtils.showToast(this, dataResource.message);
                    break;
                default:
                    break;
            }
        });
    }

    private void segueToNickNameUpdateController() {
        Intent intent = new Intent(this, NickNameUpdateController.class);
        intent.putExtra(FOR_RESULT_REQUEST_CODE, NICK_NAME_UPDATE);
        String nickName = tvNickName.getText().toString();
        if (!"未填写".equals(nickName)) {
            intent.putExtra(OLD_NICK_NAME, nickName);
        }
        segueForResult(Segue.SegueType.MODAL, intent);
    }

    public void requestPermission(int id) {
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    PictureFileUtils.deleteCacheDirFile(PersonalProfileController.this);
                    handleClick(id);
                } else {
                    DialogUtils.showDialog(PersonalProfileController.this, getResources().getString(R.string.permission_attention), (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        Intent appDetailSettingIntent = APPUtils.getAppDetailSettingIntent(PersonalProfileController.this);
                        startActivity(appDetailSettingIntent);
                    });
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    private void handleClick(int id) {
        //删除压缩目录图片
        PictureFileUtils.deleteCacheDirFile(PersonalProfileController.this);
        if (id == R.id.rl_head_image) {
            currentImage = ivUserPicture;
        } else if (id == R.id.rl_home_image) {
            currentImage = ivhomeImg;
        }
        PhotoPickUtils.takePictureFromThirdParty(this, false, false, 1, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    //获取剪裁路径
                    String cutPath = selectList.get(0).getCutPath();
                    int screenWidth = ScreenUtils.getScreenWidth(PersonalProfileController.this);
                    compressImageAndUpload(new File(cutPath), screenWidth);
                    break;
            }
        } else if (resultCode == FOR_CONTROLLER_BACK) {
            if (requestCode == NICK_NAME_UPDATE) {
                updateUserNickName(data.getStringExtra(NEW_NICK_NAME));
            } else if (requestCode == COLLEGE_CODE) {
                updateUserCollege(data.getStringExtra(COLLEGE));
            } else if (requestCode == SIGN_CODE) {
                updateUserInstruction(data.getStringExtra(INSTRUCTION));
            }
        }
    }

    private void updateUserInstruction(String insturction) {
        userViewModel.updateUserInstruction(insturction).observe(this, dataResource -> {
            switch (dataResource.status) {
                case LOADING:
                    ProgressHUD.showDialog(PersonalProfileController.this);
                    break;
                case SUCCESS:
                    ProgressHUD.close();
                    ToastSuccess.showDialog(this, "成功");
                    tvSign.setText(insturction);
                    isUserInforUpdated = true;
                    break;
                case ERROR:
                    ProgressHUD.close();
                    ToastUtils.showToast(this, getResources().getString(R.string.name_already_exist));
                    break;
                default:
                    break;
            }
        });
    }

    private void updateUserCollege(String college) {
        userViewModel.updateCollege(college).observe(this, dataResource -> {
            switch (dataResource.status) {
                case LOADING:
                    ProgressHUD.showDialog(PersonalProfileController.this);
                    break;
                case SUCCESS:
                    ProgressHUD.close();
                    ToastSuccess.showDialog(this, "成功");
                    tvUniversity.setText(college);
                    isUserInforUpdated = true;

                    break;
                case ERROR:
                    ProgressHUD.close();
                    ToastUtils.showToast(this, getResources().getString(R.string.name_already_exist));
                    break;
                default:
                    break;
            }
        });
    }

    private void updateUserNickName(String nickName) {
        userViewModel.updateNickname(nickName, userViewModel.getUserId()).observe(this, dataResource -> {
            switch (dataResource.status) {
                case LOADING:
                    ProgressHUD.showDialog(PersonalProfileController.this);
                    break;
                case SUCCESS:
                    ProgressHUD.close();
                    ToastSuccess.showDialog(this, "成功");
                    tvNickName.setText(nickName);
                    isUserInforUpdated = true;

                    break;
                case ERROR:
                    ProgressHUD.close();
                    ToastUtils.showToast(this, getResources().getString(R.string.name_already_exist));
                    break;
                default:
                    break;
            }
        });
    }


    private void compressImageAndUpload(File file, int screenWidth) {
        Flowable.just(file).map(originalFile -> {
            byte[] bytes = ImageUtil.compressBitmapToBytes(originalFile.getAbsolutePath(), screenWidth, screenWidth, 100, Bitmap.CompressFormat.WEBP);
            String webpName = originalFile.getName().substring(0, originalFile.getName().indexOf(".")) + ".webp";
            File webpFile = new File(originalFile.getParentFile(), webpName);
            FileUtils.bytesToFile(bytes, webpFile.getAbsolutePath());
            return webpFile;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(webpFile -> uploadImage(webpFile));
    }

    private void uploadImage(File file) {
        String userId = userViewModel.getUserId();
        if (TextUtils.isEmpty(userId)) {
            ToastUtils.showToast(this, getResources().getString(R.string.relogin));
            return;
        }
        userViewModel.uploadImage(file, userId).observe(this, dataResource -> {
            switch (dataResource.status) {
                case LOADING:
                    ProgressHUD.showDialog(PersonalProfileController.this);
                    break;
                case SUCCESS:
                    updateUserInfor(dataResource.data.getMessage(), file);

                    break;
                case ERROR:
                    ProgressHUD.close();
                    ToastUtils.showToast(this, dataResource.message);
                    break;
                default:
                    break;
            }
        });

    }

    private void updateUserInfor(String imageUrl, File file) {
        if (currentImage == ivhomeImg) {
            userViewModel.updateUserHomeimage(imageUrl).observe(this, new android.arch.lifecycle.Observer<DataResource>() {
                @Override
                public void onChanged(@Nullable DataResource dataResource) {
                    switch (dataResource.status) {
                        case LOADING:
                            break;
                        case SUCCESS:
                            isUserInforUpdated = true;
                            ProgressHUD.close();
                            setImageWithUri(file);
                            ToastSuccess.showDialog(PersonalProfileController.this, getResources().getString(R.string.upload_success));
                            break;
                        case ERROR:
                            ProgressHUD.close();
                            ToastUtils.showToast(PersonalProfileController.this, dataResource.message);
                            break;
                        default:
                            break;
                    }
                }
            });
        } else {
            userViewModel.updateUserAvatar(imageUrl).observe(this, new android.arch.lifecycle.Observer<DataResource>() {
                @Override
                public void onChanged(@Nullable DataResource dataResource) {
                    switch (dataResource.status) {
                        case LOADING:

                            break;
                        case SUCCESS:
                            isUserInforUpdated = true;
                            ProgressHUD.close();
                            setImageWithUri(file);
                            ToastSuccess.showDialog(PersonalProfileController.this, getResources().getString(R.string.upload_success));
                            break;
                        case ERROR:
                            ProgressHUD.close();
                            ToastUtils.showToast(PersonalProfileController.this, dataResource.message);
                            break;
                        default:
                            break;
                    }
                }
            });
        }


    }

    @Override
    public void onBackPressed() {
        if (isUserInforUpdated) {
            setResult(FOR_CONTROLLER_BACK);
        }
        super.onBackPressed();
    }

    private void setImageWithUri(File file) {
        Uri uri = Uri.fromFile(file);
        currentImage.setImageURI(uri);
    }

    @Override
    public void onDateSet(long date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(YYYY_MM_DD);
        String birthday = dateFormat.format(new Date(date));

        tvBirthday.setText(birthday);
        udateUserBirthday(birthday);
    }

    public void udateUserBirthday(String birthday) {
        userViewModel.updateBirthday(birthday, userViewModel.getUserId()).observe(this, dataResource -> {
            switch (dataResource.status) {
                case LOADING:
                    ProgressHUD.showDialog(PersonalProfileController.this);
                    break;
                case SUCCESS:
                    ProgressHUD.close();
                    ToastSuccess.showDialog(this, "成功");
                    isUserInforUpdated = true;

                    break;
                case ERROR:
                    ProgressHUD.close();
                    ToastUtils.showToast(this, dataResource.message);
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CleanLeakUtils.fixInputMethodManagerLeak(this);
    }
}

