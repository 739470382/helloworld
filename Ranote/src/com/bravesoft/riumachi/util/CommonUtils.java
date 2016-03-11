package com.bravesoft.riumachi.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Public method class
 * 
 * @author wangyang
 * 
 */
public class CommonUtils {

	public static boolean Phone_is_FHD = false;

	// Save the picture sdcard path
	public static final String PHOTOS_SDCARD_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/"
			+ "riumachi/photos/";

	private static final String TAG = "CommonUtils";

	// AlertDialog common pop-up message
	public static void showDialogMessage(Context mContext, String msg) {
		TextView mTextView = new TextView(mContext);
		mTextView.setGravity(Gravity.CENTER);
		mTextView.setPadding(0, 50, 0, 50);
		mTextView.setText(msg);
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext)
				.setCustomTitle(mTextView).setPositiveButton("OK", null);
		dialog.create();
		dialog.show();
	}

	// Common message Toast
	public static void showToastMessage(Context mContext, String msg) {
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}

	// Check and exit the keyboard
	public static void exitKeyboard(final Activity mActivity) {
		if (mActivity.getCurrentFocus() != null) {
			((InputMethodManager) mActivity
					.getSystemService(Context.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(mActivity.getCurrentFocus()
							.getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	/**
	 *Save the image to your local (JPG)
	 * 
	 * @param bm
	 *            Saved Photos
	 * @return Photos path
	 */
	public static String saveToLocal(Bitmap bm) {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return null;
		}
		FileOutputStream fileOutputStream = null;
		File file = new File(PHOTOS_SDCARD_PATH);
		if (!file.exists()) {
			file.mkdirs();
		}
		String fileName = UUID.randomUUID().toString() + ".jpg";
		String filePath = PHOTOS_SDCARD_PATH + fileName;
		File f = new File(filePath);
		if (!f.exists()) {
			try {
				f.createNewFile();
				fileOutputStream = new FileOutputStream(filePath);
				bm.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
			} catch (IOException e) {
				Log.e("PhotoUtil", e.toString());
				return null;
			} finally {
				try {
					fileOutputStream.flush();
					fileOutputStream.close();
				} catch (IOException e) {
					Log.e("PhotoUtil", e.toString());
					return null;
				}
			}
		}
		return filePath;
	}
}
