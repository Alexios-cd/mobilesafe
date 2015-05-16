package com.fwj.mobilesafe.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fwj.mobilesafe.R;

///  自定义组合控件 
public class SettingView extends RelativeLayout {

	private TextView tv_title;
	private TextView tv_des;
	private CheckBox cb;
	private String des_on;
	private String des_off;

	public SettingView(Context context) {
		super(context);
		init();
	}

	public SettingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public SettingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		//
		//
		// System.out.println(attrs.getAttributeValue(0));
		// System.out.println(attrs.getAttributeValue(1));
		// System.out.println(attrs.getAttributeValue(2));
		// System.out.println(attrs.getAttributeValue(3));
		// System.out.println(attrs.getAttributeValue(4));
		// System.out.println(attrs.getAttributeValue(5));
		String title = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/res-auto",
				"title");
		des_on = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/res-auto",
				"des_on");
		des_off = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/res-auto",
				"des_off");

		setTitle(title);
		if (cb.isChecked()) {
			setDes(des_on);
		} else {
			setDes(des_off);
		}

	}

	// 控件初始化
	private void init() {
		// TextView tv=new TextView(getContext());
		// tv.setText("我是自定义组合控件");
		// View view=View.inflate(getContext(), R.layout.setting_view, null); //
		// 创建了view对象
		// this.addView(view,new LayoutParams(LayoutParams.MATCH_PARENT, 80));
		// // view父容器 添加了view对象

		View view = View.inflate(getContext(), R.layout.setting_view, this); // 创建了view对象
																				// 顺便告诉view对象的爹是谁
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_des = (TextView) view.findViewById(R.id.tv_des);
		cb = (CheckBox) view.findViewById(R.id.cb);

	}

	/**
	 * 修改标题
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		tv_title.setText(title);
	}

	/**
	 * 修改描述信息
	 * 
	 * @param des
	 */
	public void setDes(String des) {
		tv_des.setText(des);
	}

	/**
	 * 判断是否被选中
	 * 
	 * @return
	 */
	public boolean isChecked() {
		return cb.isChecked();
	}

	/**
	 * 设置选中状态
	 * 
	 * @param isChecked
	 */
	public void setChecked(boolean isChecked) {
		cb.setChecked(isChecked);

		if (cb.isChecked()) {
			setDes(des_on);
		} else {
			setDes(des_off);
		}
	}
}
