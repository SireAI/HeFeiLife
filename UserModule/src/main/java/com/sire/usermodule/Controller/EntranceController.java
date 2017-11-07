package com.sire.usermodule.Controller;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.sire.corelibrary.Controller.Segue;
import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.Utils.APPUtils;
import com.sire.usermodule.R;
import com.sire.usermodule.Utils.DialogUtils;
import com.zhy.m.permission.PermissionDenied;
import com.zhy.m.permission.PermissionGrant;

import static com.sire.corelibrary.Permission.PermissionHandler.REQUECT_CODE_BASIC_PERMISSIONS;

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
            segue(Segue.SegueType.MODAL, intent);
        } else if ("doRegister".equals(actionType)) {
            Intent intent = new Intent(this, RegisterPhoneNumberController.class);
            segue(Segue.SegueType.MODAL, intent);
        }
    }


    @PermissionDenied(REQUECT_CODE_BASIC_PERMISSIONS)
    public void requestPermisssionFailed() {
        DialogUtils.showDialog(this, getResources().getString(R.string.permission_attention), (dialogInterface, i) -> {
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
