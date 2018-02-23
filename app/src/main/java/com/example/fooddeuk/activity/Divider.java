package com.example.fooddeuk.activity;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.fooddeuk.R;

/**
 * Created by heo on 2018. 2. 22..
 */

public class Divider extends View {
    public Divider(Context context) {
        super(context);
        inflate(getContext(), R.layout.custom_divider,null);
    }

    public Divider(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Divider(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
