package com.sire.usermodule.Controller;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
import static com.sire.usermodule.Constant.Constant.PHONENUMBER;
import static com.sire.usermodule.Constant.Constant.PHONE_REG;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/24
 * Author:Sire
 * Description:
 * ==================================================
 */

public class PasswordPhonenumberController extends SireController implements View.OnClickListener, TextWatcher, TextView.OnEditorActionListener {
    private Button btnNext;
    private TextInputLayout til;
    private EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.controller_password_phonenumber);
        editText = findViewById(R.id.tie_phonenumber);
        til = findViewById(R.id.til_phonenumber);
        btnNext = findViewById(R.id.btn_next);
        btnNext.setEnabled(false);
        btnNext.setOnClickListener(this);
        editText.addTextChangedListener(this);
        editText.setOnEditorActionListener(this);
        setActionBarEnabled(findViewById(R.id.toolbar));

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence phonenumber, int i, int i1, int i2) {
        if (phonenumber.toString().length() == 11) {
            String message = "";
            if (!phonenumber.toString().matches(PHONE_REG)) {
                message = "手机号格式不对";
                btnNext.setEnabled(false);
            } else {
                btnNext.setEnabled(true);
            }
            til.setError(message);
        } else {
            til.setError("");
            btnNext.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onClick(View view) {
        requestNeedPermissions(new String[]{
                Manifest.permission.SEND_SMS
                ,Manifest.permission.RECEIVE_SMS
                ,Manifest.permission.READ_SMS});


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_REQUEST_CODE && resultCode == LOGIN_REQUEST_CODE){
            setResult(LOGIN_REQUEST_CODE);
            finish();
        }
    }

    @PermissionGrant(REQUECT_CODE_BASIC_PERMISSIONS)
    public void requestPermisssionSuccess() {
        Intent intent = new Intent(this, PasswordVerifyCodeController.class);
        intent.putExtra(PHONENUMBER,editText.getText().toString());
        intent.putExtra(FOR_RESULT_REQUEST_CODE, LOGIN_REQUEST_CODE);
        segueForResult(Segue.SegueType.PUSH, intent);
    }


    @PermissionDenied(REQUECT_CODE_BASIC_PERMISSIONS)
    public void requestPermisssionFailed() {
        DialogUtils.showDialog(this, getResources().getString(R.string.permission_attention), (dialogInterface, i) -> {
            dialogInterface.dismiss();
            Intent appDetailSettingIntent = APPUtils.getAppDetailSettingIntent(PasswordPhonenumberController.this);
            startActivity(appDetailSettingIntent);
        });
    }

    @Override
    public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == 6 || id == EditorInfo.IME_NULL && btnNext.isEnabled()) {
            onClick(textView);
            return true;
        }
        return false;    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
