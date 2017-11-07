package com.sire.feedmodule.DI;

import com.sire.feedmodule.Controller.LatestInformationController;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent()
public interface LatestInformationControllerSubcomponent extends AndroidInjector<LatestInformationController> {
    @Subcomponent.Builder
    public abstract class Builder extends AndroidInjector.Builder<LatestInformationController> {}
}