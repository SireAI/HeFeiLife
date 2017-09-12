package com.sire.corelibrary.Utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/01/03
 * Author:Sire
 * Description:
 * ==================================================
 */

public class Object2MapUtils {
    private Object2MapUtils() {
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
    public static Map<String,String> Object2Map(Object object){
        Field[] declaredFields = object.getClass().getDeclaredFields();
        Map<String,String> map = new LinkedHashMap<>();
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
                    String stringValue = "";
                    if(value!=null){
                        stringValue = value.toString();
                    }
                    map.put(stringName,stringValue);
                }
            }
        }
        return map;
    }

}
