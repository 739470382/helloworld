package com.bravesoft.riumachi.bean;

import java.util.Comparator;


public class ScheduleBean implements Comparable<ScheduleBean>{
	
	private int id ;
	private String type;
	private String parent;
	private String title;
	private String shunichi;
	private String starttime;
	private String endtime;
	private String kurikaeshi;
	private String medicalType;
	private String tuchi;
	private String schedule_start_time;
	private String schedule_end_time;
	private String custom_loop_type;
	private String custom_loop_value;
	private String sort;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getShunichi() {
		return shunichi;
	}
	public void setShunichi(String shunichi) {
		this.shunichi = shunichi;
	}
	public String getStarttime() {
		return starttime;
	}
	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}
	public String getEndtime() {
		return endtime;
	}
	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}
	public String getKurikaeshi() {
		return kurikaeshi;
	}
	public void setKurikaeshi(String kurikaeshi) {
		this.kurikaeshi = kurikaeshi;
	}
	public String getTuchi() {
		return tuchi;
	}
	public void setTuchi(String tuchi) {
		this.tuchi = tuchi;
	}
	public String getSchedule_start_time() {
		return schedule_start_time;
	}
	public void setSchedule_start_time(String schedule_start_time) {
		this.schedule_start_time = schedule_start_time;
	}
	public String getSchedule_end_time() {
		return schedule_end_time;
	}
	public void setSchedule_end_time(String schedule_end_time) {
		this.schedule_end_time = schedule_end_time;
	}
	public String getMedicalType() {
		return medicalType;
	}
	public void setMedicalType(String medicalType) {
		this.medicalType = medicalType;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getCustom_loop_type() {
		return custom_loop_type;
	}
	public void setCustom_loop_type(String custom_loop_type) {
		this.custom_loop_type = custom_loop_type;
	}
	public String getCustom_loop_value() {
		return custom_loop_value;
	}
	public void setCustom_loop_value(String custom_loop_value) {
		this.custom_loop_value = custom_loop_value;
	}
	@Override
	public int compareTo(ScheduleBean arg0) {
		return this.sort.compareTo(arg0.getSort());
	}
}
