package com.fwj.mobilesafe.activity;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import com.fwj.mobilesafe.R;
import com.fwj.mobilesafe.base.BaseActivity;
import com.fwj.mobilesafe.db.dao.BlanknumDao;
import com.fwj.mobilesafe.domain.BlankNumInfo;
import com.fwj.mobilesafe.uitils.MyAsykTask;
import com.fwj.mobilesafe.uitils.ToastUtils;
import com.fwj.mobilesafe.uitils.adpter.CommonAdater;

/**
 * ================================ 文件名：CallSmsSafeActivity.java
 * 
 * 描 述：通讯卫士UI
 * 
 * 作 者：傅文江
 * 
 * 时 间：下午4:36:53
 * 
 * 版 权：©个人开发者 傅文江 版权所有 ================================
 */
public class CallSmsSafeActivity extends BaseActivity {
	private ListView lv_call_sms;
	private CallSmsAdapter adapter;
	private BlanknumDao blanknumDao;
	private List<BlankNumInfo> blanknumInfos;
	private ProgressBar pb_process;
	private static final int MAX_NUM = 20;
	private int startIndex = 0;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_call_sms_safe);
		lv_call_sms = (ListView) findViewById(R.id.lv_call_sms);
		pb_process = (ProgressBar) findViewById(R.id.pb_process);

	}

	@Override
	protected void initData() {
		// 查询数据库 获取所有黑名单
		blanknumDao = new BlanknumDao(this);
		fillData();
		// 给listView添加滑动的事件
		lv_call_sms.setOnScrollListener(new OnScrollListener() {
			// 当listVIew 滑动状态发生变化调用
			// 静止
			// 滑动
			// 惯性滑动 飞速滑动
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					// 静止 可以看到最后一条目 应该加速新的数据
					if (lv_call_sms.getLastVisiblePosition() == blanknumInfos
							.size() - 1) {
						startIndex += 20;
						fillData();
					}
				}
			}

			// 当listView 正在滚动的调用
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});

	}

	public void fillData() {
		new MyAsykTask() {
			@Override
			public void onPreExecute() {
				pb_process.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPostExecute() {
				if (adapter == null) {
					adapter = new CallSmsAdapter(CallSmsSafeActivity.this,
							R.layout.item_blanknum, blanknumInfos);
					lv_call_sms.setAdapter(adapter);
				} else {
					adapter.notifyDataSetChanged();
				}

				pb_process.setVisibility(View.INVISIBLE);
			}

			@Override
			public void doInBackground() {
				// 0 -19
				if (blanknumInfos == null) {
					blanknumInfos = blanknumDao.queryPart(MAX_NUM, startIndex);

				} else {
					blanknumInfos.addAll(blanknumDao.queryPart(MAX_NUM,
							startIndex));
				}
			}

		}.execute();
	}

	/**
	 * 添加黑名单
	 * 
	 * @param v
	 */
	public void addBlacknum(View v) {
		AlertDialog.Builder builder = new Builder(this, R.style.dialog);
		final AlertDialog create = builder.create();
		View view = View.inflate(this, R.layout.dialog_add_blacknum, null);
		// 初始化控件
		final EditText et_blacknum = (EditText) view
				.findViewById(R.id.et_blacknum);
		final RadioGroup rg_blacknum = (RadioGroup) view
				.findViewById(R.id.rg_blacknum);
		Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

		btn_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String num = et_blacknum.getText().toString().trim();
				if (TextUtils.isEmpty(num)) {
					ToastUtils.TextToast("黑名单号码不能为空");
					return;
				}
				int mode = 0;
				switch (rg_blacknum.getCheckedRadioButtonId()) {
				case R.id.rb_sms:
					mode = BlanknumDao.MODE_SMS;
					break;
				case R.id.rb_tel:
					mode = BlanknumDao.MODE_TEL;
					break;
				case R.id.rb_all:
					mode = BlanknumDao.MODE_ALL;
					break;
				}
				blanknumDao.addBlanknum(num, mode);
				blanknumInfos.add(0, new BlankNumInfo(num, mode));
				adapter.notifyDataSetChanged();
				create.dismiss();
			}
		});

		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				create.dismiss();
			}
		});

		create.setView(view, 0, 0, 0, 0);
		create.show();
	}

	private class CallSmsAdapter extends CommonAdater<BlankNumInfo> {

		public CallSmsAdapter(Context context, int layoutItemt,
				List<BlankNumInfo> datas) {
			super(context, layoutItemt, datas);
		}

		@Override
		public void convert(com.fwj.mobilesafe.uitils.adpter.ViewHolder holder,
				final BlankNumInfo blankNumInfo, int position) {
			holder.setText(R.id.tv_num, blankNumInfo.getNum());
			int mode = blankNumInfo.getMode();
			if (mode == BlanknumDao.MODE_TEL) {
				holder.setText(R.id.tv_mode, "电话拦截");
			} else if (mode == BlanknumDao.MODE_SMS) {
				holder.setText(R.id.tv_mode, "短信拦截");
			} else if (mode == BlanknumDao.MODE_ALL) {
				holder.setText(R.id.tv_mode, "全部拦截");
			}
			holder.getView(R.id.iv_delete).setOnClickListener(
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							AlertDialog.Builder builder = new Builder(
									CallSmsSafeActivity.this);
							builder.setTitle("警告:");
							builder.setMessage("是否删除黑名单"
									+ blankNumInfo.getNum());
							builder.setIcon(R.drawable.ic_launcher);
							builder.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// 删除黑名单
											// 删除数据库
											blanknumDao
													.deleteBlankNum(blankNumInfo
															.getNum());
											// 删除集合中的元素
											blanknumInfos.remove(blankNumInfo);
											adapter.notifyDataSetChanged();// 更新界面
											dialog.dismiss();
										}
									});
							builder.setNegativeButton("取消", null);
							builder.show();// 显示dialog

						}
					});
		}
	}

	

}
