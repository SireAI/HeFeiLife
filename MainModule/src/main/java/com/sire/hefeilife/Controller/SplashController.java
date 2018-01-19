package com.sire.hefeilife.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.sire.corelibrary.Bug.BugReport;
import com.sire.corelibrary.Bug.CleanLeakUtils;
import com.sire.corelibrary.Controller.Segue;
import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.DI.Environment.ModuleInit;
import com.sire.corelibrary.DI.Environment.ModuleInitActions;
import com.sire.corelibrary.Executors.AppExecutors;
import com.sire.corelibrary.Networking.NetConnection.NetStateReceiver;
import com.sire.hefeilife.R;
import com.sire.mediators.BBSModuleInterface.BBSMediator;
import com.sire.mediators.MessagePushModuleInterface.MessagePushMediator;
import com.sire.mediators.UserModuleInterface.UserMediator;
import com.sire.mediators.core.CallBack;
import com.sire.usermodule.Controller.CompletePersonalInforController;
import com.sire.usermodule.Controller.CompletePersonalInforPhotoController;
import com.squareup.leakcanary.LeakCanary;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/17
 * Author:Sire
 * Description:
 * ==================================================
 */

public class SplashController extends SireController {
    public static final int SLEEP_INTAVEL = 1000;
    @Inject
    UserMediator userMediator;
    @Inject
    MessagePushMediator messagePushMediator;
    @Inject
    BBSMediator bbsMediator;
    @Inject
    AppExecutors appExecutors;
    private long start;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller_splash);
        start = System.currentTimeMillis();
        //连网通知
        NetStateReceiver.registerSelf(this);
        appInit();


    }

    private void appInit() {
        //内存泄露检测
        if (LeakCanary.isInAnalyzerProcess(this.getApplication())) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this.getApplication());
        //bug记录收集
        BugReport.configuration(this);
        //模块初始化
        ModuleInitActions moduleInitActions = new ModuleInitActions();
        moduleInitActions.with((ModuleInit) messagePushMediator)
                .with((ModuleInit) bbsMediator)
                .with((ModuleInit) userMediator)
                .excute(data -> {
                    Timber.d("-------------- 应用初始化成功  ----------------");
                    long deltaTime = System.currentTimeMillis() - start;
                    if (deltaTime < SLEEP_INTAVEL) {
                        appExecutors.mainHandler().postDelayed(() -> {
                            segueToMainController();
                        }, SLEEP_INTAVEL - deltaTime);
                    } else {
                        segueToMainController();
                    }
                });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private void segueToMainController() {
        Intent intent = new Intent(this, MainController.class);
        segue(Segue.SegueType.CENTER_SCALE, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CleanLeakUtils.fixInputMethodManagerLeak(this);
    }
}
