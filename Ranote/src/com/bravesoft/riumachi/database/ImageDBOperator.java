package com.bravesoft.riumachi.database;

import java.util.ArrayList;
import java.util.List;

import com.bravesoft.riumachi.bean.MyKarteBean;
import com.bravesoft.riumachi.bean.SymptomImageBean;
import com.bravesoft.riumachi.bean.SymptomMemoBean;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ImageDBOperator {

	public static boolean isNeedUpload(SQLiteDatabase db) {

		String sql = "select * from " + ImageDBConfig.SYMPTOMIMAGE_TABLE
				+ " where isupload = '" + "0" + "'";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.getCount() != 0) {
			cursor.close();
			return true;
		}
		cursor.close();
		return false;
	}
	
	public static List<SymptomImageBean> queryUploadImage(SQLiteDatabase db){
		List<SymptomImageBean> data = new ArrayList<SymptomImageBean>();
		data.clear();
		int flag = 0;
		String sql = "select * from " + ImageDBConfig.SYMPTOMIMAGE_TABLE + " where isupload='"
				+ flag + "'";
		Cursor cursor = db.rawQuery(sql, null);
		while (cursor.moveToNext()) {
			SymptomImageBean symptomImageBean = new SymptomImageBean();
			symptomImageBean.setId(Integer.parseInt(cursor.getString(cursor
					.getColumnIndex("id"))));
			symptomImageBean
					.setSymptomid(cursor.getInt(cursor.getColumnIndex("symptomid")));
			symptomImageBean.setLocalurl(cursor.getString(cursor
					.getColumnIndex("localurl")));
			symptomImageBean
			.setIsupload(cursor.getInt(cursor.getColumnIndex("isupload")));
			data.add(symptomImageBean);
		}
		cursor.close();
		return data;
	}
	
	public static boolean updateImageRecord(SQLiteDatabase db,SymptomMemoBean memoBean){
		
		if (memoBean == null) {
			
			return false;
		}
	
		String sql = "select * from " + ImageDBConfig.SYMPTOMIMAGE_TABLE + " where symptomid='"
				+ memoBean.getId() + "'";
		Cursor cursor = db.rawQuery(sql, null);
		if(cursor.getCount() ==  0){
			ContentValues values = new ContentValues();
			values.put("symptomid", memoBean.getId());
			values.put("localurl", memoBean.getLocal_path());
			values.put("isupload", 0);
			
			db.insert(ImageDBConfig.SYMPTOMIMAGE_TABLE, null, values);
		}
		else{
		ContentValues values = new ContentValues();
		values.put("localurl", memoBean.getLocal_path());
		values.put("isupload", 0);
		
		long result = db.update(ImageDBConfig.SYMPTOMIMAGE_TABLE, values, "symptomid=?",
				new String[] { memoBean.getId() + "" });
		if (result < 0) {
			return false;
		}
		}
		return true;
		
	}
public static boolean copyImageRecord(SQLiteDatabase db,List<SymptomMemoBean> memoBeans){
		
		if (memoBeans == null || memoBeans.size() == 0) {
			
			return false;
		}
	
		db.execSQL("DELETE FROM "+ImageDBConfig.SYMPTOMIMAGE_TABLE);
		for(int i = 0 ;i<memoBeans.size();i++){
			if(memoBeans.get(i).getLocal_path() != null && !memoBeans.get(i).getLocal_path().equals("") && !memoBeans.get(i).getLocal_path().equals("null")){
			ContentValues values = new ContentValues();
			values.put("symptomid", memoBeans.get(i).getId());
			values.put("localurl", memoBeans.get(i).getLocal_path());
			values.put("isupload", 0);
			
			db.insert(ImageDBConfig.SYMPTOMIMAGE_TABLE, null, values);
			}
		}
		
		return true;
}
	
public static boolean updateImageRecordBySymptomImageBean(SQLiteDatabase db,SymptomImageBean memoBean){
		
		if (memoBean == null) {
			
			return false;
		}
		String sql = "select * from " + ImageDBConfig.SYMPTOMIMAGE_TABLE + " where symptomid='"
				+ memoBean.getSymptomid()+ "'";
		Cursor cursor = db.rawQuery(sql, null);
		if(cursor.getCount() ==  0){
			ContentValues values = new ContentValues();
			values.put("symptomid", memoBean.getSymptomid());
			values.put("localurl", memoBean.getLocalurl());
			values.put("isupload", memoBean.getIsupload());
			
			db.insert(ImageDBConfig.SYMPTOMIMAGE_TABLE, null, values);
		}else{
		
			ContentValues values = new ContentValues();
			values.put("localurl", memoBean.getLocalurl());
			values.put("isupload", memoBean.getIsupload());
			
			long result = db.update(ImageDBConfig.SYMPTOMIMAGE_TABLE, values, "symptomid=?",
					new String[] { memoBean.getSymptomid() + "" });
			if (result < 0) {
				return false;
			}
		}
		
		
		   return true;
		
	}
	public static void deleteImageRecord(SQLiteDatabase db,SymptomMemoBean memoBean){
		db.delete(ImageDBConfig.SYMPTOMIMAGE_TABLE, "symptomid=?", new String[] { memoBean.getId()+"" });
	}
}
