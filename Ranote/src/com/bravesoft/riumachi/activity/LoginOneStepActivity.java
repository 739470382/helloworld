package com.bravesoft.riumachi.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.bean.SecurityCode;
import com.bravesoft.riumachi.callback.CommonCallBack;
import com.bravesoft.riumachi.constant.RegisterLoginKey;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.http.HttpRequest;
import com.bravesoft.riumachi.util.AppManager;
import com.bravesoft.riumachi.util.CommonUtils;
import com.bravesoft.riumachi.util.MyUtils;
import com.bravesoft.riumachi.util.Utils;

@SuppressLint("NewApi") public class LoginOneStepActivity extends BaseActivity implements
		OnClickListener {

	private TextView setting_title;// title
	private TextView setting_text;// Next button text
	private EditText emailEditText;// email input box
	private ImageView titleImage;// title displayed pictures
	private View title_button_left;// Back button
	private View title_button_right;// Next button
	private String mEmailAddress;
	private String mSecurityCode;
	private boolean emailnotnull = false;
	private boolean isnextable = false;

	/**
	 * @param args
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_login_step_one);
		super.onCreate(savedInstanceState);
		
		initView(savedInstanceState);
	}

	private void initView(Bundle savedInstanceState) {

		getRateView(R.id.relative_setting_main, true);
		getRateView(R.id.linlearlayout_setting_top, true);
		getRateView(R.id.user_page1, true);
		titleImage = getRateView(R.id.setting_image, true);
		titleImage.setOnClickListener(this);
		title_button_left = getRateView(R.id.title_button_left, true);
		title_button_right = getRateView(R.id.title_button_right, true);

		title_button_left.setOnClickListener(this);
		title_button_right.setOnClickListener(this);
		titleImage.setImageResource(R.drawable.icon_arrow);
		setting_text = getTextView(R.id.setting_text, true, 33 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		setting_text.setText(R.string.next);
		setting_text.setTextColor(this.getResources().getColor(
				R.color.button_not_click));
		setting_text.setOnClickListener(this);
		setting_title = getTextView(R.id.setting_title, true, 37 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		setting_title.setText(R.string.page_user_login_title1);
		getTextView(R.id.user_page1_text, true, 29 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		getTextView(R.id.user_page1_text_mail, true, 24 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		emailEditText = getTextView(R.id.user_page1_txtMail, true, 30 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		getRateView(R.id.login1_set_line, true);
		seteditextlistener();

	}

	public void seteditextlistener() {
		emailEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() != 0) {
					emailnotnull = true;

					setting_text.setTextColor(getApplicationContext()
							.getResources().getColor(R.color.white));
					isnextable = true;

				} else {
					emailnotnull = false;
					setting_text.setTextColor(getApplicationContext()
							.getResources().getColor(R.color.button_not_click));
					isnextable = false;
				}
			}
		});

	}
	@Override
	protected void onResume() {
		super.onResume();
		isnextable =true;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.title_button_right) {
			if (isnextable) {
				CommonUtils.exitKeyboard(this);
				mEmailAddress = emailEditText.getText().toString();
				if (Utils.isEmail(mEmailAddress)) {
					
					if(MyUtils.isNetWork(getApplicationContext())){
					mProgressDialog.showDialog();
					getCode(mEmailAddress);
					}
				} else {
					if (mEmailAddress == null || mEmailAddress.length() == 0) {
						Utils.showErrorMessage(getApplicationContext(), "414");
					} else {
						Utils.showErrorMessage(getApplicationContext(), "424");
					}
				}
			}
		} else if (v.getId() == R.id.title_button_left) {
			CommonUtils.exitKeyboard(this);
			finish();
			overridePendingTransition(R.anim.activityout, R.anim.activityin);

		}
	}

	private void getCode(String emailString) {
		HttpRequest.getInstance().GetSecurityCode(emailString,
				new CommonCallBack<SecurityCode>() {

					@Override
					public void onSuccess(SecurityCode data) {
						mProgressDialog.removeDialog();
						if (data.getSecurity_code() != null) {
							title_button_right.setEnabled(true);
							Log.d("SUCCESS", data.getSecurity_code());
							mSecurityCode = data.getSecurity_code();
							System.out.println(mSecurityCode);
							if (!MyUtils.isNull(mSecurityCode)) {
								Intent intent = new Intent(
										LoginOneStepActivity.this,
										LoginTwoStepActivity.class);
								intent.putExtra(RegisterLoginKey.EMAIL_ADDRESS,
										mEmailAddress);
								intent.putExtra(RegisterLoginKey.SECURITY_CODE,
										mSecurityCode);
								startActivity(intent);
								overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
							} else {
								Utils.showErrorMessage(getApplicationContext(), "0");
							}

						} else {
							Utils.showErrorMessage(getApplicationContext(), data.getError());
						}
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onFailed(Throwable throwable, String reason) {
						mProgressDialog.removeDialog();
						title_button_right.setEnabled(true);
						Utils.showErrorMessage(getApplicationContext(), reason);
					}
				});
	}

}
