package com.sire.mediators.ShareModuleInterface;

import com.sire.mediators.core.CallBack;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/07
 * Author:Sire
 * Description:
 * ==================================================
 */

public interface ShareMediator {
    void thirdPartyLogin(String platformName,CallBack<Object[]> objects);
    boolean isPlatformExist(String platformName);
    void smsVerify(CallBack<String> callBack);
    void unRegisterSMS();
}
