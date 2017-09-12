package com.sire.usermodule.Mediator;

import com.sire.mediators.UserModuleInterface.UserInfor;
import com.sire.mediators.UserModuleInterface.UserMediator;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/21
 * Author:Sire
 * Description:
 * ==================================================
 */

 public class UserMediatorImpl implements UserMediator {
    @Override
    public UserInfor fetchUserInfor() {
        UserInfor userInfor = new UserInfor();
        userInfor.setName("sire");
        userInfor.setPwd("123");
        return userInfor;
    }

}
