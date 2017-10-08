package com.example.fooddeuk.http;

import com.example.fooddeuk.model.user.UserItem;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by heojuyeong on 2017. 10. 6..
 */

public class UserService {

    interface UserInterface{
        @GET("/api/user/readUser")
        Call<UserItem> getUser(@Query("provider_id")String provider_id);

        @GET("/api/user/readUserCheck")
        Call<Boolean> getUserCheck(@Query("provider_id")String provider_id);

        @FormUrlEncoded
        @POST("/api/user/createUser")
        Call<Void> createUser(@Field("provider_id")String provider_id,@Field("email")String email,@Field("name")String name,@Field("provider")String provider,@Field("fcm_token")String fcm_token);

        @FormUrlEncoded
        @POST("/api/user/updateToken")
        Call<Void> updateToken(@Field("provider_id")String provider_id,@Field("fcm_token")String fcm_token);
    }

    public static Call<Boolean> existUser(String provider_id){
        return RetrofitBase.getInstance().getRetrofit().create(UserInterface.class).getUserCheck(provider_id);
    }

    public static Call<Void> createUser(String provider_id,String email,String name,String provider,String fcm_token){
        return RetrofitBase.getInstance().getRetrofit().create(UserInterface.class).createUser(provider_id,email,name,provider,fcm_token);
    }


    public static Call<UserItem> getUser(String provider_id){
        return RetrofitBase.getInstance().getRetrofit().create(UserInterface.class).getUser(provider_id);
    }

    public static Call<Void> updateToken(String provider_id,String fcm_token){
        return RetrofitBase.getInstance().getRetrofit().create(UserInterface.class).updateToken(provider_id,fcm_token);
    }

}
