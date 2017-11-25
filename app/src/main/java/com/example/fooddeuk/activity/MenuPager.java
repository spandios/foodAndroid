package com.example.fooddeuk.activity;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by heo on 2017. 11. 6..
 */


public class MenuPager extends ViewPager{

    private View mCurrentView;


    public MenuPager(Context context) {
        super(context);
    }

    public MenuPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) height = h;
        }

        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

//    @Override
//    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        if (mCurrentView == null) {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//            return;
//        }
//        int height = 0;
//        mCurrentView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//        int h = mCurrentView.getMeasuredHeight();
//        if (h > height) height = h;
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
//
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//    }

    public void measureCurrentView(View currentView) {
        mCurrentView = currentView;
        requestLayout();
    }

    public int measureFragment(View view) {
        if (view == null)
            return 0;

        view.measure(0, 0);
        return view.getMeasuredHeight();
    }
}

