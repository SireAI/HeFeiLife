package com.sire.bbsmodule.DI;

import com.sire.bbsmodule.Mediator.BBSMediatorImpl;
import com.sire.mediators.BBSModuleInterface.BBSMediator;

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
@Module(includes = {CommonModule.class, ControllerModule.class, ViewModelModule.class})
public class InjectorBBSModule {

    @Singleton
    @Provides
    BBSMediator provideFeedMediator(BBSMediatorImpl bbsMediator) {
        return bbsMediator;
    }

}
