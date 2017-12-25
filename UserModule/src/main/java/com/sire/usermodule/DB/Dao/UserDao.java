package com.sire.usermodule.DB.Dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.sire.usermodule.DB.Entry.User;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Query("SELECT * FROM User WHERE name = :name")
    LiveData<User> queryUserById(String name);

    @Query("SELECT * FROM User ORDER BY loginTime DESC")
    Flowable<List<User>> queryAllUsers();


}