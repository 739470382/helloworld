package com.bravesoft.riumachi.http;


import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntity;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.bravesoft.riumachi.util.LogUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

public class HttpHelper {
	public static final String BS_RIUMACHI_SERVER = "http://api.cloud-dental.com";

	private static AsyncHttpClient client = new AsyncHttpClient();
	private static SyncHttpClient httpClient = new SyncHttpClient();


	static {
		client.setTimeout(15000);
		// client.addHeader(header, value)
	}

	public static AsyncHttpClient getClient() {
		return client;
	}

	public static AsyncHttpClient getAsyncHttpClient() {
		return client;
	}


	public static String getAbsolutelyUrl(String relativeUrl) {
		return getAbsolutelyUrl(BS_RIUMACHI_SERVER, relativeUrl);
	}

	public static String getAbsolutelyUrl(String absolute, String relativeUrl) {
		String absoluteUrl;
		if (TextUtils.isEmpty(relativeUrl))
			return "";
		String path = Uri.parse(relativeUrl).getLastPathSegment();
		if (TextUtils.isEmpty(path) || TextUtils.equals("null", path))
			return "";
		relativeUrl = relativeUrl.substring(0,
				relativeUrl.length() - path.length())
				+ Uri.encode(path);
		if (relativeUrl.charAt(0) == '/')
			absoluteUrl = absolute + relativeUrl;
		else if (!relativeUrl.contains("://"))
			absoluteUrl = absolute + "/" + relativeUrl;
		else
			absoluteUrl = relativeUrl;
		return absoluteUrl;
	}

	public static void delete(String url) {
		client.delete(url, null);
	}

	public static void delete(Context context, String url,
			AsyncHttpResponseHandler responseHandler) {
		client.delete(context, url, responseHandler);
	}

	public static void delete(Context context, String url, Header[] headers,
			AsyncHttpResponseHandler responseHandler) {
		client.delete(context, url, headers, responseHandler);
	}

	public static void delete(Context context, String url, Header[] headers,
			RequestParams params, AsyncHttpResponseHandler responseHandler) {
		client.delete(context, url, headers, params, responseHandler);
	}

	/**
	 * get  string instence
	 * 
	 * @param url
	 *             
	 * @param responseHandler
	 */
	public static void get(String url, AsyncHttpResponseHandler responseHandler) {
		client.get(url, responseHandler);
	}

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(url, params, responseHandler);
	}

	/**
	 * Gets the JSON object or an array
	 * 
	 * @param url
	 * @param jsonResponseHandler
	 */
	public static void get(String url,
			JsonHttpResponseHandler jsonResponseHandler) {
		client.get(url, jsonResponseHandler);
	}

	public static void get(String url, RequestParams params,
			JsonHttpResponseHandler jsonResponseHandler) {
		client.get(url, params, jsonResponseHandler);
	}

	/**
	 * Download data, return byte data
	 * 
	 * @param url
	 *           
	 * @param bHandler
	 */
	public static void get(String url, BinaryHttpResponseHandler bHandler) {
		client.get(url, bHandler);
	}
	

	public static void post(String url, AsyncHttpResponseHandler responseHandler) {
		client.post(url, responseHandler);
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(url, params, responseHandler);
	}

	public static void post(String url,
			JsonHttpResponseHandler jsonResponseHandler) {

		client.post(url, jsonResponseHandler);
	}

	public static void post(String url, RequestParams params,
			JsonHttpResponseHandler jsonResponseHandler) {
		
		client.post(url, params, jsonResponseHandler);
	}

	/**
	 * Synchronous submission
	 * */
	public static void httpPost(Context context, String url,
			MultipartEntity multipartEntity,
			JsonHttpResponseHandler jsonResponseHandler) {
		httpClient.post(context, url, multipartEntity, "", jsonResponseHandler);
		
	}
	
	/**
	 * Synchronous submission
	 * */
	public static void httpPost(Context context, String url,
			RequestParams params,
			JsonHttpResponseHandler responseHandler) {
		httpClient.post(context, url, params, responseHandler);
		
	}

	// /////
	public static void put(String url, AsyncHttpResponseHandler responseHandler) {
		client.put(url, responseHandler);
	}

	public static void put(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.put(url, params, responseHandler);
	}

	public static void put(String url,
			JsonHttpResponseHandler jsonResponseHandler) {
		client.put(url, jsonResponseHandler);
	}

	public static void put(String url, RequestParams params,
			JsonHttpResponseHandler jsonResponseHandler) {
		client.put(url, params, jsonResponseHandler);
	}

	public static String failedReason(int statusCode) {
		String failedReson = "Http error";
		switch (statusCode) {
		case 408:
			failedReson = "time out";
			break;
		case 404:
			failedReson = "not found";
			break;
		}
		return failedReson;
	}

	public static void post(Context context, String url, HttpEntity entity,
			String contentType, JsonHttpResponseHandler responseHandler) {
		client.post(context, url, entity, contentType, responseHandler);
	}

	public static void cancelRequest(Context context,
			boolean mayInterruptIfRunning) {
		LogUtil.i("HttpHelper cancelRequest");
		client.cancelRequests(context, mayInterruptIfRunning);
	}

	public static void cancelAllRequest(boolean arg0) {
		client.cancelAllRequests(arg0);
	}

}
