package com.fwj.mobilesafe.uitils;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类<br>
 * 主要功能:<br>
 * 1.格式化时间<br>
 * 
 * @author 傅文江
 * @2015-1-7 下午2:52:51 修订时间： 修订内容：
 */
@SuppressLint("SimpleDateFormat")
public class DataUtils {
	public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat(
			"yyyy-MM-dd");

	/**
	 * 得到以分钟结尾的时间
	 * 
	 * @param data
	 * @return 2015-04-10 11:39
	 */
	public static String formatDate(long data) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(data);
	}

	/**
	 * 将时间由Long转化为String
	 * 
	 * @param timeInMillis
	 * @param dateFormat
	 * @return
	 */
	public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
		return dateFormat.format(new Date(timeInMillis));
	}

	/**
	 * 得到以秒结束的时间
	 * 
	 * @param timeInMillis
	 * @return 2015-04-10 11:39:45
	 */
	public static String getTime(long timeInMillis) {
		return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
	}

	/**
	 * 得到当前的时间
	 * 
	 * @return
	 */
	public static long getCurrentTimeInLong() {
		return System.currentTimeMillis();
	}

	/**
	 * 以字符串形式得到当前的时间
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString() {
		return getTime(getCurrentTimeInLong());
	}

	/**
	 * 得到当前的时间
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
		return getTime(getCurrentTimeInLong(), dateFormat);
	}

	/**
	 * 得到人性化格式的时间
	 * 
	 * @param origTime
	 *            传入字符串origTime格式 字符串时间
	 * @return eg: 昨天13:59  or 今天 11:39
	 */
	public static String getFormatTime(String origTime) {
		if ("0".equals(origTime))
			return "";
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			origTime = format.format(Long.parseLong(origTime));
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String timePart = origTime.substring(11);
			String datePart = origTime.substring(0, 10);
			Date d1 = df.parse(df.format(new Date()));
			Date d2 = df.parse(datePart);

			long diff = d1.getTime() - d2.getTime();
			long days = diff / (1000 * 60 * 60 * 24);
			if (days == 0)
				return "今天 " + timePart;
			else if (days == 1)
				return "昨天 " + "" + timePart;
			else
				return origTime.substring(5);
		} catch (Exception e) {
			return origTime;
		}
	}

	/**
	 * 得到人性化格式的时间
	 * 
	 * @param origTime
	 *            传入字符串origTime格式 字符串时间
	 * @return eg: 昨天13:59
	 */
	public static String getFormatTime(long origTime) {
		return getFormatTime(origTime + "");
	}
}
