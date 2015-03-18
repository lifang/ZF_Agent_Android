package com.examlpe.zf_android.util;

import android.widget.ListView;


public class ScrollViewWithListView extends ListView {
	public ScrollViewWithListView(android.content.Context context, android.util.AttributeSet attrs) {
		super(context, attrs);
	}

	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
