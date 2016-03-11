package com.bravesoft.riumachi.dialog;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.adapter.DatePickCalendarGridAdapter;
import com.bravesoft.riumachi.bean.CarlendarBean;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.layout.LayoutUtils;
import com.bravesoft.riumachi.util.DateCalculateUtils;
import com.bravesoft.riumachi.util.DateFormatUtil;

/**
 * Select time Dialog, return selected time 0:00:00 stamp.
 * 
 * @author wangyuanshi
 * 
 */
public class DatePickDialog extends BaseDialog implements OnClickListener,
		OnItemClickListener {

	private Context context;
	private TextView mtxtSure;
	private TextView mtxtCancel;
	private TextView mtxtCurrentYear;
	private TextView mtxtNowDate;
	private TextView mTxtPickedDate;
	private GridView mGridDatePick;
	private LinearLayout mLinearContent;
	private DatePickCalendarGridAdapter mAdapter;
	private GestureDetector mGesture;
	private OnDatePickDialogClickListener mOnDatePickDialogClickListener;
	private String mRecentlyDay;
	private String mShowDate,mShowWeek,mShowYear;//Date display
	// Current timestamp
	private long mCurrenrLongDate;
	// Current calendar timestamp
	public long mCurrenrCarlendarLongDate;
	private View mViewSure,mViewCancel;
	

	public DatePickDialog(Context context,long currentDate) {
		super(context, R.style.NobackDialog);
		this.mCurrenrLongDate = currentDate;
		mCurrenrLongDate = DateFormatUtil.geDataLongZeroClick(mCurrenrLongDate);//Current time 0:00:00
		mCurrenrCarlendarLongDate = DateFormatUtil.geDataLongZeroDay(mCurrenrLongDate);
		mRecentlyDay = DateFormatUtil.getStringDay(mCurrenrLongDate);
		this.context = context;
		init();
	}

	public DatePickDialog(Context context,
			OnDatePickDialogClickListener onDatePickDialogClickListener) {
		super(context, R.style.NobackDialog);
		this.context = context;
		mOnDatePickDialogClickListener = onDatePickDialogClickListener;
		init();

	}

	public void setOnDatePickDialogClickListener(
			OnDatePickDialogClickListener onDatePickDialogClickListener) {
		mOnDatePickDialogClickListener = onDatePickDialogClickListener;
	}

	private void init() {
		setContentView(initView());
		android.view.WindowManager.LayoutParams layoutParams = getWindow()
				.getAttributes();
		layoutParams.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
		layoutParams.height = layoutParams.width;
	}

	private View initView() {
//		mCurrenrLongDate = DateFormatUtil.geDataLongZeroClick(System.currentTimeMillis());//Current time 0:00:00
//		mCurrenrCarlendarLongDate = DateFormatUtil.geDataLongZeroDay(System.currentTimeMillis());//The first day of the current month 1:00:00
		mGesture = new GestureDetector(context,new GridViewGestureListener());
//		mCurrenrLongDate = System.currentTimeMillis();
//		mRecentlyDay = DateFormatUtil.getStringDay(mCurrenrLongDate);
		View currentView = LinearLayout.inflate(context,R.layout.dialog_date_pick, null);
		currentView.findViewById(R.id.view_dialog_setting_background).setOnClickListener(this);

		mLinearContent = (LinearLayout) currentView.findViewById(R.id.linear_content);
		mGridDatePick = (GridView) currentView.findViewById(R.id.grid_date_pick_dialog);
		mTxtPickedDate = (TextView) currentView.findViewById(R.id.txt_picked_date);
		LayoutUtils.rateScale(context, mLinearContent, true);
		LayoutUtils.rateScale(context, (RelativeLayout) currentView.findViewById(R.id.relative_bottom), true);
		LayoutUtils.rateScale(context,currentView.findViewById(R.id.horizotal_line_one), true);
		LayoutUtils.rateScale(context,currentView.findViewById(R.id.relative_pick_date_title), true);
		LayoutUtils.rateScale(context,currentView.findViewById(R.id.horizontal_line_two), true);
		LayoutUtils.rateScale(context,currentView.findViewById(R.id.horizotal_line_three), true);
		LayoutUtils.rateScale(context,currentView.findViewById(R.id.horizotal_line_four), true);
		LayoutUtils.rateScale(context,currentView.findViewById(R.id.txt_current_year), true);
		LayoutUtils.rateScale(context,currentView.findViewById(R.id.txt_current_date), true);
		LayoutUtils.rateScale(context,currentView.findViewById(R.id.linear_calendar_week), true);
		mtxtCancel = (TextView) currentView.findViewById(R.id.txt_cancel);
		mtxtSure = (TextView) currentView.findViewById(R.id.txt_sure);
		LayoutUtils.rateScale(context, mtxtSure , true);
		mtxtCurrentYear = (TextView) currentView.findViewById(R.id.txt_current_year);
		mtxtNowDate = (TextView) currentView.findViewById(R.id.txt_current_date);
		
		mViewSure = currentView.findViewById(R.id.view_sure);
		mViewCancel = currentView.findViewById(R.id.view_cancel);
		LayoutUtils.rateScale(context, mViewSure , true);
		LayoutUtils.rateScale(context, mViewCancel , true);
		mViewCancel.setOnClickListener(this);
		mViewSure.setOnClickListener(this);
	
		LayoutUtils.rateScale(context, mGridDatePick, true);
		LayoutUtils.rateScale(context, mtxtCurrentYear, true);
		LayoutUtils.rateScale(context, mtxtNowDate, true);
		LayoutUtils.rateScale(context, mTxtPickedDate, true);
		LayoutUtils.setTextSize(mtxtSure, 31,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.setTextSize(mtxtCancel, 31,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.setTextSize(mtxtCurrentYear, 32,TextTypeFace.TYPEFACE_ROBOTO_REGULAR);
		LayoutUtils.setTextSize(mtxtNowDate, 61,TextTypeFace.TYPEFACE_ROBOTO_BOLD);
		LayoutUtils.setTextSize(mTxtPickedDate, 28,TextTypeFace.TYPEFACE_ROBOTO_BOLD);
		LayoutUtils.setTextSize((TextView) currentView.findViewById(R.id.txt_sunday), 26,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.setTextSize((TextView) currentView.findViewById(R.id.txt_monday), 26,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.setTextSize((TextView) currentView.findViewById(R.id.txt_tuesday), 26,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.setTextSize((TextView) currentView.findViewById(R.id.txt_wednesday), 26,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.setTextSize((TextView) currentView.findViewById(R.id.txt_thursday), 26,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.setTextSize((TextView) currentView.findViewById(R.id.txt_friday), 26,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.setTextSize((TextView) currentView.findViewById(R.id.txt_saturday), 26,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);

		/* Setting Listener */
		mGridDatePick.setOnItemClickListener(this);
		mLinearContent.setOnClickListener(this);
		mtxtCancel.setOnClickListener(this);
		mtxtSure.setOnClickListener(this);

		mGridDatePick.setOnTouchListener(new GridViewOnTouchListener());
		mAdapter = new DatePickCalendarGridAdapter(context,
				DateCalculateUtils.getCalendarData(mCurrenrCarlendarLongDate),
				DateCalculateUtils.getDistanceWithSunday(mCurrenrCarlendarLongDate));
		mGridDatePick.setAdapter(mAdapter);
		mAdapter.setFocus(mRecentlyDay);
		
		/* Setting initialization state  */
		mtxtCurrentYear.setText(DateCalculateUtils.getDefaultCurrentYear()+"");
		mTxtPickedDate.setText(DateCalculateUtils.getDefaultDay()+"");
		changeDate(mCurrenrLongDate);
		
		return currentView;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.view_dialog_setting_background:
			dismiss();
			break;
		case R.id.view_cancel:
			if (mOnDatePickDialogClickListener != null) {
				mOnDatePickDialogClickListener.OnDatePickDialogCancelClicked();
			}
			break;
		case R.id.view_sure:
			if (mOnDatePickDialogClickListener != null) {
				mOnDatePickDialogClickListener.OnDatePickDialogSureClicked(mCurrenrLongDate+"");
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void show() {
		super.show();
	}

 
	private static final int SWIPE_MIN_DISTANCE = LayoutUtils
			.getRate4densityH(40);

	private class GridViewGestureListener extends
			GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {

			try {
				if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE) {
					 mCurrenrCarlendarLongDate = DateFormatUtil
								.addOneMonthLongData(mCurrenrCarlendarLongDate);
					 refrashCarlendar();
					 checkSelectPosition();
					return true;

				} else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE) {
					mCurrenrCarlendarLongDate = DateFormatUtil
							.subOneMonthLongData(mCurrenrCarlendarLongDate);
					 refrashCarlendar();
					 checkSelectPosition();
					return true;

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		}
		
		private void checkSelectPosition() {
			if (DateFormatUtil.geDataLongZeroDay(mCurrenrLongDate) == 
					DateFormatUtil.geDataLongZeroDay(mCurrenrCarlendarLongDate)) {
				mAdapter.setFocusDate(mCurrenrLongDate);
			}else{
				mAdapter.setFocus(-1);
			}
		}

	}
	
	private class GridViewOnTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			return mGesture.onTouchEvent(event);
		}

	}
	
	/**
	 * refrashCarlendar
	 */
	private void refrashCarlendar() {
		mAdapter.setData(DateCalculateUtils.getCalendarData(mCurrenrCarlendarLongDate),
				DateCalculateUtils.getDistanceWithSunday(mCurrenrCarlendarLongDate));
		mTxtPickedDate.setText(DateFormatUtil.getStringDatayyyyMM(mCurrenrCarlendarLongDate));
		
		mAdapter.setFocus(mRecentlyDay);
		
	}

	/**
	 * Callback interface, click OK, and click on the callback is canceled
	 */
	public interface OnDatePickDialogClickListener {
		public void OnDatePickDialogSureClicked(String date);

		public void OnDatePickDialogCancelClicked();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		mAdapter.setFocus(position);
		mCurrenrLongDate = DateFormatUtil.getLongDataByArray(mCurrenrCarlendarLongDate,
				((CarlendarBean) mAdapter.getItem(position)).getDate());
		
		changeDate(mCurrenrLongDate);
	}
	/*
	 * To display the date format conversion
	 */
	public void changeDate(long mCurrenrLongDate){
		mShowDate=DateFormatUtil.getStringMonthByLong(mCurrenrLongDate);
		mShowWeek=DateFormatUtil.getStringWeekByLong(mCurrenrLongDate);
		mShowYear=DateFormatUtil.getStringYearByLong(mCurrenrLongDate);
		mtxtCurrentYear.setText(mShowYear);
		mtxtNowDate.setText(mShowDate+mShowWeek);
	}
	
}
