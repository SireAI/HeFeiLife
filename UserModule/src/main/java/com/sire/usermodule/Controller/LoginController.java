package com.sire.usermodule.Controller;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.Utils.AutoClearedValue;
import com.sire.usermodule.DB.Entry.User;
import com.sire.usermodule.R;
import com.sire.usermodule.ViewModel.UserViewModel;
import com.sire.usermodule.databinding.ControllerLoginBinding;

import javax.inject.Inject;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/7/24
 * Author:sire
 * Description:
 * ==================================================
 */
public class LoginController extends SireController {

    private static final String TAG = "LoginController";
    @Inject
    ViewModelProvider.Factory factory;

    private AutoClearedValue<ControllerLoginBinding> binding;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ControllerLoginBinding controllerLoginBinding = DataBindingUtil.setContentView(this, R.layout.controller_login);
        binding = new AutoClearedValue<>(this, controllerLoginBinding);
        binding.get().setUser(new User("1", "测试六", "32"));
        binding.get().setContext(this);

        userViewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);
        userViewModel.getLoginResult().observe(this, userDataResource -> System.out.println("通知Controller的数据:" + userDataResource.toString()));

    }

    /**
     * onclick event
     *
     * @param view
     */
    public void onClick(View view) {
        userViewModel.doLogin(binding.get().getUser());
    }


}

