package com.example.fooddeuk.order.model;

import com.example.fooddeuk.order_history.OrderMenuItem;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/**
 * Created by heo on 2018. 1. 22..
 */

public class OrderResponse {

    public RestaurantSide restaurant;
    public UserSide user;
    public ArrayList<OrderMenuItem> orderMenuItems;
    public String status;

    @SerializedName("_id")
    public String id;
    private String created_at;

    public OrderResponse(RestaurantSide restaurant, UserSide user, ArrayList<OrderMenuItem> orderMenuItems, String status) {
        this.restaurant = restaurant;
        this.user = user;
        this.orderMenuItems = orderMenuItems;
        this.status = status;
    }


    public String getCreated_at() {
        return "주문시간 : " +created_at.replace("T", " ").substring(0,16);
    }
}
