package com.fwj.mobilesafe.base;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;

public abstract class MyBaseViewPager<T> extends PagerAdapter {
	public ArrayList<T> devices;

	@SuppressWarnings("unchecked")
	public void setDeviceList(ArrayList<T> list) {
		if (list != null) {
			devices = (ArrayList<T>) list.clone();
			notifyDataSetChanged();
		}
	}

	public void clearDeviceList() {
		if (devices != null) {
			devices.clear();
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return devices.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
}