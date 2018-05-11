package com.example.fooddeuk.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

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

  public void onRefresh()
  {
    try {
      int height = 0;
      if (getChildCount() > getCurrentItem()) {
        View child = getChildAt(getCurrentItem());
        child.measure(MeasureSpec.EXACTLY, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int h = child.getMeasuredHeight();
        if(h > height) height = h;
      }

      int heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
      ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
      layoutParams.height = heightMeasureSpec;
      this.setLayoutParams(layoutParams);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public int measureFragment(View view) {
    if (view == null) {
      return 0;
    }

    view.measure(0, 0);
    return view.getMeasuredHeight();
  }

}
