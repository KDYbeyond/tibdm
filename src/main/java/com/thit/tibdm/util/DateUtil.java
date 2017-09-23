package com.thit.tibdm.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wanghaoqiang on 2016/10/24.
 */
public class DateUtil {
    /**
     * 将字符串转为时间戳
     *
     * @param user_time 用户时间
     * @return long
     */
    public static long getTime(String user_time) {
        long l = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date d;
        try {
            d = sdf.parse(user_time);
            l = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return l;
    }

    /**
     * @param time 时间
     * @return String
     */
    public static String getFormat(long time) {
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String nDate = sdf.format(time);
        return nDate;
    }

    /**
     * @param time 时间
     * @param timeFormat 时间格式
     * @return long
     */
    public static long getTimeByFormat(String time, String timeFormat) {
        long l = 0;
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
        Date d;
        try {
            d = sdf.parse(time);
            l = d.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return l;
    }

    /**
     * 如果不够的话补零
     *
     * @param num 字数
     * @return String 返回
     */
    public static String getAddZero(int num) {
        return num < 10 ? "0" + num : num + "";
    }
}
