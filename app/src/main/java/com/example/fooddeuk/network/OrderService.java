package com.example.fooddeuk.network;

import com.example.fooddeuk.model.order.OrderResponse;

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


    interface OrderInterFace {
//        Retrofit retrofit = new retrofit2.Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:3000")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

        @POST("api/order/")
        Call<Void> order(@Body OrderResponse orderResponse);

        @GET("/api/order/getCurrentOrder")
        Call<ArrayList<OrderResponse>> getCurrentOrder(@Query("user_id") String user_id);


    }


    public static Call<Void> order(OrderResponse orderResponse) {
        OrderInterFace orderInterFace = RetrofitBase.getInstance().getRetrofit().create(OrderInterFace.class);
        Call<Void> call = orderInterFace.order(orderResponse);
        return call;
    }

    public static Call<ArrayList<OrderResponse>> getCurrentOrder(String user_id) {
        return RetrofitBase.getInstance().getRetrofit().create(OrderInterFace.class).getCurrentOrder(user_id);
    }


}


