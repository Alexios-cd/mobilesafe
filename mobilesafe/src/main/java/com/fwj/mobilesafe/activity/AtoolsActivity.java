package com.fwj.mobilesafe.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.fwj.mobilesafe.R;
import com.fwj.mobilesafe.base.BaseActivity;
import com.fwj.mobilesafe.base.SmsEngine;
import com.fwj.mobilesafe.base.SmsEngine.ProcessListener;
import com.fwj.mobilesafe.uitils.UIUtils;

/**
 * ================================
 * 文件名：AtoolsActivity.java
 * 
 * 描    述：高级工具UI
 * 
 * 作    者：傅文江
 * 
 * 时    间：下午5:01:25
 * 
 * 版    权：©个人开发者 傅文江 版权所有
 * ================================
 */
public class AtoolsActivity extends BaseActivity {
	private ProgressDialog dialog;

	// private ProgressBar pb_process;
	@Override
	protected void initView() {
		setContentView(R.layout.activity_atools);

	}

	@Override
	protected void initData() {

	}

	// A
	public void address(View v) {
		// 界面的跳转
		Intent intent = new Intent(this, AddressActivity.class);
		startActivity(intent);
	}

	public void backupSms(View v) {
		dialog = new ProgressDialog(this);
		dialog.setTitle("正在备份短信……");
		dialog.setIcon(R.drawable.ic_launcher);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 设置成水平样式的dialog
		dialog.show();
		new Thread() {
			public void run() {
				SmsEngine.backupSms(getApplicationContext(),
						new ProcessListener() {

							@Override
							public void process(int process) {
								dialog.setProgress(process);
							}

							@Override
							public void max(int max) {
								dialog.setMax(max);
							}
						});
				dialog.dismiss();
				UIUtils.showToastSafe("备份结束");
			};
		}.start();
	}

	public void restoreSms(View v) {
		// 读取系统的短信 判断如果和备份的短息一样的 不还原
		// 解析xml 写入到系统短信数据库
		ContentResolver contentResolver = getContentResolver();
		Uri uri = Uri.parse("content://sms");
		ContentValues values = new ContentValues();
		values.put("address", "95588");
		values.put("date", System.currentTimeMillis());
		values.put("type", 1);
		values.put("body", "$100000 from xxx");

		contentResolver.insert(uri, values);
	}

}
