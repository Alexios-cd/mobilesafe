package com.fwj.mobilesafe.service;

import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.fwj.mobilesafe.db.dao.BlanknumDao;

public class BlacknumService extends Service {
	private SmsReceiver receiver;
	private BlanknumDao blanknumDao;
	private MyPhoneStateListener listener;
	private TelephonyManager manager;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	private class SmsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("我是代码中注册的广播接受者,屏蔽短信");

			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objs) {
				// 创建短信
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
				// String body = smsMessage.getMessageBody(); // 获取到短信的内容
				String sender = smsMessage.getOriginatingAddress(); // 获取到短信的发送
				int mode = blanknumDao.queryMode(sender);
				if (mode == BlanknumDao.MODE_SMS
						|| mode == BlanknumDao.MODE_ALL) {
					abortBroadcast();

				}
			}
		}

	}

	@Override
	public void onCreate() {
		super.onCreate();
		blanknumDao = new BlanknumDao(this);
		// 在代码中注册短信到来的广播接受者
		receiver = new SmsReceiver();
		IntentFilter filter = new IntentFilter();
		filter.setPriority(2147483647);
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(receiver, filter);

		manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyPhoneStateListener();
		manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	// 3
	private class MyPhoneStateListener extends PhoneStateListener {
		@Override
		// state 变化后的电话状态
		// incomingNumber 来电号码
		public void onCallStateChanged(int state, final String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING: // 响铃状态
				// 查询数据库 判断是否在黑名单中 挂断电话
				int queryMode = blanknumDao.queryMode(incomingNumber);
				if (queryMode == BlanknumDao.MODE_TEL
						|| queryMode == BlanknumDao.MODE_ALL) {
					System.out.println("电话挂断");
					// manager.endCall();
					endCall();

					// 删除通话记录 内容解析者 访问系统的内容提供者 内容观察者
					final ContentResolver resolver = getContentResolver();
					final Uri url = Uri.parse("content://call_log/calls");
					// 注册内容观察者 观察数据库的变化 
					resolver.registerContentObserver(url, true,
							new ContentObserver(new Handler()) {

								@Override
								public void onChange(boolean selfChange) {
									super.onChange(selfChange);
									resolver.delete(url, "number=?",
											new String[] { incomingNumber });
									resolver.unregisterContentObserver(this);// 反注册内容观察者 
								}

							});

				}
				break;
			}

		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (receiver != null)
			unregisterReceiver(receiver);

		if (listener != null) {
			manager.listen(listener, PhoneStateListener.LISTEN_NONE);// 取消监听
			listener = null;
		}
	}

	/**
	 * 挂断电话
	 */
	public void endCall() {
		Object invoke = null;
		try {
			Class<?> loadClass = BlacknumService.class.getClassLoader()
					.loadClass("android.os.ServiceManager");
			Method declaredMethod = loadClass.getDeclaredMethod("getService",
					String.class);
			invoke = declaredMethod.invoke(null, Context.TELEPHONY_SERVICE);
			// 获取ITelephony 对象 aidl
			ITelephony asInterface = ITelephony.Stub
					.asInterface((IBinder) invoke);
			asInterface.endCall();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
