package com.fwj.mobilesafe.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;

import com.fwj.mobilesafe.domain.AppInfo;

public class AppProvider {
	/**
	 * 返回程序所有应用程序的信息
	 * @param context
	 * @return
	 */
	public static List<AppInfo>  getAllAppInfos(Context context){
		List<AppInfo> appInfos=new ArrayList<AppInfo>();
		
		PackageManager manager=context.getPackageManager();
		// 返回所有已经安装应用程序的信息
		List<PackageInfo> installedPackages = manager.getInstalledPackages(0);
		for(PackageInfo info:installedPackages){
			AppInfo appInfo=new AppInfo();
			String packageName = info.packageName;
			appInfo.setPackageName(packageName);
			String versionName = info.versionName;
			appInfo.setVersion(versionName);
			String name = info.applicationInfo.loadLabel(manager).toString();
			appInfo.setName(name);
			Drawable icon=info.applicationInfo.loadIcon(manager);
			appInfo.setIcon(icon);
			// 获取到了 当前应用程序的标签  (装备)
			int flags = info.applicationInfo.flags;
			if((flags&ApplicationInfo.FLAG_SYSTEM)==ApplicationInfo.FLAG_SYSTEM){
				appInfo.setUser(false);
			}else{
				appInfo.setUser(true);
			}
			if((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)==ApplicationInfo.FLAG_EXTERNAL_STORAGE){
				appInfo.setSD(true);
			}else{
				appInfo.setSD(false);
			}
			
			appInfos.add(appInfo);
			
		}
		
		
		return appInfos;
		
		
	}
	/**
	 * 获取到了sd卡可用的内存
	 * @param context
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getSDAvailableSpace(Context context){
			// sd 路径 
		  File path = Environment.getExternalStorageDirectory();
          StatFs stat = new StatFs(path.getPath());
          long blockSize = stat.getBlockSize();  		// 获取到每块内存的大小
          //  long totalBlocks = stat.getBlockCount(); // 获取到所有的块数
          long availableBlocks = stat.getAvailableBlocks(); // 获取到可用的块数
          long space=availableBlocks * blockSize;
          String availableSpace=Formatter.formatFileSize(context, space);
          return availableSpace;
		
	}
	/**
	 * 获取到了rom可用的内存
	 * @param context
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getRomAvailableSpace(Context context){
		// sd 路径
		   File path = Environment.getDataDirectory();  //22 Eclispse  ADT   android
           StatFs stat = new StatFs(path.getPath());
           long blockSize = stat.getBlockSize();  // 获取到每块内存的大小
         //  long totalBlocks = stat.getBlockCount(); // 获取到所有的块数
           long availableBlocks = stat.getAvailableBlocks(); // 获取到可用的块数
          long space=availableBlocks * blockSize;   
          String availableSpace=Formatter.formatFileSize(context, space);
		return availableSpace;
		
	}
}
