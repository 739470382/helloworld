package com.bravesoft.riumachi.dialog;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.dialog.GeneralStringDialog.OnGenenalStringDialogClickListener;
import com.bravesoft.riumachi.layout.LayoutUtils;

public class GeneralStringHasTitleDialog extends BaseDialog implements OnClickListener {

	private Context context;
	private String mTitleContent;
	private TextView mtxtSure;
	private TextView mtxtCancel;
	private TextView mtxtTitle;
	private TextView tex_dialog_tip;
	private View mRelativeContent;
	private String mStrcancelButton;
	private String mStrCertainButton;
	private OnGenenalStringHasTitleDialogClickListener mOnGenenalDialogClickListener;
	private View mViewSure,mViewCancel;

	/**
	 * 
	 * @param context  
	 * @param content DialogTitle
	 */
	public GeneralStringHasTitleDialog(Context context,String content,String StrcancelButton,String StrCertainButton ) {
		super(context, R.style.NobackDialog);
		this.context = context;
		mTitleContent = content;
		this.mStrcancelButton =  StrcancelButton;
		this.mStrCertainButton = StrCertainButton;
		init();

	}
	
	public GeneralStringHasTitleDialog(Context context,String content ,String StrcancelButton, OnGenenalStringHasTitleDialogClickListener onGenenalDialogClickListener) {
		super(context, R.style.NobackDialog);
		this.context = context;
		mTitleContent = content;
		this.mStrcancelButton =  StrcancelButton;
		mOnGenenalDialogClickListener = onGenenalDialogClickListener;
		init();

	}
	
	public void setOnGenenalDialogClickListener(OnGenenalStringHasTitleDialogClickListener onGenenalDialogClickListener){
		this.mOnGenenalDialogClickListener = onGenenalDialogClickListener;
	}
	
	public void setContent(String content){
		mTitleContent = content;
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
				R.layout.dialog_hastitle, null);
		currentView.findViewById(R.id.view_dialog_setting_background).setOnClickListener(this);
		
		mRelativeContent = (RelativeLayout)currentView.findViewById(R.id.relative_content);
		LayoutUtils.rateScale(context, (RelativeLayout)currentView.findViewById(R.id.relative_title), true);
		LayoutUtils.rateScale(context, mRelativeContent , true);
		LayoutUtils.rateScale(context, (RelativeLayout)currentView.findViewById(R.id.relative_bottom), true);
		LayoutUtils.rateScale(context, currentView.findViewById(R.id.horizotal_line_one), true);
		LayoutUtils.rateScale(context, currentView.findViewById(R.id.horizotal_line_two), true);
		mtxtTitle = (TextView)currentView.findViewById(R.id.txt_dialog_title);
		
		mViewSure = currentView.findViewById(R.id.view_sure);
		mViewCancel = currentView.findViewById(R.id.view_cancel);
		LayoutUtils.rateScale(context, mViewSure , true);
		LayoutUtils.rateScale(context, mViewCancel , true);
		mViewCancel.setOnClickListener(this);
		mViewSure.setOnClickListener(this);
		
		
		tex_dialog_tip = (TextView)currentView.findViewById(R.id.tex_dialog_tip);
		LayoutUtils.rateScale(context, tex_dialog_tip , true);
		mtxtCancel = (TextView)currentView.findViewById(R.id.txt_cancel);
		mtxtSure = (TextView)currentView.findViewById(R.id.txt_sure);
		LayoutUtils.rateScale(context, mtxtSure , true);
		LayoutUtils.setTextSize(tex_dialog_tip, 31,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.setTextSize(mtxtTitle, 29,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		LayoutUtils.setTextSize(mtxtSure, 29,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.setTextSize(mtxtCancel, 29,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		
//		RelativeLayout relative_bottom = (RelativeLayout)currentView.findViewById(R.id.relative_bottom);
//		LayoutUtils.rateScale(context, relative_bottom , true);
		
		 
		mRelativeContent.setOnClickListener(this);
		mtxtCancel.setOnClickListener(this);
		mtxtSure.setOnClickListener(this);
		return currentView;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.view_dialog_setting_background:
			dismiss();
			break;
		case R.id.view_cancel:
			if (mOnGenenalDialogClickListener != null) {
				mOnGenenalDialogClickListener.OnGeneralCancelTextClicked();
			}
			break;
		case R.id.view_sure:
			if (mOnGenenalDialogClickListener != null) {
				mOnGenenalDialogClickListener.OnGeneralSureTextClicked();
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void show() {
		mtxtCancel.setText(mStrcancelButton);
		mtxtSure.setText(mStrCertainButton);
		
		mtxtTitle.setText(mTitleContent);
		super.show();
	}
	
	public interface OnGenenalStringHasTitleDialogClickListener{
		public void OnGeneralSureTextClicked();
		public void OnGeneralCancelTextClicked();
	}

	 

}
