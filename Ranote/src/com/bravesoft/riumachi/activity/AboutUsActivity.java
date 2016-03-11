package com.bravesoft.riumachi.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.constant.TextTypeFace;

public class AboutUsActivity extends BaseActivity implements OnClickListener {
	private TextView mtitle_name;
	private RelativeLayout layout_web;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_about_us);
		super.onCreate(savedInstanceState);
		initView(savedInstanceState);
	}

	private void initView(Bundle savedInstanceState) {
		getRateView(R.id.relative_title, true);
		getRateView(R.id.line, true);
		getRateView(R.id.line1, true);
		getRateView(R.id.line2, true);
		getRateView(R.id.line3, true);
		getRateView(R.id.line4, true);
		getRateView(R.id.img_back, true).setOnClickListener(this);
		getRateView(R.id.rnote_produce_image, true);
		getRateView(R.id.relative_good_docter, true).setOnClickListener(this);
		getRateView(R.id.relative_good_docter_web, true).setOnClickListener(this);
		getTextView(R.id.ab_us_jianxiu, true, 24,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		getTextView(R.id.note_hospital, true, 32,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		layout_web = getRateView(R.id.jianxiu_data, true);
		getTextView(R.id.rnote_professor, true, 24,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		getTextView(R.id.rnote_professor_name, true, 32,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		getTextView(R.id.txt_rnote_professor_name, true, 32,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		getTextView(R.id.rnote_produce, true, 24,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		getTextView(R.id.rnote_copyright, true, 24,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		getTextView(R.id.rnote_copyright1, true, 30,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		getTextView(R.id.rnote_copyright2, true, 30,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);

		layout_web.setOnClickListener(this);

		mtitle_name = getTextView(R.id.txt_title_name, true, 37 , TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		mtitle_name.setText(this.getResources().getString(
				R.string.about_us_title));

	}

	@Override
	public void onClick(View v) {
		Uri content_url;
		switch (v.getId()) {
		case R.id.img_back:
			finish();
			overridePendingTransition(0,R.anim.activity_bottom_out);
			break;
		case R.id.jianxiu_data:
			// intent=new Intent(AboutUsActivity.this,WebActivity.class);
			intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			content_url = Uri
					.parse("http://www.kawasaki-m.ac.jp/rheumatology/ ");
			intent.setData(content_url);
			startActivity(intent);
//			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			break;
		case R.id.relative_good_docter:
			intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			content_url = Uri
					.parse("http://www.e-oishasan.net/");
			intent.setData(content_url);
			startActivity(intent);
			break;
		case R.id.relative_good_docter_web:
			intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			content_url = Uri
					.parse("http://www.e-oishasan.net/site/morita/");
			intent.setData(content_url);
			startActivity(intent);
			break;

		default:
			break;
		}

	}

}
