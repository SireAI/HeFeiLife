

package com.sire.hefeilife.Global;


import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.sire.corelibrary.Bug.BugReport;
import com.sire.corelibrary.Networking.NetConnection.NetStateReceiver;
import com.sire.corelibrary.UICheck.UIBlockTrack;
import com.sire.hefeilife.BuildConfig;
import com.sire.hefeilife.ModuleInit.DI.Base.AppInjector;
import com.sire.mediators.MessagePushModuleInterface.MessagePushMediator;


import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;
import dagger.android.support.HasSupportFragmentInjector;
import timber.log.Timber;


public class SireApp extends Application implements HasActivityInjector,HasServiceInjector{

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
    @Inject
    DispatchingAndroidInjector<Service> serviceDispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        AppInjector.init(this);


        if (BuildConfig.DEBUG) {
            //ANR日志显示
            UIBlockTrack.start();
            Timber.plant(new Timber.DebugTree());
        }

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

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return serviceDispatchingAndroidInjector;
    }


}
