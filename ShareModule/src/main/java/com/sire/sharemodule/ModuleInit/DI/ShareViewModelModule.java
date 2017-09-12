package com.sire.sharemodule.ModuleInit.DI;

import android.arch.lifecycle.ViewModel;

import com.sire.corelibrary.DI.ViewModelKey;
import com.sire.sharemodule.ViewModule.SMSModel;

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
public abstract class ShareViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(SMSModel.class)
    abstract ViewModel bindSMSModel(SMSModel smsModel);
}
