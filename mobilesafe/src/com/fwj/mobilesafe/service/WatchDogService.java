package com.fwj.mobilesafe.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

import com.fwj.mobilesafe.activity.WatchDogActivity;
import com.fwj.mobilesafe.db.dao.WatchDogDao;

public class WatchDogService extends Service {
	private WatchDogDao dao;
	private UnlockReceiver receiver;
	private String unlockCurrentApp;
	private ScreenOffReceiver offReceiver;
	private List<String> queryAll;
	private ScreenOnReceiver onReceiver;
	boolean flag;
	private ActivityManager am;
	private Intent intent;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private class UnlockReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String packageName = intent.getStringExtra("packageName");
			unlockCurrentApp = packageName;
			
		}

	}

	private class ScreenOffReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			unlockCurrentApp = null;
			flag=false;
		}

	}
	private class ScreenOnReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			flag=true;
			dogRun();
		}
		
	}
	@Override
	public void onCreate() {
		super.onCreate();
		flag=true;
		// 解锁的广播接受者
		onReceiver=new ScreenOnReceiver();
		IntentFilter intentFilter3=new IntentFilter();
		intentFilter3.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(onReceiver, intentFilter3);
		
		offReceiver = new ScreenOffReceiver();
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(offReceiver, filter2);

		receiver = new UnlockReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.itheima.mobilesafe.unlock");
		registerReceiver(receiver, filter);

		dao = new WatchDogDao(this);
		queryAll = dao.queryAll();
		// 打开电视 看广告
		getContentResolver().registerContentObserver(Uri.parse("content://aaa.bbb.ccc"),
				true, new ContentObserver(new Handler()) {

					@Override
					public void onChange(boolean selfChange) {
						super.onChange(selfChange);
						queryAll=dao.queryAll();
					}
					
				});
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		intent = new Intent(
				WatchDogService.this,
				WatchDogActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		dogRun();

	}

	public void dogRun() {
		new Thread() {
			public void run() {
				
				while (flag) {
					@SuppressWarnings("deprecation")
					List<RunningTaskInfo> runningTasks = am.getRunningTasks(1);// 获得最近的任务栈
					for (RunningTaskInfo info : runningTasks) {
						ComponentName baseActivity = info.baseActivity; // 栈底的activity
						// info.topActivity; //栈顶的activity
						String packageName = baseActivity.getPackageName();
						System.out.println(packageName);
						if (queryAll.contains(packageName)) {
							// 判断程序是否是临时不加锁的程序
							if (!packageName.equals(unlockCurrentApp)) { // ==
								// 弹出输入密码的对话框
								// 打开activity
							
								intent.putExtra("packageName", packageName);
								// 只有activity 具有任务栈 服务没有 如果再服务或者广播之中打开aicitivty
								// 需要指定acitivty的任务栈
								// 但是一般情况下 如果当前程序已经有任务栈了 就会放到该任务栈,如果没有就会创建新的任务栈
						
								startActivity(intent);
							}
						}
					}

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		}.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			unregisterReceiver(receiver);
		}
		if (offReceiver != null) {
			unregisterReceiver(offReceiver);
		}
		if(onReceiver!=null){
			unregisterReceiver(onReceiver);
		}
	}
}
