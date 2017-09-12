package com.sire.usermodule.DI;

import android.arch.lifecycle.ViewModel;

import com.sire.corelibrary.DI.ViewModelKey;
import com.sire.usermodule.ViewModel.UserViewModel;

import dagger.Binds;
import dagger.Module;
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
    @ViewModelKey(UserViewModel.class)
    abstract ViewModel bindUserViewModel(UserViewModel userViewModel);
}
