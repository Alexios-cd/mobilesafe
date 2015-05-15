package com.fwj.mobilesafe.applaction;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.testin.agent.TestinAgent;

public class mApplication extends Application {
	// 主线程的上下文
	private static mApplication mMainContext;
	// 获取到主线程的Handler
	private static Handler mMainThreadHandler;
	// 获取到主线程的looper
	private static Looper mMainThreadLooper;
	// 获取到主线程的id
	private static int mMainThreadId;
	// 获取到程序的主线程
	private static Thread mMainThead;

	@Override
	public void onCreate() {
		super.onCreate();
		mApplication.mMainContext = this;
		mApplication.mMainThreadHandler = new Handler();
		mApplication.mMainThreadLooper = getMainLooper();
		mApplication.mMainThreadId = android.os.Process.myTid();
		mApplication.mMainThead = Thread.currentThread();
		iniTestAgent();
	}


	/**
	 * 初始化云测
	 */
	private void iniTestAgent() {
		TestinAgent.init(this, "2835bf85d3d848fffe432f7e165aa2b3", "草鱼守护1.0_4.10");
	}

	public static mApplication getApplication() {
		return mMainContext;
	}

	public static Handler getMainThreadHandler() {
		return mMainThreadHandler;
	}

	public static Looper getMainThreadLooper() {
		return mMainThreadLooper;
	}

	public static int getMainThreadId() {
		return mMainThreadId;
	}

	public static Thread getMainThread() {
		return mMainThead;
	}
}
