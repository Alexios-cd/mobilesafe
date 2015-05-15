package com.fwj.mobilesafe.uitils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceUtils {
	/**
	 * 判断服务是否开启(运行)
	 * @param serviceName 服务名字
	 * @param context 上下文
	 * @return
	 */
	public static boolean isRunningService(String serviceName,Context context){
		// 进程的管理者
		ActivityManager manager=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runningServices = manager.getRunningServices(1000);// 参数代表 最大返回的集合数量
		for(RunningServiceInfo info:runningServices){
			
			String className = info.service.getClassName();
			if(className.equals(serviceName)){
				return true;
			}
		}
		
		
		return false;
		
	}
}
