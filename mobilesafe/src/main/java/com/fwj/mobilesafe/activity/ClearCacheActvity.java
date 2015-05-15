package com.fwj.mobilesafe.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.fwj.mobilesafe.R;
import com.fwj.mobilesafe.fragment.CacheFragment;
import com.fwj.mobilesafe.fragment.SDFragment;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ClearCacheActvity extends FragmentActivity {
	@ViewInject(R.id.tv_cache)
	private TextView tv_cache;
	@ViewInject(R.id.tv_sd)
	private TextView tv_sd;
	boolean flag; // false 代表显示cache true sd
	private CacheFragment cacheFragment;
	private SDFragment sdFragment;
	private FragmentTransaction beginTransaction;
	private FragmentManager supportFragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clear_cache);
		ViewUtils.inject(this);
		cacheFragment = new CacheFragment();
		sdFragment = new SDFragment();
		supportFragmentManager = getSupportFragmentManager();
		beginTransaction = supportFragmentManager.beginTransaction();

		tv_cache.setBackgroundResource(R.drawable.btn_green_pressed);
		beginTransaction.add(R.id.rl_content, cacheFragment);
		beginTransaction.add(R.id.rl_content, sdFragment);
		beginTransaction.hide(sdFragment);
		beginTransaction.show(cacheFragment);

		beginTransaction.commit();

		tv_cache.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (flag) {
					flag = false;
					tv_cache.setBackgroundResource(R.drawable.btn_green_pressed);
					tv_sd.setBackgroundResource(R.drawable.btn_green_normal);
					beginTransaction = supportFragmentManager
							.beginTransaction();
					// beginTransaction.replace(R.id.rl_content, cacheFragment);
					beginTransaction.hide(sdFragment);
					beginTransaction.show(cacheFragment);
					beginTransaction.commit();
				}
			}
		});
		tv_sd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!flag) {
					flag = true;
					tv_cache.setBackgroundResource(R.drawable.btn_green_normal);
					tv_sd.setBackgroundResource(R.drawable.btn_green_pressed);
					beginTransaction = supportFragmentManager
							.beginTransaction();
					// beginTransaction.replace(R.id.rl_content, sdFragment);
					beginTransaction.hide(cacheFragment);
					beginTransaction.show(sdFragment);
					beginTransaction.commit();
				}
			}
		});
	}

}
