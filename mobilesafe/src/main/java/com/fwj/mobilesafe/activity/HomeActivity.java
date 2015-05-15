package com.fwj.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fwj.mobilesafe.R;
import com.fwj.mobilesafe.base.BaseActivity;
import com.fwj.mobilesafe.uitils.MD5Utils;
import com.fwj.mobilesafe.uitils.ToastUtils;
import com.fwj.mobilesafe.uitils.adpter.CommonAdater;
import com.fwj.mobilesafe.uitils.adpter.ViewHolder;

/**
 * ================================ 文件名：HomeActivity.java
 * 
 * 描 述：主UI九宫格
 * 
 * 作 者：傅文江
 * 
 * 时 间：下午4:32:40
 * 
 * 版 权：©个人开发者 傅文江 版权所有 ================================
 */
public class HomeActivity extends BaseActivity {
	private GridView gv_home;
	private SharedPreferences sp;
	private AlertDialog dialog;
	private List<String> names;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_home);
	}

	@Override
	protected void initData() {
		sp = getSharedPreferences("config", MODE_PRIVATE);

		gv_home = (GridView) findViewById(R.id.gv_home);
		names = new ArrayList<String>();
		names.add("手机防盗");
		names.add("通讯卫士");
		names.add("软件管理");
		names.add("进程管理");
		names.add("流量统计");
		names.add("手机杀毒");
		names.add("缓存清理");
		names.add("高级工具");
		names.add("设置中心");
		gv_home.setAdapter(new HomeAdapter(HomeActivity.this,
				R.layout.item_gridview, names));

		gv_home.setOnItemClickListener(new OnItemClickListener() {
			// parent GridView
			// view 每个条目的布局
			// position 点击的条目的位置
			// id
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:// 点击手机防盗
					if (TextUtils.isEmpty(sp.getString("password", ""))) {
						// 弹出设置密码的对话框
						showSetupDialog();
					} else {
						// 弹出输入密码的对话框
						showEnterDialog();
					}
					break;
				case 1:// 进入通讯卫士
					Intent intent1 = new Intent(HomeActivity.this,
							CallSmsSafeActivity.class);
					startActivity(intent1);
					break;
				case 2: // 进入软件管理界面
					Intent intent2 = new Intent(HomeActivity.this,
							SoftManagerActivity.class);
					startActivity(intent2);
					break;
				case 3:// 进程管理
					Intent intent3 = new Intent(HomeActivity.this,
							TaskManagerActivity.class);
					startActivity(intent3);
					break;
				case 4:// 流量统计
					Intent intent4 = new Intent(HomeActivity.this,
							TrafficActivity.class);
					startActivity(intent4);
					break;
				case 5: // 病毒查杀
					Intent intent5 = new Intent(HomeActivity.this,
							AntiVirusActivity.class);
					startActivity(intent5);
					break;
				case 6:// 跳转到缓存清理的界面
					Intent intent6 = new Intent(HomeActivity.this,
							ClearCacheActvity.class);
					startActivity(intent6);
					break;

				case 7: // 高级工具
					Intent intent7 = new Intent(HomeActivity.this,
							AtoolsActivity.class);
					startActivity(intent7);

					break;
				case 8:
					Intent intent8 = new Intent(getApplicationContext(),
							SettingActivity.class);
					startActivity(intent8);
					break;

				default:
					break;
				}
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	int count = 0;

	/**
	 * 输入密码的dialog
	 */
	protected void showEnterDialog() {
		AlertDialog.Builder builder = new Builder(this);
		View view = View.inflate(getApplicationContext(),
				R.layout.dialog_enter, null);
		final EditText et_password = (EditText) view
				.findViewById(R.id.et_password);
		Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		ImageView iv_show = (ImageView) view.findViewById(R.id.iv_show);

		iv_show.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (count % 2 == 0) {
					et_password.setInputType(1);

				} else {
					et_password.setInputType(129);
				}
				count++;
			}
		});
		// 取消按钮的点击事件
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 隐藏dialog
				dialog.dismiss();
			}
		});

		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String password = et_password.getText().toString().trim();
				if (TextUtils.isEmpty(password)) {
					ToastUtils.TextToast("输入密码不能为空");
					return;
				}
				// 获取到配置文件中的密码
				String sp_password = sp.getString("password", "");
				if (MD5Utils.digest(password).equals(sp_password)) {
					ToastUtils.TextToast("密码正确");
					dialog.dismiss();
					Intent intent = new Intent(getApplicationContext(),
							LostFindActivity.class);
					startActivity(intent);

				} else {
					ToastUtils.TextToast("密码错误");
				}
			}
		});

		// builder.setView(view);// 设置dialog显示的view对象

		dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	/**
	 * 设置密码的对话框
	 */
	protected void showSetupDialog() {
		AlertDialog.Builder builder = new Builder(this, R.style.dialog);
		View view = View.inflate(getApplicationContext(),
				R.layout.dialog_setup, null);
		// 加载控件
		final EditText et_password = (EditText) view
				.findViewById(R.id.et_password);
		final EditText et_password_confirm = (EditText) view
				.findViewById(R.id.et_password_confirm);
		Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
		// 取消按钮的点击事件
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 隐藏dialog
				dialog.dismiss();
			}
		});
		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String password = et_password.getText().toString().trim();
				String password_confirm = et_password_confirm.getText()
						.toString().trim();
				if (!TextUtils.isEmpty(password)) {
					if (password.equals(password_confirm)) {
						// 密码设置成功
						Editor edit = sp.edit();
						edit.putString("password", MD5Utils.digest(password));
						edit.commit();
						dialog.dismiss();
						// edit.apply()
					} else {
						ToastUtils.TextToast("两次密码不一致");
					}
				} else {
					ToastUtils.TextToast("密码不能为空");
				}

			}
		});

		builder.setView(view);// 设置dialog显示的view对象

		dialog = builder.create();
		dialog.show();
	}

	private class HomeAdapter extends CommonAdater<String> {
		int[] imageId = { R.drawable.safe, R.drawable.selector_callsmssafe,
				R.drawable.app, R.drawable.taskmanager, R.drawable.netmanager,
				R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools,
				R.drawable.settings };

		public HomeAdapter(Context context, int layoutItemt, List<String> datas) {
			super(context, layoutItemt, datas);
		}

		@Override
		public void convert(ViewHolder holder, String bean, int position) {
			holder.setImageView(R.id.iv_icon, imageId[position]);
			holder.setText(R.id.tv_name, bean);
		}

	}


}
