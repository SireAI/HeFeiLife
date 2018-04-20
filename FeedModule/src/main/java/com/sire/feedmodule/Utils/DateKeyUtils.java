package com.sire.feedmodule.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateKeyUtils {
    public static long generateDateKey(Date date)  {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(date);
        long dateSecondes = 0;
        try {
            dateSecondes = simpleDateFormat.parse(format).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateSecondes;
    }

    /**
     * 获取时间整数
     * @param date 设定的日期
     * @param times 以当前日期为基线的编译两，正数向前偏移，负数向后偏移
     * @return
     */
    public static long generateOffsetdayKey(Date date, int times)  {
        return generateDateKey(date) + 24*60*60*1000*times;
    }
}