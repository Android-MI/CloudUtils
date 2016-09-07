package com.bright.cloudutils.datetime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bright-小米
 * @version 创建时间：2016年8月30日 下午5:42:40
 */
public class DateFormatUtils {

    public static final String PATTERN10 = "yyyy-MM-dd";
    public static final String PATTERN19 = "yyyy-MM-dd HH:mm:ss";

    private static final ThreadLocal<Map<String, SimpleDateFormat>> DATE_FORMAT_HOLDER = new ThreadLocal<Map<String, SimpleDateFormat>>() {
        @Override
        protected Map<String, SimpleDateFormat> initialValue() {
            Map<String, SimpleDateFormat> map = new HashMap<String, SimpleDateFormat>();
            map.put(PATTERN10, new SimpleDateFormat(PATTERN10));
            map.put(PATTERN19, new SimpleDateFormat(PATTERN19));
            return map;
        }
    };

    private DateFormatUtils() {
        // 私有的构造函数
    }

    /**
     * 获取符合pattern格式的SimpleDateFormat对象<br/>
     * 如果pattern格式的SimpleDateFormat对象不存在，会创建并缓存之<br/>
     *
     * @param pattern
     * @return
     */
    public static SimpleDateFormat getDateFormat(String pattern) {
        Map<String, SimpleDateFormat> map = DATE_FORMAT_HOLDER.get();
        SimpleDateFormat sdf = map.get(pattern);
        if (sdf == null) {
            sdf = new SimpleDateFormat(pattern);
            map.put(pattern, sdf);
        }
        return sdf;
    }

    /**
     * 获取yyyy-MM-dd格式的SimpleDateFormat对象
     *
     * @return
     */
    public static SimpleDateFormat getDateFormat10() {
        return getDateFormat(PATTERN10);
    }

    /**
     * 获取yyyy-MM-dd HH:mm:ss格式的SimpleDateFormat对象
     *
     * @return
     */
    public static SimpleDateFormat getDateFormat19() {
        return getDateFormat(PATTERN19);
    }

    /**
     * 把日期格式化成yyyy-MM-dd字符串
     *
     * @param date
     * @return
     */
    public static String format10(Date date) {
        if (date == null) {
            return "";
        }
        return getDateFormat10().format(date);
    }

    /**
     * 把日期格式化成yyyy-MM-dd HH:mm:ss字符串
     *
     * @param date
     * @return
     */
    public static String format19(Date date) {
        if (date == null) {
            return "";
        }
        return getDateFormat19().format(date);
    }

    /**
     * 把yyyy-MM-dd格式的字符串转化成日期
     *
     * @param datestr
     * @return
     */
    public static Date parse10(String datestr) {
        if (datestr == null || datestr.length() != 10) {
            return null;
        }
        try {
            return getDateFormat10().parse(datestr);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 把yyyy-MM-dd HH:mm:ss格式的字符串转化成日期
     *
     * @param datestr
     * @return
     */
    public static Date parse19(String datestr) {
        if (datestr == null || datestr.length() != 19) {
            return null;
        }
        try {
            return getDateFormat19().parse(datestr);
        } catch (Exception e) {
            return null;
        }
    }
}
