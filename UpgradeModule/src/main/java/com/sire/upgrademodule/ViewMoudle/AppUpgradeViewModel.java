package com.sire.upgrademodule.ViewMoudle;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.sire.corelibrary.Executors.AppExecutors;
import com.sire.corelibrary.Networking.downlaod.RetrofitDownloadManager;
import com.sire.corelibrary.Networking.downlaod.downloadCore.DownloadProgressListener;
import com.sire.corelibrary.Networking.downlaod.downloadInfor.DownloadFileInfor;
import com.sire.corelibrary.Utils.APKInstallUtils;
import com.sire.corelibrary.Utils.FileBuilder;
import com.sire.corelibrary.Utils.SPUtils;
import com.sire.mediators.UpgradeModuleInterface.UpgradeCallback;
import com.sire.upgrademodule.BuildConfig;
import com.sire.upgrademodule.Pojo.UpgradeInfor;
import com.sire.upgrademodule.R;
import com.sire.upgrademodule.Utils.APPUpgradeUtils;
import com.sire.upgrademodule.Views.dialogs.ForceUpgradeDialog;
import com.sire.upgrademodule.Views.notifycations.ProgressEmit;
import com.sire.upgrademodule.WebService.AppUpgradeWebService;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/04
 * Author:Sire
 * Description:
 * ==================================================
 */
@Singleton
public class AppUpgradeViewModel extends ViewModel {
    public static final String DOWNLOADING = "downloading";
    private static final String PACKAGE = "package";
    private static final String VERSION_CODE = "versionCode";
    private static final String VERSION_NAME = "versionName";
    private static final String DOWNLOAD_APK_URL = "downloadAPKUrl";
    public static final String IS_FORCE_UPGRADE = "isForceUpgrade";
    private final Application context;
    private final AppExecutors appExecutors;

    private AppUpgradeWebService appUpgradeWebService;

    @Inject
    public AppUpgradeViewModel(AppUpgradeWebService appUpgradeWebService, Application context, AppExecutors appExecutors) {
        this.appUpgradeWebService = appUpgradeWebService;
        this.context = context;
        this.appExecutors = appExecutors;
    }


    public void checkVersion(FragmentActivity context) {
        int versionCode = APPUpgradeUtils.getVersionCode(this.context);
        String versionName = APPUpgradeUtils.getVersionName(this.context);
        Map<String, Object> params = new HashMap<>();
        params.put(VERSION_CODE, versionCode);
        params.put(VERSION_NAME, versionName);
        String downloadUrl = SPUtils.getValueString(this.context, DOWNLOAD_APK_URL);
        Observable.just(downloadUrl)
                .filter((String downloadedUrl) -> {
                    //本地检测，已经安装过的版本将apk包将删除，已经下载未安装的apk不需要重新下载
                    File file = new File(downloadedUrl);
                    boolean hasDownloaded = isRightVersionDownloaded(downloadUrl, file);
                    boolean isForceUpgrade = SPUtils.getValueBoolen(this.context, IS_FORCE_UPGRADE);

                    if (hasDownloaded && isForceUpgrade) {
                        Timber.i("apk已经下载：" + downloadedUrl);
                            installApk(this.context, file);
                    }else if(hasDownloaded){
                        //显示选择按钮是否需要安装，确定安装，取消则不安装
                        ForceUpgradeDialog.showDialog(context, false, new ForceUpgradeDialog.Callback() {
                            @Override
                            public void onUpgrade(DialogFragment dialog,boolean force) {
                                if (force) {
                                    installApk(context, file);
                                }
                                dialog.dismiss();
                            }
                        });
                    }
                    return !hasDownloaded;
                }).flatMap(undownload -> {
            //网络下载
            return appUpgradeWebService.checkAPPVersion(params);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jsonResponse -> {
                    //网络检测，是否需要下载
                    if (jsonResponse.isOK() && jsonResponse.getData() != null) {
                        if (jsonResponse.getData().isNeedUpgrade()) {
                            if (jsonResponse.getData().isForceUpgrade()) {
                                //点击确认后进行强制下载
                                ForceUpgradeDialog.showDialog(context, true, (ForceUpgradeDialog.Callback) (dialog, force) -> {
                                    if(force){
                                        forceDownloadAPK(jsonResponse.getData());
                                    }
                                    dialog.dismiss();
                                });
                                return;
                            } else {
                                //start a service to upgrade
                                startBackgroundDownload(jsonResponse.getData());
                            }
                        }
                    }
                }, throwable -> Timber.e(throwable));
    }

    private boolean isRightVersionDownloaded(String downloadUrl, File file) {
        boolean isRightVersionDownloaded = false;
        boolean fileExist = !(TextUtils.isEmpty(downloadUrl) || !file.exists());
        if(fileExist){
            int appVersionCode = APPUpgradeUtils.getVersionCode(context);
            int uninstallPackageVersionCode = APPUpgradeUtils.getUninstallPackageVersionCode(context, file.toString());
            if(appVersionCode<uninstallPackageVersionCode){
                isRightVersionDownloaded = true;
            }else {
                Timber.i("下载版本号小于运行版本");
                deletePackage(context,file);
            }
        }

        return isRightVersionDownloaded;
    }

    private void forceDownloadAPK(UpgradeInfor upgradeInfor) {
        ProgressEmit progressEmit = new ProgressEmit(context, context.getResources().getString(R.string.app_name));
        AppUpgradeViewModel.this.upgradeAppVersion(upgradeInfor, progressEmit);
    }

    private void startBackgroundDownload(UpgradeInfor upgradeInfor) {
        boolean downloading = SPUtils.getValueBoolen(context, DOWNLOADING);
        if (!downloading) {
            appExecutors.diskIO().execute(() -> upgradeAppVersion(upgradeInfor, null));
            SPUtils.saveKeyValueBlooen(context, DOWNLOADING, true);
            Timber.i("后台启动下载APK服务");
        }
    }

    private void upgradeAppVersion(UpgradeInfor upgradeInfor, ProgressEmit pogressEmit) {
        if (!TextUtils.isEmpty(upgradeInfor.getDownloadUrl()) && context != null) {
            DownloadFileInfor downloadFileInfor = new DownloadFileInfor();
            downloadFileInfor.setReadStartPonint(0);
            downloadFileInfor.setId(0);
            downloadFileInfor.setContentLength(upgradeInfor.getFileSize());
            String savePath = FileBuilder
                    .create()
                    .withFileType(FileBuilder.FileType.DATA)
                    .withFileName(upgradeInfor.getVersionName() + ".apk")
                    .build(context)
                    .toString();
            downloadFileInfor.setSavePath(savePath);
            downloadFileInfor.setUrl(upgradeInfor.getDownloadUrl());
            downloadApp(context, downloadFileInfor, new DownloadProgressListener() {
                @Override
                public void update(long read, long count, boolean done) {
                    if (pogressEmit != null) {
                        pogressEmit.notifyProgress(read, count, done);
                    }
                }

                @Override
                public void onSubscribe() {
                    Timber.i("开始下载APK");
                }

                @Override
                public void onError(Throwable e) {
                    pogressEmit.setDownloadError();
                    SPUtils.saveKeyValueBlooen(context, DOWNLOADING, false);
                }

                @Override
                public void onComplete(DownloadFileInfor downloadFileInfor) {
                    //unwrapp download state
                    SPUtils.saveKeyValueBlooen(context, DOWNLOADING, false);
                    //record the downloaded
                    SPUtils.saveKeyValueString(context, DOWNLOAD_APK_URL, downloadFileInfor.getSavePath());
                    SPUtils.saveKeyValueBlooen(context, IS_FORCE_UPGRADE,upgradeInfor.isForceUpgrade());
                    if(upgradeInfor.isForceUpgrade()){
                        installApk(context, new File(downloadFileInfor.getSavePath()));
                    }
                }
            });
        } else {
            Timber.e("下载地址为空");
        }
    }

    private void downloadApp(Context context, DownloadFileInfor downloadFileInfor, DownloadProgressListener downloadProgressListener) {
        RetrofitDownloadManager downloadManager = RetrofitDownloadManager.getInstance(context);
        downloadManager.downloadFile(downloadFileInfor, downloadProgressListener);
    }







    /**
     * 安装
     *
     * @param context
     * @param apkFile
     */
    private void installApk(Context context, File apkFile) {
        Observable.just(apkFile)
                .filter(file -> {
                    boolean isComplete = APPUpgradeUtils.getUninatllApkInfo(context, apkFile.toString());
                    if (!isComplete) {
                        deletePackage(context, apkFile);
                        Timber.e("文件" + apkFile.toString() + "不完整,已经删除");
                    }
                    return isComplete;
                })
                .doAfterNext(file -> APKInstallUtils.installApk(context, apkFile))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    private void deletePackage(Context context, File apkFile) {
        apkFile.delete();
        SPUtils.saveKeyValueString(context, DOWNLOAD_APK_URL, "");
        Timber.i("安装完成，删除apk文件："+apkFile.toString());
    }


}
