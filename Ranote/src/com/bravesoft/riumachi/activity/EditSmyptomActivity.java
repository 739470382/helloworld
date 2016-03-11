package com.bravesoft.riumachi.activity;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.adapter.EditTextAdapter;
import com.bravesoft.riumachi.appliction.App;
import com.bravesoft.riumachi.bean.FileDownRequestBean;
import com.bravesoft.riumachi.bean.SymptomMemoBean;
import com.bravesoft.riumachi.callback.CommonCallBack;
import com.bravesoft.riumachi.constant.SymptomTagConfig;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.database.DBHelper;
import com.bravesoft.riumachi.database.DBOperator;
import com.bravesoft.riumachi.http.HttpRequest;
import com.bravesoft.riumachi.layout.LayoutUtils;
import com.bravesoft.riumachi.layout.ScreenConfig;
import com.bravesoft.riumachi.util.DateFormatUtil;
import com.bravesoft.riumachi.util.MyUtils;
import com.bravesoft.riumachi.util.StringCalculateUtils;
import com.bravesoft.riumachi.util.Utils;
import com.bravesoft.riumachi.widget.NoScrollListView;

public class EditSmyptomActivity extends BaseActivity implements
		OnClickListener {


	private TextView mTxtTitle;//Back button
	private TextView mTxtEditMark;//Modify button
	private TextView mTxtSmyptomDate;//Time Display
	private ImageView mImgSmyptomMark;//Pen pictures
	private ImageView mImgShow;//Show picture
	private String mSmyptomId;//Id Symptom instance
	private SQLiteDatabase db;
	private DBHelper mDbHelper;
	private SymptomMemoBean symptombean;
	private NoScrollListView mListView;
	private EditTextAdapter mAdapter;
	private LinearLayout mLinearBackground;
	private int IMAGE_BACKGROUND = 100;
	private int TEXT_BACKGROUND = 200;
	private String mTokenString;
	private Bitmap bmp;
	Handler mHandler;

	View title_button_left;
	View title_button_right;

	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		mDbHelper = new DBHelper(getApplicationContext());
		db = mDbHelper.getReadableDatabase();
		setContentView(R.layout.activity_smyptom_edit);
		if (savedInstanceState != null) {
			mSmyptomId = getIntent().getStringExtra(SymptomTagConfig.SMYPTOM_ID);
		} else if (getIntent() != null) {
			mSmyptomId = getIntent().getStringExtra(SymptomTagConfig.SMYPTOM_ID);
		} else {
			mSmyptomId = "1";
		}
		mTokenString = App.getInstance().getmLoginUtils().getToken();
		initView();

	}


	

	private void initView() {
		
		getRateView(R.id.relative_title_other, true);
		getRateView(R.id.img_back, true);
		getRateView(R.id.horizontal_line_one, true);
		
		mImgSmyptomMark = getRateView(R.id.img_smyptom_type_mark, true);
		mImgShow = getRateView(R.id.smyptom_image_show, true);
		title_button_left = getRateView(R.id.title_button_left, true);
		title_button_right = getRateView(R.id.title_button_right, true);
		
		title_button_left.setOnClickListener(this);
		title_button_right.setOnClickListener(this);
		//backgroudlayer.setOnClickListener(this);
		
		mTxtTitle = getTextView(R.id.txt_title_name, true, 37 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		mTxtEditMark = getTextView(R.id.txt_operate, true, 33 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mTxtSmyptomDate = getTextView(R.id.txt_smyptom_date, true, 34 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		mListView = getRateView(R.id.list_memo, true);
		mLinearBackground = (LinearLayout) findViewById(R.id.linear_edit_symptom);
		initContent();

	}

	private void initContent() {
		symptombean = DBOperator.querySymptomMemoById(db, mSmyptomId);
		mTxtTitle.setText(R.string.edit_symptom_title);
		//mImgSmyptomMark.setImageDrawable(R.drawable.icon_pen);
		mTxtEditMark.setText(R.string.schedule_edit_title);
		mTxtSmyptomDate.setText(DateFormatUtil.getStringDataByLong(Long.parseLong(symptombean.getCreateTime()))+Utils.getStringWeek(Long.parseLong(symptombean.getCreateTime())));
		
		List<String> data = StringCalculateUtils.getSplitStringArray(this,symptombean.getName(),28 ,80 ,80);

		
		mAdapter = new EditTextAdapter(this, data);
		mListView.setAdapter(mAdapter);
		if((symptombean.getLocal_path() != null && symptombean.getLocal_path().length() !=0 && 
				!symptombean.getLocal_path().equals("null"))){
			File imageFile = new File(symptombean.getLocal_path());
			if(imageFile != null && imageFile.exists()){
			mImgShow.setVisibility(View.VISIBLE);
			
			int degree =  Utils.readPictureDegree(symptombean.getLocal_path());  
			 //bmp = MyUtils.decodeBitmap(symptombean.getLocal_path());
			//bmp = Utils.rotaingImageView(degree, bmp);  
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(symptombean.getLocal_path(), opt);
			float imageWidth = opt.outWidth;
			float imageheight = opt.outHeight;
			int width = LayoutUtils.getRate4px(560);
			float scale = width/imageWidth;
		    int height = (int) (imageheight*scale);
			
			LayoutParams params = (LayoutParams) mImgShow
					 .getLayoutParams();
					 params.width = width;
					 params.height = height;
					 mImgShow.setLayoutParams(params);
			bmp = Utils.readBitmapAutoSize(symptombean
								.getLocal_path(),width,height);
			mImgShow.setImageBitmap(bmp);
			}else{
				if(mTokenString != null&&symptombean.getUrl() != null && !symptombean.getUrl().equals("")){
			     loadimage();
				}else{
					mImgShow.setImageBitmap(null);
					mImgShow.setVisibility(View.GONE);
				}
				
			}
		}else if(symptombean.getUrl() != null && !symptombean.getUrl().equals("")){
			if(mTokenString != null){
			loadimage();
			}else{
				mImgShow.setImageBitmap(null);
				mImgShow.setVisibility(View.GONE);
			}
			
		}
			else{
		
			mImgShow.setImageBitmap(null);
			mImgShow.setVisibility(View.GONE);
		}
		
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initContent();
		int width = ScreenConfig.SCRREN_W - LayoutUtils.getRate4density(80);
	}

	@Override
	public void onClick(View view) {

		Intent intent;
		switch (view.getId()) {

		case R.id.title_button_left:
			
			finishActivity();
			overridePendingTransition(R.anim.activityout, R.anim.activityin);
			break;
		case R.id.title_button_right:

			intent = new Intent(this, SymptomAddActivity.class);
			intent.putExtra("open_type", "open_type_edit");
			intent.putExtra("my_smyptom_id", symptombean.getId()+"");
			startActivity(intent);
			finish();
			break;
		case R.id.symptom_add_background:

			break;
		
		default:
			break;
		}

	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		db.close();
		if(bmp != null && !bmp.isRecycled()){ 
		        
			bmp.recycle(); 
			bmp = null; 
		} 
		System.gc();
	}

	public void loadimage(){
		mProgressDialog.showDialog();
		mHandler = new Handler() {  
	          public void handleMessage(Message msg) {   
	               switch (msg.what) {   
	               case 1:    
	            	   File imageFile = new File(symptombean.getLocal_path());
	       			if(imageFile != null && imageFile.exists()){
	            	   int degree =  Utils.readPictureDegree(symptombean.getLocal_path());  
	       			Bitmap bmp = MyUtils.decodeBitmap(symptombean.getLocal_path());
	       			if(bmp != null){
	       			bmp = Utils.rotaingImageView(degree, bmp);  
	       			float imageWidth = bmp.getWidth();
	       			float imageheight = bmp.getHeight();
	       			int width = LayoutUtils.getRate4px(560);
	       			float scale = width/imageWidth;
	       		    int height = (int) (imageheight*scale);
	       			
	       			LayoutParams params = (LayoutParams) mImgShow
	       					 .getLayoutParams();
	       					 params.width = width;
	       					 params.height = height;
	       					 mImgShow.setLayoutParams(params);
	       			mImgShow.setImageBitmap(bmp);
	       			}
	       			}
	                   break;
	               case 0:
	            	   mImgShow.setImageBitmap(null);
	       			mImgShow.setVisibility(View.GONE);
	            	   break;
	               
	               }   
	               super.handleMessage(msg);   
	          }   
	     };  
	     downLoadImageFile(mTokenString,"2",symptombean.getUrl());
	}
	private void downLoadImageFile(String token,String type,String id) {
		HttpRequest.getInstance().DownLoadImage(token,type,id,
				new CommonCallBack<FileDownRequestBean>() {

					@Override
					public void onSuccess(FileDownRequestBean data) {
						if(data != null && data.getError().equals(200+"")){
						if (data.getFile_url() != null) {
							mProgressDialog.removeDialog();
							String imageString = data.getFile_url();
							String name = DateFormat.format("yyyyMMdd_hhmmss",
									Calendar.getInstance(Locale.CHINA))
									+ ".jpg";
//							Long date = System.currentTimeMillis();
//						    Long day = DateFormatUtil.geDataLongZeroDay(date);
//						    String dayString =  getApplication().getResources()
//						    		.getString(R.string.update_date_pre)+DateFormatUtil.getStringDataByLong(date);
//							App.getInstance().setUpdateTime(dayString);
							
							String localPath = Utils.saveDownLoadimage(name,imageString);
							symptombean.setLocal_path(localPath);
							mDbHelper = new DBHelper(getApplicationContext());
							db = mDbHelper.getReadableDatabase();
							DBOperator.updateSymptomMemo(db, symptombean);
							db.close();
							if(mHandler != null){
							Message message = new Message();      
				            message.what = 1;      
				            mHandler.sendMessage(message); 
							}
							} else{
								
								mProgressDialog.removeDialog();
							}
					}else{
						mProgressDialog.removeDialog();
					}
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onFailed(Throwable throwable, String reason) {
						//mProgressDialog.removeDialog();
						
							mProgressDialog.removeDialog();
							if(mHandler != null){
								Message message = new Message();      
					            message.what = 0;      
					            mHandler.sendMessage(message); 
								}
						// Log.d("FAILED", throwable+"----"+reason);
					}
				});
	}

}
