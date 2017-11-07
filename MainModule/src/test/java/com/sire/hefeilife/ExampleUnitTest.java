package com.sire.hefeilife;

import com.google.gson.Gson;
import com.sire.corelibrary.Utils.JSONUtils;
import com.sire.corelibrary.Utils.ObjectMapConversionUtils;
import com.sire.usermodule.DB.Entry.User;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        Timber.i("-------");
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        stringObjectHashMap.put("userId","231");
//        String s = JSONUtils.String2Json(stringObjectHashMap);
//        Gson gson = new Gson();
//        String s = gson.toJson(stringObjectHashMap);
//        System.out.println("======"+s);
        User user = ObjectMapConversionUtils.map2Object(stringObjectHashMap, User.class);
        System.out.println("====="+user.toString());

    }
}