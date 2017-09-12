

package com.sire.hefeilife.Global;


import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.sire.corelibrary.Bug.BugReport;
import com.sire.corelibrary.Networking.NetConnection.NetStateReceiver;
import com.sire.corelibrary.UICheck.UIBlockTrack;
import com.sire.hefeilife.BuildConfig;
import com.sire.hefeilife.ModuleInit.DI.Base.AppInjector;


import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import timber.log.Timber;


public class SireApp extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            //ANR日志显示
            UIBlockTrack.start();
            Timber.plant(new Timber.DebugTree());
        }
        //依赖注入
        AppInjector.init(this);
        //bug手机
        BugReport.configuration(this);
        //连网通知
        NetStateReceiver.registerSelf(this);
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    protected void attachBaseContext(Context base) {

        super.attachBaseContext(base);
        //dex分包方案
//        MultiDex.install(this);

    }
}
