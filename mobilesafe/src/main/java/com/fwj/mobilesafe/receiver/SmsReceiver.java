package com.fwj.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.fwj.mobilesafe.R;
import com.fwj.mobilesafe.service.GpsService;

public class SmsReceiver extends BroadcastReceiver {
	private SharedPreferences sp;
	private DevicePolicyManager policyManager;

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("短信到来了");
		policyManager = (DevicePolicyManager) context
				.getSystemService(Context.DEVICE_POLICY_SERVICE);
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		// 超过70个字
		if (sp.getBoolean("protected", false)) {

			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objs) {
				// 创建短信
				SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
				String body = smsMessage.getMessageBody(); // 获取到短信的内容
				// String sender = smsMessage.getOriginatingAddress(); //
				// 获取到短信的发送

				if ("#*location*#".equals(body)) {
					// 定位
					System.out.println("location");
					Intent gpsService = new Intent(context, GpsService.class);
					context.startService(gpsService);

					String latitude = sp.getString("latitude", "");
					String longitude = sp.getString("longitude", "");
					if (!TextUtils.isEmpty(latitude)
							&& !TextUtils.isEmpty(longitude)) {
						SmsManager manager = SmsManager.getDefault();
						manager.sendTextMessage(sp.getString("safenum", ""),
								null, latitude + "\n" + longitude, null, null);
					}

					abortBroadcast();
				} else if ("#*alarm*#".equals(body)) {
					// 声音的管理者
					AudioManager audioManager = (AudioManager) context
							.getSystemService(Context.AUDIO_SERVICE);
					int streamMaxVolume = audioManager
							.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
					// 修改声音的大小 参数1 声音的类型 参数2 修改后的音量
					audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
							streamMaxVolume, 0);
					// 播放报警音乐
					System.out.println("alarm");
					MediaPlayer mediaPlayer = MediaPlayer.create(context,
							R.raw.ylzs);
					mediaPlayer.start();
					abortBroadcast();
				} else if ("#*lockscreen*#".equals(body)) {
					// 锁屏
					System.out.println("lockscreen");
					ComponentName who = new ComponentName(context, Admin.class);
					if (policyManager.isAdminActive(who)) {
						policyManager.lockNow();
						policyManager.resetPassword("1", 0);
						// policyManager.removeActiveAdmin(who);// 反激活超级管理员
					}
					abortBroadcast();
				} else if ("#*wipe*#".equals(body)) {
					// 远程擦除数据
					System.out.println("wipe");
					ComponentName who = new ComponentName(context, Admin.class);
					if (policyManager.isAdminActive(who))
						policyManager.wipeData(0);
					abortBroadcast();
				}

			}
		}
	}

}
