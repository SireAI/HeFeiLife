package com.sire.feedmodule.DI;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.sire.corelibrary.Lifecycle.DataLife.LiveDataCallAdapterFactory;
import com.sire.corelibrary.Networking.Network;
import com.sire.corelibrary.Networking.WebUrl;
import com.sire.feedmodule.DB.Dao.FeedDao;
import com.sire.feedmodule.DB.FeedDb;
import com.sire.feedmodule.WebService.FeedWebService;

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
    FeedWebService provideUserWebService(Application application) {
        return new Retrofit.Builder()
                .baseUrl(WebUrl.getHostUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(Network.genericClient(application))
                .build()
                .create(FeedWebService.class);
    }

    @Singleton
    @Provides
    FeedDb provideUserDb(Application app) {
        return Room.databaseBuilder(app, FeedDb.class, "feed.db").build();
    }


    @Singleton
    @Provides
    FeedDao provideUserDao(FeedDb db) {
        return db.feedDao();
    }


}
