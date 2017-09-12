package com.sire.usermodule.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.sire.corelibrary.Lifecycle.DataLife.AbsentLiveData;
import com.sire.corelibrary.Networking.dataBound.DataResource;
import com.sire.usermodule.DB.Entry.User;
import com.sire.usermodule.Repository.UserRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/21
 * Author:Sire
 * Description:
 * ==================================================
 */
@Singleton
public class UserViewModel extends ViewModel {
    private final MutableLiveData<User> userInfor = new MutableLiveData<>();
    private final LiveData<DataResource<User>> loginResult;
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
    }

    /**
     * do login by user infor
     * @param user
     */
    public void doLogin(User user) {
        this.userInfor.setValue(user);
    }

    public LiveData<DataResource<User>> getLoginResult() {
        return loginResult;
    }

}
