package com.bravesoft.riumachi.constant;

import com.bravesoft.riumachi.R;

public interface DailyEventType {

	public static final String NORMAL_SCHEDULE = "1";
	public static final String SEE_THE_DOCTER = "2";
	public static final String BIOLOGCAL_AGENT = "3";
	public static final String ANTIBIOTIC = "antibiotic";
	public static final String OTHER_MEDICINE = "other_medicine";

	public static final String[] DAILY_EVENT_ARRAY = { NORMAL_SCHEDULE,
			SEE_THE_DOCTER, BIOLOGCAL_AGENT, ANTIBIOTIC, OTHER_MEDICINE };
	public static final int[] DAILY_EVENT_IMAGE = { R.drawable.icon_calendar,
			R.drawable.icon_see_the_docter, R.drawable.icon_medicine_a,
			R.drawable.icon_medicine_b, R.drawable.icon_medicine_c };
}
