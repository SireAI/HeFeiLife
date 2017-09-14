package com.sire.messagepushmodule.Mediator;

import com.sire.mediators.MessagePushModuleInterface.MessagePushMediator;
import com.sire.messagepushmodule.ViewModel.MessagePushViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/12
 * Author:Sire
 * Description:
 * ==================================================
 */
@Singleton
public class MessagePushMediatorImpl implements MessagePushMediator{
    private MessagePushViewModel messagePushViewModel;
    @Inject
    public MessagePushMediatorImpl(MessagePushViewModel messagePushViewModel) {
        this.messagePushViewModel = messagePushViewModel;
    }

    @Override
    public void initPushSDK() {
        messagePushViewModel.initPushSDK();
    }
}
