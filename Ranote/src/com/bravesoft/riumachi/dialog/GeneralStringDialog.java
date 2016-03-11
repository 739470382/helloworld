package com.bravesoft.riumachi.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.layout.LayoutUtils;


public class GeneralStringDialog extends BaseDialog implements OnClickListener {

	private Context context;
	private String mTitleContent;
	private TextView mtxtSure;
	private TextView mtxtCancel;
	private TextView mtxtTitle;
	private View mRelativeContent;
	private int type;
	private OnGenenalStringDialogClickListener mOnGenenalDialogClickListener;
	private View mViewSure,mViewCancel;

	/**
	 * 
	 * @param context
	 *             
	 * @param content
	 *            DialogTitle
	 */
	public GeneralStringDialog(Context context, String content) {
		super(context, R.style.NobackDialog);
		//super(context, R.style.AppTheme);
		this.context = context;
		mTitleContent = content;
		init();

	}

	public GeneralStringDialog(Context context, String content,
			OnGenenalStringDialogClickListener onGenenalDialogClickListener) {
		super(context, R.style.NobackDialog);
		//super(context, R.style.AppTheme);
		this.context = context;
		mTitleContent = content;
		mOnGenenalDialogClickListener = onGenenalDialogClickListener;
		init();

	}

	public GeneralStringDialog(Context context, String content, int type,
			OnGenenalStringDialogClickListener onGenenalDialogClickListener) {
		super(context, R.style.NobackDialog);
		this.context = context;
		this.type = type;
		mTitleContent = content;
		mOnGenenalDialogClickListener = onGenenalDialogClickListener;
		init();
	}

	public void setOnGenenalDialogClickListener(
			OnGenenalStringDialogClickListener onGenenalDialogClickListener) {
		this.mOnGenenalDialogClickListener = onGenenalDialogClickListener;
	}

	public void setContent(String content) {
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
				R.layout.dialog_general, null);
		currentView.findViewById(R.id.view_dialog_setting_background)
				.setOnClickListener(this);

		mRelativeContent = (RelativeLayout) currentView
				.findViewById(R.id.relative_content);
		LayoutUtils.rateScale(context,
				(RelativeLayout) currentView.findViewById(R.id.relative_title),
				true);
		LayoutUtils.rateScale(context, mRelativeContent, true);
		LayoutUtils
				.rateScale(context, (RelativeLayout) currentView
						.findViewById(R.id.relative_bottom), true);
//		LayoutUtils
//		.rateScale(context, (RelativeLayout) currentView
//				.findViewById(R.id.dialog_background), true);
		mViewSure = currentView.findViewById(R.id.view_sure);
		mViewCancel = currentView.findViewById(R.id.view_cancel);
		LayoutUtils.rateScale(context, mViewSure , true);
		LayoutUtils.rateScale(context, mViewCancel , true);
		mViewCancel.setOnClickListener(this);
		mViewSure.setOnClickListener(this);
		
		LayoutUtils.rateScale(context,
				currentView.findViewById(R.id.horizotal_line_one), true);
		
		mtxtTitle = (TextView) currentView.findViewById(R.id.txt_dialog_title);
		mtxtCancel = (TextView) currentView.findViewById(R.id.txt_cancel);
		mtxtSure = (TextView) currentView.findViewById(R.id.txt_sure);
		LayoutUtils.rateScale(context, mtxtSure , true);
		switch (type) {
		case 1:
			mtxtSure.setText("決定");
			break;
		case 2:
			mtxtSure.setText("はい");
			break;
		case 3:
			mtxtSure.setText("復元する");
			break;

		default:
			break;
		}
		LayoutUtils.setTextSize(mtxtTitle, 31,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.setTextSize(mtxtSure, 29,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.setTextSize(mtxtCancel, 29,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);

		 
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
		mtxtTitle.setText(mTitleContent);
		super.show();
	}

	public interface OnGenenalStringDialogClickListener {
		public void OnGeneralSureTextClicked();

		public void OnGeneralCancelTextClicked();
	}

}
