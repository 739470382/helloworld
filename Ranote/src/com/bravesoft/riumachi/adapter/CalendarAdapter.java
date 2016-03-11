package com.bravesoft.riumachi.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.appliction.App;
import com.bravesoft.riumachi.bean.CarlendarBean;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.layout.LayoutUtils;
import com.bravesoft.riumachi.util.DateFormatUtil;

@SuppressLint("NewApi")
public class CalendarAdapter extends BaseAdapter {

	private Context context;
	private List<CarlendarBean> data;
	private int mBlankSpaceDays = 0;
	private int fucas = -1;

	public CalendarAdapter(Context context, List<CarlendarBean> data,
			int blankSpaceDays) {
		this.context = context;
		this.data = data;
		this.mBlankSpaceDays = blankSpaceDays;
	}

	//Set GridView fixed line 6 * 7
	@Override
	public int getCount() {
		return 42;
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

	//Analyzing coordinates are legitimate
	public boolean isLegalPostion(int position){
		if (data != null && position >= mBlankSpaceDays
				&& position < data.size()+mBlankSpaceDays) {
			return true;
		}
		return false;
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
			if (day.equals(data.get(i).getDate())) {

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
					R.layout.item_calendar_grid, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.viewBackGround = convertView
					.findViewById(R.id.view_calendar_background);
			viewHolder.relativeSeeToDocter = (RelativeLayout) convertView
					.findViewById(R.id.relative_see_to_docter);
			viewHolder.relativeCalendarItem = (RelativeLayout) convertView
					.findViewById(R.id.relative_calendar_item);
			viewHolder.txtDate = (TextView) convertView
					.findViewById(R.id.txt_date);
			viewHolder.bottomLine = convertView
					.findViewById(R.id.horizotal_line_one);
			LayoutParams layoutParams = (LayoutParams) viewHolder.txtDate
					.getLayoutParams();
			layoutParams.width = layoutParams.height;
			viewHolder.txtDate.setLayoutParams(layoutParams);
			viewHolder.imgNormalSchedule = (ImageView) convertView
					.findViewById(R.id.img_normal_schedule);
			viewHolder.imgBiologicalAgent = (ImageView) convertView
					.findViewById(R.id.txt_bological_agent_mark);
			viewHolder.imgAntibiotic = (ImageView) convertView
					.findViewById(R.id.txt_antibiotic_mark);
			viewHolder.imgOtherMedicine = (ImageView) convertView
					.findViewById(R.id.txt_other_medicine_mark);
			LayoutUtils.rateScale(context, viewHolder.imgNormalSchedule, true);
			LayoutUtils.rateScale(context, viewHolder.relativeCalendarItem,
					true);
			LayoutUtils
					.rateScale(context, viewHolder.relativeSeeToDocter, true);
			LayoutUtils.rateScale(context, viewHolder.txtDate, true);
			LayoutUtils.rateScale(context, viewHolder.imgBiologicalAgent, true);
			LayoutUtils.rateScale(context, viewHolder.imgAntibiotic, true);
			LayoutUtils.rateScale(context, viewHolder.imgOtherMedicine, true);
			LayoutUtils.setTextSize(viewHolder.txtDate, 26 ,TextTypeFace.TYPEFACE_ROBOTO_REGULAR);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (data != null && position >= mBlankSpaceDays
				&& position < data.size()+mBlankSpaceDays) {
			if (fucas == position) {
				viewHolder.viewBackGround.setBackgroundColor(context.getResources().getColor(R.color.item_selected_background));
			} else {
				viewHolder.viewBackGround.setBackground(null);
			}
			viewHolder.relativeCalendarItem.setVisibility(View.VISIBLE);
			CarlendarBean carlendarBean = data.get(position - mBlankSpaceDays);

			if (carlendarBean.isHasSeeTheDocterSchedule()) {
				viewHolder.relativeSeeToDocter.setBackground(context
						.getResources().getDrawable(R.drawable.oval));
			} else {
				viewHolder.relativeSeeToDocter.setBackground(null);
			}

			if (carlendarBean.isHasNormalSchedule()) {
				viewHolder.imgNormalSchedule.setVisibility(View.VISIBLE);
			} else {
				viewHolder.imgNormalSchedule.setVisibility(View.INVISIBLE);
			}

			if (carlendarBean.isHasBiologicalAgentSchedule()) {
				viewHolder.imgBiologicalAgent.setVisibility(View.VISIBLE);
			} else {
				viewHolder.imgBiologicalAgent.setVisibility(View.GONE);
			}

			if (carlendarBean.isHasAntibioticSchedule()) {
				viewHolder.imgAntibiotic.setVisibility(View.VISIBLE);
			} else {
				viewHolder.imgAntibiotic.setVisibility(View.GONE);
			}

			if (carlendarBean.isHasOtherMedicineSchedule()) {
				viewHolder.imgOtherMedicine.setVisibility(View.VISIBLE);
			} else {
				viewHolder.imgOtherMedicine.setVisibility(View.GONE);
			}

			if (DateFormatUtil.geDataLongZeroClick(carlendarBean.getDateLong()) 
					== DateFormatUtil.geDataLongZeroClick(System.currentTimeMillis())) {
				viewHolder.txtDate.setTypeface(App.getInstance().getmTypefacRobotoBold());
				viewHolder.txtDate.setTextColor(context.getResources().getColor(R.color.blue));
			}else{
				viewHolder.txtDate.setTypeface(App.getInstance().getmTypefacRobotoRegular());
				viewHolder.txtDate.setTextColor(context.getResources().getColor(R.color.item_txt_un_select));
			}
			viewHolder.txtDate.setText(carlendarBean.getDate());
		} else {
			viewHolder.relativeCalendarItem.setVisibility(View.INVISIBLE);
			viewHolder.viewBackGround.setBackground(null);
		}
		
		if (position > 34) {
			viewHolder.bottomLine.setVisibility(View.GONE);
		}else{
			viewHolder.bottomLine.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	private class ViewHolder {
		private RelativeLayout relativeCalendarItem;
		private RelativeLayout relativeSeeToDocter;
		private ImageView imgNormalSchedule;
		private View viewBackGround;
		private View bottomLine;
		private TextView txtDate;
		private ImageView imgBiologicalAgent;
		private ImageView imgAntibiotic;
		private ImageView imgOtherMedicine;
	}

}
