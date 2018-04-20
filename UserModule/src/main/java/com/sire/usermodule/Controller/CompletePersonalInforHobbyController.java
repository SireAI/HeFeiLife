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

import static com.sire.corelibrary.Controller.Segue.FOR_RESULT_REQUEST_CODE;
import static com.sire.usermodule.Constant.Constant.LOGIN_REQUEST_CODE;
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
        segueToCompletePersonalInforController();
    }

    public void onNext(View view){
        setResult(LOGIN_REQUEST_CODE);
        finishActivity(LOGIN_REQUEST_CODE);
        finish();
    }

    private void segueToCompletePersonalInforController() {
//        Intent intent = new Intent(this,CompletePersonalInforController.class);
//        intent.putExtra(FOR_RESULT_REQUEST_CODE, LOGIN_REQUEST_CODE);
////        intent.putExtra(SEX,id);
//        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_REQUEST_CODE && resultCode == LOGIN_REQUEST_CODE){
            setResult(LOGIN_REQUEST_CODE);

            finish();
        }
    }
}
