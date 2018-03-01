package com.example.fooddeuk.util;

import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;

/**
 * Created by heo on 2018. 2. 22..
 */

public class CustomBehavior extends CoordinatorLayout.Behavior<View>{

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {

        return super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
    }


}
