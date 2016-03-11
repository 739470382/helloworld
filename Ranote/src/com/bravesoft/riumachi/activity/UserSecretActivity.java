package com.bravesoft.riumachi.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.constant.TextTypeFace;

public class UserSecretActivity extends BaseActivity implements OnClickListener{

	TextView setting_title;
	TextView setting_text;
	EditText passwordEditText;
	TextView setting_login2_buttom;
	TextView setting_login2_buttom_text;
	ImageView titleImage;
	RelativeLayout step2button;
	private WebView webView;
	/**
	 * @param args
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.user_secret);
		super.onCreate(savedInstanceState);
		initView(savedInstanceState);
	}

	private void initView(Bundle savedInstanceState) {

		getRateView(R.id.relative_setting_main, true);
		getRateView(R.id.linlearlayout_setting_top, true);
		
		titleImage = getRateView(R.id.setting_image, true);
		titleImage.setImageResource(R.drawable.icon_arrow);
		titleImage.setOnClickListener(this);
		setting_text = getTextView(R.id.setting_text, true, 33 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		setting_text.setText(R.string.next);
		setting_text.setOnClickListener(this);
		setting_text.setVisibility(View.GONE);
		setting_title = getTextView(R.id.setting_title, true, 37 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		setting_title.setText(R.string.setcomm_lbl_privacy);
		getRateView(R.id.title_button_left, true).setOnClickListener(this);
		
		webView=getRateView(R.id.webview, true);
		webView.loadUrl("file:///android_asset/html/r_policy.html");
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.setting_image || v.getId() ==  R.id.title_button_left){
			finishActivity();
			overridePendingTransition(R.anim.activityout, R.anim.activityin);
		}
	}
	

}