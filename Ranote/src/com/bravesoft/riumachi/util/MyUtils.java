package com.bravesoft.riumachi.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.widget.Toast;

import com.bravesoft.riumachi.layout.ScreenConfig;

public class MyUtils {
	/**
	 * Compress Pictures
	 * 
	 * @param bitMap
	 *            Need to compress the bitmap
	 * @param maxSize
	 *            Save to how much KB units
	 * @return
	 */
	public static Bitmap imageZoom(Bitmap bitMap, double maxSize) {
		// Photos allow maximum space units: KB
		// double maxSize = 70.00;
		// The bitmap put to the array, the size of the bitmap is intended to (and actually read the original document is larger)
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		// The byte replaced KB
		double mid = b.length / 1024;
		// Analyzing bitmap space is larger than the allowed maximum space is larger than the compression less than if no compression
		if (mid > maxSize) {
			// Get bitmap size is the maximum allowable size for the number of times
			double i = mid / maxSize;
			// Start square root compression used herein broadband and high compression off times corresponding to the square root
			// 1. Keep the scale and height and proportions of the original bitmap consistent size after compression has reached the maximum size of the space occupied)
			bitMap = zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(i),
					bitMap.getHeight() / Math.sqrt(i));
		}
		return bitMap;
	}

	/**
	 * Compressed image to the specified size
	 * 
	 * @param bgimage
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
			double newHeight) {
		// Get this image width and height
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// Create action picture with matrix objects
		Matrix matrix = new Matrix();
		 
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		 
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
				(int) height, matrix, true);
		return bitmap;
	}

	public static boolean isNull(String str) {
		return str == null || str.length() == 0;
	}

	/**
	 * Get rounded picture
	 * 
	 * @param bitmap
	 * @param roundPx
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static Bitmap decodeBitmap(Context context, int resId, float width,
			float height) {
		float maxWidth = 0;
		float maxHeight = 0;
		if (width > 0 && height > 0) {
			maxWidth = width;
			maxHeight = height;
		} else {
			maxWidth = ScreenConfig.SCRREN_W;
			maxHeight = ScreenConfig.SCRREN_H;
		}
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		InputStream stream1;
		try {
			stream1 = context.getResources().openRawResource(resId);
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();
			int max = (int) Math.max(o.outWidth / maxWidth, o.outHeight
					/ maxHeight);
			if (max < 1)
				max = 1;
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = max;
			InputStream stream2 = context.getResources().openRawResource(resId);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
			stream2.close();
			return bitmap;
		} catch (Throwable ex) {
			if (ex instanceof OutOfMemoryError)
				System.gc();
		}
		return null;
	}

	/**
	 *  Resolution path ,then get bitmap
	 * 
	 * @param url
	 *            
	 * @return
	 */
	public static Bitmap decodeBitmap(String url) {
		int maxWidth = ScreenConfig.SCRREN_W;
		int maxHeight = ScreenConfig.SCRREN_H;
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		FileInputStream stream1;
		try {
			stream1 = new FileInputStream(new File(url));
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();
			int max = Math.max(o.outWidth / maxWidth, o.outHeight / maxHeight);
			if (max < 1)
				max = 1;
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = max;
			FileInputStream stream2 = new FileInputStream(new File(url));
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
			stream2.close();
			return bitmap;
		} catch (Throwable ex) {
			if (ex instanceof OutOfMemoryError)
				System.gc();
		}

		return null;
	}

	/**
	 * drawable switch to bitmap
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {
		/*
		 * Drawable2Bitmap
		 */
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
				.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;
	}

	/**
	 * save bitmap
	 * 
	 * @param bm
	 * @param picName
	 */
	public static String saveBitmap(String nameString, Bitmap bm,
			String fileString) {
		File f = null;
		try {
			File file = new File(fileString);
			if (!file.exists()) {
				file.mkdir();
			}
			f = new File(file, nameString + ".JPEG");
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
			return f.getAbsolutePath();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/** 
	 * delete the file
	 * @param fileName
	 */
	public static void delFile(String fileName) {
		File file = new File(fileName);
		if (file.isFile()) {
			file.delete();
		}
		// file.exists();
	}
	
	
	/**
	 * check work state
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetWork(final Context context) {
		boolean isNetWork = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isAvailable()
				&& networkInfo.isConnected()) {
			isNetWork = true;
		} else {
			Toast.makeText(context, "ネットワークに接続しておりません。", Toast.LENGTH_SHORT).show();
		}
		return isNetWork;
	}


	/**
	 * Checking whether has the SDCard
	 * 
	 * @return
	 */
	public static boolean isSDCard() {
		boolean temp = false;
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			temp = true;
		}
		return temp;
	}

	// get version number
	public static String GetVersion(Context context) {
		try {
			PackageInfo manager = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return manager.versionName;
		} catch (NameNotFoundException e) {
			return "Unknown";
		}
	}
}
