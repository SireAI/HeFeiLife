package com.sire.usermodule.DI;

import com.sire.mediators.UserModuleInterface.UserMediator;
import com.sire.usermodule.Mediator.UserMediatorImpl;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.android.DaggerFragment_MembersInjector;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/21
 * Author:Sire
 * Description: interface of collection modules and supply mediator implemention
 * ==================================================
 */
@Module(includes = {CommonModule.class,ControllerModule.class,ViewModelModule.class})
public  class InjectorUserModule {

    @Singleton @Provides
    UserMediator provideUserMediator(UserMediatorImpl userMediator){
        return userMediator;
    }

}
