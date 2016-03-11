package com.bravesoft.riumachi.broadcaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bravesoft.riumachi.util.LogUtil;

public class RefreshDataRecevier extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Long time=System.currentTimeMillis();
		 
		Intent intent2=new Intent();
		intent2.setAction("com.bravesoft.notifydata");
		context.sendBroadcast(intent2);
	}

}
