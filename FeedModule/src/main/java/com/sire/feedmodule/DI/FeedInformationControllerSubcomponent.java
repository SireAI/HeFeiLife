package com.sire.feedmodule.DI;

import com.sire.feedmodule.Controller.fragment.FeedInformationController;
import com.sire.feedmodule.Controller.fragment.UserDynamicController;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent()
public interface FeedInformationControllerSubcomponent extends AndroidInjector<FeedInformationController> {
    @Subcomponent.Builder
     abstract class FeedInformationControllerBuilder extends AndroidInjector.Builder<FeedInformationController> {}

}