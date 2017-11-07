package com.sire.usermodule.Mediator;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.sire.mediators.UserModuleInterface.UserInfor;
import com.sire.mediators.UserModuleInterface.UserMediator;
import com.sire.usermodule.Controller.EntranceController;
import com.sire.usermodule.Controller.LoginController;
import com.sire.usermodule.ViewModel.UserViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/21
 * Author:Sire
 * Description:
 * ==================================================
 */
@Singleton
 public class UserMediatorImpl implements UserMediator {
    private final UserViewModel userViewModel;
    private final Application app;

    @Inject
    public UserMediatorImpl(UserViewModel userViewModel, Application app) {
        this.userViewModel = userViewModel;
        this.app = app;
    }

    @Override
    public UserInfor fetchUserInfor() {
        UserInfor userInfor = new UserInfor();
        userInfor.setName("sire");
        userInfor.setPwd("123");
        return userInfor;
    }

    @Override
    public void segueToLoginController(Object context) {
        Activity currentContext = (Activity) context;
        Intent intent = new Intent(currentContext, LoginController.class);
        (currentContext).startActivity(intent);
        currentContext.finish();
    }

    @Override
    public void segueToEntranceController(Object context) {
        Activity currentContext = (Activity) context;
        Intent intent = new Intent(currentContext, EntranceController.class);
        (currentContext).startActivity(intent);
        currentContext.finish();
    }

    @Override
    public String getUserId() {
        return userViewModel.getUserId(app);
    }
}
