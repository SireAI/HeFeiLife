package com.sire.messagepushmodule.DI;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.sire.corelibrary.Lifecycle.DataLife.LiveDataCallAdapterFactory;
import com.sire.corelibrary.Networking.Network;
import com.sire.corelibrary.Networking.WebUrl;
import com.sire.mediators.MessagePushModuleInterface.MessagePushMediator;
import com.sire.messagepushmodule.DB.Dao.MessageDao;
import com.sire.messagepushmodule.DB.MessageDb;
import com.sire.messagepushmodule.Mediator.MessagePushMediatorImpl;
import com.sire.messagepushmodule.Service.PushIntentService;
import com.sire.messagepushmodule.WebService.MessagePushWebService;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ServiceKey;
import dagger.multibindings.IntoMap;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/07
 * Author:Sire
 * Description:
 * ==================================================
 */
@Module(includes = {MessagePushModule.class,ControllerModule.class})
public class InjectorMessagePushModule {

    @Singleton
    @Provides
    MessagePushMediator provideMessagePushMediator(MessagePushMediatorImpl messagePushMediator) {
        return messagePushMediator;
    }

    @Singleton
    @Provides
    MessagePushWebService provideMessagePushWebService(Application application) {
        return new Retrofit.Builder()
                .baseUrl(WebUrl.getHostUrl())
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(Network.genericClient(application))
                .build()
                .create(MessagePushWebService.class);
    }

    @Singleton
    @Provides
    MessageDb provideMessageDb(Application app) {
        return Room.databaseBuilder(app, MessageDb.class, "messageDb.db").build();
    }


    @Singleton
    @Provides
    MessageDao provideMessageDao(MessageDb db) {
        return db.messageDao();
    }
}


