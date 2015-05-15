package com.fwj.mobilesafe.uitils;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.fwj.mobilesafe.applaction.mApplication;

/**
 * UI工蕨类
 * 
 * @author 傅文江
 * @2015-1-7 上午11:13:14 修订时间： 修订内容：
 */
public class UIUtils {

	public static Context getContext() {
		return mApplication.getApplication();
	}

	public static Thread getMainThread() {
		return mApplication.getMainThread();
	}

	public static long getMainThreadId() {
		return mApplication.getMainThreadId();
	}

	/** dip转换px */
	public static int dip2px(int dip) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f);
	}

	/** pxz转换dip */
	public static int px2dip(int px) {
		final float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (px / scale + 0.5f);
	}

	/** 获取主线程的handler */
	public static Handler getHandler() {
		return mApplication.getMainThreadHandler();
	}

	/** 延时在主线程执行runnable */
	public static boolean postDelayed(Runnable runnable, long delayMillis) {
		return getHandler().postDelayed(runnable, delayMillis);
	}

	/** 在主线程执行runnable */
	public static boolean post(Runnable runnable) {
		return getHandler().post(runnable);
	}

	/** 从主线程looper里面移除runnable */
	public static void removeCallbacks(Runnable runnable) {
		getHandler().removeCallbacks(runnable);
	}

	public static View inflate(int resId) {
		return LayoutInflater.from(getContext()).inflate(resId, null);
	}

	/** 获取资源 */
	public static Resources getResources() {
		return getContext().getResources();
	}

	/** 获取文字 */
	public static String getString(int resId) {
		return getResources().getString(resId);
	}

	/** 获取文字数组 */
	public static String[] getStringArray(int resId) {
		return getResources().getStringArray(resId);
	}

	/** 获取dimen */
	public static int getDimens(int resId) {
		return getResources().getDimensionPixelSize(resId);
	}

	/** 获取drawable */
	@SuppressWarnings("deprecation")
	public static Drawable getDrawable(int resId) {
		return getResources().getDrawable(resId);
	}

	/** 获取颜色 */
	public static int getColor(int resId) {
		return getResources().getColor(resId);
	}

	/** 获取颜色选择器 */
	public static ColorStateList getColorStateList(int resId) {
		return getResources().getColorStateList(resId);
	}

	// 判断当前的线程是不是在主线程
	public static boolean isRunInMainThread() {
		return android.os.Process.myTid() == getMainThreadId();
	}

	public static void runInMainThread(Runnable runnable) {
		if (isRunInMainThread()) {
			runnable.run();
		} else {
			post(runnable);
		}
	}

	/** 对toast的简易封装。线程安全，可以在非UI线程调用。 */
	public static void showToastSafe(final int resId) {
		showToastSafe(getString(resId));
	}

	/** 对toast的简易封装。线程安全，可以在非UI线程调用。 */
	public static void showToastSafe(final String str) {
		if (isRunInMainThread()) {
			showToast(str);
		} else {
			post(new Runnable() {
				@Override
				public void run() {
					showToast(str);
				}
			});
		}
	}

	private static void showToast(String str) {
		Toast.makeText(UIUtils.getContext(), str, Toast.LENGTH_SHORT).show();
	}

	/**
	 * 得到屏幕的高度
	 * 
	 * @param activity
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static int getSreenHeight(Activity activity) {
		return activity.getWindowManager().getDefaultDisplay().getHeight();
	}

	/**
	 * 得到屏幕的宽度
	 * 
	 * @param activity
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static int getSreenWidth(Activity activity) {
		return activity.getWindowManager().getDefaultDisplay().getWidth();
	}

	/**
	 * 得到一个控件相对于屏幕左侧的位置
	 * 
	 * @param view
	 * @return
	 */
	public static int getLeftOnScreen(View view) {
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		return location[0];

	}
	/**
	 * 得到一个控件相对于屏幕左侧的位置
	 * 
	 * @param view
	 * @return
	 */
	public static int getRightOnScreen(View view) {
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		return location[0];
		
	}

	/**
	 * 得到一个控件相对于屏幕顶部的位置
	 * 
	 * @param view
	 * @return
	 */
	public static int getTopOnScreen(View view) {
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		return location[1];

	}

}
