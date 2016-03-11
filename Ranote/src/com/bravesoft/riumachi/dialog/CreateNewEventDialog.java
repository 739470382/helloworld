package com.bravesoft.riumachi.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.activity.CreateMedicineScheduleActivity;
import com.bravesoft.riumachi.activity.CreateMyCardActivity;
import com.bravesoft.riumachi.activity.CreateScheduleActivity;
import com.bravesoft.riumachi.adapter.CreateNewEventAdapter;
import com.bravesoft.riumachi.constant.ScheduleType;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.layout.LayoutUtils;

public class CreateNewEventDialog extends BaseDialog implements
		OnClickListener, OnItemClickListener {

	private Context context;
	private ListView mListView;
	private CreateNewEventAdapter mAdapter;
	private OnNewEventDialogClickListener mONewEventDialogClickListener;

	public CreateNewEventDialog(Context context ,OnNewEventDialogClickListener onNewEventDialogClickListener) {
		super(context, R.style.NobackDialog);
		this.context = context;
		this.mONewEventDialogClickListener = onNewEventDialogClickListener;
		init();

	}

	private void init() {
		setContentView(initView());
		android.view.WindowManager.LayoutParams layoutParams = getWindow()
				.getAttributes();
		layoutParams.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
		layoutParams.height = layoutParams.width;
	}

	private View initView() {
		View currentView = LinearLayout.inflate(context,
				R.layout.dialog_add_new_event, null);
		currentView.findViewById(R.id.view_dialog_setting_background)
				.setOnClickListener(this);

		LayoutUtils.rateScale(context, (RelativeLayout) currentView
				.findViewById(R.id.relative_new_event_content), true);
		LayoutUtils.rateScale(context,
				currentView.findViewById(R.id.horizotal_line_one), true);
		View dialogTitle = LinearLayout.inflate(context,
				R.layout.view_text_title, null);
		mListView = (ListView) currentView.findViewById(R.id.list_new_event);
		LayoutUtils.setTextSize(
				(TextView) dialogTitle.findViewById(R.id.txt_title), 32,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.rateScale(context,
				(RelativeLayout) dialogTitle.findViewById(R.id.relative_title),
				true);
		mListView.setOnItemClickListener(this);
		mListView.addHeaderView(dialogTitle, null, false);
		mAdapter = new CreateNewEventAdapter(context);
		mListView.setAdapter(mAdapter);
		return currentView;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.view_dialog_setting_background:
			dismiss();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		if (mONewEventDialogClickListener!=null) {
			mONewEventDialogClickListener.onNewEventDialogClick(position);
		}
		switch (position) {
		case 1:
			CreateScheduleActivity.startCreateSchedule(context,
					CreateScheduleActivity.OPEN_TYPE_CREATE,
					ScheduleType.NORMAL_SCHEDULE);
			 
			((Activity)context).overridePendingTransition(R.anim.activity_bottom_in,R.anim.activity_bottom_out);
			break;
		case 2:
			CreateScheduleActivity.startCreateSchedule(context,
					CreateScheduleActivity.OPEN_TYPE_CREATE,
					ScheduleType.SEE_THE_DOCTER_SCHEDULE);
			((Activity)context).overridePendingTransition(R.anim.activity_bottom_in,R.anim.activity_bottom_out);
			break;
		case 3:
			CreateMedicineScheduleActivity.startCreateMedicineScheduleActivity(
					context, CreateMedicineScheduleActivity.OPEN_TYPE_CREATE);
			((Activity)context).overridePendingTransition(R.anim.activity_bottom_in,R.anim.activity_bottom_out);
			break;
		case 4:
			CreateMyCardActivity.startCreateMyCardActivity(context,
					CreateMedicineScheduleActivity.OPEN_TYPE_CREATE);
			((Activity)context).overridePendingTransition(R.anim.activity_bottom_in,R.anim.activity_bottom_out);
			break;

		default:
			break;
		}

	}
	
	public interface OnNewEventDialogClickListener{
		public void onNewEventDialogClick(int position);
	}

}
