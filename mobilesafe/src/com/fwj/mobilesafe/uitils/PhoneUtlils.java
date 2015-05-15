package com.fwj.mobilesafe.uitils;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.MediaStore;
import android.text.TextUtils;

/**
 * 手机工具类<br>
 * 主要功能<br>
 * 1.往手机通讯录插入联系人insertContact<br>
 * 2.打电话callPhone<br>
 * 3.发短信sendMessage<br>
 * 4.打开系统相机<br>
 * 5.删除联系人<br>
 * 
 * @author 傅文江
 * @2015-1-7 下午2:41:43 修订时间： 修订内容：
 */
public class PhoneUtlils {
	/**
	 * 往手机通讯录插入联系人
	 * 
	 * @param ct
	 * @param name
	 * @param tel
	 * @param email
	 */
	public static void addtContact(String name, String tel, String email) {
		ContentValues values = new ContentValues();
		// 首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId
		Uri rawContactUri = UIUtils.getContext().getContentResolver()
				.insert(RawContacts.CONTENT_URI, values);
		long rawContactId = ContentUris.parseId(rawContactUri);
		// 往data表入姓名数据
		if (!TextUtils.isEmpty(tel)) {
			values.clear();
			values.put(Data.RAW_CONTACT_ID, rawContactId);
			values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);// 内容类型
			values.put(StructuredName.GIVEN_NAME, name);
			UIUtils.getContext()
					.getContentResolver()
					.insert(android.provider.ContactsContract.Data.CONTENT_URI,
							values);
		}
		// 往data表入电话数据
		if (!TextUtils.isEmpty(tel)) {
			values.clear();
			values.put(Data.RAW_CONTACT_ID, rawContactId);
			values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);// 内容类型
			values.put(Phone.NUMBER, tel);
			values.put(Phone.TYPE, Phone.TYPE_MOBILE);
			UIUtils.getContext()
					.getContentResolver()
					.insert(android.provider.ContactsContract.Data.CONTENT_URI,
							values);
		}
		// 往data表入Email数据
		if (!TextUtils.isEmpty(email)) {
			values.clear();
			values.put(Data.RAW_CONTACT_ID, rawContactId);
			values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);// 内容类型
			values.put(Email.DATA, email);
			values.put(Email.TYPE, Email.TYPE_WORK);
			UIUtils.getContext()
					.getContentResolver()
					.insert(android.provider.ContactsContract.Data.CONTENT_URI,
							values);
		}
	}

	/**
	 * 删除联系人（此方法未测试，使用需谨慎）
	 * 
	 * @param name
	 *            需要 删除联系人的姓名
	 */
	public static void deletContact(String name) {
		Cursor cursor = UIUtils
				.getContext()
				.getContentResolver()
				.query(ContactsContract.Contacts.CONTENT_URI,
						new String[] { Data.RAW_CONTACT_ID },
						ContactsContract.Contacts.DISPLAY_NAME + "=?",
						new String[] { name }, null);
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		if (cursor.moveToFirst()) {
			do {
				long Id = cursor.getLong(cursor
						.getColumnIndex(Data.RAW_CONTACT_ID));
				ops.add(ContentProviderOperation
						.newDelete(
								ContentUris.withAppendedId(
										RawContacts.CONTENT_URI, Id)).build());
				try {
					UIUtils.getContext().getContentResolver()
							.applyBatch(ContactsContract.AUTHORITY, ops);
				} catch (Exception e) {
				}
			} while (cursor.moveToNext());
			cursor.close();
		}
	}

	/**
	 * 打电话
	 * 
	 * @param telnum
	 */
	public static void callPhone(Activity activity, String telnum) {
		Intent localIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
				+ telnum));
		activity.startActivity(localIntent);
	}

	/**
	 * 发短信
	 * 
	 * @param activity
	 *            开启发送短信的界面
	 * @param telnum
	 *            发送短信的号码
	 * @param text
	 *            发送短信的内容
	 */
	public static void sendMessage(Activity activity, String telnum, String text) {
		// 请求码为小于0的数时无法激活onactivityresult方法
		sendMessageForResult(activity, -1, telnum, text);
	}

	public static void sendMessageForResult(Activity activity, int requestCode,
			String telnum, String text) {
		Intent intent = new Intent();
		// 系统默认的action，用来打开默认的短信界面
		intent.setAction(Intent.ACTION_SENDTO);
		// 需要发短息的号码
		intent.setData(Uri.parse("smsto:" + telnum));
		intent.putExtra("sms_body", text);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 打开系统照相机，因为需要复写onActivityResult方法，所以需要当前的activity作为参数
	 * 
	 * @param activity
	 *            当前ACtivity
	 * @param 请求码
	 * @return protected void onActivityResult(int requestCode, int resultCode,
	 *         Intent data) {<br>
	 *         super.onActivityResult(requestCode, resultCode, data);<br>
	 *         if(100==requestCode && resultCode == Activity.RESULT_OK){<br>
	 *         Bitmap bitmap = (Bitmap)data.getExtras().get("data");<br>
	 *         main.setBackgroundDrawable(new BitmapDrawable(bitmap));<br>
	 *         }<br>
	 *         }
	 */
	public static void openCamera(Activity activity, int requestCode) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		activity.startActivityForResult(intent, requestCode);
	}

	/**
	 * 打开系统图库
	 * 
	 * @param activity
	 *            当前activity
	 * @param requestCode
	 *            请求码 protected void onActivityResult(int requestCode, int
	 *            resultCode, Intent data) {<br>
	 *            super.onActivityResult(requestCode, resultCode, data);<br>
	 *            if (110 == requestCode && resultCode == Activity.RESULT_OK) {<br>
	 *            Uri uri = data.getData();<br>
	 *            ContentResolver resolver =
	 *            UIUtils.getContext().getContentResolver();<br>
	 *            try {<br>
	 *            Bitmap bitmap =
	 *            BitmapFactory.decodeStream(resolver.openInputStream(uri));<br>
	 *            main.setBackgroundDrawable(new BitmapDrawable(bitmap));<br>
	 *            } catch (Exception e) {<br>
	 *            }<br>
	 *            }<br>
	 *            }<br>
	 */
	public static void openPictureStorage(Activity activity, int requestCode) {
		Intent intent = new Intent();
		// 定义intent的打开类型为图片类型
		intent.setType("image/*");
		// 设置action为ACTION_GET_CONTENT，代表获得图片内容
		intent.setAction(Intent.ACTION_GET_CONTENT);
		activity.startActivityForResult(intent, requestCode);

	}

}
