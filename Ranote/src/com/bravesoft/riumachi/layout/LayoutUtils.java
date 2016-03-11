package com.bravesoft.riumachi.layout;

import com.bravesoft.riumachi.appliction.App;
import com.bravesoft.riumachi.constant.TextTypeFace;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class LayoutUtils {
	
    public static final int TYPEFACE_ROBOTO_REGULAR = 300; //Digital normal
    public static final int TYPEFACE_ROBOTO_BOLD = 400; //Digital bold
    
	/**
	 * 
	 * @param context
	 * @param v
	 */
	public static void rateScale(Context context, View v, boolean isMin) {
		if (v == null)
			return;
		if (!ScreenConfig.INIT)
			ScreenConfig.init(context);
		if (v.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
			RelativeLayout.LayoutParams params = (LayoutParams) v
					.getLayoutParams();
			params.width = params.width <= 0 ? params.width
					: getRate4density(params.width);
			if (!isMin) {
				params.height = params.height <= 0 ? params.height
						: (int) (params.height * ScreenConfig.RATE_H + 0.5f)
								- ScreenConfig.STATUSBARHEIGHT / 2;
			} else {
				params.height = params.height <= 0 ? params.height
						: getRate4density(params.height);
			}
			params.leftMargin = params.leftMargin == 0 ? params.leftMargin
					: getRate4density(params.leftMargin);
			params.rightMargin = params.rightMargin == 0 ? params.rightMargin
					: getRate4density(params.rightMargin);
			params.topMargin = params.topMargin == 0 ? params.topMargin
					: getRate4density(params.topMargin);
			params.bottomMargin = params.bottomMargin == 0 ? params.bottomMargin
					: getRate4density(params.bottomMargin);
			v.setLayoutParams(params);

		} else if (v.getLayoutParams() instanceof LinearLayout.LayoutParams) {
			LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) v
					.getLayoutParams();
			params.width = params.width <= 0 ? params.width
					: getRate4density(params.width);
			if (!isMin) {
				params.height = params.height <= 0 ? params.height
						: (int) (params.height * ScreenConfig.RATE_H + 0.5f)
								- ScreenConfig.STATUSBARHEIGHT / 2;
			} else {
				params.height = params.height <= 0 ? params.height
						: getRate4density(params.height);
			}
			params.leftMargin = params.leftMargin == 0 ? params.leftMargin
					: getRate4density(params.leftMargin);
			params.rightMargin = params.rightMargin == 0 ? params.rightMargin
					: getRate4density(params.rightMargin);

			params.topMargin = params.topMargin == 0 ? params.topMargin
					: getRate4density(params.topMargin);

			params.bottomMargin = params.bottomMargin == 0 ? params.bottomMargin
					: getRate4density(params.bottomMargin);
			v.setLayoutParams(params);
		}
		v.setPadding(getRate4density(v.getPaddingLeft()),
				getRate4density(v.getPaddingTop()),
				getRate4density(v.getPaddingRight()),
				getRate4density(v.getPaddingBottom()));

	}

	/**
	 * 
	 * @param context
	 * @param v
	 */
	public static void rateScaleAndMargin(Context context, View v, boolean isMin) {
		if (v == null)
			return;
		if (!ScreenConfig.INIT)
			ScreenConfig.init(context);
		if (v.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
			RelativeLayout.LayoutParams params = (LayoutParams) v
					.getLayoutParams();
			params.width = params.width <= 0 ? params.width
					: getRate4density(params.width);
			if (!isMin) {
				params.height = params.height <= 0 ? params.height
						: (int) (params.height * ScreenConfig.RATE_H + 0.5f)
								- ScreenConfig.STATUSBARHEIGHT / 2;
			} else {
				params.height = params.height <= 0 ? params.height
						: getRate4density(params.height);
			}
			params.leftMargin = params.leftMargin == 0 ? params.leftMargin
					: getRate4density(params.leftMargin);
			params.rightMargin = params.rightMargin == 0 ? params.rightMargin
					: getRate4density(params.rightMargin);
			params.topMargin = params.topMargin == 0 ? params.topMargin
					: getRate4densityH(params.topMargin);
			params.bottomMargin = params.bottomMargin == 0 ? params.bottomMargin
					: getRate4densityH(params.bottomMargin);
			v.setLayoutParams(params);

		} else if (v.getLayoutParams() instanceof LinearLayout.LayoutParams) {
			LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) v
					.getLayoutParams();
			params.width = params.width <= 0 ? params.width
					: getRate4density(params.width);
			if (!isMin) {
				params.height = params.height <= 0 ? params.height
						: (int) (params.height * ScreenConfig.RATE_H + 0.5f)
								- ScreenConfig.STATUSBARHEIGHT / 2;
			} else {
				params.height = params.height <= 0 ? params.height
						: getRate4density(params.height);
			}
			params.leftMargin = params.leftMargin == 0 ? params.leftMargin
					: getRate4density(params.leftMargin);
			params.rightMargin = params.rightMargin == 0 ? params.rightMargin
					: getRate4density(params.rightMargin);

			params.topMargin = params.topMargin == 0 ? params.topMargin
					: getRate4densityH(params.topMargin);

			params.bottomMargin = params.bottomMargin == 0 ? params.bottomMargin
					: getRate4densityH(params.bottomMargin);
			v.setLayoutParams(params);
		}
		v.setPadding(getRate4density(v.getPaddingLeft()),
				getRate4density(v.getPaddingTop()),
				getRate4density(v.getPaddingRight()),
				getRate4density(v.getPaddingBottom()));

	}

	public static int getRate4density(float value) {
		return (int) (value * ScreenConfig.RATE_W + 0.5f);
	}

	public static int getRate4densityH(float value) {
		return (int) (value * ScreenConfig.RATE_H + 0.5f);
	}

	public static int getRate4px(float value) {
		return (int) (value * ScreenConfig.ABS_RATEW + 0.5f);
	}

	public static int getRate4pxH(float value) {
		return (int) (value * ScreenConfig.ABS_RATEH + 0.5f);
	}

	public static void setTextSize(TextView tv, float value) {
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getRate4px(value));
	}
	
	public static void setTextSize(TextView tv, float value ,int typeFace) {
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, getRate4px(value));
		switch (typeFace) {
		case TextTypeFace.TYPEFACE_ROBOTO_REGULAR:
			tv.setTypeface(App.getInstance().getmTypefacRobotoRegular());
			break;
		case TextTypeFace.TYPEFACE_ROBOTO_BOLD:
			tv.setTypeface(App.getInstance().getmTypefacRobotoBold());
			break;
		case TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR:
			tv.setTypeface(App.getInstance().getmTypefacGenShinGothicRegular());
			break;
		case TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD:
			tv.setTypeface(App.getInstance().getmTypeGenShinGothicBold());
			break;

		default:
			break;
		}
	}

}
