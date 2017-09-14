package com.sire.hefeilife.Controller;

import android.arch.lifecycle.ViewModelProvider;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.Utils.AutoClearedValue;
import com.sire.hefeilife.R;
import com.sire.hefeilife.databinding.ControllerMainBinding;
import com.sire.mediators.MessagePushModuleInterface.MessagePushMediator;
import com.sire.mediators.ShareModuleInterface.ShareMediator;
import com.sire.mediators.UpgradeModuleInterface.UpgradeMediator;
import com.sire.mediators.core.ActiveState;
import com.sire.mediators.core.CallBack;
import com.sire.messagepushmodule.ViewModel.MessagePushViewModel;

import javax.inject.Inject;

public class MainController extends SireController {
    @Inject
    ViewModelProvider.Factory factory;
    @Inject
    ShareMediator shareMediator;
    @Inject
    UpgradeMediator upgradeMediator;
    @Inject
    MessagePushMediator messagePushMediator;
    private AutoClearedValue<ControllerMainBinding> binding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ControllerMainBinding controllerLoginBinding = DataBindingUtil.setContentView(this, R.layout.controller_main);
        binding = new AutoClearedValue<>(this, controllerLoginBinding);
        binding.get().setContext(this);
//        shareMediator.smsVerify(data -> System.out.println("========" + data));
//        upgradeMediator.checkVersion(data -> {
//            System.out.println("======="+data.toString());
//        });
        messagePushMediator.initPushSDK();
    }


    public void onClick(View view) {


//        String downloadUrl = "http://localhost:8080/upgrade/downloadapp/com.cutt.zhiyue.android.app965004_1533.apk";
//        UpgradeInfor upgradeInfor = new UpgradeInfor();
//        upgradeInfor.setDownloadUrl(downloadUrl);
//        upgradeInfor.setVersionName("test-3-5");
//        appUpgradeViewModel.upgradeAppVersion(this,upgradeInfor);

    }
}
