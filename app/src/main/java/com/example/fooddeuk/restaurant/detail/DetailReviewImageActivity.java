package com.example.fooddeuk.restaurant.detail;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.fooddeuk.R;
import com.example.fooddeuk.activity.BaseActivity;
import com.squareup.picasso.Picasso;

public class DetailReviewImageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_review_image);
        //X onclick
//        TextView textView=(TextView)findViewById(R.id.);
        ImageView detailReviewImage= findViewById(R.id.detailReviewImage);
        Picasso.with(this).load(getIntent().getStringExtra("reviewImage")).into(detailReviewImage);

    }
}
