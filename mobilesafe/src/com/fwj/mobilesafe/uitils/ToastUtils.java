package com.fwj.mobilesafe.uitils;

import android.text.TextUtils;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 吐司工具类
 * 
 * @author 傅文江
 * @2015-1-7 下午4:10:05 修订时间： 修订内容：
 */
public class ToastUtils {
	private static Toast toast = null;

	/**
	 * 普通文本消息提示
	 * 
	 * @param context
	 * @param text
	 * @param duration
	 */
	public static void TextToast(CharSequence text, int duration) {
		if (TextUtils.isEmpty(text)) {
			LogUtils.e("吐丝为空，不予弹出");
			return;
		}
		// 防止吐司短时间内重复弹出
		if (null != toast) {
			toast.cancel();
		}
		// 创建一个Toast提示消息
		toast = Toast.makeText(UIUtils.getContext(), text, duration);
		// 设置Toast提示消息在屏幕上的位置
		toast.setGravity(Gravity.CENTER, 0, 0);
		// 显示消息
		toast.show();
	}

	/**
	 * 普通文本消息提示 (短时间提示)
	 * 
	 * @param context
	 * @param text
	 * @param duration
	 */
	public static void TextToast(CharSequence text) {
		TextToast(text, Toast.LENGTH_SHORT);
	}

	/**
	 * 带图片消息提示 (使用此方法应该注意，图片应该尽量小)
	 * 
	 * @param ImageResourceId
	 * @param text
	 * @param duration
	 */
	public static void ImageToast(int ImageResourceId, CharSequence text,
			int duration) {
		// 创建一个Toast提示消息
		toast = Toast.makeText(UIUtils.getContext(), text, duration);
		// 设置Toast提示消息在屏幕上的位置
		toast.setGravity(Gravity.CENTER, 0, 0);
		// 创建一个ImageView
		ImageView img = new ImageView(UIUtils.getContext());
		img.setImageResource(ImageResourceId);
		LinearLayout toastView = (LinearLayout) toast.getView();
		toastView.setOrientation(LinearLayout.HORIZONTAL);
		toastView.setGravity(Gravity.CENTER);
		img.setPadding(0, 0, toastView.getPaddingLeft() / 2, 0);
		toastView.addView(img, 0);
		toast.show();
	}

	/**
	 * 带图片消息提示 (短时间提示)
	 * 
	 * @param context
	 * @param ImageResourceId
	 * @param text
	 * @param duration
	 */
	public static void ImageToast(int ImageResourceId, CharSequence text) {
		ImageToast(ImageResourceId, text, Toast.LENGTH_SHORT);
	}

	public static void closeAllToast() {
		if (null != toast) {
			toast.cancel();
		}
	}


}
