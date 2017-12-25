package com.sire.bbsmodule.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.sire.corelibrary.Constant.Constant.GLOBAL_DATA_PATTEN;

/**
 * ==================================================
 * All Right Reserved
 * Date:2017/11/16
 * Author:Sire
 * Description:
 * ==================================================
 */

public class TimeConverterUtils {
    public static String date2SimpleDescription(Date date){
        Date currentDate = new Date();
        long deltaTime = currentDate.getTime() - date.getTime();
        //秒
        deltaTime =  deltaTime/1000;
        if(deltaTime <= 30){
            return "刚刚";
        }
        if(deltaTime <= 60){
            return deltaTime+"秒前";
        }
        //分钟
        deltaTime = deltaTime / 60;
        if(deltaTime>=1 && deltaTime <= 60){
            return deltaTime+"分钟前";
        }
        //小时
        deltaTime = deltaTime /60;
        if(deltaTime >= 1 && deltaTime<=24){
            return deltaTime+"小时前";
        }
        //天
        deltaTime = deltaTime/24;
        if(deltaTime>=1 && deltaTime <= 3){
            return deltaTime + "天前";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return simpleDateFormat.format(date);
    }
}
