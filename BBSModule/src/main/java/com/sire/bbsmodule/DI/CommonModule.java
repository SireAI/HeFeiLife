package com.sire.bbsmodule.DI;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.sire.bbsmodule.DB.BBSDb;
import com.sire.bbsmodule.DB.Dao.BBSDao;
import com.sire.bbsmodule.WebService.BBSWebService;
import com.sire.corelibrary.Lifecycle.DataLife.LiveDataCallAdapterFactory;
import com.sire.corelibrary.Networking.Network;
import com.sire.corelibrary.Networking.WebUrl;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

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
    BBSWebService provideBBSWebService(Application application) {
        return new Retrofit.Builder()
                .baseUrl(WebUrl.getHostUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(Network.genericClient(application))
                .build()
                .create(BBSWebService.class);
    }

    @Singleton
    @Provides
    BBSDb provideBBSDb(Application app) {
        return Room.databaseBuilder(app, BBSDb.class, "BBSDb.db").build();
    }


    @Singleton
    @Provides
    BBSDao provideBBSDao(BBSDb db) {
        return db.bbsDao();
    }



}
