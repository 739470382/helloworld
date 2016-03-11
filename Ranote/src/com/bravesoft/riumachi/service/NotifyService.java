package com.bravesoft.riumachi.service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.activity.MainActivity;
import com.bravesoft.riumachi.appliction.App;
import com.bravesoft.riumachi.bean.FileUpRequestBean;
import com.bravesoft.riumachi.bean.ScheduleBean;
import com.bravesoft.riumachi.bean.SymptomImageBean;
import com.bravesoft.riumachi.bean.SymptomMemoBean;
import com.bravesoft.riumachi.broadcaster.NotifyRecevier;
import com.bravesoft.riumachi.broadcaster.RefreshDataRecevier;
import com.bravesoft.riumachi.callback.CommonCallBack;
import com.bravesoft.riumachi.constant.SDConfig;
import com.bravesoft.riumachi.constant.SerViceBroadCastAction;
import com.bravesoft.riumachi.database.DBHelper;
import com.bravesoft.riumachi.database.DBOperator;
import com.bravesoft.riumachi.database.ImageDBHelper;
import com.bravesoft.riumachi.database.ImageDBOperator;
import com.bravesoft.riumachi.http.HttpRequest;
import com.bravesoft.riumachi.util.DateFormatUtil;
import com.bravesoft.riumachi.util.LogUtil;
import com.bravesoft.riumachi.util.MyUtils;

public class NotifyService extends Service {

	private SQLiteDatabase db;
	private DBHelper mDbHelper;
	private SQLiteDatabase imagedb;
	private ImageDBHelper mImageDbHelper;
	private long time, new_time1, new_time2;
	private SimpleDateFormat sf;
	private AlarmManager alarmManager;
	private static int ALARM_SET_FLAG = 2;
	
	
	private static final int INTERVAL = 1000 * 60 * 60 * 24;
	private static int REQUEST_CODE = 1;
	private NotifyDataRecevier dataRecevier;
	private NotifyImageUploadRecevier imageUploadRecevier;
	private List<ScheduleBean> list_notify;
	private List<SymptomImageBean> list_image_upload;
	private ArrayList<Long> list_times = new ArrayList<Long>();
	private int uploadFlag;

	@Override
	public void onCreate() {
		super.onCreate();
		mDbHelper = new DBHelper(getApplicationContext());
		db = mDbHelper.getReadableDatabase();
		mImageDbHelper = new ImageDBHelper(getApplicationContext());
		imagedb = mImageDbHelper.getReadableDatabase();
		

		//register broadcastRecevier
		dataRecevier = new NotifyDataRecevier();
		IntentFilter filter = new IntentFilter();
		filter.addAction(SerViceBroadCastAction.NOTIFY_DATA_FLAG);
		registerReceiver(dataRecevier, filter);

		//register broadcastRecevier of upload imagefile
		imageUploadRecevier = new NotifyImageUploadRecevier();
		IntentFilter uploadFilter = new IntentFilter();
		uploadFilter.addAction(SerViceBroadCastAction.NOTIFY_IMAGEUPLOAD_FLAG);
		registerReceiver(imageUploadRecevier, uploadFilter);

		
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		setAlarmData();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(dataRecevier);
		unregisterReceiver(imageUploadRecevier);
		App.getInstance().setServiceMode(0);
	
		Intent intent = new Intent();
		intent.setClass(NotifyService.this, NotifyService.class);
		startService(intent);
		intent.setAction("com.bravesoft.riumachi.notifyrestart");
		NotifyService.this.sendBroadcast(intent);
	}

	/*
	 * Refresh notification information
	 */
	private void getNewNotify() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				DBHelper dbHelper = new DBHelper(NotifyService.this);
				SQLiteDatabase db = dbHelper.getReadableDatabase();
				list_notify = DBOperator
						.queryNotificationScheduleListOrderByDate(db);
				if (list_notify == null) {
					LogUtil.v("LIST", "data   is null");
				} else {
					for (int i = 0; i < list_notify.size(); i++) {

						LogUtil.v("LIST", list_notify.get(i)
								.getSchedule_start_time());
						list_times.add(Long.parseLong(list_notify.get(i)
								.getSchedule_start_time()));
					}
					getNewTime(list_times, list_notify);
				}
				db.close();
			}
		}).start();

	}

	/*
	 * Sort of time
	 */

	private void getNewTime(List<Long> list_times,
			List<ScheduleBean> list_notify) {
		long temp;
		ScheduleBean scheduleBean;
		LogUtil.v("LIST_SIZE", list_times.size() + "----" + list_notify.size());
		int size = list_times.size();
		for (int i = 0; i < size - 1; i++) {
			for (int j = i + 1; j < size; j++) {
				if (list_times.get(i).compareTo(list_times.get(j)) >= 0) {
					temp = list_times.get(i);
					scheduleBean = list_notify.get(i);
					list_times.set(i, list_times.get(j));
					list_notify.set(i, list_notify.get(j));
					list_times.set(j, temp);
					list_notify.set(j, scheduleBean);
				}
			}
		}
		for (int i = 0; i < list_notify.size(); i++) {
			long time = Long.parseLong(list_notify.get(i)
					.getSchedule_start_time());
			String change_time = DateFormatUtil.getWholeDate(time);
			String name = list_notify.get(i).getTitle();
			String type = list_notify.get(i).getType();
			LogUtil.v("NEW_LIST", change_time + "----" + name + "----" + type);
		}
		setNotification(list_notify);
	}

	/*
	 * Setting notification
	 */
	private void setNotification(List<ScheduleBean> list_notify) {
		Intent intent = new Intent(NotifyService.this, NotifyRecevier.class);
		for (int i = 0; i < list_notify.size(); i++) {
			intent.putExtra("name", list_notify.get(i).getTitle());
			intent.putExtra("type", list_notify.get(i).getType());
			if (list_notify.get(i).getType().equals("3")) {
				intent.putExtra("mtype", list_notify.get(i).getMedicalType());
			}
			PendingIntent pendingIntent = PendingIntent.getBroadcast(
					NotifyService.this, list_notify.get(i).getId(), intent,list_notify.get(i).getId());
			ALARM_SET_FLAG++;
			alarmManager.cancel(pendingIntent);
			alarmManager
					.set(AlarmManager.RTC_WAKEUP, Long.parseLong(list_notify
							.get(i).getSchedule_start_time()), pendingIntent);
		}
		list_notify.clear();
		list_times.clear();
	}

	/*
	 * Set the timer is updated daily
	 */
	private void setAlarmData() {
		Intent intent = new Intent(NotifyService.this,
				RefreshDataRecevier.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(
				NotifyService.this, REQUEST_CODE, intent,
				PendingIntent.FLAG_CANCEL_CURRENT);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 5);
		calendar.set(Calendar.MILLISECOND, 0);
		alarmManager.cancel(pendingIntent);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				calendar.getTimeInMillis(), INTERVAL, pendingIntent);
	}

	public class NotifyDataRecevier extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(SerViceBroadCastAction.NOTIFY_DATA_FLAG)) {
				if (list_notify != null) {
					list_notify.clear();
					list_times.clear();
				}
				getNewNotify();
			}
		}

	}

	//BroadcastReceiver of notify upload file of image
	public class NotifyImageUploadRecevier extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			uploadFlag = 0;
			if (intent.getAction().equals(SerViceBroadCastAction.NOTIFY_IMAGEUPLOAD_FLAG)) {
				boolean isneedupload = ImageDBOperator.isNeedUpload(imagedb);
				if (isneedupload) {
					if (App.getInstance().getmLoginUtils().isLogin()) {
						list_image_upload = ImageDBOperator
								.queryUploadImage(imagedb);
						String token = App.getInstance().getmLoginUtils()
								.getToken();
						String type = "2";
						File imageFile = new File(list_image_upload.get(0)
								.getLocalurl());
						if (imageFile.exists()) {
							if (MyUtils.isNetWork(getApplicationContext())) {
								upImageFile(token, type, imageFile);
							}
						}
					} else {
					}
				}else{
				}
			}
		}

	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	//upload file of image
	private void upImageFile(String token, String type, File file) {

		System.out.println(file.getAbsolutePath());
		HttpRequest.getInstance().upLoadImage(token, type, file,
				new CommonCallBack<FileUpRequestBean>() {

					@Override
					public void onSuccess(FileUpRequestBean data) {
						if (data != null&&data.getError().equals(200+"")) {
							// finish();
							
							if(list_image_upload.size() != 0){
							if(db == null ||!db.isOpen()){
								mDbHelper = new DBHelper(getApplicationContext());
								db = mDbHelper.getReadableDatabase();
							}
							SymptomMemoBean mySymptomMemoBean = DBOperator
									.querySymptomMemoById(db, list_image_upload
											.get(uploadFlag).getSymptomid()
											+ "");
							if(mySymptomMemoBean == null){
							}
							if(mySymptomMemoBean != null && data.getFile_url() != null &&!data.getFile_url().equals("")){
							mySymptomMemoBean.setUrl(data.getFile_url());
							boolean isSuccess = DBOperator.updateSymptomMemo(
									db, mySymptomMemoBean);
							if(isSuccess){
							list_image_upload
							.get(uploadFlag).setIsupload(1);
							ImageDBOperator.updateImageRecordBySymptomImageBean(imagedb, list_image_upload
							.get(uploadFlag));
							}
							}
							uploadFlag++;
							if (uploadFlag < list_image_upload.size()) {
								String token = App.getInstance()
										.getmLoginUtils().getToken();
								String type = "2";
								File imageFile = new File(list_image_upload
										.get(uploadFlag).getLocalurl());
								if (imageFile.exists()) {
									if (MyUtils
											.isNetWork(getApplicationContext())) {
										upImageFile(token, type, imageFile);
									}
								}

							}else{
							}

							if (App.isfirstcome) {
								String token = App.getInstance()
										.getmLoginUtils().getToken();
								String type = "1";
								System.out.println(SDConfig.DB_PATH);
								File dbFile = new File(SDConfig.DB_PATH);
								if (MyUtils.isNetWork(getApplicationContext())) {
									upDbFile(token, type, dbFile);
								}
							}
						}
						}else if (data != null&&data.getError().equals(425+"")){
							App.getInstance().getmLoginUtils().logout();
						}
						// HttpRequest.getInstance().upLoadImage(token,type,file,this);
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onFailed(Throwable throwable, String reason) {

						// Log.d("FAILED", throwable+"----"+reason);
					}
				});
	}

	//upload dbfile
	private void upDbFile(String token, String type, File file) {
		HttpRequest.getInstance().UpDb(token, type, file,
				new CommonCallBack<FileUpRequestBean>() {

					@Override
					public void onSuccess(FileUpRequestBean data) {

						if (data != null) {
							Long date = System.currentTimeMillis();
							Long day = DateFormatUtil.geDataLongZeroDay(date);
							String dayString = getApplication().getResources()
									.getString(R.string.update_date_pre)
									+ DateFormatUtil.getStringDataByLong(date);
							App.getInstance().setUpdateTime(dayString);
						} else {
							
						}
						App.isfirstcome = false;
						Intent intent = new Intent();
						intent.setAction("com.bravesoft.notifydialogcancle");
						NotifyService.this.sendBroadcast(intent);
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onFailed(Throwable throwable, String reason) {

						App.isfirstcome = false;
						Intent intent = new Intent();
						intent.setAction("com.bravesoft.notifydialogcancle");
						NotifyService.this.sendBroadcast(intent);
						// Log.d("FAILED", throwable+"----"+reason);
					}
				});
	}

}
