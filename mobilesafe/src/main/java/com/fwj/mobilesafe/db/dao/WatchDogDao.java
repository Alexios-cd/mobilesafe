package com.fwj.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.fwj.mobilesafe.db.WatchDogOpenHelper;
import com.fwj.mobilesafe.uitils.LogUtils;

public class WatchDogDao {

	private WatchDogOpenHelper helper;
	private Context context;

	public WatchDogDao(Context context) {
		this.context = context;
		helper = new WatchDogOpenHelper(context);
	}

	/**
	 * 添加加锁程序
	 * 
	 * @param packageName
	 *            程序的包名
	 */
	public void addApp(String packageName) {
		// helper.getReadableDatabase(); // 没有加锁的数据库
		SQLiteDatabase sql = helper.getWritableDatabase();// 加锁的数据库
		ContentValues values = new ContentValues();
		values.put("packagename", packageName);
		// 表名
		sql.insert("info", null, values);
		// 发布一个通知  告诉观察当前数据库的内容观察者 数据有更新   uri (发布广告平台)
		context.getContentResolver().notifyChange(Uri.parse("content://aaa.bbb.ccc"), null);
		sql.close();
		LogUtils.e(packageName+"加锁成功！");
	}

	/**
	 * 删除加锁的程序
	 * 
	 * @param num
	 *            根据包名删除
	 */
	public void deleteApp(String packageName) {
		SQLiteDatabase sql = helper.getWritableDatabase();// 加锁的数据库
		sql.delete("info", "packagename=?", new String[] { packageName });
		sql.close();
		context.getContentResolver().notifyChange(Uri.parse("content://aaa.bbb.ccc"), null);
		LogUtils.e(packageName + "解除锁成功！");
	}
	/**
	 * 根据包名 查询是否在数据库中
	 * 
	 * @return
	 */
	public boolean queryPackageNameLocked(String packageName) {
		boolean flag;
		SQLiteDatabase sql = helper.getReadableDatabase();
		Cursor cursor = sql.query("info", new String[] { "packagename" },
				"packagename=?",new String[]{packageName} , null, null,null);
		if(cursor.moveToNext()){
			flag=true;
		}else{
			flag=false;
		}
		cursor.close();  // 几十毫秒 
		sql.close();
		LogUtils.i(packageName + "是否在数据库中"+flag);
		return flag;

	}



	/**
	 * 返回全部加锁程序
	 * 
	 * @return
	 */
	public List<String> queryAll() {
		List<String> watchDogInfos = new ArrayList<String>();
		SQLiteDatabase sql = helper.getReadableDatabase();
		Cursor cursor = sql.query("info", new String[] { "packagename" },
				null, null, null, null,null);
		while (cursor.moveToNext()) {
			String packageName = cursor.getString(0);
			watchDogInfos.add(packageName);
		}
		cursor.close();
		sql.close();
		return watchDogInfos;

	}

}
