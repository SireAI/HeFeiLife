package com.sire.upgrademodule.ModuleInit.DI;

import com.sire.corelibrary.Lifecycle.DataLife.LiveDataCallAdapterFactory;
import com.sire.corelibrary.Networking.WebUrl;
import com.sire.upgrademodule.WebService.AppUpgradeWebService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/24
 * Author:Sire
 * Description:common object di
 * ==================================================
 */
@Module
public class CommonModule {


    @Singleton
    @Provides
    AppUpgradeWebService provideUserWebService() {
        return new Retrofit.Builder()
                .baseUrl(WebUrl.getHostUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(AppUpgradeWebService.class);
    }


}
