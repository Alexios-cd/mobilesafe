package com.fwj.mobilesafe.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.fwj.mobilesafe.domain.ContactsInfo;

public class ContactsEngine {
	/**
	 * 获取系统的联系人
	 * 
	 * @param context
	 * @return
	 */
	public static List<ContactsInfo> getContacts(Context context) {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<ContactsInfo> contactsInfos = new ArrayList<ContactsInfo>();
		// 内容解析者
		ContentResolver resolver = context.getContentResolver();
		// 参数 1 对应的数据库的uri地址
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");// 代表查询raw_contacts表
		Uri dataUri = Uri.parse("content://com.android.contacts/data");// 代表查询view_data表
		// 参数2 查询的哪一列
		Cursor cursor_contacts = resolver.query(uri,
				new String[] { "contact_id" }, null, null, null);
		while (cursor_contacts.moveToNext()) {
			String id = cursor_contacts.getString(0);
			ContactsInfo contactsInfo = new ContactsInfo();
			Cursor cursor_data = resolver.query(dataUri, new String[] {
					"data1", "mimetype" }, "raw_contact_id=?",
					new String[] { id }, null);
			while (cursor_data.moveToNext()) {
				String data1 = cursor_data.getString(0);
				String mimetype = cursor_data.getString(1);
				if (mimetype.equals("vnd.android.cursor.item/name")) {
					contactsInfo.setName(data1);
				} else if (mimetype.equals("vnd.android.cursor.item/phone_v2")) {
					contactsInfo.setNum(data1);
				}
			}
			cursor_data.close();
			contactsInfos.add(contactsInfo);
		}
		cursor_contacts.close();
		return contactsInfos;
	}
}
