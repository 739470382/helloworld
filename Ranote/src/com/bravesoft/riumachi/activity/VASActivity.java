package com.bravesoft.riumachi.activity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.bean.HaqVasBean;
import com.bravesoft.riumachi.bean.VasBean;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.database.DBHelper;
import com.bravesoft.riumachi.database.VasDBOperator;
import com.bravesoft.riumachi.dialog.GeneralStringHasTitleDialog;
import com.bravesoft.riumachi.dialog.GeneralStringHasTitleDialog.OnGenenalStringHasTitleDialogClickListener;
import com.bravesoft.riumachi.layout.LayoutUtils;

public class VASActivity extends BaseActivity implements OnClickListener {
	private TextView mtitle_name, mvas_question_part1, mvas_question_part2,
			vas_count, vas_min, vas_max;
	private ImageView img_count;
	private SeekBar vas_seekbar;
	private Button vas_add_btnOK; //  
	private int value = 55; //  the vas vulue 
	private GeneralStringHasTitleDialog mAddMessageOkDialog;  
	private SQLiteDatabase db;
	private DBHelper mDbHelper;
	private View view_back;
	 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_vas);
		super.onCreate(savedInstanceState);
		initView(savedInstanceState);
		setOnclicklistener();

	}

	 /** 
	  * set the clicklistener
	  */
	private void setOnclicklistener() {
		vas_seekbar
				.setOnSeekBarChangeListener(new VasOnSeekBarChangeListener());
	}

	 
	private void initView(Bundle savedInstanceState) {
		
		mDbHelper = new DBHelper(getApplicationContext());
		db = mDbHelper.getReadableDatabase(); 
		
		getRateView(R.id.relative_title, true);
		getRateView(R.id.l, true);
		getRateView(R.id.img_back, true);
		view_back=getRateView(R.id.view_back, true);
		
		mtitle_name = getTextView(R.id.txt_title_name, true, 37 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		mvas_question_part1 = getTextView(R.id.vas_question_part1, true, 33 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mvas_question_part2 = getTextView(R.id.vas_question_part2, true, 33 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		vas_count = getTextView(R.id.vas_count, true, 173 ,TextTypeFace.TYPEFACE_ROBOTO_BOLD);
		vas_min = getTextView(R.id.vas_min, true, 24 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		vas_max = getTextView(R.id.vas_max, true, 24 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		img_count = getRateView(R.id.img_count, true);
		vas_add_btnOK = getRateView(R.id.vas_add_btnOK, true);
		vas_seekbar = getRateView(R.id.vas_seekbar, true);
		
		vas_seekbar.setProgress(55);
		
		vas_count.setText("55");
		
		
		
		vas_add_btnOK.setOnClickListener(this);
		view_back.setOnClickListener(this);
		
		LayoutUtils.setTextSize(vas_add_btnOK, 33 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		
		mtitle_name.setText(this.getResources().getString(
				R.string.vas_bar_title));
		
	}

	 
	int lastProgress = 0;
	int currentProgress = 0;
	boolean flag = false;

	private class VasOnSeekBarChangeListener implements OnSeekBarChangeListener {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean arg2) {
			value = seekBar.getProgress();
			vas_count.setText(String.valueOf(value));
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {

		}
		 

	}

	 
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.view_back:
			finishActivity();
			overridePendingTransition(0,R.anim.activity_bottom_out);
			break;

		case R.id.vas_add_btnOK:
			if (mAddMessageOkDialog == null) {
				mAddMessageOkDialog = new GeneralStringHasTitleDialog(this,
						getString(R.string.haq_dialog_title),
						getString(R.string.haq_dialog_cancel), getString(R.string.certain)  );
				
				mAddMessageOkDialog
						.setOnGenenalDialogClickListener(new OnGenenalStringHasTitleDialogClickListener() {

							@Override
							public void OnGeneralSureTextClicked() {
								 
								saveVasCount(value);
								mAddMessageOkDialog.dismiss(); 
								finishActivity();
								
							}

							@Override
							public void OnGeneralCancelTextClicked() {
								mAddMessageOkDialog.dismiss();
							}

						});
			}
			mAddMessageOkDialog.show();
			break;
		default:
			break;
		}
	}
	
/** 
 * insert the data to db
 * @param vas_count
 * @return
 */
	private boolean saveVasCount(int vas_count) {
		String currentTime =  System.currentTimeMillis()+"";
		HaqVasBean mHaqVasBean = new HaqVasBean();
		mHaqVasBean.setCount(vas_count+"");
		mHaqVasBean.setDateNo(currentTime);
		mHaqVasBean.setType("1");
		return VasDBOperator.insertVas(db, mHaqVasBean);
	}
	
	@Override
	public void onDestroy() {
		if (db != null && db.isOpen()) {
			db.close();
		}
		super.onDestroy();
	}
}
