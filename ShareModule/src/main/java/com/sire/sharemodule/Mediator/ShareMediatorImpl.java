package com.sire.sharemodule.Mediator;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;

import com.sire.mediators.ShareModuleInterface.ShareMediator;
import com.sire.mediators.core.CallBack;
import com.sire.sharemodule.ViewModule.SMSModel;
import com.sire.sharemodule.ViewModule.ThirdPartyLoginModel;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/07
 * Author:Sire
 * Description:
 * ==================================================
 */
@Singleton
public class ShareMediatorImpl implements ShareMediator {

    private ThirdPartyLoginModel thirdPartyLoginModel;

    private SMSModel smsModel;

    @Inject
    public ShareMediatorImpl(ThirdPartyLoginModel thirdPartyLoginModel, SMSModel smsModel) {
        this.thirdPartyLoginModel = thirdPartyLoginModel;
        this.smsModel = smsModel;
    }

    @Override
    public void thirdPartyLogin(String platformName, CallBack<Object[]> objects) {
        thirdPartyLoginModel.thirdPartyLogin(platformName, objects);
    }

    @Override
    public boolean isPlatformExist(String platformName) {
        return thirdPartyLoginModel.isPlatformExist(platformName);
    }

    @Override
    public void smsVerify(CallBack<String> callBack) {
        smsModel.phoneCodeVerify(callBack);
    }

    @Override
    public void unRegisterSMS() {
        smsModel.unregister();
    }
}
