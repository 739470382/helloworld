package com.bravesoft.riumachi.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.activity.EditSmyptomActivity;
import com.bravesoft.riumachi.activity.HAQActivity;
import com.bravesoft.riumachi.activity.MainActivity;
import com.bravesoft.riumachi.activity.ScheduleDetailActivity;
import com.bravesoft.riumachi.activity.SymptomAddActivity;
import com.bravesoft.riumachi.activity.VASActivity;
import com.bravesoft.riumachi.adapter.CalendarAdapter;
import com.bravesoft.riumachi.adapter.DailyEventAdapter;
import com.bravesoft.riumachi.appliction.App;
import com.bravesoft.riumachi.bean.CarlendarBean;
import com.bravesoft.riumachi.bean.HaqBean;
import com.bravesoft.riumachi.bean.HaqVasBean;
import com.bravesoft.riumachi.bean.SymptomMemoBean;
import com.bravesoft.riumachi.bean.ScheduleBean;
import com.bravesoft.riumachi.bean.VasBean;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.database.DBHelper;
import com.bravesoft.riumachi.database.DBOperator;
import com.bravesoft.riumachi.database.VasDBOperator;
import com.bravesoft.riumachi.layout.LayoutUtils;
import com.bravesoft.riumachi.util.CommonUtils;
import com.bravesoft.riumachi.util.DateCalculateUtils;
import com.bravesoft.riumachi.util.DateFormatUtil;

@SuppressLint("ValidFragment")
public class CalendarFragment extends BaseFragment implements OnClickListener {

	private static final String TAG = "CalendarFragment";
	private static final String CURRENT_LONG_DATE = "current_long_date";
	private static final String CALENDAR_LONG_DATE = "calendar_long_date";
	private View currentView;
	private GridView mGridCalendar;
	private ListView mListView;
	private View mLinearNoRecordTip;
	private CalendarAdapter mCalendarAdapter;
	private DailyEventAdapter mDailyEventAdapter;
	private CalendarFragemntListener mCalendarFragemntListener;
	private GestureDetector mGesture;
	private SQLiteDatabase db;
	private App mApp;
	private int SYMTOM_BUTTON_TODAY_HAD = 1;
	private int SYMTOM_BUTTON_TODAY_NOHAD = 2;
	private int SYMTOM_BUTTON_SELECTEDAY_HAD = 3;
	private int SYMTOM_BUTTON_SELECTEDAY_NOHAD = 4;
	public static final String SMYPTOM_ID = "smyptom_id";
	// Current timestamp
	public long mCurrenrLongDate;
	// Current calendar timestamp
	public long mCurrenrCarlendarLongDate;
	// Recently date Day
	public String mRecentlyDay;
	private boolean firstIn = true;

	private RelativeLayout mrel_vas, mrel_haq, mrel_memo;

	private TextView mVAS;
	private TextView mHAQ;
	private ImageView mImgeMemo;

	private TextView mTxtVasCount; // vascount  textview
	private TextView mTxtHaqCount; // haqcount  textview
	private TextView mTxtMemoTitle;
	private int symtopbuttonstate = 0;
	private boolean is2VasFragment = false;
	private boolean is2HaqFragment = false;

	List<HaqVasBean> mLsmHaqVasBean = new ArrayList<HaqVasBean>();// To get data from the database to store vas
	// List<HaqBean> mLshaqBean = new ArrayList<HaqBean>();//Store to get data from the database haq
	private static SymptomMemoBean symptomMemoBean = new SymptomMemoBean();// Storing query results based on the date of symptom Memo

	public CalendarFragment() {

	}

	public CalendarFragment(CalendarFragemntListener calendarFragemntListener) {
		this.mCalendarFragemntListener = calendarFragemntListener;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mApp = App.getInstance();
		if (savedInstanceState!=null) {
			mCurrenrLongDate = DateFormatUtil.geDataLongZeroClick(
					savedInstanceState.getLong(CURRENT_LONG_DATE));//Cache time of 0:00:00
			mCurrenrCarlendarLongDate = DateFormatUtil.geDataLongZeroDay(
					savedInstanceState.getLong(CALENDAR_LONG_DATE));//Cache time 1:00:00
		}else{
			mCurrenrLongDate = DateFormatUtil.geDataLongZeroClick(System.currentTimeMillis());//Current time 0:00:00
			mCurrenrCarlendarLongDate = DateFormatUtil.geDataLongZeroDay(System.currentTimeMillis());//The first day of the current month 1:00:00
		}
		mApp.setmCurrentDateLong(mCurrenrLongDate);//Cache current date
		db = ((MainActivity) getActivity()).getDb();
		currentView = inflater.inflate(R.layout.fragment_calendar, container,
				false);
		mGesture = new GestureDetector(getActivity(),
				new GridViewGestureListener());
		initView();
		return currentView;
	}

	private void initView() {

		mRecentlyDay = DateFormatUtil.getStringDay(mCurrenrLongDate);
		mGridCalendar = (GridView) currentView
				.findViewById(R.id.grid_calendar_fragment);
		mListView = (ListView) currentView
				.findViewById(R.id.list_calendar_fragment);
		LinearLayout mLinearWeek = (LinearLayout) currentView
				.findViewById(R.id.linear_calendar_week);
		View linearCalendarEvaluate = currentView
				.findViewById(R.id.linear_calendar_evaluate);
		mLinearNoRecordTip = currentView
				.findViewById(R.id.linear_no_record_calendar_bottom);
		mImgeMemo = (ImageView) currentView.findViewById(R.id.image_memo);

		mTxtVasCount = (TextView) currentView.findViewById(R.id.txt_vas_count);
		mTxtHaqCount = (TextView) currentView.findViewById(R.id.txt_haq_count);
		mTxtMemoTitle = (TextView) currentView
				.findViewById(R.id.txt_memo_title);
		mVAS = (TextView) currentView.findViewById(R.id.txt_vas_title);
		mHAQ = (TextView) currentView.findViewById(R.id.txt_haq_title);

		// LayoutUtils.rateScale(getActivity(), mImgeMemo, true);
		mLinearNoRecordTip = currentView
				.findViewById(R.id.linear_no_record_calendar_bottom);

		mTxtVasCount = (TextView) currentView.findViewById(R.id.txt_vas_count);
		mTxtHaqCount = (TextView) currentView.findViewById(R.id.txt_haq_count);
		mVAS = (TextView) currentView.findViewById(R.id.txt_vas_title);
		mHAQ = (TextView) currentView.findViewById(R.id.txt_haq_title);
		LayoutUtils.setTextSize(mVAS, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		LayoutUtils.rateScale(getActivity(), mVAS, true);
		LayoutUtils.setTextSize(mHAQ, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);     
		LayoutUtils.rateScale(getActivity(), mHAQ, true);

		LayoutUtils.setTextSize(mTxtHaqCount, 40,TextTypeFace.TYPEFACE_ROBOTO_BOLD);
		LayoutUtils.rateScale(getActivity(), mTxtHaqCount, true);
		LayoutUtils.setTextSize(mTxtVasCount, 40,TextTypeFace.TYPEFACE_ROBOTO_BOLD);
		LayoutUtils.rateScale(getActivity(), mTxtVasCount, true);
		LayoutUtils.setTextSize(mTxtMemoTitle, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		// LayoutUtils.rateScale(getActivity(), mTxtMemoTitle, true);

		mrel_memo = (RelativeLayout) currentView.findViewById(R.id.rel_memo);
		mrel_vas = (RelativeLayout) currentView.findViewById(R.id.rel_vas);
		mrel_haq = (RelativeLayout) currentView.findViewById(R.id.rel_haq);

		LayoutUtils.rateScale(getActivity(), mrel_vas, true);
		LayoutUtils.rateScale(getActivity(), mrel_haq, true);
		LayoutUtils.rateScale(getActivity(), mrel_memo, true);

		mrel_memo.setOnClickListener(this);
		mrel_vas.setOnClickListener(this);
		mrel_haq.setOnClickListener(this);

		LayoutUtils.rateScale(getActivity(), mGridCalendar, true);
		LayoutUtils.rateScale(getActivity(), mLinearWeek, true);
		LayoutUtils.rateScale(getActivity(), linearCalendarEvaluate, true);

		LayoutUtils.rateScale(getActivity(),
				currentView.findViewById(R.id.relative_no_record_bottom_one),
				true);
		LayoutUtils.rateScale(getActivity(),
				currentView.findViewById(R.id.relative_no_record_bottom_two),
				true);
		LayoutUtils.rateScale(getActivity(),
				currentView.findViewById(R.id.relative_no_record_bottom_three),
				true);
		LayoutUtils.rateScale(getActivity(),
				currentView.findViewById(R.id.relative_no_record_bottom_four),
				true);
		LayoutUtils.rateScale(getActivity(),
				currentView.findViewById(R.id.relative_no_record_bottom_five),
				true);
		LayoutUtils.rateScale(getActivity(),
				currentView.findViewById(R.id.relative_no_record_bottom_six),
				true);
		LayoutUtils.rateScale(getActivity(),
				currentView.findViewById(R.id.relative_no_record_bottom_seven),
				true);
		LayoutUtils.setTextSize((TextView) currentView
				.findViewById(R.id.txt_schedule_no_record), 31,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		LayoutUtils.setTextSize((TextView) currentView
				.findViewById(R.id.txt_sunday), 16,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		LayoutUtils.setTextSize((TextView) currentView
				.findViewById(R.id.txt_monday), 16,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		LayoutUtils.setTextSize((TextView) currentView
				.findViewById(R.id.txt_tuesday), 16,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		LayoutUtils.setTextSize((TextView) currentView
				.findViewById(R.id.txt_wednesday), 16,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		LayoutUtils.setTextSize((TextView) currentView
				.findViewById(R.id.txt_thursday), 16,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		LayoutUtils.setTextSize((TextView) currentView
				.findViewById(R.id.txt_friday), 16,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		LayoutUtils.setTextSize((TextView) currentView
				.findViewById(R.id.txt_saturday), 16,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		

		mGridCalendar.setOnItemClickListener(new MyGridItemClickListener());
		mGridCalendar.setOnTouchListener(new GridViewOnTouchListener());
		mCalendarAdapter = new CalendarAdapter(getActivity(),
				DBOperator.queryScheduleListGetCalendar(db,
						mCurrenrCarlendarLongDate + ""),
				DateCalculateUtils
						.getDistanceWithSunday(mCurrenrCarlendarLongDate));

		mGridCalendar.setAdapter(mCalendarAdapter);

		setCalendarFocus(mRecentlyDay);

		mDailyEventAdapter = new DailyEventAdapter(getActivity(), null);
		mListView.setOnItemClickListener(new MyListItemClickListener());
		mListView.setAdapter(mDailyEventAdapter);

		if (mCalendarFragemntListener != null) {
			mCalendarFragemntListener.onDateHasChanged(DateCalculateUtils
					.getDefaultDay());
		}

		setVasData(DateFormatUtil.getStringData(Long.parseLong(System
				.currentTimeMillis() + "")));  
		setHaqData(DateFormatUtil.getStringData(Long.parseLong(System
				.currentTimeMillis() + "")));

		setSymptomstate(DateFormatUtil.geDataLongZeroClick(mCurrenrLongDate)
				+ "");

	}

	private List<ScheduleBean> getDailyEventData(String currentDate) {
		return DBOperator.queryScheduleListByDateOrderByDate(db, currentDate);
	}

	@Override
	protected View findViewById(int id) {
		return currentView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	public interface CalendarFragemntListener {
		public void onDateHasChanged(String date);
	}

	private class GridViewOnTouchListener implements OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {

			return mGesture.onTouchEvent(event);
		}
	}

	// Analyzing gestures with
	private static final int SWIPE_MIN_DISTANCE = LayoutUtils
			.getRate4densityH(40);

	/**
	 * Sliding calendar listening
	 */
	class GridViewGestureListener extends
			GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			
			try {
				
				if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY() - e2.getY())) {
					return true;
				}
				
				if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE) {//top
					mCurrenrCarlendarLongDate = DateFormatUtil
							.addOneMonthLongData(mCurrenrCarlendarLongDate);
					setCarlendarGrid(); 
					checkSelectPosition(); 
					return true;

				} else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE) {//under

					mCurrenrCarlendarLongDate = DateFormatUtil
							.subOneMonthLongData(mCurrenrCarlendarLongDate);
					setCarlendarGrid(); 
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
				mCalendarAdapter.setFocusDate(mCurrenrLongDate);
			}else{
				mCalendarAdapter.setFocus(-1);
			}
		}

	}

	
	/**
	 * Schedule List Listener
	 */
	private class MyListItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			ScheduleDetailActivity.startEditScheduleActivity(getActivity(),
					mCurrenrLongDate + "", mDailyEventAdapter.getItem(position)
							.getId() + "");
			getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}

	}

	/**
	 * Select the calendar day
	 */
	private class MyGridItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (mCalendarAdapter.isLegalPostion(position)) {
				mRecentlyDay = ((CarlendarBean) mCalendarAdapter.getItem(position)).getDate();
				setCalendarFocus(mRecentlyDay);
				mCurrenrLongDate = DateFormatUtil.getLongDataByArray(mCurrenrCarlendarLongDate,
						((CarlendarBean) mCalendarAdapter.getItem(position)).getDate());
				mApp.setmCurrentDateLong(mCurrenrLongDate);
				setRefrashDailyEventList();
				setRefrashStateBar();
			}
		}

	}

	/**
	 * Set calendar selected focus
	 */
	private void setCalendarFocus(String recentlyDay) {
		if (mCalendarAdapter != null) {
			mCalendarAdapter.setFocus(mRecentlyDay);
		}
	}
	
	
	@Override
	public void onClick(View view) {
		Intent intent;
		switch (view.getId()) {
		case R.id.rel_memo:
			if (symtopbuttonstate == SYMTOM_BUTTON_TODAY_NOHAD) {
				intent = new Intent(getActivity(), SymptomAddActivity.class);
				intent.putExtra("open_type", "open_type_create");
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.activity_bottom_in,R.anim.activity_bottom_out);
			} else if (symtopbuttonstate == SYMTOM_BUTTON_TODAY_HAD) {
				if (symptomMemoBean == null) {
					symptomMemoBean = DBOperator.querySymptomMemoBytime(db,
							mCurrenrLongDate + "");
					// 
					// datalist = adapter.datalist;

				}
				((MainActivity)getActivity()).intentToMemoListFragemnt();
//				intent = new Intent(getActivity(), EditSmyptomActivity.class);
//				intent.putExtra(SMYPTOM_ID, symptomMemoBean.getId() + "");
//				startActivity(intent);

			} else if (symtopbuttonstate == SYMTOM_BUTTON_SELECTEDAY_HAD) {

				if (symptomMemoBean == null) {
					symptomMemoBean = DBOperator.querySymptomMemoBytime(db,
							mCurrenrLongDate + "");

				}
				((MainActivity)getActivity()).intentToMemoListFragemnt();
				//  
//				intent = new Intent(getActivity(), EditSmyptomActivity.class);
//				intent.putExtra(SMYPTOM_ID, symptomMemoBean.getId() + "");
//				startActivity(intent);
			} else if(symtopbuttonstate == SYMTOM_BUTTON_SELECTEDAY_NOHAD){
			//CommonUtils.showToastMessage(getActivity(), getString(R.string.memo_is_null));
		}

			break;
		case R.id.rel_vas:
			if(is2VasFragment == true){
				  
				((MainActivity)getActivity()).intentToVasHaqListFragemnt();
				
				
			}else{
			intent = new Intent(getActivity(), VASActivity.class);
			
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.activity_bottom_in,R.anim.activity_bottom_out);
			}
			break;
		case R.id.rel_haq:
			if(is2HaqFragment == true){ 
				((MainActivity)getActivity()).intentToVasHaqListFragemnt();
			}else{
			
			intent = new Intent(getActivity(), HAQActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.activity_bottom_in,R.anim.activity_bottom_out);
			}
			break;
		}
	}

	/**
	 * ,, Vas inquiry to determine whether the data display count, whether you can click, and color display parameters yy-mm-dd
	 * 
	 * @return
	 */
	private String setVasData(String selectedDate) {
		SQLiteDatabase db;
		DBHelper mDbHelper;
		String theCount = "";
		boolean isTodayflag = false;
		// Determine whether today
		isTodayflag = selectedDate.equals(DateFormatUtil.getStringData(Long
				.parseLong(System.currentTimeMillis() + "")));

		// Database Get Data
		mDbHelper = new DBHelper(this.getActivity().getApplicationContext());
		db = mDbHelper.getReadableDatabase();
		mLsmHaqVasBean = VasDBOperator.queryVasListOrderByDate(db);

		if (mLsmHaqVasBean.size() != 0) {
			 
			for (int i = 0; i < mLsmHaqVasBean.size(); i++) {
				String tempTime = DateFormatUtil.getStringData(Long
						.parseLong(mLsmHaqVasBean.get(i).getDateNo()));
				 
				if (tempTime.equals(selectedDate)
						&& mLsmHaqVasBean.get(i).getType().equals("1")) {
					theCount = mLsmHaqVasBean.get(i).getCount();
				}
			}
		}
		 
		if (!isTodayflag) {
			mTxtVasCount.setTextColor(getResources()
					.getColor(R.color.text_gray));
			mVAS.setTextColor(getResources().getColor(R.color.text_gray));
			if(theCount != ""){
				LayoutUtils.setTextSize(mVAS, 30 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
			}else{
				LayoutUtils.setTextSize(mVAS, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
			}
			//LayoutUtils.setTextSize(mTxtVasCount, 45);
			mTxtVasCount.setText(theCount);
			mrel_vas.setClickable(false);
			is2VasFragment = false;

		} else {
			if (theCount != "") {
				is2VasFragment = true;
				mTxtVasCount.setTextColor(getResources()
						.getColor(R.color.black));
				mVAS.setTextColor(getResources().getColor(R.color.black));
				LayoutUtils.setTextSize(mVAS, 30 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
				mTxtVasCount.setText(theCount);
				mrel_vas.setClickable(true);
			} else {
				is2VasFragment = false;
				mVAS.setTextColor(getResources().getColor(R.color.main_color));
				mTxtVasCount.setText(theCount);
				LayoutUtils.setTextSize(mVAS, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
				mrel_vas.setClickable(true);
			}

		}
		return null;
	}
	
	/**
	 * Discover haq data,, determine whether to display count, whether you can click, and color display
	 * yy-mm-dd
	 * @return
	 */
	private String setHaqData(String selectedDate) {
		SQLiteDatabase db;
		DBHelper mDbHelper;
		String theCount = "";
		boolean isTodayflag = false;
		 
		isTodayflag = selectedDate.equals(DateFormatUtil.getStringData(Long
				.parseLong(System.currentTimeMillis() + "")));

		 
		mDbHelper = new DBHelper(this.getActivity().getApplicationContext());
		db = mDbHelper.getReadableDatabase();
		mLsmHaqVasBean = VasDBOperator.queryVasListOrderByDate(db);

		if (mLsmHaqVasBean.size() != 0) {

			 
			for (int i = 0; i < mLsmHaqVasBean.size(); i++) {
				String tempTime = DateFormatUtil.getStringData(Long
						.parseLong(mLsmHaqVasBean.get(i).getDateNo()));
				 
				if (tempTime.equals(selectedDate) && mLsmHaqVasBean.get(i).getType().equals("2")) {
					theCount = mLsmHaqVasBean.get(i).getCount();
				}
			}
		}
	 
		if (!isTodayflag) {
			mTxtHaqCount.setTextColor(getResources()
					.getColor(R.color.text_gray));
			mHAQ.setTextColor(getResources().getColor(R.color.text_gray));
			
			if(theCount != ""){
				LayoutUtils.setTextSize(mHAQ, 30 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
			}else{
				LayoutUtils.setTextSize(mHAQ, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
			}
			
			mTxtHaqCount.setText(theCount);
			mrel_haq.setClickable(false);
			is2HaqFragment = false;

		} else {
			if (theCount != "") {
				is2HaqFragment = true;
				mTxtHaqCount.setTextColor(getResources()
						.getColor(R.color.black));
				mHAQ.setTextColor(getResources().getColor(R.color.black));
				LayoutUtils.setTextSize(mHAQ, 30 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
				mTxtHaqCount.setText(theCount);
				mrel_haq.setClickable(true);
			} else {
				is2HaqFragment = false;
				mHAQ.setTextColor(getResources().getColor(R.color.main_color));
				mTxtHaqCount.setText(theCount);
				LayoutUtils.setTextSize(mHAQ, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
				mrel_haq.setClickable(true);
			}

		}
		return null;
	}


	private void setRefrashStateBar() {
		 System.out.println("---------->"+DateFormatUtil.getStringData(mCurrenrLongDate));
 		setHaqData(DateFormatUtil.getStringData(mCurrenrLongDate));
 		setVasData(DateFormatUtil.getStringData(
				mCurrenrLongDate));
 		setSymptomstate(mCurrenrLongDate + "");
		
//		setHaqData(((String) ((TextView) getActivity().findViewById(
//				R.id.txt_date_yymm)).getText()).replace("/", "-")
//				+ "-" + mRecentlyDay);
//		setVasData(((String) ((TextView) getActivity().findViewById(
//				R.id.txt_date_yymm)).getText()).replace("/", "-")
//				+ "-" + mRecentlyDay);
//		setSymptomstate(DateFormatUtil.geDataLongZeroClick(mCurrenrLongDate)
//				+ "");
//		setVasData(DateFormatUtil.getStringData(mCurrenrLongDate)); 
//		setHaqData(DateFormatUtil.getStringData(mCurrenrLongDate));
//		setSymptomstate(mCurrenrLongDate + "");
	}
	
	/**
	 * After clicking the button effect occurs today
	 */
	public void onTodayButtonClick(){
		mCurrenrLongDate = DateFormatUtil.geDataLongZeroClick(System.currentTimeMillis());
		mApp.setmCurrentDateLong(mCurrenrLongDate);
		mCurrenrCarlendarLongDate = DateFormatUtil.geDataLongZeroDay(System.currentTimeMillis());
		onResume();
		mCalendarAdapter.setFocusDate(mCurrenrLongDate);
	}

	/**
	 * Change the date and time, and change the display TitleBar
	 */
	private void setCarlendarGrid() {
		mCalendarAdapter.setData(DBOperator.queryScheduleListGetCalendar(db,
				mCurrenrCarlendarLongDate + ""), DateCalculateUtils
				.getDistanceWithSunday(mCurrenrCarlendarLongDate));
		mCalendarFragemntListener.onDateHasChanged(DateFormatUtil.getStringDatayyyyMM(mCurrenrCarlendarLongDate));
		
	}

	// Symptoms button state set
	private void setSymptomstate(String selectedDate) {

		SQLiteDatabase db;
		DBHelper mDbHelper;
		String theCount = "";
		boolean isTodayflag = false;
		 
		isTodayflag = DateFormatUtil
				.getStringData(Long.parseLong(selectedDate)).equals(
						DateFormatUtil.getStringData(Long.parseLong(System
								.currentTimeMillis() + "")));

		 

		mDbHelper = new DBHelper(this.getActivity().getApplicationContext());
		db = mDbHelper.getReadableDatabase();

		symptomMemoBean = DBOperator.querySymptomMemoBytime(db, selectedDate);

		if (isTodayflag && symptomMemoBean != null) {
			mImgeMemo.setVisibility(View.VISIBLE);
			symtopbuttonstate = SYMTOM_BUTTON_TODAY_HAD;
			LayoutUtils.setTextSize(mTxtMemoTitle, 30 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
			mTxtMemoTitle.setTextColor(getResources().getColor(R.color.main_color));
//		//} else if (isTodayflag && symptomMemoBean == null) {
//			mImgeMemo.setVisibility(View.INVISIBLE);
		}else if(isTodayflag && symptomMemoBean == null){
			mImgeMemo.setVisibility(View.GONE);
			symtopbuttonstate = SYMTOM_BUTTON_TODAY_NOHAD;
			LayoutUtils.setTextSize(mTxtMemoTitle, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
			mTxtMemoTitle.setTextColor(getResources().getColor(
					R.color.main_color));
		} else if (!isTodayflag && symptomMemoBean != null) {
			mImgeMemo.setVisibility(View.VISIBLE);
			symtopbuttonstate = SYMTOM_BUTTON_SELECTEDAY_HAD;
			LayoutUtils.setTextSize(mTxtMemoTitle, 30 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
			mTxtMemoTitle.setTextColor(getResources().getColor(
					R.color.text_gray));
		} 
		else if(!isTodayflag && symptomMemoBean == null){
			mImgeMemo.setVisibility(View.GONE);
			symtopbuttonstate = SYMTOM_BUTTON_SELECTEDAY_NOHAD;
			LayoutUtils.setTextSize(mTxtMemoTitle, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
			mTxtMemoTitle.setTextColor(getResources().getColor(
					R.color.text_gray));
		}

	}

	/**
	 * Refresh the list of events today
	 */
	private void setRefrashDailyEventList() {
		List<ScheduleBean> data = getDailyEventData(mCurrenrLongDate + "");
		if (data != null && data.size() > 0) {
			mDailyEventAdapter.setData(data);
			mLinearNoRecordTip.setVisibility(View.GONE);
			mListView.setVisibility(View.VISIBLE);
		} else {
			mLinearNoRecordTip.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onResume() {
		setRefrashDailyEventList();
		setRefrashStateBar();
		if (firstIn) {
			firstIn = false;
		} else {
			setCarlendarGrid();
		}
		super.onResume();
	}
}
