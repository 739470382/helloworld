package com.bravesoft.riumachi.http;

/*
 * Image loading tools
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.Header;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.bravesoft.riumachi.util.LogUtil;
import com.loopj.android.http.BinaryHttpResponseHandler;

public class ImageDownLoader {
	private static final String TAG = "ImageDownLoader";
	Context context;

	public interface DownLoaderListener {
		public void onResult(int res, String s);
	}

	public ImageDownLoader() {

	}

	public void downloadImage(String url, String imageName,
			DownLoaderListener downLoaderListener) {
		String[] allowedContentTypes = new String[] { "image/png",
				"image/jpeg", "image/jpg" };
		HttpHelper.get(url, new ImageRespondHandler(allowedContentTypes,
				imageName, downLoaderListener));

	}

	public class ImageRespondHandler extends BinaryHttpResponseHandler {

		private String[] allowedContentTypes;
		private String savePathString;
		DownLoaderListener mDownLoaderListener;
		private String image_name;

		public ImageRespondHandler(String[] allowedContentTypes,
				String imageName, DownLoaderListener downLoaderListener) {
			super();
			this.allowedContentTypes = allowedContentTypes;
			mDownLoaderListener = downLoaderListener;
			image_name = imageName;
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				byte[] binaryData, Throwable reason) {
			LogUtil.i(TAG, "download failed");
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				byte[] binaryData) {
			LogUtil.i(TAG, " statusCode=========" + statusCode);
			LogUtil.i(TAG, " statusCode=========" + headers);
			LogUtil.i(TAG, " statusCode====binaryData len====="
					+ binaryData.length);

			if (statusCode == 200 && binaryData != null
					&& binaryData.length > 0) {
				boolean b = saveImage(binaryData, image_name);
				LogUtil.d(b + "");
				if (b) {
					mDownLoaderListener.onResult(0, savePathString);
				} else {
					// fail
					mDownLoaderListener.onResult(-1, savePathString);
				}
			}
		}

		private boolean saveImage(byte[] binaryData, String imageName) {
			Bitmap bmp = BitmapFactory.decodeByteArray(binaryData, 0,
					binaryData.length);
			File file = Environment.getExternalStorageDirectory();
			File file2 = new File(file, "DEMO");
			if (!file2.exists()) {
				file2.mkdir();
			}
			file2 = new File(file2, imageName + ".db");
			try {
				FileOutputStream os = new FileOutputStream(file2);
				os.write(binaryData);
				os.flush();
				os.close();
				LogUtil.v("ADDRESS", file.getPath() + "/" + "DEMO" + "/"
						+ imageName + ".db");
			} catch (IOException e) {
				e.printStackTrace();
			}

			return true;
		}

	}

}
