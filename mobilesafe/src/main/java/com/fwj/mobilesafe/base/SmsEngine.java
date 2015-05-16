package com.fwj.mobilesafe.base;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;

public class SmsEngine {
	//  定义一个接口
	public interface ProcessListener{
		void max(int max);
		void process(int process);
	}
	// 方法里引用接口 
	public static void backupSms(Context context,ProcessListener listener) {
		
		ContentResolver contentResolver = context.getContentResolver();
		XmlSerializer serializer=Xml.newSerializer();
		
		try {
			// 初始化 xml文件生成器 
			serializer.setOutput(new FileOutputStream(new File("/mnt/sdcard/backup.xml")), "utf-8");
			serializer.startDocument("utf-8", true); // xml头信息
			serializer.startTag(null, "smss");
			Uri uri = Uri.parse("content://sms");
			Cursor cursor = contentResolver.query(uri, new String[] { "address",
					"date", "type", "body" }, null, null, null);
			int max=cursor.getCount();
			//dialog.setMax(max);
		//	pb_process.setMax(max);
			listener.max(max);
			int count=0;
			while(cursor.moveToNext()){
				Thread.sleep(500);
				serializer.startTag(null, "sms");
				
				serializer.startTag(null, "address");
				String address=cursor.getString(0);
				serializer.text(address);
				serializer.endTag(null, "address");
				
				serializer.startTag(null, "date");
				String date=cursor.getString(1);
				serializer.text(date);
				serializer.endTag(null, "date");
				
				serializer.startTag(null, "type");
				String type=cursor.getString(2);
				serializer.text(type);
				serializer.endTag(null, "type");
				
				serializer.startTag(null, "body");
				String body=cursor.getString(3);
				serializer.text(body);
				serializer.endTag(null, "body");
				
				
				serializer.endTag(null, "sms");
				count++;
				listener.process(count);
				//dialog.setProgress(count);
				//pb_process.setProgress(count);
			}
			serializer.endTag(null, "smss");
			serializer.endDocument();// 结束
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
			
		}
}
