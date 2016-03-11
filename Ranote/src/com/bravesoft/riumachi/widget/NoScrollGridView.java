package com.bravesoft.riumachi.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * OnMeasure class overrides the method, make GridView fully deployed, and can not slide, here for the grid layout ScrollView
 * 
 * @author ZhouPeng
 *
 */
public class NoScrollGridView extends GridView {

	public NoScrollGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public NoScrollGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public NoScrollGridView(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
