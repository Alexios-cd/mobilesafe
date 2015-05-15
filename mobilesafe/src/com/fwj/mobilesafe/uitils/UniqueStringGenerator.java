package com.fwj.mobilesafe.uitils;

/**
 * ============================================================
 * 
 * 版权 ：鼎开互联集团 版权所有 (c) 2014
 * 
 * 版本 ：var1.3
 * 
 * 创建日期 ：2014年6月17日上午10:46:28
 * 
 * 描述 ： 生产唯一字符串
 * 
 * 修订历史 ： ============================================================
 **/
public class UniqueStringGenerator {

	private UniqueStringGenerator() {
	}

	public static synchronized String getUniqueString() {
		if (generateCount > 99999)
			generateCount = 0;
		String uniqueNumber = Long.toString(System.currentTimeMillis()) + Integer.toString(generateCount);
		generateCount++;
		return uniqueNumber;
	}

	private static int generateCount = 0;
}