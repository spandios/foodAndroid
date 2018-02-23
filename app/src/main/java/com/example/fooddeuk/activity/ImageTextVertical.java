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

public class ImageTextVertical extends LinearLayout {
    private TextView textView;
    private ImageView imageView;

    public ImageTextVertical(Context context) {
        super(context);
        initView();
    }

    public ImageTextVertical(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public ImageTextVertical(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttrs(attrs,defStyleAttr);
    }


    private void initView() {
        inflate(getContext(),R.layout.image_text_vertical,this);
        imageView = findViewById(R.id.image);
        textView = findViewById(R.id.text);

    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ImageTextVertical);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ImageTextVertical, defStyle, 0);
        setTypeArray(typedArray);
    }

    private void setTypeArray(TypedArray typedArray) {
        int bg_resID = typedArray.getResourceId(R.styleable.ImageTextVertical_image, 0);
        imageView.setBackgroundResource(bg_resID);
        int textColor = typedArray.getColor(R.styleable.ImageTextVertical_textColor, getResources().getColor(R.color.text_sub));
        textView.setTextColor(textColor);
        String text_string = typedArray.getString(R.styleable.ImageTextVertical_text);
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
