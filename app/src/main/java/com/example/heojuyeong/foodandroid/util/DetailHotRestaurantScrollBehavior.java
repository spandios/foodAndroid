package com.example.heojuyeong.foodandroid.util;

import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.view.View;

/**
 * Created by heojuyeong on 2017. 9. 13..
 */

public class DetailHotRestaurantScrollBehavior extends CoordinatorLayout.Behavior<View>{

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View directTargetChild, View target, int nestedScrollAxes) {

        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;

    }
}
