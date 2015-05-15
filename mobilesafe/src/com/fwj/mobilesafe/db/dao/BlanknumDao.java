package com.fwj.mobilesafe.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fwj.mobilesafe.db.BlankNumOpenHelper;
import com.fwj.mobilesafe.domain.BlankNumInfo;

public class BlanknumDao {
	public static final int MODE_SMS = 1;
	public static final int MODE_TEL = 0;
	public static final int MODE_ALL = 2;

	private BlankNumOpenHelper helper;
	private Context context;

	public BlanknumDao(Context context) {
		this.context = context;
		helper = new BlankNumOpenHelper(context);
	}

	/**
	 * ��Ӻ�����
	 * 
	 * @param num
	 *            ����������
	 * @param mode
	 *            ������������ģʽ
	 */
	public void addBlanknum(String num, int mode) {
		// helper.getReadableDatabase(); // û�м��������ݿ�
		SQLiteDatabase sql = helper.getWritableDatabase();// ���������ݿ�
		ContentValues values = new ContentValues();
		values.put("blanknum", num);
		values.put("mode", mode);
		// ����
		sql.insert("info", null, values);

		sql.close();
	}

	/**
	 * ɾ��������
	 * 
	 * @param num
	 *            ���ݵ绰����ɾ��
	 */
	public void deleteBlankNum(String num) {
		SQLiteDatabase sql = helper.getWritableDatabase();// ���������ݿ�
		sql.delete("info", "blanknum=?", new String[] { num });
		sql.close();
	}

	/**
	 * �޸ĺ�����������ģʽ
	 * 
	 * @param num
	 * @param mode
	 *            �µ�����ģʽ
	 */
	public void updateBlanckNumMode(String num, int mode) {
		SQLiteDatabase sql = helper.getWritableDatabase();// ���������ݿ�
		ContentValues values = new ContentValues();
		values.put("mode", mode);
		// ����
		sql.update("info", values, "blanknum=?", new String[] { num });
	}

	/**
	 * ��ѯ������������ģʽ
	 * 
	 * @param num
	 * @return -1 ����û�в�ѯ���κν�� 0 tel 1 sms 2 all
	 */
	public int queryMode(String num) {
		int mode = -1;
		SQLiteDatabase sql = helper.getReadableDatabase();
		// 1���� 2Ҫ��ѯ���� 3 ��ѯ���� 4 ������Ӧ����
		Cursor c = sql.query("info", new String[] { "mode" }, "blanknum=?",
				new String[] { num }, null, null, null);
		if (c.moveToNext()) {
			mode = c.getInt(0);

		}
		c.close();
		sql.close();
		return mode;

	}

	/**
	 * ����ȫ������������
	 * 
	 * @return
	 */
	public List<BlankNumInfo> queryAll() {
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<BlankNumInfo> blankNumInfos = new ArrayList<BlankNumInfo>();
		SQLiteDatabase sql = helper.getReadableDatabase();
		Cursor cursor = sql.query("info", new String[] { "blanknum", "mode" },
				null, null, null, null, "id desc");
		while (cursor.moveToNext()) {
			String num = cursor.getString(0);
			int mode = cursor.getInt(1);
			BlankNumInfo blankNumInfo = new BlankNumInfo(num, mode);
			blankNumInfos.add(blankNumInfo);
		}
		cursor.close();
		sql.close();
		return blankNumInfos;

	}

	/**
	 * ���ز��ֺ���������
	 * 
	 * @return
	 */
	public List<BlankNumInfo> queryPart(int maxNum, int startIndex) {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<BlankNumInfo> blankNumInfos = new ArrayList<BlankNumInfo>();
		SQLiteDatabase sql = helper.getReadableDatabase();
		Cursor cursor = sql.rawQuery(
				"select blanknum,mode from info order by id desc limit ? offset ?;",
				new String[] { maxNum+"", startIndex+"" });
		while (cursor.moveToNext()) {
			String num = cursor.getString(0);
			int mode = cursor.getInt(1);
			BlankNumInfo blankNumInfo = new BlankNumInfo(num, mode);
			blankNumInfos.add(blankNumInfo);
		}
		cursor.close();
		sql.close();
		return blankNumInfos;

	}
}
