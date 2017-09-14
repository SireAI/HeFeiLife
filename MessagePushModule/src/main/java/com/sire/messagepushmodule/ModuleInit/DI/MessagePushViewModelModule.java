package com.sire.messagepushmodule.ModuleInit.DI;

import android.arch.lifecycle.ViewModel;

import com.sire.corelibrary.DI.ViewModelKey;
import com.sire.messagepushmodule.ViewModel.MessagePushViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/08
 * Author:Sire
 * Description:
 * ==================================================
 */
@Module
public abstract class MessagePushViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MessagePushViewModel.class)
    abstract ViewModel bindMessagePushViewModel(MessagePushViewModel messagePushViewModel);
}
