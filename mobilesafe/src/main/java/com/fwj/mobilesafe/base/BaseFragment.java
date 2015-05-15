package com.fwj.mobilesafe.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

	public FragmentActivity mActivity;

	/**
	 * 此方法可以得到上下文对象
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	/**
	 * 自定义的OnResume方法，每次进入该Fragment时候都会调用此方法
	 */
	public abstract void onFragmentResume();

	/*
	 * 返回一个需要展示的View
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = getActivity();
		View view = initView(inflater);
		return view;
	}

	/*
	 * 当Activity初始化之后可以在这里进行一些数据的初始化操作
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
	}

	/**
	 * 子类实现此抽象方法返回View进行展示
	 * 
	 * @return
	 */
	public abstract View initView(LayoutInflater inflater);

	/**
	 * 子类覆盖此方法实现数据的初始化（若不需初始化数据可不复写此方法）
	 */
	public void initData() {

	}

	public void onFragmentResult(int requestCode, int resultCode, Intent data) {

	}

	public void onFragmentDestroy() {

	}

}
