package com.bravesoft.riumachi.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.constant.SDConfig;


public class Utils {
	public static final String DATABASE_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + "com.bravesoft.riumachi/databases/";

	/**
	 * bitmap2string
	 */
	public static String convertBitmapToString(Bitmap bitmap) {
		if (bitmap == null) {
			return "";
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 100, baos);
		byte[] photo = baos.toByteArray();
		return Base64.encodeToString(photo, Base64.DEFAULT);
	}

	/** The string of MD5 encryption */
	public static String EncoderToMD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	 /** 
	  * check the network  
	  * @param context
	  * @return
	  */
	public static boolean netWorkCheck(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null) {
			return info.isConnected();
		} else {
			return false;
		}
	}

	/**
	 * Check Phone Number
	 * 
	 */
	public static boolean checkPhoneNumber(String tel_area_code,
			String tel_prefix_code, String tel_number) {
		int areaCodeLength = tel_area_code.length();
		int prefixCodeLength = tel_prefix_code.length();
		int numberLength = tel_number.length();
		int total = areaCodeLength + prefixCodeLength + numberLength;
		if (areaCodeLength <= 5 && prefixCodeLength <= 4 && numberLength == 4
				&& total >= 9 && total <= 11) {
			return true;
		} else {
			return false;
		}
	}
	
	//Check whether the format of the string is the email
		public static boolean isEmail(String strEmail) {
		    //String strPattern = "^[a-zA-Z][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
			//String strPattern = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
			String strPattern = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
			Pattern p = Pattern.compile(strPattern);
		    Matcher m = p.matcher(strEmail);
		    if(strEmail.indexOf("..") != -1){
		    	return false;
		    }
		    if(strEmail.indexOf(".@") != -1){
		    	return false;
		    }
		    return m.matches();
		}

		public  static Bitmap getDiskBitmap(String pathString)  
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
		
		 public static int readPictureDegree(String path) {
		        int degree  = 0;
		        try {
		                ExifInterface exifInterface = new ExifInterface(path);
		                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
		                switch (orientation) {
		                case ExifInterface.ORIENTATION_ROTATE_90:
		                        degree = 90;
		                        break;
		                case ExifInterface.ORIENTATION_ROTATE_180:
		                        degree = 180;
		                        break;
		                case ExifInterface.ORIENTATION_ROTATE_270:
		                        degree = 270;
		                        break;
		                }
		        } catch (IOException e) {
		                e.printStackTrace();
		        }
		        return degree;
		 }
		 
		  public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {  
		       //旋转图片 动作   
		       Matrix matrix = new Matrix();
		       matrix.postRotate(angle);  
		       System.out.println("angle2=" + angle);  
		       // 创建新的图片   
		       Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,  
		               bitmap.getWidth(), bitmap.getHeight(), matrix, true);  
		       if(bitmap != null){
		       //bitmap.recycle();
		       }
		       return resizedBitmap;  
		       
		   }
		
		public static String getStringData(){  
	        final Calendar c = Calendar.getInstance();  
	        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));  
	        String mYear = String.valueOf(c.get(Calendar.YEAR)); // Get the current year
	        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);//Get the current month
	        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// Get the current month's date number  
	        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));  
	        if("1".equals(mWay)){  
	            mWay ="日";  
	        }else if("2".equals(mWay)){  
	            mWay ="月";  
	        }else if("3".equals(mWay)){  
	            mWay ="火";  
	        }else if("4".equals(mWay)){  
	            mWay ="水";  
	        }else if("5".equals(mWay)){  
	            mWay ="木";  
	        }else if("6".equals(mWay)){  
	            mWay ="金";  
	        }else if("7".equals(mWay)){  
	            mWay ="土";  
	        }  
	        return mYear + "/" + mMonth + "/" + mDay+" "+"(" + mWay+")";  
	    }  
		public static String getStringWeek(Long dateLong){  
			Calendar calendar = Calendar.getInstance();
			Date date = new Date(dateLong);
			SimpleDateFormat format = new SimpleDateFormat("MM/dd");
			String dataWithWeek = format.format(date);
			calendar.setTime(date);
			String result = null;
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
	        if("1".equals(dayOfWeek+"")){  
	        	result =" (日)";  
	        }else if("2".equals(dayOfWeek+"")){  
	        	result =" (月)";  
	        }else if("3".equals(dayOfWeek+"")){  
	        	result =" (火)";  
	        }else if("4".equals(dayOfWeek+"")){  
	        	result =" (水)";  
	        }else if("5".equals(dayOfWeek+"")){  
	        	result =" (木)";  
	        }else if("6".equals(dayOfWeek+"")){  
	        	result =" (金)";  
	        }else if("7".equals(dayOfWeek+"")){  
	        	result =" (土)";  
	        }  
	        return result;  
	    }  

		public static void writeFile(String filename, String content) {  
			
			File file = new File(DATABASE_PATH);
			File file2 = new File(filename);
			if (file2.exists()) {
				file2.delete();
			}
			if (!file.exists()) {
				file.mkdirs();
			}
			file = new File(file, "riumachidatabase" + ".db");
			//byte[] buffer = content.getBytes();
			try {
				FileOutputStream os = new FileOutputStream(file);
				os.write(Base64.decode(content, Base64.DEFAULT));
				
				os.flush();
				os.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		 
		/** 
		 * save image
		 * @param bitmap
		 * @param name
		 * @return
		 */
		public static String saveimage(Bitmap bitmap, String name) {

			// Save picture
			FileOutputStream fout = null;
			File file = new File(SDConfig.MEMOPIC);
			if (!file.exists()) {
				file.mkdirs();// Build multi-level directory, it must be mkdirs
			}

			String filename = SDConfig.MEMOPIC + name;
			
			File f = new File(filename);
			if (!f.exists()) {
				try {
					f.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}

			try {
				fout = new FileOutputStream(filename);
				
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fout);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} finally {
				try {
					fout.flush();
					fout.close();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
				return filename;
			}
		}
		
		public static String saveDownLoadimage(String name, String content) {

			// Save picture
			//FileOutputStream fout = null;
			File file = new File(SDConfig.MEMOPIC);
			if (!file.exists()) {
				file.mkdirs();// Build multi-level directory, it must be mkdirs
			}

			String filename = SDConfig.MEMOPIC + name;
			
			File f = new File(filename);
			if (!f.exists()) {
				try {
					f.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			
			}

			try {
				FileOutputStream os = new FileOutputStream(f);
				os.write(Base64.decode(content, Base64.DEFAULT));
				
				os.flush();
				os.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} finally {
				
				return filename;
			}
		}
		
		public static void deleteAllFiles(File root) {  
	        File files[] = root.listFiles();  
	        if (files != null)  
	            for (File f : files) {  
	                if (f.isDirectory()) { // 判断是否为文件夹  
	                    deleteAllFiles(f);  
	                    try {  
	                        f.delete();  
	                    } catch (Exception e) {  
	                    }  
	                } else {  
	                    if (f.exists()) { // 判断是否存在  
	                        deleteAllFiles(f);  
	                        try {  
	                            f.delete();  
	                        } catch (Exception e) {  
	                        }  
	                    }  
	                }  
	            }  
	    }  
		
		public static void showErrorMessage(Context context,String error){
			
			if (error.equals(100 + "")) {
				Toast.makeText(
						context,
						context
								.getResources()
								.getString(
										R.string.error_code_100),
						1000).show();
				
			}else if (error.equals(411 + "")) {
				Toast.makeText(
						context,
						context
								.getResources()
								.getString(
										R.string.error_code_411),
						1000).show();
				
			}else if (error.equals(412 + "")) {
				Toast.makeText(
						context,
						context
								.getResources()
								.getString(
										R.string.error_code_412),
						1000).show();
				
			} else if (error.equals(413 + "")) {
				Toast.makeText(
						context,
						context
								.getResources()
								.getString(
										R.string.error_code_413),
						1000).show();
				
			} else if (error.equals(414 + "")) {
				Toast.makeText(
						context,
						context
								.getResources()
								.getString(
										R.string.error_code_414),
						1000).show();
				
			} else if (error.equals(421 + "")) {
				Toast.makeText(
						context,
						context
								.getResources()
								.getString(
										R.string.error_code_421),
						1000).show();
				
			} else if (error.equals(422 + "")) {
				Toast.makeText(
						context,
						context
								.getResources()
								.getString(
										R.string.error_code_422),
						1000).show();
				
			} else if (error.equals(423 + "")) {
				Toast.makeText(
						context,
						context
								.getResources()
								.getString(
										R.string.error_code_423),
						1000).show();
				
			} else if (error.equals(424 + "")) {
				Toast.makeText(
						context,
						context
								.getResources()
								.getString(
										R.string.error_code_424),
						1000).show();
				
			} else if (error.equals(415 + "")) {
				Toast.makeText(
						context,
						context
								.getResources()
								.getString(
										R.string.error_code_415),
						1000).show();
				
			} else if (error.equals(425 + "")) {
				Toast.makeText(
						context,
						context
								.getResources()
								.getString(
										R.string.error_code_425),
						1000).show();
				
			} else if (error.equals(426 + "")) {
				Toast.makeText(
						context,
						context
								.getResources()
								.getString(
										R.string.error_code_426),
						1000).show();
				
			} else if (error.equals(427 + "")) {
				Toast.makeText(
						context,
						context
								.getResources()
								.getString(
										R.string.error_code_427),
						1000).show();
				
			} else if (error.equals(428 + "")) {
				Toast.makeText(
						context,
						context
								.getResources()
								.getString(
										R.string.error_code_428),
						1000).show();
				
			}else if (error.equals(434 + "")) {
				Toast.makeText(
						context,
						context
								.getResources()
								.getString(
										R.string.error_code_434),
						1000).show();
				
			}else if (error.equals(666 + "")) {
				Toast.makeText(
						context,
						context
								.getResources()
								.getString(
										R.string.error_code_666),
						1000).show();
				
			}else{
				Toast.makeText(
						context,
						context
								.getResources()
								.getString(
										R.string.error_code_100),
						1000).show();
			}
			
		}

		@TargetApi(Build.VERSION_CODES.KITKAT) @SuppressLint("NewApi") 
		public static String getPath(final Context context, final Uri uri) {  
			  
		    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;  
		  
		    // DocumentProvider  
		    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {  
		        // ExternalStorageProvider  
		        if (isExternalStorageDocument(uri)) {  
		            final String docId = DocumentsContract.getDocumentId(uri);  
		            final String[] split = docId.split(":");  
		            final String type = split[0];  
		  
		            if ("primary".equalsIgnoreCase(type)) {  
		                return Environment.getExternalStorageDirectory() + "/" + split[1];  
		            }  
		        }  
		        // DownloadsProvider  
		        else if (isDownloadsDocument(uri)) {  
		  
		            final String id = DocumentsContract.getDocumentId(uri);  
		            final Uri contentUri = ContentUris.withAppendedId(  
		                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));  
		  
		            return getDataColumn(context, contentUri, null, null);  
		        }  
		        // MediaProvider  
		        else if (isMediaDocument(uri)) {  
		            final String docId = DocumentsContract.getDocumentId(uri);  
		            final String[] split = docId.split(":");  
		            final String type = split[0];  
		  
		            Uri contentUri = null;  
		            if ("image".equals(type)) {  
		                contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;  
		            } else if ("video".equals(type)) {  
		                contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;  
		            } else if ("audio".equals(type)) {  
		                contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;  
		            }  
		  
		            final String selection = "_id=?";  
		            final String[] selectionArgs = new String[] { split[1] };  
		  
		            return getDataColumn(context, contentUri, selection, selectionArgs);  
		        }  
		    }  
		    // MediaStore (and general)  
		    else if ("content".equalsIgnoreCase(uri.getScheme())) {  
		  
		        // Return the remote address  
		        if (isGooglePhotosUri(uri))  
		            return uri.getLastPathSegment();  
		  
		        return getDataColumn(context, uri, null, null);  
		    }  
		    // File  
		    else if ("file".equalsIgnoreCase(uri.getScheme())) {  
		        return uri.getPath();  
		    }  
		  
		    return null;  
		}  
		  
		public static String getDataColumn(Context context, Uri uri, String selection,  
		        String[] selectionArgs) {  
		  
		    Cursor cursor = null;  
		    final String column = "_data";  
		    final String[] projection = { column };  
		  
		    try {  
		        cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,  
		                null);  
		        if (cursor != null && cursor.moveToFirst()) {  
		            final int index = cursor.getColumnIndexOrThrow(column);  
		            return cursor.getString(index);  
		        }  
		    } finally {  
		        if (cursor != null)  
		            cursor.close();  
		    }  
		    return null;  
		}  
		  
		/** 
		 * @param uri The Uri to check. 
		 * @return Whether the Uri     equals   "com.android.externalstorage.documents"
		 */  
		public static boolean isExternalStorageDocument(Uri uri) {  
		    return "com.android.externalstorage.documents".equals(uri.getAuthority());  
		}  
		  
		/** 
		 * @param uri The Uri to check. 
		 * @return Whether the Uri    equals "com.android.providers.downloads.documents". 
		 */  
		public static boolean isDownloadsDocument(Uri uri) {  
		    return "com.android.providers.downloads.documents".equals(uri.getAuthority());  
		}  
		  
		/** 
		 * @param uri The Uri to check. 
		 * @return Whether the Uri equals MediaProvider. 
		 */  
		public static boolean isMediaDocument(Uri uri) {  
		    return "com.android.providers.media.documents".equals(uri.getAuthority());  
		}  
		  
		/** 
		 * @param uri The Uri to check. 
		 * @return Whether the Uri equals Google Photos. 
		 */  
		public static boolean isGooglePhotosUri(Uri uri) {  
		    return "com.google.android.apps.photos.content".equals(uri.getAuthority());  
		}
		public static Bitmap readBitmapAutoSize(String filePath, int outWidth,
				int outHeight) {
			FileInputStream fs = null;
			BufferedInputStream bs = null;
			try {
				fs = new FileInputStream(filePath);
				bs = new BufferedInputStream(fs);
				BitmapFactory.Options options = setBitmapOption(filePath, outWidth,
						outHeight);
				return BitmapFactory.decodeStream(bs, null, options);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					bs.close();
					fs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		private static BitmapFactory.Options setBitmapOption(String file,
				int width, int height) {
			BitmapFactory.Options opt = new BitmapFactory.Options();
			opt.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(file, opt);

			int outWidth = opt.outWidth;
			int outHeight = opt.outHeight;
			opt.inDither = false;
			opt.inPreferredConfig = Bitmap.Config.RGB_565;

			opt.inSampleSize = 1;

			if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {
				int sampleSize = (outWidth / width + outHeight / height) / 2;
				opt.inSampleSize = sampleSize;
			}

			opt.inJustDecodeBounds = false;
			return opt;
		}
}
