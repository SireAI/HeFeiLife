package com.sire.hefeilife.ModuleInit.DI;


import com.sire.hefeilife.Controller.MainController;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/25
 * Author:Sire
 * Description:controller di
 * ==================================================
 */
@Module
public abstract class MainControllerModule {
    @ContributesAndroidInjector
    abstract MainController contributeMainController();
}
