package com.fwj.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class FocusedTextView extends TextView {
	// 在代码中使用控件
	public FocusedTextView(Context context) {
		super(context);
	}

	// 使用自定义的样式
	public FocusedTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// 在布局文件中 使用控件
	public FocusedTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// 判断到控件是否获取到焦点
	@Override
	public boolean isFocused() {
		return true; // 欺骗了系统
	}

}
