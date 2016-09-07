package com.bright.cloudutils.datetime;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间转换类
 * 
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtils {
	/**
	 * date类型转换为String类型 </br>
	 * 
	 * @param data
	 *            Date类型的时间
	 * @param formatType
	 *            自定义：比如格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
	 * @return String 格式化后的时间
	 */
	public static String dateToString(Date data, String formatType) {
		return new SimpleDateFormat(formatType).format(data);
	}

	// long类型转换为String类型
	// currentTime要转换的long类型的时间
	// formatType要转换的string类型的时间格式
	/**
	 * 当前时间格式化为指定的时间格式
	 * 
	 * @param currentTime
	 *            要转换的long类型的时间
	 * @param formatType
	 *            要转换的string类型的时间格式
	 * @throws ParseException
	 * @return String long类型转换为String类型时间
	 */
	public static String longToString(long currentTime, String formatType)
			throws ParseException {
		Date date = longToDate(currentTime, formatType); // long类型转成Date类型
		String strTime = dateToString(date, formatType); // date类型转成String
		return strTime;
	}

	// string类型转换为date类型
	// strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
	// HH时mm分ss秒，
	// strTime的时间格式必须要与formatType的时间格式相同
	/**
	 * string类型转换为date类型
	 * 
	 * @Title stringToDate
	 * @Description strTime的时间格式必须要与formatType的时间格式相同
	 * @param strTime
	 *            要转换的string类型的时间
	 * @param formatType
	 *            要转换的格式yyyy-MM-dd HH:mm:ss
	 * @throws ParseException
	 * @return Date
	 */
	public static Date stringToDate(String strTime, String formatType)
			throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		date = formatter.parse(strTime);
		return date;
	}

	// long转换为Date类型
	// currentTime要转换的long类型的时间
	// formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
	/**
	 * long转换为Date类型
	 * 
	 * @Title longToDate
	 * @Description
	 * @param currentTime
	 *            要转换的long类型的时间
	 * @param formatType
	 *            要转换的时间格式yyyy-MM-dd HH:mm:ss
	 * @throws ParseException
	 * @return Date
	 */
	public static Date longToDate(long currentTime, String formatType)
			throws ParseException {
		Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
		String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
		Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
		return date;
	}

	// string类型转换为long类型
	// strTime要转换的String类型的时间
	// formatType时间格式
	// strTime的时间格式和formatType的时间格式必须相同
	/**
	 * string类型转换为long类型
	 * 
	 * @Title stringToLong
	 * @Description strTime的时间格式和formatType的时间格式必须相同
	 * @param strTime
	 *            要转换的String类型的时间
	 * @param formatType
	 *            时间格式
	 * @throws ParseException
	 * @return long
	 */
	public static long stringToLong(String strTime, String formatType)
			throws ParseException {
		Date date = stringToDate(strTime, formatType); // String类型转成date类型
		if (date == null) {
			return 0;
		} else {
			long currentTime = dateToLong(date); // date类型转成long类型
			return currentTime;
		}
	}

	// date类型转换为long类型
	// date要转换的date类型的时间
	/**
	 * date类型转换为long类型
	 * 
	 * @Description
	 * @param date
	 *            要转换的date类型的时间
	 * @return long
	 */
	public static long dateToLong(Date date) {
		return date.getTime();
	}
}
