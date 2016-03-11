package com.bravesoft.riumachi.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.text.TextPaint;
import android.text.format.DateFormat;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.appliction.App;
import com.bravesoft.riumachi.bean.FileUpRequestBean;
import com.bravesoft.riumachi.bean.SymptomMemoBean;
import com.bravesoft.riumachi.callback.CommonCallBack;
import com.bravesoft.riumachi.constant.SDConfig;
import com.bravesoft.riumachi.constant.SymptomTagConfig;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.database.DBHelper;
import com.bravesoft.riumachi.database.DBOperator;
import com.bravesoft.riumachi.database.ImageDBHelper;
import com.bravesoft.riumachi.database.ImageDBOperator;
import com.bravesoft.riumachi.dialog.MyDialog;
import com.bravesoft.riumachi.http.HttpRequest;
import com.bravesoft.riumachi.layout.LayoutUtils;
import com.bravesoft.riumachi.layout.ScreenConfig;
import com.bravesoft.riumachi.util.CacheUtils;
import com.bravesoft.riumachi.util.CommonUtils;
import com.bravesoft.riumachi.util.DateFormatUtil;
import com.bravesoft.riumachi.util.MyUtils;
import com.bravesoft.riumachi.util.Utils;

/*
 *SymptomAddActivity
 */
public class SymptomAddActivity extends BaseActivity implements OnClickListener {

	
	private SQLiteDatabase db;// database of all data
	private DBHelper mDbHelper;
	private SQLiteDatabase imagedb;// database of image
	private ImageDBHelper mImageDbHelper;
	private TextView textView_title_name, textView_title_item, textView_date,
			textView_week, textView_image_name, textView_photo,
			textView_camera;
	private ImageView imageView_camera, imageView_back, smyptom_image,
			cancel_smytomimage;
	private EditText editText;// Input box information
	private LinearLayout layout_symptom_image;
	private LinearLayout layout_dialog;
	private View smyptom_aad_line2;// Dividing line
	private Bitmap bmp;// The method of storing the received image result
	private MyDialog dialog;
	private String localaddress = null;// Picture Address
	private String urladdress = null;
	private String mOpenType;
	private String id;
	private SymptomMemoBean mySymptomMemoBean;
	private File tempFile;// Temporary Files

	private ScrollView imageScrollView;// scroll bar
	private View view_back, view_add;
	private LinearLayout backgroudlayer;
	private RelativeLayout relative_showimage;
	private boolean isaddable = false;
	private LruCache<String, Bitmap> mMemoryCache;
	private CacheUtils mCacheUtils;
	private Bitmap bitmap;//don't delete
	int maxMemory = (int) Runtime.getRuntime().maxMemory();
	int mCacheSize = maxMemory / 8;
	private String oldLocal_url = null;

	float imageWidth;
	float imageheight;
	int width;
	float scale;
	int height;
	Uri uri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_symptom_add);
		mDbHelper = new DBHelper(getApplicationContext());
		db = mDbHelper.getReadableDatabase();
		mImageDbHelper = new ImageDBHelper(getApplicationContext());
		imagedb = mImageDbHelper.getReadableDatabase();

		if (savedInstanceState != null) {
			mOpenType = savedInstanceState.getString(SymptomTagConfig.OPEN_TYPE);
			id = savedInstanceState.getString(SymptomTagConfig.MY_SMYPTOM_ID);
			// String tempurl = savedInstanceState.getString("file_url");
			// if(tempurl != null&&tempurl.length() != 0){
			// tempFile = new File(tempurl);
			// }

		} else if (getIntent() != null) {
			Intent intent = getIntent();
			mOpenType = intent.getStringExtra(SymptomTagConfig.OPEN_TYPE);
			id = intent.getStringExtra(SymptomTagConfig.MY_SMYPTOM_ID);

		}
		// mMemoryCache = new LruCache<String, Bitmap>(mCacheSize) {
		// @Override
		// protected int sizeOf(String key, Bitmap value) {
		// return value.getRowBytes() * value.getHeight();
		// }
		// };
		// mCacheUtils = CacheUtils.getinstance();
		initView();
	}

	private void initView() {
		((LinearLayout) findViewById(R.id.linear_root))
				.setOnClickListener(this);
		layout_symptom_image = getRateView(R.id.layout_symptom_image, true);
		getRateView(R.id.layout_symptom_time, true);
		RelativeLayout titlelayout = getRateView(R.id.relative_title_other,
				true);
		getRateView(R.id.relativeLayout_symptom_image, true)
				.setOnClickListener(this);
		backgroudlayer = getRateView(R.id.symptom_add_background, true);
		imageScrollView = getRateView(R.id.smyptom_image_scroll, true);
		getRateView(R.id.layout_image_show_content, true).setOnClickListener(
				this);

		backgroudlayer.setOnClickListener(this);
		titlelayout.setOnClickListener(this);
		imageScrollView.setOnClickListener(this);

		textView_title_name = getTextView(R.id.txt_title_name, true, 37,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		textView_title_item = getTextView(R.id.txt_operate, true, 33,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		textView_date = getTextView(R.id.textview_symptom_date, true, 32,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		textView_week = getTextView(R.id.textview_symptom_week, true, 32,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		textView_image_name = getTextView(R.id.textview_image_name, true, 32,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		imageView_back = getRateView(R.id.img_back, true);
		imageView_camera = getRateView(R.id.imageview_camera, true);
		smyptom_image = getRateView(R.id.smyptom_image, true);
		smyptom_image.setOnClickListener(this);
		view_add = getRateView(R.id.view_add, true);
		view_back = getRateView(R.id.view_back, true);
		view_add.setClickable(false);
		// smyptom_image = (ImageView)findViewById(R.id.smyptom_image);
		cancel_smytomimage = getRateView(R.id.cancel_smyptomimage, true);
		editText = getRateView(R.id.edittext_symptom, true);

		textView_title_name.setText(this.getResources().getString(
				R.string.sym_title_text));
		textView_title_item.setText(this.getResources().getString(
				R.string.schedule_state_create));
		textView_date.setText(Utils.getStringData());
		// textView_week.setText("(火)");
		textView_image_name.setText(this.getResources().getString(
				R.string.sym_image_name));
		TextPaint paint_date = textView_date.getPaint();
		TextPaint paint_week = textView_week.getPaint();
		smyptom_aad_line2 = getRateView(R.id.smyptom_aad_line2, true);
		paint_date.setFakeBoldText(true);
		paint_week.setFakeBoldText(true);

		if (mOpenType.equals(SymptomTagConfig.OPEN_TYPE_EDIT)) {
			mySymptomMemoBean = DBOperator.querySymptomMemoById(db, id);
			urladdress = mySymptomMemoBean.getUrl();
			oldLocal_url = mySymptomMemoBean.getLocal_path();
			isaddable = true;
			if (mySymptomMemoBean.getLocal_path() == null) {
				imageScrollView.setVisibility(View.GONE);
			}
			editText.setText(mySymptomMemoBean.getName());

			if ((mySymptomMemoBean.getLocal_path() != null && !mySymptomMemoBean
					.getLocal_path().equals("null"))
					&& mySymptomMemoBean.getLocal_path().length() != 0) {
				File file = new File(mySymptomMemoBean.getLocal_path());
				if (file != null && file.exists()) {
					if (bmp != null && !bmp.isRecycled()) {
						bmp.recycle();
					}
					// bmp =
					// Utils.getDiskBitmap(mySymptomMemoBean.getLocal_path());
					BitmapFactory.Options opt = new BitmapFactory.Options();
					opt.inJustDecodeBounds = true;
					BitmapFactory.decodeFile(mySymptomMemoBean.getLocal_path(),
							opt);
					float imageWidth = opt.outWidth;
					float imageheight = opt.outHeight;
					int width = ScreenConfig.SCRREN_W
							- LayoutUtils.getRate4density(180);
					float scale = width / imageWidth;
					int height = (int) (imageheight * scale);

					LayoutParams params = (LayoutParams) smyptom_image
							.getLayoutParams();
					params.width = width;
					params.height = height;
					smyptom_image.setLayoutParams(params);
				}
			}

			textView_title_item.setText(R.string.schedule_state_edit);

			if ((mySymptomMemoBean.getLocal_path() != null && !mySymptomMemoBean
					.getLocal_path().equals("null"))
					&& mySymptomMemoBean.getLocal_path().length() != 0) {
				File file = new File(mySymptomMemoBean.getLocal_path());
				if (file != null && file.exists()) {
					layout_symptom_image.setVisibility(View.GONE);
					smyptom_aad_line2.setVisibility(View.GONE);
					cancel_smytomimage.setVisibility(View.VISIBLE);
					localaddress = mySymptomMemoBean.getLocal_path();

					if (CacheUtils.getcache().get(localaddress) != null) {
						smyptom_image.setImageBitmap(CacheUtils.getcache().get(
								localaddress));
					} else {
						bitmap = Utils.readBitmapAutoSize(localaddress, width,
								height);
						smyptom_image.setImageBitmap(bitmap);
						CacheUtils.getcache().put(localaddress, bitmap);
					}
				} else {
					imageScrollView.setVisibility(View.GONE);
				}

			}
		} else {
			isaddable = false;
			imageScrollView.setVisibility(View.GONE);
		}

		imageView_camera.setOnClickListener(this);
		layout_symptom_image.setOnClickListener(this);
		cancel_smytomimage.setOnClickListener(this);
		view_add.setOnClickListener(this);
		view_back.setOnClickListener(this);

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		System.out.println("onSaveInstanceState");
		outState.putString(SymptomTagConfig.MY_SMYPTOM_ID, id);
		outState.putString(SymptomTagConfig.OPEN_TYPE, mOpenType);
		if (tempFile != null && tempFile.exists()) {
			outState.putString("file_url", tempFile.getAbsolutePath());
			System.out.println("tempurl==" + tempFile.getAbsolutePath());
		} else {
			System.out.println("tempFile != null&& tempFile.exists()==");
		}
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("onDestroy");
		db.close();
		imagedb.close();

		// if (bmp != null && !bmp.isRecycled()) {
		//
		// bmp.recycle();
		// bmp = null;
		// }
		// if (bitmap != null && !bitmap.isRecycled()) {
		//
		// bitmap.recycle();
		// bitmap = null;
		// }
		System.gc();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.relativeLayout_symptom_image:
		case R.id.linear_root:
			CommonUtils.exitKeyboard(this);
			break;
		case R.id.view_back:
			CommonUtils.exitKeyboard(this);

			// Back button

			finishActivity();
			overridePendingTransition(0, R.anim.activity_bottom_out);
			break;
		case R.id.cancel_smyptomimage:

			CommonUtils.exitKeyboard(this);
			// button of Cancel picture
			if (tempFile != null && tempFile.exists()) {
				tempFile = new File(localaddress);
				tempFile.delete();
			}
			if (bmp != null && !bmp.isRecycled()) {
				bmp.recycle();
			}
			localaddress = null;
			urladdress = null;

			layout_symptom_image.setVisibility(View.VISIBLE);
			imageScrollView.setVisibility(View.GONE);
			smyptom_aad_line2.setVisibility(View.VISIBLE);
			cancel_smytomimage.setVisibility(View.GONE);
			smyptom_image.setImageBitmap(null);
			break;
		case R.id.layout_symptom_image:
			CommonUtils.exitKeyboard(this);
			// Select a picture of the pop-up dialog buttons
			dialog = new MyDialog(SymptomAddActivity.this, R.style.MyDialog);
			dialog.setContentView(R.layout.dialog_symptom_add);
			layout_dialog = (LinearLayout) dialog
					.findViewById(R.id.layout_dialog_symptom);
			LayoutUtils.rateScale(SymptomAddActivity.this, layout_dialog, true);
			textView_camera = (TextView) dialog
					.findViewById(R.id.textview_dialog_camera);
			textView_photo = (TextView) dialog
					.findViewById(R.id.textview_dialog_photo);
			LayoutUtils.setTextSize(textView_camera, 32,
					TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
			LayoutUtils.setTextSize(textView_photo, 32,
					TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
			textView_camera.setText(this.getResources().getString(
					R.string.start_camera));
			textView_camera.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					// startActivityForResult(intent, 2);

					// Determining whether the memory card can be used for
					// storage
					if (SDConfig.hasSdcard()) {
						dialog.dismiss();

						tempFile = new File(Environment
								.getExternalStorageDirectory(), DateFormat
								.format("yyyyMMdd_hhmmss",
										Calendar.getInstance(Locale.CHINA))
								+ "(temp).jpg");
						App.getInstance().setTemFileUri(
								tempFile.getAbsolutePath());
						

						Uri uri = Uri.fromFile(tempFile);
						intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
					}
					startActivityForResult(intent, 2);
				}
			});
			textView_photo.setText(this.getResources().getString(
					R.string.open_photo_album));
			// Select a picture from an album
			textView_photo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT); // Comes
																			// with
																			// a
																			// browser
																			// file
																			// Activity
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("image/*"); // This is the image path to
												// reach the next, all the files
												// default to the memory card
					startActivityForResult(Intent.createChooser(intent, null),
							1);
				}
			});

			// Set the width of the screen, the same dialog
			WindowManager windowManager = getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
			lp.width = (int) (display.getWidth());
			dialog.getWindow().setAttributes(lp);
			// Set the display at the bottom
			Window window = dialog.getWindow();
			window.setGravity(Gravity.BOTTOM);
			window.setWindowAnimations(R.style.mystyle);
			dialog.show();
			break;
		case R.id.view_add:
			// Add Record button
			CommonUtils.exitKeyboard(this);

			if (MyUtils.isNull(editText.getText().toString())||editText.getText().toString().trim().equals("")) {
				CommonUtils
						.showToastMessage(
								this,
								getString(R.string.schedule_memo_operate_failed_message));
				return;
			} else {

				boolean isSuccess = false;// Record is added successfully

				if (mOpenType.equals(SymptomTagConfig.OPEN_TYPE_EDIT)) {
					mySymptomMemoBean = DBOperator.querySymptomMemoById(db, id);
				} else {
					mySymptomMemoBean = new SymptomMemoBean();
				}
				if (editText.getText().toString().length() > 3000) {
					// CommonUtils.showToastMessage(this,
					// getString(R.string.text_outsize));
				} else {
					mySymptomMemoBean.setName(editText.getText().toString());
				}
				mySymptomMemoBean.setLocal_path(localaddress);
				mySymptomMemoBean.setUrl(urladdress);
				
				if (mOpenType.equals(SymptomTagConfig.OPEN_TYPE_EDIT)) {
					// mySymptomMemoBean.setUpdateTime(System.currentTimeMillis()+"");
				} else {
					mySymptomMemoBean.setCreateTime(System.currentTimeMillis()
							+ "");
				}
				if (mOpenType.equals(SymptomTagConfig.OPEN_TYPE_CREATE)) {
					isSuccess = DBOperator.insertSymptomMemo(db,
							mySymptomMemoBean);
				} else if (mOpenType.equals(SymptomTagConfig.OPEN_TYPE_EDIT)) {
					// mySymptomMemoBean.setId(Integer.parseInt(id));
					isSuccess = DBOperator.updateSymptomMemo(db,
							mySymptomMemoBean);
				}
				if (isSuccess) {
					mySymptomMemoBean = DBOperator.querySymptomMemoBytime(db,
							(DateFormatUtil.geDataLongZeroClick(Long
									.parseLong(mySymptomMemoBean
											.getCreateTime())) + ""));

					if (localaddress != null && localaddress.length() != 0
							&& !localaddress.equals("null")
							&& mySymptomMemoBean != null) {

						if (oldLocal_url == null
								|| (!oldLocal_url.equals(localaddress))) {
							// mySymptomMemoBean =
							// DBOperator.querySymptomMemoBytime(db,
							// mySymptomMemoBean.getCreateTime());
							boolean imageSuccess = ImageDBOperator
									.updateImageRecord(imagedb,
											mySymptomMemoBean);
							if (imageSuccess) {
								System.out.println(imageSuccess);
								Intent intent = new Intent();
								intent.setAction("com.bravesoft.notifyimageupload");
								SymptomAddActivity.this.sendBroadcast(intent);
							} else {
								System.out.println(imageSuccess);
							}
						}
					}
				}
				if (localaddress == null || localaddress.length() == 0
						|| localaddress.equals("null")) {

					if (mOpenType.equals(SymptomTagConfig.OPEN_TYPE_EDIT)) {
						mySymptomMemoBean = DBOperator.querySymptomMemoById(db,
								id);
					}
					ImageDBOperator.deleteImageRecord(imagedb,
							mySymptomMemoBean);

					System.out.println(ImageDBOperator.isNeedUpload(imagedb));

				}

				finishActivity();
			}

			break;
		case R.id.symptom_add_background:
			CommonUtils.exitKeyboard(this);
			break;
		case R.id.relative_title_other:
			CommonUtils.exitKeyboard(this);
			break;
		case R.id.smyptom_image_scroll:
			CommonUtils.exitKeyboard(this);
			break;
		case R.id.smyptom_image:
			CommonUtils.exitKeyboard(this);
			break;
		case R.id.layout_image_show_content:
			CommonUtils.exitKeyboard(this);
			break;

		default:
			break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {

				if (data == null) {
					return;
				}
				uri = data.getData();
				new Handler().postDelayed(new Runnable() {
					public void run() {

						File path = new File(Utils.getPath(
								getApplicationContext(), uri));
						// ContentResolver cr = this.getContentResolver();
						try {
							if (bmp != null && !bmp.isRecycled())
								bmp.recycle();

							
							int degree = Utils.readPictureDegree(path
									.getAbsolutePath());
							bmp = MyUtils.decodeBitmap(path.getAbsolutePath());
							bmp = Utils.rotaingImageView(degree, bmp);

						} catch (Exception e) {
							e.printStackTrace();
						}
						if (bmp != null) {
							String name = DateFormat.format("yyyyMMdd_hhmmss",
									Calendar.getInstance(Locale.CHINA))
									+ ".jpg";// Name the image, depending on the
												// time
							String filename = null;
							filename = Utils.saveimage(bmp, name);
							if (filename != null) {
								localaddress = filename;
							}

							float imageWidth = bmp.getWidth();
							float imageheight = bmp.getHeight();
							width = ScreenConfig.SCRREN_W
									- LayoutUtils.getRate4density(180);
							float scale = width / imageWidth;
							System.out.println("scale------>" + scale);
							height = (int) (imageheight * scale);
							if (dialog != null) {
								dialog.cancel();
							}
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									LayoutParams params = (LayoutParams) smyptom_image
											.getLayoutParams();
									params.width = width;
									params.height = height;
									// params.topMargin =
									// LayoutUtils.getRate4density(32);
									smyptom_image.setLayoutParams(params);
									layout_symptom_image
											.setVisibility(View.GONE);
									smyptom_aad_line2.setVisibility(View.GONE);
									cancel_smytomimage
											.setVisibility(View.VISIBLE);
									imageScrollView.setVisibility(View.VISIBLE);
									System.out.println("the bmp toString: "
											+ bmp);
									smyptom_image.setImageBitmap(bmp);

								}

							});

						}
					}
				}, 200);

			}
		}

		else if (requestCode == 2) {
			// if(tempFile != null && tempFile.exists()){

			new Handler().postDelayed(new Runnable() {

				public void run() {

					if (tempFile == null
							&& App.getInstance().getTemFileUri() != null
							&& App.getInstance().getTemFileUri().length() != 0) {

						tempFile = new File(App.getInstance().getTemFileUri());
					}

					if (tempFile != null && tempFile.exists()) {
						
						if (bmp != null && !bmp.isRecycled())
							bmp.recycle();
						
						int degree = Utils.readPictureDegree(tempFile
								.getAbsolutePath());
						
						BitmapFactory.Options opt = new BitmapFactory.Options();
						opt.inJustDecodeBounds = true;
						BitmapFactory.decodeFile(tempFile.getAbsolutePath(),
								opt);


						if (degree == 90 || degree == 270) {
							imageWidth = opt.outHeight;
							imageheight = opt.outWidth;
						} else {
							imageWidth = opt.outWidth;
							imageheight = opt.outHeight;
						}
						width = ScreenConfig.SCRREN_W
								- LayoutUtils.getRate4density(180);
						scale = width / imageWidth;
						height = (int) (imageheight * scale);

						
						if (tempFile != null && tempFile.exists()) {
						
							bmp = Utils.readBitmapAutoSize(
									tempFile.getAbsolutePath(), width, height);
							bmp = Utils.rotaingImageView(degree, bmp);

						}
						if (bmp != null) {
							String name = DateFormat.format("yyyyMMdd_hhmmss",
									Calendar.getInstance(Locale.CHINA))
									+ ".jpg";// Name the image, depending on the
												// time
							String filename = null;
							filename = Utils.saveimage(bmp, name);
							if (filename != null) {
								localaddress = filename;
							} else {

							}

							if (tempFile != null) {
								if (tempFile.exists()) {
									tempFile.delete();
								}
							}

							imageWidth = bmp.getWidth();
							imageheight = bmp.getHeight();
							width = ScreenConfig.SCRREN_W
									- LayoutUtils.getRate4density(180);
							scale = width / imageWidth;
							height = (int) (imageheight * scale);
							if (dialog != null) {
								dialog.cancel();
							}

							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									if (dialog != null) {
										dialog.cancel();
									}
									LayoutParams params = (LayoutParams) smyptom_image
											.getLayoutParams();
									params.width = width;
									params.height = height;
									smyptom_image.setLayoutParams(params);
									layout_symptom_image
											.setVisibility(View.GONE);
									smyptom_aad_line2.setVisibility(View.GONE);
									imageScrollView.setVisibility(View.VISIBLE);
									cancel_smytomimage
											.setVisibility(View.VISIBLE);
									smyptom_image.setImageBitmap(bmp);
								}

							});

						}
					} else {
						if (tempFile == null) {
						} else if (!tempFile.exists()) {
						}
					}
					App.getInstance().setTemFileUri(null);
				}
			}, 200);
		} else {
			if (tempFile == null) {
				System.out.println("tempfile ====== null");
			}
		}
		

	}

	public static Bitmap readBitMap(Context context, File tempfile) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		ContentResolver cr = context.getContentResolver();
		InputStream is = null;
		try {
			is = cr.openInputStream(Uri.fromFile(tempfile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return BitmapFactory.decodeStream(is, null, opt);
	}

	// Shear Image

	private void crop(Uri uri, int width, int height) {

		// Crop Image intention

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// The proportion of the crop box, 1: 1
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// Trimmed the size of the output image
		intent.putExtra("outputX", 360);
		intent.putExtra("outputY", 360);

		// intent.putExtra(MediaStore.EXTRA_OUTPUT, uri1);//Transfer large image
		intent.putExtra("scale", true);// Support zoom
		intent.putExtra("scaleUpIfNeeded", true);// Avoid picture cut is too
													// small and filled with
													// black
		intent.putExtra("outputFormat", "JPEG");// Image Format
		intent.putExtra("noFaceDetection", true);// Cancel Face
		intent.putExtra("return-data", true);
		// Open Activity with a return value of the request code
		// PHOTO_REQUEST_CUT

		startActivityForResult(intent, 3);
	}

	private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", outputX);
		intent.putExtra("outputY", outputY);
		intent.putExtra("scaleUpIfNeeded", true);
		intent.putExtra("scale", true);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		intent.putExtra("return-data", false);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		intent.putExtra("noFaceDetection", true); // no face detection
		startActivityForResult(intent, requestCode);
	}

	// private Bitmap decodeUriAsBitmap(Uri uri) {
	// Bitmap bitmap = null;
	// try {
	// bitmap = BitmapFactory.decodeStream(getContentResolver()
	// .openInputStream(uri));
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// return null;
	// }
	// return bitmap;
	// }

	public Bitmap PhotoRotation(Bitmap bm, final int orientationDegree) {
		Matrix m = new Matrix();
		m.setRotate(orientationDegree, (float) bm.getWidth() / 2,
				(float) bm.getHeight() / 2);

		try {
			Bitmap bm1 = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
					bm.getHeight(), m, true);
			return bm1;
		} catch (OutOfMemoryError ex) {
		}
		return null;
	}

	private void upImageFile(String token, String type, File file) {

		System.out.println(file.getAbsolutePath());
		HttpRequest.getInstance().upLoadImage(token, type, file,
				new CommonCallBack<FileUpRequestBean>() {

					@Override
					public void onSuccess(FileUpRequestBean data) {

						mProgressDialog.removeDialog();
						if (data != null) {
							// finish();
							mySymptomMemoBean.setUrl(data.getFile_url());
							boolean isSuccess = DBOperator.updateSymptomMemo(
									db, mySymptomMemoBean);
							if (isSuccess) {
							}
							App.getInstance().setShowCarlendarFragment(true);
							finishActivity();

						} else {

							finishActivity();
						}
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onFailed(Throwable throwable, String reason) {
						mProgressDialog.removeDialog();

						// Log.d("FAILED", throwable+"----"+reason);
					}
				});
	}

}
