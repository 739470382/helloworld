package com.bravesoft.riumachi.layout;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;

public class ScreenConfig {
	/**
	 * Width ratio and 1280 x 720
	 */
	public static float RATE_W;
	/**
	 * Height ratio and 1280 x 720
	 */
	public static float RATE_H; //
	public static float MINRATE; // min
	public static boolean INIT = false;
	public static boolean INITBAR = false;

	public static float ABS_RATEW;
	public static float ABS_RATEH;
	/**
	 * The width of the current screen
	 */
	public static int SCRREN_W;
	/**
	 * The current height of the screen
	 */
	public static int SCRREN_H;
	/**
	 * The height of the status bar
	 */
	public static int STATUSBARHEIGHT;
	/**
	 * density
	 * 
	 */
	public static float DENSITY;

	private static float RATIOWIDTH = 720;

	private static float RATIONHEIGHT = 1280;

	public static void setRatioScreen(int width, int height) {
		RATIOWIDTH = width;
		RATIONHEIGHT = height;
	}

	public static void init(Context context) {
		//  1280*800
		if (!INIT) {
			DisplayMetrics dm = context.getResources().getDisplayMetrics();
			SCRREN_W = dm.widthPixels;
			SCRREN_H = dm.heightPixels;
			DENSITY = dm.density;
			ABS_RATEW = SCRREN_W / RATIOWIDTH;
			ABS_RATEH = SCRREN_H / RATIONHEIGHT;
			RATE_W = SCRREN_W / (RATIOWIDTH * DENSITY);
			RATE_H = SCRREN_H / (RATIONHEIGHT * DENSITY);
			MINRATE = Math.min(RATE_H, RATE_W);
			STATUSBARHEIGHT = (int) (26 * DENSITY);
			INIT = true;
		}
	}

	public static void setStatusHeight(int height) {
		STATUSBARHEIGHT = height;
		if (STATUSBARHEIGHT != 0) {
			INITBAR = true;
		}
	}

	private static int count = 0;

	private static List<View> views = new ArrayList<View>();
	private static List<View> views2 = new ArrayList<View>();

	public static void addView(View view) {
		views.add(view);
	}

	public static void addMargView(View view) {
		views2.add(view);
	}

	private static void rateViews(Context context) {
		int len = views.size();
		for (int i = 0; i < len; i++) {
			LayoutUtils.rateScale(context, views.get(i), false);
		}
		views.clear();
	}

	private static void rateViews2(Context context) {
		if (views2 == null || views2.size() == 0)
			return;
		int len = views2.size();
		for (int i = 0; i < len; i++) {
			LayoutUtils.rateScaleAndMargin(context, views2.get(i), false);
		}
		views2.clear();
	}

	public static void initBar(final Activity activity, final View v) {
		if (count != 0)
			return;
		count++;
		v.getViewTreeObserver().addOnPreDrawListener(
				new ViewTreeObserver.OnPreDrawListener() {

					@Override
					public boolean onPreDraw() {
						if (!INITBAR) {
							Rect rect = new Rect();
							activity.getWindow().getDecorView()
									.getWindowVisibleDisplayFrame(rect);
							setStatusHeight(rect.top);
							rateViews(activity);
							rateViews2(activity);
						}
						v.getViewTreeObserver().removeOnPreDrawListener(this);
						return false;
					}
				});
	}
}
