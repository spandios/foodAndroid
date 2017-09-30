package com.example.heojuyeong.foodandroid.http;

import com.example.heojuyeong.foodandroid.model.restaurant.RestaurantItem;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by heojuyeong on 2017. 7. 19..
 */

public class RestaurantService {
    //                .baseUrl("http://10.0.2.2:3000")
        private interface CurrentLocationListInterface {
        @GET("api/restaurant/readCurrentLocation")
        Call<RestaurantItem> getCurrentLocationListItem(@Query("curLat") double curLat, @Query("curLng") double curLng, @Query("maxDistance") int maxDistance, @Query("foodtype") String foodtype);

    }

    public static Call<RestaurantItem> getCurrentLocationRestaurant(double curLat, double curLng, int maxDistance, String foodtype){

        CurrentLocationListInterface currentLocationListInterface= RetrofitBase.getInstance().getRetrofit().create(CurrentLocationListInterface.class);
        Call<RestaurantItem> call=currentLocationListInterface.getCurrentLocationListItem(curLat,curLng,maxDistance,foodtype);
        return call;

    }







}

