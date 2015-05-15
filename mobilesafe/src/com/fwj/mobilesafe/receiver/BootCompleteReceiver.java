package com.fwj.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class BootCompleteReceiver extends BroadcastReceiver {
	private SharedPreferences sp;
	private TelephonyManager manager;

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("开机启动完成");
		// 判断sim卡 有没有发生变化
		// 配置文件sim卡号
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		//当用户开启手机防盗保护的时候  
		if (sp.getBoolean("protected", false)) {
			String sp_sim = sp.getString("sim", ""); // "" null
			// 重新获取新的串号
			manager = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String sim = manager.getSimSerialNumber();
			// 判断
			if (!TextUtils.isEmpty(sp_sim) && sp_sim.equals(sim)) {
				// 没有替换
			} else {
				// sim卡发生变化
				SmsManager manager = SmsManager.getDefault();
				String safenum = sp.getString("safenum", "");
				if (!TextUtils.isEmpty(safenum)) {
					// 参数 1 发送地址
					manager.sendTextMessage(safenum, null, "sim change", null,
							null);

				}
			}
		}
	}

}
