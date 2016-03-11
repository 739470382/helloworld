package com.bravesoft.riumachi.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.activity.BaseActivity;
import com.bravesoft.riumachi.bean.SymptomMemoBean;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.database.DBHelper;
import com.bravesoft.riumachi.database.DBOperator;
import com.bravesoft.riumachi.layout.LayoutUtils;
import com.bravesoft.riumachi.util.DateFormatUtil;
import com.bravesoft.riumachi.util.MyUtils;

public class MemoSymptomAdapter extends BaseAdapter{

	public static  List<SymptomMemoBean> datalist = new ArrayList<SymptomMemoBean>();
	private LayoutInflater inflater;
	
	private Activity context;
	
	public MemoSymptomAdapter(Activity context, List<SymptomMemoBean> datalist){
		this.context = context;
		this.datalist = datalist;
		inflater = LayoutInflater.from(context); 
	}
	
	
	@Override
	public int getCount() {
		return datalist.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolderlist viewHolder = null;
		
		if(convertView == null){
		
		viewHolder = new ViewHolderlist();
		convertView = LayoutInflater.from(context).inflate(R.layout.item_symptom_list, parent, false);
		RelativeLayout layout1 = (RelativeLayout)convertView.findViewById(R.id.symptom_relative_layout);
		LayoutUtils.rateScale(context, layout1, true);
		 RelativeLayout layout2 = (RelativeLayout)convertView.findViewById(R.id.symptom_content);
		 LayoutUtils.rateScale(context,layout2, true);
		RelativeLayout layout3 = (RelativeLayout)convertView.findViewById(R.id.symptom_list_item_buttom);
		LayoutUtils.rateScale(context,layout3, true);
		
		viewHolder.date = (TextView)convertView.findViewById(R.id.symptom_record_date);
		viewHolder.name = (TextView)convertView.findViewById(R.id.symptom_list_item_name);
		viewHolder.pen = (ImageView)convertView.findViewById(R.id.symptom_list_item_pen);
		viewHolder.camera = (ImageView)convertView.findViewById(R.id.symptom_list_item_camera);
		viewHolder.right = (ImageView)convertView.findViewById(R.id.symptom_list_item_right);
		LayoutUtils.rateScale(context, viewHolder.pen, true);
		
		
		LayoutUtils.setTextSize(viewHolder.name, 34 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		LayoutUtils.rateScale(context, viewHolder.name, true);
		LayoutUtils.setTextSize(viewHolder.date, 25 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.rateScale(context, viewHolder.camera, true);
		LayoutUtils.rateScale(context, viewHolder.right, true);
		
		convertView.setTag(viewHolder);
		} else {
		viewHolder = (ViewHolderlist) convertView.getTag();
		}
		
		viewHolder.date .setText(DateFormatUtil.getStringDataByLong(Long.parseLong(datalist.get(position).getCreateTime())));
		String content = datalist.get(position).getName();
		if (!MyUtils.isNull(content)) {
			content = content.trim();
			if (content.contains("\r")||content.contains("\n")) {
				String arr[] = null;
				if (content.contains("\r")) {
					arr = content.split("\r");
				}else if (content.contains("\n")) {
					arr = content.split("\n");
				}
				if (arr != null && arr.length > 0) {
					content = arr[0];
				}
			}
			if (!MyUtils.isNull(content) && content.length()>10) {
				content = content.substring(0, 10) +"...";
			}
			
			viewHolder.name.setText(content);
		}
		if((datalist.get(position).getLocal_path() == null  || datalist.get(position).getLocal_path().length() ==0 || 
				datalist.get(position).getLocal_path().equals("null")) && (datalist.get(position).getUrl() == null||datalist.get(position).getUrl().equals(""))){
			viewHolder.camera.setVisibility(View.GONE);
		}else{
			viewHolder.camera.setVisibility(View.VISIBLE);
		}
			
	
		return convertView;
	}


      
}
class ViewHolderlist
{ 
    public TextView date; 
    public ImageView pen;
    public ImageView camera;
    public ImageView right;
    public TextView name; 
} 
