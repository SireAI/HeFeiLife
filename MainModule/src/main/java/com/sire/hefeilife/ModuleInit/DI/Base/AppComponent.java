/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sire.hefeilife.ModuleInit.DI.Base;


import android.app.Application;

import com.sire.baidulocation.DI.InjectorBaiduLocationModule;
import com.sire.bbsmodule.DI.InjectorBBSModule;
import com.sire.corelibrary.DI.Environment.InjectorEnvironmentMoudle;
import com.sire.feedmodule.DI.InjectorFeedModule;
import com.sire.hefeilife.Global.SireApp;
import com.sire.hefeilife.ModuleInit.DI.InjectorMainModule;
import com.sire.messagepushmodule.ModuleInit.DI.InjectorMessagePushModule;
import com.sire.sharemodule.ModuleInit.DI.InjectorShareModule;
import com.sire.upgrademodule.ModuleInit.DI.InjectorUpgradeModule;
import com.sire.usermodule.DI.InjectorUserModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/7/20
 * Author:sire
 * Description:注入类
 * ==================================================
 */
@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AndroidSupportInjectionModule.class,
        InjectorShareModule.class,
        InjectorEnvironmentMoudle.class,
        InjectorUserModule.class,
        InjectorUpgradeModule.class,
        InjectorMessagePushModule.class,
        InjectorMainModule.class,
        InjectorFeedModule.class,
        InjectorBBSModule.class,
        InjectorBaiduLocationModule.class
})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance Builder application(Application application);
        AppComponent build();
    }
    void inject(SireApp application);
}
