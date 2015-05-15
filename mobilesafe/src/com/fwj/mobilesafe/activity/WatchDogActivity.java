package com.fwj.mobilesafe.activity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fwj.mobilesafe.R;
import com.fwj.mobilesafe.base.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * ================================
 * 文件名：WatchDogActivity.java
 * 
 * 描    述：程序锁UI
 * 
 * 作    者：傅文江
 * 
 * 时    间：下午4:54:32
 * 
 * 版    权：©个人开发者 傅文江 版权所有
 * ================================
 */
public class WatchDogActivity extends BaseActivity {
	@ViewInject(R.id.tv_name)
	private TextView tv_name;
	@ViewInject(R.id.iv_icon)
	private ImageView iv_icon;
	@ViewInject(R.id.et_password)
	private EditText et_password;
	private String packageName;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_watchdog);
		ViewUtils.inject(this);

	}

	@Override
	protected void initData() {
		// 获取开启当前activity的意图对象
		Intent intent = getIntent();
		packageName = intent.getStringExtra("packageName");
		PackageManager pm = getPackageManager();
		try {
			ApplicationInfo applicationInfo = pm.getApplicationInfo(
					packageName, 0);
			Drawable loadIcon = applicationInfo.loadIcon(pm);
			String label = applicationInfo.loadLabel(pm).toString();
			iv_icon.setImageDrawable(loadIcon);
			tv_name.setText(label);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void unlock(View v) {
		String password = et_password.getText().toString().trim();
		if ("123".equals(password)) {
			Intent intent = new Intent("com.itheima.mobilesafe.unlock");
			// 解锁
			// 把当前程序 添加到临时不加锁的对象中 activity->服务 传递数据
			// 给服务发送广播
			intent.putExtra("packageName", packageName);
			sendBroadcast(intent);
			finish();

		}

	}

	@Override
	protected void onStop() {
		super.onStop();
		finish();
	}

	@Override
	public void onBackPressed() {

		// 打开桌面
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
		super.onBackPressed();
	}

}
