package com.fwj.mobilesafe.uitils;

import android.annotation.SuppressLint;
import android.os.Handler;

/**
 * ============================================================
 * 
 * 版权 ：鼎开互联集团 版权所有 (c) 2014
 * 
 * 版本 ：var1.3
 * 
 * 创建日期 ：2014-11-24下午1:41:35
 * 
 * 描述 ： 自定义AsyncTask
 * 
 * 修订历史 ： ============================================================
 **/
public abstract class MyAsykTask {

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			onPostExecute();
		};
	};

	public void execute() {
		UIUtils.runInMainThread(new Runnable() {

			@Override
			public void run() {
				onPreExecute();
				new Thread() {
					public void run() {
						doInBackground();
						handler.sendEmptyMessage(0);
					}

				}.start();

			}
		});

	}

	/**
	 * 
	 * @param time
	 *            模拟耗时的时间
	 */
	public void execute(final int time) {
		onPreExecute();
		new Thread() {
			public void run() {
				SimulateConsumeTime(time);
				doInBackground();
				handler.sendEmptyMessage(0);
			}

		}.start();
	}

	/**
	 * 耗时操作执行前处理
	 */
	public abstract void onPreExecute();

	/**
	 * 执行耗时操作
	 */
	public abstract void doInBackground();

	/**
	 * 耗时操作执行后处理
	 */
	public  void onPostExecute() {
		// TODO Auto-generated method stub

	}

	private void SimulateConsumeTime(int time) {
		time = RandomUtils.getRandom((int) (time - time * 0.5),
				(int) (time + time * 0.5));
		try {
			Thread.sleep(time);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	};

}
