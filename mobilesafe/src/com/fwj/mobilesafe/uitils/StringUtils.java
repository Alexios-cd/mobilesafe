package com.fwj.mobilesafe.uitils;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

import android.graphics.Paint;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * 字符串工具类<br>
 * 主要功能：<br>
 * 1.判断字符串是否有值isEmpty<br>
 * 2.判断多个字符串是否相等isEquals<br>
 * 3.给字体加下划线setUnderLine<br>
 * 
 * @author 傅文江
 * @2015-1-7 上午11:10:59 修订时间： 修订内容：
 */
public class StringUtils {
	public final static String UTF_8 = "utf-8";

	/** 判断字符串是否有值，如果为null或者是空字符串或者只有空格或者为"null"字符串，则返回true，否则则返回false */
	public static boolean isEmpty(String value) {
		if (value != null && !"".equalsIgnoreCase(value.trim())
				&& !"null".equalsIgnoreCase(value.trim())) {
			return false;
		} else {
			return true;
		}
	}

	/** 判断多个字符串是否相等，如果其中有一个为空字符串或者null，则返回false，只有全相等才返回true */
	public static boolean isEquals(String... agrs) {
		String last = null;
		for (int i = 0; i < agrs.length; i++) {
			String str = agrs[i];
			if (isEmpty(str)) {
				return false;
			}
			if (last != null && !str.equalsIgnoreCase(last)) {
				return false;
			}
			last = str;
		}
		return true;
	}

	/**
	 * 返回一个高亮spannable
	 * 
	 * @param content
	 *            文本内容
	 * @param color
	 *            高亮颜色
	 * @param start
	 *            起始位置
	 * @param end
	 *            结束位置
	 * @return 高亮spannable
	 */
	public static CharSequence getHighLightText(String content, int color,
			int start, int end) {
		if (TextUtils.isEmpty(content)) {
			return "";
		}
		start = start >= 0 ? start : 0;
		end = end <= content.length() ? end : content.length();
		SpannableString spannable = new SpannableString(content);
		CharacterStyle span = new ForegroundColorSpan(color);
		spannable.setSpan(span, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannable;
	}

	/**
	 * 获取链接样式的字符串，即字符串下面有下划线
	 * 
	 * @param resId
	 *            文字资源
	 * @return 返回链接样式的字符串
	 */
	public static Spanned getHtmlStyleString(int resId) {
		StringBuilder sb = new StringBuilder();
		sb.append("<a href=\"\"><u><b>").append(UIUtils.getString(resId))
				.append(" </b></u></a>");
		return Html.fromHtml(sb.toString());
	}

	/** 格式化文件大小，不保留末尾的0 */
	public static String formatFileSize(long len) {
		return formatFileSize(len, false);
	}

	/** 格式化文件大小，保留末尾的0，达到长度一致 */
	public static String formatFileSize(long len, boolean keepZero) {
		String size;
		DecimalFormat formatKeepTwoZero = new DecimalFormat("#.00");
		DecimalFormat formatKeepOneZero = new DecimalFormat("#.0");
		if (len < 1024) {
			size = String.valueOf(len + "B");
		} else if (len < 10 * 1024) {
			// [0, 10KB)，保留两位小数
			size = String.valueOf(len * 100 / 1024 / (float) 100) + "KB";
		} else if (len < 100 * 1024) {
			// [10KB, 100KB)，保留一位小数
			size = String.valueOf(len * 10 / 1024 / (float) 10) + "KB";
		} else if (len < 1024 * 1024) {
			// [100KB, 1MB)，个位四舍五入
			size = String.valueOf(len / 1024) + "KB";
		} else if (len < 10 * 1024 * 1024) {
			// [1MB, 10MB)，保留两位小数
			if (keepZero) {
				size = String.valueOf(formatKeepTwoZero.format(len * 100 / 1024
						/ 1024 / (float) 100))
						+ "MB";
			} else {
				size = String.valueOf(len * 100 / 1024 / 1024 / (float) 100)
						+ "MB";
			}
		} else if (len < 100 * 1024 * 1024) {
			// [10MB, 100MB)，保留一位小数
			if (keepZero) {
				size = String.valueOf(formatKeepOneZero.format(len * 10 / 1024
						/ 1024 / (float) 10))
						+ "MB";
			} else {
				size = String.valueOf(len * 10 / 1024 / 1024 / (float) 10)
						+ "MB";
			}
		} else if (len < 1024 * 1024 * 1024) {
			// [100MB, 1GB)，个位四舍五入
			size = String.valueOf(len / 1024 / 1024) + "MB";
		} else {
			// [1GB, ...)，保留两位小数
			size = String.valueOf(len * 100 / 1024 / 1024 / 1024 / (float) 100)
					+ "GB";
		}
		return size;
	}

	/**
	 * 给字加下划线
	 * 
	 * @param TextiView
	 *            需要加下划线的TextView
	 * @param color
	 *            颜色id
	 */
	public static void setUnderLine(TextView v, int color) {
		v.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		v.setTextColor(color);
	}

	/**
	 * 判断字符串是否为空或长度为0 或由空格组成
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		return (str == null || str.trim().length() == 0);
	}

	/**
	 * 判断支字符串时候含有英文字符
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isObtainLetter(String str) {
		return Pattern.compile("(?i)[a-z]").matcher(str).find();
	}

	/**
	 * 验证邮箱格式
	 * 
	 * @param email
	 *            要验证的邮箱
	 * @return 格式是否正确
	 */
	public static boolean isEmail(String email) {
		if (TextUtils.isEmpty(email)) {
			return false;
		}
		return email
				.matches("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	}

	/**
	 * 验证手机格式
	 * 
	 * @param mobile
	 *            要验证的手机号码
	 * @return 格式是否正确
	 */
	public static boolean isMobile(String mobile) {
		if (TextUtils.isEmpty(mobile)) {
			return false;
		}
		return mobile.matches("^1[0-9][0-9]\\d{8}$");
	}

	/**
	 * 验证身份证号格式(只验证格式，不验证正确性)
	 * 
	 * @param card
	 *            要验证身份证号
	 * @return 格式是否正确
	 */
	public static boolean isCard(String card) {
		if (TextUtils.isEmpty(card)) {
			return false;
		}
		return card.matches("^[1-9](\\d{16}|\\d{13})[0-9xX]$");
	}

	/**
	 * 判断str是否包含汉字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isObtainHanZi(String str) {
		String b = null;
		for (int i = str.length(); --i >= 0;) {
			b = str.substring(i, i + 1);
			boolean c = java.util.regex.Pattern.matches("[\u4E00-\u9FA5]", b);
			if (c) {
				return true;
			}
		}
		return false;

	}

	/**
	 * 对手机号码中间四位进行加密
	 * 
	 * @param num
	 *            必须为11位手机号码
	 * @return 传入参数正常正常时返回加密字符串，否则返回NULL
	 */
	public static String getEncryptPhoneNum(String num) {
		if (11 != num.length()) {
			return null;
		}
		return num.subSequence(0, 3) + "****" + num.subSequence(7, 11);

	}

	/**
	 * 判断是否是中文
	 * 
	 * @param a
	 * @return
	 */
	public static boolean isChinese(char a) {
		int v = (int) a;
		return (v >= 19968 && v <= 171941);
	}

	/**
	 * 判断是否含有中文
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isChinese(String s) {
		if (null == s || "".equals(s.trim()))
			return false;
		for (int i = 0; i < s.length(); i++) {
			if (isChinese(s.charAt(i)))
				return true;
		}
		return false;
	}

	/**
	 * 得到中英文混合字符的长度
	 * 
	 * @param str
	 * @return 中文字数*2+英文字数 有异常返回-1
	 */
	public static int getLength(String str) {
		try {
			return new String(str.getBytes("gb2312"), "iso-8859-1").length();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return -1;
		}
	}
}
