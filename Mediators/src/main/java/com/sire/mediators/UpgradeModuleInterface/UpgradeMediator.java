package com.sire.mediators.UpgradeModuleInterface;

import com.sire.mediators.core.ActiveState;
import com.sire.mediators.core.CallBack;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/11
 * Author:Sire
 * Description:
 * ==================================================
 */

public interface UpgradeMediator {
    /**
     * check version
     * @param context  fragmentActivity
     */
    void checkVersion(Object context);
}
