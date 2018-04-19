
package com.sire.messagepushmodule.DB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.sire.corelibrary.DB.Converters;
import com.sire.messagepushmodule.DB.Dao.MessageDao;
import com.sire.messagepushmodule.DB.Entry.IMMessage;
import com.sire.messagepushmodule.DB.Entry.Message;


@Database(entities = {Message.class, IMMessage.class}, version = 1,exportSchema = false)
@TypeConverters(Converters.class)
public abstract class MessageDb extends RoomDatabase {
    abstract public MessageDao messageDao();
}