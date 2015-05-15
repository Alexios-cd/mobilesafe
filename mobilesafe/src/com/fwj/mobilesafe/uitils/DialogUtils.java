package com.fwj.mobilesafe.uitils;

import android.app.Activity;
import android.app.ProgressDialog;

public class DialogUtils {
	static ProgressDialog dialog = null;

	/**
	 * ProgressDialog提示“正在加载数据”
	 * @param activity
	 */
	public static void showProgressDialog(Activity activity) {
		dialog = new ProgressDialog(activity);
		dialog.setMessage("正在加载数据");
		dialog.show();
	}
	/**
	 * ProgressDialog提示 message
	 * @param activity
	 * @param message
	 */
	public static void showProgressDialog(Activity activity,String message) {
		dialog = new ProgressDialog(activity);
		dialog.setMessage(message);
		dialog.show();
	}

	public static void CloseAllDialog() {
		if (null != dialog) {
			dialog.dismiss();
		}
	}
}
