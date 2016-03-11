package com.bravesoft.riumachi.activity;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Observable;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.appliction.App;
import com.bravesoft.riumachi.bean.FileDownRequestBean;
import com.bravesoft.riumachi.bean.FileUpRequestBean;
import com.bravesoft.riumachi.bean.SymptomMemoBean;
import com.bravesoft.riumachi.bean.UserRegist;
import com.bravesoft.riumachi.callback.CommonCallBack;
import com.bravesoft.riumachi.constant.SDConfig;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.database.DBConfig;
import com.bravesoft.riumachi.database.DBHelper;
import com.bravesoft.riumachi.database.DBOperator;
import com.bravesoft.riumachi.dialog.GeneralStringDialog;
import com.bravesoft.riumachi.dialog.GeneralStringDialog.OnGenenalStringDialogClickListener;
import com.bravesoft.riumachi.http.HttpRequest;
import com.bravesoft.riumachi.service.NotifyService;
import com.bravesoft.riumachi.util.AppManager;
import com.bravesoft.riumachi.util.CommonUtils;
import com.bravesoft.riumachi.util.DateFormatUtil;
import com.bravesoft.riumachi.util.MyUtils;
import com.bravesoft.riumachi.util.Utils;

public class RestoreOneStepActivity extends BaseActivity implements
		OnClickListener {

	

	private TextView setting_title;// title
	private TextView setting_text;// Next button text
	private EditText emailEditText;// email input box
	private EditText passwordEditText;// Password input box
	private TextView setting_login2_buttom;
	private TextView setting_login2_buttom_text;
	private ImageView titleImage;// title displayed pictures
	private GeneralStringDialog generalStringDialog;// Determine data recovery
					              					// dialog
	private View title_button_left;// Back button
	private View title_button_right;// Next button
	private boolean emailnotnull = false;
	private boolean passwordnotnull = false;
	private boolean issendable = false;
	private String mTokenString;
	private String mUserIdString;
	private Context context = this;
	private String emailText;
	private String passwordText;
	private List<SymptomMemoBean> symptomMemoBeans;
	private SQLiteDatabase db;// database of all data
	private DBHelper mDbHelper;
	private int idTag = 0;
	
	private boolean allimageDown = true;
	

	/**
	 * @param args
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_restore_step_one);
		super.onCreate(savedInstanceState);
		mDbHelper = new DBHelper(getApplicationContext());
		db = mDbHelper.getReadableDatabase();
		App.isfirstcome = false;
		initView(savedInstanceState);
	}

	private void initView(Bundle savedInstanceState) {

		getRateView(R.id.relative_setting_main, true);
		getRateView(R.id.user_restore_page1_1, true);
		getRateView(R.id.user_restore_page1_2, true);

		titleImage = getRateView(R.id.setting_image, true);
		titleImage.setImageResource(R.drawable.icon_arrow);
		titleImage.setOnClickListener(this);
		title_button_left = getRateView(R.id.title_button_left, true);
		title_button_right = getRateView(R.id.title_button_right, true);
		title_button_left.setOnClickListener(this);
		title_button_right.setOnClickListener(this);

		setting_text = getTextView(R.id.setting_text, true, 33 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		setting_text.setText(R.string.send);
		setting_text.setTextColor(this.getResources().getColor(
				R.color.button_not_click));
		setting_text.setOnClickListener(this);
		setting_title = getTextView(R.id.setting_title, true, 37 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		setting_title.setText(R.string.set_reload);
		getTextView(R.id.user_restore_page1_text, true, 29 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		getTextView(R.id.user_restore_page1_text_mail, true, 24 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		emailEditText = getTextView(R.id.user_restore_page1_txtMail, true, 30 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		getRateView(R.id.restore_set_line1, true);
		getRateView(R.id.restore_set_line2, true);

		getTextView(R.id.user_restore_page1_text_psw, true, 24 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		passwordEditText = getTextView(R.id.user_restore_page1_txt_psw, true,
				30 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
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
					if (passwordnotnull) {
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
					if (emailnotnull) {
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

	}
	
	
	
	@Override
	protected void onResume() {
		super.onResume();
	
	}

	
	

	@Override
	protected void onDestroy() {
		super.onDestroy();
		db.close();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.title_button_right) {
			if (issendable) {
				CommonUtils.exitKeyboard(this);
				emailText = emailEditText.getText().toString();
				passwordText = passwordEditText.getText().toString();
				if (passwordText.length() >= 6 && passwordText.length() <= 30
						&& Utils.isEmail(emailText)) {
					// Intent intent = new Intent(RestoreOneStepActivity.this,
					// RestoreSuccessActivity.class);
					// startActivity(intent);
					
					if(MyUtils.isNetWork(getApplicationContext())){
						mProgressDialog.showDialog();
					onLogin(emailText, passwordText);
					}

				} else {
					if (emailText == null || emailText.length() == 0
							|| passwordText == null
							|| passwordText.length() == 0) {
						if (emailText == null || emailText.length() == 0) {
							Utils.showErrorMessage(getApplicationContext(), "414");
						} else {
							Utils.showErrorMessage(getApplicationContext(), "412");
						}
					} else {
						if (!Utils.isEmail(emailText)) {
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

	private void onLogin(String emailString, String passwordString) {
		HttpRequest.getInstance().Login(emailString, passwordString,
				new CommonCallBack<UserRegist>() {

					@Override
					public void onStart() {

					}

					@Override
					public void onFailed(Throwable throwable, String reason) {
						mProgressDialog.removeDialog();
						if(reason != null){
						mProgressDialog.removeDialog();
						Utils.showErrorMessage(getApplicationContext(), reason);
					}
					}

					@Override
					public void onSuccess(UserRegist data) {
						mProgressDialog.removeDialog();
						if(data != null){
						if (data.getData() != null) {
							title_button_right.setEnabled(true);
							mTokenString = data.getData().getToken();
							mUserIdString = data.getData().getUser_id();
							if (!MyUtils.isNull(mTokenString)
									&& !MyUtils.isNull(mUserIdString)) {
								App.getInstance().getmLoginUtils()
										.save(mUserIdString, mTokenString);
								App.getInstance().setIsRegister(true);
								generalStringDialog = new GeneralStringDialog(context,
										(String) context.getResources().getString(
												R.string.restore_dialog_title), 3,
										new OnGenenalStringDialogClickListener() {

											@Override
											public void OnGeneralSureTextClicked() {
												generalStringDialog.cancel();
												
												//String token = App.getInstance().getmLoginUtils().getToken();
												String type = "1";
												File dbFile = new File(SDConfig.DB_PATH);
												if(MyUtils.isNetWork(getApplicationContext())){
												mProgressDialog.showDialog();
											
											
												
												getDbFile(mTokenString, type);
												}
											}

											@Override
											public void OnGeneralCancelTextClicked() {
												generalStringDialog.cancel();
											}
								});
								generalStringDialog.show();
								
							} else {
								CommonUtils
										.showToastMessage(
												RestoreOneStepActivity.this,
												getString(R.string.schedule_operate_failed_message));
							}
						} else {
							Utils.showErrorMessage(getApplicationContext(), data.getError());
						}
						}else {
							Utils.showErrorMessage(getApplicationContext(), "0");
						}
					}
				});
	}

	private void getDbFile(String token,String type) {
		HttpRequest.getInstance().GetDb(token,type,
				new CommonCallBack<FileDownRequestBean>() {

					@Override
					public void onSuccess(FileDownRequestBean data) {
						//mProgressDialog.removeDialog();
						if(data != null && data.getError().equals("200")){
						if (data.getFile_url() != null && !data.getFile_url().equals("")) {
							String databasesString = data.getFile_url();
							File file = new File(SDConfig.DB_PATH);
							
							Utils.writeFile(SDConfig.DB_PATH,databasesString);
							
							if (file.exists()) {
								db = SQLiteDatabase.openDatabase(SDConfig.DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
								db.beginTransaction();
								db.setVersion(DBConfig.DB_VERSION);
								db.setTransactionSuccessful();
								db.endTransaction();
							}
							
							Intent intentcast = new Intent();
							intentcast.setAction("com.bravesoft.notifydata");
							RestoreOneStepActivity.this.sendBroadcast(intentcast);
							
							
							mDbHelper = new DBHelper(getApplicationContext());
							db = mDbHelper.getReadableDatabase();
							File dirfile = new File(SDConfig.MEMOPIC);
							if (!dirfile.exists()) {
								dirfile.mkdirs();// Build multi-level directory, it must be mkdirs
							}else{
								Utils.deleteAllFiles(dirfile);
								DBOperator.clearSymptomMemoLocalPathDate(db);
							}
							
							
							
							symptomMemoBeans = DBOperator.querySymptomMemoListOrderByDate(db);
							idTag = 0;
							
							if(symptomMemoBeans != null && idTag < symptomMemoBeans.size() ){
								for(idTag = idTag;idTag<symptomMemoBeans.size();idTag++){
									if(symptomMemoBeans.get(idTag).getUrl()!=null&&!symptomMemoBeans.get(idTag).getUrl().equals("null")&&!symptomMemoBeans.get(idTag).getUrl().equals("")){
								    downLoadImageFile(mTokenString, "2", symptomMemoBeans.get(idTag).getUrl()+"");
								    break;
									}
								}
								if(idTag>=symptomMemoBeans.size()){
									mProgressDialog.removeDialog();
									Intent intent = new Intent(RestoreOneStepActivity.this,
											RestoreSuccessActivity.class);
									App.getInstance().setShowCarlendarFragment(true);
									startActivity(intent);
									overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
								}
								
							}else{
							mProgressDialog.removeDialog();
							Intent intent = new Intent(RestoreOneStepActivity.this,
									RestoreSuccessActivity.class);
							App.getInstance().setShowCarlendarFragment(true);
							startActivity(intent);
							overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
							}
							} else{
								
								Intent intent = new Intent(RestoreOneStepActivity.this,
										RestoreFailActivity.class);
								intent.putExtra("error_type", "error_dbfiledown");
								App.getInstance().setShowCarlendarFragment(true);
								startActivity(intent);
							}
						}else {
							mProgressDialog.removeDialog();
							
							if (data != null&&data.getError().equals(425+"")){
								App.getInstance().getmLoginUtils().logout();
							}
							
							Intent intent = new Intent(RestoreOneStepActivity.this,
									RestoreFailActivity.class);
							intent.putExtra("error_type", "error_dbfiledown");
							App.getInstance().setShowCarlendarFragment(true);
							startActivity(intent);
						}
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onFailed(Throwable throwable, String reason) {
						mProgressDialog.removeDialog();
						Intent intent = new Intent(RestoreOneStepActivity.this,
								RestoreFailActivity.class);
						intent.putExtra("error_type", "error_dbfiledown");
						App.getInstance().setShowCarlendarFragment(true);
						startActivity(intent);
					}
				});
	}
	
	private void downLoadImageFile(String token,String type,String id) {
		HttpRequest.getInstance().DownLoadImage(token,type,id,
				new CommonCallBack<FileDownRequestBean>() {

					@Override
					public void onSuccess(FileDownRequestBean data) {
						if(data != null && data.getError().equals("200")){
						if (data.getFile_url() != null && !data.getFile_url().equals("")) {
							String imageString = data.getFile_url();
							String name = DateFormat.format("yyyyMMdd_hhmmss",
									Calendar.getInstance(Locale.CHINA))
									+ ".jpg";
							
							String localPath = Utils.saveDownLoadimage(name,imageString);
							symptomMemoBeans.get(idTag).setLocal_path(localPath);
							DBOperator.updateSymptomMemo(db, symptomMemoBeans.get(idTag));
							idTag++;
							if(idTag<symptomMemoBeans.size()){
								for(idTag = idTag;idTag<symptomMemoBeans.size();idTag++){
									if(symptomMemoBeans.get(idTag).getUrl()!=null&&!symptomMemoBeans.get(idTag).getUrl().equals("null")&&!symptomMemoBeans.get(idTag).getUrl().equals("")){
								    downLoadImageFile(mTokenString, "2", symptomMemoBeans.get(idTag).getUrl()+"");
								    break;
									}
								}
								if(idTag>=symptomMemoBeans.size()){
									mProgressDialog.removeDialog();
									Intent intent = new Intent(RestoreOneStepActivity.this,
											RestoreSuccessActivity.class);
									App.getInstance().setShowCarlendarFragment(true);
									startActivity(intent);
									overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
								}
							}else{
							mProgressDialog.removeDialog();
							Intent intent = new Intent(RestoreOneStepActivity.this,
									RestoreSuccessActivity.class);
							App.getInstance().setShowCarlendarFragment(true);
							startActivity(intent);
							overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
							}
							} else{
								idTag++;
								allimageDown = false;
								if(idTag<symptomMemoBeans.size()){
									for(idTag = idTag;idTag<symptomMemoBeans.size();idTag++){
										if(symptomMemoBeans.get(idTag).getUrl()!=null&&!symptomMemoBeans.get(idTag).getUrl().equals("null")&&!symptomMemoBeans.get(idTag).getUrl().equals("")){
									    downLoadImageFile(mTokenString, "2", symptomMemoBeans.get(idTag).getUrl()+"");
									    break;
										}
									}
								}else{
								mProgressDialog.removeDialog();
								if(allimageDown){
									Intent intent = new Intent(RestoreOneStepActivity.this,
											RestoreSuccessActivity.class);
									
									
									App.getInstance().setShowCarlendarFragment(true);
									startActivity(intent);
									overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
										}else{
											
											Intent intent = new Intent(RestoreOneStepActivity.this,
													RestoreFailActivity.class);
											intent.putExtra("error_type", "error_imagedown");
											App.getInstance().setShowCarlendarFragment(true);
											startActivity(intent);
										}
								}
							}
					}else{
						
						allimageDown = false;
						if(data != null && data.getError().equals("425")){
							mProgressDialog.removeDialog();
							App.getInstance().getmLoginUtils().logout();
							Intent intent = new Intent(RestoreOneStepActivity.this,
									RestoreFailActivity.class);
							intent.putExtra("error_type", "error_imagedown");
							App.getInstance().setShowCarlendarFragment(true);
							startActivity(intent);
							return;
						}
						idTag++;
						if(idTag<symptomMemoBeans.size()){
							for(idTag = idTag;idTag<symptomMemoBeans.size();idTag++){
								if(symptomMemoBeans.get(idTag).getUrl()!=null&&!symptomMemoBeans.get(idTag).getUrl().equals("null")&&!symptomMemoBeans.get(idTag).getUrl().equals("")){
							    downLoadImageFile(mTokenString, "2", symptomMemoBeans.get(idTag).getUrl()+"");
							    break;
								}
							}
						}else{
						mProgressDialog.removeDialog();
						if(allimageDown){
							Intent intent = new Intent(RestoreOneStepActivity.this,
									RestoreSuccessActivity.class);
							
							App.getInstance().setShowCarlendarFragment(true);
							startActivity(intent);
							overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
								}else{
									
									Intent intent = new Intent(RestoreOneStepActivity.this,
											RestoreFailActivity.class);
									intent.putExtra("error_type", "error_imagedown");
									App.getInstance().setShowCarlendarFragment(true);
									startActivity(intent);
								}
						}
					}
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onFailed(Throwable throwable, String reason) {
						//mProgressDialog.removeDialog();
						idTag++;
						allimageDown = false;
						if(idTag<symptomMemoBeans.size()){
							for(idTag = idTag;idTag<symptomMemoBeans.size();idTag++){
								if(symptomMemoBeans.get(idTag).getUrl()!=null&&!symptomMemoBeans.get(idTag).getUrl().equals("null")&&!symptomMemoBeans.get(idTag).getUrl().equals("")){
							    downLoadImageFile(mTokenString, "2", symptomMemoBeans.get(idTag).getUrl()+"");
							    break;
								}
							}
						}else{
							mProgressDialog.removeDialog();
							if(allimageDown){
						Intent intent = new Intent(RestoreOneStepActivity.this,
								RestoreSuccessActivity.class);
						
						App.getInstance().setShowCarlendarFragment(true);
						startActivity(intent);
						overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
							}else{
								
								Intent intent = new Intent(RestoreOneStepActivity.this,
										RestoreFailActivity.class);
								intent.putExtra("error_type", "error_imagedown");
								App.getInstance().setShowCarlendarFragment(true);
								startActivity(intent);
							}
						}
						
					}
				});
	}

}