package com.sire.mediators.UserModuleInterface;

import com.sire.mediators.core.CallBack;

public interface UserMediator  {
    void segueToLoginController(Object context);
    void segueToEntranceController(Object context);
    void segueToPersonalProfileController(Object context);
    Object getPersonalInforController();
    String getUserId();

    String getUserImage();


    /**
     * 登陆状态判断
     *
     * @param context
     * @param needLogin 如果未登录，是否自动去登陆页面
     * @return 是否处于登陆状态
     */
    boolean isLoginState(Object context, boolean needLogin);

    String getUserLevel();

    String getUserName();


    String getUserCurrentAddress();

    String getPhoneNumber();

}
