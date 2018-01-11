package com.sire.baidulocation.DI;

import android.arch.lifecycle.ViewModel;

import com.sire.baidulocation.ViewModel.BaiduLocationViewModel;
import com.sire.corelibrary.DI.ViewModelKey;

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
public abstract class BaiduLocationViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(BaiduLocationViewModel.class)
    abstract ViewModel bindBaiduLocationViewModel(BaiduLocationViewModel baiduLocationViewModel);
}
