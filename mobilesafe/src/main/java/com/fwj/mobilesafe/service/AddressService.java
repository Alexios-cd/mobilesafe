package com.fwj.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.fwj.mobilesafe.R;
import com.fwj.mobilesafe.db.dao.AddressDao;

public class AddressService extends Service {
	private TelephonyManager manager;
	private AddressDao addressDao;
	private MyPhoneStateListener listener;
	private OutGoingCallReceiver callReceiver;
	
	private SharedPreferences sp;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		sp=getSharedPreferences("config", MODE_PRIVATE);
		// 注册广播
		callReceiver = new OutGoingCallReceiver();
		IntentFilter filter = new IntentFilter();
		// 添加action
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(callReceiver, filter);// 注册成功

		addressDao = new AddressDao(this);
		// 1 创建TelePhoneManager监听电话状态的变化
		manager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		// 2 监听电话状态变化
		listener = new MyPhoneStateListener();
		manager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	private class OutGoingCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 外拨电话的电话号码
			String phonenum = getResultData();
			String queryAddress = addressDao.queryAddress(phonenum);
			showMyToast(queryAddress);
		}
	}

	// 3
	private class MyPhoneStateListener extends PhoneStateListener {
		@Override
		// state 变化后的电话状态
		// incomingNumber 来电号码
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE: // 空闲状态
				hideMyToast();
				break;
			case TelephonyManager.CALL_STATE_RINGING: // 响铃状态
				String queryAddress = addressDao.queryAddress(incomingNumber);
				// Toast.makeText(getApplicationContext(), queryAddress,
				// 1).show();
				showMyToast(queryAddress);
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:// 接电话的状态
			default:
				break;
			}

		}

	}

	private WindowManager wm;
	private View view;
	private WindowManager.LayoutParams params;

	public void hideMyToast() {
		if (wm != null && view != null) {
			wm.removeView(view);
			wm = null;
			view = null;
		}
	}

	public void showMyToast(String str) {
		hideMyToast();
		int[] bgcolor = new int[] { 
				R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		view = View.inflate(getApplicationContext(), R.layout.my_toast, null);
		view.setBackgroundResource(bgcolor[sp.getInt("which", 0)]);// 动态切换Toast背景
		TextView tv_location = (TextView) view.findViewById(R.id.tv_location);
		tv_location.setText(str);
		
		setTouch();
		params = new WindowManager.LayoutParams();
		
		params.gravity=Gravity.LEFT+Gravity.TOP;// top上面对齐  left左面面对齐 
		params.x=sp.getInt("lastX", 100);  // Gravity.LEFT 距离左面的距离 Gravity.Right 距离右面的距离 
		params.y=sp.getInt("lastY", 100);  // Gravity.TOP  距离上面的距离 Gravity.Bottom 距离下面的距离 
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		// 不可获取焦点
		params.flags =WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				 |WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON; // 保持屏幕常亮
		params.format = PixelFormat.TRANSLUCENT; // 半透明
		params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE; // 定义类型 toast类型
		// 第一个参数 显示的布局 第二个参数显示布局需要的参数
		
		wm.addView(view, params);
		
	}

	public void setTouch() {
		view.setOnTouchListener(new OnTouchListener() {
			
			private int startX;
			private int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 1 记录开始的位置
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:// 移动
					// 2 获取新的位置
					int newX=(int) event.getRawX();
					int newY=(int) event.getRawY();
					// 3 计算位置的变化
					int dX=newX-startX;
					int dY=newY-startY;
					// 4控件 重新分配位置 
					params.x=params.x+dX;
					params.y=params.y+dY;
					wm.updateViewLayout(view, params);// 更新控件的params参数
					// 5 
					startX=newX;
					startY=newY;
				}
				
				
				return true;
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (listener != null) {
			manager.listen(listener, PhoneStateListener.LISTEN_NONE);// 取消监听
			listener = null;
		}
		// 反注册广播接受者
		if (callReceiver != null) {
			unregisterReceiver(callReceiver);
			callReceiver = null;
		}
	}
}
