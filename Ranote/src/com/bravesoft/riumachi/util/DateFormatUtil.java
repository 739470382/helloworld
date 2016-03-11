package com.bravesoft.riumachi.util;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Time Conversion Tools
 * 
 * @author wangyuanshi
 * 
 */
public class DateFormatUtil {

	public static final long FIVE_MINUTE = 5 * 60l * 1000; // 5minutes
	public static final long FIFTEEN_MINUTE = 15 * 60l * 1000; // 15minutes
	public static final long THIRTY_MINUTE = 30 * 60l * 1000; // 30minutes
	public static final long ONE_HOUR = 60 * 60l * 1000; // 1hours
	public static final long TWO_HOUR = 2 * 60 * 60l * 1000; // 2hours
	public static final long ONE_DAY = 24 * 60 * 60l * 1000; // one day
	public static final long TWO_DAY = 2 * 24 * 60 * 60l * 1000; // two days
	public static final long ONE_WEEK = 7 * 24 * 60 * 60l * 1000; // one week
	public static final long TWO_WEEK = 14 * 24 * 60 * 60l * 1000; // two weeks

	/**
	 * @param strDate is a string and it has "yyyy-MM-dd" format.
	 * @return return a timestamp date 
	 */
	public static long getLongData(String strDate) {
		long longDate = 0;
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = format.parse(strDate);
			longDate = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return longDate;
	}

	/**
	 * @param strDate a timestamp
	 * @return the string date has been formatted to "yyyy-MM-dd"
	 */
	public static String getStringData(long dateLong) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = null;
		try {
			strDate = format.format(dateLong);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	/**
	 * @param strDate a timestamp
	 * @return the string date has been formatted to "yyyy/MM/dd"
	 */
	public static String getStringDataDiagonal(long dateLong) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		String strDate = null;
		try {
			strDate = format.format(dateLong);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	/**
	 * @param strDate a timestamp
	 * @return the string date has been formatted to "yyyy/MM/dd"
	 */
	public static long getLongDataDiagonal(String strDate) {
		long longDate = 0;
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		try {
			date = format.parse(strDate);
			longDate = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return longDate;
	}
	
	/**
	 * @param strDate a timestamp
	 * @return the string time has been formatted to "kk:mm:ss" (24-hour format)
	 */
	public static String getStringTime(long dateLong) {
		SimpleDateFormat format = new SimpleDateFormat("kk:mm:ss");
		String strDate = null;
		try {
			strDate = format.format(dateLong);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	/**
	 * @param strDate a timestamp
	 * @return the string time has been formatted to "kk:mm" (24-hour format)
	 */
	public static String getStringHourAndMinute(long dateLong) {
		SimpleDateFormat format = new SimpleDateFormat("kk:mm");
		String strDate = null;
		try {
			strDate = format.format(dateLong);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	/**
	 * @param strDate a timestamp
	 * @return the string time has been formatted to "kk:mm" (24-hour format)
	 * if the time is zero like 0:0:0,will change to 00:00 
	 */
	public static String getStringHourAndMinuteZero(long dateLong) {
		String arrDate[] = DateFormatUtil.getArrayWholeData(dateLong);
		int hour = 0 , minute = 0;
		String hourStr , minuteStr;
		if (arrDate.length > 4) {
			hour = Integer.parseInt(arrDate[3]);
			minute =  Integer.parseInt(arrDate[4]);
		}
		String strDate = null;
		if (hour == 24) {
			hour = 0;
		}
		hourStr = hour < 10 ? "0"+hour : hour+"";
		minuteStr = minute < 10 ? "0"+minute : minute+"";
		return hourStr+":"+minuteStr;
	}

	/**
	 * @param strDate a timestamp
	 * @return the string time has been formatted to "hh:mm" 
	 */
	public static String getStringHourAndMinuteHH(long dateLong) {
		SimpleDateFormat format = new SimpleDateFormat("hh:mm");
		String strDate = null;
		try {
			strDate = format.format(dateLong);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	/**
	 * @param strDate a timestamp
	 * @return the string time has been formatted to "HH:00" 
	 */
	public static String getStringHourAndMinuteZeroClick(long dateLong) {
		SimpleDateFormat format = new SimpleDateFormat("HH");
		String strDate = null;
		try {
			strDate = format.format(dateLong);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate + ":00";
	}

	/**
	 * the time add one hour
	 * @param dateLong a timestamp
	 * @return the string time has been formatted to "HH:00" 
	 */
	public static String getStringAddOneHourZeroClick(long dateLong) {
		dateLong += 60 * 60l * 1000;
		SimpleDateFormat format = new SimpleDateFormat("HH");
		String strDate = null;
		try {
			strDate = format.format(dateLong);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate + ":00";
	}

	/**
	 * the time add one hour
	 * @param dateLong a timestamp
	 * @return a long timestamp
	 */
	public static long getLongAddOneHour(long dateLong) {
		return dateLong += 60 * 60l * 1000;
	}

	/**
	 * @param strDate is a string and it has "yyyy年MM月dd日" format.
	 * @return return a timestamp date 
	 */
	public static long getLongDataByString(String strDate) {
		long longDate = 0;
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		try {
			date = format.parse(strDate);
			longDate = date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return longDate;
	}

	/**
	 * @param dateLong a timestamp
	 * @return the string date has been formatted to "yyyy-MM-dd"
	 */
	public static String getStringDataByLong(long dateLong) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
		String strDate = null;
		try {
			strDate = format.format(dateLong);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}
	
	/**
	 * get the year "yyyy"
	 * @param dateLong a timestamp
	 * @return the string date has been formatted to "yyyy"
	 */
	public static String getStringYearByLong(long dateLong) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		String strDate = null;
		try {
			strDate = format.format(dateLong);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	/**
	 * get the week "*曜日"
	 * @param dateLong a timestamp
	 * @return the string date has been formatted to "*曜日"
	 */
	public static String getStringWeekByLong(long dateLong) {
		Calendar calendar = Calendar.getInstance();
		Date date = new Date(dateLong);
		SimpleDateFormat format = new SimpleDateFormat("MM/dd");
		String dataWithWeek = format.format(date);
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		switch (dayOfWeek) {
		case 1:
			dataWithWeek=("日曜日");
			break;
		case 2:
			dataWithWeek=("月曜日");
			break;
		case 3:
			dataWithWeek=("火曜日");
			break;
		case 4:
			dataWithWeek=("水曜日");
			break;
		case 5:
			dataWithWeek=("木曜日");
			break;
		case 6:
			dataWithWeek=("金曜日");
			break;
		case 7:
			dataWithWeek=("土曜日");
			break;

		default:
			break;
		}
		return dataWithWeek;
	}

	/**
	 * get the month and day "MM月dd日"
	 * @param dateLong a timestamp
	 * @return the string date has been formatted to "MM月dd日"
	 */
	public static String getStringMonthByLong(long dateLong) {
		SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
		String strDate = null;
		try {
			strDate = format.format(dateLong);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}
	
	/**
	 * @param dateLong a timestamp
	 * @return return a timestamp date ,is the month first day and one clock
	 */
	public static long geDataLongZeroDay(long dateLong) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String[] DateArr = getArrayData(dateLong);
		String strDate = DateArr[0] + "-" + DateArr[1] + "-01" + " 01:00:00";
		long zeroClick = 0;
		try {
			date = format.parse(strDate);
			zeroClick = date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zeroClick;
	}

	/**
	 * get date 0 hour ,0 minute and 0 second.
	 * @param dateLong a timestamp
	 * @return return a timestamp date ,0 hour ,0 minute and 0 second.
	 */
	public static long geDataLongZeroClick(long dateLong) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String strDate = getStringData(dateLong) + " 00:00:00";
		long zeroClick = 0;
		try {
			date = format.parse(strDate);
			zeroClick = date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zeroClick;
	}

	/**
	 * get date 24 hour ,0 minute and 0 second.
	 * @param dateLong a timestamp
	 * @return return a timestamp date ,24 hour ,0 minute and 0 second.
	 */
	public static long geDataLongZeroTheLastClick(long dateLong) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String strDate = getStringData(dateLong) + " 24:00:00";
		long zeroClick = 0;
		try {
			date = format.parse(strDate);
			zeroClick = date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zeroClick;
	}

	/*
	 * Return the time passed 24 to 0 minutes 0 seconds of the timestamp
	 */
	public static String geDataStringZeroTheLastClick(long dateLong) {
		return geDataLongZeroTheLastClick(dateLong) + "";
	}

	/*
	 * Return time timestamp 0 minutes 0 seconds
	 */
	public static long geDataLongZeroMinuteAndSeconed(long dateLong) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String[] arrDate = getArrayWholeData(dateLong);
		String strDate = arrDate[0] + "-" + arrDate[1] + "-" + arrDate[2] + " "
				+ arrDate[3] + ":00:00";
		long zeroClick = 0;
		try {
			date = format.parse(strDate);
			zeroClick = date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zeroClick;
	}

	/*
	 * Return time 00:00:00 timestamp String format
	 */
	public static String geDataStringZeroClick(long dateLong) {
		return geDataLongZeroClick(dateLong) + "";
	}

	/*
	 * Return "yyyy-MM-dd HH: mm: ss" formatted time
	 */
	public static String getWholeDate(long dateLong) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strDate = null;
		try {
			strDate = format.format(dateLong);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	/**
	 * get the month and day "yyyy/mm"
	 * @param dateLong a timestamp
	 * @return the string date has been formatted to "yyyy/mm"
	 */
	public static String getStringDatayyyyMM(long dateLong) {
		String[] dateArr = getArrayData(dateLong);
		return dateArr[0] + "/" + Integer.parseInt(dateArr[1]);
	}

	/**
	 * get the month and day "yyyy-MM"
	 * @param dateLong a timestamp
	 * @return the string date has been formatted to "yyyy-MM"
	 */
	public static String getStringDatayyyy_MM(long dateLong) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		String strDate = null;
		try {
			strDate = format.format(dateLong);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	/**
	 * get the day "dd" ignore 0 like '9'
	 * @param dateLong a timestamp
	 * @return the string date has been formatted to "dd"
	 */
	public static String getStringDay(long dateLong) {
		SimpleDateFormat format = new SimpleDateFormat("dd");
		String strDate = null;
		try {
			strDate = format.format(dateLong);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Integer.parseInt(strDate) + "";
	}

	/**
	 * get a timestamp splice by "dateLong"'s day and "HourAndMinute"'s hour and minute ignore second.
	 * @param dateLong a timestamp
	 * @param HourAndMinute hour and minute
	 * @return return a timestamp date.yyyy-MM-dd HH:mm:00.
	 */
	public static long getLongDateBySplicing(long dateLong, String HourAndMinute) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String strDate = getStringData(dateLong) + " " + HourAndMinute + ":00";
		long zeroMillisecond = 0;
		try {
			date = format.parse(strDate);
			zeroMillisecond = date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zeroMillisecond;
	}
	

	/**
	 * get a timestamp splice by "dateLong"'s day and "timeLong"'s hour and minute ignore second.
	 * @param dateLong a timestamp
	 * @param timeLong a timestamp
	 * @return return a timestamp date,yyyy-MM-dd HH:mm:00
	 */
	public static long getLongDateBySplicing(long dateLong, long timeLong) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String strDate = getStringData(dateLong) + " " + getStringHourAndMinuteZero(timeLong) + ":00";
		long zeroMillisecond = 0;
		try {
			date = format.parse(strDate);
			zeroMillisecond = date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zeroMillisecond;
	}
	
	/**
	 * get a timestamp splice by "dateLong"'s day and "timeLong"'s hour and minute ignore second.
	 * @param dateLong a timestamp
	 * @param timeLong a timestamp
	 * @return return a timestamp date,yyyy-MM-dd HH:mm:ss
	 */
	public static long getLongDateBySplicingAll(long dateLong, String time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String strDate = getStringData(dateLong) + " " + time;
		long zeroMillisecond = 0;
		try {
			date = format.parse(strDate);
			zeroMillisecond = date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zeroMillisecond;
	}

	/**
	 * @param startDate a timestamp start date 
	 * @param endDate a timestamp end date
	 * @return two string date has been splice to HH:mm~HH:mm
	 */
	public static String getStringTwoDateDistance(String startDate,
			String endDate) {

		String strDate = getStringHourAndMinuteZero(Long.parseLong(startDate))
				+ "~" + getStringHourAndMinuteZero(Long.parseLong(endDate));

		return strDate;
	}

	/**
	 * @param dateLong a timestamp.
	 * @return the string date has been formatted to  "(week)"
	 */
	public static String getStringDateWithWeek(String dateLong) {

		String strDate = getStringDataByLong(Long.parseLong(dateLong)) + "("
				+ getStringWeek(Long.parseLong(dateLong)) + ")";

		return strDate;
	}

	public static String getStringWeek(long dateLong) {
		SimpleDateFormat format = new SimpleDateFormat("E");
		String strDate = null;
		try {
			strDate = format.format(dateLong);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	/**
	 * get a timestamp splice by "dateLong"'s day and "timeLong"'s hour and minute ignore second.
	 * @param dateLong a timestamp
	 * @param timeLong a timestamp
	 * @return return a timestamp date.yyyy-MM-dd HH:mm:ss
	 */
	public static long getStringTwoDatemerge(long dateLong, long timeLong) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String[] arrDate = getArrayWholeData(dateLong);
		String[] arrTime = getArrayWholeData(timeLong);
		String strDate = arrDate[0] + "-" + arrDate[1] + "-" + arrDate[2] + " "
				+ arrTime[3] + ":" + arrTime[4] + ":" + arrTime[5];
		long zeroClick = 0;
		try {
			date = format.parse(strDate);
			zeroClick = date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zeroClick;
	}

	/**
	 * get a timestamp splice by "dateLong"'s day and "timeLong"'s hour and minute ignore second.
	 * @param dateLong a timestamp
	 * @param timeLong a timestamp
	 * @return return a timestamp date.yyyy-MM-dd HH:mm:ss
	 */
	public static long getStringTwoDatemergeZeroClock(long dateLong,
			long timeLong) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String[] arrDate = getArrayWholeData(dateLong);
		String[] arrTime = getArrayWholeData(timeLong);
		String strDate = arrDate[0] + "-" + arrDate[1] + "-" + arrDate[2] + " "
				+ arrTime[3] + ":" + arrTime[4] + ":" + arrTime[5];
		long zeroClick = 0;
		try {
			date = format.parse(strDate);
			zeroClick = date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zeroClick;
	}

	/*
	 * Incoming timestamp, get the timestamp on the 1st of the month
	 */
	public static long getDataLongFirstDayZeroClick(long dateLong) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String strDate = getStringDatayyyy_MM(dateLong) + "-01 00:00:00";
		long zeroClick = 0;
		try {
			date = format.parse(strDate);
			zeroClick = date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zeroClick;
	}

	/*
	 * return the day 00:00:00 timestamp
	 */
	public static long getDataLongLastDayZeroClick(long dateLong) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String[] arrDate = DateFormatUtil.getArrayWholeData(dateLong);
		int year = Integer.parseInt(arrDate[0]);
		int month = Integer.parseInt(arrDate[1]);
		int day = Integer.parseInt(arrDate[2]);
		int totalDay = countDaysOfCurrentMounth(year, month);
		String strDate = year + "-" + month + "-" + totalDay + " 00:00:00";
		long zeroClick = 0;
		try {
			date = format.parse(strDate);
			zeroClick = date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zeroClick;
	}

	/*
	 *  return the day 24:00:00 timestamp
	 */
	public static long getDataLongLastDayLastClick(long dateLong) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String[] arrDate = DateFormatUtil.getArrayWholeData(dateLong);
		int year = Integer.parseInt(arrDate[0]);
		int month = Integer.parseInt(arrDate[1]);
		int day = Integer.parseInt(arrDate[2]);
		int totalDay = countDaysOfCurrentMounth(year, month);
		String strDate = year + "-" + month + "-" + totalDay + " 24:00:00";
		long zeroClick = 0;
		try {
			date = format.parse(strDate);
			zeroClick = date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zeroClick;
	}

	/*
	 * return the time add one month timestamp
	 */
	public static long addOneMonthLongData(long longDate) {
		String[] date = getArrayData(longDate);
		int totalDay = countDaysOfCurrentMounth(Integer.parseInt(date[0]),
				Integer.parseInt(date[1]));
		return longDate + totalDay * 60l * 60 * 24 * 1000;
	}

	/*
	 * return the time subtract one month timestamp
	 */
	public static long subOneMonthLongData(long longDate) {
		String[] date = getArrayData(longDate);
		int totalDay = countDaysOfPreMounth(Integer.parseInt(date[0]),
				Integer.parseInt(date[1]));
		return longDate - (totalDay * 60l * 60 * 24 * 1000);
	}
	
	/*
	 * Calculate how many days in the next month
	 */
	public static int countDaysOfNextMounth(long currentDate) {
		String[] arrDate = DateFormatUtil.getArrayData(currentDate);
		int year = Integer.parseInt(arrDate[0]);
		int month = Integer.parseInt(arrDate[1]);
		int totalDay = countDaysOfNextMounth(year, month);
		return totalDay;
	}

	/*
	 * Calculate how many days in the next month
	 */
	public static int countDaysOfNextMounth(int year, int month) {
		if (month == 12) {
			year += 1;
			month = 1;
		} else {
			month++;
		}
		if (year <= 0) {
			return 0;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		case 2:
			if ((year % 4 != 0) || ((year % 100 == 0) && (year % 400 != 0)))
				return 28;
			else
				return 29;
		default:
			return 0;
		}
	}
	
	/*
	 * Calculate how many days in the previous month
	 */
	public static int countDaysOfPreMounth(long currentDate) {
		String[] arrDate = DateFormatUtil.getArrayData(currentDate);
		int year = Integer.parseInt(arrDate[0]);
		int month = Integer.parseInt(arrDate[1]);
		int totalDay = countDaysOfPreMounth(year, month);
		return totalDay;
	}

	/*
	 * Calculate how many days in the previous month
	 */
	public static int countDaysOfPreMounth(int year, int month) {
		if (month == 1) {
			year -= 1;
			month = 12;
		} else {
			month--;
		}
		if (year <= 0) {
			return 0;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		case 2:
			if ((year % 4 != 0) || ((year % 100 == 0) && (year % 400 != 0)))
				return 28;
			else
				return 29;
		default:
			return 0;
		}
	}

	/*
	 * Calculate how many days in the current month
	 */
	public static int countDaysOfCurrentMounth(long currentDate) {
		String[] arrDate = DateFormatUtil.getArrayData(currentDate);
		int year = Integer.parseInt(arrDate[0]);
		int month = Integer.parseInt(arrDate[1]);
		int totalDay = countDaysOfCurrentMounth(year, month);
		return totalDay;
	}

	/*
	 * Calculate how many days in the current month
	 */
	public static int countDaysOfCurrentMounth(int year, int month) {
		if (year <= 0) {
			return 0;
		}
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		case 2:
			if ((year % 4 != 0) || ((year % 100 == 0) && (year % 400 != 0)))
				return 28;
			else
				return 29;
		default:
			return 0;
		}
	}

	/*
	 * split "dateLong" to String[6]. 0:year ,1:month , 2:day .
	 */
	public static String[] getArrayData(long dateLong) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = null;

		try {
			strDate = format.format(dateLong);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String[] arrDate = strDate.split("-");
		return arrDate;
	}

	/*
	 * put dateArr[3]. 0:year ,1:month , 2:day ,and retrun a timestamp.
	 */
	public static long getLongDataByArray(String[] dateArr) {
		long dateLong = getLongData(dateArr[0] + "-" + dateArr[1] + "-"
				+ dateArr[2]);
		return dateLong;
	}

	/**
	 * 
	 * @param currenrLongDate the current day
	 * @param changeDay you wanted to change day
	 * @return a long timestamp
	 */
	public static long getLongDataByArray(long currenrLongDate, String changeDay) {
		String[] dateArr = getArrayData(currenrLongDate);
		dateArr[2] = changeDay;
		return getLongDataByArray(dateArr);
	}

	/*
	 * split "dateLong" to String[6]. 0:year ,1:month , 2:day , 3:hour , 4:minute ,5:second .
	 */
	public static String[] getArrayWholeData(long dateLong) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String strDate = null;

		try {
			strDate = format.format(dateLong);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String[] arrDate = strDate.split("-");
		return arrDate;
	}

	/**
	 * get day by a timestamp
	 * @param dataLong
	 * @return
	 */
	public static int getIntDay(long dataLong){
		String[] arr = DateFormatUtil.getArrayData(dataLong);
		if (arr.length >= 2) {
			return Integer.parseInt(arr[2]);
		}
		return -1;
	}
	public static boolean isTheMonthLastDate(long currentDateLong) {
		int currentDay = DateFormatUtil.getIntDay(currentDateLong);
		int theMonthTotalDay = DateFormatUtil.countDaysOfCurrentMounth(currentDateLong);
		if (currentDay == theMonthTotalDay) {
			return true;
		}
		return false;
	}

	/**
	 * get next month last day
	 * @param currentDateLong
	 * @return
	 */
	public static long getNextMonthLastDate(long currentDateLong) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		long theNextMonthLastDate = 0;
		String[] arr = DateFormatUtil.getArrayData(currentDateLong);
		int totalDay = DateFormatUtil.countDaysOfNextMounth(currentDateLong);
		int year = 0;
		int month = 0;
		if (arr.length >= 2) {
			year = Integer.parseInt(arr[0]);
			month = Integer.parseInt(arr[1]);
		}
		if (month == 12) {
			year += 1;
			month = 1;
		} else {
			month++;
		}
		if (year <= 0) {
			return 0;
		}
		Date date = new Date();
		String strDate = year+"-"+month+"-"+totalDay;
		
		try {
			date = format.parse(strDate);
			theNextMonthLastDate = date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return theNextMonthLastDate;
	}

	/**
	 * get last month the day
	 * @param currentDateLong
	 * @param day
	 * @return
	 */
	public static long getNextMonthTheDate(long currentDateLong, int day) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		long theNextMonthTheDate = 0;
		String[] arr = DateFormatUtil.getArrayData(currentDateLong);
		int year = 0;
		int month = 0;
		if (arr.length >= 2) {
			year = Integer.parseInt(arr[0]);
			month = Integer.parseInt(arr[1]);
		}
		if (month == 12) {
			year += 1;
			month = 1;
		} else {
			month++;
		}
		if (year <= 0) {
			return 0;
		}
		Date date = new Date();
		String strDate = year+"-"+month+"-"+day;
		
		try {
			date = format.parse(strDate);
			theNextMonthTheDate = date.getTime();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return theNextMonthTheDate;
	}

}
