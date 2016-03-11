package com.bravesoft.riumachi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.bravesoft.riumachi.activity.MainActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;

/**
 * Activity  Management
 * 
 * @author wanglin
 * 
 */
public class AppManager {
	private static Stack<Activity> activityStack;
	private static AppManager instance;

	private AppManager() {
	}

	/**
	 * Single Instance
	 */
	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	/**
	 * Activity added to the stack
	 */
	public void addActivity(Activity activity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 *Get Current Activity (stack pushed last)
	 */
	public Activity currentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * End the current Activity (stack pushed last)
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	/**
	 * End specified Activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}
	
	/**
	 * Keep the current activity, the end has been pushed onto the stack
	 */
	
	public void finishExceptThis(){
		if (activityStack.size()>2) {
			for (int i = 0; i <activityStack.size()-1; i++) {
				finishActivity(activityStack.get(i));
			}
		}
	}

	/**
	 * End of the specified class name Activity
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * Until the end of the specified class name Activity
	 */
	public void finishToActivity(Class<?> cls) {
		List<Activity> list = new ArrayList<Activity>();
		for (int i = activityStack.size(); i > 0; i--) {
			Activity activity = activityStack.get(i - 1);
			if (activity.getClass().equals(cls)) {
				break;
			} else {
				activity.finish();
				list.add(activity);
			}
		}
		for (Activity activity : list) {
			activityStack.remove(activity);
		}
	}

	/**
	 * End All Activity
	 */
	public void finishAllActivity() {
		for (int i =activityStack.size()-1; i >=0; i--) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 
	 * Quit the application
	 */

	public void AppExit(Context context) {

		try {

			finishAllActivity();

			ActivityManager activityMgr = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);

			activityMgr.killBackgroundProcesses(context.getPackageName());

		} catch (Exception e) {
		}

	}
	
	/**
	 * Whether it is the first time (according to the current version number to determine if the current version number corresponding to 
	 * the number of starts is greater than 1, it is not the first start, after the easy version update judgment)
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isFirstStart(Context context) {
		try {
			int versinCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
			int lastStartCount = readLastStartCount(context, versinCode);
			if (lastStartCount <= 0) {
				saveStartCount(context, versinCode, 1);
				return true;
			} else {
				saveStartCount(context, versinCode, ++lastStartCount);
				return false;
			}
		} catch (NameNotFoundException e) {
			LogUtil.e(e.toString());
			return false;
		}
	}

	private static int readLastStartCount(Context context, int versinCode) {
		return PreferenceManager.getDefaultSharedPreferences(context).getInt(
				String.valueOf(versinCode), 0);
	}
	
	private static void saveStartCount(Context context, int versinCode,
			int count) {
		PreferenceManager.getDefaultSharedPreferences(context).edit()
				.putInt(String.valueOf(versinCode), count).commit();
	}
	
	
	/**
	 * finish all activity and reopen mainactivity
	 */
	public void finishAllAndReopenMainActivity(Activity thisActivity ,Class<?> cls) {
		List<Activity> list = new ArrayList<Activity>();
		for (int i = activityStack.size(); i > 0; i--) {
			Activity activity = activityStack.get(i - 1);
			if (activity.getClass().equals(cls.getClass())) {
				break;
			} else {
				activity.finish();
				list.add(activity);
			}
		}
		for (Activity activity : list) {
			activityStack.remove(activity);
		}
		Intent intent = new Intent(thisActivity , MainActivity.class);
		thisActivity.startActivity(intent);
		finishActivity(thisActivity);
	}
}
