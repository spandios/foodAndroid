package com.example.heojuyeong.foodandroid.http;

import com.example.heojuyeong.foodandroid.item.CurrentLocationListItem;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by heojuyeong on 2017. 7. 19..
 */

public class CurrentLocationListSerivce {
    //                .baseUrl("http://10.0.2.2:3000")
        private interface CurrentLocationListInterface{
        @GET("api/restaurant/readCurrentLocation")


        Call<CurrentLocationListItem> getCurrentLocationListItem(@Query("curLat") double curLat, @Query("curLng") double curLng, @Query("maxDistance") int maxDistance, @Query("foodtype") String foodtype);

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://13.124.97.184")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }


    public Call<CurrentLocationListItem> getCall(double curLat, double curLng, int maxDistance, String foodtype){
        CurrentLocationListInterface currentLocationListInterface= CurrentLocationListInterface.retrofit.create(CurrentLocationListInterface.class);
        Call<CurrentLocationListItem> call=currentLocationListInterface.getCurrentLocationListItem(curLat,curLng,maxDistance,foodtype);
        return call;

    }

    public interface CurrentLocationItemCallBack{
        public void getItem(ArrayList<CurrentLocationListItem> item);
    }





}

