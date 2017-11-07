package com.sire.usermodule.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.sire.corelibrary.Controller.Segue;
import com.sire.corelibrary.Controller.SireController;
import com.sire.usermodule.R;

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
    public void onSkip(View view){

    }
    public void onComplete(View view){
        segueToCompletePersonalInforPhotoController();
    }

    private void segueToCompletePersonalInforPhotoController() {
        Intent intent = new Intent(this, CompletePersonalInforPhotoController.class);
        segue(Segue.SegueType.PUSH,intent);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
