package com.sire.feedmodule.DI;

import android.arch.lifecycle.ViewModel;
import android.support.v4.app.Fragment;

import com.sire.corelibrary.DI.ViewModelKey;
import com.sire.feedmodule.Controller.LatestInformationController;
import com.sire.feedmodule.ViewModel.FeedViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/24
 * Author:Sire
 * Description:parameter is the implemention of the return type ,it will be
 * a {@code Map<K, Provider<V>>} where key is annotated and value is the return type
 * ==================================================
 */
@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(FeedViewModel.class)
    abstract ViewModel bindUserViewModel(FeedViewModel feedViewModel);


}
