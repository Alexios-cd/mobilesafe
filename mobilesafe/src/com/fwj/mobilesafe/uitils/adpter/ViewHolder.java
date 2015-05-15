package com.fwj.mobilesafe.uitils.adpter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ================================
 * 文件名：ViewHolder.java
 * 
 * 描    述：通用适配器的ViewHolder（与CommonAdater搭配使用）
 * 
 * 作    者：傅文江
 * 
 * 时    间：下午4:03:15
 * 
 * 版    权：©个人开发者 傅文江 版权所有
 * ================================
 */
public class ViewHolder {
	private SparseArray<View> mViews;
	private int mPosition;
	private View mConvertView;

	public View getmConvertView() {
		return mConvertView;
	}

	public ViewHolder(Context context, ViewGroup parent, int layoutId,
			int position) {
		this.mPosition = position;
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
				false);
		mConvertView.setTag(this);
	}

	public static ViewHolder get(Context context, View convertView,
			ViewGroup parent, int position, int layoutId) {
		if (null == convertView) {
			return new ViewHolder(context, parent, layoutId, position);
		} else {
			ViewHolder holder = (ViewHolder) convertView.getTag();
			holder.mPosition = position;
			return holder;
		}
	}

	/**
	 * 通过viewId获取控件
	 * 
	 * @param viewId
	 * @return
	 */
	public <T extends View> T getView(int viewId) {
		View view = mViews.get(viewId);
		if (null == view) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 设置TextView的值
	 * 
	 * @param viewId
	 * @param text
	 * @return
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView tv = getView(viewId);
		tv.setText(text);
		return this;
	}

	/**
	 * 设置 ImageView
	 * @param viewId
	 * @param resId
	 * @return
	 */
	public ViewHolder setImageView(int viewId, int resId) {
		ImageView imageView = getView(viewId);
		imageView.setImageResource(resId);
		return this;
	}

}
