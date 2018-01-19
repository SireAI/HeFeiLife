package com.sire.upgrademodule.Mediator;

import android.support.v4.app.FragmentActivity;

import com.sire.mediators.UpgradeModuleInterface.UpgradeCallback;
import com.sire.mediators.UpgradeModuleInterface.UpgradeMediator;
import com.sire.mediators.core.ActiveState;
import com.sire.mediators.core.CallBack;
import com.sire.upgrademodule.ViewMoudle.AppUpgradeViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/11
 * Author:Sire
 * Description:
 * ==================================================
 */
public class UpgradeMediatorImpl implements UpgradeMediator {
    private final AppUpgradeViewModel appUpgradeViewModel;
    @Inject
    public UpgradeMediatorImpl(AppUpgradeViewModel appUpgradeViewModel) {
        this.appUpgradeViewModel = appUpgradeViewModel;
    }


    @Override
    public void checkVersion(Object context) {
        if(context instanceof FragmentActivity){
            this.appUpgradeViewModel.checkVersion((FragmentActivity)context);
        }else {
            throw new RuntimeException("context 必须是FragmentActivity对象");
        }
    }
}
