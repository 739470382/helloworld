package com.bravesoft.riumachi.util;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.bravesoft.riumachi.bean.CarlendarBean;

/**
 * @author wangyuanshi
 */
public class DateCalculateUtils {

	/*
	 * Returns the month of the calendar month based on the number of days
	 */
	public static List<CarlendarBean> getCalendarData(long dateLong) {
		long firstDateLong = DateFormatUtil
				.getDataLongFirstDayZeroClick(dateLong);
		int totalDay = DateFormatUtil.countDaysOfCurrentMounth(dateLong);
		List<CarlendarBean> datas = new ArrayList<CarlendarBean>();

		for (int i = 0; i < totalDay; i++) {
			CarlendarBean carlendarBean = new CarlendarBean();
			carlendarBean.setDate(i + 1 + "");
			carlendarBean.setDateLong(DateFormatUtil.ONE_DAY * i
					+ firstDateLong);
			datas.add(carlendarBean);
		}
		return datas;
	}

	/*
	 * Get on the 1st of this month a few days away from Sunday, calculate the number of spaces in front of the calendar
	 */
	public static int getDistanceWithSunday(long dateLong) {
		String[] arrDate = DateFormatUtil.getArrayData(dateLong);
		int iFirstDayOfWeek = Calendar.SUNDAY;
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Integer.parseInt(arrDate[0]),
				Integer.parseInt(arrDate[1]) - 1, 1);
		int day = lastDate.get(Calendar.DAY_OF_WEEK) - iFirstDayOfWeek;
		return day;
	}

	/**
	 * Change the calendar month after the selected date display
	 * @param dateLong Current timestamp
	 * @param recentlyDay Change before the selected date
	 * @param operate Greater than 0 to less than zero for the month plus minus one month
	 * @return
	 */
	public static String getDayWithMonthChanged(long dateLong, String recentlyDay, int operate) {
		int recentlyDayInt = Integer.parseInt(recentlyDay);
		if (recentlyDayInt <= 28) {
			return recentlyDay;
		} else {
			int totalDay ;
			if (operate > 0) {
				totalDay = (int) DateFormatUtil.addOneMonthLongData(dateLong);
			}else {
				totalDay = (int) DateFormatUtil.subOneMonthLongData(dateLong);
			}
			
			if (Integer.parseInt(recentlyDay) <= totalDay) {
				return recentlyDay;
			} else {
				recentlyDay = totalDay+"";

			}
		}
		return recentlyDay;
	}


	public static List<CarlendarBean> getDefaultCalendarData() {
		List<CarlendarBean> datas = new ArrayList<CarlendarBean>();

		for (int i = 0; i < getDefaultMonthTotalDay(); i++) {
			CarlendarBean carlendarBean = new CarlendarBean();
			carlendarBean.setDate(i + 1 + "");
//			carlendarBean.setHasAntibioticSchedule(true);
//			carlendarBean.setHasBiologicalAgentSchedule(true);
//			carlendarBean.setHasNormalSchedule(true);
//			carlendarBean.setHasOtherMedicineSchedule(true);
//			carlendarBean.setHasSeeTheDocterSchedule(true);
			datas.add(carlendarBean);
		}
		return datas;
	}

	public static int getDefaultMonthTotalDay() {
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// Set on the 1st of the current month
		lastDate.add(Calendar.MONTH, 1);// Plus one month, becomes the 1st of next month
		lastDate.add(Calendar.DATE, -1);// Minus one day becomes the last day of the month
		System.out.println(str);
		str = sdf.format(lastDate.getTime());
		return Integer.parseInt(str.substring(8));
	}

	public static int getDefaultDistanceWithSunday() {
		int iFirstDayOfWeek = Calendar.SUNDAY;
		Calendar lastDate = Calendar.getInstance();
		lastDate.set(Calendar.DATE, 1);// Set on the 1st of the current month
		int day = lastDate.get(Calendar.DAY_OF_WEEK) - iFirstDayOfWeek;
		return day;
	}

	public static int getDefaultCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	public static int getDefaultCurrentMonth() {
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	public static int getDefaultCurrentDay() {
		return Calendar.getInstance().get(Calendar.DATE);
	}

	public static int getDayOfMonth() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	public static int getDayOfYear() {
		return Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
	}

	public static String getDefaultDay() {
		return getDefaultCurrentYear() + "/" + getDefaultCurrentMonth();
	}

}
