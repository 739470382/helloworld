package com.bravesoft.riumachi.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.bean.CarlendarBean;
import com.bravesoft.riumachi.bean.DailyEventBean;
import com.bravesoft.riumachi.bean.ScheduleBean;
import com.bravesoft.riumachi.constant.DailyEventType;
import com.bravesoft.riumachi.constant.MedicineType;
import com.bravesoft.riumachi.constant.ScheduleAllDay;
import com.bravesoft.riumachi.constant.ScheduleType;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.layout.LayoutUtils;
import com.bravesoft.riumachi.util.DateFormatUtil;
import com.bravesoft.riumachi.util.MyUtils;

public class DailyEventAdapter extends BaseAdapter {

	private Context context;
	private List<ScheduleBean> data;

	public DailyEventAdapter(Context context, List<ScheduleBean> data) {
		this.context = context;
		this.data = data;
	}

	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}

	@Override
	public ScheduleBean getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setData(List<ScheduleBean> data) {
		this.data = data;
		this.notifyDataSetChanged();
	}

	public void addData(List<ScheduleBean> data) {
		if (data == null) {
			data = new ArrayList<ScheduleBean>();
		}
		this.data.addAll(data);
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;

		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_daily_event_list, parent, false);
			viewHolder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relative_daily_event_item);
			viewHolder.mImgType = (ImageView) convertView.findViewById(R.id.img_daily_event_type);
			viewHolder.mImgArrow = (ImageView) convertView.findViewById(R.id.img_allow_all);
			viewHolder.mTxtTitle = (TextView) convertView.findViewById(R.id.txt_daily_event_title);
			viewHolder.mTxtDate = (TextView) convertView.findViewById(R.id.txt_daily_event_date);
			viewHolder.mTxtDate.setVisibility(View.GONE);
			viewHolder.viewLineOne = convertView.findViewById(R.id.horizotal_line_one);
			LayoutUtils.rateScale(context, viewHolder.viewLineOne, true);
			LayoutUtils.rateScale(context, viewHolder.relativeLayout, true);
			LayoutUtils.rateScale(context, viewHolder.mImgType, true);
			LayoutUtils.rateScale(context, viewHolder.mImgArrow, true);
			LayoutUtils.setTextSize( viewHolder.mTxtTitle, 32 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
			LayoutUtils.setTextSize( viewHolder.mTxtDate, 26 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		ScheduleBean scheduleBean = data.get(position);
		
//		for (int i = 0; i < DailyEventType.DAILY_EVENT_ARRAY.length; i++) {
//			if (scheduleBean.getType().equals(DailyEventType.DAILY_EVENT_ARRAY[i])) {
//				viewHolder.mImgType.setBackgroundResource(DailyEventType.DAILY_EVENT_IMAGE[i]);
//				break;
//			}
//		}
		String type = data.get(position).getType();
		if (!MyUtils.isNull(type)) {
			if (type.equals(ScheduleType.NORMAL_SCHEDULE)) {
				viewHolder.mImgType.setBackgroundResource(ScheduleType.SCHEDULE_IMAGE[0]);
			}else if (type.equals(ScheduleType.SEE_THE_DOCTER_SCHEDULE)) {
				viewHolder.mImgType.setBackgroundResource(ScheduleType.SCHEDULE_IMAGE[1]);
			}else if (type.equals(ScheduleType.MEDICINE_SCHEDULE)) {
				String medicineType = data.get(position).getMedicalType();
				if (!MyUtils.isNull(medicineType)) {
					if (medicineType.equals(MedicineType.ANTIBIOTIC)) {
						viewHolder.mImgType.setBackgroundResource(R.drawable.icon_medicine_a);
					}else if (medicineType.equals(MedicineType.BIOLOGCAL_AGENT)) {
						viewHolder.mImgType.setBackgroundResource(R.drawable.icon_medicine_b);
					}if (medicineType.equals(MedicineType.OTHER_MEDICINE)) {
						viewHolder.mImgType.setBackgroundResource(R.drawable.icon_medicine_c);
					}
				}
			}
		}
		
		ScheduleBean mScheduleBean = data.get(position);
		viewHolder.mTxtTitle.setText(scheduleBean.getTitle());
		String mStartDate = mScheduleBean.getStarttime();
		String mEndDate = mScheduleBean.getEndtime();
		if (mScheduleBean.getShunichi() == null|| mScheduleBean.getShunichi().equals(ScheduleAllDay.OFF)) {
			viewHolder.mTxtDate.setVisibility(View.VISIBLE);
			viewHolder.mTxtDate.setText(DateFormatUtil.getStringTwoDateDistance(mStartDate , mEndDate));
		}else{
			viewHolder.mTxtDate.setVisibility(View.GONE);
//			mStartDate = DateFormatUtil.geDataLongZeroClick(Long.parseLong(mStartDate))+"";
//			mEndDate = DateFormatUtil.geDataLongZeroClick(Long.parseLong(mEndDate))+"";
//			viewHolder.mTxtDate.setText(context.getString(R.string.schedule_all_day));
		}


		return convertView;
	}

	private class ViewHolder {
		private View viewLineOne;
		private RelativeLayout relativeLayout;
		private ImageView mImgType;
		private ImageView mImgArrow;
		private TextView mTxtTitle;
		private TextView mTxtDate;
	}

}
