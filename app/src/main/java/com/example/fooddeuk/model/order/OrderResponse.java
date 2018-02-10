package com.example.fooddeuk.model.order;

import com.example.fooddeuk.model.cart.CartItem;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by heo on 2018. 1. 22..
 */

public class OrderResponse {

    public RestaurantSide restaurant;
    public UserSide user;
    public ArrayList<CartItem> cartItems;
    public String status;

    @SerializedName("_id")
    public String id;
    private String created_at;

    public OrderResponse(RestaurantSide restaurant, UserSide user, ArrayList<CartItem> cartItems, String status) {
        this.restaurant = restaurant;
        this.user = user;
        this.cartItems = cartItems;
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderResponse{" +
                "restaurant=" + restaurant +
                ", user=" + user +
                ", cartItems=" + cartItems +
                ", status='" + status + '\'' +
                '}';
    }

    public String getCreated_at() {
        return "주문시간 : " +created_at.replace("T", " ").substring(0,16);
    }
}
