package com.fwj.mobilesafe.activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.fwj.mobilesafe.R;
import com.fwj.mobilesafe.base.BaseActivity;

/**
 * ================================
 * 文件名：DragViewActivity.java
 * 
 * 描    述：吐司拖拽UI
 * 
 * 作    者：傅文江
 * 
 * 时    间：下午4:59:57
 * 
 * 版    权：©个人开发者 傅文江 版权所有
 * ================================
 */
public class DragViewActivity extends BaseActivity {
	private LinearLayout ll_drag_view;
	private SharedPreferences sp;
	private WindowManager windowManager;
	private int widthPixels;
	private int heightPixels;
	private TextView tv_top;
	private TextView tv_bottom;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_drag_view);

	}

	@Override
	protected void initData() {

		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		ll_drag_view = (LinearLayout) findViewById(R.id.ll_drag_view);
		tv_top = (TextView) findViewById(R.id.tv_top);
		tv_bottom = (TextView) findViewById(R.id.tv_bottom);
		int l = sp.getInt("lastX", 100);
		int t = sp.getInt("lastY", 100);

		DisplayMetrics outMetrics = new DisplayMetrics(); // 创建了一个白纸
		windowManager.getDefaultDisplay().getMetrics(outMetrics);// 在白纸上写上屏幕的款和高
		widthPixels = outMetrics.widthPixels;
		heightPixels = outMetrics.heightPixels;
		// System.out.println("l:"+l);
		// System.out.println("t:"+t);
		//
		// System.out.println(ll_drag_view.getWidth());
		// System.out.println(ll_drag_view.getHeight());
		// int r=l+ll_drag_view.getWidth();
		// int b=t+ll_drag_view.getHeight();
		// // 回显位置 second 这个方法 在加载界面的第二步执行
		// ll_drag_view.layout(l, t, r, b);
		// 控件显示的参数
		RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) ll_drag_view
				.getLayoutParams();
		params.leftMargin = l;
		params.topMargin = t;
		// 给控件设置参数 告诉控件 距离左边距离上面像素
		ll_drag_view.setLayoutParams(params);

		doubleClick();
		touch();

	}

	// private long firstTime;
	long[] mHits = new long[2];

	public void doubleClick() {
		ll_drag_view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 单位时间内 点击了两次
				// 记录第一次点击的时间
				// if (firstTime == 0) {
				// firstTime = System.currentTimeMillis();
				// new Thread(){
				// public void run() {
				// try {
				// Thread.sleep(500);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
				// firstTime=0;
				// };
				//
				// }.start();
				//
				// }else{
				// long sencondTime=System.currentTimeMillis();
				// if(sencondTime-firstTime<500){
				// System.out.println("被双击了");
				// firstTime=0;
				// }
				// }
				System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
				mHits[mHits.length - 1] = SystemClock.uptimeMillis(); // 距离手机开机的时间
				if (mHits[0] >= (SystemClock.uptimeMillis() - 500)) {
					System.out.println("双击事件");
					int l = (widthPixels - ll_drag_view.getWidth()) / 2;
					int t = ((heightPixels - ll_drag_view.getHeight()) - 100) / 2;
					int r = l + ll_drag_view.getWidth();
					int b = t + ll_drag_view.getHeight();
					ll_drag_view.layout(l, t, r, b);// 重新分配控件的位置
				}

			}
		});
	}

	public void touch() {
		// 拖拽事件
		ll_drag_view.setOnTouchListener(new OnTouchListener() {

			private int startX;
			private int startY;

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: // 按下事件
					System.out.println("ACTION_DOWN");
					// 步骤1 记录开始的位置
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					// 步骤2 记录移动后新的坐标
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					// 步骤3 记录新的位置和旧的位置 坐标改变
					int dX = newX - startX;
					int dY = newY - startY;
					// 步骤4 重新分配控件的位置
					// l left t top r right b bottom
					int l = ll_drag_view.getLeft() + dX;
					int t = ll_drag_view.getTop() + dY;
					int r = l + ll_drag_view.getWidth();
					int b = t + ll_drag_view.getHeight();
					// 一旦移除屏幕 直接跳出事件
					if (l < 0 || r > widthPixels || t < 0
							|| b > heightPixels - 15) {
						break;
					}
					//
					if (t > heightPixels / 2) {
						tv_top.setVisibility(View.VISIBLE);
						tv_bottom.setVisibility(View.INVISIBLE);
					} else {
						tv_top.setVisibility(View.INVISIBLE);
						tv_bottom.setVisibility(View.VISIBLE);
					}

					ll_drag_view.layout(l, t, r, b);

					// 步骤5 更新开始的位置
					startX = newX;
					startY = newY;

					System.out.println("ACTION_MOVE");
					break;
				case MotionEvent.ACTION_UP: // 抬起
					System.out.println("ACTION_UP");
					// 开始记录坐标
					int lastX = ll_drag_view.getLeft();
					int lastY = ll_drag_view.getTop();
					Editor edit = sp.edit();
					edit.putInt("lastX", lastX);
					edit.putInt("lastY", lastY);
					edit.commit();
					break;
				default:
					break;
				}
				// True if the listener has consumed the event, false otherwise.
				// true代表事件被消费 执行掉了 false 没有执行了
				return false;
			}
		});
	}

}
