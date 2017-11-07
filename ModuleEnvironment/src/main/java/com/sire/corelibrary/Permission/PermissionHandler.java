package com.sire.corelibrary.Permission;

import android.Manifest;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.sire.corelibrary.Controller.SireController;
import com.zhy.m.permission.MPermissions;

import java.util.HashMap;
import java.util.Map;



/**
 * ==================================================
 * All Right Reserved
 * Date:2017/01/06
 * Author:Sire
 * Description:
 * ==================================================
 */

public class PermissionHandler {
    public static final int REQUECT_CODE_BASIC_PERMISSIONS = 100;
    private final String[] permissions;
    private String instructPermission = "";
    public PermissionHandler(String[] permissions) {
        this.permissions = permissions;
    }
    private static Map<String,String> instructions = new HashMap<>();

    static {
        instructions.put(Manifest.permission.WRITE_EXTERNAL_STORAGE,"存储读写权限");
        instructions.put(Manifest.permission.READ_EXTERNAL_STORAGE,"存储读写权限");
        instructions.put(Manifest.permission.CAMERA,"开启照相权限");
    }

    /**
     * permission request
     * @param needRationale wheather via instuction
     */
    public void requestPermissions(SireController controller, boolean needRationale) {
        if(permissions!=null&&permissions.length>0){
            if(needRationale){
                for (int i = 0; i < permissions.length; i++) {
                    instructPermission = permissions[i];
                    if(MPermissions.shouldShowRequestPermissionRationale(controller,permissions[i],REQUECT_CODE_BASIC_PERMISSIONS)){
                        return;
                    }
                }
                if(!MPermissions.shouldShowRequestPermissionRationale(controller,permissions[0],REQUECT_CODE_BASIC_PERMISSIONS)){
                    MPermissions.requestPermissions(controller, REQUECT_CODE_BASIC_PERMISSIONS, permissions);
                }
            }else {
                MPermissions.requestPermissions(controller, REQUECT_CODE_BASIC_PERMISSIONS, permissions);
            }

        }
    }

    public void showPermissionInstructions(View view){
        Snackbar.make(view, String.format("安卓6.0以上系统需要授予%s权限才能使用对应功能",instructions.get(instructPermission)), Snackbar.LENGTH_INDEFINITE).setAction("确定", new View.OnClickListener() {
            /**
             * Called when a view has been clicked.
             *
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                requestPermissions((SireController) view.getContext(),false);
            }
        }).show();
    }

    public void onRequestPermissionsResult(SireController controller, int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(controller, requestCode, permissions, grantResults);
    }

}
