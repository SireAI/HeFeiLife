package com.sire.feedmodule.DI;

import android.support.v4.app.Fragment;

import com.sire.feedmodule.Controller.fragment.FeedInformationController;

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
@Module(subcomponents = {LatestInformationControllerSubcomponent.class})
public abstract class ControllerModule {


    @Binds
    @IntoMap
    @FragmentKey(FeedInformationController.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindLatestInformationControllerInjectorFactory(LatestInformationControllerSubcomponent.Builder builder);
}
