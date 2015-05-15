package com.fwj.mobilesafe.base;

import java.util.List;

import android.widget.BaseAdapter;

public abstract class MBaseAdater<T> extends BaseAdapter {

	@Override
	public int getCount() {
		return getData().size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	/**
	 * 此处用于设置数据源，否则会报空指针异常
	 * @return
	 */
	public abstract List<T> getData();

}
