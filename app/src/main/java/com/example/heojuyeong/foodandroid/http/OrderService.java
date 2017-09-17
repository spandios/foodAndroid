package com.example.heojuyeong.foodandroid.http;

import com.example.heojuyeong.foodandroid.item.MenuItem;
import com.example.heojuyeong.foodandroid.item.OrderItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by heojuyeong on 2017. 9. 13..
 */

public class OrderService {


    interface OrderInterFace{
        @POST("api/order/")
//        int menu_id;
//        int rest_id;
//        int user_id;
//        int quantity;
//        int status;
//        String arrived_time;
//        String message;
//        String payment;
        Call<OrderItem> order(@Field("menu_id")int menu_id,
                                @Field("rest_id") int rest_id,
                                @Field("user_id")int user_id,
                                @Field("quantity")int quantity,
                                @Field("message")String message,
                                @Field("payment")String payment,
                                @Field("arrived_time")String arrived_time,
                                @Field("status")int status
                                );
        Call<OrderItem> order2(@Body OrderItem orderItem);






        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://13.124.97.184")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
