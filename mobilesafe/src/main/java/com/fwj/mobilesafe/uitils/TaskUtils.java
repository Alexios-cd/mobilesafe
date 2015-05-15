package com.fwj.mobilesafe.uitils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;

public class TaskUtils {

	/**
	 * 获取到当前的进程数
	 * 
	 * @param context
	 * @return
	 */
	public static int getTaskCount(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		return am.getRunningAppProcesses().size();
	}

	/**
	 * 获取到手机可用的ram
	 * 
	 * @param context
	 * @return
	 */
	public static long getAvailableRam(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo(); // 创建了一张白纸 用来记录ram 信息
		am.getMemoryInfo(outInfo);// 把手机的ram信息 写到白纸上
		return outInfo.availMem;
	}

	/**
	 * 获取到手机总共的ram
	 * 
	 * @param context
	 * @return
	 */
	// public static long getTotalRam1(Context context){
	// ActivityManager am=(ActivityManager)
	// context.getSystemService(Context.ACTIVITY_SERVICE);
	// MemoryInfo outInfo=new MemoryInfo(); // 创建了一张白纸 用来记录ram 信息
	// am.getMemoryInfo(outInfo);// 把手机的ram信息 写到白纸上
	// return outInfo.totalMem;
	//
	// }
	public static long getTotalRam(){
		  BufferedReader bufferedReader = null;
		try {
			FileReader reader=new FileReader(new File("/proc/meminfo"));
			bufferedReader = new BufferedReader(reader);
			String str=bufferedReader.readLine();
			StringBuilder sb=new StringBuilder();
			char[] charArray = str.toCharArray();
			for(char c:charArray){
				if(c>='0'&&c<='9'){
					sb.append(c);
				}
			}
			long parseLong = Long.parseLong(sb.toString());
			return parseLong*1024;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return 0;
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}finally{
			if(null!=bufferedReader){
				try {
					bufferedReader.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		
		
	}
}
