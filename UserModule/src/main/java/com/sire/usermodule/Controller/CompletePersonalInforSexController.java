package com.sire.usermodule.Controller;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

import com.sire.corelibrary.Controller.Segue;
import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.Utils.SnackbarUtils;
import com.sire.usermodule.R;
import com.sire.usermodule.View.ProgressHUD;
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

public class CompletePersonalInforSexController extends SireController{
    private UserViewModel userViewModel;
    @Inject
    ViewModelProvider.Factory factory;
    private CoordinatorLayout clSex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller_complete_personalinfor_sex);
        setSupportActionBar(findViewById(R.id.toolbar));

        clSex = findViewById(R.id.cl_sex);
        userViewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);

    }

    public void onSkip(View view){

    }

    public void onSex(View view){
        String sex = "女";
        if(view.getId() == R.id.male){
            sex = "男";
        }
        view.setTag(sex);
        userViewModel.updateSex(sex,userViewModel.getUserId(this)).observe(this, dataResource -> {
            switch (dataResource.status) {
                case LOADING:
                    ProgressHUD.showDialog(CompletePersonalInforSexController.this);
                    break;
                case SUCCESS:
                    ProgressHUD.close();
                    segueToCompletePersonalInforBirthdayController((String)view.getTag());
                    break;
                case ERROR:
                    ProgressHUD.close();
                    SnackbarUtils.basicSnackBar(clSex, dataResource.message, CompletePersonalInforSexController.this);
                    break;
                default:
                    break;
            }
        });
    }

    private void segueToCompletePersonalInforBirthdayController(String sex) {
        Intent intent = new Intent(this,CompletePersonalInforBirthdayController.class);
        intent.putExtra(SEX,sex);
        segue(Segue.SegueType.PUSH,intent);
    }

}
