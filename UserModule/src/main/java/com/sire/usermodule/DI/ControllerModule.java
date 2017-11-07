package com.sire.usermodule.DI;

import com.sire.usermodule.Controller.CompletePersonalInforBirthdayController;
import com.sire.usermodule.Controller.CompletePersonalInforController;
import com.sire.usermodule.Controller.CompletePersonalInforHobbyController;
import com.sire.usermodule.Controller.CompletePersonalInforPhotoController;
import com.sire.usermodule.Controller.CompletePersonalInforSexController;
import com.sire.usermodule.Controller.EntranceController;
import com.sire.usermodule.Controller.LoginController;
import com.sire.usermodule.Controller.PasswordPhonenumberController;
import com.sire.usermodule.Controller.PasswordResetController;
import com.sire.usermodule.Controller.PasswordVerifyCodeController;
import com.sire.usermodule.Controller.RegisterPhoneNumberController;
import com.sire.usermodule.Controller.RegisterVerifyCodeController;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/25
 * Author:Sire
 * Description:controller di
 * ==================================================
 */
@Module
public abstract class ControllerModule {
    @ContributesAndroidInjector
    abstract LoginController contributeLoginController();
    @ContributesAndroidInjector
    abstract EntranceController contributeEntranceController();
    @ContributesAndroidInjector
    abstract RegisterPhoneNumberController contributeRegisterPhoneNumberController();
    @ContributesAndroidInjector
    abstract RegisterVerifyCodeController contributeRegisterVerifyCodeController();
    @ContributesAndroidInjector
    abstract CompletePersonalInforController contributeCompletePersonalInforController();
    @ContributesAndroidInjector
    abstract CompletePersonalInforPhotoController contributeCompletePersonalInforPhoto();
    @ContributesAndroidInjector
    abstract CompletePersonalInforSexController contributeCompletePersonalInforSexController();
    @ContributesAndroidInjector
    abstract CompletePersonalInforBirthdayController contributeCompletePersonalInforBirthdayController();
    @ContributesAndroidInjector
    abstract CompletePersonalInforHobbyController contributeCompletePersonalInforHobbyController();
    @ContributesAndroidInjector
    abstract PasswordPhonenumberController contributePasswordPhonenumberController();
    @ContributesAndroidInjector
    abstract PasswordVerifyCodeController contributePasswordVerifyCodeController();
    @ContributesAndroidInjector
    abstract PasswordResetController contributePasswordResetController();
}
