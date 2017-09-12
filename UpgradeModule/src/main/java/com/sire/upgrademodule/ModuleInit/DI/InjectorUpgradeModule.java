package com.sire.upgrademodule.ModuleInit.DI;

import com.sire.mediators.ShareModuleInterface.ShareMediator;
import com.sire.mediators.UpgradeModuleInterface.UpgradeMediator;
import com.sire.upgrademodule.Mediator.UpgradeMediatorImpl;

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
@Module(includes = {CommonModule.class, ViewModelModule.class})
public class InjectorUpgradeModule {
    @Singleton
    @Provides
    UpgradeMediator provideShareMediator(UpgradeMediatorImpl upgradeMediator){
        return upgradeMediator;
    }
}
