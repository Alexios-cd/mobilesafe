package com.fwj.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.fwj.mobilesafe.R;
import com.fwj.mobilesafe.base.BaseActivity;
import com.fwj.mobilesafe.db.dao.AntiVirusDao;
import com.fwj.mobilesafe.uitils.MD5Utils;

/**
 * ================================
 * 文件名：AntiVirusActivity.java
 * 
 * 描    述：病毒扫面UI
 * 
 * 作    者：傅文江
 * 
 * 时    间：下午5:01:13
 * 
 * 版    权：©个人开发者 傅文江 版权所有
 * ================================
 */
public class AntiVirusActivity extends BaseActivity {
	protected static final int MSG_SCANNER = 10;
	private ImageView iv_scanner;
	private LinearLayout ll_container;
	private ProgressBar pb_process;
	private TextView tv_scanner;
	private AntiVirusDao dao;
	private List<String> virusInfos;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == MSG_SCANNER) {
				String lable = (String) msg.obj;
				TextView tv = new TextView(getApplicationContext());
				tv.setTextColor(Color.BLACK);
				tv.setText(lable);
				if (msg.arg1 == 1) {
					tv.setTextColor(Color.RED);
				}
				ll_container.addView(tv, 0); // 添加view对象 指定位置
				tv_scanner.setText("正在扫描" + lable);
			}
		};
	};

	@Override
	protected void initView() {
		setContentView(R.layout.activity_anti_virus);
		iv_scanner = (ImageView) findViewById(R.id.iv_scanner);
		ll_container = (LinearLayout) findViewById(R.id.ll_container);
		pb_process = (ProgressBar) findViewById(R.id.pb_process);
		tv_scanner = (TextView) findViewById(R.id.tv_scanner);
	}

	@Override
	protected void initData() {
		dao = new AntiVirusDao(this);
		virusInfos = new ArrayList<String>();

		RotateAnimation animation = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animation.setDuration(5000);
		animation.setRepeatCount(Animation.INFINITE);// 一直循环
		iv_scanner.startAnimation(animation);
		scanner();

	}

	private void scanner() {
		tv_scanner.setText("正在初始化8核扫描引擎....");
		virusInfos.clear();
		new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				PackageManager manager = getPackageManager();
				List<PackageInfo> installedPackages = manager
						.getInstalledPackages(PackageManager.GET_SIGNATURES);
				pb_process.setMax(installedPackages.size());
				int count = 0;
				for (PackageInfo info : installedPackages) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					String packageName = info.packageName;
					String label = info.applicationInfo.loadLabel(manager)
							.toString();
					Message message = Message.obtain();
					message.what = MSG_SCANNER;
					message.obj = label;

					Signature[] signatures = info.signatures;
					String string = signatures[0].toCharsString();
					String digest = MD5Utils.digest(string);
					if (dao.isVirus(digest)) {
						message.arg1 = 1;// 1 代表程序是病毒
						virusInfos.add(packageName);
					} else {
						message.arg1 = 0;
					}

					handler.sendMessage(message);
					// System.out.println(label+"..."+digest);

					count++;
					pb_process.setProgress(count);
				}
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (virusInfos.size() < 1) {
							tv_scanner.setText("扫描完成,您的手机非常安全!");

						} else {
							tv_scanner.setText("扫描完成,发现了" + virusInfos.size()
									+ "病毒");
							AlertDialog.Builder builder = new Builder(
									AntiVirusActivity.this);
							builder.setTitle("警告:");
							builder.setIcon(R.drawable.ic_launcher);
							builder.setMessage("是否删除病毒");
							builder.setNegativeButton("取消", null);
							builder.setPositiveButton("确定",
									new OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											for (String packageName : virusInfos) {
												Intent intent = new Intent();
												intent.setAction("android.intent.action.DELETE");
												intent.addCategory("android.intent.category.DEFAULT");
												intent.setData(Uri
														.parse("package:"
																+ packageName));
												startActivity(intent);
											}
											dialog.dismiss();
										}
									});
							builder.show();
						}
						iv_scanner.clearAnimation();// 移除所有动画效果
					}
				});
			}
		}.start();
	}

}
