package com.bravesoft.riumachi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.layout.LayoutUtils;

public class SettingDialogAdapter extends BaseAdapter {
	private Context context;
	private int[] data = {R.string.setting_popup_setting ,R.string.setting_popup_about };
	
	public SettingDialogAdapter(Context context) {
		this.context = context;
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
	
	public void setData (int[] data){
		this.data = data;
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_dialog_setting_list, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.txtContent = (TextView) convertView.findViewById(R.id.txt_dialog_setting);
			viewHolder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relative_item_setting);
			LayoutUtils.setTextSize(viewHolder.txtContent, 30  ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
			LayoutUtils.rateScale(context, viewHolder.relativeLayout, true);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.txtContent.setText(data[position]);
		
		return convertView;
	}

	private class ViewHolder{
		private TextView txtContent;
		private RelativeLayout relativeLayout;
	}
	
}
