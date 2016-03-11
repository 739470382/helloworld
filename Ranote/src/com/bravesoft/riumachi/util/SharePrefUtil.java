package com.bravesoft.riumachi.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Local SharePreference storage tools
 * 
 * @author ZhouPeng
 *
 */
public class SharePrefUtil {

	private static final String DEVICE_MANAGE = "device_manage";

	private static SharedPreferences getSharedPreferences(Context context) {
		SharedPreferences settings = context.getSharedPreferences(
				DEVICE_MANAGE, Context.MODE_PRIVATE);
		return settings;
	}

	public static synchronized boolean putString(Context context, String key,
			String value) {
		SharedPreferences.Editor editor = getSharedPreferences(context).edit();
		editor.putString(key, value);
		return editor.commit();
	}

	public static synchronized boolean putBoolean(Context context, String key,
			boolean value) {
		SharedPreferences.Editor editor = getSharedPreferences(context).edit();
		editor.putBoolean(key, value);
		return editor.commit();

	}

	public static synchronized boolean putLong(Context context, String key,
			long value) {
		SharedPreferences.Editor editor = getSharedPreferences(context).edit();
		editor.putLong(key, value);
		return editor.commit();
	}

	public static synchronized boolean putInt(Context context, String key,
			int value) {

		SharedPreferences.Editor editor = getSharedPreferences(context).edit();
		editor.putInt(key, value);
		return editor.commit();
	}

	public static synchronized boolean putFloat(Context context, String key,
			float value) {
		SharedPreferences.Editor editor = getSharedPreferences(context).edit();
		editor.putFloat(key, value);
		return editor.commit();

	}

	public static synchronized String getString(Context context, String key,
			String defValue) {
		String defValues = getSharedPreferences(context).getString(key,
				defValue);
		return defValues;
	}

	public static synchronized boolean getBoolean(Context context, String key,
			boolean defValue) {
		return getSharedPreferences(context).getBoolean(key, defValue);
	}

	public static synchronized int getInt(Context context, String key,
			Integer defValue) {
		Integer result = getSharedPreferences(context).getInt(key, defValue);
		return result;
	}

	public static synchronized float getFloat(Context context, String key,
			float defValue) {
		return getSharedPreferences(context).getFloat(key, defValue);
	}

	public static synchronized long getLong(Context context, String key,
			long defValue) {
		return getSharedPreferences(context).getLong(key, defValue);
	}

	public static synchronized void clearValueByKey(Context context, String key) {
		SharedPreferences.Editor editor = getSharedPreferences(context).edit();
		editor.remove(key);
		editor.commit();
	}

	public static void clearAll(Context context) {
		getSharedPreferences(context).edit().clear().commit();;
	}
}
