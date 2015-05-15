package com.fwj.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;

import com.fwj.mobilesafe.domain.TaskInfo;

public class TaskProvider {
	/**
	 * 获取到所有的进程信息
	 * @param context
	 * @return
	 */
	public static List<TaskInfo> getTaskInfos(Context context){
		List<TaskInfo> taskInfos=new ArrayList<TaskInfo>();
		ActivityManager am=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
		for(RunningAppProcessInfo appProcessInfo:runningAppProcesses){
			TaskInfo taskInfo=new TaskInfo();
			
			String packageName=appProcessInfo.processName;
			taskInfo.setPackageName(packageName);
			// 内存  根据pid 获取内存信息  接受参数数组 ,参数数组中有几个元素 返回值数组中就有几个元素
			MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(new int[]{appProcessInfo.pid});
			long mem=processMemoryInfo[0].getTotalPss()*1024;
			taskInfo.setMem(mem);
			PackageManager  manager=context.getPackageManager();
			try {
				ApplicationInfo applicationInfo = manager.getApplicationInfo(packageName, 0);
				String name = applicationInfo.loadLabel(manager).toString();
				Drawable icon = applicationInfo.loadIcon(manager);
				taskInfo.setName(name);
				taskInfo.setIcon(icon);
				if((applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==ApplicationInfo.FLAG_SYSTEM){
					taskInfo.setUser(false);
				}else{
					taskInfo.setUser(true);
				}
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			
			taskInfos.add(taskInfo);
		}
		return taskInfos;
	}
}
