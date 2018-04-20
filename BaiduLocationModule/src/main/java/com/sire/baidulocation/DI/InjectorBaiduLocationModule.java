package com.sire.baidulocation.DI;


import com.sire.baidulocation.Mediator.BaiduLocationMediatorImpl;
import com.sire.mediators.BaiduLocationModuleInterface.BaiduLocationMeditor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/21
 * Author:Sire
 * Description: interface of collection modules and supply mediator implemention
 * ==================================================
 */
@Module(includes = {BaiduLocationViewModelModule.class})
public class InjectorBaiduLocationModule {

    @Singleton
    @Provides
    BaiduLocationMeditor provideBaiduLocationMeditor(BaiduLocationMediatorImpl baiduLocationMeditor) {
        return baiduLocationMeditor;
    }

}
