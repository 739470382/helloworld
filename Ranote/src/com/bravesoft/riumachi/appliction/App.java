package com.bravesoft.riumachi.appliction;

import java.util.ArrayList;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Application;
import android.content.Intent;
import android.graphics.Typeface;

import com.bravesoft.riumachi.constant.SharePrefrenceKey;
import com.bravesoft.riumachi.service.NotifyService;
import com.bravesoft.riumachi.util.LoginUtils;
import com.bravesoft.riumachi.util.SharePrefUtil;

public class App extends Application {

	private static App sInstance = null;
	public static boolean isfirstcome = true;
	public static boolean upLoadDbCastIsRegist = false;
	public static final int NORMAL_MODE = 100;
	public static final int SEE_THE_DOCTER_MODE = 200;
	public static int DIALOG_ADD_EVENT_STATE_KEEP_VISIBLE = 2;// setting
	public static int DIALOG_ADD_EVENT_STATE_KEEP_GONE = 1;
	private boolean setDialogAddEventDismiss = false;
	private boolean isShowMyKarteFragment = false;
	private boolean isShowCarlendarFragment = false;
	private Typeface mTypefacRobotoRegular;
	private Typeface mTypefacRobotoBold;
	private Typeface mTypefacGenShinGothicRegular;
	private Typeface mTypeGenShinGothicBold;
	private long mCurrentDateLong;
	private LoginUtils mLoginUtils;

	public App() {
		sInstance = this;
	}

	@Override
	public void onTrimMemory(int level) {
	System.out.println("****************************");
		super.onTrimMemory(level);
	}

	/**
	 * get App Instance 这是一种错误的写法 这就属于application跟单例混合使用
	 * Application是属于系统组件，系统组件的实例是要由系统来去创建的，
	 * 如果这里我们自己去new一个MyApplication的实例，它就只是一个普通的Java对象而已，而不具备任何Context的能力。 正确写法
	 * private static MyApplication app;
	 * 
	 * public static MyApplication getInstance() { return app; }
	 * 
	 * @Override public void onCreate() { super.onCreate(); app = this; }
	 */
	public static App getInstance() {
		if (sInstance == null) {
			sInstance = new App();
		}
		return sInstance;
	}

	@Override
	public void onCreate() {
		mTypefacRobotoRegular = Typeface.createFromAsset(getAssets(),
				"fonts/Roboto-Regular.ttf");
		mTypefacRobotoBold = Typeface.createFromAsset(getAssets(),
				"fonts/Roboto-Bold.ttf");
		mTypefacGenShinGothicRegular = Typeface.createFromAsset(getAssets(),
				"fonts/GenShinGothic-Regular.ttf");
		mTypeGenShinGothicBold = Typeface.createFromAsset(getAssets(),
				"fonts/GenShinGothic-Bold.ttf");
		mLoginUtils = new LoginUtils(getApplicationContext());
		if (isWorked() == true) {
			// upLoadDbCastIsRegist = true;
		} else {
			// upLoadDbCastIsRegist = true;
			Intent intent = new Intent(getApplicationContext(),
					NotifyService.class);
			startService(intent);
		}
		super.onCreate();
	}

	public static boolean isWorked() {
		ActivityManager myManager = (ActivityManager) sInstance
				.getSystemService(Application.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(30);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString()
					.equals("com.bravesoft.riumachi.service.NotifyService")) {
				upLoadDbCastIsRegist = false;
				return true;
			}
		}
		upLoadDbCastIsRegist = false;
		return false;
	}

	public int getAppMode() {
		return SharePrefUtil.getInt(getApplicationContext(),
				SharePrefrenceKey.APP_MODE, App.NORMAL_MODE);
	}

	public void setAppMode(int mode) {
		SharePrefUtil.putInt(getApplicationContext(),
				SharePrefrenceKey.APP_MODE, mode);
	}

	public int getServiceMode() {
		return SharePrefUtil.getInt(getApplicationContext(),
				SharePrefrenceKey.SERVIUCE_MODE, 22);
	}

	public void setServiceMode(int mode) {
		SharePrefUtil.putInt(getApplicationContext(),
				SharePrefrenceKey.SERVIUCE_MODE, mode);
	}

	public String getTemFileUri() {
		return SharePrefUtil.getString(getApplicationContext(),
				SharePrefrenceKey.MEMO_TEMFILE, null);
	}

	public void setTemFileUri(String uri) {
		SharePrefUtil.putString(getApplicationContext(),
				SharePrefrenceKey.MEMO_TEMFILE, uri);
	}

	public boolean getIsRegister() {
		return SharePrefUtil.getBoolean(getApplicationContext(),
				SharePrefrenceKey.IS_REGISTER, false);
	}

	public void setIsRegister(boolean isregister) {
		SharePrefUtil.putBoolean(getApplicationContext(),
				SharePrefrenceKey.IS_REGISTER, isregister);
	}

	public String getUpdateTime() {
		return SharePrefUtil.getString(getApplicationContext(),
				SharePrefrenceKey.UPDATE_TIME, null);
	}

	public void setUpdateTime(String updatetime) {
		SharePrefUtil.putString(getApplicationContext(),
				SharePrefrenceKey.UPDATE_TIME, updatetime);
	}

	public boolean isGuideDialogNeedShow() {
		return SharePrefUtil.getBoolean(getApplicationContext(),
				SharePrefrenceKey.NEED_GUIDE, true);
	}

	public void setGuideDialogNeedShow(boolean isShow) {
		SharePrefUtil.putBoolean(getApplicationContext(),
				SharePrefrenceKey.NEED_GUIDE, isShow);
	}

	public boolean isSetDialogAddEventDismiss() {
		return setDialogAddEventDismiss;
	}

	public void SetDialogAddEventDismiss(boolean setDialogAddEventDismiss) {
		this.setDialogAddEventDismiss = setDialogAddEventDismiss;
	}

	public boolean isShowMyKarteFragment() {
		return isShowMyKarteFragment;
	}

	public void setShowMyKarteFragment(boolean isShowMyKarteFragment) {
		this.isShowMyKarteFragment = isShowMyKarteFragment;
	}

	public boolean isShowCarlendarFragment() {
		return isShowCarlendarFragment;
	}

	public void setShowCarlendarFragment(boolean isShowCarlendarFragment) {
		this.isShowCarlendarFragment = isShowCarlendarFragment;
	}

	public Typeface getmTypefacRobotoRegular() {
		return mTypefacRobotoRegular;
	}

	public void setmTypefacRobotoRegular(Typeface mTypefacRobotoRegular) {
		this.mTypefacRobotoRegular = mTypefacRobotoRegular;
	}

	public Typeface getmTypefacRobotoBold() {
		return mTypefacRobotoBold;
	}

	public void setmTypefacRobotoBold(Typeface mTypefacRobotoBold) {
		this.mTypefacRobotoBold = mTypefacRobotoBold;
	}

	public Typeface getmTypefacGenShinGothicRegular() {
		return mTypefacGenShinGothicRegular;
	}

	public void setmTypefacGenShinGothicRegular(
			Typeface mTypefacGenShinGothicRegular) {
		this.mTypefacGenShinGothicRegular = mTypefacGenShinGothicRegular;
	}

	public Typeface getmTypeGenShinGothicBold() {
		return mTypeGenShinGothicBold;
	}

	public void setmTypeGenShinGothicBold(Typeface mTypeGenShinGothicBold) {
		this.mTypeGenShinGothicBold = mTypeGenShinGothicBold;
	}

	public long getmCurrentDateLong() {
		return mCurrentDateLong;
	}

	public void setmCurrentDateLong(long mCurrentDateLong) {
		this.mCurrentDateLong = mCurrentDateLong;
	}

	public LoginUtils getmLoginUtils() {
		return mLoginUtils;
	}

}
