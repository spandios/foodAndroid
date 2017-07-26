package com.example.heojuyeong.foodandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.heojuyeong.foodandroid.listview.CurrentLocationListItem;

import java.io.Serializable;

public class DetailRestaurantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_restaurant);
        CurrentLocationListItem.Restaurant restaurant=(CurrentLocationListItem.Restaurant) getIntent().getExtras().get("serialData");


        Toast.makeText(this,restaurant.getName(),Toast.LENGTH_SHORT).show();

    }
}
