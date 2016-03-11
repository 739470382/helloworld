package com.bravesoft.riumachi.bean;

public class HaqVasBean {
	private int id;
	private String dateNo; 
	private String count;
	private String type;  //vas 1, haq 2
	 
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
	public String getDateNo() {
		return dateNo;
	}
	public void setDateNo(String dateNo) {
		this.dateNo = dateNo;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	 
	
}
