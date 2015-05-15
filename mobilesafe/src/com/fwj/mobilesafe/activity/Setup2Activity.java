package com.fwj.mobilesafe.activity;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.fwj.mobilesafe.R;
import com.fwj.mobilesafe.view.SettingView;

/**
 * ================================
 * 文件名：Setup2Activity.java
 * 
 * 描    述：手机防盗引导2
 * 
 * 作    者：傅文江
 * 
 * 时    间：下午4:56:23
 * 
 * 版    权：©个人开发者 傅文江 版权所有
 * ================================
 */
public class Setup2Activity extends SetupBaseActivity {
	private SettingView sv_bind;
	private TelephonyManager manager;  // 电话的管理者  可以获取电话的信息
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup2);
		manager=(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		sv_bind=(SettingView) findViewById(R.id.sv_bind);
		//manager.getLine1Number();// 获取电话号码   但是中国 可能获取不到电话号码 
		//  数据回显
		if(TextUtils.isEmpty(sp.getString("sim", ""))){
			sv_bind.setChecked(false);
		}else{
			sv_bind.setChecked(true);// nullPoint  null.()
		}
		
		
		sv_bind.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Editor edit = sp.edit();
				if(sv_bind.isChecked()){  // 解绑
					sv_bind.setChecked(false);
					edit.putString("sim", "");
				}else {  // 绑定
					sv_bind.setChecked(true);
					String simSerialNumber = manager.getSimSerialNumber();// 获取到sim卡的串号  串号相当于身份证号
					edit.putString("sim", simSerialNumber);
					
				}
				edit.commit();
			}
		});
		
	}

	@Override
	public void next_activity() {
		if(TextUtils.isEmpty(sp.getString("sim", ""))){
			Toast.makeText(getApplicationContext(), "请先绑定sim卡", 0).show();
			return;
		}
		
		Intent intent=new Intent(getApplicationContext(), Setup3Activity.class);
		startActivity(intent);
		finish();
		// 参数1  新的activity进入的动画  
		// 参数2  旧的activity移出的动画
		overridePendingTransition(R.anim.tran_next_enter, R.anim.tran_next_exit);
	}

	@Override
	public void pre_activity() {
		Intent intent=new Intent(getApplicationContext(), Setup1Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_enter, R.anim.tran_pre_exit);
	}
	
}
