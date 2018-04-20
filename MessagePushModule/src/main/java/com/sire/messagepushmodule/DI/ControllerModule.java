package com.sire.messagepushmodule.DI;

import android.app.IntentService;
import android.app.Service;
import android.support.v4.app.Fragment;

import com.sire.messagepushmodule.Controller.IMMessageController;
import com.sire.messagepushmodule.Controller.fragment.MessageCommentDetailController;
import com.sire.messagepushmodule.Controller.fragment.MessageController;
import com.sire.messagepushmodule.Service.PushIntentService;

import dagger.Binds;
import dagger.Component;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import dagger.android.ContributesAndroidInjector;
import dagger.android.ServiceKey;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

/**
 * ==================================================
 * All Right Reserved
 * Date:2018/01/22
 * Author:Sire
 * Description:
 * ==================================================
 */
@Module(subcomponents = {MessageControllerSubcomponent.class})
public abstract class ControllerModule {
    @Binds
    @IntoMap
    @FragmentKey(MessageController.class)
    abstract AndroidInjector.Factory<? extends Fragment>
    bindMessageControllerInjectorFactory(MessageControllerSubcomponent.MessageControllerBuilder builder);

    @ContributesAndroidInjector
    abstract MessageCommentDetailController contributeMessageCommentDetailController();

    @ContributesAndroidInjector
    abstract IMMessageController contributeIMMessageController();
}
