package com.sire.usermodule.Controller;

import android.arch.lifecycle.Observer;
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

import com.sire.corelibrary.Controller.SireController;
import com.sire.corelibrary.Networking.dataBound.DataResource;
import com.sire.corelibrary.Utils.AutoClearedValue;
import com.sire.corelibrary.Utils.SnackbarUtils;
import com.sire.corelibrary.View.ProgressHUD;
import com.sire.corelibrary.View.ToastSuccess;
import com.sire.usermodule.DB.Entry.User;
import com.sire.usermodule.R;
import com.sire.usermodule.ViewModel.UserViewModel;
import com.sire.usermodule.databinding.ControllerPasswordResetBinding;

import javax.inject.Inject;

import timber.log.Timber;

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

public class PasswordResetController extends SireController  {
    @Inject
    ViewModelProvider.Factory factory;

    private AutoClearedValue<ControllerPasswordResetBinding> binding;
    private boolean newPasswordReady = false;
    private boolean confirmPasswordReady = false;
    private UserViewModel userViewModel;
    private String phoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ControllerPasswordResetBinding passwordResetBinding = DataBindingUtil.setContentView(this, R.layout.controller_password_reset);
        binding = new AutoClearedValue<>(this, passwordResetBinding);
        passwordResetBinding.setContext(this);
        setActionBarEnabled(passwordResetBinding.toolbar);
        phoneNumber = getPhoneNumber();
        String regPhoneNumber = regPhoneNumber(phoneNumber);
        passwordResetBinding.tvPhoneNumber.setText(regPhoneNumber);
        passwordResetBinding.btnResetLogin.setEnabled(newPasswordReady && confirmPasswordReady);
        userViewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);

    }





    private void segueToPreviousController() {
        setResult(LOGIN_REQUEST_CODE);
        finishActivity(LOGIN_REQUEST_CODE);
        finish();
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
        if ((id == 6 || id == EditorInfo.IME_NULL) && binding.get().btnResetLogin.isEnabled()) {
            onResetAndLogin(textView);
            return true;
        }
        return false;
    }

    public void onPasswordTextChanged(CharSequence password, int start, int before, int count) {
        confirmPasswordReady = password.toString().length() >= 6;
        binding.get().btnResetLogin.setEnabled(newPasswordReady && confirmPasswordReady);
    }

    public void onVerifyCodeTextChanged(CharSequence verifycode, int start, int before, int count) {
        newPasswordReady = verifycode.toString().length() >= 6;
        binding.get().btnResetLogin.setEnabled(newPasswordReady && confirmPasswordReady);

    }


    private String getPhoneNumber() {
        Intent intent = getIntent();
        String phoneNumber = intent.getStringExtra(PHONENUMBER);
        return phoneNumber;
    }



    public void onResetAndLogin(View view) {
        if(checkPassowrd()){
            String confirmPassword = binding.get().tieConfirmPassword.getText().toString();
            userViewModel.updatePassword(getPhoneNumber(),confirmPassword).observe(this, new Observer<DataResource<User>>() {
                @Override
                public void onChanged(@Nullable DataResource<User> dataResource) {
                    Timber.d("dataResource = [" + dataResource + "]");
                    switch (dataResource.status) {
                        case LOADING:
                            ProgressHUD.showDialog(PasswordResetController.this);
                            break;
                        case SUCCESS:
                            ProgressHUD.close();
                            ToastSuccess.showDialog(PasswordResetController.this, getResources().getString(R.string.reset_success), new ToastSuccess.CallBack() {
                                @Override
                                public void onFinish() {
                                    segueToPreviousController();
                                }
                            });
                            break;
                        case ERROR:
                            ProgressHUD.close();
                            SnackbarUtils.basicSnackBar(binding.get().clPasswordReset, dataResource.message, PasswordResetController.this);
                            break;
                        default:
                            break;
                    }
                }
            });
        }else {
            SnackbarUtils.basicSnackBar(binding.get().clPasswordReset,getResources().getString(R.string.not_same_password),this);
        }
    }

    private boolean checkPassowrd() {
        String newPassword = binding.get().tieNewPassowrd.getText().toString();
        String confirmPassword = binding.get().tieConfirmPassword.getText().toString();
        return newPassword.equals(confirmPassword);
    }
}
