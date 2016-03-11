package com.bravesoft.riumachi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.layout.LayoutUtils;

public class CreateNewEventAdapter extends BaseAdapter {
	private Context context;
	private int[][] data = {{R.drawable.icon_calendar ,R.string.schedule_title },
							{R.drawable.icon_see_the_docter ,R.string.see_to_docter_title },
							{R.drawable.icon_medicine_a ,R.string.medicine_title },
							{R.drawable.icon_human ,R.string.my_car_title }};
	
	public CreateNewEventAdapter(Context context) {
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
	
	public void setData (int[][] data){
		this.data = data;
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;
		
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_new_event_list, parent, false);
			viewHolder.txtContent = (TextView) convertView.findViewById(R.id.txt_new_event_title);
			viewHolder.imgNewEventType = (ImageView) convertView.findViewById(R.id.img_new_event_type);
			viewHolder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relative_new_event_item);
			LayoutUtils.rateScale(context, viewHolder.relativeLayout, true);
			LayoutUtils.setTextSize(viewHolder.txtContent, 31 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
			LayoutUtils.rateScale(context, viewHolder.imgNewEventType, true);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
//		viewHolder.imgNewEventType.setBackgroundResource(data[position][0]);
		viewHolder.imgNewEventType.setImageResource(data[position][0]);
		viewHolder.txtContent.setText(data[position][1]);
		
		return convertView;
	}

	private class ViewHolder{
		private TextView txtContent;
		private ImageView imgNewEventType;
		private RelativeLayout relativeLayout;
	}
	
}
