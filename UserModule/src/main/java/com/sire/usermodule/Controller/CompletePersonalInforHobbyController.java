package com.sire.usermodule.Controller;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import com.sire.corelibrary.Controller.SireController;
import com.sire.usermodule.R;
import com.sire.usermodule.ViewModel.UserViewModel;

import javax.inject.Inject;

import static com.sire.usermodule.Constant.Constant.SEX;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/20
 * Author:Sire
 * Description:
 * ==================================================
 */

public class CompletePersonalInforHobbyController extends SireController{
    private UserViewModel userViewModel;
    @Inject
    ViewModelProvider.Factory factory;
    private CoordinatorLayout clSex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller_complete_personalinfor_hobby);
        clSex = findViewById(R.id.cl_sex);
        setSupportActionBar(findViewById(R.id.toolbar));

        userViewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);

    }

    public void onSkip(View view){

    }

    public void onNext(View view){

    }

    private void segueToCompletePersonalInforBirthdayController(int id) {
        Intent intent = new Intent(this,CompletePersonalInforBirthdayController.class);
        intent.putExtra(SEX,id);
        startActivity(intent);
    }

}
