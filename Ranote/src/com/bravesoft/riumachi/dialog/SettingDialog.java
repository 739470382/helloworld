package com.bravesoft.riumachi.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.adapter.SettingDialogAdapter;
import com.bravesoft.riumachi.appliction.App;
import com.bravesoft.riumachi.constant.ViewSize;
import com.bravesoft.riumachi.layout.LayoutUtils;

public class SettingDialog extends BaseDialog implements OnClickListener,
		OnItemClickListener {

	private Context context;
	private ListView mListView;
	private SettingDialogAdapter mAdapter;
	private OnSettingDialogListener mSettingDialogListener;
	private int currentMode;
	private App mApp = App.getInstance();

	public SettingDialog(Context context,
			OnSettingDialogListener settingDialogListener) {
		super(context, R.style.TransparentDialog);
		this.context = context;
		this.mSettingDialogListener = settingDialogListener;
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
				R.layout.dialog_setting, null);
		currentView.findViewById(R.id.view_dialog_setting_background)
				.setOnClickListener(this);
		mListView = (ListView) currentView
				.findViewById(R.id.list_setting_dialog);
//		LayoutParams listLayoutParams = (LayoutParams) mListView
//				.getLayoutParams();
		LayoutUtils.rateScale(context, mListView, true);
		LayoutUtils.rateScale(context, (RelativeLayout)currentView
				.findViewById(R.id.rel_list), true);
		LayoutUtils.rateScale(context, (View)currentView
				.findViewById(R.id.buttom_view), true);
//		if (listLayoutParams != null) {
//			if (getAppMode() == App.NORMAL_MODE) {
//				listLayoutParams.topMargin = ViewSize.NORNAL_TITLE_BAR;
//				currentMode = App.NORMAL_MODE;
//			} else if (getAppMode() == App.SEE_THE_DOCTER_MODE) {
//				listLayoutParams.topMargin = ViewSize.SEE_THE_DOCTER_TITLE_BAR;
//				currentMode = App.SEE_THE_DOCTER_MODE;
//			}
//			mListView.setLayoutParams(listLayoutParams);
//		}
		mListView.setOnItemClickListener(this);
		mAdapter = new SettingDialogAdapter(context);
		mListView.setAdapter(mAdapter);
		
		
		 
		
		return currentView;
	}

	@Override
	public void show() {
//		if (currentMode != getAppMode()) {
//			LayoutParams listLayoutParams = (LayoutParams) mListView
//					.getLayoutParams();
//			LayoutUtils.rateScale(context, mListView, true);
//			if (listLayoutParams != null) {
//				if (getAppMode() == App.NORMAL_MODE) {
//					listLayoutParams.topMargin = ViewSize.NORNAL_TITLE_BAR;
//					listLayoutParams.width = ViewSize.SETTING_LIST_WIDTH;
//					currentMode = App.NORMAL_MODE;
//				} else if (getAppMode() == App.SEE_THE_DOCTER_MODE) {
//					listLayoutParams.topMargin = ViewSize.SEE_THE_DOCTER_TITLE_BAR;
//					listLayoutParams.width = ViewSize.SETTING_LIST_WIDTH;
//					currentMode = App.SEE_THE_DOCTER_MODE;
//				}
//				mListView.setLayoutParams(listLayoutParams);
//			}
//		}
		super.show();
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
		switch (position) {
		case 0:
			if (mSettingDialogListener != null) {
				mSettingDialogListener.onSettingMenuClick();
			}
			break;
		case 1:
			if (mSettingDialogListener != null) {
				mSettingDialogListener.onAboutMenuClick();
			}
			break;

		default:
			break;
		}
	}

	public interface OnSettingDialogListener {
		void onSettingMenuClick();

		void onAboutMenuClick();
	}

}
