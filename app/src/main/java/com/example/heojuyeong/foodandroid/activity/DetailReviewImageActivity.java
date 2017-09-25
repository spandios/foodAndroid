package com.example.heojuyeong.foodandroid.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.heojuyeong.foodandroid.R;
import com.squareup.picasso.Picasso;

public class DetailReviewImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_review_image);
        //X onclick
//        TextView textView=(TextView)findViewById(R.id.);
        ImageView detailReviewImage=(ImageView)findViewById(R.id.detailReviewImage);
        Picasso.with(this).load(getIntent().getStringExtra("reviewImage")).into(detailReviewImage);

    }
}
