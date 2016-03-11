package com.bravesoft.riumachi.activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.bean.ScheduleBean;
import com.bravesoft.riumachi.constant.CustomLoopType;
import com.bravesoft.riumachi.constant.DialogType;
import com.bravesoft.riumachi.constant.NotificationType;
import com.bravesoft.riumachi.constant.ScheduleAllDay;
import com.bravesoft.riumachi.constant.ScheduleLoopType;
import com.bravesoft.riumachi.constant.ScheduleType;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.database.DBHelper;
import com.bravesoft.riumachi.database.DBOperator;
import com.bravesoft.riumachi.dialog.DatePickDialog;
import com.bravesoft.riumachi.dialog.DatePickDialog.OnDatePickDialogClickListener;
import com.bravesoft.riumachi.dialog.GeneralListDialog;
import com.bravesoft.riumachi.dialog.GeneralListDialog.OnGenenalDialogClickListener;
import com.bravesoft.riumachi.dialog.GeneralListDialog.OnGenenalDialogItemClickListener;
import com.bravesoft.riumachi.dialog.GeneralStringDialog;
import com.bravesoft.riumachi.dialog.GeneralStringDialog.OnGenenalStringDialogClickListener;
import com.bravesoft.riumachi.dialog.TimePickDialog;
import com.bravesoft.riumachi.dialog.TimePickDialog.OnTimePickDialogClickListener;
import com.bravesoft.riumachi.dialog.WheelChoiceDialog;
import com.bravesoft.riumachi.dialog.WheelChoiceDialog.OnWheelChoiceDialogClickListener;
import com.bravesoft.riumachi.util.CommonUtils;
import com.bravesoft.riumachi.util.DateFormatUtil;
import com.bravesoft.riumachi.util.MyUtils;

public class CreateMedicineScheduleActivity extends BaseActivity implements
		OnClickListener ,OnCheckedChangeListener {

	public static final String OPEN_TYPE = "open_type";
	public static final String OPEN_TYPE_CREATE = "open_type_create";
	public static final String OPEN_TYPE_EDIT = "open_type_edit";
	public static final String SCHEDULE_TYPE = "schedule_type";
	public static final String SCHEDULE_ID = "schedule_id";
	public static final String CURRENT_DATE = "current_date";
	private String mScheduleId = "-1";
	private String mOpenType;
	private TextView mTxtTitle;
	private TextView mTxtSave;
	private TextView mTxtDelete;
	private TextView mTxtName;
	private TextView mTxtStartDate;
	private TextView mTxtEndDate;
	private TextView mTxtStartTime;
	private TextView mTxtEndTime;
	private TextView mTxtLoop;
	private TextView mTxtNotification;
	private TextView mTxtNotificationTitle;
	private TextView mTxtNotifyAllDayTip;
	private ToggleButton mToggleNotificationAllDay;
	private LinearLayout mLinearDelete;
	private RelativeLayout mRelativewName;
	private RelativeLayout mRelativewNotification;
	private GeneralStringDialog mDeleteNoLoopDialog;
	private GeneralListDialog mDeleteLoopDialog; 
	private GeneralListDialog mUpdateLoopDialog; 
	private GeneralListDialog mLoopTimePickDialog;
	private GeneralListDialog mNotificationTimePickDialog;
	private DatePickDialog mDatePickDialog;
	private TimePickDialog mTimePickDialogStart;
	private TimePickDialog mTimePickDialogEnd;
	public  String mDateType = ScheduleAllDay.OFF;
	private SQLiteDatabase db;
	private DBHelper mDbHelper;
	private ScheduleBean mScheduleBean;
	private long mStartTime; //schedule start time
	private long mEndTime; //schedule end time
	private String mStartHourAndMinute;
	private String mEndHourAndMinute;
	private String mMedicalType;
	private String mMedicalName;
	private String mCustomLoopType = "1";
	private String mCustomLoopValue= "1";
	private int mLoopType = 1;
	private String mCurrentDate;
	private long mStartDate; //schedule start date
	private long mEndDate; //schedule end date
	private int mNotificationType = Integer.parseInt(NotificationType.ONE_HOUR);
	private View view_back,view_add;
	private String flag = "false";
	private WheelChoiceDialog mWheelChoiceDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_create_medicine_schedule);
		mDbHelper = new DBHelper(getApplicationContext());
		db = mDbHelper.getReadableDatabase();
		
		if (savedInstanceState != null) {
			mOpenType = savedInstanceState.getString(OPEN_TYPE);
			mScheduleId = savedInstanceState.getString(SCHEDULE_ID);
			mCurrentDate = savedInstanceState.getString(CURRENT_DATE); 
		} else if (getIntent() != null) {
			Intent intent = getIntent();
			mOpenType = intent.getStringExtra(OPEN_TYPE);
			mScheduleId = intent.getStringExtra(SCHEDULE_ID);
			mCurrentDate = getIntent().getStringExtra(CURRENT_DATE);
		}
		initView();
		super.onCreate(savedInstanceState);
	}

	public static void startCreateMedicineScheduleActivity(Context context, String openType , 
			String scheduleId , String currentDate) {
		Intent intent = new Intent(context, CreateMedicineScheduleActivity.class);
		intent.putExtra(OPEN_TYPE, openType);
		intent.putExtra(SCHEDULE_ID, scheduleId);
		intent.putExtra(CURRENT_DATE, currentDate);
		context.startActivity(intent);
	}
	
	public static void startCreateMedicineScheduleActivity(Context context, String openType) {
		Intent intent = new Intent(context, CreateMedicineScheduleActivity.class);
		intent.putExtra(OPEN_TYPE, openType);
		context.startActivity(intent);
	}

	private void initView() {
		view_back=getRateView(R.id.view_back, true);
		view_add=getRateView(R.id.view_add, true);
		getRateView(R.id.relative_title_other, true);
		mLinearDelete = getRateView(R.id.linear_delete_schedule, true);
		getRateView(R.id.relative_notify_all_day, true);
		mRelativewName=getRateView(R.id.relative_schedule_name, true);
		getRateView(R.id.relative_start_time, true);
		getRateView(R.id.relative_end_time, true);
		getRateView(R.id.relative_loop, true).setOnClickListener(this);
		mRelativewNotification = getRateView(R.id.relative_notification, true);
		getRateView(R.id.horizotal_line_one, true);
		getRateView(R.id.horizotal_line_two, true);
		getRateView(R.id.horizotal_line_three, true);
		getRateView(R.id.horizotal_line_four, true);
		getRateView(R.id.horizotal_line_five, true);
		getRateView(R.id.horizotal_line_six, true);
		

		getRateView(R.id.img_back, true);
		getRateView(R.id.img_allow_all, true).setOnClickListener(this);
		mToggleNotificationAllDay = getRateView(R.id.radio_notify_all_day, true);

		mTxtTitle = getTextView(R.id.txt_title_name, true, 37 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		mTxtSave = getTextView(R.id.txt_operate, true, 33,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mTxtDelete = getTextView(R.id.txt_delete_mark, true, 32,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		getTextView(R.id.txt_medicine_schedule_tip, true, 32,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mTxtName=getTextView(R.id.txt_medicine_schedule_name, true, 32,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mTxtNotifyAllDayTip = getTextView(R.id.txt_notify_all_day_title, true, 32,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		getTextView(R.id.txt_start_time_title, true, 32,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mTxtStartDate = getTextView(R.id.txt_start_date, true, 32,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mTxtStartTime = getTextView(R.id.txt_start_time, true, 32,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		getTextView(R.id.txt_end_time_title, true, 32,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mTxtEndDate = getTextView(R.id.txt_end_date, true, 32,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mTxtEndTime = getTextView(R.id.txt_end_time, true, 32,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		getTextView(R.id.txt_loop_title, true, 32,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mTxtLoop = getTextView(R.id.txt_loop, true, 32,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mTxtNotificationTitle = getTextView(R.id.txt_notification_title, true, 32,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mTxtNotification = getTextView(R.id.txt_notification, true, 32,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);

		 
		view_add.setOnClickListener(new OnSaveButtonClickListener());
		mLinearDelete.setOnClickListener(new OnDeleteButtonClickListener());
		mRelativewNotification.setOnClickListener(this);
		mRelativewName.setOnClickListener(this);
		mTxtStartDate.setOnClickListener(this);
		mTxtStartTime.setOnClickListener(this);
		mTxtEndTime.setOnClickListener(this);
		view_back.setOnClickListener(this);
		mToggleNotificationAllDay.setOnCheckedChangeListener(this);
		
	 
		initContent();
	}

	/**
	 * Initialization parameters for incoming interface, access to schedule the start time and end time.
	 */
	private void initContent() {
		
		mTxtTitle.setText(getString(R.string.schedule_medicine_create));
		mTxtNotifyAllDayTip.setText(getString(R.string.medicine_schedule_notification_tip));
		
		if (mOpenType != null) {
			
			if (mOpenType.equals(OPEN_TYPE_CREATE)) {   //dialog
				 
				mStartDate = DateFormatUtil.geDataLongZeroMinuteAndSeconed(
						DateFormatUtil.getLongDateBySplicing(mApp.getmCurrentDateLong(),System.currentTimeMillis() ));  
				 
				mEndDate =  mStartDate + DateFormatUtil.ONE_HOUR ;   
				mTxtSave.setText(getString(R.string.schedule_state_create)); 
				mTxtDelete.setTextColor(getResources().getColor(R.color.gray_line));
				mLinearDelete.setClickable(false);
				mTxtNotification.setText(getResources().getStringArray(  
						R.array.schedule_notification_time_pick_array)[mNotificationType-1]);
			} else if (mOpenType.equals(OPEN_TYPE_EDIT)) { // list 
				 
				mTxtSave.setText(getString(R.string.schedule_state_edit));
				mTxtDelete.setTextColor(getResources().getColor(R.color.pink));
				mLinearDelete.setClickable(true);
				mScheduleBean = DBOperator.queryScheduleById(db, mScheduleId);  
				if (mScheduleBean != null ) {
					if (mScheduleBean.getShunichi() == null|| mScheduleBean.getShunichi().equals(ScheduleAllDay.OFF)) {
						mStartDate = Long.parseLong(mScheduleBean.getStarttime());
						mEndDate = Long.parseLong(mScheduleBean.getEndtime()); 
						mStartDate = DateFormatUtil.getStringTwoDatemerge(Long.parseLong(mCurrentDate), mStartDate);
						System.out.println("203 list "+mStartDate);
						mEndDate = DateFormatUtil.getStringTwoDatemerge(Long.parseLong(mCurrentDate), mEndDate);
					}else{
//						mStartDate = DateFormatUtil.getDataLongLastDayZeroClick(Long.parseLong(mCurrentDate));
//						mEndDate = DateFormatUtil.getDataLongLastDayLastClick(Long.parseLong(mCurrentDate));
						mStartDate = DateFormatUtil.geDataLongZeroClick(Long.parseLong(mCurrentDate)); 
						mEndDate = DateFormatUtil.geDataLongZeroClick(Long.parseLong(mCurrentDate)+DateFormatUtil.ONE_DAY);
						System.out.println("209 list "+mStartDate);
					}
					
					fillEditContent();
				}
			}
		}
		mTxtStartDate.setText(DateFormatUtil.getStringDataDiagonal(mStartDate));  
		mTxtEndDate.setText(DateFormatUtil.getStringDataDiagonal(mStartDate));  
		if ((mStartDate-DateFormatUtil.geDataLongZeroClick(mStartDate))%DateFormatUtil.ONE_DAY == 0) { //list
			System.out.println("if  "+mStartDate);
			
			mStartHourAndMinute = getString(R.string.schedule_start_time_zero);
		}else{  //dialog
			System.out.println("else  "+mStartDate);
			mStartHourAndMinute = DateFormatUtil.getStringHourAndMinute(mStartDate); 
		}
		if ((mEndDate-DateFormatUtil.geDataLongZeroClick(mEndDate))%DateFormatUtil.ONE_DAY == 0) { 
			mEndHourAndMinute = getString(R.string.schedule_end_time_zero);
		}else{ 
			mEndHourAndMinute = DateFormatUtil.getStringHourAndMinute(mEndDate);
		} 
		mTxtStartTime.setText(mStartHourAndMinute); 
		mTxtEndTime.setText(mEndHourAndMinute);  
		
		if(flag.equals("false")){//lasttime do not save shifen
			 
			mTxtStartTime.setText(DateFormatUtil.getStringHourAndMinute(DateFormatUtil.geDataLongZeroMinuteAndSeconed(
					DateFormatUtil.getLongDateBySplicing(mApp.getmCurrentDateLong(),System.currentTimeMillis() )) )); 
			mTxtEndTime.setText(DateFormatUtil.getStringHourAndMinute(DateFormatUtil.geDataLongZeroMinuteAndSeconed(
					DateFormatUtil.getLongDateBySplicing(mApp.getmCurrentDateLong(),System.currentTimeMillis() ))+DateFormatUtil.ONE_HOUR ));  
			
			mEndDate = DateFormatUtil.geDataLongZeroMinuteAndSeconed(
					DateFormatUtil.getLongDateBySplicing(mApp.getmCurrentDateLong(),System.currentTimeMillis() ))+DateFormatUtil.ONE_HOUR;
			mStartDate = DateFormatUtil.geDataLongZeroMinuteAndSeconed(
					DateFormatUtil.getLongDateBySplicing(mApp.getmCurrentDateLong(),System.currentTimeMillis() ));
			mStartHourAndMinute = DateFormatUtil.getStringHourAndMinute(mStartDate); 
			mEndHourAndMinute = DateFormatUtil.getStringHourAndMinute(mEndDate);
			
		}
		
		
		//System.out.println("mStartDate=="+mStartDate+"=mEndDate="+mEndDate);
	}

	/**
	 * Edit Open schedule initialization
	 */
	private void fillEditContent() {
		mMedicalName = mScheduleBean.getTitle();
		mMedicalType = mScheduleBean.getType();
		mTxtName.setText(mMedicalName); 
		mLoopType = Integer.parseInt(mScheduleBean.getKurikaeshi());
		if (mLoopType >= 0 && mLoopType < 7) {
			if (mLoopType == 6) {
				mCustomLoopType = mScheduleBean.getCustom_loop_type();
				mCustomLoopValue = mScheduleBean.getCustom_loop_value();
				if (!MyUtils.isNull(mCustomLoopType)&&!MyUtils.isNull(mCustomLoopValue)) {
					if (mCustomLoopType.equals(CustomLoopType.CUSTOM_LOOP_DAY)) {
						mTxtLoop.setText(mCustomLoopValue+CustomLoopType.CUSTOM_LOOP_DAY_TIP);
					}else if (mCustomLoopType.equals(CustomLoopType.CUSTOM_LOOP_WEEK)) {
						mTxtLoop.setText(mCustomLoopValue+CustomLoopType.CUSTOM_LOOP_WEEK_TIP);
					}
				}
			}else{
				if (mLoopType == 0 || mLoopType == 1) {
					mTxtLoop.setText(getString(R.string.schedule_default_repeat));
				}else{
					mTxtLoop.setText(getResources().getStringArray( 
							R.array.schedule_loop_interval_pick_medical_array)[mLoopType-1]); 
				}
			}
		}
		mNotificationType = Integer.parseInt(mScheduleBean.getTuchi());
		if (mNotificationType > 0 && mNotificationType < 9) {
			mTxtNotification.setText(getResources().getStringArray(  
					R.array.schedule_notification_time_pick_array)[mNotificationType-1]);
		}
		mMedicalType = mScheduleBean.getMedicalType();
		if (mScheduleBean.getShunichi().equals(ScheduleAllDay.OFF)) {  
			mToggleNotificationAllDay.setChecked(false);
			flag = "true";
		}else{
			mToggleNotificationAllDay.setChecked(true);//  If it is "on", then the front to save time when there is no saving hours
			flag = "false";
		}
	}
	
	/**
	 * Click the Save button
	 */
	private class OnSaveButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View view) {
			if (MyUtils.isNull(mTxtName.getText().toString())) { // Schedule called empty)
				CommonUtils.showToastMessage(CreateMedicineScheduleActivity.this, getString(R.string.schedule_medicine_name_is_null));
				return;
			}
			if (mStartDate >= mEndDate) {
				CommonUtils.showToastMessage(CreateMedicineScheduleActivity.this, getString(R.string.schedule_date_error));
				return;
			}
			System.out.println("mStartDate=pickDate="+DateFormatUtil.getWholeDate(mStartDate));
			final ScheduleBean scheduleBean = new ScheduleBean();
			scheduleBean.setType(ScheduleType.MEDICINE_SCHEDULE);
			scheduleBean.setTitle(mTxtName.getText().toString());
			scheduleBean.setSchedule_start_time(mStartDate+"");
			scheduleBean.setKurikaeshi(mLoopType+"");
			scheduleBean.setTuchi(mNotificationType+"");
			scheduleBean.setMedicalType(mMedicalType);
			scheduleBean.setShunichi(mDateType);
			scheduleBean.setCustom_loop_type(mCustomLoopType);
			scheduleBean.setCustom_loop_value(mCustomLoopValue);
			if (mDateType.equals(ScheduleAllDay.ON)) { 
				scheduleBean.setStarttime(DateFormatUtil.geDataStringZeroClick(mStartDate));
				scheduleBean.setEndtime(DateFormatUtil.geDataStringZeroTheLastClick(mEndDate));
			}else{
				scheduleBean.setStarttime(mStartDate+"");
				scheduleBean.setEndtime(mEndDate+"");
			}
			//System.out.println("mStartDate"+mStartDate+"--mEndDate--"+mEndDate);
			if (mOpenType.equals(OPEN_TYPE_CREATE)) {  
				boolean result = DBOperator.insertSchedule(db, scheduleBean);
				if (mNotificationType!=0 && mNotificationType!=1) {
					if (result) {
						Intent intent=new Intent();
						intent.setAction("com.bravesoft.notifydata");
						CreateMedicineScheduleActivity.this.sendBroadcast(intent);
					}
				}
				mApp.SetDialogAddEventDismiss(true);
				mApp.setShowCarlendarFragment(true);
				finishActivity();
			}else if (mOpenType.equals(OPEN_TYPE_EDIT)) {  
				if (mScheduleBean.getKurikaeshi()==null ||  
						mScheduleBean.getKurikaeshi().equals(ScheduleLoopType.NO_LOOP)|| 
						mScheduleBean.getKurikaeshi().equals(ScheduleLoopType.NO_RECORD)) {
					scheduleBean.setId(Integer.parseInt(mScheduleId));
					boolean result =  DBOperator.updateSchedule(db, scheduleBean);
					if (mNotificationType!=0 && mNotificationType!=1) {
						if (result) {
							Intent intent=new Intent();
							intent.setAction("com.bravesoft.notifydata");
							CreateMedicineScheduleActivity.this.sendBroadcast(intent);
						}
					}
					mApp.SetDialogAddEventDismiss(true);
					mApp.setShowCarlendarFragment(true);
					finishActivity();
				}else{
					if (mUpdateLoopDialog == null) {
						mUpdateLoopDialog = new GeneralListDialog(CreateMedicineScheduleActivity.this,
								GeneralListDialog.SCHEDULE_LOOP_UPDATE_DIALOG);
						mUpdateLoopDialog.setOnGenenalDialogClickListener(new OnGenenalDialogClickListener() {
							
							@Override
							public void OnGeneralSureTextClicked(int position) {
								
								boolean result = DBOperator.updateLoopScheduleChangeOther(db, mScheduleId, scheduleBean);
								if (mNotificationType!=0 && mNotificationType!=1) {
									if (result) {
										Intent intent=new Intent();
										intent.setAction("com.bravesoft.notifydata");
										CreateMedicineScheduleActivity.this.sendBroadcast(intent);
									}
								}
								mApp.SetDialogAddEventDismiss(true);
								mApp.setShowCarlendarFragment(true);
								finishActivity();
							}
							
							@Override
							public void OnGeneralCancelTextClicked() {
								mUpdateLoopDialog.dismiss();
							}
						});
					}
					mUpdateLoopDialog.show();
				}
				
			}
		}
		
	}

	/**
	 * Click the Delete button
	 */
	private class OnDeleteButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View view) {
			if (mScheduleBean.getKurikaeshi()==null ||  
					mScheduleBean.getKurikaeshi().equals(ScheduleLoopType.NO_LOOP)|| 
					mScheduleBean.getKurikaeshi().equals(ScheduleLoopType.NO_RECORD)) {
				if (mDeleteNoLoopDialog == null) { 
					mDeleteNoLoopDialog = new GeneralStringDialog(CreateMedicineScheduleActivity.this,
							getString(R.string.dialog_title_delete_schedule_no_loop),DialogType.DIALOG_SURE,
							new OnGenenalStringDialogClickListener() {

						@Override
						public void OnGeneralSureTextClicked() {
							if (DBOperator.deleteScheduleById(db, mScheduleId)) {
								Intent intent=new Intent();
								intent.setAction("com.bravesoft.notifydata");
								CreateMedicineScheduleActivity.this.sendBroadcast(intent);
								mDeleteNoLoopDialog.dismiss();
								finishActivity();
							}else{
								mDeleteNoLoopDialog.dismiss();
								CommonUtils.showToastMessage(CreateMedicineScheduleActivity.this,
										getString(R.string.schedule_operate_failed_message));
							}
							
						}

						@Override
						public void OnGeneralCancelTextClicked() {
							mDeleteNoLoopDialog.dismiss();
						}
						

					});
				}
				mDeleteNoLoopDialog.show();  
			}else {
				if (mDeleteLoopDialog == null) {
					mDeleteLoopDialog = new GeneralListDialog(CreateMedicineScheduleActivity.this,
							GeneralListDialog.SCHEDULE_LOOP_DELETE_DIALOG);
					mDeleteLoopDialog.setOnGenenalDialogClickListener(new OnGenenalDialogClickListener() {
						
						@Override
						public void OnGeneralSureTextClicked(int position) {
							if (position == 0) {  
								if (DBOperator.deleteOneScheduleByIdLoop(db, mScheduleId,mStartDate)) {
									Intent intent=new Intent();
									intent.setAction("com.bravesoft.notifydata");
									CreateMedicineScheduleActivity.this.sendBroadcast(intent);
									finishActivity();
								}else{
									CommonUtils.showToastMessage(CreateMedicineScheduleActivity.this, 
											getString(R.string.schedule_operate_failed_message));
								}
							}else if (position == 1) { 
								if (DBOperator.deleteManyScheduleByIdLoop(db, mScheduleId,mStartDate)) {
									Intent intent=new Intent();
									intent.setAction("com.bravesoft.notifydata");
									CreateMedicineScheduleActivity.this.sendBroadcast(intent);
									finishActivity();
								}else{
									CommonUtils.showToastMessage(CreateMedicineScheduleActivity.this, 
											getString(R.string.schedule_operate_failed_message));
								}
							}
							mDeleteLoopDialog.dismiss();
						}
						
						@Override
						public void OnGeneralCancelTextClicked() {
							mDeleteLoopDialog.dismiss();
						}
					});
				}
				mDeleteLoopDialog.show();
			}
		}
	}
	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(OPEN_TYPE, mOpenType);
		outState.putString(SCHEDULE_ID, mScheduleId);
		outState.putString(CURRENT_DATE, mCurrentDate);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void finish() {
		CommonUtils.exitKeyboard(this);
		super.finish();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data==null||data.equals("")) {
			
		}else if (data!=null||!data.equals("")) {
		
			if (requestCode==1) {
				System.out.println("opentype"+mOpenType);
				
				String name = data.getExtras().getString(DrugListActivity.MEDICINE_NAME);
				String type = data.getExtras().getString(DrugListActivity.MEDICINE_TYPE);
				if (!MyUtils.isNull(name)) {
					mMedicalName = name;
				}
				if (!MyUtils.isNull(type)) {
					mMedicalType = type;
				}
//				LogUtil.v("NAME", mMedicalName);
//				LogUtil.v("TYPE", mMedicalType);
				mTxtName.setText(mMedicalName);
			}
		}
	}
	
	

	private String customLoopValue = "1";
	private String customLoopType = "1";
	private boolean isCustomLoopShow  = false;
	private boolean isUseCacheData = true;
	private int theLastPosition  = 0;
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.view_back:
			finish();
			overridePendingTransition(0,R.anim.activity_bottom_out);
			break;
		case R.id.relative_schedule_name:
			Intent intent = new Intent(this,DrugListActivity.class);
//			intent.putExtra(DrugListActivity.MEDICINE_NAME, mMedicalName);
//			intent.putExtra(DrugListActivity.MEDICINE_TYPE, mMedicalType);
			startActivityForResult(intent, 1);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			break;
		case R.id.relative_loop:
			if (isUseCacheData) {
				customLoopType = mCustomLoopType;
				customLoopValue = mCustomLoopValue;
			}
			
			if (mLoopTimePickDialog == null) {
				mLoopTimePickDialog = new GeneralListDialog(this,
						GeneralListDialog.SCHEDULE_LOOP_INTERVAL_PICK_MEDICAL_DIALOG);
				mLoopTimePickDialog.setFocus(mLoopType-1);
				
				if (mLoopType == Integer.parseInt(ScheduleLoopType.CUSTOM_LOOP)) {
					isCustomLoopShow = true;
					String value = "";
					if (customLoopType.equals(CustomLoopType.CUSTOM_LOOP_DAY)) {
						value = customLoopValue+CustomLoopType.CUSTOM_LOOP_DAY_TIP;
					}else if (customLoopType.equals(CustomLoopType.CUSTOM_LOOP_WEEK)) {
						value = customLoopValue+CustomLoopType.CUSTOM_LOOP_WEEK_TIP;
					}
					mLoopTimePickDialog.addOnGenenalDialogListValue(value);
				}
				mLoopTimePickDialog.setOnGenenalDialogItemClickListener(new OnGenenalDialogItemClickListener() {
					
					@Override
					public void onGeneralDialogItemClickListener(final int position) {
						if (mLoopTimePickDialog.isTheLastOne(position)) {
							mLoopTimePickDialog.dismiss();
							if (mWheelChoiceDialog == null) {
								mWheelChoiceDialog = new  WheelChoiceDialog(CreateMedicineScheduleActivity.this );
										 
								mWheelChoiceDialog.setOnWheelChoiceDialogClickListener(new OnWheelChoiceDialogClickListener() {
									@Override
									public void OnWheelChoiceDialogSureClicked(
											String loopvaule, String looptype) {
										isCustomLoopShow = true;
										mLoopTimePickDialog.show();
										mLoopType = Integer.parseInt(ScheduleLoopType.CUSTOM_LOOP);
										mLoopTimePickDialog.setFocus(mLoopType-1);
										mWheelChoiceDialog.dismiss();
										customLoopType = looptype;
										customLoopValue = loopvaule;
										String value = "";
										
										if (customLoopType.equals(CustomLoopType.CUSTOM_LOOP_DAY)) {
											value = customLoopValue+CustomLoopType.CUSTOM_LOOP_DAY_TIP;
										}else if (customLoopType.equals(CustomLoopType.CUSTOM_LOOP_WEEK)) {
											value = customLoopValue+CustomLoopType.CUSTOM_LOOP_WEEK_TIP;
										}
										
										if (mLoopTimePickDialog.getDatasLenght() > 6) {
											mLoopTimePickDialog.setOnGenenalDialogListValue(5,value);
										}else{
											mLoopTimePickDialog.addOnGenenalDialogListValue(value);
										}
										
									}

									@Override
									public void OnWheelChoiceDialogCancelClicked() {
										mLoopTimePickDialog.show();
										mWheelChoiceDialog.dismiss();
										mLoopTimePickDialog.setFocus(mLoopType-1);
									}
									
									 
								});
							}
							
							mWheelChoiceDialog.setposition(Integer.parseInt(customLoopValue),Integer.parseInt(customLoopType));
							mWheelChoiceDialog.show();

						}
					}
				});
				
				
				mLoopTimePickDialog
						.setOnGenenalDialogClickListener(new OnGenenalDialogClickListener() {

							@Override
							public void OnGeneralSureTextClicked(int position) {
								mLoopTimePickDialog.dismiss();
								theLastPosition = position;
								mCustomLoopType = customLoopType;
								mCustomLoopValue = customLoopValue;
								if (position >= 0) {
									mLoopType = position + 1;
									if (position == 5) {
										isUseCacheData = true;
										if (customLoopType.equals(CustomLoopType.CUSTOM_LOOP_DAY)) {
											mTxtLoop.setText(customLoopValue+CustomLoopType.CUSTOM_LOOP_DAY_TIP);
										}else if (mCustomLoopType.equals(CustomLoopType.CUSTOM_LOOP_WEEK)) {
											mTxtLoop.setText(customLoopValue+CustomLoopType.CUSTOM_LOOP_WEEK_TIP);
										}
									}else{
										if (position == 0) {
											mTxtLoop.setText(getString(R.string.schedule_default_repeat));
										}else{
											mTxtLoop.setText(getResources().
													getStringArray(R.array.schedule_loop_interval_pick_medical_array)[position]);
										}
									}
								}
							}

							@Override
							public void OnGeneralCancelTextClicked() {
								if (theLastPosition != 5) {
									isUseCacheData = false;
								}
								mLoopTimePickDialog.dismiss();
							}
						});
			}
			if (isCustomLoopShow) {
				String value = "";
				if (customLoopType.equals(CustomLoopType.CUSTOM_LOOP_DAY)) {
					value = customLoopValue+CustomLoopType.CUSTOM_LOOP_DAY_TIP;
				}else if (customLoopType.equals(CustomLoopType.CUSTOM_LOOP_WEEK)) {
					value = customLoopValue+CustomLoopType.CUSTOM_LOOP_WEEK_TIP;
				}
				
				if (mLoopTimePickDialog.getDatasLenght() > 6) {
					mLoopTimePickDialog.setOnGenenalDialogListValue(5,value);
				}else{
					mLoopTimePickDialog.addOnGenenalDialogListValue(value);
				}
			}
			mLoopTimePickDialog.show();
			break;
		case R.id.relative_notification:
			if (mDateType.equals(ScheduleAllDay.ON)) {
				return;
			}else if(mDateType.equals(ScheduleAllDay.OFF)){
				if (mNotificationTimePickDialog == null) {
					mNotificationTimePickDialog = new GeneralListDialog(
							this,
							GeneralListDialog.SCHEDULE_NOTIFICATION_TIME_PICK_DIALOG);
					mNotificationTimePickDialog.setFocus(mNotificationType-1);
					mNotificationTimePickDialog
							.setOnGenenalDialogClickListener(new OnGenenalDialogClickListener() {

								@Override
								public void OnGeneralSureTextClicked(int position) {
									if (position >= 0) {
										mNotificationType = position + 1;
										mTxtNotification.setText(getResources().
												getStringArray(R.array.schedule_notification_time_pick_array)[position]);
									}
									mNotificationTimePickDialog.dismiss();
								}
								
								@Override
								public void OnGeneralCancelTextClicked() {
									mNotificationTimePickDialog.dismiss();
								}
							});
				}
				mNotificationTimePickDialog.show();
			}
			break;
		case R.id.txt_start_date:
			if (mDatePickDialog == null) {
				mDatePickDialog = new DatePickDialog(this,mStartDate);
				mDatePickDialog.setOnDatePickDialogClickListener(new OnDatePickDialogClickListener() {
					
					@Override
					public void OnDatePickDialogSureClicked(String date) {
						mStartDate = DateFormatUtil.getLongDateBySplicing(Long.parseLong(date), mStartHourAndMinute);
						mEndDate =  DateFormatUtil.getLongDateBySplicing(Long.parseLong(date),mEndHourAndMinute);
						mTxtStartDate.setText(DateFormatUtil.getStringDataDiagonal(mStartDate));
						mTxtEndDate.setText(DateFormatUtil.getStringDataDiagonal(mEndDate));
						mDatePickDialog.dismiss();
						System.out.println("mStartDate=pickDate="+DateFormatUtil.getWholeDate(mStartDate));
					}
					
					@Override
					public void OnDatePickDialogCancelClicked() {
						mDatePickDialog.dismiss();
					}
				});
			}
			mDatePickDialog.show();
			break;
		case R.id.txt_start_time:
			if (mTimePickDialogStart == null) {
				mTimePickDialogStart = new TimePickDialog(this,mStartHourAndMinute);
				mTimePickDialogStart.setOnTimePickDialogClickListener(new OnTimePickDialogClickListener() {
					
					@Override
					public void OnTimePickDialogSureClicked(String date) {
						mTimePickDialogStart.dismiss();
						mStartHourAndMinute = date;
						mTxtStartTime.setText(date);
						mStartDate = DateFormatUtil.getLongDateBySplicing(mStartDate,date);
					}
					
					@Override
					public void OnTimePickDialogCancelClicked() {
						mTimePickDialogStart.dismiss();
					}
				});
			}
			mTimePickDialogStart.setCurrentTime(mStartHourAndMinute);
			mTimePickDialogStart.show();
			break;
		case R.id.txt_end_time:
			if (mTimePickDialogEnd == null) {
				mTimePickDialogEnd = new TimePickDialog(this, mEndHourAndMinute);
				mTimePickDialogEnd.setOnTimePickDialogClickListener(new OnTimePickDialogClickListener() {
					
					@Override
					public void OnTimePickDialogSureClicked(String date) {
						System.out.println(mTxtEndTime+"  "+date);
						mTimePickDialogEnd.dismiss();
						mEndHourAndMinute = date;
						mTxtEndTime.setText(date);
						mEndDate = DateFormatUtil.getLongDateBySplicing(mEndDate,date);
					}
					
					@Override
					public void OnTimePickDialogCancelClicked() {
						mTimePickDialogEnd.dismiss();
					}
				});
			}
			mTimePickDialogEnd.setCurrentTime(mEndHourAndMinute);
			mTimePickDialogEnd.show();
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) { 
		mNotificationType = Integer.parseInt(NotificationType.NO_NOTIFICATION);
		mTxtNotification.setText(getResources().getStringArray(R.array.schedule_notification_time_pick_array)[0]);
		if (isChecked) {
			mDateType = ScheduleAllDay.ON;
			mTxtNotificationTitle.setTextColor(getResources().getColor(R.color.text_enable_gray));
			mTxtNotification.setTextColor(getResources().getColor(R.color.text_enable_gray));
		}else{
			mDateType =  ScheduleAllDay.OFF;
			mTxtNotificationTitle.setTextColor(getResources().getColor(R.color.black));
			mTxtNotification.setTextColor(getResources().getColor(R.color.black));
		}
		onDateDomeChanged();
	}

	private void onDateDomeChanged() {
		if (mDateType.equals(ScheduleAllDay.ON)) {
			mTxtStartTime.setVisibility(View.GONE);
			mTxtEndTime.setVisibility(View.GONE);
			mTxtEndDate.setVisibility(View.VISIBLE);
		}else if(mDateType.equals(ScheduleAllDay.OFF)){
			mTxtStartTime.setVisibility(View.VISIBLE);
			mTxtEndTime.setVisibility(View.VISIBLE);
			mTxtEndDate.setVisibility(View.GONE);
		}
	}
	@Override
	protected void onDestroy() {
		if (db != null && db.isOpen()) {
			db.close();
		}
		super.onDestroy();
	}

}
