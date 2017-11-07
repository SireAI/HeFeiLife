package com.sire.feedmodule.DB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.sire.corelibrary.DB.Converters;
import com.sire.feedmodule.DB.Dao.FeedDao;
import com.sire.feedmodule.DB.Entry.FeedInfor;

@Database(entities = {FeedInfor.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class FeedDb extends RoomDatabase {
    abstract public FeedDao feedDao();
}