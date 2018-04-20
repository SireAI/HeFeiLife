package com.sire.usermodule.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.sire.corelibrary.Controller.Segue;
import com.sire.corelibrary.Controller.SireController;
import com.sire.usermodule.R;

import static com.sire.corelibrary.Controller.Segue.FOR_RESULT_REQUEST_CODE;
import static com.sire.usermodule.Constant.Constant.LOGIN_REQUEST_CODE;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/18
 * Author:Sire
 * Description:
 * ==================================================
 */

public class CompletePersonalInforController extends SireController {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller_complete_personalinfor);
    }

    private void closePreviouseController() {
        setResult(LOGIN_REQUEST_CODE);
        finishActivity(LOGIN_REQUEST_CODE);
    }


    @Override
    protected void onResume() {
        super.onResume();
        closePreviouseController();
    }

    public void onComplete(View view){
        segueToCompletePersonalInforPhotoController();
    }

    private void segueToCompletePersonalInforPhotoController() {
        Intent intent = new Intent(this, CompletePersonalInforPhotoController.class);
        intent.putExtra(FOR_RESULT_REQUEST_CODE, LOGIN_REQUEST_CODE);
        segueForResult(Segue.SegueType.PUSH,intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_REQUEST_CODE && resultCode == LOGIN_REQUEST_CODE){
            finish();
        }
    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
