package com.sire.feedmodule.DI;

import com.sire.feedmodule.Controller.fragment.FeedInformationController;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent()
public interface LatestInformationControllerSubcomponent extends AndroidInjector<FeedInformationController> {
    @Subcomponent.Builder
    public abstract class Builder extends AndroidInjector.Builder<FeedInformationController> {}
}