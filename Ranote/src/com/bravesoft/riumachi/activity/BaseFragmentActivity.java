package com.bravesoft.riumachi.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.bravesoft.riumachi.appliction.App;
import com.bravesoft.riumachi.constant.SharePrefrenceKey;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.layout.LayoutUtils;
import com.bravesoft.riumachi.layout.ScreenConfig;
import com.bravesoft.riumachi.util.AppManager;
import com.bravesoft.riumachi.util.SharePrefUtil;
import com.bravesoft.riumachi.widget.CustomProgressDialog;
import com.bravesoft.riumachi.widget.CustomProgressDialog.DialogCancelListener;

public class BaseFragmentActivity extends FragmentActivity implements DialogCancelListener {

	public App mApp = App.getInstance(); // App instance
    protected CustomProgressDialog mProgressDialog; //loading dialog
    private AppManager mAppManager; //activity manager
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mAppManager = AppManager.getAppManager();
		mAppManager.addActivity(this);
		mProgressDialog = new CustomProgressDialog(this);
		mProgressDialog.setOnCancelListener(this);
		super.onCreate(savedInstanceState);
	}

	protected <T extends View> T getRateView(int id, boolean isMin) {
		View childView = findViewById(id);
		if (!ScreenConfig.INITBAR) {
			if (!isMin) {
				ScreenConfig.addView(childView);
			} else {
				rateView(childView, isMin);
			}
			ScreenConfig.initBar(this, childView);
		} else {
			rateView(childView, isMin);
		}
		return (T) childView;
	}

	protected <T extends View> T getTextView(int id, boolean isMin,
			float textSize) {
		View childView = getRateView(id, isMin);
		if (childView instanceof TextView) {
			TextView tv = (TextView) childView;
			LayoutUtils.setTextSize(tv, textSize);
		}
		return (T) childView;
	}
	
	 
		protected <T extends View> T getTextView(int id, boolean isMin,
				float textSize ,int typeFace) {
			View childView = getRateView(id, isMin);
			if (childView instanceof TextView) {
				TextView tv = (TextView) childView;
				LayoutUtils.setTextSize(tv, textSize);
				switch (typeFace) {
				case TextTypeFace.TYPEFACE_ROBOTO_REGULAR:
					tv.setTypeface(mApp.getmTypefacRobotoRegular());
					break;
				case TextTypeFace.TYPEFACE_ROBOTO_BOLD:
					tv.setTypeface(mApp.getmTypefacRobotoBold());
					break;
				case TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR:
					tv.setTypeface(mApp.getmTypefacGenShinGothicRegular());
					break;
				case TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD:
					tv.setTypeface(mApp.getmTypeGenShinGothicBold());
					break;

				default:
					break;
				}
			}
			return (T) childView;
		}
		
	
	protected void rateView(View v, boolean isMin) {
		LayoutUtils.rateScale(this, v, isMin);
	}

	/**
	 * @return current App mode.
	 */
	protected int getAppMode(){
		return SharePrefUtil.getInt(getApplicationContext(), SharePrefrenceKey.APP_MODE, App.NORMAL_MODE);
	}
	
	/**
	 * set App mode
	 * @param the mode's constant been defined in 'App'.
	 */
	protected void setAppMode(int mode){
		SharePrefUtil.putInt(getApplicationContext(),SharePrefrenceKey.APP_MODE, mode);
	}
	
	@Override
	public void onDialogCancel() {
		
	}
	
	/**
	 * when you what finish some activity,please use this method.
	 */
	protected void finishActivity(){
		mAppManager.finishActivity(this);
	}

}
