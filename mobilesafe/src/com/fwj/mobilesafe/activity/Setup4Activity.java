package com.fwj.mobilesafe.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.fwj.mobilesafe.R;
import com.fwj.mobilesafe.receiver.Admin;

/**
 * ================================
 * 文件名：Setup4Activity.java
 * 
 * 描    述：手机防盗引导4
 * 
 * 作    者：傅文江
 * 
 * 时    间：下午4:56:37
 * 
 * 版    权：©个人开发者 傅文江 版权所有
 * ================================
 */
public class Setup4Activity extends SetupBaseActivity {
	private CheckBox cb_protected;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup4);
		cb_protected=(CheckBox) findViewById(R.id.cb_protected);
		boolean b=sp.getBoolean("protected", false);
		if(b){
			cb_protected.setText("您开启了手机防盗保护");
			cb_protected.setChecked(true);
		}else{
			cb_protected.setText("您没有开启了手机防盗保护");
			cb_protected.setChecked(false);
		}
		// 当checkBox 选择状态改变的时候调用
		cb_protected.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			//第一个参数代表checkBox  第二个参数 代表当前是否被选择
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Editor edit = sp.edit();
				if(isChecked){
					cb_protected.setText("您开启了手机防盗保护");
					edit.putBoolean("protected", true);
				}else{
					cb_protected.setText("您没有开启了手机防盗保护");
					edit.putBoolean("protected", false);
				}
				edit.commit();
			}
		});
	}

	@Override
	public void next_activity() {
		Editor edit = sp.edit();
		edit.putBoolean("first", false);
		edit.commit();
		Intent intent=new Intent(this, LostFindActivity.class);
		startActivity(intent);
		finish();
		// 参数1  新的activity进入的动画  
		// 参数2  旧的activity移出的动画
		overridePendingTransition(R.anim.tran_next_enter, R.anim.tran_next_exit);
	}
	public void active(View v){
		//  定义intent  指定action
		   Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		   ComponentName mDeviceAdminSample=new ComponentName(this, Admin.class);
           intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
           // 设置额外的描述信息
           intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                   "激活后可以远程锁屏清除数据");
           startActivity(intent);
	}
	@Override
	public void pre_activity() {
		Intent intent=new Intent(getApplicationContext(), Setup3Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_enter, R.anim.tran_pre_exit);
	}
	
}
