package com.bravesoft.riumachi.http;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.bravesoft.riumachi.bean.FileDownRequestBean;
import com.bravesoft.riumachi.bean.FileUpRequestBean;
import com.bravesoft.riumachi.bean.SecurityCode;
import com.bravesoft.riumachi.bean.UserData;
import com.bravesoft.riumachi.bean.UserRegist;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class JsonParser {

	public static final String JSON_USER = "user";
	public static final String JSON_ERROR = "error";
	public static final String JSON_USER_DATA = "data";

	private static JsonParser instance = null;
	private Gson gson = null;

	private JsonParser() {
		gson = new Gson();
	};

	public static JsonParser getIntance() {
		if (instance == null) {
			instance = new JsonParser();
		}
		return instance;
	}

	public <T> T jsonObject(JSONObject object, Class<T> c) {
		T t = gson.fromJson(object.toString(), c);
		return t;
	}

	public <T> ArrayList<T> jsonList(JSONArray jsonArray, String jsonKey,
			Class<T> c) {
		ArrayList<T> list = new ArrayList<T>();
		try {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject object = jsonArray.getJSONObject(i);
				if (jsonKey != null && !TextUtils.isEmpty(jsonKey)) {
					object = object.getJSONObject(jsonKey);
				}
				T s = gson.fromJson(object.toString(), c);
				list.add(s);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	public UserRegist jsonUserRegist(JSONObject response) {
		System.out.println("regist----->"+response.toString());
		UserRegist userRegist = new UserRegist();
		try {

			UserData userData = new UserData();

			if (response.getString("error").toString().equals("200")) {
				JSONObject jsonObject = response.getJSONObject("data");
				userData = gson.fromJson(jsonObject.toString(), UserData.class);
				userRegist.setData(userData);
			} else {
				userRegist.setData(null);
			}
			userRegist.setError(response.getString("error"));
			userRegist.setError_message(response.getString("error_message"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return userRegist;
	}

	public FileUpRequestBean jsonFileUpRequestBean(JSONObject response) {
		System.out.println("database---->"+response.toString());
		FileUpRequestBean fileUpRequestBean = new FileUpRequestBean();
		try {
			fileUpRequestBean.setError(response.getString("error"));
			if (response.getString("error").equals("200")) {
				fileUpRequestBean.setError_message(response
						.getString("error_message"));
				//fileUpRequestBean.setFile_url(response.getString("image_id"));
				fileUpRequestBean.setFile_url(null);
			} 

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return fileUpRequestBean;
	}
	public FileUpRequestBean jsonImageFileUpRequestBean(JSONObject response) {
		System.out.println("imagefile---->"+response.toString());
		FileUpRequestBean fileUpRequestBean = new FileUpRequestBean();
		try {
			fileUpRequestBean.setError(response.getString("error"));
			if (response.getString("error").equals("200")) {
				
				fileUpRequestBean.setError_message(response
						.getString("error_message"));
				fileUpRequestBean.setFile_url(response.getString("image_id"));
				
			} else {
				//fileUpRequestBean = null;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return fileUpRequestBean;
	}

	public FileDownRequestBean jsonFileDownRequestBean(JSONObject response) {
		FileDownRequestBean fileUpRequestBean = new FileDownRequestBean();

		System.out.println(response.toString());
		try {
			if (response.getString("error").equals("200")) {
				fileUpRequestBean.setError(response.getString("error"));
				fileUpRequestBean.setError_message(response
						.getString("error_message"));
				String byteString = response.getString("file_string");
				fileUpRequestBean.setFile_url(byteString);
			} else {
				fileUpRequestBean = null;
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return fileUpRequestBean;
	}

	public SecurityCode jsonGetSecurityCode(JSONObject response) {
		SecurityCode securityCode = new SecurityCode();
		try {

			UserData userData = new UserData();

			if (response.getString("error").toString().equals("200")) {
				securityCode.setSecurity_code(response.getString("security_code"));
			} else {
				securityCode.setSecurity_code(null);
			}
			securityCode.setError(response.getString("error"));
			securityCode.setError_message(response.getString("error_message"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return securityCode;

	}

}
