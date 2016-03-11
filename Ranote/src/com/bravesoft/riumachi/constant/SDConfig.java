package com.bravesoft.riumachi.constant;

import android.os.Environment;

public class SDConfig {
	public static String ROOT;
	public static final String DB_PATH = "/data"
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ "com.bravesoft.riumachi/databases/riumachidatabase.db";
	public static final String DATABASE_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + "com.bravesoft.riumachi/databases/";
	static {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			ROOT = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/Riumachi";
//			ROOT = "/data/data"
//					+ "/Riumachi";
		} else {
			// TO DO
		}
	}
	public static final String MEMOPIC = ROOT + "/memopic/";
	public static boolean hasSdcard() {  
	    if (android.os.Environment.getExternalStorageState().equals(  
	    android.os.Environment.MEDIA_MOUNTED)) {  
	    return true;  
	    } else  
	    return false;  
	    }
}
