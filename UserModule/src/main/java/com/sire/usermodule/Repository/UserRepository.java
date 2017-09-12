package com.sire.usermodule.Repository;

import android.arch.lifecycle.LiveData;

import com.sire.corelibrary.Networking.dataBound.DataSourceStrategy;
import com.sire.usermodule.DB.Dao.UserDao;
import com.sire.usermodule.DB.Entry.User;
import com.sire.usermodule.WebService.UserWebService;

import javax.inject.Inject;
import javax.inject.Singleton;

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
    public UserRepository( UserDao userDao,UserWebService webService) {
        this.userDao = userDao;
        this.webService = webService;
    }

    public LiveData userLogin(final User userInfor){
        return  new DataSourceStrategy.Builder<User,User>()
                .appDataFromStrategy(DataSourceStrategy.DataFromStrategy.CACHE_NET)
                .build()
                .apply(new DataSourceStrategy.DataDecision<User,User>() {

                    @Override
                    public LiveData loadFromDb() {
                        return userDao.queryUserById(userInfor.getName());
                    }

                    @Override
                    public LiveData<Response<User>> makeNetCall() {
                        return webService.userLogin(userInfor);
                    }

                    @Override
                    public void saveData2DB(User user) {
                        userDao.insert(user);
                    }
                });

    }
}
