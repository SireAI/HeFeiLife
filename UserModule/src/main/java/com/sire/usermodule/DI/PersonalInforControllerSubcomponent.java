package com.sire.usermodule.DI;

import com.sire.usermodule.Controller.fragment.PersonalInforController;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent()
public interface PersonalInforControllerSubcomponent extends AndroidInjector<PersonalInforController> {

    @Subcomponent.Builder
     abstract class PersonalInforControllerBuilder extends Builder<PersonalInforController> {}
}