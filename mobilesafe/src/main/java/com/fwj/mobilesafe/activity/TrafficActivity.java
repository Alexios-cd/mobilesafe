package com.fwj.mobilesafe.activity;

import android.net.TrafficStats;
import android.text.format.Formatter;
import android.widget.TextView;

import com.fwj.mobilesafe.R;
import com.fwj.mobilesafe.base.BaseActivity;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * ================================
 * 文件名：TrafficActivity.java
 * 
 * 描    述：流量统计UI
 * 
 * 作    者：傅文江
 * 
 * 时    间：下午4:54:21
 * 
 * 版    权：©个人开发者 傅文江 版权所有
 * ================================
 */
public class TrafficActivity extends BaseActivity {
	@ViewInject(R.id.tv_traffic_tx)
	private TextView tv_traffic_tx;
	@ViewInject(R.id.tv_traffic_rx)
	private TextView tv_traffic_rx;
	@ViewInject(R.id.tv_traffic_totle_tx)
	private TextView tv_traffic_totle_tx;
	@ViewInject(R.id.tv_traffic_totle_rx)
	private TextView tv_traffic_totle_rx;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_traffic);

	}

	@Override
	protected void initData() {
		TrafficStats.getMobileRxBytes(); // 手机 接受流量 不包含wifi
		TrafficStats.getMobileTxBytes(); // 手机上传的流量
		TrafficStats.getTotalRxBytes(); // 总接受流量 包含wifi
		TrafficStats.getTotalTxBytes(); // 总上传的流量
		// info.applicationInfo.uid
		// TrafficStats.getUidRxBytes(uid);// 根据指定程序的uid 获取到对应流量
		// TrafficStats.get
		tv_traffic_tx.setText("手机接受流量（不包含wifi）"
				+ Formatter.formatFileSize(this,
						TrafficStats.getMobileRxBytes()));
		tv_traffic_rx.setText("手机上传的流量"
				+ Formatter.formatFileSize(this,
						TrafficStats.getMobileTxBytes()));
		tv_traffic_totle_tx
				.setText("总上传流量 （包含wifi）"
						+ Formatter.formatFileSize(this,
								TrafficStats.getTotalTxBytes()));
		tv_traffic_totle_rx
				.setText("总接受流量 （包含wifi）"
						+ Formatter.formatFileSize(this,
								TrafficStats.getTotalRxBytes()));

	}
}
