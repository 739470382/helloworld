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
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.adapter.DatePickCalendarGridAdapter;
import com.bravesoft.riumachi.bean.CarlendarBean;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.layout.LayoutUtils;
import com.bravesoft.riumachi.util.DateCalculateUtils;
import com.bravesoft.riumachi.util.DateFormatUtil;

/**
 * 
 * @author wangyuanshi
 * 
 */
public class TimePickDialog extends BaseDialog implements OnClickListener,OnTimeChangedListener {

	private Context context;
	private TextView mtxtSure;
	private TextView mtxtCancel;
	private TimePicker mTimePick;
	private LinearLayout mLinearContent;
	private OnTimePickDialogClickListener mOnTimePickDialogClickListener;
	private int mCurrenrHour;
	private int mCurrenrMinute;
	private long mCurrenrLongDate;
	private View mViewSure,mViewCancel;

	public TimePickDialog(Context context,String date) {
		super(context, R.style.NobackDialog);
		this.context = context;
		init();
		setCurrentTime(date);
	}
	
	public TimePickDialog(Context context ) {
		super(context, R.style.NobackDialog);
		this.context = context;
		init();
	}

	public TimePickDialog(Context context,
			OnTimePickDialogClickListener onDatePickDialogClickListener) {
		super(context, R.style.NobackDialog);
		this.context = context;
		this.mOnTimePickDialogClickListener = onDatePickDialogClickListener;
		init();

	}

	public void setOnTimePickDialogClickListener(
			OnTimePickDialogClickListener onDatePickDialogClickListener) {
		this.mOnTimePickDialogClickListener = onDatePickDialogClickListener;
	}
	
	public void setCurrentTime(String date) {
		String[] arr = date.split(":");
		if (arr!= null &&arr.length >= 2) {
			this.mCurrenrHour = Integer.parseInt(arr[0]);
			this.mCurrenrMinute = Integer.parseInt(arr[1]);
			if (mTimePick != null) {
				System.out.println("call le ");
				mTimePick.setCurrentHour(mCurrenrHour); 
				mTimePick.setCurrentMinute(mCurrenrMinute); 
			}
		}
	}

	private void init() {
		setContentView(initView());
		android.view.WindowManager.LayoutParams layoutParams = getWindow()
				.getAttributes();
		layoutParams.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
		layoutParams.height = layoutParams.width;
	}

	private View initView() {
//		mCurrenrLongDate = System.currentTimeMillis();
//		String[] dateArr = DateFormatUtil.getArrayWholeData(mCurrenrLongDate);
//		if (dateArr.length >= 4) {
//			mCurrenrHour = Integer.parseInt(dateArr[3]);
//			mCurrenrMinute = Integer.parseInt(dateArr[4]);
//		}
		View currentView = LinearLayout.inflate(context,R.layout.dialog_time_pick, null);
		currentView.findViewById(R.id.view_dialog_setting_background).setOnClickListener(this);
		mTimePick = (TimePicker) currentView.findViewById(R.id.timepick_dialog); 
		mLinearContent = (LinearLayout) currentView.findViewById(R.id.linear_content);
		LayoutUtils.rateScale(context, mLinearContent , true);
		 LayoutUtils.rateScale(context, (LinearLayout) currentView.findViewById(R.id.lin_top), true);
		LayoutUtils.rateScale(context, (RelativeLayout) currentView.findViewById(R.id.relative_bottom), true);
		LayoutUtils.rateScale(context,currentView.findViewById(R.id.horizotal_line_one), true);
		LayoutUtils.rateScale(context,currentView.findViewById(R.id.horizotal_line_two), true);
		LayoutUtils.rateScale(context,currentView.findViewById(R.id.horizotal_line_three), true);
		LayoutUtils.rateScale(context,mTimePick, true);
		mtxtCancel = (TextView) currentView.findViewById(R.id.txt_cancel);
		mtxtSure = (TextView) currentView.findViewById(R.id.txt_sure);
		LayoutUtils.setTextSize((TextView)currentView.findViewById(R.id.txt_time_pick_title), 35,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.setTextSize(mtxtSure, 31,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.setTextSize(mtxtCancel, 31,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		
		mViewSure = currentView.findViewById(R.id.view_sure);
		mViewCancel = currentView.findViewById(R.id.view_cancel);
		LayoutUtils.rateScale(context, mViewSure , true);
		LayoutUtils.rateScale(context, mViewCancel , true);
		mViewCancel.setOnClickListener(this);
		mViewSure.setOnClickListener(this);
		LayoutUtils.rateScale(context, mtxtSure , true);
		mLinearContent.setOnClickListener(this);
		mtxtCancel.setOnClickListener(this);
		mtxtSure.setOnClickListener(this);
		mTimePick.setOnTimeChangedListener(this);

		
		 
		mTimePick.setIs24HourView(true);
		return currentView;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.view_dialog_setting_background:
			dismiss();
			break;
		case R.id.linear_content:
			break;
		case R.id.view_cancel:
			if (mOnTimePickDialogClickListener != null) {
				mOnTimePickDialogClickListener.OnTimePickDialogCancelClicked();
			}
			break;
		case R.id.view_sure:
			if (mOnTimePickDialogClickListener != null) {
				mOnTimePickDialogClickListener.OnTimePickDialogSureClicked(getCurrentTime());
			}
			break;

		default:
			break;
		}
	}

	private String getCurrentTime() {
		String hour = mCurrenrHour < 10 ? "0"+ mCurrenrHour : mCurrenrHour+"";
		String minute = mCurrenrMinute < 10 ? "0"+ mCurrenrMinute : mCurrenrMinute+"";
		return hour +":"+ minute;
	}

	@Override
	public void show() {
		System.out.println("call me "+mCurrenrHour+"=="+mCurrenrMinute);
		mTimePick.setCurrentHour(mCurrenrHour); 
		mTimePick.setCurrentMinute(mCurrenrMinute); 
		super.show();
	}


	/**
	 * Callback interface, click OK, and click on the callback is canceled
	 */
	public interface OnTimePickDialogClickListener {
		public void OnTimePickDialogSureClicked(String date);

		public void OnTimePickDialogCancelClicked();
	}


	
	
	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		mCurrenrHour = hourOfDay;
		mCurrenrMinute = minute;
	}


}
