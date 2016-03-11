package com.bravesoft.riumachi.activity;

import android.R.layout;
import android.annotation.SuppressLint;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.appliction.App;
import com.bravesoft.riumachi.constant.SerViceBroadCastAction;
import com.bravesoft.riumachi.constant.SharePrefrenceKey;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.layout.LayoutUtils;
import com.bravesoft.riumachi.service.NotifyService;
import com.bravesoft.riumachi.util.AppManager;
import com.bravesoft.riumachi.util.SharePrefUtil;


public class SettingActivity extends BaseActivity implements
 OnClickListener{

	
	TextView login_top_text;//text of login prompt 
	TextView login; //text of login button
	TextView restore_top_text; //text of restore prompt 
	TextView restore;//text of restore button
	TextView data_update_time;//display update time
	TextView setting_title;//text of title
	TextView set_txtMode;//text of mode
	TextView set_txtMode_setumei;//text of mode detail
	ToggleButton set_mode_toggle;//Switching Mode
	TextView set_txtRole;//text of role
	ImageView set_imgRight1;//
	TextView set_txtPri;//text of privacy
	ImageView set_imgRight2;
	TextView setting_text;
	RelativeLayout login_button;//button of login
	RelativeLayout listone_button;//list item of mode
	RelativeLayout listtwo_button;// list item of role
	RelativeLayout listthree_button;//list item of privacy
	RelativeLayout restore_button;//button of restore
	ImageView title_Image;//image on title
	View title_button_left;//button of title left
	View title_button_right;//button of title right
	int mode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		initView(savedInstanceState);
	}

	private void initView(Bundle savedInstanceState) {

		getRateView(R.id.relative_setting_main, true);
		getRateView(R.id.setting_scroll, true);
		getRateView(R.id.setting_relative_page, true);
		getRateView(R.id.linlearlayout_setting_top, true);
		title_Image = getRateView(R.id.setting_image, true);
		title_Image.setImageResource(R.drawable.icon_cancel);
		title_button_left = getRateView(R.id.title_button_left, true);
		title_button_right = getRateView(R.id.title_button_right, true);
		title_button_left.setOnClickListener(this);
		title_button_right.setOnClickListener(this);
		
		title_Image.setOnClickListener(this);
		setting_text = (TextView)getTextView(R.id.setting_text,true,28,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		setting_text.setVisibility(View.GONE);
		setting_title = (TextView)getTextView(R.id.setting_title,true,37 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		setting_title.setText(this.getResources().getString(R.string.setting_popup_setting));
		login_top_text = getTextView(R.id.setting_login_top_text,true,28,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		login = getTextView(R.id.setting_login_top, true,30,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
	    
		
		login_top_text.setText(R.string.setting_text1);
	    login_button = getRateView(R.id.user_setting_button1, true);
	    login_button.setOnClickListener(this);
	    if(App.getInstance().getmLoginUtils().isLogin()){
	    	login_button.setBackgroundColor(this.getResources().getColor(R.color.loginbutton_not_click));
	    	login.setText(R.string.login_button_notclick);
	    	login_button.setEnabled(false);
	    }
	    
	    restore_top_text = getTextView(R.id.setting_restore_top_text, true,28,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
	    restore = getTextView(R.id.setting_restore_top, true,30,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
	    restore_top_text.setText(R.string.setting_text2);
	    restore_button = getRateView(R.id.user_setting_button2, true);
	    restore_button.setOnClickListener(this);
	    
	    getRateView(R.id.update_time_layout, true);
	    data_update_time = getTextView(R.id.data_update_time, true,23,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
	    String updatetime = App.getInstance().getUpdateTime();
//	    if(updatetime != null){
//	    	data_update_time.setText(updatetime);
//	    }else{
//	    data_update_time.setVisibility(View.INVISIBLE);
//	    }
	    
	    listone_button = getRateView(R.id.set_footer_line1, true);
	    listtwo_button = getRateView(R.id.set_footer_line2, true);
	    listthree_button = getRateView(R.id.set_footer_line3, true);
	    listone_button.setOnClickListener(this);
	    listtwo_button.setOnClickListener(this);
	    listthree_button.setOnClickListener(this);
	    
	    set_txtMode = getTextView(R.id.set_txtMode, true,30,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
	    set_txtMode_setumei = getTextView(R.id.set_txtMode_setumei, true,20,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
	    mode = SharePrefUtil.getInt(getApplicationContext(), SharePrefrenceKey.APP_MODE, 0);
	    set_mode_toggle = getRateView(R.id.set_mode_toggle, true);
	    
	    if(mode == App.SEE_THE_DOCTER_MODE){
	    	set_mode_toggle.setChecked(true);
	    }else{
	    	set_mode_toggle.setChecked(false);
	    }
	    set_mode_toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
	    	  
	        @Override  
	        public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {  
	            if(isChecked){  
	                  
	    			setAppMode(App.SEE_THE_DOCTER_MODE);
	    			SharePrefUtil.putInt(getApplicationContext(),SharePrefrenceKey.APP_MODE, App.SEE_THE_DOCTER_MODE);
	    			//checkAppModeChange();
	            }else{  
	                
	    			setAppMode(App.NORMAL_MODE);
	    			SharePrefUtil.putInt(getApplicationContext(),SharePrefrenceKey.APP_MODE, App.NORMAL_MODE );
	    			//checkAppModeChange();
	            } 
	            Intent intent = new Intent();
	            intent.setAction(SerViceBroadCastAction.NOTIFY_APP_MODE_CHANGED);
				SettingActivity.this.sendBroadcast(intent);
	        }  
	    }); 
	    getRateView(R.id.set_footer_line2, true);
	    set_txtRole = getTextView(R.id.set_txtRole, true, 30,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
	    set_imgRight1 = getRateView(R.id.set_imgRight1, true);
	    
	    getRateView(R.id.set_footer_line3, true);
	    set_txtPri = getTextView(R.id.set_txtPri, true, 30,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
	    set_imgRight2 = getRateView(R.id.set_imgRight2, true);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.user_setting_button1){
			Intent intent = new Intent(SettingActivity.this, LoginOneStepActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}else if(v.getId() == R.id.user_setting_button2){
			Intent intent = new Intent(SettingActivity.this, RestoreOneStepActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}
		else if(v.getId() == R.id.title_button_left){
			finish();
			overridePendingTransition(0,R.anim.activity_bottom_out);
		}
		else if(v.getId() == R.id.set_footer_line2){
			Intent intent = new Intent(SettingActivity.this,UserRuleActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}
        else if(v.getId() == R.id.set_footer_line3){
        	Intent intent = new Intent(SettingActivity.this,UserSecretActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
		}
	}
}
