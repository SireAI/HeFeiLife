package com.sire.messagepushmodule.ViewModel;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;

import com.igexin.sdk.PushManager;
import com.sire.corelibrary.Executors.AppExecutors;
import com.sire.corelibrary.Utils.APPUtils;
import com.sire.messagepushmodule.Service.PushClientService;
import com.sire.messagepushmodule.Service.PushIntentService;


import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/12
 * Author:Sire
 * Description:
 * ==================================================
 */
@Singleton
public class MessagePushViewModel extends ViewModel{
    private final AppExecutors appExecutors;
    private  Application context;

    @Inject
    public MessagePushViewModel(Application context,AppExecutors appExecutors) {
        this.context = context;
        this.appExecutors = appExecutors;
    }

    /**
     * 初始化推送SDK
     */
    public void initPushSDK(){

        appExecutors.networkIO().execute(() -> {
            //注册核心服务
            PushManager.getInstance().initialize(context, PushClientService.class);
            //注册推送的回调接收服务
            PushManager.getInstance().registerPushIntentService(context.getApplicationContext(), PushIntentService.class);
        });
//        //注册推送的回调接收服务
//        PushManager.getInstance().registerPushIntentService(context.getApplicationContext(), PushIntentService.class);
    }

    /**
     * 开关推送
     * @param on
     */
    public void togglePushService(boolean on){
        if(on){
            if(!PushManager.getInstance().isPushTurnedOn(context.getApplicationContext())){
                PushManager.getInstance().turnOnPush(context.getApplicationContext());
            }
        }else {
            if(PushManager.getInstance().isPushTurnedOn(context.getApplicationContext())){
                PushManager.getInstance().turnOffPush(context.getApplicationContext());
            }
        }
    }

    /**
     * 关闭推送SDK不连网，
     * @param beginHour 开始时间，设置范围在0-23小时之间，单位 h
     * @param durationHour  持续时间，设置范围在0-23小时之间。持续时间为0则不静默，单位 h
     * @return 是否成功
     */
    public boolean setPushSleepTime(int beginHour,int durationHour){

       return PushManager.getInstance().setSilentTime(context.getApplicationContext(), beginHour, durationHour);
    }

}
