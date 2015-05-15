package com.fwj.mobilesafe.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.graphics.Color;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fwj.mobilesafe.R;
import com.fwj.mobilesafe.base.BaseActivity;
import com.fwj.mobilesafe.domain.TaskInfo;
import com.fwj.mobilesafe.engine.TaskProvider;
import com.fwj.mobilesafe.uitils.DialogUtils;
import com.fwj.mobilesafe.uitils.MyAsykTask;
import com.fwj.mobilesafe.uitils.TaskUtils;
import com.fwj.mobilesafe.uitils.ToastUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * ================================
 * 文件名：TaskManagerActivity.java
 * 
 * 描    述：进程管理UI
 * 
 * 作    者：傅文江
 * 
 * 时    间：下午4:52:48
 * 
 * 版    权：©个人开发者 傅文江 版权所有
 * ================================
 */
public class TaskManagerActivity extends BaseActivity {
	@ViewInject(R.id.tv_task_count)
	private TextView tv_task_count;
	@ViewInject(R.id.tv_ram)
	private TextView tv_ram;
	@ViewInject(R.id.lv_task_manager)
	private ListView lv_task_manager;
	private TaskAdapter adapter;

	private List<TaskInfo> taskInfos;
	private List<TaskInfo> userInfos; // 用来存放用户程序
	private List<TaskInfo> systemInfos; // 用来存放系统程序
	private int taskCount;
	private long availableRam;
	private String totalRamSize;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_task_manager);
		ViewUtils.inject(this);

	}

	@Override
	protected void initData() {
		taskCount = TaskUtils.getTaskCount(this);
		tv_task_count.setText("当前手机的进程数:\n" + taskCount + "个");

		long totalRam = TaskUtils.getTotalRam();
		availableRam = TaskUtils.getAvailableRam(this);

		totalRamSize = Formatter.formatFileSize(this, totalRam);
		String availableRamSize = Formatter.formatFileSize(this, availableRam);
		tv_ram.setText("剩余/总内存:\n" + availableRamSize + "/" + totalRamSize);

		fillData();
		// 条目的点击事件
		lv_task_manager.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					return;
				} else if (position == userInfos.size() + 1) {
					return;
				}

				TaskInfo taskInfo = null;

				if (position <= userInfos.size()) {
					taskInfo = userInfos.get(position - 1);
				} else {
					taskInfo = systemInfos.get(position - userInfos.size() - 2);
				}
				if (taskInfo.getPackageName().equals(getPackageName())) {
					return;
				}
				ViewHolder holder = (ViewHolder) view.getTag();
				if (taskInfo.isChecked()) {
					taskInfo.setChecked(false);

					holder.cb.setChecked(false);
				} else {
					taskInfo.setChecked(true);
					holder.cb.setChecked(true);
				}
			}
		});

	}

	/**
	 * 全选
	 * 
	 * @param v
	 */
	public void all(View v) {
		for (TaskInfo info : userInfos) {
			if (info.getPackageName().equals(getPackageName())) {
				continue;
			}
			info.setChecked(true);
		}
		for (TaskInfo info : systemInfos) {
			info.setChecked(true);
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 取消
	 * 
	 * @param v
	 */
	public void cancel(View v) {
		for (TaskInfo info : userInfos) {
			info.setChecked(false);
		}
		for (TaskInfo info : systemInfos) {
			info.setChecked(false);
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 清理
	 * 
	 * @param v
	 */
	public void clear(View v) {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<TaskInfo> deleteInfos = new ArrayList<TaskInfo>();
		for (TaskInfo info : userInfos) {
			if (info.isChecked()) {
				manager.killBackgroundProcesses(info.getPackageName());
				// userInfos.remove(info);
				deleteInfos.add(info);
			}
		}

		for (TaskInfo info : systemInfos) {
			if (info.isChecked()) {
				// 只能清理后台或者空进程
				manager.killBackgroundProcesses(info.getPackageName());
				// systemInfos.remove(info);
				deleteInfos.add(info);
			}
		}
		int count = 0;
		long memSpace = 0;
		for (TaskInfo info : deleteInfos) {
			if (info.isUser()) {
				userInfos.remove(info);

			} else {
				systemInfos.remove(info);
			}
			memSpace += info.getMem();
			count++;
		}
		taskCount = taskCount - count;
		tv_task_count.setText("当前手机的进程数:\n" + taskCount + "个");
		availableRam += memSpace;
		String availableRamSize = Formatter.formatFileSize(this, availableRam);
		tv_ram.setText("剩余/总内存:\n" + availableRamSize + "/" + totalRamSize);
		ToastUtils.TextToast("清理"
						+ count
						+ "个进程,释放"
						+ Formatter.formatFileSize(getApplicationContext(),
								memSpace) + "内存");
		adapter.notifyDataSetChanged();

	}

	// 显示隐藏系统进程
	public void setting(View v) {
		if (isShowSystem) {
			isShowSystem = false;
		} else {
			isShowSystem = true;
		}
		adapter.notifyDataSetChanged();
	}

	/**
	 * 2 加载界面
	 */
	public void fillData() {
		new MyAsykTask() {
			@Override
			public void onPreExecute() {
				DialogUtils.showProgressDialog(TaskManagerActivity.this);
			}

			@Override
			public void onPostExecute() {
				adapter = new TaskAdapter();
				lv_task_manager.setAdapter(adapter);
				DialogUtils.CloseAllDialog();
			}

			@Override
			public void doInBackground() {
				taskInfos = TaskProvider.getTaskInfos(getApplicationContext());
				userInfos = new ArrayList<TaskInfo>();
				systemInfos = new ArrayList<TaskInfo>();
				for (TaskInfo info : taskInfos) {
					if (info.isUser()) {
						userInfos.add(info);
					} else {
						systemInfos.add(info);
					}
				}

			}

		}.execute();
	}

	boolean isShowSystem = true;

	private class TaskAdapter extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (position == 0) {
				TextView tv = new TextView(getApplicationContext());
				tv.setText("用户进程" + userInfos.size() + "个");
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				return tv;
			} else if (position == userInfos.size() + 1) {
				TextView tv = new TextView(getApplicationContext());
				tv.setText("系统进程" + systemInfos.size() + "个");
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				return tv;
			}
			TaskInfo taskInfo = null;
			if (position <= userInfos.size()) {
				taskInfo = userInfos.get(position - 1);
			} else {
				taskInfo = systemInfos.get(position - userInfos.size() - 2);
			}
			View view;
			ViewHolder holder;
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.item_task_manager, null);
				holder = new ViewHolder();
				holder.tv_name = (TextView) view.findViewById(R.id.tv_name);
				holder.tv_mem = (TextView) view.findViewById(R.id.tv_mem);
				holder.iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
				holder.cb = (CheckBox) view.findViewById(R.id.cb);
				view.setTag(holder);
			}
			if (TextUtils.isEmpty(taskInfo.getName())) {
				holder.tv_name.setText(taskInfo.getPackageName());
			} else {
				holder.tv_name.setText(taskInfo.getName());
			}
			if (taskInfo.getIcon() == null) {
				holder.iv_icon.setImageResource(R.drawable.icon_default);
			} else {
				holder.iv_icon.setImageDrawable(taskInfo.getIcon());
			}
			holder.tv_mem.setText("手机内存:"
					+ Formatter.formatFileSize(getApplicationContext(),
							taskInfo.getMem()));
			if (taskInfo.getPackageName().equals(getPackageName())) {
				holder.cb.setVisibility(View.INVISIBLE);
			} else {
				holder.cb.setVisibility(View.VISIBLE);
			}
			if (taskInfo.isChecked()) {
				holder.cb.setChecked(true);
			} else {
				holder.cb.setChecked(false);
			}
			return view;
		}

		@Override
		public int getCount() {
			if (isShowSystem) {
				return userInfos.size() + 1 + systemInfos.size() + 1;
			} else {
				return userInfos.size() + 1;
			}
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

	static class ViewHolder {
		ImageView iv_icon;
		TextView tv_name;
		TextView tv_mem;
		CheckBox cb;
	}

}
