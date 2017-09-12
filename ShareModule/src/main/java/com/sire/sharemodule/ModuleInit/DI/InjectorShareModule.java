package com.sire.sharemodule.ModuleInit.DI;

import com.sire.mediators.ShareModuleInterface.ShareMediator;
import com.sire.mediators.UserModuleInterface.UserMediator;
import com.sire.sharemodule.Mediator.ShareMediatorImpl;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/07
 * Author:Sire
 * Description:
 * ==================================================
 */
@Module(includes = ShareViewModelModule.class)
public class InjectorShareModule {


    @Singleton
    @Provides
    ShareMediator provideShareMediator(ShareMediatorImpl shareMediator){

        return shareMediator;
    }
}
