package com.sire.corelibrary.DI.Environment;

import android.arch.lifecycle.ViewModelProvider;

import com.sire.corelibrary.Viewmodel.ViewModelFactory;

import dagger.Binds;
import dagger.Module;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/05
 * Author:Sire
 * Description:
 * ==================================================
 */
@Module
public abstract class
ViewModelFactoryInject {
    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
