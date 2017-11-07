package com.sire.usermodule.Repository;

import android.arch.lifecycle.LiveData;

import com.sire.corelibrary.Networking.Response.JsonResponse;
import com.sire.corelibrary.Networking.dataBound.DataResource;
import com.sire.corelibrary.Networking.dataBound.DataSourceStrategy;
import com.sire.usermodule.DB.Dao.UserDao;
import com.sire.usermodule.DB.Entry.User;
import com.sire.usermodule.Pojo.UserAuth;
import com.sire.usermodule.Pojo.UserLoginInfo;
import com.sire.usermodule.Pojo.UserRegisterInfo;
import com.sire.usermodule.WebService.UserWebService;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/07/24
 * Author:Sire
 * Description:
 * ==================================================
 */
@Singleton
public class UserRepository {
    private final UserDao userDao;
    private final UserWebService webService;

    @Inject
    public UserRepository(UserDao userDao, UserWebService webService) {
        this.userDao = userDao;
        this.webService = webService;
    }

    public LiveData userLogin(final UserLoginInfo userInfor) {
        return new DataSourceStrategy.Builder()
                .appDataFromStrategy(DataSourceStrategy.DataFromStrategy.NET)
                .cacheData(true)
                .build()
                .apply(new DataSourceStrategy.DataDecision<JsonResponse<User>, User>() {

                    @Override
                    public LiveData<Response<JsonResponse<User>>> makeNetCall() {
                        return webService.userLogin(userInfor);
                    }

                    @Override
                    public void saveData2DB(User user) {
                        userDao.insert(user);
                    }
                });

    }

    public LiveData<List<User>> fetchUsers() {
        return userDao.queryAllUsers();
    }

    public LiveData userRegister(UserRegisterInfo userRegisterInfo) {
        return new DataSourceStrategy.Builder()
                .cacheData(true)
                .appDataFromStrategy(DataSourceStrategy.DataFromStrategy.NET)
                .build()
                .apply(new DataSourceStrategy.DataDecision<JsonResponse<User>, User>() {

                    @Override
                    public LiveData<Response<JsonResponse<User>>> makeNetCall() {
                        return webService.userRegister(userRegisterInfo);
                    }

                    @Override
                    public void saveData2DB(User user) {
                        userDao.insert(user);
                    }
                });
    }

    public LiveData uploadHeadImage(File file, String userId) {
        //构建body
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("userId", userId)
                .addFormDataPart("file", "file", RequestBody.create(MediaType.parse("image/*"), file))
                .build();
        return new DataSourceStrategy.Builder()
                .cacheData(true)
                .appDataFromStrategy(DataSourceStrategy.DataFromStrategy.NET)
                .build()
                .apply(new DataSourceStrategy.DataDecision<JsonResponse<User>, User>() {

                    @Override
                    public LiveData<Response<JsonResponse<User>>> makeNetCall() {
                        return webService.uploadFile(requestBody);
                    }

                    @Override
                    public void saveData2DB(User user) {
                        userDao.insert(user);
                    }
                });

    }

    public LiveData completeUserInfor(User user) {

        return new DataSourceStrategy.Builder()
                .appDataFromStrategy(DataSourceStrategy.DataFromStrategy.NET)
                .build()
                .apply(new DataSourceStrategy.DataDecision<JsonResponse, User>() {

                    @Override
                    public LiveData<Response<JsonResponse>> makeNetCall() {
                        return webService.updateUserInfor(user);
                    }
                });
    }

    public LiveData<DataResource<User>> updateUserPassword(UserAuth userAuth) {
        return new DataSourceStrategy.Builder()
                .appDataFromStrategy(DataSourceStrategy.DataFromStrategy.NET)
                .build()
                .apply(new DataSourceStrategy.DataDecision<JsonResponse, User>() {

                    @Override
                    public LiveData<Response<JsonResponse>> makeNetCall() {
                        return webService.updateUserPassword(userAuth);
                    }
                });
    }
}
