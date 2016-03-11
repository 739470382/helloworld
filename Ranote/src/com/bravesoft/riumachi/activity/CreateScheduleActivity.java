package com.bravesoft.riumachi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.bean.ScheduleBean;
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
import com.bravesoft.riumachi.dialog.GeneralStringDialog;
import com.bravesoft.riumachi.dialog.GeneralStringDialog.OnGenenalStringDialogClickListener;
import com.bravesoft.riumachi.dialog.TimePickDialog;
import com.bravesoft.riumachi.dialog.TimePickDialog.OnTimePickDialogClickListener;
import com.bravesoft.riumachi.util.CommonUtils;
import com.bravesoft.riumachi.util.DateFormatUtil;
import com.bravesoft.riumachi.util.InputTools;
import com.bravesoft.riumachi.util.MyUtils;

public class CreateScheduleActivity extends BaseActivity implements
		OnClickListener ,OnCheckedChangeListener ,OnTouchListener {

	public static final String OPEN_TYPE = "open_type"; //activity open type key
	public static final String OPEN_TYPE_CREATE = "open_type_create"; //the open type is create schedule key
	public static final String OPEN_TYPE_EDIT = "open_type_edit"; //the open type is edit schedule key
	public static final String SCHEDULE_TYPE = "schedule_type"; // schedule type key
	public static final String SCHEDULE_ID = "schedule_id"; // schedule ID Key
	public static final String CURRENT_DATE = "current_date"; // current date key
	private String mOpenType; //open type
	private String mScheduleId = "-1"; //schedule id 
	private String mScheduleType; //schedule type
	private TextView mTxtTitle; //title
	private TextView mTxtSave; //save button
	private TextView mTxtDelete; //delete tip
	private TextView mTxtStartDate; //start date
	private TextView mTxtEndDate; //end Date
	private TextView mTxtStartTime; //etart time
	private TextView mTxtEndTime; //end time
	private TextView mTxtLoop; //current loop type
	private TextView mTxtNotification; //current nitify type
	private EditText mEditScheduleName; //schedule name
	private ToggleButton mToggleNotificationAllDay; //is notify all day 
	private LinearLayout mLinearDelete; //delete schedule
	private GeneralListDialog mDeleteLoopDialog; //delete schedule dialog(loop)
	private GeneralStringDialog mDeleteNoLoopDialog; //delete schedule dialog(no loop)
	private DatePickDialog mDatePickDialog; //datepicker dialog
	private GeneralListDialog mLoopTimePickDialog; //loop interval dialog
	private GeneralListDialog mUpdateLoopDialog; //update dialog(loop)
	private GeneralListDialog mNotificationTimePickDialog; //notification time
	private GeneralListDialog mNotificationAllDayTimePickDialog; //notification time
	private TimePickDialog mTimePickDialogStart; //timepicker dialog(start time)
	private TimePickDialog mTimePickDialogEnd; //timepicker dialog (end time)
	private SQLiteDatabase db; 
	private DBHelper mDbHelper; 
	public  String mDateType = ScheduleAllDay.OFF; //notification all day flag
	private ScheduleBean mScheduleBean;
	private String mCurrentDate; //currentdate
	private ScrollView mScrollView;
	private long mStartDate; //Starting time
	private long mEndDate; //End Time
	private String mStartHourAndMinute; //Start Time Division
	private String mEndHourAndMinute; //End time
	private int mLoopType = 1; //schedule loop type, index start at 1
	private int mNotificationType = 1; //Notifications
	private View view_back,view_add;
	private String flag = "false";//Analyzing last saved time, there is no saving minutes

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_create_schedule);
		mDbHelper = new DBHelper(getApplicationContext());
		db = mDbHelper.getReadableDatabase();
		
		if (savedInstanceState != null) {
			mOpenType = savedInstanceState.getString(OPEN_TYPE);
			mScheduleType = savedInstanceState.getString(SCHEDULE_TYPE);
			mScheduleId = savedInstanceState.getString(SCHEDULE_ID);
			mCurrentDate = savedInstanceState.getString(CURRENT_DATE); 
		} else if (getIntent() != null) {
			Intent intent = getIntent();
			mOpenType = intent.getStringExtra(OPEN_TYPE);
			mScheduleType = intent.getStringExtra(SCHEDULE_TYPE);
			mScheduleId = intent.getStringExtra(SCHEDULE_ID);
			mCurrentDate = getIntent().getStringExtra(CURRENT_DATE);
		}
		initView();
		super.onCreate(savedInstanceState);
	}

	public static void startCreateSchedule(Context context, String openType,
			String scheduleType,String scheduleId , String currentDate) {
		Intent intent = new Intent(context, CreateScheduleActivity.class);
		intent.putExtra(OPEN_TYPE, openType);
		intent.putExtra(SCHEDULE_TYPE, scheduleType);
		intent.putExtra(SCHEDULE_ID, scheduleId);
		intent.putExtra(CURRENT_DATE, currentDate);
		context.startActivity(intent);
		( (Activity)context).overridePendingTransition(R.anim.activity_bottom_in,0);
	}
	
	public static void startCreateSchedule(Context context, String openType,
			String scheduleType) {
		Intent intent = new Intent(context, CreateScheduleActivity.class);
		intent.putExtra(OPEN_TYPE, openType);
		intent.putExtra(SCHEDULE_TYPE, scheduleType);
		context.startActivity(intent);
	}

	private void initView() {
		
		((RelativeLayout)findViewById(R.id.relative_root)).setOnClickListener(this);
		mScrollView = getRateView(R.id.scrollview_content, true);
		mScrollView.setOnClickListener(this);
		mScrollView.setOnTouchListener(this);
		getRateView(R.id.relative_title_other, true);
		mLinearDelete = getRateView(R.id.linear_delete_schedule, true);
		getRateView(R.id.relative_notify_all_day, true).setOnClickListener(this);
		getRateView(R.id.relative_schedule_name, true).setOnClickListener(this);
		getRateView(R.id.relative_start_time, true).setOnClickListener(this);
		getRateView(R.id.relative_end_time, true).setOnClickListener(this);
		getRateView(R.id.relative_loop, true).setOnClickListener(this);
		getRateView(R.id.relative_notification, true).setOnClickListener(this);

		getRateView(R.id.img_back, true);
		view_back=getRateView(R.id.view_back, true);
		view_add=getRateView(R.id.view_add, true);
		mToggleNotificationAllDay = getRateView(R.id.radio_notify_all_day, true);

		mTxtTitle = getTextView(R.id.txt_title_name, true, 37,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		mTxtSave = getTextView(R.id.txt_operate, true, 33,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mTxtDelete = getTextView(R.id.txt_delete_mark, true, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mEditScheduleName = getTextView(R.id.edit_schedule_name, true, 30 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);

		getTextView(R.id.txt_notify_all_day_title, true, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		getTextView(R.id.txt_start_time_title, true, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR).setOnClickListener(this);
		mTxtStartDate = getTextView(R.id.txt_start_date, true, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mTxtStartTime = getTextView(R.id.txt_start_time, true, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		getTextView(R.id.txt_end_time_title, true, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR).setOnClickListener(this);
		mTxtEndDate = getTextView(R.id.txt_end_date, true, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mTxtEndTime = getTextView(R.id.txt_end_time, true, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		getTextView(R.id.txt_loop_title, true, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mTxtLoop = getTextView(R.id.txt_loop, true, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		getTextView(R.id.txt_notification_title, true, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mTxtNotification = getTextView(R.id.txt_notification, true, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);

		/* Setting Listener */
		view_add.setOnClickListener(new OnSaveButtonClickListener());
		mLinearDelete.setOnClickListener(new OnDeleteButtonClickListener());
		mTxtStartDate.setOnClickListener(this);
		mTxtStartTime.setOnClickListener(this);
		mTxtEndTime.setOnClickListener(this);
		view_back.setOnClickListener(this);
		mToggleNotificationAllDay.setOnCheckedChangeListener(this);
		
		/* The passed parameter setting control values and status */
		initContent();
	}

	/**
	 * Initialization parameters for incoming interface, access to schedule the start time and end time.
	 */
	private void initContent() {
		
		if (mScheduleType != null) {
			if (mScheduleType.equals(ScheduleType.NORMAL_SCHEDULE)) { //Normal schedule
				mTxtTitle.setText(getString(R.string.schedule_normal_create));
				mNotificationType = Integer.parseInt(NotificationType.NO_NOTIFICATION);
			} else if (mScheduleType.equals(ScheduleType.SEE_THE_DOCTER_SCHEDULE)) {  
				mTxtTitle.setText(getString(R.string.schedule_see_to_docter_create));
				mNotificationType = Integer.parseInt(NotificationType.ONE_DAY);
			}
		} 
		if (mOpenType != null) { 
			if (mOpenType.equals(OPEN_TYPE_CREATE)) {  
				mStartDate = DateFormatUtil.geDataLongZeroMinuteAndSeconed(
						DateFormatUtil.getLongDateBySplicing(mApp.getmCurrentDateLong(),System.currentTimeMillis() ));  
				mEndDate =  mStartDate + DateFormatUtil.ONE_HOUR ;   
				mTxtSave.setText(getString(R.string.schedule_state_create)); 
				mTxtDelete.setTextColor(getResources().getColor(R.color.gray_line));
				mLinearDelete.setClickable(false);
				mTxtNotification.setText(getResources().getStringArray(  
						R.array.schedule_notification_time_pick_array)[mNotificationType-1]);
			} else if (mOpenType.equals(OPEN_TYPE_EDIT)) {  
				mTxtSave.setText(getString(R.string.schedule_state_edit));
				mTxtDelete.setTextColor(getResources().getColor(R.color.pink));
				mLinearDelete.setClickable(true);
				mScheduleBean = DBOperator.queryScheduleById(db, mScheduleId);  
				if (mScheduleBean != null ) {
					if (mScheduleBean.getShunichi() == null|| mScheduleBean.getShunichi().equals(ScheduleAllDay.OFF)) {
						mStartDate = Long.parseLong(mScheduleBean.getStarttime());
						mEndDate = Long.parseLong(mScheduleBean.getEndtime()); 
						mStartDate = DateFormatUtil.getStringTwoDatemerge(Long.parseLong(mCurrentDate), mStartDate);
						mEndDate = DateFormatUtil.getStringTwoDatemerge(Long.parseLong(mCurrentDate), mEndDate);
					}else{
						mStartDate = DateFormatUtil.geDataLongZeroClick(Long.parseLong(mCurrentDate)); 
						mEndDate = DateFormatUtil.geDataLongZeroClick(Long.parseLong(mCurrentDate)+DateFormatUtil.ONE_DAY); 
					}
					fillEditContent();
				}
			}
		}
		
		mTxtStartDate.setText(DateFormatUtil.getStringDataDiagonal(mStartDate));  
		mTxtEndDate.setText(DateFormatUtil.getStringDataDiagonal(mStartDate));  
		
		if ((mStartDate-DateFormatUtil.geDataLongZeroClick(mStartDate))%DateFormatUtil.ONE_DAY == 0) { 
			mStartHourAndMinute = getString(R.string.schedule_start_time_zero);
		}else{
			mStartHourAndMinute = DateFormatUtil.getStringHourAndMinute(mStartDate); 
		}
		if ((mEndDate-DateFormatUtil.geDataLongZeroClick(mEndDate))%DateFormatUtil.ONE_DAY == 0) { 
			mEndHourAndMinute = getString(R.string.schedule_end_time_zero);
		}else{
			mEndHourAndMinute = DateFormatUtil.getStringHourAndMinute(mEndDate);
		}
		mTxtStartTime.setText(mStartHourAndMinute);  
		mTxtEndTime.setText(mEndHourAndMinute); 
		
		if(flag.equals("false")){
			 
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
	 * 编辑方式打开日程初始化
	 */
	private void fillEditContent() {
		mEditScheduleName.setText(mScheduleBean.getTitle());  
		mEditScheduleName.setSelection(mScheduleBean.getTitle().length());
		mLoopType = Integer.parseInt(mScheduleBean.getKurikaeshi());
		if (mLoopType >= 0 && mLoopType < 6) {
			if (mLoopType == 0 || mLoopType == 1) {
				mTxtLoop.setText(getString(R.string.schedule_default_repeat));
			}else{
				mTxtLoop.setText(getResources().getStringArray( 
						R.array.schedule_loop_interval_pick_array)[mLoopType-1]); 
			}
		}
		
		if (mScheduleBean.getShunichi().equals(ScheduleAllDay.OFF)) {  
			mToggleNotificationAllDay.setChecked(false);
			flag = "true";
			if (mNotificationType > 0 && mNotificationType < 9) {
				mTxtNotification.setText(getResources().getStringArray(  
						R.array.schedule_notification_time_pick_array)[Integer.parseInt(mScheduleBean.getTuchi())-1]);
			}
			mNotificationType = Integer.parseInt(mScheduleBean.getTuchi());
		}else{
			mToggleNotificationAllDay.setChecked(true);
			flag = "false";
			String[] notifyArr = getResources().getStringArray(R.array.schedule_notification_all_day_time_pick_array);
			switch (Integer.parseInt(mScheduleBean.getTuchi())) {
			case 0:
			case 1:
				mTxtNotification.setText(notifyArr[0]);
				break;
			case 5:
				mTxtNotification.setText(notifyArr[1]);
				break;
			case 7:
				mTxtNotification.setText(notifyArr[2]);
				break;
			case 9:
				mTxtNotification.setText(notifyArr[3]);
				break;

			default:
				break;
			}
			mNotificationType = Integer.parseInt(mScheduleBean.getTuchi());
		}
	}
	
	/**
	 * save button
	 */
	private class OnSaveButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View view) {
			
			if (MyUtils.isNull(mEditScheduleName.getText().toString())) {  
				CommonUtils.showToastMessage(CreateScheduleActivity.this, getString(R.string.schedule_name_is_null));
				return;
			}
			if (mStartDate >= mEndDate) {
				CommonUtils.showToastMessage(CreateScheduleActivity.this, getString(R.string.schedule_date_error));
				return;
			}
			final ScheduleBean scheduleBean = new ScheduleBean();
			scheduleBean.setType(mScheduleType);
			scheduleBean.setTitle(mEditScheduleName.getText().toString());
			scheduleBean.setSchedule_start_time(mStartDate+"");
			scheduleBean.setKurikaeshi(mLoopType+"");
			scheduleBean.setTuchi(mNotificationType+"");
			scheduleBean.setShunichi(mDateType);
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
						CreateScheduleActivity.this.sendBroadcast(intent);
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
							CreateScheduleActivity.this.sendBroadcast(intent);
						}
					}
					mApp.SetDialogAddEventDismiss(true);
					mApp.setShowCarlendarFragment(true);
					finishActivity();
				}else{
					if (mUpdateLoopDialog == null) {
						mUpdateLoopDialog = new GeneralListDialog(CreateScheduleActivity.this,
								GeneralListDialog.SCHEDULE_LOOP_UPDATE_DIALOG);
						mUpdateLoopDialog.setOnGenenalDialogClickListener(new OnGenenalDialogClickListener() {
							
							@Override
							public void OnGeneralSureTextClicked(int position) {

								boolean result = DBOperator.updateLoopScheduleChangeOther(db, mScheduleId, scheduleBean);
								if (mNotificationType!=0 && mNotificationType!=1) {
									if (result) {
										Intent intent=new Intent();
										intent.setAction("com.bravesoft.notifydata");
										CreateScheduleActivity.this.sendBroadcast(intent);
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
	 * 点击删除按钮
	 */
	private class OnDeleteButtonClickListener implements OnClickListener{

		@Override
		public void onClick(View view) {
			if (mScheduleBean.getKurikaeshi()==null ||  
					mScheduleBean.getKurikaeshi().equals(ScheduleLoopType.NO_LOOP)|| 
					mScheduleBean.getKurikaeshi().equals(ScheduleLoopType.NO_RECORD)) {
				if (mDeleteNoLoopDialog == null) { 
					mDeleteNoLoopDialog = new GeneralStringDialog(CreateScheduleActivity.this,
							getString(R.string.dialog_title_delete_schedule_no_loop),DialogType.DIALOG_SURE
							,new OnGenenalStringDialogClickListener() {

						@Override
						public void OnGeneralSureTextClicked() {
							if (DBOperator.deleteScheduleById(db, mScheduleId)) {
								mDeleteNoLoopDialog.dismiss();
								Intent intent=new Intent();
								intent.setAction("com.bravesoft.notifydata");
								CreateScheduleActivity.this.sendBroadcast(intent);
								finishActivity();
							}else{
								mDeleteNoLoopDialog.dismiss();
								CommonUtils.showToastMessage(CreateScheduleActivity.this,
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
					mDeleteLoopDialog = new GeneralListDialog(CreateScheduleActivity.this,
							GeneralListDialog.SCHEDULE_LOOP_DELETE_DIALOG);
					mDeleteLoopDialog.setOnGenenalDialogClickListener(new OnGenenalDialogClickListener() {
						
						@Override
						public void OnGeneralSureTextClicked(int position) {
							if (position == 0) { 
								if (DBOperator.deleteOneScheduleByIdLoop(db, mScheduleId,mStartDate)) {
									Intent intent=new Intent();
									intent.setAction("com.bravesoft.notifydata");
									CreateScheduleActivity.this.sendBroadcast(intent);
									finishActivity();
								}else{
									CommonUtils.showToastMessage(CreateScheduleActivity.this, 
											getString(R.string.schedule_operate_failed_message));
								}
							}else if (position == 1) {  
								if (DBOperator.deleteManyScheduleByIdLoop(db, mScheduleId,mStartDate)) {
									Intent intent=new Intent();
									intent.setAction("com.bravesoft.notifydata");
									CreateScheduleActivity.this.sendBroadcast(intent);
									finishActivity();
								}else{
									CommonUtils.showToastMessage(CreateScheduleActivity.this, 
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
		outState.putString(SCHEDULE_TYPE, mScheduleType);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void finish() {
		CommonUtils.exitKeyboard(this);
		super.finish();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.relative_notify_all_day:
		case R.id.relative_schedule_name:
		case R.id.relative_start_time:
		case R.id.relative_end_time:
		case R.id.relative_root:
		case R.id.scrollview_content:
		case R.id.txt_start_time_title:
		case R.id.txt_end_time_title:
			InputTools.HideKeyboard(mEditScheduleName);
			break;
		case R.id.view_back:
			finishActivity();
			overridePendingTransition(0,R.anim.activity_bottom_out);
			break;
		case R.id.relative_loop:
			InputTools.HideKeyboard(mEditScheduleName);
			if (mLoopTimePickDialog == null) {
				mLoopTimePickDialog = new GeneralListDialog(this,
						GeneralListDialog.SCHEDULE_LOOP_INTERVAL_PICK_DIALOG);
				mLoopTimePickDialog.setFocus(mLoopType-1);
				mLoopTimePickDialog
						.setOnGenenalDialogClickListener(new OnGenenalDialogClickListener() {

							@Override
							public void OnGeneralSureTextClicked(int position) {
								mLoopTimePickDialog.dismiss();
								if (position >= 0) {
									mLoopType = position + 1;
									if (position == 0) {
										mTxtLoop.setText(getString(R.string.schedule_default_repeat));
									}else{
										mTxtLoop.setText(getResources().
												getStringArray(R.array.schedule_loop_interval_pick_array)[position]);
									}
								}
							}

							@Override
							public void OnGeneralCancelTextClicked() {
								mLoopTimePickDialog.dismiss();
							}
						});
			}
			mLoopTimePickDialog.show();
			break;
		case R.id.relative_notification:
			InputTools.HideKeyboard(mEditScheduleName);
			if (mDateType.equals(ScheduleAllDay.ON)) {
				if (mNotificationAllDayTimePickDialog ==  null) {
					mNotificationAllDayTimePickDialog = new GeneralListDialog(
							this,GeneralListDialog.SCHEDULE_NOTIFICATION_ALL_DAY_TIME_PICK_DIALOG);
					switch (mNotificationType) {
						case 0:
						case 1:
							mNotificationAllDayTimePickDialog.setFocus(0);
							break;
						case 5:
							mNotificationAllDayTimePickDialog.setFocus(1);
							break;
						case 7:
							mNotificationAllDayTimePickDialog.setFocus(2);
							break;
						case 9:
							mNotificationAllDayTimePickDialog.setFocus(3);
							break;
	
						default:
							break;
					}
					mNotificationAllDayTimePickDialog.setOnGenenalDialogClickListener(new OnGenenalDialogClickListener() {
						
						@Override
						public void OnGeneralSureTextClicked(int position) {
							mTxtNotification.setText(getResources().
									getStringArray(R.array.schedule_notification_all_day_time_pick_array)[position]);
							switch (position) {
							case 0:
								mNotificationType = Integer.parseInt(NotificationType.NO_NOTIFICATION);
								break;
							case 1:
								mNotificationType = Integer.parseInt(NotificationType.ONE_HOUR);
								break;
							case 2:
								mNotificationType = Integer.parseInt(NotificationType.ONE_DAY);
								break;
							case 3:
								mNotificationType = Integer.parseInt(NotificationType.ONE_WEEK);
								break;

							default:
								break;
							}
							mNotificationAllDayTimePickDialog.dismiss();
						}
						
						@Override
						public void OnGeneralCancelTextClicked() {
							mNotificationAllDayTimePickDialog.dismiss();
						}
					});
				}
				mNotificationAllDayTimePickDialog.show();
				
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
				mTimePickDialogEnd = new TimePickDialog(this,mEndHourAndMinute);
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
		}else{
			mDateType =  ScheduleAllDay.OFF;
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

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_MOVE:
		case MotionEvent.ACTION_DOWN:
			InputTools.HideKeyboard(mEditScheduleName);
			break;

		default:
			break;
		}
		return false;
	}

}
