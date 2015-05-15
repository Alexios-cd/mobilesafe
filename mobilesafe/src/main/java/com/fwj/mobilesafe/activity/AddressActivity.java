package com.fwj.mobilesafe.activity;

import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.fwj.mobilesafe.R;
import com.fwj.mobilesafe.base.BaseActivity;
import com.fwj.mobilesafe.db.dao.AddressDao;
import com.fwj.mobilesafe.uitils.ToastUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * ================================
 * 文件名：AddressActivity.java
 * 
 * 描    述：号码归属地查询UI
 * 
 * 作    者：傅文江
 * 
 * 时    间：下午5:00:47
 * 
 * 版    权：©个人开发者 傅文江 版权所有
 * ================================
 */
public class AddressActivity extends BaseActivity {
	@ViewInject(R.id.tv_address)
	private TextView tv_address;
	@ViewInject(R.id.et_phonenum)
	private EditText et_phone_num;
	private AddressDao addressDao;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_address);
		ViewUtils.inject(this); // findViewById

	}

	@Override
	protected void initData() {
		addressDao = new AddressDao(this);
		// 监听EditText文本的变化
		et_phone_num.addTextChangedListener(new TextWatcher() {
			// 文本发生变化
			// s 变化后的文本
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				String phonenum = s.toString();
				String queryAddress = addressDao.queryAddress(phonenum);
				if (!TextUtils.isEmpty(queryAddress)) {
					tv_address.setText(queryAddress);
				}
			}

			// 变化之前
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}

			// 变化之后
			@Override
			public void afterTextChanged(Editable s) {

			}
		});

	}

	public void query(View v) {
		String phonenum = et_phone_num.getText().toString().trim();
		if (!TextUtils.isEmpty(phonenum)) {
			String address = addressDao.queryAddress(phonenum);
			if (!TextUtils.isEmpty(address)) {
				tv_address.setText(address);
			} else {
				ToastUtils.TextToast("数据库未收录");
			}
		} else {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(50);
			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
			// shake.setInterpolator(new Interpolator() {
			//
			// @Override
			// public float getInterpolation(float x) {
			// return x*x;
			// }
			// });
			et_phone_num.startAnimation(shake);
		}
	}

}
