package com.sire.hefeilife.Controller;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.sire.corelibrary.Bug.BugReport;
import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.Executors.AppExecutors;
import com.sire.corelibrary.Networking.NetConnection.NetStateReceiver;
import com.sire.corelibrary.UICheck.UIBlockTrack;
import com.sire.hefeilife.BuildConfig;
import com.sire.hefeilife.R;
import com.sire.mediators.MessagePushModuleInterface.MessagePushMediator;
import com.sire.mediators.UserModuleInterface.UserMediator;

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
@Inject
 UserMediator userMediator;
    @Inject
    MessagePushMediator messagePushMediator;
@Inject
 AppExecutors appExecutors;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller_splash);

        appInit();
        appExecutors.mainHandler().postDelayed(() -> userMediator.segueToEntranceController(SplashController.this),1000);

        //连网通知
        NetStateReceiver.registerSelf(this);

    }

    private void appInit() {

        //依赖注入
        //bug手机
        BugReport.configuration(this);
        messagePushMediator.initPushSDK();
    }


}
