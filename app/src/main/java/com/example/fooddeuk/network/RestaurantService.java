package com.example.fooddeuk.network;

import com.example.fooddeuk.model.restaurant.RestaurantResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by heojuyeong on 2017. 7. 19..
 */

public class RestaurantService {
    //                .baseUrl("http://10.0.2.2:3000")
        private interface RestaurantInterface {

        @GET("api/restaurant/readCurrentLocation")
        Call<RestaurantResponse> getCurrentLocationListItem(@Query("curLat") double curLat, @Query("curLng") double curLng, @Query("maxDistance") int maxDistance, @Query("foodtype") String foodtype, @Query("filter")String filter, @Query("rest_name")String rest_name);

        @GET("api/restaurant/readRestaurant")
        Call<RestaurantResponse> getRestaurantByRestId(@Query("rest_id")int rest_id);

        @GET("api/restaurant/getPicture")
        Call<ArrayList<String>> getPicture(@Query("rest_id")String rest_id);

    }

    public static Call<RestaurantResponse> getCurrentLocationRestaurant(double curLat, double curLng, int maxDistance, String foodtype, String filter, String rest_name){
        return RetrofitBase.getInstance().getRetrofit().create(RestaurantInterface.class).getCurrentLocationListItem(curLat,curLng,maxDistance,foodtype,filter,rest_name);

    }

    public static Call<RestaurantResponse> getRestaurantById(int rest_id){
        return RetrofitBase.getInstance().getRetrofit().create(RestaurantInterface.class).getRestaurantByRestId(rest_id);
    }

    public static Call<ArrayList<String>> getPicture(String rest_id){
        return RetrofitBase.getInstance().getRetrofit().create(RestaurantInterface.class).getPicture(rest_id);
    }








}

