package com.sire.hefeilife.ModuleInit.APPInitManager;

import com.sire.corelibrary.Lifecycle.ModuleLife.ModuleInitService;
import com.sire.mediators.UserModuleInterface.UserInfor;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/21
 * Author:Sire
 * Description:
 * ==================================================
 */

public class APPInitActionManager {
    @Inject
    ModuleInitService<UserInfor> userInforModuleInitService;

//    @Inject
//    ModuleInitService<CommunityInfor> communityInforModuleInitService;

    public APPInitActionManager() {

    }

    public <T> Observable<T> initService(ModuleInitService... services) {
//        System.out.println("userInforModuleInitService:"+userInforModuleInitService);
//        System.out.println("communityInforModuleInitService:"+communityInforModuleInitService);
//        ModuleInitService<UserInfor> userModuleInitService = APPInitService.getModuleInitServiceFrom(Module.USER_MODULE);
//        ModuleInitService<UserInfor> communityModuleInitService = APPInitService.getModuleInitServiceFrom(Module.USER_MODULE);
//        System.out.println("======"+communityModuleInitService);
//        userModuleInitService.a
//        Observable<UserInfor> userInforObservable = userModuleInitService.moduleInitAction();
//        Observable<UserInfor> userInforObservable = userInforModuleInitService.moduleInitAction();
        return null;
    }
}
