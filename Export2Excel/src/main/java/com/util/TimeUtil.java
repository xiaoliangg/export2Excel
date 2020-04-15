package com.util;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间操作工具类
 * 
 * 
 */
public abstract class TimeUtil {

	/**
	 * 获取当前系统时间
	 * 
	 * @return java.util.Date 对象
	 */
	public static Date getSystemTime() {
		return new Date();
	}

	/**
	 * 获取当前系统时间的字符串格式，以yyyyMMddHHmmss的格式表示
	 * 
	 * @return
	 */
	public static String getStringToday() {
		Date currentTime = new Date();
		return format(currentTime, "yyyyMMddHHmmss");
	}

	/**
	 * 将传入的 java.util.Date 对象，按 'yyyy-MM-dd' 的格式进行格式化
	 * 
	 * @param date
	 *            需要格式化的 java.util.Date 对象
	 * @return 已格式化的时间字符串
	 */
	public static String getFormatDate(Date date) {
		return format(date, "yyyy-MM-dd");
//		return "2017-11-03";
	}

	/**
	 * 将传入的 java.util.Date 对象，按所指定的格式进行格式化
	 * 
	 * @param date
	 *            时间对象
	 * @param formatStr
	 *            指定格式
	 * @return
	 */
	public static String getFormat(Date date, String formatStr) {
		return format(date, formatStr);
	}

	/**
	 * 将传入的 java.util.Date 对象，按 'hh:mm:ss' 的格式进行格式化
	 * 
	 * @param date
	 *            需要格式化的 java.util.Date 对象
	 * @return 已格式化的时间字符串
	 */
	public static String getFormatTime(Date date) {
		return format(date, "hh:mm:ss");
	}

	/**
	 * 将传入的 java.util.Date 对象，按'yyyy-MM-dd hh:mm:ss'的格式进行格式化
	 * 
	 * @param date
	 *            需要格式化的 java.util.Date 对象
	 * @return 已格式化的时间字符串
	 */
	public static String getFormatDateTime(Date date) {
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String format(Date date, String formatStr) {
		SimpleDateFormat formater = new SimpleDateFormat(formatStr);
		return formater.format(date);
	}

	public static Date format(String pattern, String str) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(str);
		} catch (ParseException e) {
			return new Date();
		}
	}
	
	/***
	 * UTC时间转为北京时间
	 */
	public static String converUTCTime(String dateStr){
		Date date=format("yyyy-MM-dd'T'HH:mm:ss", dateStr);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, 8);
		date = calendar.getTime();
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	/***
	 * UTC时间转为北京时间
	 */
	public static String converUTCTime(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, 8);
		date = calendar.getTime();
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	/***
	 * 获取昨天结束时间
	 * @return
	 */
	public static String getEndCETIntegralPoint(){
		Date date=new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, -7);
		date = calendar.getTime();
		return format(date, "yyyy-MM-dd'T'HH:00:00")+"CET";
	}
	/***
	 * 获取昨天开始时间
	 * @return
	 */
	public static String getStartCETIntegralPoint(){
		Date date=new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, -10);
		date = calendar.getTime();
		return format(date, "yyyy-MM-dd'T'HH:00:00")+"CET";
	}
	
	public static String getFormateYesterday(String formatStr){

		Date date = getNow();
//		Date date=new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
		return formatter.format(date);
	}

	public static String getFormateLastMonth(String formatStr,Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, -1);
		date = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
		return formatter.format(date);
	}
	
	public static String getFormateNextMonth(String formatStr,Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, 1);
		date = calendar.getTime();
		SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
		return formatter.format(date);
	}
	public static String getFormateNow(String formatStr){
		Date date = getNow();
//		Date date=new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
		return formatter.format(date);
	}
	public static Date getNow() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse("2018-04-16 02:00:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		return date;
		return new Date();
	}



	public static Date getAgo(Date current, String timeAgo) {
		int days = 0 - Integer.parseInt(timeAgo);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(current);
		calendar.add(Calendar.DAY_OF_MONTH, days);
		Date ago = calendar.getTime();
		return ago;
	}
	
	/**
	 * 获取指定日期前/后amount天的日期
	 * @param date
	 * 			指定日期(Date)
	 * @param amount
	 * 			加减数值
	 * @param pattern
	 * 			输出格式
	 * @return
	 */
	public static String getDiffDate(Date date, int amount, String pattern){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, amount);
		return format(cal.getTime(),pattern);
	}
	

	/**
	 * 获取指定秒数后的时间
	 * @Description: 
	 * @author: yuliang
	 * @Package: com.roamingServer.util 
	 * @date: 2018-11-27 上午10:42:08
	 * @param date
	 * @param seconds
	 * @return
	 */
	public static Date addSeconds(Date date, int seconds){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return cal.getTime();
	}
	
	/**
	 * 获取指定日期前/后amount天的日期
	 * @param date
	 * 			指定日期(String)
	 * @param amount
	 * 			加减数值
	 * @param pattern
	 * 			输出格式
	 * @return
	 */
	public static String getDiffDate(String date, int amount, String pattern){
		Date temp = format(pattern, date);
		return getDiffDate(temp,amount,pattern);
	}
	/**
	 * 获取指定日期前/后amount天的日期,输出格式为yyyy-MM-dd
	 * @param date
	 * 			指定日期(String)
	 * @param amount
	 * 			加减数值
	 * @return
	 */
	public static String getDiffDate(String date, int amount){
		return getDiffDate(date, amount, "yyyy-MM-dd");
	}
	/***
	 * 
	 * @param date 格式为：dd-MMM-yyyy
	 * @param locale
	 * @return  格式：yyyy-MM-dd
	 */
	public static String format(String date,Locale locale){
		try {
			SimpleDateFormat in = new SimpleDateFormat("dd-MMM-yyyy", locale);
			SimpleDateFormat out = new SimpleDateFormat("yyyy-MM-dd");	
			Date d = in.parse(date);
			return out.format(d);
		} catch (Exception e) {
			return date;
		}
		
		
	}
	public static void main(String[] args) {
		String s = "2013-10-23 00:00:00";
		Date d = format("yyyy-MM-dd HH:mm:ss", s);
		System.out.println(addSeconds(d, 80));
		
	}
}
