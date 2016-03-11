package com.bravesoft.riumachi.layout;

import android.view.View;
import android.view.ViewTreeObserver;

public class RealSize {

	public static void setPt(final SizeListener listener, final View view) {
		view.getViewTreeObserver().addOnPreDrawListener(
				new ViewTreeObserver.OnPreDrawListener() {

					@Override
					public boolean onPreDraw() {
						listener.setSize(view.getWidth(), view.getHeight());
						view.getViewTreeObserver()
								.removeOnPreDrawListener(this);
						return false;
					}
				});
	}

	public interface SizeListener {
		void setSize(int width, int height);
	}
}
