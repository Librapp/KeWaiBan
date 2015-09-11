package com.lims.kewaiban.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * SharedPreferences的工具类
 * 
 * @author wangli
 *
 */
public class SPUtil {

	private final SharedPreferences sp;
	private Editor edit;
	private static SPUtil spUtil;

	private SPUtil(Context context) {
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
	}

	public static SPUtil getInstance(Context context) {
		if (spUtil == null) {
			spUtil = new SPUtil(context);
		}
		return spUtil;

	}

	public void putString(String key, String value) {
		edit = sp.edit();
		edit.putString(key, value);
		edit.commit();
	}

	public String getString(String key, String defaultValue) {
		return sp.getString(key, defaultValue);
	}

	public void putBoolean(String key, boolean value) {
		edit = sp.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		return sp.getBoolean(key, defaultValue);
	}

	public void remove(String... key) {
		edit = sp.edit();
		if (key != null) {
			for (int i = 0; i < key.length; i++) {
				edit.remove(key[i]);
			}
		}
		;
		edit.commit();
		;
	}

	public void putLong(String key, Long value) {
		edit = sp.edit();
		edit.putLong(key, value);
		edit.commit();
	}

	public Long getLong(String key, Long defaultValue) {
		return sp.getLong(key, defaultValue);
	}

	public void putInt(String key, int value) {
		edit = sp.edit();
		edit.putInt(key, value);
		edit.commit();
	}

	public int getInt(String key, int defaultValue) {
		return sp.getInt(key, defaultValue);
	}
}
