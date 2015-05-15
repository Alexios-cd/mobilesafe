package com.fwj.mobilesafe.uitils;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {
	private static AlertDialog dialog;

	/**
	 * 网络未连接时，调用设置方法
	 */
	public static void setNetwork(final Context context) {
		if (null != dialog)
			return;
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false);
		builder.setTitle("网络提示信息");
		builder.setMessage("网络不可用，如果继续，请先设置网络！");
		builder.setPositiveButton("设置", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = null;
				/**
				 * 判断手机系统的版本！如果API大于10 就是3.0+ 因为3.0以上的版本的设置和3.0以下的设置不一样，调用的方法不同
				 */
				if (android.os.Build.VERSION.SDK_INT > 10) {
					intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
				} else {
					intent = new Intent();
					ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
					intent.setComponent(component);
					intent.setAction("android.intent.action.VIEW");
				}
				context.startActivity(intent);
			}
		});

		builder.setNegativeButton("取消", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				NetUtil.dialog = null;
			}
		});
		dialog = builder.create();
		dialog.show();
	}

	/**
	 * 关闭提示对话框
	 */
	public static void closeSetNet() {
		if (null != dialog)
			dialog.dismiss();
		dialog = null;
	}

	/**
	 * 判断当前是否有可用的网络以及网络类型
	 * 
	 * @param context 上下文对象
	 * @return
	 */
	public static boolean checkNetworkAvailable(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		return (info != null && info.isAvailable());
	}
}