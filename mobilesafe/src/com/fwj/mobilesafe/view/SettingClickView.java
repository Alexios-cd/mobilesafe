package com.fwj.mobilesafe.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fwj.mobilesafe.R;
///  自定义组合控件 
public class SettingClickView extends RelativeLayout {

	private TextView tv_title;
	private TextView tv_des;
	public SettingClickView(Context context) {
		super(context);
		init();
	}



	public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		
		
	}
	// 控件初始化
	private void init() {
		
		
		View view=View.inflate(getContext(), R.layout.setting_click_view, this); // 创建了view对象 顺便告诉view对象的爹是谁
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_des = (TextView) view.findViewById(R.id.tv_des);
		
		
		
	}
	/**
	 * 修改标题
	 * @param title
	 */
	public void setTitle(String title){
		tv_title.setText(title);
	}
	/**
	 * 修改描述信息
	 * @param des
	 */
	public void setDes(String des){
		tv_des.setText(des);
	}
}
