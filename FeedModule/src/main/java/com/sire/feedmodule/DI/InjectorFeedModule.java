package com.sire.feedmodule.DI;

import com.sire.feedmodule.Mediator.FeedMediatorImpl;
import com.sire.mediators.FeedmoduleInterface.FeedMediator;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/21
 * Author:Sire
 * Description: interface of collection modules and supply mediator implemention
 * ==================================================
 */
@Module(includes = {CommonModule.class, ControllerModule.class, ViewModelModule.class})
public class InjectorFeedModule {

    @Singleton
    @Provides
    FeedMediator provideFeedMediator(FeedMediatorImpl feedMediator) {
        return feedMediator;
    }

}
