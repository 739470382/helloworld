package com.bravesoft.riumachi.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context) {
		super(context, DBConfig.DB_NAME, null, DBConfig.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sqlScheduleTable = "create table schedule_table(" +
				"id integer primary key autoincrement," +
				"parent text," +
				"type text," +
				"title text," +
				"shunichi text," +
				"starttime text," +
				"endtime text," +
				"kurikaeshi text," +
				"tuchi text," +
				"medicalType text," +
				"custom_loop_type," +
				"custom_loop_value," +
				"schedule_start_time text," +
				"schedule_end_time text)";
		db.execSQL(sqlScheduleTable);
		
		String sqlMedicineTable = "create table medicine_table(" +
				"id integer primary key autoincrement," +
				"type text," +
				"name text)";
		db.execSQL(sqlMedicineTable);


		String sqlSymptomMome = "create table symptommemo_table(" +

				"id integer primary key autoincrement," +
				"dateNo text," +
				"memo text," +
				"photoaddress text," +
				"urladdress text)";
		db.execSQL(sqlSymptomMome);
		 
		String sqlVasTable = "create table vas_table(" +
				"id integer primary key autoincrement," +
				"dateNo text," +
				"count text," +
				"type text)";
		db.execSQL(sqlVasTable);
		
		
		String sqlMyKarteTable = "create table my_karte_table(" +
				"id integer primary key autoincrement," +
				"dateNo text," +
				"content text)";
		db.execSQL(sqlMyKarteTable);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		db.execSQL("DROP TABLE IF EXISTS schedule_table");
//		db.execSQL("DROP TABLE IF EXISTS medicine_table");
//		db.execSQL("DROP TABLE IF EXISTS symptommemo_table");
//		db.execSQL("DROP TABLE IF EXISTS vas_table");
//		db.execSQL("DROP TABLE IF EXISTS haq_table");
//		db.execSQL("DROP TABLE IF EXISTS my_karte_table");
//		onCreate(db);
	}

}
