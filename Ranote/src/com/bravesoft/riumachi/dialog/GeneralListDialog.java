package com.bravesoft.riumachi.dialog;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.adapter.GeneralDialogListAdapter;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.layout.LayoutUtils;
import com.bravesoft.riumachi.util.MyUtils;

/**
 * General containing the ListView Dialog
  * There are several styles default, select the cycle time, select the notification time, select the Delete rotation schedule
 * @author wangyuanshi
 *
 */
public class GeneralListDialog extends BaseDialog implements OnClickListener ,OnItemClickListener{

	public static final String SCHEDULE_LOOP_UPDATE_DIALOG = "schedule_roop_update_dialog";//Update rotation schedule selection
	public static final String SCHEDULE_LOOP_DELETE_DIALOG = "schedule_roop_delete_dialog";//Delete rotation schedule selection
	public static final String SCHEDULE_NOTIFICATION_TIME_PICK_DIALOG = "schedule_notification_time_pick_dialog";//Notification timing
	public static final String SCHEDULE_NOTIFICATION_ALL_DAY_TIME_PICK_DIALOG = "schedule_notification_all_day_time_pick_dialog";//Notification timing
	public static final String SCHEDULE_LOOP_INTERVAL_PICK_DIALOG = "schedule_loop_interval_pick_dialog";//Cycle time interval selection
	public static final String SCHEDULE_LOOP_INTERVAL_PICK_MEDICAL_DIALOG = "schedule_loop_interval_pick_medical_dialog";//Cycle time interval selection medical
	private Context context;
	private TextView mtxtSure;
	private TextView mtxtCancel;
	private TextView mtxtTitle;
	private View mRelativeContent;
	private ListView mListView;
	private String[] data;
	private String mTitleContent;
	private String mDialogType;
	private GeneralDialogListAdapter mGeneralDialogListAdapter;
	private OnGenenalDialogClickListener mOnGenenalDialogClickListener;
	private OnGenenalDialogItemClickListener mOnGenenalDialogItemClickListener;
	private View view_line;
	private View mViewSure,mViewCancel;

	
	public GeneralListDialog(Context context,String type) {
		super(context, R.style.NobackDialog);
		this.context = context;
		this.mDialogType = type;
		if (type.equals(SCHEDULE_LOOP_DELETE_DIALOG)) {
			this.mTitleContent = context.getString(R.string.dialog_title_delete_schedule_loop);
			this.data = context.getResources().getStringArray(R.array.schedule_loop_delete_array);
		}else if(type.equals(SCHEDULE_NOTIFICATION_TIME_PICK_DIALOG)){
			this.mTitleContent = context.getString(R.string.dialog_title_schedule_notification);
			this.data = context.getResources().getStringArray(R.array.schedule_notification_time_pick_array);
		}else if(type.equals(SCHEDULE_NOTIFICATION_ALL_DAY_TIME_PICK_DIALOG)){
			this.mTitleContent = context.getString(R.string.dialog_title_schedule_notification);
			this.data = context.getResources().getStringArray(R.array.schedule_notification_all_day_time_pick_array);
		}else if(type.equals(SCHEDULE_LOOP_INTERVAL_PICK_DIALOG)){
			this.mTitleContent = context.getString(R.string.dialog_title_schedule_interval_pick);
			this.data = context.getResources().getStringArray(R.array.schedule_loop_interval_pick_array);
		}else if(type.equals(SCHEDULE_LOOP_INTERVAL_PICK_MEDICAL_DIALOG)){
			this.mTitleContent = context.getString(R.string.dialog_title_schedule_interval_pick);
			this.data = context.getResources().getStringArray(R.array.schedule_loop_interval_pick_medical_array);
		}else if(type.equals(SCHEDULE_LOOP_UPDATE_DIALOG)){
			this.mTitleContent = context.getString(R.string.dialog_title_update_schedule_loop);
			this.data = context.getResources().getStringArray(R.array.schedule_loop_update_array);
		}
		
		init();
	}
	
	public void setOnGenenalDialogClickListener(OnGenenalDialogClickListener onGenenalDialogClickListener){
		mOnGenenalDialogClickListener = onGenenalDialogClickListener;
	}
	
	public void setOnGenenalDialogItemClickListener(OnGenenalDialogItemClickListener onGenenalDialogItemClickListener){
		mOnGenenalDialogItemClickListener = onGenenalDialogItemClickListener;
	}
	
	public void addOnGenenalDialogListValue(String value){
		System.out.println("value=="+value);
		String[] newData = new String[data.length+1];
		for (int i = 0; i < data.length-1; i++) {
			newData[i] = data[i];
		}
		newData[newData.length-2] = value;
		newData[newData.length-1] = data[data.length-1];
		data = newData;
		mGeneralDialogListAdapter.setData(data);
	}
	
	public void setOnGenenalDialogListValue(int position , String value){
		if (position > 0 && position < data.length) {
			data[position] = value;
			mGeneralDialogListAdapter.setData(data);
		}
	}
	
	public boolean isTheLastOne(int position){
		if (position > 0 && position < data.length) {
			if (position == data.length-1) {
				return true;
			}
		}
		return false;
	}
	
	public int getDatasLenght(){
		if (data != null) {
			return data.length;
		}
		return 0;
	}
	private void init() {
		setContentView(initView());
		android.view.WindowManager.LayoutParams layoutParams = getWindow()
				.getAttributes();
		layoutParams.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
		layoutParams.height = layoutParams.width;
	}

	private View initView() {
		View currentView = null;
		View footView = null;
		
		if (mDialogType.equals(SCHEDULE_LOOP_DELETE_DIALOG)) {
			this.mTitleContent = context.getString(R.string.dialog_title_delete_schedule_loop);
			this.data = context.getResources().getStringArray(R.array.schedule_loop_delete_array);
			 currentView = LinearLayout.inflate(context,
						R.layout.dialog_list_delete, null);
			 footView = LinearLayout.inflate(context,
						R.layout.view_genenal_list_dialog_delete, null);
			 view_line=footView.findViewById(R.id.view_list_dialog);
			 view_line.setVisibility(View.GONE);
		}else if(mDialogType.equals(SCHEDULE_NOTIFICATION_TIME_PICK_DIALOG)){
			this.mTitleContent = context.getString(R.string.dialog_title_schedule_notification);
			this.data = context.getResources().getStringArray(R.array.schedule_notification_time_pick_array);
			 currentView = LinearLayout.inflate(context,
						R.layout.dialog_list_general, null);
			 footView = LinearLayout.inflate(context,
						R.layout.view_genenal_list_dialog_bottom, null);
			 view_line=footView.findViewById(R.id.view_list_dialog);
		}else if(mDialogType.equals(SCHEDULE_NOTIFICATION_ALL_DAY_TIME_PICK_DIALOG)){
			this.mTitleContent = context.getString(R.string.dialog_title_schedule_notification);
			this.data = context.getResources().getStringArray(R.array.schedule_notification_all_day_time_pick_array);
			 currentView = LinearLayout.inflate(context,
						R.layout.dialog_list_turn, null);
			 footView = LinearLayout.inflate(context,
						R.layout.view_genenal_list_dialog_bottom, null);
			 view_line=footView.findViewById(R.id.view_list_dialog);
		}else if(mDialogType.equals(SCHEDULE_LOOP_INTERVAL_PICK_DIALOG)){
			this.mTitleContent = context.getString(R.string.dialog_title_schedule_interval_pick);
			this.data = context.getResources().getStringArray(R.array.schedule_loop_interval_pick_array);
			 currentView = LinearLayout.inflate(context,
						R.layout.dialog_list_turn, null);
			 footView = LinearLayout.inflate(context,
						R.layout.view_genenal_list_dialog_turn, null);
			 view_line=footView.findViewById(R.id.view_list_dialog);
		}else if(mDialogType.equals(SCHEDULE_LOOP_INTERVAL_PICK_MEDICAL_DIALOG)){
			this.mTitleContent = context.getString(R.string.dialog_title_schedule_interval_pick);
			this.data = context.getResources().getStringArray(R.array.schedule_loop_interval_pick_medical_array);
			 currentView = LinearLayout.inflate(context,
						R.layout.dialog_list_turn, null);
			 footView = LinearLayout.inflate(context,
						R.layout.view_genenal_list_dialog_turn, null);
			 view_line=footView.findViewById(R.id.view_list_dialog);
		}else if(mDialogType.equals(SCHEDULE_LOOP_UPDATE_DIALOG)){ 
			this.mTitleContent = context.getString(R.string.dialog_title_update_schedule_loop);
			this.data = context.getResources().getStringArray(R.array.schedule_loop_update_array);
			 currentView = LinearLayout.inflate(context,
						R.layout.dialog_list_delete, null);
			 footView = LinearLayout.inflate(context,
						R.layout.view_genenal_list_dialog_delete, null);
			 view_line=footView.findViewById(R.id.view_list_dialog);
			 view_line.setVisibility(View.GONE);
		}
		currentView.findViewById(R.id.view_dialog_setting_background).setOnClickListener(this);
		LayoutUtils.rateScale(context,view_line, true);
		mRelativeContent = (RelativeLayout)currentView.findViewById(R.id.relative_content);
		mListView  = (ListView)currentView.findViewById(R.id.list_general_dialog);
		LayoutUtils.rateScale(context, (RelativeLayout)currentView.findViewById(R.id.relative_title), true);
		LayoutUtils.rateScale(context, mRelativeContent , true);
		LayoutUtils.rateScale(context, mListView , true);
		LayoutUtils.rateScale(context, (RelativeLayout)footView.findViewById(R.id.relative_bottom), true);
		 
		LayoutUtils.rateScale(context, footView.findViewById(R.id.horizotal_line_one), true);
		LayoutUtils.rateScale(context, currentView.findViewById(R.id.horizontal_line_two), true);
		mtxtTitle = (TextView)currentView.findViewById(R.id.txt_dialog_title);
		mtxtCancel = (TextView)footView.findViewById(R.id.txt_cancel);
		mtxtSure = (TextView)footView.findViewById(R.id.txt_sure);
		LayoutUtils.rateScale(context, mtxtSure, true);
		LayoutUtils.rateScale(context, mtxtCancel, true);
		LayoutUtils.rateScale(context, mtxtTitle, true);
		LayoutUtils.setTextSize(mtxtTitle, 31,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.setTextSize(mtxtSure, 29,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.setTextSize(mtxtCancel, 29,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		
		mViewSure = footView.findViewById(R.id.view_sure);
		mViewCancel = footView.findViewById(R.id.view_cancel);
		LayoutUtils.rateScale(context, mViewSure , true);
		LayoutUtils.rateScale(context, mViewCancel , true);
		mViewCancel.setOnClickListener(this);
		mViewSure.setOnClickListener(this);
		
		mListView.addFooterView(footView);
		
		 
		mListView.setOnItemClickListener(this);
		mRelativeContent.setOnClickListener(this);
		mtxtCancel.setOnClickListener(this);
		mtxtSure.setOnClickListener(this);
		
		 
		if (!MyUtils.isNull(mDialogType) && (mDialogType.equals(SCHEDULE_LOOP_DELETE_DIALOG)||mDialogType.equals(SCHEDULE_LOOP_UPDATE_DIALOG))) {
			mGeneralDialogListAdapter = new GeneralDialogListAdapter(context, data ,true);
		}else if(mDialogType.equals(SCHEDULE_LOOP_INTERVAL_PICK_MEDICAL_DIALOG)){
			mGeneralDialogListAdapter = new GeneralDialogListAdapter(context, data ,false,true);
		}else{
			mGeneralDialogListAdapter = new GeneralDialogListAdapter(context, data ,false);
		}
		
		mListView.setAdapter(mGeneralDialogListAdapter);
		mtxtTitle.setText(mTitleContent);
		mGeneralDialogListAdapter.setFocus(0);
		
		return currentView;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.view_dialog_setting_background:
			dismiss();
			break;
		case R.id.view_cancel:
			if (mOnGenenalDialogClickListener != null) {
				mOnGenenalDialogClickListener.OnGeneralCancelTextClicked();
			}
			break;
		case R.id.view_sure:
			if (mOnGenenalDialogClickListener != null) {
				mOnGenenalDialogClickListener.OnGeneralSureTextClicked(mGeneralDialogListAdapter.getFocus());
			}
			break;

		default:
			break;
		}
	}

	@Override
	public void show() {
		super.show();
	}
	
	public void setFocus(int position){
		mGeneralDialogListAdapter.setFocus(position);
	}
	
	/**
	 * Callback interface, click OK, and click on the callback is canceled
	 */
	public interface OnGenenalDialogClickListener{
		public void OnGeneralSureTextClicked(int position);
		public void OnGeneralCancelTextClicked();
	}
	
	
	public interface OnGenenalDialogItemClickListener{
		public void onGeneralDialogItemClickListener(int position);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mGeneralDialogListAdapter.setFocus(position);
		if (mOnGenenalDialogItemClickListener != null) {
			mOnGenenalDialogItemClickListener.onGeneralDialogItemClickListener(position);
		}
	}

}
