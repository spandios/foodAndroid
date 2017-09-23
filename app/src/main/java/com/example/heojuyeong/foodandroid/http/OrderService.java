package com.example.heojuyeong.foodandroid.http;

import com.example.heojuyeong.foodandroid.model.OrderItem;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;

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



    }


}
