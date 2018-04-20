package com.sire.messagepushmodule.Mediator;

import android.os.Message;

import com.sire.corelibrary.DI.Environment.ModuleInit;
import com.sire.corelibrary.DI.Environment.ModuleInitInfor;
import com.sire.mediators.MessagePushModuleInterface.MessagePushMediator;
import com.sire.messagepushmodule.Controller.fragment.MessageController;
import com.sire.messagepushmodule.ViewModel.MessagePushViewModel;

import org.greenrobot.eventbus.EventBus;
import org.reactivestreams.Publisher;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/12
 * Author:Sire
 * Description:
 * ==================================================
 */
public class MessagePushMediatorImpl implements MessagePushMediator,ModuleInit{
    private final MessageController messageController;
    private final MessagePushViewModel messagePushViewModel;
    @Inject
    public MessagePushMediatorImpl(MessagePushViewModel messagePushViewModel, MessageController messageController) {
        this.messagePushViewModel = messagePushViewModel;
        this.messageController = messageController;
    }


    @Override
    public Flowable<ModuleInitInfor> init() {
        return Flowable.just("messagePush").flatMap(new Function<String, Publisher<ModuleInitInfor>>() {
            @Override
            public Publisher<ModuleInitInfor> apply(String s) throws Exception {
                messagePushViewModel.initPushSDK();
                return Flowable.just(new ModuleInitInfor("MessagePushModule","推送服务初始化"));
            }
        });
    }

    @Override
    public Object getMessageController() {

        return messageController;
    }
}
