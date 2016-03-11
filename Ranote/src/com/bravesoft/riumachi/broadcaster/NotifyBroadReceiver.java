package com.bravesoft.riumachi.broadcaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bravesoft.riumachi.service.NotifyService;

public class NotifyBroadReceiver extends BroadcastReceiver {
	private  static final String ACTION = "android.intent.action.BOOT_COMPLETED";   

	@Override
	public void onReceive(Context context, Intent intent) {
		 if (intent.getAction().equals(ACTION))    
	        {   
	                  context.startService(new Intent(context,NotifyService.class));
	        }   
	}

}
