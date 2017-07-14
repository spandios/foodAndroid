package com.example.heojuyeong.foodandroid.http;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.orhanobut.logger.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GeocodingThread {

    final static String TAG="Geocoding THREA";
    Context mconext;
    String latlng;
    GeocodingService geocodingService;


    public GeocodingThread(Context mconext, String latlng){
        this.mconext=mconext;
        this.latlng=latlng;
//        this.handler=handler;

    }

}

