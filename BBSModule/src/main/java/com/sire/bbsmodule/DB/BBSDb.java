package com.sire.bbsmodule.DB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.sire.bbsmodule.DB.Dao.BBSDao;
import com.sire.bbsmodule.DB.Entry.CommentPraiseInfor;
import com.sire.corelibrary.DB.Converters;


@Database(entities = {CommentPraiseInfor.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class BBSDb extends RoomDatabase {
    abstract public BBSDao bbsDao();
}