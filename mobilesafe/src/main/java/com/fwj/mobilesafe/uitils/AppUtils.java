package com.fwj.mobilesafe.uitils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class AppUtils {
	private static InputMethodManager imm;


	

	public static void closeDB(Cursor cursor, SQLiteDatabase db) {
		if (null != db)
			try {
				db.close();
			} catch (Exception e) {
			}
		if (null != cursor)
			try {
				cursor.close();
			} catch (Exception e) {

			}
	}

	/**
	 * 显示软键盘
	 */
	public static void showSoft(EditText view, Context context) {
		if (null == imm)
			imm = (InputMethodManager) UIUtils.getContext().getSystemService(
					Context.INPUT_METHOD_SERVICE);
		view.requestFocus();
		imm.showSoftInput(view, 0);
	}

	/**
	 * 显示软键盘
	 */
	public static void showSoft(EditText view) {
		if (null == imm)
			imm = (InputMethodManager) UIUtils.getContext().getSystemService(
					Context.INPUT_METHOD_SERVICE);
		view.requestFocus();
		imm.showSoftInput(view, 0);
	}

	/**
	 * 隐藏软键盘
	 */
	public static void hideSoft(EditText view, Context context) {
		if (null == imm)
			imm = (InputMethodManager) context
					.getSystemService(Context.INPUT_METHOD_SERVICE);
		view.clearFocus();
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
	/**
	 * 判断当前应用程序是否处于后台，通过getRunningTasks的方式
	 * @return true 在后台; false 在前台
	 */
	public static boolean isApplicationBroughtToBackgroundByTask(String packageName, Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
		if (!tasks.isEmpty()) {
			ComponentName topActivity = tasks.get(0).topActivity;
			if (!topActivity.getPackageName().equals(packageName)) {
				return true;
			}
		}
		return false;
	}

	
}
