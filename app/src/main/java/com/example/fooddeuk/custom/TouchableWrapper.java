package com.example.fooddeuk.custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by heo on 2018. 3. 22..
 */

public class TouchableWrapper extends FrameLayout {
  public Boolean isTouched;
  public TouchableWrapper(@NonNull Context context) {
    super(context);
  }

  public TouchableWrapper(@NonNull Context context,
      @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public TouchableWrapper(@NonNull Context context, @Nullable AttributeSet attrs,
      int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  public TouchableWrapper(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr,
      int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {

    switch (ev.getAction()) {
      case MotionEvent.ACTION_DOWN:
        isTouched=true;
        break;
      case MotionEvent.ACTION_UP:
        isTouched = false;

        break;
    }

    return super.dispatchTouchEvent(ev);

  }

}
