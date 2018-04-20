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
import com.sire.corelibrary.View.ToastSuccess;
import com.sire.mediators.ShareModuleInterface.ShareMediator;
import com.sire.usermodule.R;
import com.sire.usermodule.Utils.TimeUtils;
import com.sire.usermodule.ViewModel.UserViewModel;
import com.sire.usermodule.databinding.ControllerRegisterVerifycodeBinding;

import javax.inject.Inject;

import timber.log.Timber;

import static com.sire.corelibrary.Controller.Segue.FOR_RESULT_REQUEST_CODE;
import static com.sire.usermodule.Constant.Constant.LOGIN_REQUEST_CODE;
import static com.sire.usermodule.Constant.Constant.PHONENUMBER;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/10/17
 * Author:Sire
 * Description:
 * ==================================================
 */

public class RegisterVerifyCodeController extends SireController implements TimeUtils.CountDownDelegate {
    private static int RETRAY_INTERVAL = 60;
    @Inject
    ViewModelProvider.Factory factory;
    @Inject
    ShareMediator shareMediator;
    @Inject
    AppExecutors appExecutors;
    private AutoClearedValue<ControllerRegisterVerifycodeBinding> binding;
    private boolean verifycodeReady = false;
    private boolean passwordReady = false;
    private UserViewModel userViewModel;
    private String phoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ControllerRegisterVerifycodeBinding controllerRegisterVerifycodeBinding = DataBindingUtil.setContentView(this, R.layout.controller_register_verifycode);
        binding = new AutoClearedValue<>(this, controllerRegisterVerifycodeBinding);
        controllerRegisterVerifycodeBinding.setContext(this);
        setActionBarEnabled(controllerRegisterVerifycodeBinding.toolbar);
        phoneNumber = getPhoneNumber();
        String regPhoneNumber = regPhoneNumber(phoneNumber);
        controllerRegisterVerifycodeBinding.tvPhoneNumber.setText(regPhoneNumber);
        controllerRegisterVerifycodeBinding.btnRegister.setEnabled(verifycodeReady && passwordReady);
        userViewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);

        SMSCountDown();
    }

    private void SMSCountDown() {
        getSMS();
        setRetrayState(false);
        TimeUtils.countDownTime(this, RETRAY_INTERVAL);
    }


    private void getSMS() {
        shareMediator.sendSMS(phoneNumber, data ->
                appExecutors.mainThread().execute(() ->
                        smshandler(data)));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == LOGIN_REQUEST_CODE && resultCode == LOGIN_REQUEST_CODE){
            setResult(LOGIN_REQUEST_CODE);
            finish();
        }
    }
    private void smshandler(String data) {

        if (data.equals("success")) {
            Timber.d("校验码验证成功");
            String password = binding.get().tiePassword.getText().toString();
            userViewModel.getRegisterResult().observe(RegisterVerifyCodeController.this, userDataResource -> {
                switch (userDataResource.status) {
                    case SUCCESS:
                        ProgressHUD.close();
                        ToastSuccess.showDialog(RegisterVerifyCodeController.this, () -> {
                            userViewModel.saveUserState(RegisterVerifyCodeController.this, userDataResource.data);
                            segueToCompletePersonalInforController();
                        });
                        break;
                    case ERROR:
                        ProgressHUD.close();
                        SnackbarUtils.basicSnackBar(binding.get().clRegister, userDataResource.message, RegisterVerifyCodeController.this);
                        break;
                    case LOADING:
                        break;
                    default:
                        break;
                }
            });
            userViewModel.register(phoneNumber, password);
        } else {
            ProgressHUD.close();
            SnackbarUtils.basicSnackBar(binding.get().clRegister, data, RegisterVerifyCodeController.this);
        }
    }

    private void segueToCompletePersonalInforController() {
        Intent intent = new Intent(this, CompletePersonalInforController.class);
        intent.putExtra(FOR_RESULT_REQUEST_CODE, LOGIN_REQUEST_CODE);
        segueForResult(Segue.SegueType.MODAL, intent);
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
            onRegister(textView);
            return true;
        }
        return false;
    }

    public void onPasswordTextChanged(CharSequence password, int start, int before, int count) {
        passwordReady = password.toString().length() >= 6;
        binding.get().btnRegister.setEnabled(verifycodeReady && passwordReady);
    }

    public void onVerifyCodeTextChanged(CharSequence verifycode, int start, int before, int count) {
        verifycodeReady = verifycode.toString().length() > 0;
        binding.get().btnRegister.setEnabled(verifycodeReady && passwordReady);

    }

    public void onRegister(View view) {
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
