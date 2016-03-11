package com.bravesoft.riumachi.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.bean.CarlendarBean;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.layout.LayoutUtils;
import com.bravesoft.riumachi.util.DateFormatUtil;

@SuppressLint("NewApi")
public class DatePickCalendarGridAdapter extends BaseAdapter {

	private Context context;
	private List<CarlendarBean> data;
	private int mBlankSpaceDays = 0;
	private int fucas = -1;

	public DatePickCalendarGridAdapter(Context context, List<CarlendarBean> data,
			int blankSpaceDays) {
		this.context = context;
		this.data = data;
		this.mBlankSpaceDays = blankSpaceDays;
	}

	@Override
	public int getCount() {
		return data == null ? 0 : data.size() + mBlankSpaceDays;
	}

	@Override
	public Object getItem(int position) {
		return data.get(position - mBlankSpaceDays);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setData(List<CarlendarBean> data, int blankSpaceDays) {
		this.data = data;
		this.notifyDataSetChanged();
		this.mBlankSpaceDays = blankSpaceDays;
		notifyDataSetChanged();
	}

	public void addData(List<CarlendarBean> data) {
		if (data == null) {
			data = new ArrayList<CarlendarBean>();
		}
		this.data.addAll(data);
		this.notifyDataSetChanged();
	}

	public void setFocus(int position) {

		if (position == -1) {
			fucas = position;
		}else if (position >= 0 && position < data.size() + mBlankSpaceDays) {

			if (fucas == position) {
				return;
			} else {
				fucas = position;
			}
			notifyDataSetChanged();
		}
	}

	public void setFocus(String day) {
		
		for (int i = 0; i < data.size(); i++) {
			if(day.equals(data.get(i).getDate())){
				
				if (fucas == i + mBlankSpaceDays) {
					return;
				} else {
					fucas = i + mBlankSpaceDays;
					notifyDataSetChanged();
					break;
				}
			}
		}
	}

	public void setFocusDate(long focuasDate) {

		for (int i = 0; i < data.size(); i++) {
			if (DateFormatUtil.geDataLongZeroClick(data.get(i).getDateLong())
					== DateFormatUtil.geDataLongZeroClick(focuasDate)) {
				fucas = i + mBlankSpaceDays;
			}
		}
	}

	
	public int getFocus() {
		return fucas;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_date_picker_calendar_grid, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.viewBackGround = convertView
					.findViewById(R.id.relative_date);
			viewHolder.relativeCalendarItem = (RelativeLayout) convertView
					.findViewById(R.id.relative_calendar_item);
			viewHolder.txtDate = (TextView) convertView
					.findViewById(R.id.txt_date);
			LayoutUtils.rateScale(context, viewHolder.relativeCalendarItem,true);
			LayoutUtils.setTextSize(viewHolder.txtDate, 24 ,TextTypeFace.TYPEFACE_ROBOTO_REGULAR);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (position < mBlankSpaceDays) {
			viewHolder.relativeCalendarItem.setVisibility(View.GONE);
			viewHolder.viewBackGround.setBackground(null);
		} else {
			if (fucas == position) {
				viewHolder.txtDate.setTextColor(context.getResources().getColor(R.color.white));
				viewHolder.viewBackGround.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.selection_pickedate_calender));
			} else {
				viewHolder.txtDate.setTextColor(context.getResources().getColor(R.color.gray));
				viewHolder.viewBackGround.setBackground(null);
			}
			viewHolder.relativeCalendarItem.setVisibility(View.VISIBLE);
			CarlendarBean carlendarBean = data.get(position - mBlankSpaceDays);

			viewHolder.txtDate.setText(carlendarBean.getDate());
		}

		return convertView;
	}

	private class ViewHolder {
		private RelativeLayout relativeCalendarItem;
		private View viewBackGround;
		private TextView txtDate;
	}

}
