package com.bravesoft.riumachi.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import android.app.LauncherActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.cardemulation.OffHostApduService;
import android.util.Log;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.appliction.App;
import com.bravesoft.riumachi.bean.CarlendarBean;
import com.bravesoft.riumachi.bean.DrugBean;
import com.bravesoft.riumachi.bean.MyKarteBean;
import com.bravesoft.riumachi.bean.ScheduleBean;
import com.bravesoft.riumachi.bean.SymptomMemoBean;
import com.bravesoft.riumachi.constant.CustomLoopType;
import com.bravesoft.riumachi.constant.MedicineType;
import com.bravesoft.riumachi.constant.NotificationType;
import com.bravesoft.riumachi.constant.ScheduleAllDay;
import com.bravesoft.riumachi.constant.ScheduleLoopType;
import com.bravesoft.riumachi.constant.ScheduleType;
import com.bravesoft.riumachi.util.DateFormatUtil;
import com.bravesoft.riumachi.util.MyUtils;

public class DBOperator {

	/**
	 * Query the karte table by id ,and return there are records match this id has exists or not.
	 * @param db The databese entity
	 * @param id  
	 * @return If the karte record exists return true else return false.
	 */
	public static boolean queryMyKarteExsitById(SQLiteDatabase db, String id) {

		String sql = "select * from " + DBConfig.MY_KARTE_TABLE
				+ " where id = '" + id + "'";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToNext()) {
			cursor.close();
			return true;
		}
		cursor.close();
		return false;
	}

	/**
	 * Query the karte table by id ,and return the karte entity.
	 * @param db The databese entity
	 * @param id
	 * @return Karte entity
	 */
	public static MyKarteBean queryMyKarteById(SQLiteDatabase db, String id) {

		String sql = "select * from " + DBConfig.MY_KARTE_TABLE + " where id='"
				+ id + "'";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			MyKarteBean karteBean = new MyKarteBean();
			karteBean.setDate(cursor.getString(cursor.getColumnIndex("id")));
			karteBean
					.setDate(cursor.getString(cursor.getColumnIndex("dateNo")));
			karteBean.setContent(cursor.getString(cursor
					.getColumnIndex("content")));
			cursor.close();
			return karteBean;
		}
		cursor.close();
		return null;
	}

	/**
	 * Query the karte table by id ,and return the karte entity.
	 * @param db The databese entity
	 * @return All karte records order by date desc
	 */
	public static List<MyKarteBean> queryMyKarteListOrderByDate(
			SQLiteDatabase db) {

		List<MyKarteBean> data = new ArrayList<MyKarteBean>();
		String sql = "select * from " + DBConfig.MY_KARTE_TABLE
				+ " order by dateNo desc";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			MyKarteBean karteBean = new MyKarteBean();
			karteBean.setId(Integer.parseInt(cursor.getString(cursor
					.getColumnIndex("id"))));
			karteBean
					.setDate(cursor.getString(cursor.getColumnIndex("dateNo")));
			karteBean.setContent(cursor.getString(cursor
					.getColumnIndex("content")));
			data.add(karteBean);
		}
		cursor.close();
		return data;
	}

	/**
	 * Insert a karte record in database.
	 * @param db The databese entity
	 * @param myKarteBean The karte entity
	 * @return If the insert operation success return true else return false
	 */
	public static boolean insertMyKarte(SQLiteDatabase db,
			MyKarteBean myKarteBean) {

		ContentValues values = new ContentValues();
		values.put("dateNo", myKarteBean.getDate());
		values.put("content", myKarteBean.getContent());
		db.insert(DBConfig.MY_KARTE_TABLE, null, values);
		return true;
	}

	/**
	 * Update a karte record in database.
	 * @param db The databese entity
	 * @param myKarteBean The karte entity
	 * @return If the update operation success return true else return false
	 */
	public static boolean updateMyKarte(SQLiteDatabase db,
			MyKarteBean myKarteBean) {
		ContentValues values = new ContentValues();
		values.put("dateNo", myKarteBean.getDate());
		values.put("content", myKarteBean.getContent());
		db.update(DBConfig.MY_KARTE_TABLE, values, "id=?",
				new String[] { myKarteBean.getId() + "" });
		return true;
	}

	/**
	 * Delete the karte record by id.
	 * @param db The databese entity
	 * @param id
	 */
	public static void deleteMyKarteById(SQLiteDatabase db, String id) {

		db.delete(DBConfig.MY_KARTE_TABLE, "id=?", new String[] { id });
	}

	/**
	 * Query the schedule table by id ,and return there are records match this id has exists or not.
	 * @param db The databese entity
	 * @param id  
	 * @return If the schedule record exists return true else return false.
	 */
	public static boolean queryScheduleExsitById(SQLiteDatabase db, String id) {

		String sql = "select * from " + DBConfig.SCHEDULE_TABLE
				+ " where id = '" + id + "'";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToNext()) {
			return true;
		}
		return false;
	}

	/**
	 * Query the schedule table by id ,and return the schedule entity.
	 * @param db The databese entity
	 * @param id
	 * @return Schedule entity
	 */
	public static ScheduleBean queryScheduleById(SQLiteDatabase db, String id) {

		String sql = "select * from " + DBConfig.SCHEDULE_TABLE + " where id='"
				+ id + "'";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			ScheduleBean scheduleBean = new ScheduleBean();
			scheduleBean.setId(Integer.parseInt(cursor.getString(cursor
					.getColumnIndex("id"))));
			scheduleBean.setParent(cursor.getString(cursor.getColumnIndex("parent")));
			scheduleBean
					.setType(cursor.getString(cursor.getColumnIndex("type")));
			scheduleBean.setTitle(cursor.getString(cursor
					.getColumnIndex("title")));
			scheduleBean.setShunichi(cursor.getString(cursor
					.getColumnIndex("shunichi")));
			scheduleBean.setStarttime(cursor.getString(cursor
					.getColumnIndex("starttime")));
			scheduleBean.setEndtime(cursor.getString(cursor
					.getColumnIndex("endtime")));
			scheduleBean.setKurikaeshi(cursor.getString(cursor
					.getColumnIndex("kurikaeshi")));
			scheduleBean.setTuchi(cursor.getString(cursor
					.getColumnIndex("tuchi")));
			scheduleBean.setMedicalType(cursor.getString(cursor
					.getColumnIndex("medicalType")));
			scheduleBean.setSchedule_start_time(cursor.getString(cursor
					.getColumnIndex("schedule_start_time")));
			scheduleBean.setSchedule_end_time(cursor.getString(cursor
					.getColumnIndex("schedule_end_time")));
			scheduleBean.setCustom_loop_type(cursor.getString(cursor
					.getColumnIndex("custom_loop_type")));
			scheduleBean.setCustom_loop_value(cursor.getString(cursor
					.getColumnIndex("custom_loop_value")));
			cursor.close();
			return scheduleBean;
		}
		cursor.close();
		return null;
	}

	/**
	 * Insert a schedule record in database.
	 * @param db The databese entity
	 * @param scheduleBean The schedule entity
	 * @return If the insert operation success return true else return false.
	 */
	public static boolean insertSchedule(SQLiteDatabase db,
			ScheduleBean scheduleBean) {

		ContentValues values = new ContentValues();
		values.put("type", scheduleBean.getType());
		values.put("parent", scheduleBean.getParent());
		values.put("title", scheduleBean.getTitle());
		values.put("shunichi", scheduleBean.getShunichi());
		values.put("starttime", scheduleBean.getStarttime());
		values.put("medicalType", scheduleBean.getMedicalType());
		values.put("endtime", scheduleBean.getEndtime());
		values.put("kurikaeshi", scheduleBean.getKurikaeshi());
		values.put("tuchi", scheduleBean.getTuchi());
		values.put("custom_loop_type", scheduleBean.getCustom_loop_type());
		values.put("custom_loop_value", scheduleBean.getCustom_loop_value());
		values.put("schedule_start_time", scheduleBean.getSchedule_start_time());
		values.put("schedule_end_time", scheduleBean.getSchedule_end_time());
		long insertId = db.insert(DBConfig.SCHEDULE_TABLE, null, values);
		if (insertId < 0) {
			return false;
		}
		// System.out.println("insertId"+insertId);
		return true;
	}

	/**
	 * Query the schedule table by date ,and return the day schedule list.
	 * @param db The databese entity
	 * @param currentDate A timestamp,a day you wanted to search.
	 * @return All schedule start date in the day, the order is 'antibiotic schedule' to 'biologicals schedule'
	 * to 'other medicine schedule' to 'see to the doctor schedule' to 'normal schedule' and order by date desc in the same group.
	 * (tip: If the application mode is 'see to the doctor mode',all the normal schedule will be ignore. )
	 * (tip: If the day is the month's last day and there are loop schedule loop by month and the schedule start date exceed 
	 * the month's last day,the schedule belongs the day's schedule.)
	 */
	public static List<ScheduleBean> queryScheduleListByDateOrderByDate(
			SQLiteDatabase db, String currentDate) {

		if (MyUtils.isNull(currentDate)) {
			return null;
		}
		boolean isSeeToDocterMode = false;//  
		if (App.getInstance().getAppMode() == App.SEE_THE_DOCTER_MODE) {
			isSeeToDocterMode = true;
		}
		
		List<ScheduleBean> normalDatas = new ArrayList<ScheduleBean>();
		List<ScheduleBean> seeToDocterDatas = new ArrayList<ScheduleBean>();
		List<ScheduleBean> antibioticDatas = new ArrayList<ScheduleBean>();
		List<ScheduleBean> biologcalAgentDatas = new ArrayList<ScheduleBean>();
		List<ScheduleBean> otherMedicineDatas = new ArrayList<ScheduleBean>();
		List<ScheduleBean> data = new ArrayList<ScheduleBean>();
		String sql = "select * from " + DBConfig.SCHEDULE_TABLE
				+ " order by type desc , starttime asc ,medicalType asc";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			boolean match = false;
			String scheduleType = cursor.getString(cursor
					.getColumnIndex("type"));
			if (isSeeToDocterMode
					&& scheduleType.equals(ScheduleType.NORMAL_SCHEDULE)) {//  
				continue;
			}
			String loopType = cursor.getString(cursor
					.getColumnIndex("kurikaeshi"));
			String scheduleStartDate = cursor.getString(cursor
					.getColumnIndex("schedule_start_time"));
			String scheduleEndDate = cursor.getString(cursor
					.getColumnIndex("schedule_end_time"));
			
			if (!MyUtils.isNull(scheduleStartDate)) {
				long currentDateLong = Long.parseLong(DateFormatUtil
						.geDataStringZeroClick(Long.parseLong(currentDate)));
				long startDateLong = Long.parseLong(DateFormatUtil
						.geDataStringZeroClick(Long
								.parseLong(scheduleStartDate)));
				if (MyUtils.isNull(loopType)
						|| loopType.equals(ScheduleLoopType.NO_LOOP)
						|| loopType.equals(ScheduleLoopType.NO_RECORD)) {
					if (currentDateLong == startDateLong) {
						match = true;
					}
				} else if (!MyUtils.isNull(loopType)) {
					if (currentDateLong >= startDateLong) {
						if (MyUtils.isNull(scheduleEndDate)
								|| Long.parseLong(scheduleEndDate) >= currentDateLong) {
							if (loopType.equals(ScheduleLoopType.EVERY_DAY)) {
								if ((double) (currentDateLong - startDateLong)
										% DateFormatUtil.ONE_DAY == 0) {
									match = true;
								}
							} else if (loopType
									.equals(ScheduleLoopType.EVERY_WEEK)) {
								if ((double) (currentDateLong - startDateLong)
										% DateFormatUtil.ONE_WEEK == 0) {
									match = true;
								}
							} else if (loopType
									.equals(ScheduleLoopType.EVERY_TWO_WEEK)) {
								if ((double) (currentDateLong - startDateLong)
										% DateFormatUtil.TWO_WEEK == 0) {
									match = true;
								}
							} else if (loopType
									.equals(ScheduleLoopType.EVERY_MONTH)) {
								int startDay = -1;
								String parentId = cursor.getString(cursor.getColumnIndex("parent"));
								if (MyUtils.isNull(parentId)) {
									startDay = DateFormatUtil.getIntDay(startDateLong);
								}else{
									ScheduleBean parent = DBOperator.queryScheduleById(db, parentId);
									if (parent != null && !MyUtils.isNull(parent.getSchedule_start_time())) {
										startDay =  DateFormatUtil.getIntDay(Long.parseLong(parent.getSchedule_start_time()));
									}
								}
								
								int currentDay = DateFormatUtil.getIntDay(currentDateLong);
								//System.out.println("currentDay=="+currentDay);
								//System.out.println("startDay=="+startDay);
								//System.out.println("islastdate=="+DateFormatUtil.isTheMonthLastDate(currentDay));
								if (startDay <= 28) {
									if (startDay == currentDay) {
										match = true;
									}
								}else{
									if (DateFormatUtil.isTheMonthLastDate(currentDateLong)) {
										if (startDay >= currentDay) {
											match = true;
										}
									}else{
										if (startDay == currentDay) {
											match = true;
										}
									}
								}
							}else if (loopType
									.equals(ScheduleLoopType.CUSTOM_LOOP)) {
								String typeStr = scheduleEndDate = cursor.getString(cursor
										.getColumnIndex("custom_loop_type"));
								String valueStr = scheduleEndDate = cursor.getString(cursor
										.getColumnIndex("custom_loop_value"));
								long per = 0;
								int value = 0;
								if (!MyUtils.isNull(typeStr)&&!MyUtils.isNull(valueStr)) {
									if (typeStr.equals(CustomLoopType.CUSTOM_LOOP_DAY)) {
										per = DateFormatUtil.ONE_DAY;
									}else if(typeStr.equals(CustomLoopType.CUSTOM_LOOP_WEEK)){
										per = DateFormatUtil.ONE_WEEK;
									}
									value = Integer.parseInt(valueStr);
									if ((double) (currentDateLong - startDateLong)
											% (per*value) == 0) {
										match = true;
									}
								}
								
							}
						}
					}
				}
				if (match) {
					ScheduleBean scheduleBean = new ScheduleBean();
					String startTime = cursor.getString(cursor.getColumnIndex("starttime"));
					String sort = DateFormatUtil.getLongDateBySplicing(currentDateLong, Long.parseLong(startTime))+"";
					String type =  cursor.getString(cursor.getColumnIndex("type"));
					String medicineType =  cursor.getString(cursor.getColumnIndex("medicalType"));
					scheduleBean.setSort(sort);
					scheduleBean.setId(Integer.parseInt(cursor.getString(cursor
							.getColumnIndex("id"))));
					scheduleBean.setParent(cursor.getString(cursor
							.getColumnIndex("parent")));
					scheduleBean.setType(cursor.getString(cursor
							.getColumnIndex("type")));
					scheduleBean.setTitle(cursor.getString(cursor
							.getColumnIndex("title")));
					scheduleBean.setShunichi(cursor.getString(cursor
							.getColumnIndex("shunichi")));
					scheduleBean.setStarttime(cursor.getString(cursor
							.getColumnIndex("starttime")));
					scheduleBean.setEndtime(cursor.getString(cursor
							.getColumnIndex("endtime")));
					scheduleBean.setKurikaeshi(cursor.getString(cursor
							.getColumnIndex("kurikaeshi")));
					scheduleBean.setTuchi(cursor.getString(cursor
							.getColumnIndex("tuchi")));
					scheduleBean.setMedicalType(cursor.getString(cursor
							.getColumnIndex("medicalType")));
					scheduleBean.setSchedule_start_time(cursor.getString(cursor
							.getColumnIndex("schedule_start_time")));
					scheduleBean.setSchedule_end_time(cursor.getString(cursor
							.getColumnIndex("schedule_end_time")));
					scheduleBean.setCustom_loop_type(cursor.getString(cursor
							.getColumnIndex("custom_loop_type")));
					scheduleBean.setCustom_loop_value(cursor.getString(cursor
							.getColumnIndex("custom_loop_value")));
					if (type.equals(ScheduleType.NORMAL_SCHEDULE)) {
						normalDatas.add(scheduleBean);
					} else if (type.equals(ScheduleType.SEE_THE_DOCTER_SCHEDULE)) {
						seeToDocterDatas.add(scheduleBean);
					} else if (type.equals(ScheduleType.MEDICINE_SCHEDULE)) {
						if (medicineType.equals(MedicineType.ANTIBIOTIC)) {
							antibioticDatas.add(scheduleBean);
						}else if (medicineType.equals(MedicineType.BIOLOGCAL_AGENT)) {
							biologcalAgentDatas.add(scheduleBean);
						}if (medicineType.equals(MedicineType.OTHER_MEDICINE)) {
							otherMedicineDatas.add(scheduleBean);
						}
					}
				}
			}
		}
		cursor.close();
		
		if (antibioticDatas != null && antibioticDatas.size() > 0) {
			Collections.sort(antibioticDatas);
			data.addAll(antibioticDatas);
		}
		if (biologcalAgentDatas != null && biologcalAgentDatas.size() > 0) {
			Collections.sort(biologcalAgentDatas);
			data.addAll(biologcalAgentDatas);
		}
		if (otherMedicineDatas != null && otherMedicineDatas.size() > 0) {
			Collections.sort(otherMedicineDatas);
			data.addAll(otherMedicineDatas);
		}
		if (seeToDocterDatas != null && seeToDocterDatas.size() > 0) {
			Collections.sort(seeToDocterDatas);
			data.addAll(seeToDocterDatas);
		}
		if (normalDatas != null && normalDatas.size() > 0) {
			Collections.sort(normalDatas);
			data.addAll(normalDatas);
		}
		return data;
	}
	
	/**
	 * Query the schedule table by date ,and return the month schedule list.
	 * @param db The databese entity
	 * @param currentDate A timestamp,a month you wanted to search.
	 * @return All schedule start date in the month,order by date desc.
	 * (tip: If the application mode is 'see to the doctor mode',all the normal schedule will be ignore. )
	 * (tip: If there are loop schedule loop by month and the day exceed the month's last day,
	 * the schedule will be shown in the month's last day.)
	 */
	public static List<CarlendarBean> queryScheduleListGetCalendar(
			SQLiteDatabase db, String currentDate) {

		if (MyUtils.isNull(currentDate)) {
			return null;
		}

		boolean isSeeToDocterMode = false;//  
		if (App.getInstance().getAppMode() == App.SEE_THE_DOCTER_MODE) {
			isSeeToDocterMode = true;
		}
		long currentDateLong = DateFormatUtil.geDataLongZeroClick(Long
				.parseLong(currentDate));
		long firstDateLong = DateFormatUtil
				.getDataLongFirstDayZeroClick(currentDateLong);
		long lastDateLong = DateFormatUtil
				.getDataLongLastDayZeroClick(currentDateLong);
		int totalDay = DateFormatUtil.countDaysOfCurrentMounth(currentDateLong);

		List<CarlendarBean> datas = new ArrayList<CarlendarBean>();
		for (int i = 0; i < totalDay; i++) {
			CarlendarBean carlendarBean = new CarlendarBean();
			carlendarBean.setDate(i + 1 + "");
			carlendarBean.setDateLong(DateFormatUtil.ONE_DAY * i
					+ firstDateLong);
			datas.add(carlendarBean);
		}

		String sql = "select * from " + DBConfig.SCHEDULE_TABLE
				+ " order by schedule_start_time desc";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			String scheduleType = cursor.getString(cursor
					.getColumnIndex("type"));
			if (isSeeToDocterMode
					&& scheduleType.equals(ScheduleType.NORMAL_SCHEDULE)) {
				continue;
			}
			String medicineType = cursor.getString(cursor
					.getColumnIndex("medicalType"));
			String loopType = cursor.getString(cursor
					.getColumnIndex("kurikaeshi"));
			String scheduleStartDate = cursor.getString(cursor
					.getColumnIndex("schedule_start_time"));
			String scheduleEndDate = cursor.getString(cursor
					.getColumnIndex("schedule_end_time"));
			String scheduleId = cursor.getString(cursor
					.getColumnIndex("medicalType"));
			long scheduleStartDateLong;
			if (!MyUtils.isNull(scheduleStartDate)) {
				scheduleStartDateLong = DateFormatUtil.geDataLongZeroClick(Long
						.parseLong(scheduleStartDate)); // 
			} else {
				break;
			}

			for (int i = 0; i < datas.size(); i++) {
				
				boolean match = false;
				long carlendarDateLong = datas.get(i).getDateLong(); // 
				if (MyUtils.isNull(loopType)
						|| loopType.equals(ScheduleLoopType.NO_LOOP)
						|| loopType.equals(ScheduleLoopType.NO_RECORD)) {
					if (scheduleStartDateLong == carlendarDateLong) {
						match = true;
					}
				} else if (!MyUtils.isNull(loopType)
						&& MyUtils.isNull(scheduleEndDate)
						|| Long.parseLong(scheduleEndDate) >= carlendarDateLong) {
					if (carlendarDateLong == scheduleStartDateLong) {
						match = true;
					} else if (carlendarDateLong > scheduleStartDateLong) {
						if (loopType.equals(ScheduleLoopType.EVERY_DAY)) {
							if ((double) (scheduleStartDateLong - carlendarDateLong)
									% DateFormatUtil.ONE_DAY == 0) {
								match = true;
							}
						} else if (loopType.equals(ScheduleLoopType.EVERY_WEEK)) {
							if ((double) (scheduleStartDateLong - carlendarDateLong)
									% DateFormatUtil.ONE_WEEK == 0) {
								match = true;
							}
						} else if (loopType
								.equals(ScheduleLoopType.EVERY_TWO_WEEK)) {
							if ((double) (scheduleStartDateLong - carlendarDateLong)
									% DateFormatUtil.TWO_WEEK == 0) {
								match = true;
							}
						} else if (loopType
								.equals(ScheduleLoopType.EVERY_MONTH)) {
							int startDay = -1;
							String parentId = cursor.getString(cursor.getColumnIndex("parent"));
							if (MyUtils.isNull(parentId)) {
								startDay = DateFormatUtil.getIntDay(scheduleStartDateLong);
							}else{
								ScheduleBean parent = DBOperator.queryScheduleById(db, parentId);
								if (parent != null && !MyUtils.isNull(parent.getSchedule_start_time())) {
									startDay =  DateFormatUtil.getIntDay(Long.parseLong(parent.getSchedule_start_time()));
								}
							}
							
							int currentDay = DateFormatUtil.getIntDay(carlendarDateLong);
							if (startDay <= 28) {
								if (startDay == currentDay) {
									match = true;
								}
							}else{
								if (DateFormatUtil.isTheMonthLastDate(carlendarDateLong)) {
									if (startDay >= currentDay) {
										match = true;
									}
								}else{
									if (startDay == currentDay) {
										match = true;
									}
								}
							}
						}else if (loopType
								.equals(ScheduleLoopType.CUSTOM_LOOP)) {
							String typeStr = cursor.getString(cursor
									.getColumnIndex("custom_loop_type"));
							String valueStr = cursor.getString(cursor
									.getColumnIndex("custom_loop_value"));
							long per = 0;
							int value = 0;
							if (!MyUtils.isNull(typeStr)&&!MyUtils.isNull(valueStr)) {
								if (typeStr.equals(CustomLoopType.CUSTOM_LOOP_DAY)) {
									per = DateFormatUtil.ONE_DAY;
								}else if(typeStr.equals(CustomLoopType.CUSTOM_LOOP_WEEK)){
									per = DateFormatUtil.ONE_WEEK;
								}
								value = Integer.parseInt(valueStr);
								if ((double) (scheduleStartDateLong - carlendarDateLong)
										% (per*value) == 0) {
									match = true;
								}
							}
							
						}
					}
				}
				if (match) {
					if (!MyUtils.isNull(scheduleType)) {
						if (scheduleType.equals(ScheduleType.NORMAL_SCHEDULE)
								&& !datas.get(i).isHasNormalSchedule()) {
							datas.get(i).setHasNormalSchedule(true);
						} else if (scheduleType
								.equals(ScheduleType.SEE_THE_DOCTER_SCHEDULE)
								&& !datas.get(i).isHasSeeTheDocterSchedule()) {
							datas.get(i).setHasSeeTheDocterSchedule(true);
						} else if (scheduleType
								.equals(ScheduleType.MEDICINE_SCHEDULE)
								&& !MyUtils.isNull(medicineType)) {
							if (medicineType.equals(MedicineType.ANTIBIOTIC)
									&& !datas.get(i).isHasAntibioticSchedule()) {
								datas.get(i).setHasAntibioticSchedule(true);
							} else if (medicineType
									.equals(MedicineType.BIOLOGCAL_AGENT)
									&& !datas.get(i)
											.isHasBiologicalAgentSchedule()) {
								datas.get(i)
										.setHasBiologicalAgentSchedule(true);
							} else if (medicineType
									.equals(MedicineType.OTHER_MEDICINE)
									&& !datas.get(i)
											.isHasOtherMedicineSchedule()) {
								datas.get(i).setHasOtherMedicineSchedule(true);
							}
						}
					}
				}
			}

		}
		cursor.close();
		return datas;
	}
	
	/**
	 * Query the schedule table by current time ,and return the schedule notify time belongs today and 
	 * start time more than current time,order by desc.
	 * @param db The databese entity
	 * @return All schedule should notify in today,and start time more than current time.
	 */
	public static List<ScheduleBean> queryNotificationScheduleListOrderByDate(SQLiteDatabase db) {

		List<ScheduleBean> threeDayScheduleList = new ArrayList<ScheduleBean>();
		List<ScheduleBean> notificationList = new ArrayList<ScheduleBean>();
		String currentDate = System.currentTimeMillis()+"";// 
		long currentDateLong = Long.parseLong(DateFormatUtil
				.geDataStringZeroClick(Long.parseLong(currentDate)));// 
		for (int i = 0; i < 8 ; i++) {// 
			if (i >= 3 && i <= 6) {
				continue;
			}
			long theDate = currentDateLong + (i*DateFormatUtil.ONE_DAY);
			List<ScheduleBean> dailyEventList = queryScheduleListByDateOrderByDate(db, theDate +"");
			if (dailyEventList != null && dailyEventList.size() > 0) {
				for (ScheduleBean schedule : dailyEventList) {
					if (!MyUtils.isNull(schedule.getStarttime())) {
						String time = DateFormatUtil.getStringTime(Long.parseLong(schedule.getStarttime()));
						long dateHadSplicing = 0;
						if (schedule.getShunichi() == null || schedule.getShunichi().equals(ScheduleAllDay.OFF)) {
							dateHadSplicing = DateFormatUtil.getLongDateBySplicingAll(theDate, time);
						}else{
							dateHadSplicing = DateFormatUtil.getLongDateBySplicingAll(theDate, "09:00:00");
						}
						schedule.setSchedule_start_time(dateHadSplicing+"");
						threeDayScheduleList.add(schedule);
					}
				}
			}
		}
		
		
		if(threeDayScheduleList == null || threeDayScheduleList.size() <= 0 ){
			return null;
		}
		
		for (ScheduleBean scheduleBean : threeDayScheduleList) {
			boolean match = false;
			String scheduleStartDate = scheduleBean.getSchedule_start_time();
			if (MyUtils.isNull(scheduleStartDate)) {
				continue;
			}
			String notificationType  = scheduleBean.getTuchi();
			if (notificationType == null || 
					notificationType.equals(NotificationType.NO_RECORD) ||
					notificationType.equals(NotificationType.NO_NOTIFICATION)) {
					continue;
			}
			long distanceValue = 0;
			System.out.println("==="+notificationType);
			if (notificationType.equals(NotificationType.FIVE_MINUTE) &&
					(scheduleBean.getShunichi() == null || scheduleBean.getShunichi().equals(ScheduleAllDay.OFF))) {
				distanceValue = DateFormatUtil.FIVE_MINUTE;
			}else if (notificationType.equals(NotificationType.FIFTEEN_MINUTE) &&
					(scheduleBean.getShunichi() == null || scheduleBean.getShunichi().equals(ScheduleAllDay.OFF))) {
				distanceValue = DateFormatUtil.FIFTEEN_MINUTE;
			}else if (notificationType.equals(NotificationType.THIRTY_MINUTE) &&
					(scheduleBean.getShunichi() == null || scheduleBean.getShunichi().equals(ScheduleAllDay.OFF))) {
				distanceValue = DateFormatUtil.THIRTY_MINUTE;
			}else if (notificationType.equals(NotificationType.ONE_HOUR) &&
					(scheduleBean.getShunichi() == null || scheduleBean.getShunichi().equals(ScheduleAllDay.OFF))) {
				distanceValue = DateFormatUtil.ONE_HOUR;
			}else if (notificationType.equals(NotificationType.TWO_HOUR) &&
					(scheduleBean.getShunichi() == null || scheduleBean.getShunichi().equals(ScheduleAllDay.OFF))) {
				distanceValue = DateFormatUtil.TWO_HOUR;
			}else if (notificationType.equals(NotificationType.ONE_DAY)) {
				distanceValue = DateFormatUtil.ONE_DAY;
			}else if (notificationType.equals(NotificationType.TWO_DAY)) {
				distanceValue = DateFormatUtil.TWO_DAY;
			}else if (notificationType.equals(NotificationType.ONE_WEEK)) {
				distanceValue = DateFormatUtil.ONE_WEEK;
			}
			long startDateLong = Long.parseLong(scheduleStartDate);
			long begin = DateFormatUtil.geDataLongZeroClick(currentDateLong);
			long end = begin + DateFormatUtil.ONE_DAY;
			if (startDateLong - distanceValue > System.currentTimeMillis() && startDateLong - distanceValue <= end) {
				match = true;
				scheduleBean.setSchedule_start_time((startDateLong-distanceValue)+"");
			}
			
			if (match) {
				notificationList.add(scheduleBean);
			}
			
		}
		
		if (notificationList != null && notificationList.size() > 0) {
			return notificationList;
		}
		
		return null;
	}

	/**
	 * Query all schedule record's order by date
	 * @param db The databese entity
	 * @return All schedule schedule record's.
	 */
	public static List<ScheduleBean> queryScheduleListOrderByDate(
			SQLiteDatabase db) {

		List<ScheduleBean> data = new ArrayList<ScheduleBean>();
		String sql = "select * from " + DBConfig.SCHEDULE_TABLE
				+ " order by schedule_start_time desc";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			ScheduleBean scheduleBean = new ScheduleBean();
			scheduleBean.setId(Integer.parseInt(cursor.getString(cursor
					.getColumnIndex("id"))));
			scheduleBean.setParent(cursor.getString(cursor.getColumnIndex("parent")));
			scheduleBean
					.setType(cursor.getString(cursor.getColumnIndex("type")));
			scheduleBean.setTitle(cursor.getString(cursor
					.getColumnIndex("title")));
			scheduleBean.setShunichi(cursor.getString(cursor
					.getColumnIndex("shunichi")));
			scheduleBean.setStarttime(cursor.getString(cursor
					.getColumnIndex("starttime")));
			scheduleBean.setEndtime(cursor.getString(cursor
					.getColumnIndex("endtime")));
			scheduleBean.setKurikaeshi(cursor.getString(cursor
					.getColumnIndex("kurikaeshi")));
			scheduleBean.setTuchi(cursor.getString(cursor
					.getColumnIndex("tuchi")));
			scheduleBean.setMedicalType(cursor.getString(cursor
					.getColumnIndex("medicalType")));
			scheduleBean.setSchedule_start_time(cursor.getString(cursor
					.getColumnIndex("schedule_start_time")));
			scheduleBean.setSchedule_end_time(cursor.getString(cursor
					.getColumnIndex("schedule_end_time")));
			scheduleBean.setCustom_loop_type(cursor.getString(cursor
					.getColumnIndex("custom_loop_type")));
			scheduleBean.setCustom_loop_value(cursor.getString(cursor
					.getColumnIndex("custom_loop_value")));
			data.add(scheduleBean);
		}
		cursor.close();
		return data;
	}

	/**
	 * Delete a schedule record (no loop) by schedule's id
	 * @param db The databese entity
	 * @param id schedule's id
	 * @return If the delete operation success return true else return false
	 */
	public static boolean deleteScheduleById(SQLiteDatabase db, String id) {

		long success = db.delete(DBConfig.SCHEDULE_TABLE, "id=?",
				new String[] { id });
		if (success > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Delete a loop schedule record(only one day) by schedule's id and the day.
	 * @param db The databese entity
	 * @param id schedule's id
	 * @param currentDate
	 * @return If the delete operation success return true else return false
	 */
	public static boolean deleteOneScheduleByIdLoop(SQLiteDatabase db,
			String id, long currentDate) {

		if (MyUtils.isNull(id)) {
			return false;
		}
		ScheduleBean scheduleBean = DBOperator.queryScheduleById(db, id);
		if (scheduleBean == null) {
			return false;
		}
		String loopType = scheduleBean.getKurikaeshi();
		if (MyUtils.isNull(loopType)
						|| loopType.equals(ScheduleLoopType.NO_LOOP)
						|| loopType.equals(ScheduleLoopType.NO_RECORD)) {
			if (deleteScheduleById(db, id)) {
				return true;
			}
			return false;
		}
		
		long currentDateLong = DateFormatUtil.geDataLongZeroClick(currentDate);
		String scheduleStartDate = scheduleBean.getSchedule_start_time(); // 
		if (!MyUtils.isNull(scheduleStartDate)) {
			long scheduleStartLong = DateFormatUtil.geDataLongZeroClick(Long.parseLong(scheduleStartDate));
			if (scheduleStartLong==currentDateLong) {// 
				long newStartTime = 0;
				if (loopType.equals(ScheduleLoopType.EVERY_DAY)) {
					newStartTime = currentDateLong + DateFormatUtil.ONE_DAY;
				} else if (loopType.equals(ScheduleLoopType.EVERY_WEEK)) {
					newStartTime = currentDateLong + DateFormatUtil.ONE_WEEK;
				} else if (loopType.equals(ScheduleLoopType.EVERY_TWO_WEEK)) {
					newStartTime = currentDateLong + DateFormatUtil.TWO_WEEK;
				} else if (loopType.equals(ScheduleLoopType.EVERY_MONTH)) {
					if (DateFormatUtil.getIntDay(scheduleStartLong) <= 28) {
						newStartTime = currentDateLong + DateFormatUtil.countDaysOfCurrentMounth(currentDateLong)*DateFormatUtil.ONE_DAY;
					}else{
						int startDay = -1;
						String parentId = scheduleBean.getParent();
						if (MyUtils.isNull(parentId)) {
							startDay = DateFormatUtil.getIntDay(scheduleStartLong);
						}else{
							if (!MyUtils.isNull(parentId)) {
								ScheduleBean parent = DBOperator.queryScheduleById(db, parentId);
								String parentStartTime = parent.getSchedule_start_time();
								if (parent != null && !MyUtils.isNull(parentStartTime)) {
									startDay =  DateFormatUtil.getIntDay(Long.parseLong(parentStartTime));
								}
							}
						}
						
						if (DateFormatUtil.countDaysOfNextMounth(currentDateLong) < startDay) {
							newStartTime = DateFormatUtil.getNextMonthLastDate(currentDateLong);
						}else{
							newStartTime = DateFormatUtil.getNextMonthTheDate(currentDateLong ,startDay);
						}
					}
				}else if (loopType.equals(ScheduleLoopType.CUSTOM_LOOP)) {
					String typeStr = scheduleBean.getCustom_loop_type();
					String valueStr  = scheduleBean.getCustom_loop_value();
					long per = 0;
					int value = 0;
					if (!MyUtils.isNull(typeStr)&&!MyUtils.isNull(valueStr)) {
						if (typeStr.equals(CustomLoopType.CUSTOM_LOOP_DAY)) {
							per = DateFormatUtil.ONE_DAY;
						}else if(typeStr.equals(CustomLoopType.CUSTOM_LOOP_WEEK)){
							per = DateFormatUtil.ONE_WEEK;
						}
						value = Integer.parseInt(valueStr);
						newStartTime = currentDateLong + per*value;
					}
					
				}
				ContentValues values = new ContentValues();
				values.put("schedule_start_time", newStartTime+"");
				long deleteIndex = db.update(DBConfig.SCHEDULE_TABLE, values, "id=?", new String[]{id});
				if (deleteIndex > 0) {
					return true;
				}
			}else{
				long endDate = currentDateLong  - DateFormatUtil.ONE_DAY;// 
				ContentValues values = new ContentValues();
				values.put("schedule_end_time", endDate+"");
				
				 
				long deleteIndex = db.update(DBConfig.SCHEDULE_TABLE, values, "id=?", new String[]{id});
				if (deleteIndex > 0) {
					long newStartTime = 0;
					if (loopType.equals(ScheduleLoopType.EVERY_DAY)) {
						newStartTime = currentDateLong + DateFormatUtil.ONE_DAY;
					} else if (loopType.equals(ScheduleLoopType.EVERY_WEEK)) {
						newStartTime = currentDateLong + DateFormatUtil.ONE_WEEK;
					} else if (loopType.equals(ScheduleLoopType.EVERY_TWO_WEEK)) {
						newStartTime = currentDateLong + DateFormatUtil.TWO_WEEK;
					} else if (loopType.equals(ScheduleLoopType.EVERY_MONTH)) {
						if (DateFormatUtil.getIntDay(scheduleStartLong) <= 28) {
							newStartTime = currentDateLong + DateFormatUtil.countDaysOfCurrentMounth(currentDateLong)*DateFormatUtil.ONE_DAY;
						}else{
							int startDay = -1;
							String parentId = scheduleBean.getParent();
							if (MyUtils.isNull(parentId)) {
								startDay = DateFormatUtil.getIntDay(scheduleStartLong);
							}else{
								if (!MyUtils.isNull(parentId)) {
									ScheduleBean parent = DBOperator.queryScheduleById(db, parentId);
									String parentStartTime = parent.getSchedule_start_time();
									if (parent != null && !MyUtils.isNull(parentStartTime)) {
										startDay =  DateFormatUtil.getIntDay(Long.parseLong(parentStartTime));
									}
								}
							}
							
							if (DateFormatUtil.countDaysOfNextMounth(currentDateLong) < startDay) {
								newStartTime = DateFormatUtil.getNextMonthLastDate(currentDateLong);
							}else{
								newStartTime = DateFormatUtil.getNextMonthTheDate(currentDateLong ,startDay);
							}
						}
					}else if (loopType.equals(ScheduleLoopType.CUSTOM_LOOP)) {
						String typeStr = scheduleBean.getCustom_loop_type();
						String valueStr  = scheduleBean.getCustom_loop_value();
						long per = 0;
						int value = 0;
						if (!MyUtils.isNull(typeStr)&&!MyUtils.isNull(valueStr)) {
							if (typeStr.equals(CustomLoopType.CUSTOM_LOOP_DAY)) {
								per = DateFormatUtil.ONE_DAY;
							}else if(typeStr.equals(CustomLoopType.CUSTOM_LOOP_WEEK)){
								per = DateFormatUtil.ONE_WEEK;
							}
							value = Integer.parseInt(valueStr);
							newStartTime = currentDateLong + per*value;
						}
						
					}
					scheduleBean.setSchedule_start_time( newStartTime+ "");//  
					if (MyUtils.isNull(scheduleBean.getParent())) { // 
						scheduleBean.setParent(id); 
					}else{ // 
						scheduleBean.setParent(scheduleBean.getParent());
					}
					 
					if (DBOperator.insertSchedule(db, scheduleBean)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Delete a loop schedule record(all schedule after today) by schedule's id and the day.
	 * @param db The databese entity
	 * @param id schedule's id
	 * @param currentDate
	 * @return If the delete operation success return true else return false
	 */
	public static boolean deleteManyScheduleByIdLoop(SQLiteDatabase db,
			String id, long currentDate) {
		if (MyUtils.isNull(id)) {
			return false;
		}
		ScheduleBean scheduleBean = DBOperator.queryScheduleById(db, id);
		if (scheduleBean == null) {
			return false;
		}
		String loopType = scheduleBean.getKurikaeshi();
		if (MyUtils.isNull(loopType)
						|| loopType.equals(ScheduleLoopType.NO_LOOP)
						|| loopType.equals(ScheduleLoopType.NO_RECORD)) {
			if (deleteScheduleById(db, id)) {
				return true;
			}
			return false;
		}
		/*
		 * If the schedule is root(parent == null), and the 'currentDate' is the schedule first day,delete all his childs and himself.
		 * Else if the schedule is root(parent == null), but the 'currentDate' isn't the schedule first day,delete all his childs and
		 * set the schedule end date('schedule_end_time') into the 'currentDate' subtract loop day.
		 * Else if the schedule is child(parent != null), and the 'currentDate' is the schedule first day,
		 * delete himself and all schedule has the same father and behind himself.
		 * Else if the schedule is child(parent != null), and the 'currentDate' isn't the schedule first day,
		 * delete all schedule has the same father and behind himself and then set the schedule end date('schedule_end_time') 
		 * into the 'currentDate' subtract loop day.
		 */
		String parent = scheduleBean.getParent();
		if (MyUtils.isNull(parent)) {
			String sql = "delete from " + DBConfig.SCHEDULE_TABLE + " where parent='" + id +"'";
			db.execSQL(sql);
			long scheduleStartTime = Long.parseLong(scheduleBean.getSchedule_start_time());
			if (scheduleStartTime == currentDate) {
				sql = "delete from " + DBConfig.SCHEDULE_TABLE + " where id='" + id +"'";
				db.execSQL(sql);
				return true;
			}
		}else{
			String sql = "select * from " + DBConfig.SCHEDULE_TABLE + " where parent='" + parent+"'";
			Cursor cursor = db.rawQuery(sql, null);
			List<Integer> childIds = new ArrayList<Integer>();
			while (cursor.moveToNext()) {
				String childStartDate = cursor.getString(cursor.getColumnIndex("schedule_start_time"));
				if (!MyUtils.isNull(childStartDate)) {
					if (DateFormatUtil.geDataLongZeroClick(Long.parseLong(childStartDate)) >
							DateFormatUtil.geDataLongZeroClick(currentDate)) {
						childIds.add(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
					}
				}
			}
			cursor.close();
			for (Integer childId : childIds) {
				DBOperator.deleteScheduleById(db, childId+"");
			}
		}
		
		long currentDateLong = DateFormatUtil.geDataLongZeroClick(currentDate);
		long endDate = 0;//  
		if (loopType.equals(ScheduleLoopType.EVERY_DAY)) {
			endDate = currentDateLong - DateFormatUtil.ONE_DAY;
		} else if (loopType.equals(ScheduleLoopType.EVERY_WEEK)) {
			endDate = currentDateLong - DateFormatUtil.ONE_WEEK;
		} else if (loopType.equals(ScheduleLoopType.EVERY_TWO_WEEK)) {
			endDate = currentDateLong - DateFormatUtil.TWO_WEEK;
		} else if (loopType.equals(ScheduleLoopType.EVERY_MONTH)) {
			endDate = currentDateLong - DateFormatUtil.countDaysOfPreMounth(currentDateLong)*DateFormatUtil.ONE_DAY;
		}else if (loopType.equals(ScheduleLoopType.CUSTOM_LOOP)) {
			String typeStr = scheduleBean.getCustom_loop_type();
			String valueStr  = scheduleBean.getCustom_loop_value();
			long per = 0;
			int value = 0;
			if (!MyUtils.isNull(typeStr)&&!MyUtils.isNull(valueStr)) {
				if (typeStr.equals(CustomLoopType.CUSTOM_LOOP_DAY)) {
					per = DateFormatUtil.ONE_DAY;
				}else if(typeStr.equals(CustomLoopType.CUSTOM_LOOP_WEEK)){
					per = DateFormatUtil.ONE_WEEK;
				}
				value = Integer.parseInt(valueStr);
				endDate = currentDateLong - per*value;
			}
			
		}
		scheduleBean.setEndtime(endDate+"");
		ContentValues values = new ContentValues();
		values.put("schedule_end_time", endDate+"");
		long updateIndex = db.update(DBConfig.SCHEDULE_TABLE, values, "id=?", new String[]{id});
		if (updateIndex > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Update Schedule (no loop).
	 * @param db The databese entity
	 * @param scheduleBean the schedule entity
	 * @return If the update operation success return true else return false
	 */
	public static boolean updateSchedule(SQLiteDatabase db,
			ScheduleBean scheduleBean) {

		if (scheduleBean.getId() < 0) {
			return false;
		}
		ContentValues values = new ContentValues();
		values.put("type", scheduleBean.getType());
		values.put("parent", scheduleBean.getParent());
		values.put("title", scheduleBean.getTitle());
		values.put("shunichi", scheduleBean.getShunichi());
		values.put("starttime", scheduleBean.getStarttime());
		values.put("medicalType", scheduleBean.getMedicalType());
		values.put("endtime", scheduleBean.getEndtime());
		values.put("kurikaeshi", scheduleBean.getKurikaeshi());
		values.put("tuchi", scheduleBean.getTuchi());
		values.put("custom_loop_type", scheduleBean.getCustom_loop_type());
		values.put("custom_loop_value", scheduleBean.getCustom_loop_value());
		values.put("schedule_start_time", scheduleBean.getSchedule_start_time());
		values.put("schedule_end_time", scheduleBean.getSchedule_end_time());
		long result = db.update(DBConfig.SCHEDULE_TABLE, values, "id=?",
				new String[] { scheduleBean.getId() + "" });
		if (result < 0) {
			return false;
		}
		return true;
	}
	
	
	/**
	 * Update Schedule (loop).
	 * @param db The databese entity
	 * @param id The schedule's id
	 * @param newScheduleBean the schedule entity
	 * @return If the update operation success return true else return false
	 */
	public static boolean updateLoopScheduleChangeOther(SQLiteDatabase db,
			String id,ScheduleBean newScheduleBean) {
		if (MyUtils.isNull(id)) {
			return false;
		}
		ScheduleBean scheduleBean = DBOperator.queryScheduleById(db, id);
		if (scheduleBean == null) {
			return false;
		}
		String oldStartDate = scheduleBean.getSchedule_start_time(); // 
		String newStartDate = newScheduleBean.getSchedule_start_time(); // 
		if (MyUtils.isNull(oldStartDate) ||MyUtils.isNull(newStartDate) ) {
			return false;
		}
		ContentValues values = new ContentValues();
		values.put("type", newScheduleBean.getType());
		values.put("title", newScheduleBean.getTitle());
		values.put("shunichi", newScheduleBean.getShunichi());
		values.put("starttime", newScheduleBean.getStarttime());
		values.put("medicalType", newScheduleBean.getMedicalType());
		values.put("endtime", newScheduleBean.getEndtime());
		values.put("tuchi", newScheduleBean.getTuchi());
		values.put("custom_loop_type", scheduleBean.getCustom_loop_type());
		values.put("custom_loop_value", scheduleBean.getCustom_loop_value());
		String parent = scheduleBean.getParent();
		
		if (MyUtils.isNull(parent) &&  oldStartDate.equals(newScheduleBean)) {
			String sql = "delete from " + DBConfig.SCHEDULE_TABLE + " where parent='" + id +"'";
			db.execSQL(sql);
			sql = "delete from " + DBConfig.SCHEDULE_TABLE + " where id ='" + id +"'";
			db.execSQL(sql);
		}
		
		insertSchedule(db, newScheduleBean);
		
		return true;
	}

	// symptommemo
	public static boolean insertSymptomMemo(SQLiteDatabase db,
			SymptomMemoBean mySymptomMemoBean) {

		ContentValues values = new ContentValues();
		values.put("memo", mySymptomMemoBean.getName());
		values.put("photoaddress", mySymptomMemoBean.getLocal_path());
		values.put("urladdress", mySymptomMemoBean.getUrl());
		if (!MyUtils.isNull( mySymptomMemoBean.getCreateTime())) {
 			values.put("dateNo", DateFormatUtil.geDataStringZeroClick(Long.parseLong( mySymptomMemoBean.getCreateTime())));
		}
		db.insert(DBConfig.SYMPTOMMEMO_TABLE, null, values);
		return true;
	}

	public static boolean updateSymptomMemo(SQLiteDatabase db,
			SymptomMemoBean mySymptomMemoBean) {
		ContentValues values = new ContentValues();
		values.put("memo", mySymptomMemoBean.getName());
		values.put("photoaddress", mySymptomMemoBean.getLocal_path());
		values.put("urladdress", mySymptomMemoBean.getUrl());
		values.put("dateNo", mySymptomMemoBean.getCreateTime());
		db.update(DBConfig.SYMPTOMMEMO_TABLE, values, "id=?",
				new String[] { mySymptomMemoBean.getId() + "" });
		return true;
	}

	public static void deleteSymptomMemoById(SQLiteDatabase db, String id) {

		db.delete(DBConfig.SYMPTOMMEMO_TABLE, "id=?", new String[] { id });
	}

	public static List<SymptomMemoBean> querySymptomMemoListOrderByDate(
			SQLiteDatabase db) {

		List<SymptomMemoBean> data = new ArrayList<SymptomMemoBean>();
		String sql = "select * from " + DBConfig.SYMPTOMMEMO_TABLE
				+ " order by dateNo desc";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			SymptomMemoBean mySymptomMemoBean = new SymptomMemoBean();
			mySymptomMemoBean.setId(Integer.parseInt(cursor.getString(cursor
					.getColumnIndex("id"))));
			mySymptomMemoBean.setName(cursor.getString(cursor
					.getColumnIndex("memo")));
			mySymptomMemoBean.setLocal_path(cursor.getString(cursor
					.getColumnIndex("photoaddress")));
			mySymptomMemoBean.setUrl(cursor.getString(cursor
					.getColumnIndex("urladdress")));
			mySymptomMemoBean.setCreateTime(cursor.getString(cursor
					.getColumnIndex("dateNo")));

			data.add(mySymptomMemoBean);
		}
		cursor.close();
		return data;
	}
	
	public static List<SymptomMemoBean> querySymptomMemoListImageDownFail(
			SQLiteDatabase db) {

		List<SymptomMemoBean> data = new ArrayList<SymptomMemoBean>();
		String sql = "select * from " + DBConfig.SYMPTOMMEMO_TABLE+
				" where photoaddress='" + "null" + "'"+"and"+
				" urladdress is not null"
				+ " order by dateNo desc";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			SymptomMemoBean mySymptomMemoBean = new SymptomMemoBean();
			mySymptomMemoBean.setId(Integer.parseInt(cursor.getString(cursor
					.getColumnIndex("id"))));
			mySymptomMemoBean.setName(cursor.getString(cursor
					.getColumnIndex("memo")));
			mySymptomMemoBean.setLocal_path(cursor.getString(cursor
					.getColumnIndex("photoaddress")));
			mySymptomMemoBean.setUrl(cursor.getString(cursor
					.getColumnIndex("urladdress")));
			mySymptomMemoBean.setCreateTime(cursor.getString(cursor
					.getColumnIndex("dateNo")));

			data.add(mySymptomMemoBean);
		}
		cursor.close();
		return data;
	}
	
	public static boolean clearSymptomMemoLocalPathDate(
			SQLiteDatabase db) {

		List<SymptomMemoBean> data = new ArrayList<SymptomMemoBean>();
		String sql = "select * from " + DBConfig.SYMPTOMMEMO_TABLE
				+ " order by dateNo desc";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			
			ContentValues values = new ContentValues();
			values.put("memo", cursor.getString(cursor
					.getColumnIndex("memo")));
			values.put("photoaddress","null");
			values.put("urladdress", cursor.getString(cursor
					.getColumnIndex("urladdress")));
			values.put("dateNo", cursor.getString(cursor
					.getColumnIndex("dateNo")));
			db.update(DBConfig.SYMPTOMMEMO_TABLE, values, "id=?",
					new String[] { Integer.parseInt(cursor.getString(cursor
							.getColumnIndex("id"))) + "" });
			
			
		}
		cursor.close();
		return true;
	}
	
	public static boolean clearSymptomMemoUrlPathDate(
			SQLiteDatabase db) {

		List<SymptomMemoBean> data = new ArrayList<SymptomMemoBean>();
		String sql = "select * from " + DBConfig.SYMPTOMMEMO_TABLE
				+ " order by dateNo desc";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			
			ContentValues values = new ContentValues();
			values.put("memo", cursor.getString(cursor
					.getColumnIndex("memo")));
			values.put("photoaddress",cursor.getString(cursor
					.getColumnIndex("photoaddress")));
			values.put("urladdress","");
			values.put("dateNo", cursor.getString(cursor
					.getColumnIndex("dateNo")));
			db.update(DBConfig.SYMPTOMMEMO_TABLE, values, "id=?",
					new String[] { Integer.parseInt(cursor.getString(cursor
							.getColumnIndex("id"))) + "" });
			
			
		}
		cursor.close();
		return true;
	}

	public static SymptomMemoBean querySymptomMemoById(SQLiteDatabase db,
			String id) {

		String sql = "select * from " + DBConfig.SYMPTOMMEMO_TABLE
				+ " where id='" + id + "'";
		Cursor cursor = db.rawQuery(sql, null);

		if (cursor.moveToFirst()) {
			SymptomMemoBean mySymptomMemoBean = new SymptomMemoBean();
			mySymptomMemoBean.setId(Integer.parseInt(cursor.getString(cursor
					.getColumnIndex("id"))));
			mySymptomMemoBean.setName(cursor.getString(cursor
					.getColumnIndex("memo")));
			mySymptomMemoBean.setLocal_path(cursor.getString(cursor
					.getColumnIndex("photoaddress")));
			mySymptomMemoBean.setUrl(cursor.getString(cursor
					.getColumnIndex("urladdress")));
			mySymptomMemoBean.setCreateTime(cursor.getString(cursor
					.getColumnIndex("dateNo")));
			cursor.close();
			return mySymptomMemoBean;
		}
		cursor.close();
		return null;
	}
	
	

	// By timestamp fuzzy queries on the database
	public static SymptomMemoBean querySymptomMemoBytime(SQLiteDatabase db,
			String time) {

		List<SymptomMemoBean> data = new ArrayList<SymptomMemoBean>();
		String sql = "select * from " + DBConfig.SYMPTOMMEMO_TABLE
				 + " order by dateNo desc";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			long savetime = Long.parseLong(cursor.getString(cursor
					.getColumnIndex("dateNo")));
			
			if ((DateFormatUtil.geDataLongZeroClick(savetime) + "").equals(time)) {
				SymptomMemoBean mySymptomMemoBean = new SymptomMemoBean();
				mySymptomMemoBean.setId(Integer.parseInt(cursor
						.getString(cursor.getColumnIndex("id"))));
				mySymptomMemoBean.setName(cursor.getString(cursor
						.getColumnIndex("memo")));
				mySymptomMemoBean.setLocal_path(cursor.getString(cursor
						.getColumnIndex("photoaddress")));
				mySymptomMemoBean.setUrl(cursor.getString(cursor
						.getColumnIndex("urladdress")));
				mySymptomMemoBean.setCreateTime(cursor.getString(cursor
						.getColumnIndex("dateNo")));
				cursor.close();

				return mySymptomMemoBean;
			}
		}
		cursor.close();
		return null;
	}

	/*
	 * Insert Drugs
	 */
	public static int insertDrugData(SQLiteDatabase db, String drug_name) {
		int FLAG = 2;
		ContentValues values = new ContentValues();
		String name = drug_name.replace(" ", "");
		if (name.length()==0) {
			
		}else {
			values.put("name", drug_name);
			values.put("type", "3");
			if (!drug_name.equals("")) {
				Cursor cursor = db.rawQuery("select*from "
						+ DBConfig.MEDICINE_TABLE + " where name=?",
						new String[] { drug_name });
				if (cursor.getCount() > 0) {
					 
				} else {
					long result = db.insert(DBConfig.MEDICINE_TABLE, null, values);
					if (result > 0) {
						 
						FLAG = 1;
					} else {
						 
						FLAG = 2;
					}
				}
			} else {
				 
			}
		}
		return FLAG;
	}

	/*
	 * Discover all custom Drugs
	 */
	public static ArrayList<DrugBean> selectDrug(SQLiteDatabase db) {
		Cursor cursor = db.query(DBConfig.MEDICINE_TABLE, new String[] { "id",
				"type", "name" }, null, null, null, null, null);
		ArrayList<DrugBean> drugBeans = new ArrayList<DrugBean>();
		if (cursor != null) {
			while (cursor.moveToNext()) {

				DrugBean bean = new DrugBean();
				bean.setDrug_name(cursor.getString(cursor
						.getColumnIndex("name")));
				bean.setIsCheck(1);
				bean.setId(cursor.getInt(cursor.getColumnIndex("id")));
				Log.d("ID", cursor.getInt(cursor.getColumnIndex("id")) + "");
				drugBeans.add(bean);
			}
		}
		return drugBeans;

	}

	/*
	 *Remove custom Drugs
	 */
	public static void deleteDrug(SQLiteDatabase db, String drug_name) {
		String sql = "delete from " + DBConfig.MEDICINE_TABLE + " where name=?";
		db.execSQL(sql, new String[] { drug_name });
	}

}
