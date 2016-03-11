package com.bravesoft.riumachi.activity;

import java.io.File;
import java.util.List;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.appliction.App;
import com.bravesoft.riumachi.bean.FileUpRequestBean;
import com.bravesoft.riumachi.bean.SymptomImageBean;
import com.bravesoft.riumachi.bean.SymptomMemoBean;
import com.bravesoft.riumachi.bean.UserRegist;
import com.bravesoft.riumachi.callback.CommonCallBack;
import com.bravesoft.riumachi.constant.RegisterLoginKey;
import com.bravesoft.riumachi.constant.SDConfig;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.database.DBHelper;
import com.bravesoft.riumachi.database.DBOperator;
import com.bravesoft.riumachi.database.ImageDBHelper;
import com.bravesoft.riumachi.database.ImageDBOperator;
import com.bravesoft.riumachi.http.HttpRequest;
import com.bravesoft.riumachi.util.CommonUtils;
import com.bravesoft.riumachi.util.DateFormatUtil;
import com.bravesoft.riumachi.util.MyUtils;
import com.bravesoft.riumachi.util.Utils;

public class LoginThreeStepActivity extends BaseActivity implements
		OnClickListener {

	private TextView setting_title;// title
	private TextView setting_text;// Next button text
	private EditText emailEditText;// email input box
	private EditText passwordEditText1;// Password input box
	private EditText passwordEditText2;// Repeat password input box
	private TextView setting_login2_buttom;
	private TextView setting_login2_buttom_text;
	private ImageView titleImage;// title displayed pictures
	private View title_button_left;// Back button
	private View title_button_right;// Next button
	private String mSecurityCode;
	private String mEmailString;
	private String mTokenString;
	private String mUserIdString;
	private boolean emailnotnull = true;
	private boolean passwordnotnull = false;
	private boolean repasswordnotnull = false;
	private boolean issendable = false;
	private SQLiteDatabase db;
	private DBHelper mDbHelper;
	private SQLiteDatabase imagedb;
	private ImageDBHelper mImageDbHelper;
	private List<SymptomMemoBean> memoBeans;
	private List<SymptomImageBean> list_image_upload;
	private boolean imageIsUpFail = false;
	private int uploadFlag = 0;
	
	

	/**
	 * @param args
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_login_step_three);

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
		mDbHelper = new DBHelper(getApplicationContext());
		db = mDbHelper.getReadableDatabase();
		mImageDbHelper = new ImageDBHelper(getApplicationContext());
		imagedb = mImageDbHelper.getReadableDatabase();
		super.onCreate(savedInstanceState);
		initView(savedInstanceState);
	}

	private void initView(Bundle savedInstanceState) {

		getRateView(R.id.relative_setting_main, true);
		getRateView(R.id.linlearlayout_setting_top, true);
		getRateView(R.id.user_page3_1, true);
		getRateView(R.id.user_page3_2, true);
		getRateView(R.id.user_page3_3, true);
		titleImage = getRateView(R.id.setting_image, true);
		titleImage.setImageResource(R.drawable.icon_arrow);
		titleImage.setOnClickListener(this);
		title_button_left = getRateView(R.id.title_button_left, true);
		title_button_right = getRateView(R.id.title_button_right, true);
		title_button_left.setOnClickListener(this);
		title_button_right.setOnClickListener(this);

		setting_text = getTextView(R.id.setting_text, true, 33,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		setting_text.setText(R.string.send);
		setting_text.setOnClickListener(this);
		setting_text.setTextColor(this.getResources().getColor(
				R.color.button_not_click));
		setting_title = getTextView(R.id.setting_title, true, 37,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		setting_title.setText(R.string.page_user_login_title3);
		getTextView(R.id.user_page3_text, true, 29,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		getTextView(R.id.user_page3_text_mail, true, 24,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		emailEditText = getTextView(R.id.user_page3_txtMail, true, 30,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		emailEditText.setText(mEmailString);
		emailEditText.setEnabled(false);
		getTextView(R.id.user_page3_text_psw1, true, 24,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		passwordEditText1 = getTextView(R.id.user_page3_txt_psw1, true, 30,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		getTextView(R.id.user_page3_text_psw2, true, 24,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		passwordEditText2 = getTextView(R.id.user_page3_txt_psw2, true, 30,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
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
					// view_add.setClickable(true);
					emailnotnull = true;
					if (passwordnotnull && repasswordnotnull) {
						setting_text.setTextColor(getApplicationContext()
								.getResources().getColor(R.color.white));
						issendable = true;
					}
				} else {
					emailnotnull = false;
					setting_text.setTextColor(getApplicationContext()
							.getResources().getColor(R.color.button_not_click));
					issendable = false;
				}
			}
		});
		passwordEditText1.addTextChangedListener(new TextWatcher() {

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
					passwordnotnull = true;
					if (emailnotnull && repasswordnotnull) {
						setting_text.setTextColor(getApplicationContext()
								.getResources().getColor(R.color.white));
						issendable = true;
					}
				} else {
					passwordnotnull = false;
					setting_text.setTextColor(getApplicationContext()
							.getResources().getColor(R.color.button_not_click));
					issendable = false;
				}
			}
		});
		passwordEditText2.addTextChangedListener(new TextWatcher() {

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
					repasswordnotnull = true;
					if (emailnotnull && passwordnotnull) {
						setting_text.setTextColor(getApplicationContext()
								.getResources().getColor(R.color.white));
						issendable = true;
					}
				} else {
					repasswordnotnull = false;
					setting_text.setTextColor(getApplicationContext()
							.getResources().getColor(R.color.button_not_click));
					issendable = false;
				}
			}
		});

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.title_button_right) {
			CommonUtils.exitKeyboard(this);
			if (issendable) {
				String emailtext = emailEditText.getText().toString();
				String passwordtext1 = passwordEditText1.getText().toString();
				String passwordtext2 = passwordEditText2.getText().toString();
				if (checkinput(emailEditText.getText().toString(),
						passwordEditText1.getText().toString(),
						passwordEditText2.getText().toString())) {

					if (MyUtils.isNetWork(getApplicationContext())) {
						mProgressDialog.showDialog();
						onRegister(mEmailString, mSecurityCode, passwordtext1);
					}
				} else {
					if (emailtext == null || emailtext.length() == 0
							|| passwordtext1 == null
							|| passwordtext1.length() == 0
							|| passwordtext2 == null
							|| passwordtext2.length() == 0) {

						if (emailtext == null || emailtext.length() == 0) {
							Utils.showErrorMessage(getApplicationContext(), "414");
						} else {
							Toast.makeText(
									this,
									this.getResources().getString(
											R.string.input_password_is_null),
									1000).show();
						}
					} else if (!passwordtext1.equals(passwordtext2)) {
						Toast.makeText(
								this,
								this.getResources().getString(
										R.string.input_password_difference),
								1000).show();
					} else {
						if (!Utils.isEmail(emailtext)) {
							Utils.showErrorMessage(getApplicationContext(), "424");
						} else {
							Utils.showErrorMessage(getApplicationContext(), "422");
						}
					}

				}
			}
		} else if (v.getId() == R.id.title_button_left) {
			CommonUtils.exitKeyboard(this);
			finish();
			overridePendingTransition(R.anim.activityout, R.anim.activityin);
		}

	}

	public boolean checkinput(String email, String pwd1, String pwd2) {

		if (Utils.isEmail(email) && (6 <= pwd1.length() && pwd1.length() <= 30)
				&& (6 <= pwd2.length() && pwd2.length() <= 30)
				&& (pwd1.equals(pwd2))) {
			return true;
		}

		return false;
	}

	private void onRegister(String emailString, String codeString,
			String passwordString) {
		HttpRequest.getInstance().Register(emailString, passwordString,
				codeString, new CommonCallBack<UserRegist>() {

					@Override
					public void onStart() {

					}

					@Override
					public void onFailed(Throwable throwable, String reason) {
						mProgressDialog.removeDialog();
						Utils.showErrorMessage(getApplicationContext(), reason);
							Intent intent = new Intent(
									LoginThreeStepActivity.this,
									LoginFailActivity.class);
							startActivity(intent);
						
					}

					@Override
					public void onSuccess(UserRegist data) {
						mProgressDialog.removeDialog();
						if(data != null && data.getError().equals("200")){
						if (data.getData() != null && !data.getData().equals("")) {
							title_button_right.setEnabled(true);
							mTokenString = data.getData().getToken();
							mUserIdString = data.getData().getUser_id();
							if (!MyUtils.isNull(mTokenString)
									&& !MyUtils.isNull(mUserIdString)) {
								
								memoBeans = DBOperator.querySymptomMemoListOrderByDate(db);
								if(memoBeans != null && memoBeans.size() != 0){
									boolean isSuccese = ImageDBOperator.copyImageRecord(imagedb, memoBeans);
									DBOperator.clearSymptomMemoUrlPathDate(db);
								}
								
								
								if (!MyUtils.isNull(mTokenString)) {// whether user is Online

									File dbFile = new File(SDConfig.DB_PATH);
								
									if (MyUtils.isNetWork(getApplicationContext()) && dbFile.exists()) {// whether net of device is open and database file is exist
										mProgressDialog.showDialog();
										uploadDB();
									}else{
										Intent intent = new Intent(
												LoginThreeStepActivity.this,
												LoginFailActivity.class);
									
										startActivity(intent);
										overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
									}
								}
								
								
								
							} else {
								Intent intent = new Intent(
										LoginThreeStepActivity.this,
										LoginFailActivity.class);
							
								startActivity(intent);
								overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
							}
						} else {
							Utils.showErrorMessage(getApplicationContext(), data.getError());
							Intent intent = new Intent(
									LoginThreeStepActivity.this,
									LoginFailActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
						}
						}else{
							Utils.showErrorMessage(getApplicationContext(), data.getError());
							Intent intent = new Intent(
									LoginThreeStepActivity.this,
									LoginFailActivity.class);
							startActivity(intent);
							overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
						}
					}
				});
	}
	
	
	
	public void uploadDB() {

		boolean isNeedUpImage = false;
		isNeedUpImage = ImageDBOperator.isNeedUpload(imagedb);
		if (isNeedUpImage) {
			uploadFlag = 0;

			list_image_upload = ImageDBOperator.queryUploadImage(imagedb);
			
			String type = "2";
			for (uploadFlag = 0; uploadFlag < list_image_upload.size(); uploadFlag++) {
				File imageFile = new File(list_image_upload.get(uploadFlag)
						.getLocalurl());
				if (imageFile.exists()) {
					if (MyUtils.isNetWork(getApplicationContext())) {
						upImageFile(mTokenString, type, imageFile);
						break;
					}
				}
			}
			if (uploadFlag >= list_image_upload.size()) {
				String type1 = "1";
				File dbFile = new File(SDConfig.DB_PATH);
				if (MyUtils.isNetWork(getApplicationContext())) {
					if (dbFile.exists()) {
						upDbFile(mTokenString, type1, dbFile);
					} else {
						mProgressDialog.removeDialog();
					}
				}
			}

		} else {
			String type = "1";
			File dbFile = new File(SDConfig.DB_PATH);
			if (MyUtils.isNetWork(getApplicationContext())) {
			
				if (dbFile.exists()) {
					upDbFile(mTokenString, type, dbFile);
				} else {
					mProgressDialog.removeDialog();
					Intent intent = new Intent(
							LoginThreeStepActivity.this,
							LoginFailActivity.class);
				
					startActivity(intent);
					overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				}
			}
		}

	}

	// upload file of image
	private void upImageFile(String token, String type, File file) {

		System.out.println(file.getAbsolutePath());
		HttpRequest.getInstance().upLoadImage(token, type, file,
				new CommonCallBack<FileUpRequestBean>() {

					@Override
					public void onSuccess(FileUpRequestBean data) {
						if (data != null && data.getError().equals(200 + "")) {
							SymptomMemoBean mySymptomMemoBean = DBOperator
									.querySymptomMemoById(db, list_image_upload
											.get(uploadFlag).getSymptomid()
											+ "");
							mySymptomMemoBean.setUrl(data.getFile_url());
							boolean isSuccess = DBOperator.updateSymptomMemo(
									db, mySymptomMemoBean);
							list_image_upload.get(uploadFlag).setIsupload(1);
							ImageDBOperator
									.updateImageRecordBySymptomImageBean(
											imagedb,
											list_image_upload.get(uploadFlag));
							uploadFlag++;
							if (uploadFlag < list_image_upload.size()) {
										
								String type = "2";
								File imageFile = new File(list_image_upload
										.get(uploadFlag).getLocalurl());
								if (imageFile.exists()) {
									if (MyUtils
											.isNetWork(getApplicationContext())) {
										upImageFile(mTokenString, type, imageFile);
									}
								}

							} else {

								
								String type = "1";
								File dbFile = new File(SDConfig.DB_PATH);
								if (MyUtils.isNetWork(getApplicationContext())
										&& dbFile.exists() && dbFile != null) {
									upDbFile(mTokenString, type, dbFile);
								} else {

								}
							}

						} else {
							
							imageIsUpFail = true;

							if (data != null
									&& data.getError().equals(425 + "")) {
								mProgressDialog.removeDialog();
								App.getInstance().getmLoginUtils().logout();
							} else {
								uploadFlag++;
								if (uploadFlag < list_image_upload.size()) {
									
									String type = "2";
									File imageFile = new File(list_image_upload
											.get(uploadFlag).getLocalurl());
									if (imageFile.exists()) {
										if (MyUtils
												.isNetWork(getApplicationContext())) {
											upImageFile(mTokenString, type, imageFile);
										}
									}

								} else {

									
									String type = "1";
									File dbFile = new File(SDConfig.DB_PATH);
									if (MyUtils
											.isNetWork(getApplicationContext())
											&& dbFile.exists()
											&& dbFile != null) {
										upDbFile(mTokenString, type, dbFile);
									}
								}
							}
						}

					}

					@Override
					public void onStart() {

					}

					@Override
					public void onFailed(Throwable throwable, String reason) {
						imageIsUpFail = true;
						uploadFlag++;
						if (uploadFlag < list_image_upload.size()) {
							
							String type = "2";
							File imageFile = new File(list_image_upload.get(
									uploadFlag).getLocalurl());
							if (imageFile.exists()) {
								if (MyUtils.isNetWork(getApplicationContext())) {
									upImageFile(mTokenString, type, imageFile);
								}
							}

						} else {

							String type = "1";
							File dbFile = new File(SDConfig.DB_PATH);
							if (MyUtils.isNetWork(getApplicationContext())
									&& dbFile.exists() && dbFile != null) {
								upDbFile(mTokenString, type, dbFile);
							}
						}
					}

				});
	}

	// upload dbfile
	private void upDbFile(String token, String type, File file) {
		HttpRequest.getInstance().UpDb(token, type, file,
				new CommonCallBack<FileUpRequestBean>() {

					@Override
					public void onSuccess(FileUpRequestBean data) {
						mProgressDialog.removeDialog();
						
						if (data != null && data.getError().equals("200")) {
							Long date = System.currentTimeMillis();
							Long day = DateFormatUtil.geDataLongZeroDay(date);
							String dayString = getApplication().getResources()
									.getString(R.string.update_date_pre)
									+ DateFormatUtil.getStringDataByLong(date);
							App.getInstance().setUpdateTime(dayString);
							
							App.getInstance().setIsRegister(true);
							App.getInstance().getmLoginUtils()
							.save(mUserIdString, mTokenString);
							
							Intent intent = new Intent(
									LoginThreeStepActivity.this,
									LoginSuccessActivity.class);
						
							startActivity(intent);
							overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
						} else if (data != null
								&& data.getError().equals("425")) {
							
							App.getInstance().getmLoginUtils().logout();
							Intent intent = new Intent(
									LoginThreeStepActivity.this,
									LoginFailActivity.class);
						
							startActivity(intent);
							overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
						} else {
							Intent intent = new Intent(
									LoginThreeStepActivity.this,
									LoginFailActivity.class);
						
							startActivity(intent);
							overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
							
						}
						

					}

					@Override
					public void onStart() {

					}

					@Override
					public void onFailed(Throwable throwable, String reason) {
						mProgressDialog.removeDialog();
						Intent intent = new Intent(
								LoginThreeStepActivity.this,
								LoginFailActivity.class);
					
						startActivity(intent);
						overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
					}
				});
	}
}
