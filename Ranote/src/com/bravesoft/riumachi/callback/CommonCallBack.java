package com.bravesoft.riumachi.callback;
/*
 * Network requests a callback results
 */
public interface CommonCallBack<T> {

	void onStart();

	void onSuccess(T data);

	void onFailed(Throwable throwable, String reason);
}
