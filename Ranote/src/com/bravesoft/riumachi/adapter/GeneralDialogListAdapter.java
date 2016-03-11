package com.bravesoft.riumachi.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.layout.LayoutUtils;

public class GeneralDialogListAdapter extends BaseAdapter {
	private Context context;
	private String[] data;
	private boolean isMoreClose = false;
	private boolean isCustomLoop = false;
	private int fucas = -1;
	
	public GeneralDialogListAdapter(Context context , String[] data ,boolean isMoreClose) {
		this.context = context;
		this.data = data;
		this.isMoreClose = isMoreClose;
	}
	
	public GeneralDialogListAdapter(Context context , String[] data ,boolean isMoreClose,boolean isCustomLoop) {
		this.context = context;
		this.data = data;
		this.isMoreClose = isMoreClose;
		this.isCustomLoop = isCustomLoop;
	}

	@Override
	public int getCount() {
		return data == null ? 0 : data.length;
	}

	@Override
	public Object getItem(int position) {
		return data[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setData (String[] data){
		this.data = data;
		this.notifyDataSetChanged();
	}

	public void setFocus(int position) {

		if (position >= 0 && position < data.length) {

			if (fucas == position) {
				return;
			} else {
				fucas = position;
			}
			notifyDataSetChanged();
		}
	}
	public int getFocus(){
		return fucas;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;
		
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_general_dialog_list, parent, false);
			viewHolder.txtContent = (TextView) convertView.findViewById(R.id.txt_content);
			viewHolder.radioChooseMark = (RadioButton) convertView.findViewById(R.id.radio_choose_mark);
			viewHolder.relativeLayout = (RelativeLayout)convertView.findViewById(R.id.relative_new_event_item);
			LayoutUtils.rateScale(context, viewHolder.radioChooseMark, true);
			LayoutUtils.rateScale(context, viewHolder.txtContent, true);
			LayoutUtils.rateScale(context, viewHolder.relativeLayout, true);
			LayoutUtils.setTextSize(viewHolder.txtContent, 29 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
			if (isMoreClose) {
				LayoutParams params = (LayoutParams) viewHolder.radioChooseMark.getLayoutParams();
				params.rightMargin = LayoutUtils.getRate4px(30);
				viewHolder.radioChooseMark.setLayoutParams(params);
			}
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if (fucas == position) {
			viewHolder.radioChooseMark.setChecked(true);
		} else {
			viewHolder.radioChooseMark.setChecked(false);
		}
		
		if (isCustomLoop && position == data.length-1) {
			viewHolder.radioChooseMark.setVisibility(View.GONE);
		}else{
			viewHolder.radioChooseMark.setVisibility(View.VISIBLE);
		}
		
		viewHolder.txtContent.setText(data[position]);
		return convertView;
	}

	private class ViewHolder{
		private TextView txtContent;
		private RadioButton radioChooseMark;
		private RelativeLayout relativeLayout;
	}
	
}
