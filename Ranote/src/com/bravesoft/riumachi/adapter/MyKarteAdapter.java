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

public class MyKarteAdapter extends BaseAdapter {
	private Context context;
	private List<MyKarteBean> data;
	
	public MyKarteAdapter(Context context , List<MyKarteBean> data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}

	@Override
	public MyKarteBean getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setData (List<MyKarteBean> data){
		if (this.data == null) {
			this.data = new ArrayList<MyKarteBean>();
		}
		this.data = data;
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;
		
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_mycard_list, parent, false);
			viewHolder.txtContent = (TextView) convertView.findViewById(R.id.txt_mycard_content);
			viewHolder.imgMyCardMark = (ImageView) convertView.findViewById(R.id.img_mycard_mark);
			viewHolder.imgAllow = (ImageView) convertView.findViewById(R.id.img_allow_all);
			viewHolder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relative_my_card_item);
			//viewHolder.viewLineOne = convertView.findViewById(R.id.horizotal_line_one);
			LayoutUtils.rateScale(context, viewHolder.relativeLayout, true);
			LayoutUtils.rateScale(context, viewHolder.imgAllow, true);
			//LayoutUtils.rateScale(context, viewHolder.viewLineOne, true);
			LayoutUtils.setTextSize(viewHolder.txtContent, 34 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
			 LayoutUtils.rateScale(context, viewHolder.imgMyCardMark, true);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		String content = data.get(position).getContent();
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
					viewHolder.txtContent.setText(arr[0]);
				}
			}else{
				viewHolder.txtContent.setText(content);
			}
		}
		
		return convertView;
	}

	private class ViewHolder{
		//private View viewLineOne;
		private TextView txtContent;
		private ImageView imgMyCardMark;
		private ImageView imgAllow;
		private RelativeLayout relativeLayout;
	}
	
}
