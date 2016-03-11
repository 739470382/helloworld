package com.bravesoft.riumachi.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.bean.HaqVasBean;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.layout.LayoutUtils;
import com.bravesoft.riumachi.util.DateFormatUtil;

@SuppressLint("NewApi")
public class VasHaqAdapter extends BaseAdapter {
	// private String predate = "";
	private Context context;
	private List<HaqVasBean> data;

	// private int fucas = -1;

	public VasHaqAdapter(Context context, List<HaqVasBean> data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}

	@Override
	public Object getItem(int position) {

		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_vas_haq_list, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.vashaq_data = (TextView) convertView
					.findViewById(R.id.vashaq_data);
			viewHolder.vashaq_time = (TextView) convertView
					.findViewById(R.id.vashaq_time);
			viewHolder.item_line = convertView.findViewById(R.id.item_line);
			viewHolder.item_height = (LinearLayout) convertView
					.findViewById(R.id.item_height);
			viewHolder.vashaq_img = (ImageView) convertView
					.findViewById(R.id.vashaq_img);
			viewHolder.view1 = convertView.findViewById(R.id.view1);
			viewHolder.view2 = convertView.findViewById(R.id.view2);
			viewHolder.view = convertView.findViewById(R.id.view);

			LayoutUtils.rateScale(context, viewHolder.view2, true);
			LayoutUtils.rateScale(context, viewHolder.view1, true);
			LayoutUtils.rateScale(context, viewHolder.view, true);

			LayoutUtils.rateScale(context, viewHolder.vashaq_time, true);
			LayoutUtils.setTextSize(viewHolder.vashaq_time, 25  ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
			LayoutUtils.setTextSize(viewHolder.vashaq_data, 34,
					TextTypeFace.TYPEFACE_ROBOTO_REGULAR);

			LayoutUtils.rateScale(context, viewHolder.vashaq_data, true);
			LayoutUtils.rateScale(context, viewHolder.vashaq_img, true);
			LayoutUtils.rateScale(context, viewHolder.item_height, true);
			LayoutUtils.rateScale(context,
					(RelativeLayout) convertView.findViewById(R.id.vashaq_lin),
					true);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		String temp = DateFormatUtil.getStringData(Long.parseLong(data.get(
				position).getDateNo()));

		if (position != 0
				&& (DateFormatUtil.getStringData(Long.parseLong(data.get(
						position - 1).getDateNo())).equals(temp))) {
			viewHolder.vashaq_time.setVisibility(View.GONE);

			// ViewGroup.LayoutParams params=convertView.getLayoutParams();
			// params.height=95;
			// convertView.setLayoutParams(params);
			viewHolder.item_line.setVisibility(View.GONE);
			viewHolder.view1.setVisibility(View.GONE);

		} else {
			// ViewGroup.LayoutParams params=convertView.getLayoutParams();
			// params.height=193;
			// convertView.setLayoutParams(params);
			viewHolder.vashaq_time.setVisibility(View.VISIBLE);
			viewHolder.view1.setVisibility(View.VISIBLE);
			viewHolder.item_line.setVisibility(View.VISIBLE);
			String date = temp.replaceFirst("-", "年").replace("-", "月");
			viewHolder.vashaq_time.setText(date + "日");
		}
		if (position == 0) {

			viewHolder.item_line.setVisibility(View.INVISIBLE);
		}
		viewHolder.vashaq_data.setText(data.get(position).getCount() + "点");
		if (data.get(position).getType().equals("1")) {

			viewHolder.vashaq_img.setBackground(context.getResources()
					.getDrawable(R.drawable.icon_vas));
		} else {

			viewHolder.vashaq_img.setBackground(context.getResources()
					.getDrawable(R.drawable.icon_haq));
		}
		return convertView;
	}

	private class ViewHolder {
		private View item_line;
		private View currentView;
		private TextView vashaq_time, vashaq_data;
		private RelativeLayout vashaq_lin;
		private ImageView vashaq_img;
		private LinearLayout item_height;
		private View view1;
		private View view2;
		private View view;
	}

}
