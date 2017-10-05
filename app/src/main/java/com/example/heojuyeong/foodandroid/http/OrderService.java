package com.example.heojuyeong.foodandroid.http;

import com.example.heojuyeong.foodandroid.model.OrderItem;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

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


    }


    public static Call<OrderItem> order(OrderItem orderItem){
        OrderInterFace orderInterFace= RetrofitBase.getInstance().getRetrofit().create(OrderInterFace.class);
        Call<OrderItem> call=orderInterFace.order(orderItem);
        return call;
    }


    }


