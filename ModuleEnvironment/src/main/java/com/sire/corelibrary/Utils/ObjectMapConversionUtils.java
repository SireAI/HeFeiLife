package com.sire.corelibrary.Utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.sire.corelibrary.Constant.Constant.GLOBAL_DATA_PATTEN;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/01/03
 * Author:Sire
 * Description:
 * ==================================================
 */

public class ObjectMapConversionUtils {
    private ObjectMapConversionUtils() {
    }

    @Retention(RetentionPolicy.CLASS)
    @Target(ElementType.FIELD)
    public @interface MappableIngore{
    }

    /**
     * turn an object to a key-value map
     * @param object
     * @return map
     */
    public static Map<String,Object> Object2Map(Object object){
        Field[] declaredFields = object.getClass().getDeclaredFields();
        Map<String,Object> map = new LinkedHashMap<>();
        if(declaredFields!=null&&declaredFields.length>0){
            for (int i = 0; i < declaredFields.length; i++) {
                Field declaredField = declaredFields[i];
                if(!declaredField.isAccessible()){
                    declaredField.setAccessible(true);
                }
                if(!declaredField.isAnnotationPresent(MappableIngore.class)){
                    Object value = null;
                    try {
                        value = declaredField.get(object);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    String stringName = declaredField.getName();
                    if(stringName.equals("serialVersionUID")||stringName.equals("$change")){
                        continue;
                    }
//                    String stringValue = "";
//                    if(value!=null){
//                        stringValue = value.toString();
//                    }
                    if(value instanceof Date){
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(GLOBAL_DATA_PATTEN);
                        map.put(stringName,simpleDateFormat.format(value));
                    }else {
                        map.put(stringName,value==null?"":value);
                    }

                }
            }
        }
        return map;
    }
    public static <T> T map2Object(Map<String,Object> mapParmas,Class<T> clazz){
        return JSONUtils.jsonString2Bean(JSONUtils.String2Json(mapParmas),clazz);
    }
}
