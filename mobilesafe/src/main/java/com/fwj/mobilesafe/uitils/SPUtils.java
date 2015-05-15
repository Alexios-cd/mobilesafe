package com.fwj.mobilesafe.uitils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * PreferencesUtils, easy to get or put data
 * <ul>
 * <strong>Preference Name</strong>
 * <li>you can change preference name by {@link #PREFERENCE_NAME}</li>
 * </ul>
 * <ul>
 * <strong>Put Value</strong>
 * <li>put string {@link #putString(Context, String, String)}</li>
 * <li>put int {@link #putInt(Context, String, int)}</li>
 * <li>put long {@link #putLong(Context, String, long)}</li>
 * <li>put float {@link #putFloat(Context, String, float)}</li>
 * <li>put boolean {@link #putBoolean(Context, String, boolean)}</li>
 * </ul>
 * <ul>
 * <strong>Get Value</strong>
 * <li>get string {@link #getString(Context, String)},
 * {@link #getString(Context, String, String)}</li>
 * <li>get int {@link #getInt(Context, String)},
 * {@link #getInt(Context, String, int)}</li>
 * <li>get long {@link #getLong(Context, String)},
 * {@link #getLong(Context, String, long)}</li>
 * <li>get float {@link #getFloat(Context, String)},
 * {@link #getFloat(Context, String, float)}</li>
 * <li>get boolean {@link #getBoolean(Context, String)},
 * {@link #getBoolean(Context, String, boolean)}</li>
 * </ul>
 * 
 */
public class SPUtils {

	/**
	 * 配置文件名
	 */
	public static final String ISFRISTIN = "is_fristin";
	public static final String PREFERENCE_NAME = "SJQCONFIG";
	/**
	 * 活动的链接
	 */
	public static final String ACTIVITY_URL = "sjqactivity_url";
	/**
	 * 活动的标题
	 */
	public static final String ACTIVITY_TITLE = "sjqactivity_title";
	public static final String TELL = "mytell";
	/**
	 * 商情页面下拉刷新时顶部显示的字体
	 */
	public static String CONJUNCTUREPAGERPULLDOWNREFRESH_TOPMSG = "conjuncturePagerpulldownRefreshfont";
	/**
	 * 程序启动界面的时间
	 */
	public static String SPLASH_TIME = "splash_time";
	/**
	 * 程序启动界面需要分享的url
	 */

	public static String SPLASH_SHARE_URL = "share_url";
	/**
	 * 程序启动界面需要 分享 内容的标题
	 */
	public static String SPLASH_SHARE_TITLE = "share_title";
	/**
	 * 程序启动界面需要分享的内容正文
	 */
	public static String SPLASH_SHARE_INFO = "share_info";
	/**
	 * 程序启动界面需要分享的内容图片的url
	 */
	public static String SPLASH_SHARE_IMG = "share_img";
	/**
	 * 通讯录页面下拉刷新时顶部显示的字体
	 */
	public static String ADDRESSBOOKPAGERPULLDOWNREFRESH_TOPMSG = "addressBookPagerpulldownRefresh";

	/**
	 * app客服qq号码(动态 配置)
	 */
	public static String APPCONFIG_QQ_NUM = "appConfig_qq_num";
	/**
	 * app客服电话(动态 配置)
	 */
	public static String APPCONFIG_TEL_NUM = "appConfig_tel_num";
	/**
	 * app客服微信(动态 配置)
	 */
	public static String APPCONFIG_MICROMSG_NUM = "appConfig_micromsg_num";
	/**
	 * app客服公司地址（动态配置）
	 */
	public static String APPCONFIG_ADDRESS = "appConfig_address";
	/**
	 * app分享标题（动态配置）
	 */
	public static String APPCONFIG_SHARE_TITLE = "appConfig_share_title";
	/**
	 * app分享内容（动态配置）
	 */
	public static String APPCONFIG_SHARE_INFO = "appConfig_share_info";
	/**
	 * app客服默认分享图片(动态 配置)
	 */
	public static String APPCONFIG_SHARE_ICON = "appConfig_app_share_icon";
	/**
	 * 商情详情默认分享图片(动态 配置)
	 */
	public static String APPCONFIG_MARKET_SHARE_ICON = "appConfig_market_share_icon";

	/**
	 * 判断在商情条目和商情详情页面是否需要发送消息
	 */
	public static String ISNEEDTOSENDMARKET = "IsNeedToSendMarket";

	/**
	 * 应用开启界面的启动图
	 */
	public static String GETLAUNCHIMGURL = "getLaunchImgurl";

	public static String LOGINNUM = "loginnum";
	public static String ISLOGIN = "islogin";
	public static String NAME = "name";
	public static String PWD = "pwd";
	public static String UID = "uid";
	public static String U = "U";
	public static String PROVINCE_ID = "province_id";
	public static String CITY_ID = "city_id";
	public static String CITY = "city";
	public static String PROVINCE = "province";
	public static String ISNEEDACTIVITY = "isneedactivity";

	private SPUtils() {
		throw new AssertionError();
	}

	/**
	 * put string preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to modify
	 * @param value
	 *            The new value for the preference
	 * @return True if the new values were successfully written to persistent
	 *         storage.
	 */
	public static boolean putString(String key, String value) {
		SharedPreferences settings = UIUtils.getContext().getSharedPreferences(
				PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		return editor.commit();
	}

	/**
	 * get string preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @return The preference value if it exists, or null. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a string
	 * @see #getString(Context, String, String)
	 */
	public static String getString(String key) {
		return getString(key, null);
	}

	/**
	 * get string preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @param defaultValue
	 *            Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a string
	 */
	public static String getString(String key, String defaultValue) {
		SharedPreferences settings = UIUtils.getContext().getSharedPreferences(
				PREFERENCE_NAME, Context.MODE_PRIVATE);
		return settings.getString(key, defaultValue);
	}

	/**
	 * put int preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to modify
	 * @param value
	 *            The new value for the preference
	 * @return True if the new values were successfully written to persistent
	 *         storage.
	 */
	public static boolean putInt(String key, int value) {
		SharedPreferences settings = UIUtils.getContext().getSharedPreferences(
				PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key, value);
		return editor.commit();
	}

	/**
	 * 默认返回-1
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @return The preference value if it exists, or -1. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a int
	 * @see #getInt(Context, String, int)
	 */
	public static int getInt(String key) {
		return getInt(key, -1);
	}

	/**
	 * get int preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @param defaultValue
	 *            Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a int
	 */
	public static int getInt(String key, int defaultValue) {
		SharedPreferences settings = UIUtils.getContext().getSharedPreferences(
				PREFERENCE_NAME, Context.MODE_PRIVATE);
		return settings.getInt(key, defaultValue);
	}

	/**
	 * put long preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to modify
	 * @param value
	 *            The new value for the preference
	 * @return True if the new values were successfully written to persistent
	 *         storage.
	 */
	public static boolean putLong(String key, long value) {
		SharedPreferences settings = UIUtils.getContext().getSharedPreferences(
				PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(key, value);
		return editor.commit();
	}

	/**
	 * get long preferences 默认返回-1
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @return The preference value if it exists, or -1. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a long
	 * @see #getLong(Context, String, long)
	 */
	public static long getLong(String key) {
		return getLong(key, -1);
	}

	/**
	 * get long preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @param defaultValue
	 *            Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a long
	 */
	public static long getLong(String key, long defaultValue) {
		SharedPreferences settings = UIUtils.getContext().getSharedPreferences(
				PREFERENCE_NAME, Context.MODE_PRIVATE);
		return settings.getLong(key, defaultValue);
	}

	/**
	 * put float preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to modify
	 * @param value
	 *            The new value for the preference
	 * @return True if the new values were successfully written to persistent
	 *         storage.
	 */
	public static boolean putFloat(String key, float value) {
		SharedPreferences settings = UIUtils.getContext().getSharedPreferences(
				PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat(key, value);
		return editor.commit();
	}

	/**
	 * get float preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @return The preference value if it exists, or -1. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a float
	 * @see #getFloat(Context, String, float)
	 */
	public static float getFloat(String key) {
		return getFloat(key, -1);
	}

	/**
	 * get float preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @param defaultValue
	 *            Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a float
	 */
	public static float getFloat(String key, float defaultValue) {
		SharedPreferences settings = UIUtils.getContext().getSharedPreferences(
				PREFERENCE_NAME, Context.MODE_PRIVATE);
		return settings.getFloat(key, defaultValue);
	}

	/**
	 * put boolean preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to modify
	 * @param value
	 *            The new value for the preference
	 * @return True if the new values were successfully written to persistent
	 *         storage.
	 */
	public static boolean putBoolean(String key, boolean value) {
		SharedPreferences settings = UIUtils.getContext().getSharedPreferences(
				PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}

	/**
	 * get boolean preferences, default is false
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @return The preference value if it exists, or false. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a boolean
	 * @see #getBoolean(Context, String, boolean)
	 */
	public static boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	/**
	 * get boolean preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @param defaultValue
	 *            Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a boolean
	 */
	public static boolean getBoolean(String key, boolean defaultValue) {
		SharedPreferences settings = UIUtils.getContext().getSharedPreferences(
				PREFERENCE_NAME, Context.MODE_PRIVATE);
		return settings.getBoolean(key, defaultValue);
	}
}