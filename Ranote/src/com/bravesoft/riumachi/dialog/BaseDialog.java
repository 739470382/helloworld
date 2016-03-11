package com.bravesoft.riumachi.dialog;

import com.bravesoft.riumachi.appliction.App;
import com.bravesoft.riumachi.constant.SharePrefrenceKey;
import com.bravesoft.riumachi.util.SharePrefUtil;

import android.app.Dialog;
import android.content.Context;

public class BaseDialog extends Dialog {
	
	private Context context;
	
	public App mApp = App.getInstance();

	public BaseDialog(Context context) {
		super(context);
		this.context = context;
	}

	public BaseDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	protected BaseDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.context = context;
	}
	
	protected int getAppMode(){
		return SharePrefUtil.getInt(context, SharePrefrenceKey.APP_MODE, App.NORMAL_MODE);
	}
	
	protected void setAppMode(int mode){
		SharePrefUtil.putInt(context,SharePrefrenceKey.APP_MODE, mode);
	}

}