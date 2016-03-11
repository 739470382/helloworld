package com.bravesoft.riumachi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.appliction.App;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.util.AppManager;

public class LoginFailActivity extends BaseActivity implements OnClickListener {

	TextView setting_title;// title

	RelativeLayout buttonToLoginOneStep;// open first step toward registration
	RelativeLayout buttonToMain;// open to the mainActivity

	/**
	 * @param args
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_login_fail);
		super.onCreate(savedInstanceState);

		initView(savedInstanceState);
	}

	private void initView(Bundle savedInstanceState) {

		getRateView(R.id.relative_title_other, true);

		getRateView(R.id.user_page5_1, true);
		buttonToLoginOneStep = getRateView(R.id.user_page5_2, true);
		buttonToMain = getRateView(R.id.user_page5_3, true);
		buttonToLoginOneStep.setOnClickListener(this);
		buttonToMain.setOnClickListener(this);

		setting_title = getTextView(R.id.txt_title_name, true, 37,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		setting_title.setText(R.string.page_user_login_fail);
		getTextView(R.id.user_fail_text, true, 34,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		getTextView(R.id.user_fail_button_text1, true, 30,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		getTextView(R.id.user_fail_button_text2, true, 30,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);

	}

	public void onBackPressed() {
		return;
	}

	public void onClick(View v) {
		if (v.getId() == R.id.user_page5_2) {
			Intent intent = new Intent(LoginFailActivity.this,
					LoginOneStepActivity.class);
			startActivity(intent);

		} else if (v.getId() == R.id.user_page5_3) {
			Intent intent = new Intent(LoginFailActivity.this,
					MainActivity.class);
			App.getInstance().setShowCarlendarFragment(true);
			startActivity(intent);
			AppManager.getAppManager().finishAllAndReopenMainActivity(
					LoginFailActivity.this, RestoreSuccessActivity.class);

		}
	}
}
