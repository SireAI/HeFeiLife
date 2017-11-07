package com.sire.feedmodule.DI;

import android.support.v4.app.Fragment;

import com.sire.feedmodule.Controller.InformationFlowController;
import com.sire.feedmodule.Controller.LatestInformationController;

import dagger.Binds;
import dagger.Module;
import dagger.Subcomponent;
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
//    @ContributesAndroidInjector
//    abstract InformationFlowController contributeInformationFlowController();



    @Binds
    @IntoMap
    @FragmentKey(LatestInformationController.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindLatestInformationControllerInjectorFactory(LatestInformationControllerSubcomponent.Builder builder);
}
