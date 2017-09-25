package com.example.heojuyeong.foodandroid.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.heojuyeong.foodandroid.R;
import com.squareup.picasso.Picasso;

public class IntroActivity extends AppCompatActivity{

    private Handler handler;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(IntroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };

    public void init() {

        handler = new Handler();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ImageView introImageView = (ImageView) findViewById(R.id.introImageView);
        Picasso.with(this)
                .load(R.drawable.intro)
                .into(introImageView);
        init();
        handler.postDelayed(runnable, 2500);

    }


    @Override
    public void onBackPressed(){
        super.onBackPressed();
        handler.removeCallbacks(runnable);
    }

}



