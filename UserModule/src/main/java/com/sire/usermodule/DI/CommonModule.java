package com.sire.usermodule.DI;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.sire.corelibrary.Lifecycle.DataLife.LiveDataCallAdapterFactory;
import com.sire.corelibrary.Networking.Network;
import com.sire.corelibrary.Networking.WebUrl;
import com.sire.usermodule.DB.Dao.UserDao;
import com.sire.usermodule.DB.UserDb;
import com.sire.usermodule.WebService.UserWebService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
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
    UserWebService provideUserWebService(Application application) {
        return new Retrofit.Builder()
                .baseUrl(WebUrl.getHostUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(Network.genericClient(application))
                .build()
                .create(UserWebService.class);
    }

    @Singleton
    @Provides
    UserDb provideUserDb(Application app) {
        return Room.databaseBuilder(app, UserDb.class, "user.db").build();
    }


    @Singleton
    @Provides
    UserDao provideUserDao(UserDb db) {
        return db.userDao();
    }


}
