package com.sire.bbsmodule.DI;

import android.support.v4.app.Fragment;


import com.sire.bbsmodule.Controller.NearyByController;
import com.sire.bbsmodule.Controller.OtherReasonReportController;
import com.sire.bbsmodule.Controller.PersonalHomePageController;
import com.sire.bbsmodule.Controller.PostController;
import com.sire.bbsmodule.Controller.PostPublishController;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/25
 * Author:Sire
 * Description:controller di
 * ==================================================
 */
@Module()
public abstract class ControllerModule {
    @ContributesAndroidInjector
    abstract PostController contributePostController();

    @ContributesAndroidInjector
    abstract OtherReasonReportController contributeOtherReasonReportController();
    @ContributesAndroidInjector
    abstract PostPublishController contributePostPublishController();
    @ContributesAndroidInjector
    abstract NearyByController contributeNearyByController();
    @ContributesAndroidInjector
    abstract PersonalHomePageController contributePersonalHomePageController();
}
