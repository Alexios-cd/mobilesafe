package com.fwj.mobilesafe.uitils.adpter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * ================================
 * 文件名：CommonAdater.java
 * 
 * 描    述：ListView & GridView 通用适配器（与ViewHolder搭配使用）
 * 
 * 作    者：傅文江
 * 
 * 时    间：下午4:02:40
 * 
 * 版    权：©个人开发者 傅文江 版权所有
 * ================================
 */
public abstract class CommonAdater<T> extends BaseAdapter {
	protected Context mContext;
	protected List<T> mDatas;
	protected LayoutInflater mInflater;
	private int mLayoutItemt;

	public CommonAdater(Context context, int layoutItemt, List<T> datas) {
		this.mContext = context;
		this.mDatas = datas;
		this.mInflater = LayoutInflater.from(context);
		this.mLayoutItemt = layoutItemt;
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = ViewHolder.get(mContext, convertView, parent,
				position, mLayoutItemt);
		convert(holder, getItem(position),position);
		return holder.getmConvertView();
	}

	/**
	 * 进行数据的设置<br>
	 * eg:holder.setText(R.id.item_title, bean.title).setText(
	 * R.id.item_content, bean.content);
	 * 
	 * @param holder
	 * @param bean
	 */
	public abstract void convert(ViewHolder holder, T bean,int position);

}
