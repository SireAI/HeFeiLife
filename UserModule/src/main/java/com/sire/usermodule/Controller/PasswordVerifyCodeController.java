package com.sire.usermodule.Controller;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.sire.corelibrary.Controller.Segue;
import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.Executors.AppExecutors;
import com.sire.corelibrary.Utils.AutoClearedValue;
import com.sire.corelibrary.Utils.SnackbarUtils;
import com.sire.corelibrary.View.ProgressHUD;
import com.sire.mediators.ShareModuleInterface.ShareMediator;
import com.sire.usermodule.R;
import com.sire.usermodule.Utils.TimeUtils;
import com.sire.usermodule.ViewModel.UserViewModel;
import com.sire.usermodule.databinding.ControllerPasswordVerifycodeBinding;

import javax.inject.Inject;

import timber.log.Timber;

import static com.sire.usermodule.Constant.Constant.PHONENUMBER;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/17
 * Author:Sire
 * Description:
 * ==================================================
 */

public class PasswordVerifyCodeController extends SireController implements TimeUtils.CountDownDelegate {
    private static int RETRAY_INTERVAL = 60;
    @Inject
    ViewModelProvider.Factory factory;
    @Inject
    ShareMediator shareMediator;
    @Inject
    AppExecutors appExecutors;
    private AutoClearedValue<ControllerPasswordVerifycodeBinding> binding;
    private boolean verifycodeReady = false;
    private UserViewModel userViewModel;
    private String phoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ControllerPasswordVerifycodeBinding controllerPasswordVerifycodeBinding = DataBindingUtil.setContentView(this, R.layout.controller_password_verifycode);
        binding = new AutoClearedValue<>(this, controllerPasswordVerifycodeBinding);
        controllerPasswordVerifycodeBinding.setContext(this);
        setActionBarEnabled(controllerPasswordVerifycodeBinding.toolbar);
        phoneNumber = getPhoneNumber();
        String regPhoneNumber = regPhoneNumber(phoneNumber);
        controllerPasswordVerifycodeBinding.tvPhoneNumber.setText(regPhoneNumber);
        controllerPasswordVerifycodeBinding.btnRegister.setEnabled(verifycodeReady);
        userViewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);

        SMSCountDown();
    }

    private void SMSCountDown() {
        getSMS();
        setRetrayState(false);
        TimeUtils.countDownTime(this, RETRAY_INTERVAL);
    }


    private void getSMS() {
        shareMediator.sendSMS(phoneNumber, data -> {
            //提交验证码后会调用此回调
            appExecutors.mainThread().execute(() ->
                    smshandler(data));
        });
    }

    private void smshandler(String data) {
        ProgressHUD.close();
        if ("success".equals(data)) {
            Timber.d("校验码验证成功");
            segueToPasswordResetController();
        } else {
            SnackbarUtils.basicSnackBar(binding.get().clRegister, data, PasswordVerifyCodeController.this);
        }
    }

    private void segueToPasswordResetController() {
        Intent intent = new Intent(this, PasswordResetController.class);
        intent.putExtra(PHONENUMBER,getPhoneNumber());
        segue(Segue.SegueType.PUSH, intent);
    }

    private String regPhoneNumber(String phoneNumber) {
        StringBuilder regPhone = new StringBuilder();
        for (int i = 0; i < phoneNumber.length(); i++) {
            char charStr = phoneNumber.charAt(i);
            regPhone.append(charStr);
            if (i == 2 || i == 6) {
                regPhone.append(' ');
            }
        }
        return regPhone.toString();
    }

    public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == 6 || id == EditorInfo.IME_NULL && binding.get().btnRegister.isEnabled()) {
            onVerify(textView);
            return true;
        }
        return false;
    }


    public void onVerifyCodeTextChanged(CharSequence verifycode, int start, int before, int count) {
        verifycodeReady = verifycode.toString().length() > 0;
        binding.get().btnRegister.setEnabled(verifycodeReady);
    }

    public void onVerify(View view) {
        String verifycode = binding.get().tieVerifycode.getText().toString();
        shareMediator.submitVerifyCode(phoneNumber, verifycode);
        ProgressHUD.showDialog(this);
    }

    private String getPhoneNumber() {
        Intent intent = getIntent();
        String phoneNumber = intent.getStringExtra(PHONENUMBER);
        return phoneNumber;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TimeUtils.stopCountDown();
    }

    @Override
    public void countDown(int when) {
        String countText = "";
        if (when == 0) {
            countText = "重新发送";
        } else {
            countText = "重新发送" + "(" + when + ")";
        }
        if (binding != null && binding.get() != null && binding.get().tvRetray != null) {
            binding.get().tvRetray.setText(countText);
            setRetrayState(when == 0);
        }

    }

    public void setRetrayState(boolean enabled) {
        binding.get().tvRetray.setEnabled(enabled);
        binding.get().tvRetray.setTextColor(enabled ? getResources().getColor(R.color.colorPrimary) : getResources().getColor(R.color.main_bg_2_text));
    }

    public void onRetray(View view) {
        SMSCountDown();
    }
}
