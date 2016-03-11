package com.bravesoft.riumachi.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.bean.MyKarteBean;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.layout.LayoutUtils;
import com.bravesoft.riumachi.util.MyUtils;

public class EditTextAdapter extends BaseAdapter {
	private Context context;
	private List<String> data;
	
	public EditTextAdapter(Context context , List<String> data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}

	@Override
	public String getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setData (List<String> data){
		if (this.data == null) {
			this.data = new ArrayList<String>();
		}
		this.data = data;
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;
		
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_edit_view_list, parent, false);
			viewHolder.txtContent = (TextView) convertView.findViewById(R.id.txt_mycard_content);
			viewHolder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relative_my_card_item);
			LayoutUtils.rateScale(context, viewHolder.relativeLayout, true);
			LayoutUtils.rateScale(context, viewHolder.txtContent, true);
			LayoutUtils.setTextSize(viewHolder.txtContent, 28 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.txtContent.setText(data.get(position));
															
		return convertView;
	}

	private class ViewHolder{
		//private View viewLineOne;
		private TextView txtContent;
		private RelativeLayout relativeLayout;
	}
	
}
