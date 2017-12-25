package com.sire.usermodule.Mediator;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;

import com.sire.corelibrary.Controller.Segue;
import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.DI.Environment.ModuleInit;
import com.sire.corelibrary.DI.Environment.ModuleInitInfor;
import com.sire.corelibrary.Executors.AppExecutors;
import com.sire.mediators.UserModuleInterface.UserMediator;
import com.sire.mediators.core.CallBack;
import com.sire.usermodule.Controller.EntranceController;
import com.sire.usermodule.Controller.LoginController;
import com.sire.usermodule.ViewModel.UserViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Flowable;

import static com.sire.usermodule.Constant.Constant.LOGIN_REQUEST_CODE;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/21
 * Author:Sire
 * Description:
 * ==================================================
 */
@Singleton
 public class UserMediatorImpl implements UserMediator,ModuleInit {
    private final UserViewModel userViewModel;
    private final Application app;
    private final AppExecutors appExecutors;

    @Inject
    public UserMediatorImpl(UserViewModel userViewModel, Application app, AppExecutors appExecutors) {
        this.userViewModel = userViewModel;
        this.app = app;
        this.appExecutors = appExecutors;
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
        return userViewModel.getUserId();
    }

    @Override
    public String getUserImage() {
        return userViewModel.getUserImage();
    }


    @Override
    public boolean isLoginState(Object context, boolean needLogin) {
        boolean notInLoginState = TextUtils.isEmpty(userViewModel.getUserId());
        if(notInLoginState&&needLogin){
            appExecutors.mainHandler().postDelayed(() -> {
                Intent intent = new Intent(app, EntranceController.class);
                intent.putExtra(Segue.FOR_RESULT_REQUEST_CODE,LOGIN_REQUEST_CODE);
                ((SireController)context).segueForResult(Segue.SegueType.MODAL,intent);

            },1);
        }
        return !notInLoginState;
    }

    @Override
    public String getUserLevel() {
        return userViewModel.getUserLevel();
    }

    @Override
    public String getUserName() {
        return userViewModel.getUserName();
    }



    @Override
    public String getUserCurrentAddress() {
        return userViewModel.getUserCurrentAddress();
    }

    @Override
    public Flowable<ModuleInitInfor> init() {
        return userViewModel.initUserState(app);
    }
}
