package com.sire.corelibrary.DI.Environment;

import io.reactivex.Flowable;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/12/05
 * Author:Sire
 * Description:
 * ==================================================
 */

public interface ModuleInit {
     Flowable<ModuleInitInfor> init();
}
