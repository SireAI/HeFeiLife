package com.sire.messagepushmodule.DI;

import com.sire.messagepushmodule.Controller.fragment.MessageController;
import com.sire.messagepushmodule.Service.PushIntentService;

import dagger.Subcomponent;
import dagger.android.AndroidInjector;

@Subcomponent()
public interface MessageControllerSubcomponent extends AndroidInjector<MessageController> {

    @Subcomponent.Builder
     abstract class MessageControllerBuilder extends Builder<MessageController> {}

}