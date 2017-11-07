package com.sire.usermodule.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import com.sire.corelibrary.Lifecycle.DataLife.AbsentLiveData;
import com.sire.corelibrary.Networking.Response.JsonResponse;
import com.sire.corelibrary.Networking.dataBound.DataResource;
import com.sire.corelibrary.Utils.SPUtils;
import com.sire.usermodule.Controller.RegisterVerifyCodeController;
import com.sire.usermodule.DB.Entry.User;
import com.sire.usermodule.Pojo.UserAuth;
import com.sire.usermodule.Pojo.UserLoginInfo;
import com.sire.usermodule.Pojo.UserRegisterInfo;
import com.sire.usermodule.Repository.UserRepository;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import static com.sire.corelibrary.Networking.Network.LOGIN_TOKEN;
import static com.sire.usermodule.Constant.Constant.CUREENT_LOGIN_USER_ID;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/21
 * Author:Sire
 * Description:
 * ==================================================
 */
public class UserViewModel extends ViewModel {
    private MutableLiveData<UserLoginInfo> userInfor = new MutableLiveData<>();
    private MutableLiveData<UserRegisterInfo> userRegisterInfo = new MutableLiveData<>();
    private LiveData<DataResource<User>> loginResult;
    private LiveData<DataResource<User>> registerResult;
    private LiveData<List<User>> users;
    private UserRepository userRepository;

    @SuppressWarnings("unchecked")
    @Inject
    public UserViewModel(UserRepository userRepository) {
        loginResult = Transformations.switchMap(userInfor, user -> {
            if (user == null) {
                return AbsentLiveData.create();
            } else {
                return userRepository.userLogin(user);
            }
        });
        registerResult = Transformations.switchMap(userRegisterInfo, registerInfo -> {
            if (registerInfo == null) {
                return AbsentLiveData.create();
            } else {
                return userRepository.userRegister(registerInfo);
            }
        });
        this.userRepository = userRepository;
    }

    /**
     * do login by user infor
     *
     * @param user
     */
    public void doLogin(User user) {
        UserLoginInfo userLoginInfo = new UserLoginInfo("PHONE_NUMBER", user.getPhonenumber(), user.getPwd());
        this.userInfor.setValue(userLoginInfo);
    }

    public LiveData<DataResource<User>> getLoginResult() {
        return loginResult;
    }

    public LiveData<DataResource<User>> getRegisterResult() {
        return registerResult;
    }

    public LiveData<List<User>> getUsers() {
        return users = userRepository.fetchUsers();
    }

    public void
    register(String phoneNumber, String password) {
        UserRegisterInfo userRegisterInfo = new UserRegisterInfo(phoneNumber, phoneNumber, password);
        this.userRegisterInfo.setValue(userRegisterInfo);
    }

    public LiveData<DataResource> updateNickname(String nickName, String userId) {
        User user = new User();
        user.setUserId(userId);
        user.setName(nickName);
        return userRepository.completeUserInfor(user);
    }

    public LiveData<DataResource> updateSex(String sex, String userId) {
        User user = new User();
        user.setUserId(userId);
        user.setSex(sex);
        return userRepository.completeUserInfor(user);
    }
    public LiveData<DataResource> updateBirthday(String birthday, String userId) {
        User user = new User();
        user.setUserId(userId);
        user.setBirthday(birthday);
        return userRepository.completeUserInfor(user);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        userInfor = null;
        loginResult = null;
        users = null;
        userRepository = null;
    }


    public LiveData<DataResource<JsonResponse<User>>> uploadHeadImage(File file, String userId) {
        return userRepository.uploadHeadImage(file, userId);
    }

    public String getUserId(Context context) {
        String userId = SPUtils.getValueString(context, CUREENT_LOGIN_USER_ID);
        return userId;
    }

    public void saveUserState(Context context, User user) {
        SPUtils.saveKeyValueString(context, CUREENT_LOGIN_USER_ID, user.getUserId());
        SPUtils.saveKeyValueString(context, LOGIN_TOKEN, user.getToken());
    }

    public LiveData<DataResource<User>> updatePassword(String phoneNumber, String confirmPassword) {
        UserAuth userAuth = new UserAuth();
        userAuth.setIdentityType("PHONE_NUMBER");
        userAuth.setIdentifier(phoneNumber);
        userAuth.setCredential(confirmPassword);
        return userRepository.updateUserPassword(userAuth);
    }
}
