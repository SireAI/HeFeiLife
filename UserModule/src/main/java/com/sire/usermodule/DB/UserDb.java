
package com.sire.usermodule.DB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.sire.usermodule.DB.Dao.UserDao;
import com.sire.usermodule.DB.Entry.User;

@Database(entities = {User.class}, version = 1,exportSchema = false)
public abstract class UserDb extends RoomDatabase {
    abstract public UserDao userDao();
}