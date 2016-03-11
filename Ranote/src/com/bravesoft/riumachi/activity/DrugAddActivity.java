package com.bravesoft.riumachi.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.database.DBHelper;
import com.bravesoft.riumachi.database.DBOperator;
import com.bravesoft.riumachi.util.AppManager;

public class DrugAddActivity extends BaseActivity implements OnClickListener{
	private ImageView img_back;
	private TextView textView_add,textView_titlename;
	private EditText editText;
	private Intent intent;
	private View view_back,view_add;
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_drug_add);
			initView();
		}

		private void initView() {
			img_back=getRateView(R.id.img_back, true);
			textView_titlename=getTextView(R.id.txt_title_name, true, 37,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
			textView_add=getTextView(R.id.txt_operate, true, 33,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
			textView_titlename.setText("新規お薬登録");
			textView_add.setText("追加");
			view_back=getRateView(R.id.view_back, true);
			view_add=getRateView(R.id.view_add, true);
			
			getRateView(R.id.relative_title_other, true);
			editText=getTextView(R.id.edittext_drug_add, true, 32,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
			getRateView(R.id.view_drugadd_line, true);
			
			view_add.setOnClickListener(this);
			view_back.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.view_back:
				 intent=new Intent();
				intent.putExtra("value", "");
				DrugAddActivity.this.setResult(2, intent);
				finishActivity();
				overridePendingTransition(R.anim.activityout, R.anim.activityin);
				break;
			case R.id.view_add:
				String drug_name=editText.getText().toString();
				DBHelper dbHelper=new DBHelper(DrugAddActivity.this);
				SQLiteDatabase db=dbHelper.getReadableDatabase();
				int FLAG=DBOperator.insertDrugData(db, drug_name);
				db.close();
				 intent=new Intent();
				intent.putExtra("value", drug_name);
				DrugAddActivity.this.setResult(FLAG, intent);
				finishActivity();
				break;
			default:
				break;
			}
		}

}
