package com.sire.upgrademodule.ViewMoudle;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.sire.corelibrary.Networking.Network;
import com.sire.corelibrary.Networking.downlaod.RetrofitDownloadManager;
import com.sire.corelibrary.Networking.downlaod.downloadCore.DownloadProgressListener;
import com.sire.corelibrary.Networking.downlaod.downloadInfor.DownloadFileInfor;
import com.sire.corelibrary.Pojo.JsonResponse;
import com.sire.corelibrary.Utils.APKInstallUtils;
import com.sire.corelibrary.Utils.FileBuilder;
import com.sire.mediators.core.ActiveState;
import com.sire.mediators.core.CallBack;
import com.sire.upgrademodule.Pojo.UpgradeInfor;
import com.sire.upgrademodule.Utils.APPUpgradeUtils;
import com.sire.upgrademodule.WebService.AppUpgradeWebService;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

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

public class AppUpgradeViewModel extends ViewModel {
    public static final String PACKAGE = "package";
    public static final String VERSION_CODE = "versionCode";
    public static final String VERSION_NAME = "versionName";
    private final Application context;
    private BroadcastReceiver versionUpdateResuslt;

    private AppUpgradeWebService appUpgradeWebService;

    @Inject
    public AppUpgradeViewModel(AppUpgradeWebService appUpgradeWebService, Application context) {
        this.appUpgradeWebService = appUpgradeWebService;
        this.context = context;
    }

    public void checkVersion(CallBack<ActiveState> callBack) {
        int versionCode = APPUpgradeUtils.getVersionCode(context);
        String versionName = APPUpgradeUtils.getVersionName(context);
        Map<String, Object> params = new HashMap<>();
        params.put(VERSION_CODE, versionCode);
        params.put(VERSION_NAME, versionName);
        Observable<JsonResponse<UpgradeInfor>> responseObservable = appUpgradeWebService.checkAPPVersion(params);
        responseObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jsonResponse -> {
                    if(jsonResponse.isOK()){
                        callBack.apply(jsonResponse.getData().isNeedUpgrade() ? ActiveState.SUCCESS : ActiveState.FAILED);
                        if (jsonResponse.getData().isNeedUpgrade()) {
                            upgradeAppVersion(jsonResponse.getData());
                        }
                    }else {
                        callBack.apply(ActiveState.FAILED);
                    }
                });
    }

    public void upgradeAppVersion(UpgradeInfor upgradeInfor) {
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
                    System.out.println("read = [" + read + "], count = [" + count + "], done = [" + done + "]");
                }

                @Override
                public void onComplete(DownloadFileInfor downloadFileInfor) {
                    installApk(context, new File(downloadFileInfor.getSavePath()));
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
     * 应用内版本更新安装完成监听
     *
     * @param context
     * @param apkFile
     */
    private void registerVersionUpdateBroadCastReceiver(final Context context, File apkFile) {
        if (versionUpdateResuslt == null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_PACKAGE_ADDED);
            filter.addDataScheme(PACKAGE);
            versionUpdateResuslt = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
//                         && intent.getData().toString().contains(context.getPackageName())
                        context.unregisterReceiver(this);
                        apkFile.delete();
                        versionUpdateResuslt = null;
                    }
                }
            };
            context.registerReceiver(versionUpdateResuslt, filter);
        }
    }


    /**
     * 安装
     *
     * @param context
     * @param apkFile
     */
    public void installApk(Context context, File apkFile) {
        Observable.just(apkFile)
                .filter(file -> {
                    boolean isComplete = APPUpgradeUtils.getUninatllApkInfo(context, apkFile.toString());
                    if (!isComplete) {
                        apkFile.delete();
                        Timber.e("文件" + apkFile.toString() + "不完整,已经删除");
                    }
                    return isComplete;
                })
                .doOnNext(file -> registerVersionUpdateBroadCastReceiver(context, apkFile))
                .doAfterNext(file -> APKInstallUtils.installApk(context, apkFile))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }


}
