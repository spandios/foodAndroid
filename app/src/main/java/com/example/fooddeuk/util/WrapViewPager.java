package com.example.fooddeuk.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by heo on 2018. 4. 30..
 */

public class WrapViewPager extends ViewPager {

  public WrapViewPager(Context context) {
    super(context);
  }

  WrapViewPager(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    boolean wrapHeight = MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST;

    int width = getMeasuredWidth();
    if (wrapHeight) {
      // Keep the current measured width.
      widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
    }

    int fragmentHeight = measureFragment(
        ((Fragment) getAdapter().instantiateItem(this, getCurrentItem())).getView());
    heightMeasureSpec = MeasureSpec.makeMeasureSpec(fragmentHeight, MeasureSpec.AT_MOST);

    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  public int measureFragment(View view) {
    if (view == null) {
      return 0;
    }

    view.measure(0, 0);
    return view.getMeasuredHeight();
  }

}
