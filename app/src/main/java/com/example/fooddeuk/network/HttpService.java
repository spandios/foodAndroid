package com.example.fooddeuk.network;

import com.example.fooddeuk.model.menu.ReviewItem;
import com.example.fooddeuk.model.order.OrderResponse;
import com.example.fooddeuk.model.restaurant.RestaurantResponse;
import com.example.fooddeuk.model.user.User;
import com.example.fooddeuk.model.user.UserResponse;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by heo on 2018. 2. 10..
 */

public interface HttpService {

    //User--
    @GET("/api/user/readUser")
    Single<UserResponse> getUser(@Query("provider_id")String provider_id);

    @POST("/api/user/createUser")
    Completable createUser(@Body User user);

    @FormUrlEncoded
    @POST("/api/user/updateToken")
    Completable updateToken(@Field("provider_id")String provider_id, @Field("fcm_token")String fcm_token);


    //Restaurant--
    @GET("api/restaurant/readCurrentLocation")
    Single<RestaurantResponse> getCurrentLocationListItem(@Query("curLat") double curLat, @Query("curLng") double curLng, @Query("maxDistance") int maxDistance, @Query("foodtype") String foodtype, @Query("filter")String filter, @Query("rest_name")String rest_name);

    @GET("api/restaurant/readRestaurant")
    Single<RestaurantResponse> getRestaurantByRestId(@Query("rest_id")String rest_id);

    @GET("api/restaurant/getPicture")
    Single<ArrayList<String>> getPicture(@Query("rest_id")String rest_id);


    //Review
    @GET("api/review/readReview")
    Single<ArrayList<ReviewItem>> getReview(@Query("menu_id") String menu_id);


    //Order
    @POST("api/order/")
    Completable order(@Body OrderResponse orderResponse);

    @GET("/api/order/getCurrentOrder")
    Single<ArrayList<OrderResponse>> getCurrentOrder(@Query("user_id") String user_id);






}