package com.example.fooddeuk.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.orhanobut.logger.Logger;

/**
 * Created by heo on 2018. 2. 15..
 */


public class SwipeViewPager extends ViewPager {
    private boolean isSlide = false;


    public SwipeViewPager(Context context) {
        super(context);
    }

    public SwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Logger.d(isSlide);
        return isSlide && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isSlide && super.onInterceptTouchEvent(ev);
    }

    public void setIsSlide(boolean isSlide) {
        this.isSlide=isSlide;
    }
}


