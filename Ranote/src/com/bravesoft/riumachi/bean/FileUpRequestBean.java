package com.bravesoft.riumachi.bean;
/*
 * Returns data uploaded file
 */

public class FileUpRequestBean {
	private String error;
	private String error_message;
	private String file_url;
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
		return file_url;
	}
	public void setFile_url(String file_url) {
		this.file_url = file_url;
	}
	
	
}
