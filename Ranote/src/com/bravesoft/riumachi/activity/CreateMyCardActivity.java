package com.bravesoft.riumachi.activity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.appliction.App;
import com.bravesoft.riumachi.bean.MyKarteBean;
import com.bravesoft.riumachi.constant.DialogType;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.database.DBHelper;
import com.bravesoft.riumachi.database.DBOperator;
import com.bravesoft.riumachi.dialog.GeneralStringDialog;
import com.bravesoft.riumachi.dialog.GeneralStringDialog.OnGenenalStringDialogClickListener;
import com.bravesoft.riumachi.util.CommonUtils;
import com.bravesoft.riumachi.util.InputTools;
import com.bravesoft.riumachi.util.MyUtils;
import com.bravesoft.riumachi.widget.KeyboardListenRelativeLayout;
import com.bravesoft.riumachi.widget.KeyboardListenRelativeLayout.IOnKeyboardStateChangedListener;

public class CreateMyCardActivity extends BaseActivity implements
		OnClickListener{

	public static final String OPEN_TYPE = "open_type"; 
	public static final String OPEN_TYPE_CREATE = "open_type_create";
	public static final String OPEN_TYPE_EDIT = "open_type_edit";
	public static final String SCHEDULE_TYPE = "schedule_type";
	public static final String MY_KARTE_ID = "my_karte_id";
	private String mOpenType;
	private TextView mTxtTitle;
	private TextView mTxtSave;
	private TextView mTxtDelete;
	private EditText mEditConetnt;
	private LinearLayout mLinearDelete;
	private GeneralStringDialog mDeleteNoLoopDialog;
	private SQLiteDatabase db;
	private DBHelper mDbHelper;
	private String id ;
	private View view_back,view_add;
	private static boolean isaddable;
	private KeyboardListenRelativeLayout rootView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_create_my_card);
		mDbHelper = new DBHelper(getApplicationContext());
		db = mDbHelper.getReadableDatabase();
		if (savedInstanceState != null) {
			mOpenType = savedInstanceState.getString(OPEN_TYPE);
			id = savedInstanceState.getString(MY_KARTE_ID);
		} else if (getIntent() != null) {
			Intent intent = getIntent();
			mOpenType = intent.getStringExtra(OPEN_TYPE);
			id = intent.getStringExtra(MY_KARTE_ID);
		}
		initView();
		super.onCreate(savedInstanceState);
	}

	public static void startCreateMyCardActivity(Context context, String openType) {
		Intent intent = new Intent(context, CreateMyCardActivity.class);
		intent.putExtra(OPEN_TYPE, openType);
		context.startActivity(intent);
	}
	
	public static void startCreateMyCardActivity(Context context, String openType, String id) {
		Intent intent = new Intent(context, CreateMyCardActivity.class);
		intent.putExtra(OPEN_TYPE, openType);
		intent.putExtra(MY_KARTE_ID, id);
		context.startActivity(intent);
	}

	private void initView() {
		
		rootView = (KeyboardListenRelativeLayout) findViewById(R.id.relative_root);
		view_back=getRateView(R.id.view_back, true);
		view_add=getRateView(R.id.view_add, true);
		RelativeLayout titleRelativeLayout = getRateView(R.id.relative_title_other, true);
		mLinearDelete = getRateView(R.id.linear_delete_schedule, true);
		getRateView(R.id.relative_schedule_name, true).setOnClickListener(this);
		getRateView(R.id.horizotal_line_one, true);
		getRateView(R.id.horizotal_line_two, true);
		getRateView(R.id.horizotal_line_three, true);
		getRateView(R.id.horizotal_line_four, true);

		getRateView(R.id.img_back, true);

		mTxtTitle = getTextView(R.id.txt_title_name, true, 37,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		mTxtSave = getTextView(R.id.txt_operate, true, 33,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mTxtDelete = getTextView(R.id.txt_delete_mark, true, 32,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mEditConetnt = getTextView(R.id.edit_create_my_card, true, 32,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);

		/* Setting Listener*/
		mTxtSave.setOnClickListener(this);
		mLinearDelete.setOnClickListener(this);
		view_add.setOnClickListener(this);
		view_back.setOnClickListener(this);
		titleRelativeLayout.setOnClickListener(this);
		rootView.setOnKeyboardStateChangedListener(new IOnKeyboardStateChangedListener() {
			
			@Override
			public void onKeyboardStateChanged(int state) {
			    switch (state) {  
                case KeyboardListenRelativeLayout.KEYBOARD_STATE_HIDE://keyboard has been hide  
                    mLinearDelete.setVisibility(View.VISIBLE);  
                    break;  
                case KeyboardListenRelativeLayout.KEYBOARD_STATE_SHOW://keyboard has been shown
                	mLinearDelete.setVisibility(View.INVISIBLE);  
                    break;  
                default:  
                    break;  
                }  
			}
		});
		
		/* The passed parameter setting control values and status */
		initContent();
	}

	
	/**
	 * For incoming parameter initialization interface
	 */
	private void initContent() {
		mTxtTitle.setText(getString(R.string.schedule_mycard_create));
		if (mOpenType != null) {
			if (mOpenType.equals(OPEN_TYPE_CREATE)) {
				isaddable = false;
				mTxtSave.setText(getString(R.string.schedule_state_create));
				mTxtDelete.setTextColor(getResources().getColor(R.color.gray_line));
				mLinearDelete.setClickable(false);
			} else if (mOpenType.equals(OPEN_TYPE_EDIT)) {
				isaddable = true;
				mTxtSave.setText(getString(R.string.schedule_state_edit));
				mTxtDelete.setTextColor(getResources().getColor(R.color.pink));
				mLinearDelete.setClickable(true);
				MyKarteBean myKarteBean =  DBOperator.queryMyKarteById(db, id);
				if (myKarteBean != null && !MyUtils.isNull(myKarteBean.getId()+"")) {
					mEditConetnt.setText(myKarteBean.getContent());
					mEditConetnt.setSelection(myKarteBean.getContent().length());
				}
			}
		} else {
			mTxtSave.setText(getString(R.string.schedule_state_create));
			mTxtDelete.setTextColor(getResources().getColor(R.color.gray_line));
			mLinearDelete.setClickable(true);
		}
		
		
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(OPEN_TYPE, mOpenType);
		outState.putString(MY_KARTE_ID, id);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void finish() {
		InputTools.HideKeyboard(mEditConetnt);
		super.finish();
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.view_back:
			CommonUtils.exitKeyboard(this);
			finishActivity();
			overridePendingTransition(0,R.anim.activity_bottom_out);
			break;
		case R.id.view_add:
			CommonUtils.exitKeyboard(this);
			String content = mEditConetnt.getText().toString();
			if (MyUtils.isNull(content) || content.trim().equals("")) { 
				CommonUtils.showToastMessage(this, getString(R.string.schedule_mycard_operate_failed_message));
				return;
			}
			boolean isSuccess = false;
			MyKarteBean myKarteBean = new MyKarteBean();
			myKarteBean.setContent(mEditConetnt.getText().toString());
			myKarteBean.setDate(System.currentTimeMillis()+"");
			if (mOpenType.equals(OPEN_TYPE_CREATE)) {
				isSuccess = DBOperator.insertMyKarte(db, myKarteBean);
			}else if (mOpenType.equals(OPEN_TYPE_EDIT)) {
				myKarteBean.setId(Integer.parseInt(id));
				isSuccess = DBOperator.updateMyKarte(db, myKarteBean);
			}
			if (isSuccess) {
				App.getInstance().SetDialogAddEventDismiss(true);
				App.getInstance().setShowMyKarteFragment(true);
				finishActivity();
			}else{
				CommonUtils.showToastMessage(this, getString(R.string.schedule_mycard_operate_failed_message));
			}
			
			break;
		case R.id.img_allow_all:
			CommonUtils.exitKeyboard(this);
			break;
		case R.id.relative_schedule_name:
			CommonUtils.exitKeyboard(this);
			InputTools.ShowKeyboard(mEditConetnt);
			break;
		case R.id.linear_delete_schedule:
			CommonUtils.exitKeyboard(this);
		case R.id.txt_delete_mark:
			CommonUtils.exitKeyboard(this);
			if (mDeleteNoLoopDialog == null) {
				mDeleteNoLoopDialog = new GeneralStringDialog(this,
						getString(R.string.dialog_title_delete_my_karte),DialogType.DIALOG_SURE,
						new OnGenenalStringDialogClickListener() {

					@Override
					public void OnGeneralSureTextClicked() {
						DBOperator.deleteMyKarteById(db, id);
						mDeleteNoLoopDialog.dismiss();
						finishActivity();
					}

					@Override
					public void OnGeneralCancelTextClicked() {
						mDeleteNoLoopDialog.dismiss();
					}
					

				});
			}
			mDeleteNoLoopDialog.show();
			break;
		case R.id.relative_title_other:
			CommonUtils.exitKeyboard(this);
			break;
		default:
			break;
		}
	}

	
	@Override
	protected void onDestroy() {
		if (db != null && db.isOpen()) {
			db.close();
		}
		super.onDestroy();
	}


}
