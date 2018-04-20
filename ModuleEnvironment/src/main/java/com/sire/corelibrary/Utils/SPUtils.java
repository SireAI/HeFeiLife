package com.sire.corelibrary.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/09/18
 * Author:Sire
 * Description:
 * ==================================================
 */

public class SPUtils {
    private static String spName = "cookie";
    public static void saveKeyValueString(Context context,String key,String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key,value).apply();
    }
    public static void removeKeyValue(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        if(sharedPreferences.contains(key)){
            sharedPreferences.edit().remove(key).apply();
        }
    }

    public static void saveKeyValueBlooen(Context context,String key,boolean value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(key,value).apply();

    }
    public static void saveKeyValueInteger(Context context,String key,int value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(key,value).apply();
    }
    public static int getValueInteger(Context context,String key,int defaultValue){
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        int value = sharedPreferences.getInt(key, defaultValue);
        return value;
    }
    public static String getValueString(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(key, "");
        return value;
    }
    public static boolean getValueBoolen(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
        boolean value = sharedPreferences.getBoolean(key,false);
        return value;
    }

}
