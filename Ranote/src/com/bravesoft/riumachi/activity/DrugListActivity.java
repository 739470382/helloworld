package com.bravesoft.riumachi.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.adapter.DrugListAdapter;
import com.bravesoft.riumachi.bean.DrugBean;
import com.bravesoft.riumachi.bean.DrugGroup;
import com.bravesoft.riumachi.callback.InformCallBack;
import com.bravesoft.riumachi.constant.MedicineType;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.database.DBHelper;
import com.bravesoft.riumachi.database.DBOperator;
import com.bravesoft.riumachi.layout.LayoutUtils;
import com.bravesoft.riumachi.service.NotifyService;
import com.bravesoft.riumachi.util.MyUtils;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ExpandableListView.OnGroupClickListener;

public class DrugListActivity extends BaseActivity implements OnClickListener{
	
	public static String MEDICINE_NAME = "medicine_name";
	public static String MEDICINE_TYPE = "medicine_type";
	private String mMedicineName;
	private String mMedicineType;
	private ImageView imageView_back;
	private TextView textView_back,textView_next,textView_add;
	private ExpandableListView listView;
	private ArrayList<DrugGroup> group;
	private ArrayList<ArrayList<DrugBean>> child;
	private DrugListAdapter listAdapter;
	private View view_line;
	private RelativeLayout layout_add;
	private Intent intent;
	private static int isChange=1;
	private View view_back,view_add;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			if (getIntent() != null && getIntent().getExtras() != null) {
				mMedicineName = getIntent().getExtras().getString(MEDICINE_NAME);
				mMedicineType = getIntent().getExtras().getString(MEDICINE_TYPE);
			}
			setContentView(R.layout.activity_drug_list);
			initView();
		}
		
		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			if (resultCode==1) {
				
				if (data!=null||!data.equals("")) {
						String drug_name=data.getExtras().getString("value");
						DrugBean bean=new DrugBean();
						bean.setDrug_name(drug_name);
						bean.setIsCheck(1);
						child.get(2).add(bean);
						listAdapter.notifyDataSetChanged();
				}else if(data==null) {
					
				}
			}
		}

		private void initView() {
			getRateView(R.id.relative_title_other, true);
			view_back=getRateView(R.id.view_back, true);
			view_add=getRateView(R.id.view_add, true);
			imageView_back=getRateView(R.id.img_back, true);
			textView_back=getTextView(R.id.txt_title_name, true, 37 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
			textView_next=getTextView(R.id.txt_operate, true, 33 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
			
			imageView_back.setImageResource(R.drawable.icon_arrow);
			textView_back.setText("お薬登録");
			textView_next.setText("編集");
			//init expandlistview
			initList();
			view_add.setOnClickListener(this);
			view_back.setOnClickListener(this);
		}

		private void initList() {
			listView=(ExpandableListView) findViewById(R.id.drug_listview);
			view_line=View.inflate(DrugListActivity.this, R.layout.item_druglist_footer,null);
			getRateView(R.id.view_druglist_line, true);
			getRateView(R.id.view_druglist_space, true);
			listView.addFooterView(view_line);
			layout_add=(RelativeLayout) view_line.findViewById(R.id.drualist_add);
			textView_add=(TextView) view_line.findViewById(R.id.textview_druglist_add);
			LayoutUtils.setTextSize(textView_add, 34 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
			getRateView(R.id.view_druglist3_line, true);
			layout_add.setOnClickListener(this);
			
			listView.setGroupIndicator(null);
			 
			listView.setOnGroupClickListener(new OnGroupClickListener() {

				@Override
				public boolean onGroupClick(ExpandableListView parent, View v,
						int groupPosition, long id) {
					return true;
				}
			});
			 
			group=new ArrayList<DrugGroup>();
			String[] group_names={"MTX","生物学的製剤","その他お薬"};
			int[] group_pics={R.drawable.icon_medicine_a,R.drawable.icon_medicine_b,R.drawable.icon_medicine_c};
			for (int i = 0; i < 3; i++) {
				DrugGroup drugGroup=new DrugGroup();
				drugGroup.setName(group_names[i]);
				drugGroup.setPic_drug(group_pics[i]);
				group.add(drugGroup);
			}
			//  child content
			child=new ArrayList<ArrayList<DrugBean>>();
			ArrayList<DrugBean> child1=new ArrayList<DrugBean>();
			ArrayList<DrugBean> child2=new ArrayList<DrugBean>();
			ArrayList<DrugBean> child3=new ArrayList<DrugBean>();
			 
			//String[] child1_names={"シオゾール","アザルフィジン","リマチル","メトトレキサート","リウマトレックス","ブログラフ","ブレディニン"};
			String[] child1_names={"メトトレキサート","メトレート","リウマトレックス"};
			for (int i = 0; i < child1_names.length; i++) {
				DrugBean bean=new DrugBean();
				bean.setDrug_name(child1_names[i]);
				bean.setIsCheck(1);
				if (i==child1_names.length-1) {
					bean.setIsShow(2);
				}else {
					bean.setIsShow(1);
				}
				child1.add(bean);
			}
			 
			//String[] child2_names={"アクテムラ","レミケード","エンブレル","ヒュミラ","シンポニー","シムジア","オレンシア"};
			String[] child2_names={"アクテムラ","エンブレル","オレンシア","シムジア","シンポニー","ヒュミラ","レミケード"};
			for (int i = 0; i < child2_names.length; i++) {
				DrugBean bean=new DrugBean();
				bean.setDrug_name(child2_names[i]);
				bean.setIsCheck(1);
				if (i==child2_names.length-1) {
					bean.setIsShow(2);
				}else {
					bean.setIsShow(1);
				}
				child2.add(bean);
			}
			 
//			for (int i = 0; i < 2; i++) {
//				DrugBean bean=new DrugBean();
//				bean.setDrug_name("dummy"+i);
//				bean.setIsCheck(1);
//				child3.add(bean);
//			}
			DBHelper dbHelper=new DBHelper(DrugListActivity.this);
			SQLiteDatabase db=dbHelper.getReadableDatabase();
			child3=DBOperator.selectDrug(db);
			db.close();
			child.add(0,child1);
			child.add(1,child2);
			child.add(2,child3);
			listAdapter=new DrugListAdapter(DrugListActivity.this, group, child);
			listView.setAdapter(listAdapter);
			// all  open
			for (int i = 0; i < listAdapter.getGroupCount(); i++) {

				listView.expandGroup(i);

			}
			listView.setOnChildClickListener(new OnChildClickListener() {
				
				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {
					ImageView imageView=(ImageView) v.findViewById(R.id.imageview_drug_ischeck);
					imageView.setImageResource(R.drawable.check_on);
					Log.v("POSITION", childPosition+"");
					switch (groupPosition) {
					case 0:
						mMedicineType=MedicineType.ANTIBIOTIC;
						break;
					case 1:
						mMedicineType=MedicineType.BIOLOGCAL_AGENT;
						break;
					case 2:
						mMedicineType=MedicineType.OTHER_MEDICINE;
						break;
					default:
						break;
					}
					intent=new Intent();
					intent.putExtra(MEDICINE_NAME, child.get(groupPosition).get(childPosition).getDrug_name());
					intent.putExtra(MEDICINE_TYPE, mMedicineType);
					DrugListActivity.this.setResult(1, intent);
					finishActivity();
					return false;
				}
			});
		}
		
		

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.drualist_add:
				Intent intent=new Intent(DrugListActivity.this,DrugAddActivity.class);
				startActivityForResult(intent, 1);
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				break;
			case R.id.view_back:
				intent=new Intent();
				if (!MyUtils.isNull(mMedicineName)) {
					intent.putExtra(MEDICINE_NAME,mMedicineName);
				}else{
					intent.putExtra(MEDICINE_NAME,"");
				}
				if (!MyUtils.isNull(mMedicineType)) {
					intent.putExtra(MEDICINE_TYPE,mMedicineType);
				}else{
					intent.putExtra(MEDICINE_TYPE,"");
				}
				DrugListActivity.this.setResult(1, intent);
				finishActivity();
				overridePendingTransition(R.anim.activityout, R.anim.activityin);
				break;
			case R.id.view_add:
				
				switch (isChange) {
				case 1:
					if (child.get(2) != null && child.get(2).size() > 0) {
						textView_next.setText("保存");
						changData(isChange);
						isChange=3;
					}
					break;
				case 3:
					textView_next.setText("編集");
					changData(isChange);
					isChange=1;
					checkOperateEnable();
					break;
				default:
					break;
				}
				break;
				
			default:
				break;
			}
		}

		private void changData(final int isChange) {
			if (isChange==1) {
				for (int i = 0; i < child.get(0).size(); i++) {
					child.get(0).get(i).setIsFoucs(1);
				}
				for (int i = 0; i < child.get(1).size(); i++) {
					child.get(1).get(i).setIsFoucs(1);
				}
				for (int i = 0; i < child.get(2).size(); i++) {
					child.get(2).get(i).setIsFoucs(2);
					child.get(2).get(i).setIsDelete(2);
				}
				layout_add.setEnabled(false);
				textView_add.setTextColor(0xff9b9b9b);
			}else if (isChange==3) {
				for (int i = 0; i < child.get(0).size(); i++) {
					child.get(0).get(i).setIsFoucs(3);
				}
				for (int i = 0; i < child.get(1).size(); i++) {
					child.get(1).get(i).setIsFoucs(3);
				}
				for (int i = 0; i < child.get(2).size(); i++) {
					child.get(2).get(i).setIsFoucs(3);
				}
				layout_add.setEnabled(true);
				textView_add.setTextColor(0xff000000);
			}
			listAdapter=new DrugListAdapter(DrugListActivity.this, group, child);
			listAdapter.notifyDataSetChanged();
			listView.setAdapter(listAdapter);
			
			for (int i = 0; i < listAdapter.getGroupCount(); i++) {

				listView.expandGroup(i);

			}
			listView.setOnChildClickListener(new OnChildClickListener() {
				
				@Override
				public boolean onChildClick(ExpandableListView parent, View v,
						final int groupPosition, final int childPosition, long id) {
					if (isChange==1) {
						
						if (groupPosition==0||groupPosition==1) {
							
						}else {
							
							ImageView imageView=(ImageView) v.findViewById(R.id.imageview_drug_ischeck);
							imageView.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									DBHelper dbHelper=new DBHelper(DrugListActivity.this);
									SQLiteDatabase db=dbHelper.getReadableDatabase();
									DBOperator.deleteDrug(db, child.get(groupPosition).get(childPosition).getDrug_name());
									db.close();
									child.get(groupPosition).remove(childPosition);
									listAdapter.notifyDataSetChanged();
								}
							});
						}
					}else if(isChange==3){
						ImageView imageView=(ImageView) v.findViewById(R.id.imageview_drug_ischeck);
						imageView.setImageResource(R.drawable.check_on);
						String medicien_type = null;
						switch (groupPosition) {
						case 0:
							medicien_type=MedicineType.ANTIBIOTIC;
							break;
						case 1:
							medicien_type=MedicineType.BIOLOGCAL_AGENT;
							break;
						case 2:
							medicien_type=MedicineType.OTHER_MEDICINE;
							break;
						default:
							break;
						}
						intent=new Intent();
						intent.putExtra(MEDICINE_NAME, child.get(groupPosition).get(childPosition).getDrug_name());
						intent.putExtra(MEDICINE_TYPE, medicien_type);
						DrugListActivity.this.setResult(1, intent);
						finishActivity();
					}
					return false;
				}
			});
		}
		@Override
		protected void onResume() {
			checkOperateEnable();
			super.onResume();
		}

		private void checkOperateEnable() {
			DBHelper dbHelper=new DBHelper(DrugListActivity.this);
			SQLiteDatabase db=dbHelper.getReadableDatabase();
			List<DrugBean> data = DBOperator.selectDrug(db);
			db.close();
			if (data != null && data.size() > 0) {
				textView_next.setTextColor(getResources().getColor(R.color.white));
			}else{
				textView_next.setTextColor(getResources().getColor(R.color.button_not_click));
			}
		}
		
		
}
