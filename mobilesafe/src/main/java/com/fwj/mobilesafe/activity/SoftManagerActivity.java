package com.fwj.mobilesafe.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fwj.mobilesafe.R;
import com.fwj.mobilesafe.base.BaseActivity;
import com.fwj.mobilesafe.db.dao.WatchDogDao;
import com.fwj.mobilesafe.domain.AppInfo;
import com.fwj.mobilesafe.engine.AppProvider;
import com.fwj.mobilesafe.uitils.DensityUtil;
import com.fwj.mobilesafe.uitils.DialogUtils;
import com.fwj.mobilesafe.uitils.MyAsykTask;
import com.fwj.mobilesafe.uitils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ================================
 * 文件名：SoftManagerActivity.java
 * 
 * 描    述：软件管理UI
 * 
 * 作    者：傅文江
 * 
 * 时    间：下午4:37:13
 * 
 * 版    权：©个人开发者 傅文江 版权所有
 * ================================
 */
public class SoftManagerActivity extends BaseActivity implements
		OnClickListener {
	private ListView lv_soft_manager;
	private AppAdapter adapter;
	private List<AppInfo> appInfos;
	private List<AppInfo> userInfos; // 用来存放用户程序
	private List<AppInfo> systemInfos; // 用来存放系统程序
	private TextView tv_num;
	private PopupWindow popupWindow;
	private AppInfo appInfo;
	private TextView tv_sd;
	private TextView tv_rom;

	// 1
	@Override
	protected void initView() {
		setContentView(R.layout.activity_soft_manager);
		lv_soft_manager = (ListView) findViewById(R.id.lv_soft_manager);
		tv_sd = (TextView) findViewById(R.id.tv_sd);
		tv_rom = (TextView) findViewById(R.id.tv_rom);
	}

	@Override
	protected void initData() {

		tv_sd.setText("SD卡可用内存:\n" + AppProvider.getSDAvailableSpace(this));
		tv_rom.setText("Rom可用内存:\n" + AppProvider.getRomAvailableSpace(this));

		tv_num = (TextView) findViewById(R.id.tv_num);
		// 2
		fillData();
		// 5 条目点击事件
		setItemClick();

		// 4 滑动事件
		setOnScroll();

	}

	// 4
	public void setOnScroll() {
		lv_soft_manager.setOnScrollListener(new OnScrollListener() {
			// listVIew 滑动状态变化的时候调用
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			// 当lv 滑动的时候调用 但是listView 界面加载的时候 默认调用onScoll
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// 5 隐藏popupWindow
				if (popupWindow != null) {
					popupWindow.dismiss();
					popupWindow = null;
				}

				if (userInfos != null && systemInfos != null) {
					if (firstVisibleItem >= userInfos.size() + 1) {
						tv_num.setText("系统程序" + systemInfos.size() + "个");
					} else {
						tv_num.setText("用户程序" + userInfos.size() + "个");
					}
				}
			}
		});
	}

	/**
	 * 5 设置条目的点击事件 popupWIndow
	 */
	public void setItemClick() {
		lv_soft_manager.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					return;
				} else if (position == userInfos.size() + 1) {
					return;
				}
				if (position <= userInfos.size()) {
					appInfo = userInfos.get(position - 1);
				} else {
					appInfo = systemInfos.get(position - userInfos.size() - 2);
				}
				int[] location = new int[2];
				view.getLocationInWindow(location); // 测量view的坐标 x 和 y 存放到数组中
				int x = location[0];
				int y = location[1];

				View popupView = View.inflate(getApplicationContext(),
						R.layout.popup_soft_manager, null);
				LinearLayout ll_unintall = (LinearLayout) popupView
						.findViewById(R.id.ll_uninstall);
				LinearLayout ll_start = (LinearLayout) popupView
						.findViewById(R.id.ll_start);
				LinearLayout ll_shared = (LinearLayout) popupView
						.findViewById(R.id.ll_shared);

				ll_unintall.setOnClickListener(SoftManagerActivity.this);
				ll_start.setOnClickListener(SoftManagerActivity.this);
				ll_shared.setOnClickListener(SoftManagerActivity.this);

				// 创建popupWindow
				popupWindow = new PopupWindow(popupView, -2, -2);
				// 指定popupWindow 的背景
				popupWindow.setBackgroundDrawable(new ColorDrawable(
						Color.TRANSPARENT));// 给popupWindow设置了背景
				// 指定位置显示popupWindow parent popupWindow挂载的父容器
				// 显示popup
				popupWindow.showAtLocation(parent, Gravity.TOP | Gravity.LEFT,
						x + DensityUtil.dp2px(SoftManagerActivity.this, 60), y);
				// dp 比例 像素和屏幕密度比例

				AlphaAnimation alphaAnimation = new AlphaAnimation(0.4f, 1);
				alphaAnimation.setDuration(1000);

				ScaleAnimation animation = new ScaleAnimation(0, 1, 0, 1,
						Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_SELF, 0.5f);
				animation.setDuration(1000);
				// 定义动画的集合 添加了两个动画
				AnimationSet animationSet = new AnimationSet(false);
				animationSet.addAnimation(alphaAnimation);
				animationSet.addAnimation(animation);
				popupView.startAnimation(animationSet);// 同时播放多个动画
			}
		});
	}

	/**
	 * 2 加载界面
	 */
	public void fillData() {
		new MyAsykTask() {
			@Override
			public void onPreExecute() {
				DialogUtils.showProgressDialog(SoftManagerActivity.this);
			}

			@Override
			public void onPostExecute() {
				adapter = new AppAdapter();
				lv_soft_manager.setAdapter(adapter);
				DialogUtils.CloseAllDialog();
			}

			@Override
			public void doInBackground() {
				appInfos = AppProvider.getAllAppInfos(getApplicationContext());
				userInfos = new ArrayList<AppInfo>();
				systemInfos = new ArrayList<AppInfo>();
				for (AppInfo info : appInfos) {
					if (info.isUser()) {
						userInfos.add(info);
					} else {
						systemInfos.add(info);
					}
				}

			}

		}.execute();
	}

	/**
	 * 3 适配器
	 * 
	 * @author yu
	 *
	 */
	private class AppAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return userInfos.size() + systemInfos.size() + 2;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (position == 0) {
				TextView tv = new TextView(getApplicationContext());
				tv.setText("用户程序" + userInfos.size() + "个");
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				return tv;
			} else if (position == userInfos.size() + 1) {
				TextView tv = new TextView(getApplicationContext());
				tv.setText("系统程序" + systemInfos.size() + "个");
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				return tv;
			}
			
			if (position <= userInfos.size()) {
				appInfo = userInfos.get(position - 1);
			} else {
				appInfo = systemInfos.get(position - userInfos.size() - 2);
			}
			View view = null;
			ViewHolder holder = null;
			// 保证convertView 不是textView 才可以复用view对象
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.item_soft_manager, null);
				holder = new ViewHolder();
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
				holder.tv_version = (TextView) view
						.findViewById(R.id.tv_version);
				holder.tv_position = (TextView) view
						.findViewById(R.id.tv_position);
				holder.iv_lock = (ImageView) view.findViewById(R.id.iv_lock);
				view.setTag(holder);
			}
			holder.iv_icon.setImageDrawable(appInfo.getIcon());
			holder.tv_name.setText(appInfo.getName());
			holder.tv_version.setText(appInfo.getVersion());
			if (appInfo.isSD()) {
				holder.tv_position.setText("SD内存");
			} else {
				holder.tv_position.setText("手机内存");
			}
			holder.iv_lock.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					WatchDogDao dao = new WatchDogDao(SoftManagerActivity.this);
					dao.addApp(appInfo.getPackageName());
					
				}
			});

			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

	}

	class ViewHolder {
		TextView tv_name;
		TextView tv_position;
		TextView tv_version;
		ImageView iv_icon;
		ImageView iv_lock;
	}

	// 5.1
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 防止窗体泄露
		if (popupWindow != null) {
			popupWindow.dismiss();
			popupWindow = null;
		}

	}

	/**
	 * 6 处理popupwindow 一些功能 启动 卸载 分享 详细信息
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_uninstall: // 点击的卸载的按钮
			uninstall();
			break;
		case R.id.ll_start:// 点击的启动按钮
			start();
			break;
		case R.id.ll_shared:// 点击的分享
			shared();
			break;
		default:
			break;

		}
		if (popupWindow != null) {
			popupWindow.dismiss();
			popupWindow = null;
		}
	}

	/**
	 * 6.3分享
	 */
	private void shared() {
		// <intent-filter>
		// <action android:name="android.intent.action.SEND" />
		// <category android:name="android.intent.category.DEFAULT" />
		// <data android:mimeType="text/plain" />
		// </intent-filter>
		 Intent intent=new Intent();
		 intent.setAction("android.intent.action.SEND");
		 intent.setType("text/plain");
		 intent.putExtra(Intent.EXTRA_TEXT,
		 "推荐您使用一款软件"+appInfo.getName()+",下载地址google市场");
		 startActivity(intent);

		// 详细信息
//		Intent intent = new Intent();
//		intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
//		intent.setData(Uri.parse("package:" + appInfo.getPackageName()));
//		startActivity(intent);
	}

	/**
	 * 6.2 启动
	 */
	private void start() {
		PackageManager manager = getPackageManager();
		// 可以获取到指定包名的应用程序的启动的intent对象
		Intent launchIntentForPackage = manager
				.getLaunchIntentForPackage(appInfo.getPackageName());
		if (launchIntentForPackage != null) {
			startActivity(launchIntentForPackage);
		} else {
			ToastUtils.TextToast("系统底层应用不能启动");
		}
	}

	/**
	 * 6.1卸载
	 */
	private void uninstall() {
		// <intent-filter>
		// <action android:name="android.intent.action.VIEW" />
		// <action android:name="android.intent.action.DELETE" />
		// <category android:name="android.intent.category.DEFAULT" />
		// <data android:scheme="package" />
		// </intent-filter>
		if (!appInfo.isUser()) {
			ToastUtils.TextToast("系统程序不能直接卸载");
			return;
		}

		if (!appInfo.getPackageName().equals(getPackageName())) {
			Intent intent = new Intent();
			intent.setAction("android.intent.action.DELETE");
			intent.addCategory("android.intent.category.DEFAULT");
			intent.setData(Uri.parse("package:" + appInfo.getPackageName()));
			startActivityForResult(intent, 0);

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		fillData();
	}

}
