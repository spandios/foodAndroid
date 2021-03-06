package com.example.fooddeuk.activity;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fooddeuk.R;

/**
 * Created by heo on 2018. 2. 22..
 */

public class CustomImageTextVertical extends LinearLayout {
    private TextView textView;
    private ImageView imageView;

    public CustomImageTextVertical(Context context) {
        super(context);
        initView();
    }

    public CustomImageTextVertical(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public CustomImageTextVertical(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttrs(attrs,defStyleAttr);
    }


    private void initView() {
        inflate(getContext(),R.layout.custom_img_txt_vertical,this);
        imageView = findViewById(R.id.image);
        textView = findViewById(R.id.text);

    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomImageTextVertical);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomImageTextVertical, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        int bg_resID = typedArray.getResourceId(R.styleable.CustomImageTextVertical_image, 0);
        imageView.setBackgroundResource(bg_resID);
        int textColor = typedArray.getColor(R.styleable.CustomImageTextVertical_textColor, getResources().getColor(R.color.text_main));
        textView.setTextColor(textColor);
        String text_string = typedArray.getString(R.styleable.CustomImageTextVertical_text);
        textView.setText(text_string);
        typedArray.recycle();
    }

    public void setTextColor(int textColor){
        textView.setTextColor(textColor);
    }

    public void setText(String text){
        textView.setText(text);
    }

    public void setImage(Drawable image){
        imageView.setImageDrawable(image);
    }

}
