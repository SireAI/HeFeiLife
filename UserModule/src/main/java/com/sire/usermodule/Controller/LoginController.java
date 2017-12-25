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
import com.sire.corelibrary.Networking.dataBound.DataResource;
import com.sire.corelibrary.Utils.AutoClearedValue;
import com.sire.corelibrary.Utils.SnackbarUtils;
import com.sire.corelibrary.Utils.ToastUtils;
import com.sire.corelibrary.View.ProgressHUD;
import com.sire.usermodule.DB.Entry.User;
import com.sire.usermodule.R;
import com.sire.usermodule.ViewModel.UserViewModel;
import com.sire.usermodule.databinding.ControllerLoginBinding;

import javax.inject.Inject;

import static com.sire.usermodule.Constant.Constant.LOGIN_REQUEST_CODE;
import static com.sire.usermodule.Constant.Constant.PHONE_REG;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/7/24
 * Author:sire
 * Description:
 * ==================================================
 */
public class LoginController extends SireController {

    @Inject
    ViewModelProvider.Factory factory;
    @Inject
    AppExecutors appExecutors;
    private AutoClearedValue<ControllerLoginBinding> binding;
    private UserViewModel userViewModel;
    private boolean phoneNumberReady = false;
    private boolean passwordReady = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ControllerLoginBinding controllerLoginBinding = DataBindingUtil.setContentView(this, R.layout.controller_login);
        binding = new AutoClearedValue<>(this, controllerLoginBinding);
        binding.get().setContext(this);
        setActionBarEnabled(controllerLoginBinding.toolbar);
        userViewModel = ViewModelProviders.of(this, factory).get(UserViewModel.class);
        User currentUser = userViewModel.getCurrentUser();
        if (currentUser == null) {
            currentUser = new User();
        }
        binding.get().setUser(currentUser);
        userViewModel.getLoginResult().observe(this, (DataResource<User> userDataResource) -> {
            switch (userDataResource.status) {
                case SUCCESS:
                    ProgressHUD.close();
                    userViewModel.saveUserState(LoginController.this, userDataResource.data);
                    ToastUtils.showToast(this, "登陆成功");
                    appExecutors.mainHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            segueToPreviousController();
                        }
                    }, 100);
                    break;
                case ERROR:
                    ProgressHUD.close();
                    SnackbarUtils.basicSnackBar(binding.get().clLogin, userDataResource.message, LoginController.this);
                    break;
                case LOADING:
                    ProgressHUD.showDialog(LoginController.this);
                    break;
                default:
                    break;
            }

        });
    }

    private void segueToPreviousController() {
        setResult(LOGIN_REQUEST_CODE);
        finishActivity(LOGIN_REQUEST_CODE);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.get().btnLogin.setEnabled(phoneNumberReady && passwordReady);
    }

    /**
     * onclick event
     *
     * @param view
     */
    public void onLogin(View view) {
        userViewModel.doLogin(binding.get().getUser());
    }

    public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == 6 || id == EditorInfo.IME_NULL && binding.get().btnLogin.isEnabled()) {
            onLogin(textView);
            return true;
        }
        return false;
    }

    public void onPhoneTextChanged(CharSequence phonenumber, int start, int before, int count) {
        phoneNumberReady = phonenumber.length() == 11;
        if (phoneNumberReady) {
            String message = "";
            if (!phonenumber.toString().matches(PHONE_REG)) {
                message = "手机号格式不对";
            }
            binding.get().tilPhonenumber.setError(message);
        }
        binding.get().btnLogin.setEnabled(phoneNumberReady && passwordReady);
    }

    public void onPasswordTextChanged(CharSequence charSequence, int start, int before, int count) {
        passwordReady = charSequence.length() > 0;
        binding.get().btnLogin.setEnabled(phoneNumberReady && passwordReady);
    }

    public void onResetPassword(View view) {
        Intent intent = new Intent(this, PasswordPhonenumberController.class);
        segue(Segue.SegueType.PUSH, intent);
    }

}

