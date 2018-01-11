package com.sire.feedmodule.DI;

import android.support.v4.app.Fragment;

import com.sire.feedmodule.Controller.fragment.FeedInformationController;
import com.sire.feedmodule.Controller.fragment.UserDynamicController;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
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
@Module(subcomponents = {FeedInformationControllerSubcomponent.class,UserDynamicControllerSubcomponent.class})
public abstract class ControllerModule {


    @Binds
    @IntoMap
    @FragmentKey(FeedInformationController.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindFeedInformationControllerInjectorFactory(FeedInformationControllerSubcomponent.FeedInformationControllerBuilder builder);

    @Binds
    @IntoMap
    @FragmentKey(UserDynamicController.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindUserDynamicControllerInjectorFactory(UserDynamicControllerSubcomponent.UserDynamicControllerBuilder builder);
}
