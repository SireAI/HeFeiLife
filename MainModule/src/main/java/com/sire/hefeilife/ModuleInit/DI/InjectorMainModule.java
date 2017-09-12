package com.sire.hefeilife.ModuleInit.DI;

import dagger.Module;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/21
 * Author:Sire
 * Description: interface of collection modules and supply mediator implemention
 * ==================================================
 */
@Module(includes = {MainCommonModule.class, MainControllerModule.class, MainViewModelModule.class})
public class InjectorMainModule {

}
