package com.fwj.mobilesafe.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;

import com.fwj.mobilesafe.R;
import com.fwj.mobilesafe.base.BaseActivity;
import com.fwj.mobilesafe.service.AddressService;
import com.fwj.mobilesafe.service.BlacknumService;
import com.fwj.mobilesafe.service.WatchDogService;
import com.fwj.mobilesafe.uitils.SPUtils;
import com.fwj.mobilesafe.uitils.ServiceUtils;
import com.fwj.mobilesafe.view.SettingClickView;
import com.fwj.mobilesafe.view.SettingView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * ================================
 * 文件名：SettingActivity.java
 * 
 * 描    述：设置UI
 * 
 * 作    者：傅文江
 * 
 * 时    间：下午4:53:00
 * 
 * 版    权：©个人开发者 傅文江 版权所有
 * ================================
 */
public class SettingActivity extends BaseActivity {
	@ViewInject(R.id.sv_update)
	private SettingView sv_update;
	@ViewInject(R.id.sv_address)
	private SettingView sv_address;

	@ViewInject(R.id.scv_location_bg)
	private SettingClickView scv_location_bg;

	@ViewInject(R.id.scv_location_postion)
	private SettingClickView scv_location_postion;
	@ViewInject(R.id.sv_blacknum)
	private SettingView sv_blacknum;
	@ViewInject(R.id.sv_watchdog)
	private SettingView sv_watchdog;
	private SharedPreferences sp;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_setting);

	}

	@Override
	protected void initData() {
		sp = getSharedPreferences("config", MODE_PRIVATE);
		ViewUtils.inject(this);
		update();
		changeLocationBb();
		changeLocationPostion();
	}

	private void blacknum() {
		// 动态判断服务是否开启
		if (ServiceUtils.isRunningService(
				"com.fwj.mobilesafe.service.BlacknumService", this)) {
			sv_blacknum.setChecked(true);
		} else {
			sv_blacknum.setChecked(false);
		}
		sv_blacknum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						BlacknumService.class);
				if (sv_blacknum.isChecked()) {
					sv_blacknum.setChecked(false);
					stopService(intent);
				} else {
					sv_blacknum.setChecked(true);

					startService(intent);
				}
			}
		});
	}

	private void changeLocationPostion() {
		scv_location_postion.setTitle("归属地提示框位置");
		scv_location_postion.setDes("设置归属地提示框显示位置");
		scv_location_postion.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 来到新的界面
				Intent intent = new Intent(getApplicationContext(),
						DragViewActivity.class);
				startActivity(intent);
			}
		});
	}

	private void changeLocationBb() {
		final String[] items = { "半透明", "活力橙", "卫士蓝", "金属灰", "苹果绿" };
		scv_location_bg.setTitle("归属地提示框风格");
		scv_location_bg.setDes(items[sp.getInt("which", 0)]);

		scv_location_bg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new Builder(SettingActivity.this);
				builder.setIcon(R.drawable.ic_launcher);
				builder.setTitle("归属地提示框风格");
				// 第一个参数显示的条目的数组 第二个参数 默认显示的位置
				builder.setSingleChoiceItems(items, sp.getInt("which", 0),
						new DialogInterface.OnClickListener() {
							// which 代表点击的条目的位置
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Editor edit = sp.edit();
								edit.putInt("which", which);
								edit.commit();
								scv_location_bg.setDes(items[which]);

								dialog.dismiss();

							}
						});
				builder.setNegativeButton("取消", null);
				builder.show();
			}
		});
	}

	// 7个 onCreat onstart onResume onPause onstop onDestory onRestart
	@Override
	protected void onStart() {
		super.onStart();
		address();

		blacknum();

		watchdog();
	}

	private void watchdog() {
		// 动态判断服务是否开启
		if (ServiceUtils.isRunningService(
				"com.fwj.mobilesafe.service.WatchDogService", this)) {
			sv_watchdog.setChecked(true);


		} else {
			sv_watchdog.setChecked(false);

		}

		sv_watchdog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						WatchDogService.class);
				if (sv_watchdog.isChecked()) {
					sv_watchdog.setChecked(false);
					stopService(intent);
					SPUtils.putBoolean(SPUtils.IsRunWatchDogService, false);
				} else {
					sv_watchdog.setChecked(true);
					startService(intent);
					SPUtils.putBoolean(SPUtils.IsRunWatchDogService, true);
				}
			}
		});
	}

	private void address() {
		// 动态判断服务是否开启
		if (ServiceUtils.isRunningService(
				"com.fwj.mobilesafe.service.AddressService", this)) {
			sv_address.setChecked(true);
			SPUtils.putBoolean(SPUtils.IsRunAddressService, true);
		} else {
			sv_address.setChecked(false);
			SPUtils.putBoolean(SPUtils.IsRunAddressService, false);
		}

		sv_address.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						AddressService.class);
				if (sv_address.isChecked()) {
					sv_address.setChecked(false);
					stopService(intent);
				} else {
					sv_address.setChecked(true);

					startService(intent);
				}
			}
		});
	}

	public void update() {
		// 数据回显 如果找不到update 这一个key 默认返回true
		boolean b = sp.getBoolean("update", true);
		if (b) {
			// sv_update.setDes("自动更新开启");
			sv_update.setChecked(true);
		} else {
			// sv_update.setDes("自动更新关闭");
			sv_update.setChecked(false);
		}

		sv_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Editor edit = sp.edit();
				if (sv_update.isChecked()) {
					sv_update.setChecked(false);
					// sv_update.setDes("自动更新关闭");
					edit.putBoolean("update", false);
				} else {
					sv_update.setChecked(true);
					// sv_update.setDes("自动更新开启");
					edit.putBoolean("update", true);
				}
				edit.commit();
			}
		});
	}

}
