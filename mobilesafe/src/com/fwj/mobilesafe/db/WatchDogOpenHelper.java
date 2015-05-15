package com.fwj.mobilesafe.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class WatchDogOpenHelper extends SQLiteOpenHelper {

	public WatchDogOpenHelper(Context context) {
		// name 数据库的名字 factory 游标工厂 version 版本号
		super(context, "watchdog.db", null, 1);
	}

	// 数据库第一次创建的时候调用 适合初始化表结构
	// id 包名
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table info(id integer primary key autoincrement,packagename varchar(20));");
	}
	// 数据版本更新的时候调用
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
