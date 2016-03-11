package com.bravesoft.riumachi.bean;
/*
 * Returns data uploaded file
 */

public class FileDownRequestBean {
	private String error;
	private String error_message;
	private String file_content;
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getError_message() {
		return error_message;
	}
	public void setError_message(String error_message) {
		this.error_message = error_message;
	}
	public String getFile_url() {
		return file_content;
	}
	public void setFile_url(String bs) {
		this.file_content = bs;
	}
	
	
}
