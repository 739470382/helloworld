package com.bravesoft.riumachi.broadcaster;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.activity.MainActivity;

public class NotifyRecevier extends BroadcastReceiver {
	private static int NOTIFICATION_FLAG=1;
	private int image_type;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String name=intent.getExtras().getString("name");
		String type=intent.getExtras().getString("type");
		String medcine_type;
//		if (type.equals("3")) {
//			medcine_type=intent.getExtras().getString("mtype");
//			if (medcine_type.equals("1")) {
//				image_type=R.drawable.icon_medicine_a;
//			}else if (medcine_type.equals("2")) {
//				image_type=R.drawable.icon_medicine_b;
//			}else if(medcine_type.equals("3")){
//				image_type=R.drawable.icon_medicine_c;
//			}
//		}else if (type.equals("1")) {
//			image_type=R.drawable.icon_calendar;
//		}else if (type.equals("2")) {
//			image_type=R.drawable.icon_see_the_docter;
//		}
		//Get system notification service
		NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		@SuppressWarnings("deprecation")
		//Adding prompt notification bar
//		Notification notification = new Notification(
//				R.drawable.ic_notify, name,
//				System.currentTimeMillis());
		Bitmap bitmap=BitmapFactory.decodeResource(null, R.drawable.icon_notification);
		Notification notification=new Notification.Builder(context)
							.setSmallIcon(R.drawable.ic_notify)
							.setLargeIcon(bitmap)
							.setContentTitle(context.getString(R.string.notification_title_name))
							.setContentText(name)
							.getNotification();
		//Message Clear mode FLAG_AUTO_CANCEL
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		//Message tone
		notification.defaults=Notification.DEFAULT_SOUND;
		//Add a custom layout
//		RemoteViews remoteViews=new RemoteViews(context.getPackageName(), R.layout.itme_notification);
//		remoteViews.setImageViewResource(R.id.notification_image, R.drawable.ic_launcher);
//		remoteViews.setTextViewText(R.id.notification_text,name);
//		notification.contentView=remoteViews;
		//Add to adjust intent
		Intent intent1 = new Intent(context,MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, NOTIFICATION_FLAG, intent1,NOTIFICATION_FLAG);
		notification.contentIntent = contentIntent;
		manager.notify(NOTIFICATION_FLAG, notification);
		NOTIFICATION_FLAG++;
	}

}
