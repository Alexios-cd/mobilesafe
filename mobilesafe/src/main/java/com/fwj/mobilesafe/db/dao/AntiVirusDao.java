package com.fwj.mobilesafe.db.dao;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AntiVirusDao {
	private Context context;

	public AntiVirusDao(Context context) {
		super();
		this.context = context;
	}
	/**
	 * 查询程序是否是病毒
	 * @param md5  程序签名加密后的值
	 * @return
	 */
	public boolean isVirus(String md5) {
		boolean flag;
		File file = new File(context.getFilesDir(), "antivirus.db");
		SQLiteDatabase database = SQLiteDatabase.openDatabase(
				file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = database.query("datable", new String[] { "desc" },
				"md5=?", new String[] { md5 }, null, null, null);
		if (cursor.moveToNext()) {
			flag=true;
		} else {
			flag=false;
		}
		cursor.close();
		database.close();
		return flag;
	}
}
