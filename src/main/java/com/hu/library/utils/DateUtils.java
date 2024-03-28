package com.hu.library.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间日期工具类
 */
public class DateUtils {

    /**
     * 格式化当前时间
     * 日期格式模式，默认 "yyyy-MM-dd HH:mm:ss"
     * @return 格式化后的当前日期字符串
     */
    public static String formatCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
}
