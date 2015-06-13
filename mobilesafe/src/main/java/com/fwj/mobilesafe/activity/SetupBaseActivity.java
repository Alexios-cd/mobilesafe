package com.fwj.mobilesafe.activity;

import android.content.SharedPreferences;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.fwj.mobilesafe.base.BaseActivity;
import com.fwj.mobilesafe.uitils.DensityUtil;
import com.fwj.mobilesafe.uitils.ToastUtils;

/**
 * ================================
 * 文件名：SetupBaseActivity.java
 * 
 * 描    述：模板设计模式,统一管理手势
 * 
 * 作    者：傅文江
 * 
 * 时    间：下午4:55:21
 * 
 * 版    权：©个人开发者 傅文江 版权所有
 * ================================
 */
public abstract class SetupBaseActivity extends BaseActivity {
	protected SharedPreferences sp;
	// 1定义手势识别
	private GestureDetector detector;

	@Override
	protected void initView() {

	}

	@Override
	protected void initData() {
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 2初始化
		detector = new GestureDetector(this, new MySimpleOnGestureListener());

	}

	public abstract void next_activity();

	public abstract void pre_activity();

	// 3 处理手势事件
	private class MySimpleOnGestureListener extends SimpleOnGestureListener {
		// 猛动 滑动
		// MotionEvent e1 滑动之前按下的事件
		// MotionEvent e2 滑动最后抬起的事件
		// velocityX x轴速率 V
		// velocityY y轴速率
		// true if the event is consumed, 执行了 else false 没有执行
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			float startX = e1.getRawX();
			float endX = e2.getRawX();
			float startY = e1.getRawY();
			float endY = e2.getRawY();
			if (Math.abs(startY - endY) > 200) {
				ToastUtils.TextToast("不要乱滑");
				return true;
			}
			if ((startX - endX) > DensityUtil.dp2px(getApplicationContext(),
					100)) {
				next_activity();
			} else if ((endX - startX) > DensityUtil.dp2px(
					getApplicationContext(), 100)) {
				pre_activity();
			}
			// ViewPager

			return true;
		}

	}

	// 4处理触摸事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		detector.onTouchEvent(event);// 把手势识别注册到触摸事件上
		return super.onTouchEvent(event);
	}

	// 返回按钮事件
	// @Override
	// public void onBackPressed() {
	// super.onBackPressed();
	// pre_activity();
	// }
	// 一般可以处理所有按键
	// keyCode 点击的是哪个键
	// return true 处理了事件 false 代表没有处理
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			pre_activity();
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 下一步
	 * 
	 * @param v
	 */
	public void next(View v) {
		next_activity();
	}

	/**
	 * 上一步
	 * 
	 * @param v
	 */
	public void pre(View v) {
		pre_activity();
	}

}
