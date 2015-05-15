package com.fwj.mobilesafe.receiver;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenOffReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ActivityManager manager=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = manager.getRunningAppProcesses();
		for(RunningAppProcessInfo info:runningAppProcesses){
			if(info.processName.equals(context.getPackageName())){
				continue;
			}
			manager.killBackgroundProcesses(info.processName);
		}
	}

}
