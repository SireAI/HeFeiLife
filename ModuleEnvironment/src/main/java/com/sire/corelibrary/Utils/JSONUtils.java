package com.sire.corelibrary.Utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/1/4
 * Author:sire
 * Description:
 * ==================================================
 */
public class JSONUtils {
    private static Gson gson;
    private static void checkInstance(){
        if (gson==null){
            gson = new Gson();
        }
    }


    /**
     * format key and value to json string
     * @param jsonMap
     * @return
     */
    public static String String2Json(Map<String,String> jsonMap){

        if (jsonMap==null || jsonMap.size()==0){
            return null;
        }
        checkInstance();
        return gson.toJson(new JSONObject(jsonMap));
    }

    /**
     * format json string to bean
     * @param jsonString
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T jsonString2Bean(String jsonString, Class<T> clazz){
        checkInstance();
        if(jsonString == null)return null;
        try {
            return  gson.fromJson(jsonString, clazz);
        }catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getJsonValue(String jsonString, String key){
        if (jsonString==null){
            return "";
        }
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject.get(key).toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String bean2JsonString(Object src){
        checkInstance();
        return gson.toJson(src);
    }
}
