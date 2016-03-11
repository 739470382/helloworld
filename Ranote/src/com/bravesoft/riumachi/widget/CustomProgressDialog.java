package com.bravesoft.riumachi.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import com.bravesoft.riumachi.R;

/**
 * Schedule tips
 * 
 * @author ZhouPeng
 * 
 */
public class CustomProgressDialog implements OnCancelListener {

	private Dialog mDialog;
	private Context mContext;
	private DialogCancelListener mCancelListener;

	public CustomProgressDialog(Context context) {
		this.mContext = context;
		mDialog = new Dialog(mContext, R.style.NobackDialog);
		mDialog.setContentView(R.layout.view_dialog_progress);
		mDialog.setCanceledOnTouchOutside(false);
		mDialog.setCancelable(false);
		mDialog.setOnCancelListener(this);
	}

	public void showDialog() {
		mDialog.show();
	}

	public void removeDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.cancel();
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		if (mCancelListener != null) {
			mCancelListener.onDialogCancel();
		}
	}

	public void setOnCancelListener(DialogCancelListener listener) {
		this.mCancelListener = listener;
	}

	public interface DialogCancelListener {
		void onDialogCancel();
	}

}
