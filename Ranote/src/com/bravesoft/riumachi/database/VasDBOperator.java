package com.bravesoft.riumachi.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bravesoft.riumachi.bean.HaqVasBean;
import com.bravesoft.riumachi.bean.MyKarteBean;
import com.bravesoft.riumachi.bean.ScheduleBean;
import com.bravesoft.riumachi.bean.VasBean;
import com.bravesoft.riumachi.util.DateFormatUtil;
import com.bravesoft.riumachi.util.MyUtils;

public class VasDBOperator {

 
	 
 
	/**
	 * Query vasBean list
	 */
	public static List<HaqVasBean> queryVasListOrderByDate(
			SQLiteDatabase db) {
		List<HaqVasBean> data = new ArrayList<HaqVasBean>();
		String sql = "select * from " + DBConfig.VAS_TABLE 
 				+ " order by dateNo desc";
		Cursor cursor = db.rawQuery(sql, null);
		while(cursor.moveToNext()) {
			HaqVasBean haqvasBean = new HaqVasBean();
			haqvasBean.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
			haqvasBean.setDateNo(cursor.getString(cursor.getColumnIndex("dateNo")));
			
			haqvasBean.setCount(cursor.getString(cursor
					.getColumnIndex("count")));
			
			haqvasBean.setType(cursor.getString(cursor
					.getColumnIndex("type")));
			data.add(haqvasBean);
		}
		cursor.close();
		
		
		return data;
	}

	/**
	 * insert vasBean
	 */
	public static boolean insertVas(SQLiteDatabase db,
			HaqVasBean haqvasBean) {
 		ContentValues values = new ContentValues();
 		if (!MyUtils.isNull(haqvasBean.getDateNo())) {
 			values.put("dateNo", DateFormatUtil.geDataStringZeroClick(Long.parseLong(haqvasBean.getDateNo())));
		}
 		values.put("count", haqvasBean.getCount());
 		values.put("type", haqvasBean.getType());
 		
 		db.insert(DBConfig.VAS_TABLE, null, values);
		return true;
	}
 
}
