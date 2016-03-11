package com.bravesoft.riumachi.activity;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.appliction.App;
import com.bravesoft.riumachi.bean.FileDownRequestBean;
import com.bravesoft.riumachi.bean.SymptomMemoBean;
import com.bravesoft.riumachi.callback.CommonCallBack;
import com.bravesoft.riumachi.constant.SDConfig;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.database.DBHelper;
import com.bravesoft.riumachi.database.DBOperator;
import com.bravesoft.riumachi.http.HttpRequest;
import com.bravesoft.riumachi.util.AppManager;
import com.bravesoft.riumachi.util.Utils;

public class RestoreFailActivity extends BaseActivity implements
		OnClickListener {

	TextView setting_title;//title
	ImageView titleImage;//title imageview
	RelativeLayout buttonToRegistOneStep;//open first step toward restoring data
	RelativeLayout buttonToMain;//open to the main interface
	String error_type;
	String mTokenString;
	private SQLiteDatabase db;// database of all data
	private DBHelper mDbHelper;
	private int idTag = 0;
	private List<SymptomMemoBean> symptomMemoBeans;
	private boolean allimageDown = true;
	
	/**
	 * @param args
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_restore_fail);
	
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		error_type = intent.getStringExtra("error_type");
		mDbHelper = new DBHelper(getApplicationContext());
		db = mDbHelper.getReadableDatabase();
		initView(savedInstanceState);
	}

	private void initView(Bundle savedInstanceState) {

		getRateView(R.id.relative_title_other, true);
		
		getRateView(R.id.user_restore_page2_1, true);
		buttonToRegistOneStep = getRateView(R.id.user_restore_page2_2, true);
		buttonToMain = getRateView(R.id.user_restore_page2_3, true);
		buttonToRegistOneStep.setOnClickListener(this);
		buttonToMain.setOnClickListener(this);

		setting_title = getTextView(R.id.txt_title_name, true, 37  ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		setting_title.setText(R.string.data_reback_fail);
		getTextView(R.id.user_restore_fail_text, true, 34 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		getTextView(R.id.user_restore_fail_button_text1, true, 30 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		getTextView(R.id.user_restore_fail_button_text2, true, 30 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);

	}
	

	@Override
	public void onBackPressed() {
		return;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.user_restore_page2_3) {

			Intent intent = new Intent(RestoreFailActivity.this, MainActivity.class);
			
			App.getInstance().setShowCarlendarFragment(true);
		
			startActivity(intent);
			AppManager.getAppManager().finishAllAndReopenMainActivity(RestoreFailActivity.this, RestoreFailActivity.class);

		} else if (v.getId() == R.id.user_restore_page2_2) {
			mTokenString = App.getInstance().getmLoginUtils().getToken();
			if(error_type.equals("error_dbfiledown")){
				mProgressDialog.showDialog();
				String type = "1";
				getDbFile(mTokenString, type);
			}else if(error_type.equals("error_imagedown")){
				mProgressDialog.showDialog();
				symptomMemoBeans = DBOperator.querySymptomMemoListImageDownFail(db);
				idTag = 0;

				if(symptomMemoBeans != null && idTag < symptomMemoBeans.size() && symptomMemoBeans.get(idTag).getUrl() != null &&symptomMemoBeans.get(idTag).getUrl().length() != 0){
					downLoadImageFile(mTokenString, "2", symptomMemoBeans.get(idTag).getUrl()+"");
				}else{
				mProgressDialog.removeDialog();
				Intent intent = new Intent(RestoreFailActivity.this,
						RestoreSuccessActivity.class);
				App.getInstance().setShowCarlendarFragment(true);
				startActivity(intent);
				}
			}

		}
	}
	@SuppressLint("NewApi") private void getDbFile(String token,String type) {
		HttpRequest.getInstance().GetDb(token,type,
				new CommonCallBack<FileDownRequestBean>() {

					@Override
					public void onSuccess(FileDownRequestBean data) {
						//mProgressDialog.removeDialog();
						if(data != null){
							if (data.getFile_url() != null && !data.getFile_url().equals("")) {
							String databasesString = data.getFile_url();
							File file = new File(SDConfig.DB_PATH);
//							
							
							Utils.writeFile(SDConfig.DB_PATH,databasesString);
							
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
									Intent intent = new Intent(RestoreFailActivity.this,
											RestoreSuccessActivity.class);
									App.getInstance().setShowCarlendarFragment(true);
									startActivity(intent);
									overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
								}
								
							}else{
							mProgressDialog.removeDialog();
							Intent intent = new Intent(RestoreFailActivity.this,
									RestoreSuccessActivity.class);
							App.getInstance().setShowCarlendarFragment(true);
							startActivity(intent);
							}
							} else{
								mProgressDialog.removeDialog();
								Utils.showErrorMessage(getApplicationContext(), data.getError());
								error_type = "error_dbfiledown";
							}
						}else {
							mProgressDialog.removeDialog();
							Utils.showErrorMessage(getApplicationContext(), "0");
							error_type = "error_dbfiledown";
						}
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onFailed(Throwable throwable, String reason) {
						mProgressDialog.removeDialog();
						Utils.showErrorMessage(getApplicationContext(), reason);
						error_type = "error_dbfiledown";
					}
				});
	}
	
	private void downLoadImageFile(String token,String type,String id) {
		HttpRequest.getInstance().DownLoadImage(token,type,id,
				new CommonCallBack<FileDownRequestBean>() {

					@Override
					public void onSuccess(FileDownRequestBean data) {
						
						if (data.getFile_url() != null) {
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
							}else{
							mProgressDialog.removeDialog();
							Intent intent = new Intent(RestoreFailActivity.this,
									RestoreSuccessActivity.class);
							App.getInstance().setShowCarlendarFragment(true);
							startActivity(intent);
							}
							} else{
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
									Intent intent = new Intent(RestoreFailActivity.this,
											RestoreSuccessActivity.class);
									
									App.getInstance().setShowCarlendarFragment(true);
									startActivity(intent);
										}else{
											
											error_type = "error_imagedown";
										}
								}
							}
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onFailed(Throwable throwable, String reason) {
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
						Intent intent = new Intent(RestoreFailActivity.this,
								RestoreSuccessActivity.class);
						
						App.getInstance().setShowCarlendarFragment(true);
						startActivity(intent);
							}else{
								
								error_type = "error_imagedown";
							}
						}
						
					}
				});
	}
}