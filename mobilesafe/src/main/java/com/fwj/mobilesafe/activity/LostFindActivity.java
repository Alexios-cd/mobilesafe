package com.fwj.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fwj.mobilesafe.R;
import com.fwj.mobilesafe.base.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * ================================
 * 文件名：LostFindActivity.java
 * 
 * 描    述：手机防盗UI
 * 
 * 作    者：傅文江
 * 
 * 时    间：下午4:36:02
 * 
 * 版    权：©个人开发者 傅文江 版权所有
 * ================================
 */
public class LostFindActivity extends BaseActivity {
	private SharedPreferences sp;
	@ViewInject(R.id.tv_safenum)
	private TextView tv_safenum;
	@ViewInject(R.id.iv_protected)
	private ImageView iv_protected;

	@Override
	protected void initView() {
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// TextView tv=new TextView(getApplicationContext());
		// tv.setText("这是手机防盗界面");
		// 如果用户第一次进入手机防盗界面 让界面跳转到设置向导
		boolean b = sp.getBoolean("first", true);
		if (b) {
			//
			Intent intent = new Intent(getApplicationContext(),
					Setup1Activity.class);
			startActivity(intent);
			finish();// 结束当前的activity
		} else {

		}

		setContentView(R.layout.activity_lostfind);
		ViewUtils.inject(this);

	}

	@Override
	protected void initData() {
		tv_safenum.setText(sp.getString("safenum", ""));
		boolean b2 = sp.getBoolean("protected", false);
		if (b2) {
			iv_protected.setImageResource(R.drawable.lock);
		} else {
			iv_protected.setImageResource(R.drawable.unlock);
		}

	}

	public void resetup(View v) {
		//
		Intent intent = new Intent(getApplicationContext(),
				Setup1Activity.class);
		startActivity(intent);
		finish();// 结束当前的activity
	}

}
