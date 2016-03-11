package com.bravesoft.riumachi.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ImageDBHelper extends SQLiteOpenHelper {

	public ImageDBHelper(Context context) {
		super(context, ImageDBConfig.DB_NAME, null, ImageDBConfig.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sqlSymptomImageTable = "create table symptomimage_table(" +
				"id integer primary key autoincrement," +
				"symptomid integer," +
				"localurl text," +
				"isupload integer)";
		db.execSQL(sqlSymptomImageTable);
		
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS symptomimage_table");
		
		onCreate(db);
	}

}
