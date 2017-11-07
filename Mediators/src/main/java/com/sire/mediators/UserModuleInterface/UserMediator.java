package com.sire.mediators.UserModuleInterface;

public interface UserMediator {
    UserInfor fetchUserInfor();
    void segueToLoginController(Object context);
    void segueToEntranceController(Object context);
    String getUserId();
}
