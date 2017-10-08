package com.example.fooddeuk.http;

import com.example.fooddeuk.model.menu.OrderItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by heojuyeong on 2017. 9. 13..
 */

public class OrderService {


    interface OrderInterFace{
//        Retrofit retrofit = new retrofit2.Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:3000")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

        @POST("api/order/")
        Call<OrderItem> order(@Body OrderItem orderItem);

        @GET("/api/order/getCurrentOrder")
        Call<ArrayList<OrderItem>> getCurrentOrder(@Query("user_id")int user_id);


    }


    public static Call<OrderItem> order(OrderItem orderItem){
        OrderInterFace orderInterFace= RetrofitBase.getInstance().getRetrofit().create(OrderInterFace.class);
        Call<OrderItem> call=orderInterFace.order(orderItem);
        return call;
    }

    public static Call<ArrayList<OrderItem>> getCurrentOrder(int user_id){
        return RetrofitBase.getInstance().getRetrofit().create(OrderInterFace.class).getCurrentOrder(user_id);
    }


    }


