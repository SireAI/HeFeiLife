package com.sire.messagepushmodule.ModuleInit.DI;

import com.sire.mediators.MessagePushModuleInterface.MessagePushMediator;
import com.sire.messagepushmodule.Mediator.MessagePushMediatorImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/07
 * Author:Sire
 * Description:
 * ==================================================
 */
@Module(includes = MessagePushViewModelModule.class)
public class InjectorMessagePushModule {

    @Singleton
    @Provides
    MessagePushMediator provideMessagePushMediator(MessagePushMediatorImpl messagePushMediator) {
        return messagePushMediator;
    }
}
