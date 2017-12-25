package com.sire.usermodule.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.Nullable;

import com.sire.corelibrary.DI.Environment.ModuleInitInfor;
import com.sire.corelibrary.Lifecycle.DataLife.AbsentLiveData;
import com.sire.corelibrary.Networking.Response.JsonResponse;
import com.sire.corelibrary.Networking.dataBound.DataResource;
import com.sire.corelibrary.Utils.SPUtils;
import com.sire.mediators.UserModuleInterface.UserLoginState;
import com.sire.mediators.core.CallBack;
import com.sire.usermodule.DB.Entry.User;
import com.sire.usermodule.Pojo.UserAuth;
import com.sire.usermodule.Pojo.UserLoginInfo;
import com.sire.usermodule.Pojo.UserRegisterInfo;
import com.sire.usermodule.Repository.UserRepository;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

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
    private static User currentUser;
    private MutableLiveData<UserLoginInfo> userInfor = new MutableLiveData<>();
    private MutableLiveData<UserRegisterInfo> userRegisterInfo = new MutableLiveData<>();
    private LiveData<DataResource<User>> loginResult;
    private LiveData<DataResource<User>> registerResult;
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
     * do login by currentUser infor
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




    public void register(String phoneNumber, String password) {
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
        userRepository = null;
    }


    public LiveData<DataResource<JsonResponse<User>>> uploadHeadImage(File file, String userId) {
        return userRepository.uploadHeadImage(file, userId);
    }


    /**
     * 初始化用户登陆状态
     * @param context
     */
    public Flowable<ModuleInitInfor> initUserState(Context context) {
        String userId = getUserIdFromSP(context);
        Flowable<List<User>> usersFromCache = getUsers();

        return usersFromCache.flatMap((List<User> users) -> {
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getUserId().equals(userId)) {
                    UserViewModel.currentUser = users.get(i);
                    break;
                }
            }
            return Flowable.just(new ModuleInitInfor("UserModule","用户信息初始化"));
        });

    }

    public  User getCurrentUser() {
        return currentUser;
    }

    private Flowable<List<User>> getUsers() {
        return userRepository.fetchUsers();
    }

    public String getUserId() {
        return currentUser != null ? currentUser.getUserId() : "";
    }

    public String getUserIdFromSP(Context context) {
        String userId = SPUtils.getValueString(context, CUREENT_LOGIN_USER_ID);
        return userId;
    }

    /**
     * 保存用户登陆完成后的状态
     * @param context
     * @param user
     */
    public void saveUserState(Context context, User user) {
        SPUtils.saveKeyValueString(context, CUREENT_LOGIN_USER_ID, user.getUserId());
        SPUtils.saveKeyValueString(context, LOGIN_TOKEN, user.getToken());
        currentUser = user;
        notifyUserLoginState(true);
    }

    /**
     * 通知登陆状态，刷新用户数据
     * @param isLogin
     */
    private void notifyUserLoginState(boolean isLogin) {
        UserLoginState userLoginState = new UserLoginState();
        userLoginState.setLogin(isLogin);
        EventBus.getDefault().post(userLoginState);
    }

    public LiveData<DataResource<User>> updatePassword(String phoneNumber, String confirmPassword) {
        UserAuth userAuth = new UserAuth();
        userAuth.setIdentityType("PHONE_NUMBER");
        userAuth.setIdentifier(phoneNumber);
        userAuth.setCredential(confirmPassword);
        return userRepository.updateUserPassword(userAuth);
    }

    public String getUserImage() {
        return currentUser != null ? currentUser.getAvatar() : "";
    }



    public String getUserCurrentAddress() {
        return "北京";
    }

    public String getUserName() {
        return currentUser.getName();
    }

    public String getUserLevel() {
        return currentUser.getLevel()+"";
    }

}
