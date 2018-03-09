package com.example.fooddeuk.network;

import com.example.fooddeuk.home.HomeEventPictureResponse;
import com.example.fooddeuk.login.LocationResult;
import com.example.fooddeuk.login.User;
import com.example.fooddeuk.login.UserResponse;
import com.example.fooddeuk.order.model.OrderPost;
import com.example.fooddeuk.order.model.OrderResponse;
import com.example.fooddeuk.review.ReviewResponse;
import com.example.fooddeuk.restaurant.model.RestaurantResponse;

import java.util.ArrayList;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

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

    @GET("api/home/readHomeEvent")
    Single<HomeEventPictureResponse> getHomeEvent();

    @GET("api/restaurant/readCurrentLocation")
    Single<RestaurantResponse> getCurrentLocationRestaurant(@QueryMap Map<String,String> queryMap);

    @GET("api/restaurant/readRestaurant")
    Single<RestaurantResponse> getRestaurantByRestId(@Query("rest_id")String rest_id);

    @GET("api/restaurant/getPicture")
    Single<ArrayList<String>> getPicture(@Query("_id")String _id);


    @GET("api/restaurant/getLocationName")
    Single<LocationResult> getLocationNameByNaver(@Query("query")String lnglat);

    //Review
    @GET("api/review/readReview")
    Single<ReviewResponse> getReview(@Query("menu_id") String menu_id);


    //Order
    @POST("api/order/")
    Completable order(@Body OrderPost orderPost);

    @GET("/api/order/getCurrentOrder")
    Single<ArrayList<OrderResponse>> getCurrentOrder(@Query("user_id") String user_id);






}
