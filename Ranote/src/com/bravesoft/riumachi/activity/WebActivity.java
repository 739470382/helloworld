package com.bravesoft.riumachi.activity;

import com.bravesoft.riumachi.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.ImageView;

public class WebActivity extends BaseActivity implements OnClickListener{
	private ImageView img_back;
	private WebView webView;
	private View view_back,view_add;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_web);
			initView();
		}

		@SuppressLint("NewApi") private void initView() {
			img_back=getRateView(R.id.img_back, true);
			webView=getRateView(R.id.webview, true);
			getRateView(R.id.relative_title_other, true);
			
			view_back=getRateView(R.id.view_back, true);
			view_back.setOnClickListener(this);
			
			webView.loadUrl("http://www.kawasaki-m.ac.jp/rheumatology/ ");
			// Achieve webview page zoom
			webView.getSettings().setUseWideViewPort(true);
			webView.getSettings().setBuiltInZoomControls(true);
			webView.getSettings().setDisplayZoomControls(false);
			webView.getSettings().setJavaScriptEnabled(true);
			//Load the page display mode adaptive screen ---
			WebSettings mWebSettings = webView.getSettings();
			mWebSettings.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
			webView.getSettings().setLoadWithOverviewMode(true);
			webView.setWebViewClient(new WebViewClient(){

				@Override
				public boolean shouldOverrideUrlLoading(WebView view, String url) {
					view.loadUrl(url);
					return super.shouldOverrideUrlLoading(view, url);
				}
				
			});
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.view_back:
				finish();
				break;

			default:
				break;
			}
		}
}
