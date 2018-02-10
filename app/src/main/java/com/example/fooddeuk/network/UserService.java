package com.example.fooddeuk.network;

import com.example.fooddeuk.model.user.User;
import com.example.fooddeuk.model.user.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by heojuyeong on 2017. 10. 6..
 */

public class UserService {

    public interface UserInterface{
        @GET("/api/user/readUser")
        Call<UserResponse> getUser(@Query("provider_id")String provider_id);


        @POST("/api/user/createUser")
        Call<Void> createUser(@Body User user);


        @FormUrlEncoded
        @POST("/api/user/updateToken")
        Call<Void> updateToken(@Field("provider_id")String provider_id,@Field("fcm_token")String fcm_token);
    }




}
