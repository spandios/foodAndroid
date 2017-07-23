package com.example.heojuyeong.foodandroid.http;

import com.example.heojuyeong.foodandroid.listview.CurrentLocationListItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by heojuyeong on 2017. 7. 19..
 */

public class CurrentLocationListSerivce {

        public interface CurrentLocationListInterface{
        @GET("api/restaurant/readCurrentLocation")
        Call<CurrentLocationListItem> getCurrentLocationListItem(@Query("curLat") double curLat, @Query("curLng") double curLng, @Query("maxDistance") int maxDistance, @Query("foodtype") String foodtype);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://13.124.115.29")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }


    public Call<CurrentLocationListItem> getCall(double curLat, double curLng, int maxDistance, String foodtype){
        CurrentLocationListSerivce.CurrentLocationListInterface currentLocationListInterface=CurrentLocationListSerivce.CurrentLocationListInterface.retrofit.create(CurrentLocationListSerivce.CurrentLocationListInterface.class);
        Call<CurrentLocationListItem> call=currentLocationListInterface.getCurrentLocationListItem(curLat,curLng,maxDistance,foodtype);
        return call;

    }

    public interface CurrentLocationItemCallBack{
        public void getItem(ArrayList<CurrentLocationListItem> item);
    }





}

