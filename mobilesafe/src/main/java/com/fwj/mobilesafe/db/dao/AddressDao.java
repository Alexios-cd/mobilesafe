package com.fwj.mobilesafe.db.dao;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDao {
	private Context context;

	public AddressDao(Context context) {
		super();
		this.context = context;
	}

	public String queryAddress(String phonenum) {
		String result = null;
		File file = new File(context.getFilesDir(), "address.db");
		SQLiteDatabase database = SQLiteDatabase.openDatabase(
				file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
		// 1数据库的路径     
		// 正则表达式   // 身份证   ^\d{17}[0-9x]$   前6位 地区    后8位 生日  后四位   倒数第二位  倒数第一位加密算法
		//  ^1[34578]\d{9}$
		if (phonenum.matches("1[34578]\\d{9}")) {
		
			Cursor cursor = database
					.rawQuery(
							"select location from data2 where id=(select outkey from data1 where id=?);",
							new String[] { phonenum.substring(0, 7) });
			if (cursor.moveToNext()) {
				result = cursor.getString(0);
			}
			cursor.close();

		}else{
			switch (phonenum.length()) {
			case 3:   // 110  120  999  911
				result="特殊号码";
				break;
			case 4 :
			
				result="模拟器";
				break;
			case 5:   // 95588
				result="服务电话";
				break;
			case 6:    // 本地电话
			case 7:
			case 8:  
				result="本地电话";
				break;  
			default:
				if(phonenum.startsWith("0")&&phonenum.length()>=10){
					//result="长途电话";  phonenum   1112313123
					//  3 位   4位 
					Cursor cursor = database.rawQuery("select location from data2 where area=?", new String[]{phonenum.substring(1, 3)});
					if(cursor.moveToNext()){
						result=cursor.getString(0);
						result=result.substring(0, result.length()-2);
						cursor.close();
					}else{
						cursor=database.rawQuery("select location from data2 where area=?", new String[]{phonenum.substring(1, 4)});
						if(cursor.moveToNext()){
							result=cursor.getString(0);
							result=result.substring(0, result.length()-2);
							cursor.close();
						}
					}
					
				}
				break;
			}
		}
		return result;

	}

}
