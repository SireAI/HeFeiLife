package com.sire.usermodule.Controller;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.sire.corelibrary.Controller.Segue;
import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.Utils.APPUtils;
import com.sire.corelibrary.Utils.DialogUtils;
import com.sire.usermodule.R;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import static com.sire.corelibrary.Controller.Segue.FOR_RESULT_REQUEST_CODE;
import static com.sire.corelibrary.Permission.PermissionHandler.REQUECT_CODE_BASIC_PERMISSIONS;
import static com.sire.usermodule.Constant.Constant.LOGIN_REQUEST_CODE;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/11
 * Author:Sire
 * Description:
 * ==================================================
 */

public class EntranceController extends SireController {
    private String actionType = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller_entrance);
    }


    @PermissionGrant(REQUECT_CODE_BASIC_PERMISSIONS)
    public void requestPermisssionSuccess() {
        if ("doLogin".equals(actionType)) {
            Intent intent = new Intent(this, LoginController.class);
            intent.putExtra(FOR_RESULT_REQUEST_CODE, LOGIN_REQUEST_CODE);
            segueForResult(Segue.SegueType.MODAL, intent);
        } else if ("doRegister".equals(actionType)) {
            Intent intent = new Intent(this, RegisterPhoneNumberController.class);
            intent.putExtra(FOR_RESULT_REQUEST_CODE, LOGIN_REQUEST_CODE);
            segueForResult(Segue.SegueType.MODAL, intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_REQUEST_CODE && resultCode == LOGIN_REQUEST_CODE){
            finish();
        }
    }

    @PermissionDenied(REQUECT_CODE_BASIC_PERMISSIONS)
    public void requestPermisssionFailed() {
        DialogUtils.showDialog(this, getResources().getString(R.string.permission_attention), (dialogInterface, i) -> {
            dialogInterface.dismiss();
            Intent appDetailSettingIntent = APPUtils.getAppDetailSettingIntent(EntranceController.this);
            startActivity(appDetailSettingIntent);
        });
    }


    public void doLogin(View view) {
        actionType = "doLogin";
        requestPermission();
    }

    private void requestPermission() {
        requestNeedPermissions(new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE});

    }

    public void doRegister(View view) {
        actionType = "doRegister";
        requestPermission();
    }
}
