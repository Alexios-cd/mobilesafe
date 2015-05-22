package com.fwj.mobilesafe.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.fwj.mobilesafe.R;
import com.fwj.mobilesafe.base.BaseActivity;
import com.fwj.mobilesafe.service.AddressService;
import com.fwj.mobilesafe.service.WatchDogService;
import com.fwj.mobilesafe.uitils.SPUtils;
import com.fwj.mobilesafe.uitils.StreamUtils;
import com.fwj.mobilesafe.uitils.ToastUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * ================================
 * 文件名：SplashActivity.java
 * 
 * 描    述：Splash UI
 * 
 * 作    者：傅文江
 * 
 * 时    间：下午4:54:05
 * 
 * 版    权：©个人开发者 傅文江 版权所有
 * ================================
 */
public class SplashActivity extends BaseActivity {
	private SharedPreferences sp;

	protected static final int MSG_SHOW_UPDATE = 10;

	protected static final int MSG_ENTER_HOME = 20;

	protected static final int MSG_URL_ERROR = 30;

	protected static final int MSG_IO_ERROR = 40;

	protected static final int MSG_JSON_ERROR = 50;

	protected static final int MSG_NET_ERROR = 60;
	@ViewInject(R.id.tv_splash_version)
	private TextView tv_splash_version;
	@ViewInject(R.id.tv_splash_process)
	private TextView tv_splash_process;

	private String apkCode;
	private String des;
	private String apkUrl;
	// 3
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MSG_SHOW_UPDATE:
				// 4
				showUpdateDialog();
				break;
			case MSG_ENTER_HOME:
				enterHome();
				break;
			case MSG_URL_ERROR:
				ToastUtils.TextToast("错误号:" + MSG_URL_ERROR);
				enterHome();
				break;
			case MSG_IO_ERROR:
				ToastUtils.TextToast("错误号:" + MSG_IO_ERROR);
				enterHome();
				break;
			case MSG_JSON_ERROR:
				ToastUtils.TextToast("错误号:" + MSG_JSON_ERROR);
				enterHome();
				break;
			case MSG_NET_ERROR:
				ToastUtils.TextToast("错误号:" + "您的手机网络不给力");
				enterHome();
				break;
			default:
				break;
			}

		};
	};

	@Override
	protected void initView() {
		setContentView(R.layout.activity_splash);
		ViewUtils.inject(this);// 初始化所有带注解的控件
	}

	@Override
	protected void initData() {
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 动态显示版本号
		tv_splash_version.setText("版本号:" + getVersionName());

		// 检查更新
		if (sp.getBoolean("update", true)) {
			checkUpdate();

		} else {
			new Thread() {
				public void run() {
					try {

						Thread.sleep(2000); // 主线程需要渲染界面
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							enterHome(); // 进入下一个界面
						}
					});

				};
			}.start();

		}
		copyDb("address.db");
		copyDb("antivirus.db");
		if(SPUtils.getBoolean(SPUtils.IsRunAddressService)) {
			startService(new Intent(this, AddressService.class));
		}
		if(SPUtils.getBoolean(SPUtils.IsRunWatchDogService)) {
			startService(new Intent(this, WatchDogService.class));
		}
	}

	private void copyDb(String dbName) {

		AssetManager assetManager = getAssets();
		File file = new File(getFilesDir(), dbName); // /data/data/com.mobilesafe/files
		if (file != null && file.exists()) {

			return;
		}
		InputStream is = null;
		// copy 数据库
		FileOutputStream fos = null;
		try {
			// 输入流
			is = assetManager.open(dbName);
			// 输出流
			fos = new FileOutputStream(file);

			byte[] bs = new byte[1024 * 4];
			int len = -1;
			while ((len = is.read(bs)) != -1) {
				fos.write(bs, 0, len);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 进入下一个界面
	 */
	protected void enterHome() {
		// 开启下一个activity
		Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
		startActivity(intent);
		finish();
	}

	// 2
	private void checkUpdate() {
		// 访问网络 比较耗时的 android4.0 以后 联网操作不能在主线程
		new Thread() {

			@Override
			public void run() {
				super.run();
				URL url;
				Message message = Message.obtain();// new Message();
				long startTime = System.currentTimeMillis();
				try {
					url = new URL(getResources().getString(R.string.server_url));
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(2000); // 连接 超时时间
					conn.setReadTimeout(2000);// 读取超时时间
					int code = conn.getResponseCode();
					if (code == 200) {
						// 解析服务器数据 获取服务器的版本号
						// 最新的版本
						// 最新版本的下载的地址
						// 新版本的描述信息 json xml

						InputStream inputStream = conn.getInputStream();// 返回服务器输入流
						String json = StreamUtils
								.parserInputStream(inputStream);
						// System.out.println(str);
						// 解析json
						JSONObject jsonObject = new JSONObject(json);
						apkCode = jsonObject.getString("code");
						des = jsonObject.getString("des");
						apkUrl = jsonObject.getString("apkurl");

						System.out.println(apkCode + "...." + des + "..."
								+ apkUrl);

						if (getVersionName().equals(apkCode)) {
							// 一致 进入下一个界面
							message.what = MSG_ENTER_HOME;
						} else {
							// 不一致 弹出升级对话框
							// showUpdateDialog();
							message.what = MSG_SHOW_UPDATE;
						}
					} else {
						message.what = MSG_NET_ERROR;
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
					message.what = MSG_URL_ERROR;
				} catch (ProtocolException e) {
					e.printStackTrace();
					message.what = MSG_URL_ERROR;
				} catch (IOException e) {
					e.printStackTrace();
					message.what = MSG_IO_ERROR;
				} catch (JSONException e) {
					e.printStackTrace();
					message.what = MSG_JSON_ERROR;
				} finally {
					long endTime = System.currentTimeMillis();
					long dTime = endTime - startTime; // 程序联网执行的时间
					if (dTime < 2000) {
						try {
							Thread.sleep(2000 - dTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					handler.sendMessage(message);
				}

			}
		}.start();
	}

	/**
	 * 4 弹出升级对话框
	 */
	protected void showUpdateDialog() {
		AlertDialog.Builder builder = new Builder(this); // 上下文必须是activity
		builder.setIcon(R.drawable.ic_launcher);// 设置对话框的图标
		builder.setTitle("发现新版本:" + apkCode);
		builder.setMessage(des);
		builder.setCancelable(false);// 不可以直接取消
		builder.setPositiveButton("升级", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				download();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();// 隐藏对话框
				enterHome();
			}
		});// 如果点击事件设置为空 默认点击取消dialog

		// builder.create().show();
		builder.show(); //
	}

	/**
	 * 下载 5
	 */
	protected void download() {
		HttpUtils httpUtils = new HttpUtils();
		// 1 参数 下载路径 参数2 下载到哪一个位置 参数3 下载的回调方法
		// Environment.getExternalStorageState()==Environment.MEDIA_MOUNTED
		httpUtils.download(apkUrl, Environment.getDataDirectory()
				+ "mobilesafe2.0.apk", new RequestCallBack<File>() {
			// 下载成功
			@Override
			public void onSuccess(ResponseInfo<File> arg0) {
				// 安装
				// 打开系统程序安装
				installApk();

			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				super.onLoading(total, current, isUploading);
				tv_splash_process.setVisibility(View.VISIBLE);
				tv_splash_process.setText(current + "/" + total);
			}

			// 下载失败
			@Override
			public void onFailure(HttpException arg0, String arg1) {
				ToastUtils.TextToast("下载失败");
			}
		});
	}

	/**
	 * 6 安装 打开系统的packageInstallerActivity
	 */
	protected void installApk() {
		// <intent-filter>
		// <action android:name="android.intent.action.VIEW" />
		// <category android:name="android.intent.category.DEFAULT" />
		// <data android:scheme="content" />
		// <data android:scheme="file" />
		// <data android:mimeType="application/vnd.android.package-archive" />
		// </intent-filter>
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setDataAndType(
				Uri.fromFile(new File(Environment.getDataDirectory()
						+ "mobilesafe2.0.apk")),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}

	/**
	 * 获取到当前版本号
	 * 
	 * @return
	 */
	public String getVersionName() {
		// PackageManager 包管理器 读取所有程序清单文件信息
		PackageManager pm = getPackageManager();
		// 根据应用程序的包名 获取到应用程序信息
		try {
			// getPackageName() 获取当前程序的包名
			PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			// can't reach // my wife is a idot!!!!
			return null;
		}

	}

}
