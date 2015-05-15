package com.fwj.mobilesafe.service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.fwj.mobilesafe.R;
import com.fwj.mobilesafe.receiver.MyWigets;
import com.fwj.mobilesafe.receiver.ScreenOffReceiver;
import com.fwj.mobilesafe.uitils.TaskUtils;

public class WidgetsService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private AppWidgetManager appWidgetManager;
	private Timer timer;
	private ClearReceiver receiver;
	private ScreenOffReceiver offReceiver;
	private ScreenOffReceiver2 offReceiver2;
	private ScreenOnReceiver onReceiver;
	private class ClearReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			ActivityManager manager=(ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> runningAppProcesses = manager.getRunningAppProcesses();
			for(RunningAppProcessInfo info:runningAppProcesses){
				if(info.processName.equals(getPackageName())){
					continue;
				}
				manager.killBackgroundProcesses(info.processName);
			}
		}
	}
	private class ScreenOffReceiver2 extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if(timer!=null){
				timer.cancel();
				timer=null;
			}
		}
		
	}
	private class ScreenOnReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			update();
		}
		
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		offReceiver=new ScreenOffReceiver();
		IntentFilter filter2=new IntentFilter();
		filter2.addAction("android.intent.action.SCREEN_OFF");
		registerReceiver(offReceiver, filter2);
		
		offReceiver2=new ScreenOffReceiver2();
		registerReceiver(offReceiver2, filter2);
		
		onReceiver =new ScreenOnReceiver();
		IntentFilter filter3=new IntentFilter();
		filter3.addAction("android.intent.action.SCREEN_ON");
		registerReceiver(onReceiver, filter3);
		
		receiver=new ClearReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction("com.itheima.mobilesafe.clearall");
		registerReceiver(receiver, filter);
		
		update();

	}

	public void update() {
		appWidgetManager = AppWidgetManager.getInstance(this);
		// 更新widgets 界面
		final ComponentName provider = new ComponentName(this, MyWigets.class);
		// 第一个参数 widgets组件的名字 第二个参数
		// 1 程序的包名
		final RemoteViews views = new RemoteViews(getPackageName(),
				R.layout.process_widget);
		timer = new Timer();
		// 创建定时任务
		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				System.out.println("aaaaa");
				// 修改远程view对象中的textView 里的text文本 第一个参数 textView 对象id
				views.setTextViewText(R.id.process_count, "当前运行的进程数:"
						+ TaskUtils.getTaskCount(WidgetsService.this));
				views.setTextViewText(
						R.id.process_memory,
						"可用内存:"
								+ Formatter
										.formatFileSize(
												WidgetsService.this,
												TaskUtils
														.getAvailableRam(WidgetsService.this)));
				Intent intent=new Intent("com.itheima.mobilesafe.clearall");
				// 创建了延期意图 发送广播
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						WidgetsService.this, 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				// 第一个参数 button的id
				views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
				appWidgetManager.updateAppWidget(provider, views);
			}
		};
		// 开启定时任务
		timer.schedule(task, 1000, 3000);
	}

	@Override
	public void onDestroy() {
		if (timer != null) {
			timer.cancel();// 取消定时任务
			timer = null;
		}
		if(receiver!=null){
			unregisterReceiver(receiver);
		}
		if(onReceiver!=null){
			unregisterReceiver(onReceiver);
		}
		if(offReceiver!=null){
			unregisterReceiver(offReceiver);
		}
		if(offReceiver2!=null){
			unregisterReceiver(offReceiver2);
		}
		super.onDestroy();
	}
}
