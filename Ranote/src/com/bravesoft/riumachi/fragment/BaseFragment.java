package com.bravesoft.riumachi.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.bravesoft.riumachi.layout.LayoutUtils;
import com.bravesoft.riumachi.layout.ScreenConfig;

public abstract class BaseFragment extends Fragment {
	
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    }

	protected <T extends View> T getRateView(int id, boolean isMin) {
		View childView = findViewById(id);
		if (!ScreenConfig.INITBAR) {
			if (!isMin) {
				ScreenConfig.addView(childView);
			} else {
				rateView(childView, isMin);
			}
			ScreenConfig.initBar(getActivity(), childView);
		} else {
			rateView(childView, isMin);
		}
		return (T) childView;
	}

	protected <T extends View> T getTextView(int id, boolean isMin,
			float textSize) {
		View childView = getRateView(id, isMin);
		if (childView instanceof TextView) {
			TextView tv = (TextView) childView;
			LayoutUtils.setTextSize(tv, textSize);
		}
		return (T) childView;
	}

	/**
	 * Control scaled
	 */
	protected void rateView(View v, boolean isMin) {
		LayoutUtils.rateScale(getActivity(), v, isMin);
	}
	
	protected abstract View findViewById(int id);
	

}
