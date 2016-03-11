package com.bravesoft.riumachi.util;

import android.content.Context;

import com.bravesoft.riumachi.bean.UserData;
import com.bravesoft.riumachi.constant.SharePrefrenceKey;

public class LoginUtils {
	private Context context;

	public LoginUtils(Context context) {
		this.context = context;
	}

	/**
	 * save user info
	 * 
	 * @param UserData
	 */
	public void save(UserData user) {
		SharePrefUtil.putString(context ,SharePrefrenceKey.USER_ID, user.getUser_id());
		SharePrefUtil.putString(context ,SharePrefrenceKey.TOKEN, user.getToken());
	}

	/**
	 * 
	 * @param userId
	 * @param token
	 */
	public void save(String userId, String token) {
		SharePrefUtil.putString(context ,SharePrefrenceKey.USER_ID, userId);
		SharePrefUtil.putString(context ,SharePrefrenceKey.TOKEN, token);
	}
	
	public String getToken() {
		return SharePrefUtil.getString(context ,SharePrefrenceKey.TOKEN, null);
	}

	public void logout() {
		SharePrefUtil.putString(context ,SharePrefrenceKey.USER_ID, null);
		SharePrefUtil.putString(context ,SharePrefrenceKey.TOKEN, null);
	}

	public void update(UserData user) {
		save(user);
	}

	public boolean isLogin() {
		String userId =  SharePrefUtil.getString(context ,SharePrefrenceKey.USER_ID, null);
		if (!MyUtils.isNull(userId)) {
			return true;
		}else{
			return false;
		}
	}

	public UserData getUser() {
		UserData user = new UserData();
		user.setUser_id(SharePrefUtil.getString(context ,SharePrefrenceKey.USER_ID, user.getUser_id()));
		user.setToken(SharePrefUtil.getString(context ,SharePrefrenceKey.TOKEN, user.getToken()));
		return null;
	}
}
