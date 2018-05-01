package com.example.fooddeuk.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.orhanobut.logger.Logger;

/**
 * Created by heo on 2017. 11. 6..
 */


public class WrapPager extends ViewPager {

  private View mCurrentView;
  private int mCurrentPosition;
  private int width = 0;

  public WrapPager(Context context) {
    super(context);
  }

  public WrapPager(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//    View child = getChildAt(getCurrentItem());
//    if (child != null) {
//      child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//      int h = child.getMeasuredHeight();
//      heightMeasureSpec = MeasureSpec.makeMeasureSpec(h, MeasureSpec.EXACTLY);
//    }
//    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int height = 0;
    width = widthMeasureSpec;
    if (getChildCount() > getCurrentItem()) {
      View child = getChildAt(getCurrentItem());
      child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
      int h = child.getMeasuredHeight();
      if(h > height) height = h;
    }

    heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

  }

  public void measureCurrentView(View currentView) {
    mCurrentView = currentView;
  }

  public void initPageChangeListener() {
    addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
      @Override
      public void onPageSelected(int position) {
        Logger.d("request layout");

      }
    });
  }


  public int measureFragment(View view) {
      if (view == null) {
        return 0;
      }

    view.measure(0, 0);
    return view.getMeasuredHeight();
  }

  public void onRefresh()
  {
    try {
      int height = 0;
      if (getChildCount() > getCurrentItem()) {
        View child = getChildAt(getCurrentItem());
        child.measure(width, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
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
}

