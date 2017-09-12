package com.sire.usermodule.DI;

import com.sire.usermodule.Controller.LoginController;

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
public abstract class ControllerModule {
    @ContributesAndroidInjector
    abstract LoginController contributeLoginController();
}
