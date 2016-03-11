package com.bravesoft.riumachi.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.bean.ScheduleBean;
import com.bravesoft.riumachi.constant.MedicineType;
import com.bravesoft.riumachi.constant.ScheduleAllDay;
import com.bravesoft.riumachi.constant.ScheduleType;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.database.DBHelper;
import com.bravesoft.riumachi.database.DBOperator;
import com.bravesoft.riumachi.layout.LayoutUtils;
import com.bravesoft.riumachi.util.DateFormatUtil;

public class ScheduleDetailActivity extends BaseActivity implements
		OnClickListener {

	public static final String SCHEDULE_ID = "schedule_id";
	public static final String CURRENT_DATE = "current_date";
	private TextView mTxtTitle;
	private TextView mTxtEditMark;
	private TextView mTxtScheduleTitle;
	private TextView mTxtScheduleDate;
	private TextView mTxtScheduleTime;
	private ImageView mImgScheduleMark;
	private LinearLayout mLinearLayoutContent;
	private String mScheduleId;
	private String mScheduleType;
	private ScheduleBean mScheduleBean;
	private String mCurrentDate;
	private String mStartDate;
	private String mEndDate;
	private SQLiteDatabase db;
	private DBHelper mDbHelper;
	private View view_back,view_add;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mDbHelper = new DBHelper(getApplicationContext());
		db = mDbHelper.getReadableDatabase();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_edit);
		if (savedInstanceState != null) {
			mScheduleId = savedInstanceState.getString(SCHEDULE_ID);
			mCurrentDate = savedInstanceState.getString(CURRENT_DATE); 
		}else if (getIntent() != null) {
			mScheduleId = getIntent().getStringExtra(SCHEDULE_ID);
			mCurrentDate = getIntent().getStringExtra(CURRENT_DATE);
		}
		initView();

	}

	public static void startEditScheduleActivity(Context context,String currentDate, String id ) {
		Intent intent = new Intent(context, ScheduleDetailActivity.class);
		intent.putExtra(SCHEDULE_ID, id);
		intent.putExtra(CURRENT_DATE, currentDate);
		context.startActivity(intent);
	}

	private void initView() {
		getRateView(R.id.relative_title_other, true);
		getRateView(R.id.img_back, true);
		view_back=getRateView(R.id.view_back, true);
		view_add=getRateView(R.id.view_add, true);
		getRateView(R.id.horizontal_line_one, true);
		mImgScheduleMark = getRateView(R.id.img_schedule_type_mark, true);

		mTxtTitle = getTextView(R.id.txt_title_name, true, 37 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		mTxtEditMark = getTextView(R.id.txt_operate, true, 33 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mTxtScheduleTitle = getTextView(R.id.txt_schedule_title, true, 35 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		mTxtScheduleDate = getTextView(R.id.txt_schedule_date, true, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mTxtScheduleTime = getTextView(R.id.txt_schedule_time, true, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mTxtScheduleTime.setVisibility(View.GONE);
		mLinearLayoutContent = getRateView(R.id.linear_schedule_edit, true);
		
		 
		view_add.setOnClickListener(this);
		view_back.setOnClickListener(this);

		 
		initContent();

	}

	private void initContent() {
		List<String> data = new ArrayList<String>();
		mTxtEditMark.setText(R.string.schedule_edit_title);
		mScheduleBean = DBOperator.queryScheduleById(db, mScheduleId);
		if (mScheduleBean == null) {
			return;
		}
		mTxtScheduleDate.setText(DateFormatUtil.getStringDateWithWeek(mCurrentDate));
		data.add(DateFormatUtil.getStringDateWithWeek(mCurrentDate));
		mStartDate = mScheduleBean.getStarttime();
		mEndDate = mScheduleBean.getEndtime();
		if (mScheduleBean.getShunichi() == null|| mScheduleBean.getShunichi().equals(ScheduleAllDay.OFF)) {
			mTxtScheduleTime.setVisibility(View.VISIBLE);
			mTxtScheduleTime.setText(DateFormatUtil.getStringTwoDateDistance(mStartDate , mEndDate));
			data.add(DateFormatUtil.getStringTwoDateDistance(mStartDate , mEndDate));
		}else{
			mTxtScheduleTime.setVisibility(View.GONE);
			mStartDate = DateFormatUtil.geDataLongZeroClick(Long.parseLong(mStartDate))+"";
			mEndDate = DateFormatUtil.geDataLongZeroClick(Long.parseLong(mEndDate))+"";
		}
		mTxtScheduleTitle.setText(mScheduleBean.getTitle());
		mScheduleType = mScheduleBean.getType();
		if (mScheduleType.equals(ScheduleType.NORMAL_SCHEDULE)) {
			mTxtTitle.setText(R.string.schedule_edit_normal);
			mImgScheduleMark.setBackgroundResource(R.drawable.icon_calendar);
		} else if (mScheduleType.equals(ScheduleType.SEE_THE_DOCTER_SCHEDULE)) {
			mTxtTitle.setText(R.string.schedule_edit_see_to_doctor);
			mImgScheduleMark.setBackgroundResource(R.drawable.icon_see_the_docter);
		} else if (mScheduleType.equals(ScheduleType.MEDICINE_SCHEDULE)) {
			mTxtTitle.setText(R.string.schedule_edit_medicine);
			String medicineType = mScheduleBean.getMedicalType();
			if (medicineType.equals(MedicineType.ANTIBIOTIC)) {
				mImgScheduleMark.setBackgroundResource(R.drawable.icon_medicine_a);
			}else if (medicineType.equals(MedicineType.BIOLOGCAL_AGENT)) {
				mImgScheduleMark.setBackgroundResource(R.drawable.icon_medicine_b);
			}if (medicineType.equals(MedicineType.OTHER_MEDICINE)) {
				mImgScheduleMark.setBackgroundResource(R.drawable.icon_medicine_c);
			}
		}
		setData(data);
	}

	private void setData(List<String> data) {
		for (int i = 0; i < 45; i++) {
			View view = LayoutInflater.from(this).inflate(R.layout.item_edit_view_list, null);
			TextView txtContent = (TextView) view.findViewById(R.id.txt_mycard_content);
			RelativeLayout mRelativeLayout = (RelativeLayout) view.findViewById(R.id.relative_my_card_item);
			LayoutUtils.setTextSize( txtContent, 28);
			LayoutUtils.rateScale(this, txtContent, true);
			LayoutUtils.rateScale(this, mRelativeLayout, true);
			if (data != null && i >= 0 && i < data.size()) {
				txtContent.setText(data.get(i));
			}else{
				txtContent.setText("");
			}
			mLinearLayoutContent.addView(view);
		}
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.view_back:
			finish();
			overridePendingTransition(R.anim.activityout, R.anim.activityin);
			break;
		case R.id.view_add:
			if (mScheduleType.equals(ScheduleType.NORMAL_SCHEDULE)) {
				CreateScheduleActivity.startCreateSchedule(this,
						CreateScheduleActivity.OPEN_TYPE_EDIT,
						ScheduleType.NORMAL_SCHEDULE , mScheduleId,mCurrentDate);
			} else if (mScheduleType.equals(ScheduleType.SEE_THE_DOCTER_SCHEDULE)) {
				CreateScheduleActivity.startCreateSchedule(this,
						CreateScheduleActivity.OPEN_TYPE_EDIT,
						ScheduleType.SEE_THE_DOCTER_SCHEDULE , mScheduleId ,mCurrentDate);
			} else if (mScheduleType.equals(ScheduleType.MEDICINE_SCHEDULE)) {
				CreateMedicineScheduleActivity.startCreateMedicineScheduleActivity(
						this, CreateMedicineScheduleActivity.OPEN_TYPE_EDIT,mScheduleId,mCurrentDate);
			}
			//overridePendingTransition(R.anim.activity_bottom_in,R.anim.activity_bottom_out);
			finishActivity();
			 overridePendingTransition(R.anim.activity_bottom_in,R.anim.activity_bottom_out);
			
			//overridePendingTransition(R.anim.activity_bottom_in,0);
			break;
		default:
			break;
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(SCHEDULE_ID, mScheduleId);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onDestroy() {
		if (db != null && db.isOpen()) {
			db.close();
		}
		super.onDestroy();
	}
}
