package com.sire.feedmodule.DI;

import com.sire.feedmodule.Controller.fragment.FeedInformationController;
import com.sire.feedmodule.Controller.fragment.UserDynamicController;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent()
public interface UserDynamicControllerSubcomponent extends AndroidInjector<UserDynamicController> {

    @Subcomponent.Builder
     abstract class UserDynamicControllerBuilder extends Builder<UserDynamicController> {}
}