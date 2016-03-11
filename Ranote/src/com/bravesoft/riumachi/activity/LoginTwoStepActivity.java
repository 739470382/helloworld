package com.bravesoft.riumachi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.bean.SecurityCode;
import com.bravesoft.riumachi.callback.CommonCallBack;
import com.bravesoft.riumachi.constant.RegisterLoginKey;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.http.HttpRequest;
import com.bravesoft.riumachi.util.CommonUtils;
import com.bravesoft.riumachi.util.LogUtil;
import com.bravesoft.riumachi.util.MyUtils;
import com.bravesoft.riumachi.util.Utils;

public class LoginTwoStepActivity extends BaseActivity implements
		OnClickListener {

	private TextView setting_title;// title
	private TextView setting_text;// Next button text
	private EditText passwordEditText;// securitycode input box
	private TextView setting_login2_buttom;
	private TextView setting_login2_buttom_text;
	private ImageView titleImage;// title displayed pictures
	private RelativeLayout step2button;// Button at the bottom of the
	private View title_button_left;// Back button
	private View title_button_right;// Next button
	private String mEmailString;
	private String mSecurityCode;
	private boolean passwordnotnull = false;
	private boolean isnextable = false;

	

	/**
	 * @param args
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_login_step_two);
		if (savedInstanceState != null) {
			mEmailString = savedInstanceState
					.getString(RegisterLoginKey.EMAIL_ADDRESS);
			mSecurityCode = savedInstanceState
					.getString(RegisterLoginKey.SECURITY_CODE);
		}
		if (getIntent() != null) {
			mEmailString = getIntent().getExtras().getString(
					RegisterLoginKey.EMAIL_ADDRESS);
			mSecurityCode = getIntent().getExtras().getString(
					RegisterLoginKey.SECURITY_CODE);
		}
		super.onCreate(savedInstanceState);
		initView(savedInstanceState);
	}

	private void initView(Bundle savedInstanceState) {

		getRateView(R.id.relative_setting_main, true);
		getRateView(R.id.linlearlayout_setting_top, true);
		getRateView(R.id.user_page2, true);
		titleImage = getRateView(R.id.setting_image, true);
		titleImage.setImageResource(R.drawable.icon_arrow);
		titleImage.setOnClickListener(this);
		title_button_left = getRateView(R.id.title_button_left, true);
		title_button_right = getRateView(R.id.title_button_right, true);
		title_button_left.setOnClickListener(this);
		title_button_right.setOnClickListener(this);

		setting_text = getTextView(R.id.setting_text, true, 33 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		setting_text.setText(R.string.next);
		setting_text.setOnClickListener(this);
		setting_text.setTextColor(this.getResources().getColor(
				R.color.button_not_click));
		setting_title = getTextView(R.id.setting_title, true, 37 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		setting_title.setText(R.string.page_user_login_title2);
		getTextView(R.id.user_page2_text, true, 29 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		getTextView(R.id.page2_text_code, true, 24 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		passwordEditText = getTextView(R.id.user_page2_txtCode, true, 30 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		getRateView(R.id.user_page2_buttom, true);
		setting_login2_buttom = getTextView(R.id.setting_login2_buttom, true,
				30);
		setting_login2_buttom.setOnClickListener(this);
		getRateView(R.id.login2_set_line2, true);
		setting_login2_buttom_text = getTextView(
				R.id.setting_login2_buttom_text, true, 30 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		step2button = getRateView(R.id.user_step2_button, true);
		step2button.setOnClickListener(this);
		seteditextlistener();

	}

	public void seteditextlistener() {
		passwordEditText.addTextChangedListener(new TextWatcher() {

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
					// view_add.setClickable(true);
					passwordnotnull = true;

					setting_text.setTextColor(getApplicationContext()
							.getResources().getColor(R.color.white));
					isnextable = true;

				} else {
					passwordnotnull = false;
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
	
		
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.title_button_right) {
			CommonUtils.exitKeyboard(this);
			System.out.println(mSecurityCode.toLowerCase());
			if (isnextable) {
				String text = passwordEditText.getText().toString();
				if (text != null && text.length() != 0) {
					if (text.toLowerCase().equals(mSecurityCode.toLowerCase())) {
						
						Intent intent = new Intent(LoginTwoStepActivity.this,
								LoginThreeStepActivity.class);
						intent.putExtra(RegisterLoginKey.EMAIL_ADDRESS,
								mEmailString);
						intent.putExtra(RegisterLoginKey.SECURITY_CODE,
								mSecurityCode);
						startActivity(intent);
						overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
					} else {
						Utils.showErrorMessage(getApplicationContext(),"421");
					}
				} else {

					Toast.makeText(
							this,
							this.getResources().getString(
									R.string.input_identify_code_is_null), 1000)
							.show();

				}
			}
		} else if (v.getId() == R.id.user_step2_button||v.getId() == R.id.setting_login2_buttom){
			CommonUtils.exitKeyboard(this);
			mProgressDialog.showDialog();
			getCode(mEmailString);
		
		
		} else if (v.getId() == R.id.title_button_left) {
			CommonUtils.exitKeyboard(this);
			finish();
			overridePendingTransition(R.anim.activityout, R.anim.activityin);
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(RegisterLoginKey.EMAIL_ADDRESS, mEmailString);
		outState.putString(RegisterLoginKey.SECURITY_CODE, mSecurityCode);
		super.onSaveInstanceState(outState);
	}
	
	private void getCode(String emailString) {
		HttpRequest.getInstance().GetSecurityCode(emailString,
				new CommonCallBack<SecurityCode>() {

					@Override
					public void onSuccess(SecurityCode data) {
						mProgressDialog.removeDialog();
						if (data != null) {
							title_button_right.setEnabled(true);
							Log.d("SUCCESS", data.getSecurity_code());
							mSecurityCode = data.getSecurity_code();
							if (!MyUtils.isNull(mSecurityCode)) {
								
							} else {
								
							}

						} else {
							title_button_right.setEnabled(true);
						}
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onFailed(Throwable throwable, String reason) {
						mProgressDialog.removeDialog();
						title_button_right.setEnabled(true);
					}
				});
	}
}
