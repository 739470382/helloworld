package com.bravesoft.riumachi.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.appliction.App;
import com.bravesoft.riumachi.bean.FileDownRequestBean;
import com.bravesoft.riumachi.bean.FileUpRequestBean;
import com.bravesoft.riumachi.callback.CommonCallBack;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.http.HttpRequest;
import com.bravesoft.riumachi.util.AppManager;
import com.bravesoft.riumachi.util.MyUtils;
import com.bravesoft.riumachi.util.Utils;
import com.loopj.android.http.Base64;

public class RestoreSuccessActivity extends BaseActivity implements
		OnClickListener {

	TextView setting_title;//title
	ImageView titleImage;// 
	RelativeLayout buttonToMain;//open mainActivity
	
	
	
	/**
	 * @param args
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_restore_success);
		super.onCreate(savedInstanceState);
		
		initView(savedInstanceState);
	}

	private void initView(Bundle savedInstanceState) {

		getRateView(R.id.relative_title_other, true);
		
		getRateView(R.id.user_restore_success_page, true);
		buttonToMain = getRateView(R.id.user_restore_page, true);
		buttonToMain.setOnClickListener(this);

		

		setting_title = getTextView(R.id.txt_title_name, true, 37 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		setting_title.setText(R.string.data_reback_success);
		getTextView(R.id.user_restore_success_text, true, 34 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		getTextView(R.id.user_restore_success_button_text, true, 30 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);

	}
	
	@Override
	public void onBackPressed() {
		return;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.user_restore_page) {
			App.isfirstcome = false;
			App.getInstance().setShowCarlendarFragment(true);
			AppManager.getAppManager().finishAllAndReopenMainActivity(RestoreSuccessActivity.this, RestoreSuccessActivity.class);
	

		}
	}
	
	
  
}

