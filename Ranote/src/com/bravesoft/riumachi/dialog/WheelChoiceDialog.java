package com.bravesoft.riumachi.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
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
import com.bravesoft.riumachi.wheelChoice.PickerUI;
import com.bravesoft.riumachi.wheelChoice.PickerUISettings;

/**
 * 
 * @author wangyuanshi
 * 
 */
public class WheelChoiceDialog extends BaseDialog implements OnClickListener {

	private PickerUI mPickerUI, picker_ui_view2;
	private Button btSlide, save;
	private int currentPosition = -1;
	private List<String> options1, options2;
	private TextView tv, tv_save;
	private String loopVaule = "1", looptype = "1";// 2=週ごと
	private Context context;
	private TextView mtxtSure;
	private TextView mtxtCancel;
	private RelativeLayout mLinearContent;
	private OnWheelChoiceDialogClickListener mOnWheelChoiceDialogClickListener;
	private View mViewSure, mViewCancel;

	public WheelChoiceDialog(Context context, String date) {
		super(context, R.style.NobackDialog);
		this.context = context;
		init();

	}

	public WheelChoiceDialog(Context context) {
		super(context, R.style.NobackDialog);
		this.context = context;
		init();
	}

	public WheelChoiceDialog(Context context,
			OnWheelChoiceDialogClickListener onDatePickDialogClickListener) {
		super(context, R.style.NobackDialog);
		this.context = context;
		this.mOnWheelChoiceDialogClickListener = onDatePickDialogClickListener;
		init();

	}

	public void setOnWheelChoiceDialogClickListener(
			OnWheelChoiceDialogClickListener onDatePickDialogClickListener) {
		this.mOnWheelChoiceDialogClickListener = onDatePickDialogClickListener;
	}

	private void init() {
		setContentView(initView());
		android.view.WindowManager.LayoutParams layoutParams = getWindow()
				.getAttributes();
		layoutParams.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
		layoutParams.height = layoutParams.width;
	}

	@SuppressLint("ResourceAsColor")
	private View initView() {

		View currentView = LinearLayout.inflate(context,
				R.layout.dialog_wheel_choice, null);
		currentView.findViewById(R.id.view_dialog_setting_background)
				.setOnClickListener(this);
		mLinearContent = (RelativeLayout) currentView
				.findViewById(R.id.linear_content);
		LayoutUtils.rateScale(context, mLinearContent, true);
		LayoutUtils.rateScale(context,
				(LinearLayout) currentView.findViewById(R.id.lin), true);
		LayoutUtils.rateScale(context,
				(LinearLayout) currentView.findViewById(R.id.lin_top), true);
		LayoutUtils
				.rateScale(context, (RelativeLayout) currentView
						.findViewById(R.id.relative_bottom), true);
		LayoutUtils.rateScale(context,
				(RelativeLayout) currentView.findViewById(R.id.relRight), true);
		LayoutUtils.rateScale(context,
				(RelativeLayout) currentView.findViewById(R.id.relLeft), true);

		LayoutUtils.rateScale(context,
				currentView.findViewById(R.id.horizotal_line_one), true);
		LayoutUtils.rateScale(context,
				currentView.findViewById(R.id.horizotal_line_two), true);
		LayoutUtils.rateScale(context,
				currentView.findViewById(R.id.horizotal_line_three), true);
		mtxtCancel = (TextView) currentView.findViewById(R.id.txt_cancel);
		mtxtSure = (TextView) currentView.findViewById(R.id.txt_sure);
		LayoutUtils.setTextSize(
				(TextView) currentView.findViewById(R.id.txt_time_pick_title),
				35, TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.setTextSize(mtxtSure, 31,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.setTextSize(mtxtCancel, 31,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);

		mViewSure = currentView.findViewById(R.id.view_sure);
		mViewCancel = currentView.findViewById(R.id.view_cancel);
		LayoutUtils.rateScale(context, mViewSure, true);
		LayoutUtils.rateScale(context, mViewCancel, true);
		mViewCancel.setOnClickListener(this);
		mViewSure.setOnClickListener(this);
		LayoutUtils.rateScale(context, mtxtSure, true);
		mLinearContent.setOnClickListener(this);
		mtxtCancel.setOnClickListener(this);
		mtxtSure.setOnClickListener(this);

		mPickerUI = (PickerUI) currentView.findViewById(R.id.picker_ui_view);
		picker_ui_view2 = (PickerUI) currentView
				.findViewById(R.id.picker_ui_view2);
		LayoutUtils.rateScale(context, mPickerUI, true);
		LayoutUtils.rateScale(context, picker_ui_view2, true);
		options1 = Arrays.asList(context.getResources().getStringArray(
				R.array.loopvalues));
		List<String> newpotion  = new ArrayList<String>();
		newpotion.addAll(options1);
		
		List<String> temp  = new ArrayList<String>();
		temp.addAll(options1);
		
		for(int i = 0; i < 40;i++){
			newpotion.addAll(temp);
		}
		System.out.println("----"+newpotion.size());
		

		mPickerUI.setItems(context, newpotion);
		PickerUISettings pickerUISettings = new PickerUISettings.Builder()
				.withItems(newpotion)
				// .withBackgroundColor(R.color.b)
				.withAutoDismiss(false).withItemsClickables(false)
				.withUseBlur(false).build();
		mPickerUI.setSettings(pickerUISettings);

		mPickerUI
				.setOnClickItemPickerUIListener(new PickerUI.PickerUIItemClickListener() {
					@Override
					public void onItemClickPickerUI(int which, int position,
							String valueResult) {
						currentPosition = position;
						loopVaule = valueResult;
						 
					}
				});

		options2 = Arrays.asList(context.getResources().getStringArray(
				R.array.looptypes));
		// Populate list
		// picker_ui_view2.setShowitemPosition(1);
		picker_ui_view2.setItems(context, options2);
		PickerUISettings pickerUISettings2 = new PickerUISettings.Builder()
				.withItems(options2)
				// .withBackgroundColor(R.color.red)
				.withAutoDismiss(false)//
				.withItemsClickables(false).withUseBlur(false).build();
		picker_ui_view2.setSettings(pickerUISettings2);
		picker_ui_view2
				.setOnClickItemPickerUIListener(new PickerUI.PickerUIItemClickListener() {
					@Override
					public void onItemClickPickerUI(int which, int position,
							String valueResult) {
						currentPosition = position;

						if (valueResult.equals(context.getResources()
								.getString(R.string.looptype2))) {
							looptype = "2";
						} else {
							looptype = "1";
						}

					}
				});

		mPickerUI.slide();
		picker_ui_view2.slide();
		return currentView;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.view_dialog_setting_background:
			 
			break;
		case R.id.linear_content:
			break;
		case R.id.view_cancel:
			if (mOnWheelChoiceDialogClickListener != null) {
				mOnWheelChoiceDialogClickListener
						.OnWheelChoiceDialogCancelClicked();
			}
			break;
		case R.id.view_sure:

			if (mOnWheelChoiceDialogClickListener != null) {
				System.out.println("dianji");
				mOnWheelChoiceDialogClickListener
						.OnWheelChoiceDialogSureClicked(loopVaule, looptype);
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

	/**
	 * Callback interface, click OK, and click on the callback is canceled
	 */
	public interface OnWheelChoiceDialogClickListener {
		public void OnWheelChoiceDialogSureClicked(String loopvaule,
				String looptype);

		public void OnWheelChoiceDialogCancelClicked();
	}

	public void setposition(int valuesPosition, int typePosition) {
		mPickerUI.setShowitemPosition(valuesPosition + 1980- 1);
		picker_ui_view2.setShowitemPosition(typePosition  - 1);
		loopVaule = valuesPosition+"";
				looptype = typePosition+"";
	}

}
