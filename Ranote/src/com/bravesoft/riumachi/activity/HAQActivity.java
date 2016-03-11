package com.bravesoft.riumachi.activity;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.bean.HaqVasBean;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.database.DBHelper;
import com.bravesoft.riumachi.database.VasDBOperator;
import com.bravesoft.riumachi.dialog.GeneralStringHasTitleDialog;
import com.bravesoft.riumachi.dialog.GeneralStringHasTitleDialog.OnGenenalStringHasTitleDialogClickListener;
import com.bravesoft.riumachi.layout.LayoutUtils;

public class HAQActivity extends BaseActivity implements OnClickListener {
	private TextView mtitle_name, haq_question_num, haq_question_total,
			haq_que_content;
	private Button haq_button1, haq_button2, haq_button3, haq_button4;
	private RelativeLayout haq_btnPre, haq_btnNext;
	public int iCurrentQueNum = 1;
	public Map<String, String> mapQue = new HashMap<String, String>();
	public Map<String, String> mapResult = new HashMap<String, String>();
	private LinearLayout lin_question;
	private View view_back;

	// result layout
	private TextView haq_result_tip1, haq_result_tip2, haq_count, haq_min,
			haq_max, haq_result_btnPre;
	private ImageView result_img_count;
	private SeekBar haq_seekbar;
	private Button haq_add_btnOK;
	private RelativeLayout rel_result;
	private GeneralStringHasTitleDialog mAddMessageOkDialog;
	private String haqValue;
	private SQLiteDatabase db;
	private DBHelper mDbHelper;
	private boolean btFlag = false;// butten
	private int fl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_haq);
		super.onCreate(savedInstanceState);
		initView(savedInstanceState);
		mapQue = this.setList();
		setBtnAndQue();
	}

	private void initView(Bundle savedInstanceState) {

		mDbHelper = new DBHelper(getApplicationContext());
		db = mDbHelper.getReadableDatabase();
		getRateView(R.id.img_result_pre, true);
		getRateView(R.id.img_pre, true);
		getRateView(R.id.img_next, true);
		getRateView(R.id.relative_title, true);
		getRateView(R.id.rel_part2, true);
		getRateView(R.id.rel_haq_question, true);
		getRateView(R.id.img_question, true);
		getRateView(R.id.rel_haq_button, true);
		getRateView(R.id.haq_rel_pre, true);
		getTextView(R.id.haq_top_title1, true, 35 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		getTextView(R.id.haq_top_title2, true, 35 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		getRateView(R.id.lin_but_1, true);
		getRateView(R.id.lin_but_2, true);

		getRateView(R.id.img_back, true);
		getRateView(R.id.rel_left, true);

		view_back = getRateView(R.id.view_back, true);
		view_back.setOnClickListener(this);

		haq_btnNext = getRateView(R.id.haq_RelNext, true);
		haq_btnPre = getRateView(R.id.haq_RelPre, true);

		haq_button1 = getRateView(R.id.haq_button1, true);
		haq_button2 = getRateView(R.id.haq_button2, true);
		haq_button3 = getRateView(R.id.haq_button3, true);
		haq_button4 = getRateView(R.id.haq_button4, true);
		lin_question = getRateView(R.id.lin_question, true);
		rel_result = getRateView(R.id.haq_result, true);

		getTextView(R.id.txt_pre, true, 30 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		getTextView(R.id.txt_next, true, 30 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		getTextView(R.id.haq_question_total, true, 37, TextTypeFace.TYPEFACE_ROBOTO_BOLD);

		LayoutUtils.setTextSize(haq_button1, 35,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.setTextSize(haq_button2, 35,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.setTextSize(haq_button3, 35,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.setTextSize(haq_button4, 35,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);

		haq_button1.setOnTouchListener(new BtnOntouchListener());
		haq_button2.setOnTouchListener(new BtnOntouchListener());
		haq_button3.setOnTouchListener(new BtnOntouchListener());
		haq_button4.setOnTouchListener(new BtnOntouchListener());

		haq_btnNext.setOnClickListener(this);
		haq_btnPre.setOnClickListener(this);

		mtitle_name = getTextView(R.id.txt_title_name, true, 37 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		//haq_que_content = getTextView(R.id.haq_que_content, true, 35 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		haq_que_content = getRateView(R.id.haq_que_content, true);
		LayoutUtils.setTextSize(haq_que_content, 35 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		
		haq_question_num = getTextView(R.id.haq_question_num, true, 77,
				TextTypeFace.TYPEFACE_ROBOTO_BOLD);

		mtitle_name.setText(this.getResources().getString(
				R.string.haq_bar_title));

		getRateView(R.id.rela, true);
		getRateView(R.id.haq_result_rel_pre, true);
		haq_result_btnPre = getTextView(R.id.haq_result_btnPre, true, 28,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);

		haq_result_tip1 = getTextView(R.id.haq_result_tip1, true, 33,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		haq_result_tip2 = getTextView(R.id.haq_result_tip2, true, 33,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		haq_count = getTextView(R.id.haq_count, true, 157, TextTypeFace.TYPEFACE_ROBOTO_BOLD);
		haq_min = getTextView(R.id.haq_min, true, 25, TextTypeFace.TYPEFACE_ROBOTO_REGULAR);
		haq_max = getTextView(R.id.haq_max, true, 25, TextTypeFace.TYPEFACE_ROBOTO_REGULAR);
		result_img_count = getRateView(R.id.result_img_count, true);
		haq_add_btnOK = getRateView(R.id.haq_add_btnOK, true);
		haq_seekbar = getRateView(R.id.haq_seekbar, true);

		LayoutUtils.setTextSize(haq_add_btnOK, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		 
		haq_result_btnPre.setOnClickListener(this);
		haq_add_btnOK.setOnClickListener(this);

		haq_seekbar.setOnTouchListener(new SeekbarOntouchListener());
	}
	
/** 
 * Settings the seekbar can't click
 * @author BraveSoft
 */
	private class SeekbarOntouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			return true;
		}
	}

	private class BtnOntouchListener implements OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {

			String strQueNum = "Q" + String.valueOf(iCurrentQueNum);

			switch (event.getAction()) {

			case MotionEvent.ACTION_DOWN:

				switch (v.getId()) {
				case R.id.haq_button1:
					if (btFlag == false) {
						setButSelectedFalse();
						fl = R.id.haq_button1;
						// haq_button1.setBackgroundResource(R.drawable.haq_button_selected);
						// haq_button1.setTextColor(getResources().getColor(R.color.main_color));
						haq_button1
								.setBackgroundResource(R.drawable.haq_button_selected);
						haq_button1.setTextColor(getResources().getColor(
								R.color.white));
						mapResult.put(strQueNum, "0");
						btFlag = true;
					}
					break;
				case R.id.haq_button2:
					if (btFlag == false) {
						setButSelectedFalse();
						fl = R.id.haq_button2;
						// haq_button2.setBackgroundResource(R.drawable.haq_button_selected);
						// haq_button2.setTextColor(getResources().getColor(R.color.main_color));
						haq_button2
								.setBackgroundResource(R.drawable.haq_button_selected);
						haq_button2.setTextColor(getResources().getColor(
								R.color.white));
						mapResult.put(strQueNum, "1");
						btFlag = true;
					}
					break;
				case R.id.haq_button3:
					if (btFlag == false) {
						setButSelectedFalse();
						fl = R.id.haq_button3;
						// haq_button3.setBackgroundResource(R.drawable.haq_button_selected);
						// haq_button3.setTextColor(getResources().getColor(R.color.main_color));
						haq_button3
								.setBackgroundResource(R.drawable.haq_button_selected);
						haq_button3.setTextColor(getResources().getColor(
								R.color.white));
						mapResult.put(strQueNum, "2");
						btFlag = true;
					}
					break;
				case R.id.haq_button4:
					if (btFlag == false) {
						setButSelectedFalse();
						fl = R.id.haq_button4;
						// haq_button4.setBackgroundResource(R.drawable.haq_button_selected);
						// haq_button4.setTextColor(getResources().getColor(R.color.main_color));
						haq_button4
								.setBackgroundResource(R.drawable.haq_button_selected);
						haq_button4.setTextColor(getResources().getColor(
								R.color.white));
						mapResult.put(strQueNum, "3");
						btFlag = true;
					}
					break;

				}
				break;

			case MotionEvent.ACTION_UP:

				switch (v.getId()) {
				case R.id.haq_button1:
					if (fl == R.id.haq_button1 && btFlag == true) {
						haq_button1
								.setBackgroundResource(R.drawable.haq_button_style);
						haq_button1.setTextColor(getResources().getColor(
								R.color.main_color));
						isend();
						btFlag = false;
					}
					break;
				case R.id.haq_button2:
					if (fl == R.id.haq_button2 && btFlag == true) {
						haq_button2
								.setBackgroundResource(R.drawable.haq_button_style);
						haq_button2.setTextColor(getResources().getColor(
								R.color.main_color));
						isend();
						btFlag = false;
					}
					break;
				case R.id.haq_button3:
					if (fl == R.id.haq_button3 && btFlag == true) {
						haq_button3
								.setBackgroundResource(R.drawable.haq_button_style);
						haq_button3.setTextColor(getResources().getColor(
								R.color.main_color));
						isend();
						btFlag = false;
					}
					break;
				case R.id.haq_button4:
					if (fl == R.id.haq_button4 && btFlag == true) {
						haq_button4
								.setBackgroundResource(R.drawable.haq_button_style);
						haq_button4.setTextColor(getResources().getColor(
								R.color.main_color));
						isend();
						btFlag = false;
					}
					break;
				}
				break;
			}

			return false;
		}

	}

	/** 
	 * To answer the question over,if end showing the result layout 
	 */
	private void isend() {

		if (iCurrentQueNum == 20) {
			iCurrentQueNum = 20;
			lin_question.setVisibility(View.INVISIBLE);
			rel_result.setVisibility(View.VISIBLE);
			mtitle_name.setText(this.getResources().getString(
					R.string.haq_result_title));

			haqValue = getCountValue();
			if (haqValue.substring(haqValue.length() - 2, haqValue.length())
					.equals("00")) {
				haqValue = haqValue.substring(0, 1);
			}

			haq_count.setText(haqValue);
			double b = Double.parseDouble(haqValue);
			b = b / 3 * 100;

			haq_seekbar.setProgress((int) b);

		} else {
			iCurrentQueNum++;
		}

		setBtnAndQue();
	}

	@Override
	public void onClick(View v) {

		setButSelectedFalse();

		switch (v.getId()) {
		case R.id.view_back:
			finishActivity();
			overridePendingTransition(0,R.anim.activity_bottom_out);
			break;

		case R.id.haq_RelPre:
			if (iCurrentQueNum == 1) {
				iCurrentQueNum = 1;
			} else {
				iCurrentQueNum--;
			}
			setButSelectedFalse();
			setBtnAndQue();
			break;
		case R.id.haq_RelNext:
			if (iCurrentQueNum == 20) {
				iCurrentQueNum = 20;
			} else {
				iCurrentQueNum++;
			}
			setButSelectedFalse();
			setBtnAndQue();
			break;
		case R.id.haq_result_btnPre:

			iCurrentQueNum = 20;
			lin_question.setVisibility(View.VISIBLE);
			rel_result.setVisibility(View.INVISIBLE);
			mtitle_name.setText(this.getResources().getString(
					R.string.haq_bar_title));
			setButSelectedFalse();
			setBtnAndQue();
			break;
		case R.id.haq_add_btnOK:
			if (mAddMessageOkDialog == null) {
				mAddMessageOkDialog = new GeneralStringHasTitleDialog(this,
						getString(R.string.haq_dialog_title),
						getString(R.string.haq_dialog_cancel),
						getString(R.string.certain));
				mAddMessageOkDialog
						.setOnGenenalDialogClickListener(new OnGenenalStringHasTitleDialogClickListener() {

							@Override
							public void OnGeneralSureTextClicked() {

								saveHaqCount(haqValue);
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
	 * update the next question , 
	 */
	public void setBtnAndQue() {
		haq_btnNext.setVisibility(View.INVISIBLE);
		haq_btnPre.setVisibility(View.INVISIBLE);
		setPreNext(iCurrentQueNum);
		if (iCurrentQueNum == 20) {
			haq_btnNext.setVisibility(View.INVISIBLE);
		}
		String strQueNum = "Q" + String.valueOf(iCurrentQueNum);
		haq_question_num.setText(strQueNum);
		haq_que_content.setText(mapQue.get(strQueNum));
		if(iCurrentQueNum == 4){
			LayoutUtils.setTextSize(haq_que_content, 28 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		}else{
			LayoutUtils.setTextSize(haq_que_content, 35 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		}

		if (mapResult.get(strQueNum) != null) {

			switch (Integer.parseInt(mapResult.get(strQueNum))) {
			case 3:
				haq_button4
						.setBackgroundResource(R.drawable.haq_button_selected);
				haq_button4
						.setTextColor(getResources().getColor(R.color.white));

				break;
			case 2:
				haq_button3
						.setBackgroundResource(R.drawable.haq_button_selected);
				haq_button3
						.setTextColor(getResources().getColor(R.color.white));
				break;
			case 1:
				haq_button2
						.setBackgroundResource(R.drawable.haq_button_selected);
				haq_button2
						.setTextColor(getResources().getColor(R.color.white));
				break;
			case 0:
				haq_button1
						.setBackgroundResource(R.drawable.haq_button_selected);
				haq_button1
						.setTextColor(getResources().getColor(R.color.white));
				break;

			}
		} else {
			setButSelectedFalse();

		}
	}
	
/** 
 * set the button state to normal 
 */
	private void setButSelectedFalse() {
		haq_button1.setBackgroundResource(R.drawable.haq_button_style);
		haq_button1.setTextColor(getResources().getColor(R.color.main_color));
		haq_button2.setBackgroundResource(R.drawable.haq_button_style);
		haq_button2.setTextColor(getResources().getColor(R.color.main_color));
		haq_button3.setBackgroundResource(R.drawable.haq_button_style);
		haq_button3.setTextColor(getResources().getColor(R.color.main_color));
		haq_button4.setBackgroundResource(R.drawable.haq_button_style);
		haq_button4.setTextColor(getResources().getColor(R.color.main_color));

		// haq_button1.setBackgroundResource(R.drawable.haq_button_selected);
		// haq_button1.setTextColor(getResources().getColor(R.color.main_color));
		// haq_button2.setBackgroundResource(R.drawable.haq_button_selected);
		// haq_button2.setTextColor(getResources().getColor(R.color.main_color));
		// haq_button3.setBackgroundResource(R.drawable.haq_button_selected);
		// haq_button3.setTextColor(getResources().getColor(R.color.main_color));
		// haq_button4.setBackgroundResource(R.drawable.haq_button_selected);
		// haq_button4.setTextColor(getResources().getColor(R.color.main_color));

	}

	public Map<String, String> setList() {
		mapQue.put("Q1", getResources().getString(R.string.question1));
		mapQue.put("Q2", getResources().getString(R.string.question2));
		mapQue.put("Q3", getResources().getString(R.string.question3));
		mapQue.put("Q4", getResources().getString(R.string.question4));
		mapQue.put("Q5", getResources().getString(R.string.question5));
		mapQue.put("Q6", getResources().getString(R.string.question6));
		mapQue.put("Q7", getResources().getString(R.string.question7));
		mapQue.put("Q8", getResources().getString(R.string.question8));
		mapQue.put("Q9", getResources().getString(R.string.question9));
		mapQue.put("Q10", getResources().getString(R.string.question10));
		mapQue.put("Q11", getResources().getString(R.string.question11));
		mapQue.put("Q12", getResources().getString(R.string.question12));
		mapQue.put("Q13", getResources().getString(R.string.question13));
		mapQue.put("Q14", getResources().getString(R.string.question14));
		mapQue.put("Q15", getResources().getString(R.string.question15));
		mapQue.put("Q16", getResources().getString(R.string.question16));
		mapQue.put("Q17", getResources().getString(R.string.question17));
		mapQue.put("Q18", getResources().getString(R.string.question18));
		mapQue.put("Q19", getResources().getString(R.string.question19));
		mapQue.put("Q20", getResources().getString(R.string.question20));
		return mapQue;
	}

	private void setPreNext(int currentque) {
		int tmpnext = 0;
		int tmppre = 0;

		if (currentque == 1 && mapResult.isEmpty()) {
			haq_btnPre.setVisibility(View.INVISIBLE);
			haq_btnNext.setVisibility(View.INVISIBLE);
		}
		tmpnext = currentque + 1;
		if (mapResult.get("Q" + String.valueOf(tmpnext)) == null) {

			if (mapResult.get("Q" + String.valueOf(currentque)) != null) {
				haq_btnNext.setVisibility(View.VISIBLE);
			} else {
				haq_btnNext.setVisibility(View.INVISIBLE);
			}

		} else {
			haq_btnNext.setVisibility(View.VISIBLE);
		}
		tmppre = currentque - 1;
		if (mapResult.get("Q" + String.valueOf(tmppre)) != null) {
			haq_btnPre.setVisibility(View.VISIBLE);
		}
	}
	
/** 
 * calculate the haq value
 * @return
 */
	private String getCountValue() {
		double countValue = 0.00;
		int[] intList = new int[20];
		int max1 = 0;
		int max2 = 0;
		int max3 = 0;
		int max4 = 0;
		int max5 = 0;
		int max6 = 0;
		int max7 = 0;
		int max8 = 0;

		for (int i = 1; i <= 20; i++) {
			intList[i - 1] = Integer.parseInt(mapResult.get("Q" + i));
		}

		for (int a = 0; a < 2; a++) {
			if (intList[a] > max1) {
				max1 = intList[a];
			}
		}

		for (int a = 2; a < 4; a++) {
			if (intList[a] > max2) {
				max2 = intList[a];
			}
		}

		for (int a = 4; a < 7; a++) {
			if (intList[a] > max3) {
				max3 = intList[a];
			}
		}

		for (int a = 7; a < 9; a++) {
			if (intList[a] > max4) {
				max4 = intList[a];
			}
		}

		for (int a = 9; a < 12; a++) {
			if (intList[a] > max5) {
				max5 = intList[a];
			}
		}

		for (int a = 12; a < 14; a++) {
			if (intList[a] > max6) {
				max6 = intList[a];
			}
		}

		for (int a = 14; a < 17; a++) {
			if (intList[a] > max7) {
				max7 = intList[a];
			}
		}

		for (int a = 17; a < 20; a++) {
			if (intList[a] > max8) {
				max8 = intList[a];
			}
		}

		int total = max1 + max2 + max3 + max4 + max5 + max6 + max7 + max8;
		countValue = (double) total / 8;
		// BigDecimal countV = new BigDecimal(Double.toString(countValue));
		DecimalFormat df = new DecimalFormat("0.00");
		String strValue = df.format(countValue);

		return strValue;

	}
/** 
 * save the haqcount to db
 * @param Haq_count
 * @return
 */
	private boolean saveHaqCount(String Haq_count) {
		String currentTime = System.currentTimeMillis() + "";
		HaqVasBean mHaqVasBean = new HaqVasBean();
		mHaqVasBean.setCount(Haq_count);
		mHaqVasBean.setDateNo(currentTime);
		mHaqVasBean.setType("2");
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
