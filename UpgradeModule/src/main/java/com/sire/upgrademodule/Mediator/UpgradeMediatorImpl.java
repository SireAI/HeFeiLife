package com.sire.upgrademodule.Mediator;

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
@Singleton
public class UpgradeMediatorImpl implements UpgradeMediator {
    private final AppUpgradeViewModel appUpgradeViewModel;
    @Inject
    public UpgradeMediatorImpl(AppUpgradeViewModel appUpgradeViewModel) {
        this.appUpgradeViewModel = appUpgradeViewModel;
    }


    /**
     * 成功表示需要升级，失败表示不需要升级或者错误
     * @param callBack
     */
    @Override
    public void checkVersion(CallBack<ActiveState> callBack) {
        appUpgradeViewModel.checkVersion(callBack);
    }
}
