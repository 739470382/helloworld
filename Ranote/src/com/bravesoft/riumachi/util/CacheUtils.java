package com.bravesoft.riumachi.util;

import java.io.File;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;

public class CacheUtils {
	private static LruCache<String, Bitmap> mMemoryCache;
	static int maxMemory = (int) Runtime.getRuntime().maxMemory();
	static int mCacheSize = maxMemory / 8;
	static CacheUtils mCacheUtils;

	public CacheUtils(){
		if (mMemoryCache == null) { 
			mMemoryCache = new LruCache<String, Bitmap>(mCacheSize) {
				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getRowBytes() * value.getHeight();
				}
			};
		} 
	}
	public static LruCache<String, Bitmap> getcache() {
		if (mMemoryCache == null) {
			mMemoryCache = new LruCache<String, Bitmap>(mCacheSize) {
				@Override
				protected int sizeOf(String key, Bitmap value) {
					return value.getRowBytes() * value.getHeight();
				}
			};
			return mMemoryCache;
		} 
		return mMemoryCache;
	}
	public static CacheUtils getinstance() {
		if(mCacheUtils ==null ){
			mCacheUtils = new CacheUtils();
			return mCacheUtils;
		}else{
			return mCacheUtils;
		}
	}

	public void putBitmap(String key, Bitmap bitmap) {
		if (getBitmapFromMemCache(key) == null && bitmap != null) {
			mMemoryCache.put(key, bitmap);
			
			 
		}
	}
	
	//remove the cache by key
	public void clearCache(String key ){
		if(mMemoryCache.get(key)!=null)
		mMemoryCache.remove(key);
	}

	 public Bitmap getBitmapFromMemCache(String key) {
		return mMemoryCache.get(key);
	}
	
	public    Bitmap getDiskBitmap(String pathString)  
	{  
	  //  Bitmap bitmap = null;  
	    try  
	    {  
	        File file = new File(pathString);  
	        if(file.exists())  
	        {  
	        	return  BitmapFactory.decodeFile(pathString);  
	        }  
	    } catch (Exception e)  
	    {  
	    }  
	      
	    return null;
	   // return bitmap;  
	} 
}
