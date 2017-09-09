package com.example.heojuyeong.foodandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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
