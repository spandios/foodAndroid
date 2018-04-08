package com.example.fooddeuk.network;

import com.example.fooddeuk.home.HomeEventPictureResponse;
import com.example.fooddeuk.menu.model.Menu;
import com.example.fooddeuk.order.model.OrderPost;
import com.example.fooddeuk.order.model.OrderResponse;
import com.example.fooddeuk.restaurant.model.Restaurant;
import com.example.fooddeuk.restaurant.model.RestaurantResponse;
import com.example.fooddeuk.review.ReviewResponse;
import com.example.fooddeuk.user.LocationResult;
import com.example.fooddeuk.user.User;
import com.example.fooddeuk.user.UserResponse;
import io.reactivex.Completable;
import io.reactivex.Single;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    @POST("/api/user/createDangol")
    Completable createDangol(@Field("user_id")String user_id,@Field("rest_id")String rest_id);

    @FormUrlEncoded
    @POST("/api/user/deleteDangol")
    Completable deleteDangol(@Field("user_id")String user_id,@Field("rest_id")String rest_id);

    @FormUrlEncoded
    @POST("/api/user/updateToken")
    Completable updateToken(@Field("provider_id")String provider_id, @Field("fcm_token")String fcm_token);

    //HOME

    @GET("api/home/readHomeEvent")
    Single<HomeEventPictureResponse> getHomeEvent();


    //Restaurant

    @GET("api/restaurant/readCurrentLocation")
    Single<RestaurantResponse> getCurrentLocationRestaurant(@QueryMap Map<String,String> queryMap);

    @GET("api/restaurant/readRestaurant")
    Single<RestaurantResponse> getRestaurantByRestId(@Query("rest_id")String rest_id);

    @GET("api/restaurant/readRestaurant")
    Single<RestaurantResponse> getRestaurantById(@Query("restaurant_id")String _id);

    @FormUrlEncoded
    @POST("api/restaurant/getDangolRestaurant")
    Single<ArrayList<Restaurant>> getDangolRestaurant(
        @Field("rest_id[]") ArrayList<String> rest_id);

    @GET("api/restaurant/getHotRestaurant")
    Single<List<Restaurant>> getHotRestaurant(@Query("lat")Double lat,@Query("lng")Double lng);

    @GET("api/restaurant/searchRestaurant")
    Single<RestaurantResponse> searchRestaurant(@Query("lat")Double lat, @Query("lng")Double lng, @Query("searchText")String searchText);

    @GET("api/restaurant/getPicture")
    Single<ArrayList<String>> getPicture(@Query("_id")String _id);


    @GET("api/restaurant/getLocationName")
    Single<LocationResult> getLocationNameByNaver(@Query("query")String lnglat);

  @GET("api/restaurant/getLocationName")
  Single<LocationResult> getLocationNameByNaver(@Query("query")String lnglat,@Query("dong")Boolean dong);

  @GET("api/menu/getHotMenu")
  Single<List<Menu>> getHotMenu(@Query("lat") Double lat, @Query("lng") Double lng);

    //Review
    @GET("api/review/readReview")
    Single<ReviewResponse> getReview(@Query("menu_id") String menu_id);

    //Order
    @POST("api/order/")
    Completable order(@Body OrderPost orderPost);

    @GET("/api/order/getCurrentOrder")
    Single<ArrayList<OrderResponse>> getCurrentOrder(@Query("user_id") String user_id);






}
