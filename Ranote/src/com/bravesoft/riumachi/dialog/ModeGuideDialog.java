package com.bravesoft.riumachi.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.layout.LayoutUtils;

/** 
 * first open the app ,you can see the GuideDialog
 * @author BraveSoft
 *
 */
public class ModeGuideDialog extends BaseDialog implements OnClickListener {

	private Context context;
	private TextView dialog_cancel;
	private ImageView dialog_content_image;
	OnModeGuideDialogClickListener listener;
	private View mViewClose;

	
	public ModeGuideDialog(Context context,OnModeGuideDialogClickListener listener) {
		super(context, R.style.NobackDialog);
		this.context = context;
		this.listener = listener;
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
				R.layout.dialog_mode_guide, null);
		currentView.findViewById(R.id.view_dialog_setting_background).setOnClickListener(this);
		LayoutUtils.rateScale(context, (RelativeLayout)currentView.findViewById(R.id.relative_title), true);
		LayoutUtils.rateScale(context, (RelativeLayout)currentView.findViewById(R.id.relative_content), true);
		LayoutUtils.rateScale(context,(RelativeLayout)currentView.findViewById(R.id.rel_content), true);
		
		mViewClose = (View)currentView.findViewById(R.id.relative_close);
		LayoutUtils.rateScale(context,mViewClose, true);
		mViewClose.setOnClickListener(this);
		
		dialog_cancel = (TextView)currentView.findViewById(R.id.dialog_cancel);
		dialog_content_image = (ImageView)currentView.findViewById(R.id.mode_guide_dialog_image);
		LayoutUtils.rateScale(context,(View)currentView.findViewById(R.id.textview_buttom_diatance), true);
		LayoutUtils.rateScale(context,dialog_content_image, true);
		LayoutUtils.rateScale(context,dialog_cancel, true);
		
		LayoutUtils.rateScale(context,currentView.findViewById(R.id.img_setting), true);
		
		LayoutUtils.setTextSize(dialog_cancel,28,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		dialog_cancel.setOnClickListener(this);
		 
		
		return currentView;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.view_dialog_setting_background:
			break;
		case R.id.relative_close:
		case R.id.dialog_cancel:
			if(listener != null){
				listener.OncancelTextClicked();
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
	public interface OnModeGuideDialogClickListener{
		public void OncancelTextClicked();
		
	}

}
