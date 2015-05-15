package com.fwj.mobilesafe.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.fwj.mobilesafe.R;
import com.fwj.mobilesafe.uitils.ToastUtils;

/**
 * ================================ 文件名：Setup3Activity.java
 * 
 * 描 述：手机防盗引导3
 * 
 * 作 者：傅文江
 * 
 * 时 间：下午4:56:30
 * 
 * 版 权：©个人开发者 傅文江 版权所有 ================================
 */
public class Setup3Activity extends SetupBaseActivity {
	private EditText et_safenum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup3);
		et_safenum = (EditText) findViewById(R.id.et_safenum);

		// 数据回显
		String safenum = sp.getString("safenum", "");
		et_safenum.setText(safenum);
	}

	// 选择联系人的按钮
	public void selectContacts(View v) {

		Intent intent = new Intent();
		intent.setAction("android.intent.action.PICK");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("vnd.android.cursor.dir/phone_v2");
		startActivityForResult(intent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if (data != null) {
		// String stringExtra = data.getStringExtra("num");
		// et_safenum.setText(stringExtra);
		// }

		if (data != null) {
			// String num = data.getStringExtra("num");
			Uri uri = data.getData();
			String num = null;
			// 创建内容解析者
			ContentResolver contentResolver = getContentResolver();
			Cursor cursor = contentResolver.query(uri, null, null, null, null);
			while (cursor.moveToNext()) {
				num = cursor.getString(cursor.getColumnIndex("data1"));

			}
			cursor.close();
			// 把"-" 删除
			num = num.replaceAll("-", "");
			et_safenum.setText(num);
		}
	}

	@Override
	public void next_activity() {
		String safenum = et_safenum.getText().toString().trim();
		if (TextUtils.isEmpty(safenum)) {
			ToastUtils.TextToast("安全号码不能为空");
			return;
		}
		Editor edit = sp.edit();
		edit.putString("safenum", safenum);
		edit.commit();

		Intent intent = new Intent(getApplicationContext(),
				Setup4Activity.class);
		startActivity(intent);
		finish();
		// 参数1 新的activity进入的动画
		// 参数2 旧的activity移出的动画
		overridePendingTransition(R.anim.tran_next_enter, R.anim.tran_next_exit);
	}

	@Override
	public void pre_activity() {
		Intent intent = new Intent(getApplicationContext(),
				Setup2Activity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(R.anim.tran_pre_enter, R.anim.tran_pre_exit);
	}

}
