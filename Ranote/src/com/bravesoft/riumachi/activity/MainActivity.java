package com.bravesoft.riumachi.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.appliction.App;
import com.bravesoft.riumachi.bean.FileUpRequestBean;
import com.bravesoft.riumachi.bean.SymptomImageBean;
import com.bravesoft.riumachi.bean.SymptomMemoBean;
import com.bravesoft.riumachi.callback.CommonCallBack;
import com.bravesoft.riumachi.constant.MainFragmentTag;
import com.bravesoft.riumachi.constant.SDConfig;
import com.bravesoft.riumachi.constant.SerViceBroadCastAction;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.database.DBHelper;
import com.bravesoft.riumachi.database.DBOperator;
import com.bravesoft.riumachi.database.ImageDBHelper;
import com.bravesoft.riumachi.database.ImageDBOperator;
import com.bravesoft.riumachi.dialog.CreateNewEventDialog;
import com.bravesoft.riumachi.dialog.CreateNewEventDialog.OnNewEventDialogClickListener;
import com.bravesoft.riumachi.dialog.GeneralStringDialog;
import com.bravesoft.riumachi.dialog.GeneralStringDialog.OnGenenalStringDialogClickListener;
import com.bravesoft.riumachi.dialog.ModeGuideDialog;
import com.bravesoft.riumachi.dialog.ModeGuideDialog.OnModeGuideDialogClickListener;
import com.bravesoft.riumachi.dialog.SettingDialog;
import com.bravesoft.riumachi.dialog.SettingDialog.OnSettingDialogListener;
import com.bravesoft.riumachi.fragment.CalendarFragment;
import com.bravesoft.riumachi.fragment.CalendarFragment.CalendarFragemntListener;
import com.bravesoft.riumachi.fragment.RecordFragment;
import com.bravesoft.riumachi.fragment.RecordFragment.RecordFragmentListener;
import com.bravesoft.riumachi.http.HttpRequest;
import com.bravesoft.riumachi.service.NotifyService;
import com.bravesoft.riumachi.service.NotifyService.NotifyDataRecevier;

import com.bravesoft.riumachi.util.AppManager;
import com.bravesoft.riumachi.util.DateFormatUtil;
import com.bravesoft.riumachi.util.MyUtils;
import com.bravesoft.riumachi.widget.CustomProgressDialog;

public class MainActivity extends BaseFragmentActivity implements
OnClickListener, MainFragmentTag {


	private static final String TAG = "MainActivity";
	private SparseArray<Fragment> mMainFragmentMap;
	private CalendarFragment mCalendarFragment;
	private RecordFragment mrRecordFragment;
	private Fragment mRecentlyFragment;
	private RadioButton mImgCalendar;
	private RadioButton mImgIndagation;
	private ImageView mImgSetting;
	private ImageView mImageToday;
	private ImageView mImgPlus;
	private ImageView mTxtModeTip;
	private TextView mTxtDate;
	private TextView mTxtTabTitle;
	private View mRelativeTitleText;
	private SettingDialog mSettingDialog;
	private CreateNewEventDialog mCreateNewEventDialog;
	private GeneralStringDialog mSeeToDocterModeDialog;
	private int currentMode;
	private boolean isFirstIn = true;
	private boolean isFristClickIndagationFragment = true;
	private SQLiteDatabase db;
	private DBHelper mDbHelper;
	private SQLiteDatabase imagedb;
	private ImageDBHelper mImageDbHelper;
	private ModeGuideDialog mModeGuideDialog;
	private NotifyCancelDialogRecevier cancelDialogRecevier;
	private NotifyServiceOpenRecevier serviceOpenRecervier;
	private NotifyAppModeChangedRecevier mAppModeChangedRecervier;

	private List<SymptomImageBean> list_image_upload;
	private int uploadFlag = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);

		System.out.println("isLogin=="+mApp.getmLoginUtils().isLogin());
		System.out.println("token=="+mApp.getmLoginUtils().getToken());
		mDbHelper = new DBHelper(getApplicationContext());
		db = mDbHelper.getReadableDatabase();
		mImageDbHelper = new ImageDBHelper(getApplicationContext());
		imagedb = mImageDbHelper.getReadableDatabase();
		super.onCreate(savedInstanceState);
		currentMode = getAppMode();
		if (mSettingDialog == null) {
			mSettingDialog = new SettingDialog(this,
					new MySettingDialogListener());
		}
		if (mCreateNewEventDialog == null) {
			mCreateNewEventDialog = new CreateNewEventDialog(this,
					new OnNewEventDialogClickListener() {

				@Override
				public void onNewEventDialogClick(int position) {

				}
			});
		}

		initView(savedInstanceState);
		if(App.isfirstcome){//Whether the judgment is first opened
			String token = App.getInstance().getmLoginUtils().getToken();
			if(token != null){//whether user is Online

				File dbFile = new File(SDConfig.DB_PATH);
				if (MyUtils.isNetWork(getApplicationContext())&&dbFile.exists()) {//whether net of device is open
					//and database file is exist
					mProgressDialog.showDialog();
					uploadDB();
				}
			}else{
				App.isfirstcome = false;
			}
		}

		mAppModeChangedRecervier = new NotifyAppModeChangedRecevier();
		IntentFilter filter = new IntentFilter();
		filter.addAction(SerViceBroadCastAction.NOTIFY_APP_MODE_CHANGED);
		registerReceiver(mAppModeChangedRecervier, filter);

	}

	private void initView(Bundle savedInstanceState) {

		/*
		 * When the activity is recovered re-created, remove the existing
		 * fragment, to avoid re-creating a new activity resulting fragment was
		 * recovered after overlapping
		 */
		if (savedInstanceState != null) {
			//
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			for (int i = 0; i < FRAGMENT_TAG_ARRAY.length; i++) {
				Fragment fragment = getSupportFragmentManager()
						.findFragmentByTag(FRAGMENT_TAG_ARRAY[i]);
				if (fragment != null) {
					transaction.remove(fragment);
				}
			}
			transaction.commit();
		}

		/* Gets the control and conduct resolution adaptation */
		getRateView(R.id.framelayout_main, true);
		getRateView(R.id.relative_title_main, true);
		mTxtModeTip = getRateView(R.id.img_mode_tip, true);
		mTxtTabTitle = getTextView(R.id.txt_tab_name, true, 39,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		mImgCalendar = (RadioButton) findViewById(R.id.img_main_calendar);
		mImgIndagation = (RadioButton) findViewById(R.id.img_main_record);
		mRelativeTitleText = findViewById(R.id.relative_title_main_today);
		mTxtDate = getTextView(R.id.txt_date_yymm, true, 37,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mImgPlus = getRateView(R.id.img_main_activity_add, true);
		mImgSetting = getRateView(R.id.img_main_setting, true);
		mImageToday = (ImageView) findViewById(R.id.img_today);
		getRateView(R.id.view_today, true).setOnClickListener(this);

		checkAppModeChange();
		mImgCalendar.setChecked(true);

		mImgSetting.setOnClickListener(this);
		mImgCalendar.setOnClickListener(this);
		mImgIndagation.setOnClickListener(this);
		mImgPlus.setOnClickListener(this);
		mImageToday.setOnClickListener(this);
		//mTxtModeTip.setOnClickListener(new onModeTipClickListener());

		mImgCalendar.setTag(CALENDAR_FRAGMENT);
		mImgIndagation.setTag(INDAGATION_FRAGMENT);

		mCalendarFragment = new CalendarFragment(
				new MyCalendarFragemntListener());
		mrRecordFragment = new RecordFragment(new MyRecordFragmentListener());
		mMainFragmentMap = new SparseArray<Fragment>();
		mMainFragmentMap.put(R.id.img_main_calendar, mCalendarFragment);
		mMainFragmentMap.put(R.id.img_main_record, mrRecordFragment);

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.add(R.id.framelayout_main, mrRecordFragment, mImgIndagation
				.getTag().toString());
		transaction.add(R.id.framelayout_main, mCalendarFragment, mImgCalendar
				.getTag().toString());
		transaction.commit();
		mRecentlyFragment = mCalendarFragment;

		if (mApp.isGuideDialogNeedShow()) {
			mApp.setGuideDialogNeedShow(false);
			if (mModeGuideDialog == null) {
				mModeGuideDialog = new ModeGuideDialog(this,
						new OnModeGuideDialogClickListener() {

					@Override
					public void OncancelTextClicked() {
						mModeGuideDialog.dismiss();
					}
				});
			}
			mModeGuideDialog.show();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		db.close();
		imagedb.close();
		if (mAppModeChangedRecervier != null) {
			unregisterReceiver(mAppModeChangedRecervier);
		}
		if (cancelDialogRecevier != null) {
			unregisterReceiver(cancelDialogRecevier);
		}
		if (serviceOpenRecervier != null) {
			unregisterReceiver(serviceOpenRecervier);
		}

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.img_main_calendar:
			showCalendarTab();
			mRelativeTitleText.setVisibility(View.VISIBLE);
			mTxtTabTitle.setVisibility(View.GONE);
			mImgCalendar.setChecked(true);
			mImgIndagation.setChecked(false);
			break;
		case R.id.img_main_record:
			if (isFristClickIndagationFragment) {
				intentToMyKarteFragemnt();
			} else {
				showIndagationTab();
				mImgCalendar.setChecked(false);
				mImgIndagation.setChecked(true);
			}
			break;

		case R.id.img_main_setting:
			if (mSettingDialog == null) {
				mSettingDialog = new SettingDialog(this,
						new MySettingDialogListener());
			}
			mSettingDialog.show();
			break;

		case R.id.img_main_activity_add:
			if (mCreateNewEventDialog == null) {
				mCreateNewEventDialog = new CreateNewEventDialog(this,
						new OnNewEventDialogClickListener() {

					@Override
					public void onNewEventDialogClick(int position) {
						finish();
					}
				});
			}
			mCreateNewEventDialog.show();
			break;
		case R.id.img_today:
		case R.id.view_today:
			if (mRecentlyFragment != null) {
				if (mRecentlyFragment.getTag() == CALENDAR_FRAGMENT) {
					((CalendarFragment) mRecentlyFragment).onTodayButtonClick();
				}
			}
			break;

		default:
			break;
		}
	}

	private class onModeTipClickListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			if (mSeeToDocterModeDialog == null) {
				mSeeToDocterModeDialog = new GeneralStringDialog(
						MainActivity.this,
						getString(R.string.dialog_title_off_see_to_docter));
				mSeeToDocterModeDialog
				.setOnGenenalDialogClickListener(new OnGenenalStringDialogClickListener() {

					@Override
					public void OnGeneralSureTextClicked() {

						setAppMode(App.NORMAL_MODE);
						checkAppModeChange();
						if (mRecentlyFragment != null
								&& mRecentlyFragment.getTag().equals(
										CALENDAR_FRAGMENT)) {
							((CalendarFragment) mRecentlyFragment)
							.onResume();
						}
						mSeeToDocterModeDialog.dismiss();
					}

					@Override
					public void OnGeneralCancelTextClicked() {
						mSeeToDocterModeDialog.dismiss();
					}
				});
			}
			mSeeToDocterModeDialog.show();
		}

	}

	/*
	 * CalendarFragemnt
	 */
	private void showCalendarTab() {
		replaceFragment(mMainFragmentMap.get(mImgCalendar.getId()),
				mImgCalendar.getTag().toString());
	}

	/*
	 * indagationFragemnt
	 */
	private void showIndagationTab() {
		replaceFragment(mMainFragmentMap.get(mImgIndagation.getId()),
				mImgIndagation.getTag().toString());
	}

	/*
	 * set fragment
	 */
	public void replaceFragment(final Fragment fragment, String tag) {
		int count = getSupportFragmentManager().getBackStackEntryCount();
		for (int i = 0; i < count; i++) {
			getSupportFragmentManager().popBackStack();
		}
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();

		System.out.println("main fragment is add --->" + fragment.isAdded());

		if (fragment.isAdded()) {
			if (mRecentlyFragment != fragment && mRecentlyFragment != null) {
				transaction.hide(mRecentlyFragment);
				mRecentlyFragment = fragment;
				transaction.show(fragment);
			}
		} else {
			if (mRecentlyFragment != null) {
				transaction.hide(mRecentlyFragment);
			}
			transaction.add(R.id.framelayout_main, fragment, tag);
			mRecentlyFragment = fragment;
		}
		checkFragmentState();
		transaction.commitAllowingStateLoss();
	}

	/**
	 * Settings Dialog callback
	 */
	private class MySettingDialogListener implements OnSettingDialogListener {

		@Override
		public void onSettingMenuClick() {
			Intent intent = new Intent(MainActivity.this, SettingActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.activity_bottom_in,
					R.anim.activity_bottom_out);
			mSettingDialog.dismiss();
		}

		@Override
		public void onAboutMenuClick() {
			Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.activity_bottom_in,
					R.anim.activity_bottom_out);
			mSettingDialog.dismiss();
		}

	}

	/**
	 * Setting Calendar Fragment callback
	 */
	private class MyCalendarFragemntListener implements
	CalendarFragemntListener {

		@Override
		public void onDateHasChanged(String date) {
			// Determine whether the calendar page
			if (mRecentlyFragment != null
					&& mRecentlyFragment.getTag().equals(CALENDAR_FRAGMENT)) {
				mTxtDate.setText(date);
				mRelativeTitleText.setVisibility(View.VISIBLE);
				mTxtTabTitle.setVisibility(View.GONE);
			}
		}

	}

	/**
	 * Set Record Fragment callback
	 */
	private class MyRecordFragmentListener implements RecordFragmentListener {

		@Override
		public void OnChildPageChange(int position, String title) {
			// Record to determine whether the page
			if (mRecentlyFragment != null
					&& !mRecentlyFragment.getTag().equals(CALENDAR_FRAGMENT)) {
				mRelativeTitleText.setVisibility(View.GONE);
				mTxtTabTitle.setVisibility(View.VISIBLE);
				mTxtTabTitle.setText(title);
			}
		}

	}

	/**
	 * 2MyKarteListFragemnt
	 */
	public void intentToMyKarteFragemnt() {
		isFristClickIndagationFragment = false;
		showIndagationTab();
		if (mRecentlyFragment != null) {
			((RecordFragment) mRecentlyFragment).showMyKarteFragment();
			mImgCalendar.setChecked(false);
			mImgIndagation.setChecked(true);
		}
	}

	/**
	 * 2MemoListFragemnt
	 */
	public void intentToMemoListFragemnt() {
		isFristClickIndagationFragment = false;
		showIndagationTab();
		if (mRecentlyFragment != null) {
			((RecordFragment) mRecentlyFragment).showMemoFragment();
			mImgCalendar.setChecked(false);
			mImgIndagation.setChecked(true);
		}
	}

	/**
	 * 2VasHaqListFragemnt
	 */
	public void intentToVasHaqListFragemnt() {
		isFristClickIndagationFragment = false;
		showIndagationTab();
		if (mRecentlyFragment != null) {
			((RecordFragment) mRecentlyFragment).showVasHaqFragment();
			mImgCalendar.setChecked(false);
			mImgIndagation.setChecked(true);
		}
	}

	protected void checkFragmentState() {
		if (mRecentlyFragment != null
				&& !MyUtils.isNull(mRecentlyFragment.getTag())) {
			if (mRecentlyFragment.getTag().toString()
					.equals(mImgIndagation.getTag().toString())) {
				((RecordFragment) mRecentlyFragment).reFrashViewMainTitle();

			}
		}
	}

	@Override
	protected void onResume() {

		//checkAppModeChange();
		checkFragmentState();
		checkDialogState();
		checkShowMykarte();
		checkShowCarlendar();
		super.onResume();
	}

	private void checkDialogState() {
		if (mApp.isSetDialogAddEventDismiss()) {
			if (mCreateNewEventDialog != null
					&& mCreateNewEventDialog.isShowing()) {
				mApp.SetDialogAddEventDismiss(false);
				mCreateNewEventDialog.dismiss();
			}
		}
	}

	private void checkShowCarlendar() {
		if (mApp.isShowCarlendarFragment()) {
			mApp.setShowCarlendarFragment(false);
			showCalendarTab();
		}
	}

	private void checkShowMykarte() {
		if (mApp.isShowMyKarteFragment()) {
			mApp.setShowMyKarteFragment(false);
			intentToMyKarteFragemnt();
		}
	}

	public SQLiteDatabase getDb() {
		return db;
	}

	public void setDb(SQLiteDatabase db) {
		this.db = db;
	}

	/**
	 * Check the current mode and the system mode are matched, adjust the UI
	 * display
	 */
	private void checkAppModeChange() {
		if (getAppMode() != currentMode || isFirstIn) {
			isFirstIn = false;
			currentMode = getAppMode();
			if (mTxtModeTip == null || mImgPlus == null) {
				return;
			}
			if (currentMode == App.NORMAL_MODE) {
				mTxtModeTip.setVisibility(View.GONE);
				mImgPlus.setVisibility(View.VISIBLE);
			} else {
				mTxtModeTip.setVisibility(View.VISIBLE);
				mImgPlus.setVisibility(View.GONE);
			}

		}
	}

	//upload database
	public void uploadDB() {


		
		boolean isNeedUpImage = false;
		isNeedUpImage = ImageDBOperator.isNeedUpload(imagedb);
		if (isNeedUpImage) {
			uploadFlag = 0;

			list_image_upload = ImageDBOperator
					.queryUploadImage(imagedb);
			String token = App.getInstance().getmLoginUtils()
					.getToken();
			String type = "2";
			for(uploadFlag = 0;uploadFlag < list_image_upload.size();uploadFlag++){
				File imageFile = new File(list_image_upload.get(uploadFlag)
						.getLocalurl());
				if (imageFile.exists()) {
					if (MyUtils.isNetWork(getApplicationContext())) {
						upImageFile(token, type, imageFile);
						break;
					}
				}
			}
			if(uploadFlag >= list_image_upload.size()){
				String token1 = App.getInstance().getmLoginUtils()
						.getToken();
				String type1 = "1";
				File dbFile = new File(SDConfig.DB_PATH);
				if (MyUtils.isNetWork(getApplicationContext())) {
					if (dbFile.exists()) {
						upDbFile(token1, type1, dbFile);
					} else {
						mProgressDialog.removeDialog();
					}
				}
			}

		} else {
			String token = App.getInstance().getmLoginUtils()
					.getToken();
			String type = "1";
			File dbFile = new File(SDConfig.DB_PATH);
			if (MyUtils.isNetWork(getApplicationContext())) {
				if (dbFile.exists()) {
					upDbFile(token, type, dbFile);
				} else {
					mProgressDialog.removeDialog();
				}
			}
		}


	}


	//notify mainthread cancel the dialog
	public class NotifyCancelDialogRecevier extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(SerViceBroadCastAction.NOTIFY_CANCELDIALOG_FLAG)) {
				mProgressDialog.removeDialog();
			}

		}
	}

	//create broadcast monitor that Service is open
	public class NotifyServiceOpenRecevier extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(SerViceBroadCastAction.NOTIFY_SERVICEOPEN_FLAG)) {
				String token = App.getInstance().getmLoginUtils().getToken();
				if (token != null) {
					File dbFile = new File(SDConfig.DB_PATH);
					if (App.isfirstcome&&MyUtils.isNetWork(getApplicationContext())&&dbFile.exists()) {
						mProgressDialog.showDialog();
						//uploadDB();

					}
				}
			}

		}
	}

	@Override
	public void onBackPressed() {
		mApp.isfirstcome = true;
		AppManager.getAppManager().AppExit(this);
		super.onBackPressed();
	}

	public class NotifyAppModeChangedRecevier extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(SerViceBroadCastAction.NOTIFY_APP_MODE_CHANGED)) {
				checkAppModeChange();
			}
		}
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
					SymptomMemoBean mySymptomMemoBean = DBOperator
							.querySymptomMemoById(db, list_image_upload
									.get(uploadFlag).getSymptomid()
									+ "");
					mySymptomMemoBean.setUrl(data.getFile_url());
					boolean isSuccess = DBOperator.updateSymptomMemo(
							db, mySymptomMemoBean);
					list_image_upload
					.get(uploadFlag).setIsupload(1);
					ImageDBOperator.updateImageRecordBySymptomImageBean(imagedb, list_image_upload
							.get(uploadFlag));
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


						if (App.isfirstcome) {
							String token = App.getInstance()
									.getmLoginUtils().getToken();
							String type = "1";
							File dbFile = new File(SDConfig.DB_PATH);
							if (MyUtils.isNetWork(getApplicationContext())) {
								upDbFile(token, type, dbFile);
							}
						}
					}
				}else if (data != null&&data.getError().equals(425+"")){
					mProgressDialog.removeDialog();
					App.getInstance().getmLoginUtils().logout();
				}else{
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


						if (App.isfirstcome) {
							String token = App.getInstance()
									.getmLoginUtils().getToken();
							String type = "1";
							File dbFile = new File(SDConfig.DB_PATH);
							if (MyUtils.isNetWork(getApplicationContext())) {
								upDbFile(token, type, dbFile);
							}
						}
					}
				}
			}

			@Override
			public void onStart() {

			}

			@Override
			public void onFailed(Throwable throwable, String reason) {
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


					if (App.isfirstcome) {
						String token = App.getInstance()
								.getmLoginUtils().getToken();
						String type = "1";
						File dbFile = new File(SDConfig.DB_PATH);
						if (MyUtils.isNetWork(getApplicationContext()) && dbFile.exists() && dbFile !=null) {
							upDbFile(token, type, dbFile);
						}
					}
				}
			}
		});
	}

	//upload dbfile
	private void upDbFile(String token, String type, File file) {
		HttpRequest.getInstance().UpDb(token, type, file,
				new CommonCallBack<FileUpRequestBean>() {

			@Override
			public void onSuccess(FileUpRequestBean data) {
				mProgressDialog.removeDialog();
				if (data != null && data.getError().equals("200")) {
					//mProgressDialog.removeDialog();
					Long date = System.currentTimeMillis();
					Long day = DateFormatUtil.geDataLongZeroDay(date);
					String dayString = getApplication().getResources()
							.getString(R.string.update_date_pre)
							+ DateFormatUtil.getStringDataByLong(date);
					App.getInstance().setUpdateTime(dayString);
				} else if(data != null && data.getError().equals("425")){
					App.getInstance().getmLoginUtils().logout();
				}
				else {

				}

			}

			@Override
			public void onStart() {

			}

			@Override
			public void onFailed(Throwable throwable, String reason) {
				mProgressDialog.removeDialog();

			}
		});
	}
}
