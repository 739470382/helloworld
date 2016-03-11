package com.bravesoft.riumachi.http;


/*
 * 网络请求操作
 */
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


import org.apache.http.Header;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

import com.bravesoft.riumachi.bean.FileDownRequestBean;
import com.bravesoft.riumachi.bean.FileUpRequestBean;
import com.bravesoft.riumachi.bean.SecurityCode;
import com.bravesoft.riumachi.bean.UserRegist;
import com.bravesoft.riumachi.callback.CommonCallBack;
import com.bravesoft.riumachi.util.LogUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.Base64;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpRequest {

	private static HttpRequest instance = null;
	private static final String USER_REGIST="/user/register";
	private static final String USER_LOGIN="/user/login";
	private static final String UP_DB="/user/upload_file";
	private static final String GET_DB="/user/get_file";
	private static final String USER_GET_SECURITY_CODE="/user/get_security_code";

	private HttpRequest() {
	}

	public static HttpRequest getInstance() {
		if (instance == null) {
			instance = new HttpRequest();
		}
		return instance;
	}

	public void downLoadFile(String FILE_URL,
			final CommonCallBack<byte[]> callback) {
		if (callback == null) {
			return;
		}

		String url = FILE_URL;
		HttpHelper.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				callback.onSuccess(arg2);
				LogUtil.d("SUCCESS", arg2.toString());
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				if (arg2 != null) {

					callback.onFailed(arg3, arg2.toString());
					LogUtil.d("FAILURE", arg2.toString());
				}
			}
		});

	}
	
	public void upLoadImage(String token,String type,File file,
			CommonCallBack<FileUpRequestBean> callBack){
		if (callBack == null) {
			return;
		}
		String url=null;
		RequestParams params=null;
		
		url=UP_DB;
		params=new RequestParams();
		params.put("token", token);
		params.put("type", type);
		
			try {
				
				params.put("file",file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		

		url = HttpHelper.getAbsolutelyUrl(url);
		HttpHelper.post(url, params, new UpImageJsonResponseHandler(callBack));
		System.out.println("token:"+token+"type:"+type+"url:"+url);
		
	}
	
	
	


	
	class UpImageJsonResponseHandler extends JsonHttpResponseHandler{
		CommonCallBack<FileUpRequestBean> callBack;
		UpImageJsonResponseHandler(CommonCallBack<FileUpRequestBean> callBack){
			this.callBack=callBack;
		}
//		@Override
//		public void onFailure(int statusCode, Header[] headers,
//				String responseString, Throwable throwable) {
//			super.onFailure(statusCode, headers, responseString, throwable);
//		}
		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			if (callBack == null) {
				return;
			}
			super.onFailure(statusCode, headers, throwable, errorResponse);
			if(errorResponse != null){
			callBack.onFailed(throwable, errorResponse.toString());
			}else{
				callBack.onFailed(throwable, "700");
			}
				
		}
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			if (callBack == null) {
				return;
			}
			FileUpRequestBean fileUpRequestBean=JsonParser.getIntance().jsonImageFileUpRequestBean(response);
			super.onSuccess(statusCode, headers, response);
			callBack.onSuccess(fileUpRequestBean);
		}
	
	}
		

	
//	HttpRequest.getInstance().upLoadImage("http://192.168.1.180/Test1/baseimageup.php", data, new CommonCallBack<String>() {
//		
//		@Override
//		public void onSuccess(String data) {
//			LogUtil.v("SUCCESS");
//		}
//		
//		@Override
//		public void onStart() {
//			
//		}
//		
//		@Override
//		public void onFailed(Throwable throwable, String reason) {
//			
//		}
//	});
//
//	}
	

	public void UpDb(String token,String type,File file, 
			CommonCallBack<FileUpRequestBean> callBack){
		if (callBack == null) {
			return;
		}
		String url=null;
		RequestParams params=null;
		
		url=UP_DB;
		params=new RequestParams();
		params.put("token", token);
		params.put("type", type);
		

		try {
			params.put("file", file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		url = HttpHelper.getAbsolutelyUrl(url);
		HttpHelper.post(url, params, new UpDbjsonResponseHandler(callBack));
	}
	
	public void DownLoadImage(String token,String type,String id,
			CommonCallBack<FileDownRequestBean> callBack){
		
		if (callBack == null) {
			return;
		}
		String url=null;
		RequestParams params=null;
		
		url=GET_DB;
		params=new RequestParams();
		params.put("token", token);
		params.put("type", type);
		params.put("image_id", id);

	
		
		url = HttpHelper.getAbsolutelyUrl(url);
		System.out.println("url----->"+url);
		HttpHelper.post(url, params, new DownDbjsonResponseHandler(callBack));
	}
	public void GetDb(String token,String type, 
			CommonCallBack<FileDownRequestBean> callBack){
		
		if (callBack == null) {
			return;
		}
		String url=null;
		RequestParams params=null;
		
		url=GET_DB;
		params=new RequestParams();
		params.put("token", token);
		params.put("type", type);

	
		
		url = HttpHelper.getAbsolutelyUrl(url);
		HttpHelper.post(url, params, new DownDbjsonResponseHandler(callBack));
	}
	
	public void Register(String mail, String password, String security_code,
			final CommonCallBack<UserRegist> callBack) {
		if (callBack == null) {
			return;
		}
		String url=null;
		RequestParams params=null;
		url=USER_REGIST;
		params=new RequestParams();
		params.put("mail", mail);
		params.put("password", password);
		params.put("security_code", security_code);
		url = HttpHelper.getAbsolutelyUrl(url);
		HttpHelper.post(url, params, new RegistjsonResponseHandler(callBack));

	}
	
	public void Login(String mail, String password,
			final CommonCallBack<UserRegist> callBack) {
		if (callBack == null) {
			return;
		}
		String url=null;
		RequestParams params=null;
		url=USER_LOGIN;
		params=new RequestParams();
		params.put("mail", mail);
		params.put("password", password);
		url = HttpHelper.getAbsolutelyUrl(url);
		HttpHelper.post(url, params, new RegistjsonResponseHandler(callBack));

	}
	
	public void GetSecurityCode(String mail,final CommonCallBack<SecurityCode> callBack){
		if (callBack==null) {
			return;
		}
		String url=null;
		RequestParams params=null;
		params=new RequestParams();
		params.put("mail", mail);
		url=USER_GET_SECURITY_CODE;
		url=HttpHelper.getAbsolutelyUrl(url);
		HttpHelper.post(url, params, new GetCodeJsonHttpResponseHandler(callBack));
		
	}
	
	private class GetCodeJsonHttpResponseHandler extends JsonHttpResponseHandler{
		CommonCallBack<SecurityCode> callBack;
		private GetCodeJsonHttpResponseHandler(CommonCallBack<SecurityCode> callBack){
			this.callBack=callBack;
		}
		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			if (callBack==null) {
				return;
			}
			super.onFailure(statusCode, headers, throwable, errorResponse);
			if(errorResponse != null){
				callBack.onFailed(throwable, errorResponse.toString());
				}else{
					callBack.onFailed(throwable, "700");
				}
		}
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			if (callBack==null) {
				return;
			}
			SecurityCode securityCode=JsonParser.getIntance().jsonGetSecurityCode(response);
			super.onSuccess(statusCode, headers, response);
			callBack.onSuccess(securityCode);
		}
		
		
	}
	
	private class RegistjsonResponseHandler extends JsonHttpResponseHandler{
		CommonCallBack<UserRegist> callBack;
		private RegistjsonResponseHandler(CommonCallBack<UserRegist> callBack){
			this.callBack=callBack;
		}
		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			if (callBack == null) {
				return;
			}
			super.onFailure(statusCode, headers, throwable, errorResponse);
			if(errorResponse != null){
			callBack.onFailed(throwable, errorResponse.toString());
			}else{
				callBack.onFailed(throwable, null);
			}
		}
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			if (callBack == null) {
				return;
			}
			UserRegist userRegist=JsonParser.getIntance().jsonUserRegist(response);
			super.onSuccess(statusCode, headers, response);
			callBack.onSuccess(userRegist);
		}
		
		
	}
	
	private class UpDbjsonResponseHandler extends JsonHttpResponseHandler{
		CommonCallBack<FileUpRequestBean> callBack;
		private UpDbjsonResponseHandler(CommonCallBack<FileUpRequestBean> callBack){
			this.callBack=callBack;
		}
		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			
			System.out.println("error_respose--->"+errorResponse);
			if (callBack == null) {
				return;
			}
			super.onFailure(statusCode, headers, throwable, errorResponse);
			if(errorResponse != null){
				callBack.onFailed(throwable, errorResponse.toString());
				}else{
					callBack.onFailed(throwable, "700");
				}
		}
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			System.out.println("respose--->"+response.toString());
			if (callBack == null) {
				return;
			}
			FileUpRequestBean fileUpRequestBean=JsonParser.getIntance().jsonFileUpRequestBean(response);
			super.onSuccess(statusCode, headers, response);
			callBack.onSuccess(fileUpRequestBean);
		}
		
		
	}
	
	private class DownDbjsonResponseHandler extends JsonHttpResponseHandler{
		CommonCallBack<FileDownRequestBean> callBack;
		private DownDbjsonResponseHandler(CommonCallBack<FileDownRequestBean> callBack){
			this.callBack=callBack;
		}
		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			if (callBack == null) {
				return;
			}
			super.onFailure(statusCode, headers, throwable, errorResponse);
			if(errorResponse != null){
				callBack.onFailed(throwable, errorResponse.toString());
				}else{
					callBack.onFailed(throwable, "700");
				}
		}
		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			if (callBack == null) {
				return;
			}
			FileDownRequestBean fileUpRequestBean=(FileDownRequestBean) JsonParser.getIntance().jsonFileDownRequestBean(response);
			super.onSuccess(statusCode, headers, response);
			callBack.onSuccess(fileUpRequestBean);
		}
		
		
	}

}
