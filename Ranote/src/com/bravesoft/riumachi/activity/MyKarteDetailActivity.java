package com.bravesoft.riumachi.activity;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.adapter.EditTextAdapter;
import com.bravesoft.riumachi.bean.MyKarteBean;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.database.DBHelper;
import com.bravesoft.riumachi.database.DBOperator;
import com.bravesoft.riumachi.util.MyUtils;
import com.bravesoft.riumachi.util.StringCalculateUtils;

public class MyKarteDetailActivity extends BaseActivity implements
		OnClickListener {

	public static final String MYKARTE_ID = "mykarte_id";
	private TextView mTxtTitle;
	private TextView mTxtEditMark;
	private TextView mTxtScheduleTitle;
	private ImageView mImgScheduleMark;
	private String mMyKarteId;
	private MyKarteBean myKarteBean;
	private SQLiteDatabase db;
	private DBHelper mDbHelper;
	private ListView mListView;
	private EditTextAdapter mAdapter;
	private View view_back,view_add;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mDbHelper = new DBHelper(getApplicationContext());
		db = mDbHelper.getReadableDatabase();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_card_detial);
		if (savedInstanceState != null) {
			mMyKarteId = savedInstanceState.getString(MYKARTE_ID);
		}else if (getIntent() != null) {
			mMyKarteId = getIntent().getStringExtra(MYKARTE_ID);
		}
		initView();

	}

	public static void startMyCardDetailActivity(Context context, String id) {
		Intent intent = new Intent(context, MyKarteDetailActivity.class);
		intent.putExtra(MYKARTE_ID, id);
		context.startActivity(intent);
	}

	private void initView() {
		getRateView(R.id.relative_title_other, true);
		getRateView(R.id.img_back, true);
		view_back=getRateView(R.id.view_back, true);
		view_add=getRateView(R.id.view_add, true);
		getRateView(R.id.horizontal_line_one, true);
		mImgScheduleMark = getRateView(R.id.img_schedule_type_mark, true);

		mTxtTitle = getTextView(R.id.txt_title_name, true, 37 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		mTxtEditMark = getTextView(R.id.txt_operate, true, 33 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		mTxtScheduleTitle = getTextView(R.id.txt_schedule_title, true, 35 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		
		view_back.setOnClickListener(this);
		view_add.setOnClickListener(this);
		 
		myKarteBean = DBOperator.queryMyKarteById(db, mMyKarteId);
		initContent();

		mListView = getRateView(R.id.list_mykarte, true);
		List<String> data = StringCalculateUtils.getSplitStringArray(this,myKarteBean.getContent(),28 ,80 ,40);
		mAdapter = new EditTextAdapter(this, data);
		mListView.setAdapter(mAdapter);
		
	}
	
	private void initContent() {
		mTxtTitle.setText(getString(R.string.schedule_edit_mycard));
		mTxtEditMark.setText(R.string.schedule_edit_title);
		//myKarteBean = DBOperator.queryMyKarteById(db, mMyKarteId);
		if (myKarteBean == null) {
			return;
		}
		String content = myKarteBean.getContent();
		if (!MyUtils.isNull(content)) {
			if (content.contains("\r")||content.contains("\n")) {
				String arr[] = null;
				if (content.contains("\r")) {
					arr = content.split("\r");
				}else if (content.contains("\n")) {
					arr = content.split("\n");
				}
				if (arr != null && arr.length > 0) {
					mTxtScheduleTitle.setText(arr[0]);
				}
			}else{
				mTxtScheduleTitle.setText(content);
			}
		}
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.view_back:
			finishActivity();
			overridePendingTransition(R.anim.activityout, R.anim.activityin);
			break;
		case R.id.view_add:
			CreateMyCardActivity.startCreateMyCardActivity(this, CreateMyCardActivity.OPEN_TYPE_EDIT, mMyKarteId);
			finishActivity();
			break;
		default:
			break;
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(MYKARTE_ID, mMyKarteId);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onDestroy() {
		if (db != null && db.isOpen()) {
			db.close();
		}
		super.onDestroy();
	}
}
