package com.fwj.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;

import com.fwj.mobilesafe.R;

/**
 * ================================
 * 文件名：Setup1Activity.java
 * 
 * 描    述：手机防盗引导1
 * 
 * 作    者：傅文江
 * 
 * 时    间：下午4:56:05
 * 
 * 版    权：©个人开发者 傅文江 版权所有
 * ================================
 */
public class Setup1Activity extends SetupBaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup1);
		
	}

	@Override
	public void next_activity() {
		Intent intent=new Intent(getApplicationContext(), Setup2Activity.class);
		startActivity(intent);
		finish();
		// 参数1  新的activity进入的动画  
		// 参数2  旧的activity移出的动画
		overridePendingTransition(R.anim.tran_next_enter, R.anim.tran_next_exit);

	}

	@Override
	public void pre_activity() {
		finish();
	}
	
}
