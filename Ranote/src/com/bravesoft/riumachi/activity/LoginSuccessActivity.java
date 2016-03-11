package com.bravesoft.riumachi.activity;

import java.io.File;
import java.util.List;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.appliction.App;
import com.bravesoft.riumachi.bean.FileUpRequestBean;
import com.bravesoft.riumachi.bean.SymptomImageBean;
import com.bravesoft.riumachi.bean.SymptomMemoBean;
import com.bravesoft.riumachi.callback.CommonCallBack;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.database.DBHelper;
import com.bravesoft.riumachi.database.DBOperator;
import com.bravesoft.riumachi.database.ImageDBHelper;
import com.bravesoft.riumachi.database.ImageDBOperator;
import com.bravesoft.riumachi.http.HttpRequest;
import com.bravesoft.riumachi.util.AppManager;
import com.bravesoft.riumachi.util.DateFormatUtil;
import com.bravesoft.riumachi.util.MyUtils;
import com.bravesoft.riumachi.util.Utils;

public class LoginSuccessActivity extends BaseActivity implements
		OnClickListener {

	TextView setting_title;// title
	RelativeLayout successButton;// Login successful open to the main interface
	
	

	/**
	 * @param args
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_login_success);
		super.onCreate(savedInstanceState);
		
		initView(savedInstanceState);
		
		
	}

	private void initView(Bundle savedInstanceState) {

		getRateView(R.id.relative_title_other, true);
		getRateView(R.id.user_page4_1, true);
		successButton = getRateView(R.id.user_page4_2, true);
		successButton.setOnClickListener(this);

		setting_title = getTextView(R.id.txt_title_name, true, 37,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		setting_title.setText(R.string.page_user_login_success);
		getTextView(R.id.user_success_text, true, 34,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		getTextView(R.id.user_success_button_text, true, 30,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);

	}
	
	@Override
	public void onBackPressed() {
		return;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.user_page4_2) {

			Intent intent = new Intent(LoginSuccessActivity.this,
					MainActivity.class);

			App.getInstance().setShowCarlendarFragment(true);

			startActivity(intent);
			AppManager.getAppManager().finishAllAndReopenMainActivity(
					LoginSuccessActivity.this, RestoreSuccessActivity.class);

		}
	}

	
}