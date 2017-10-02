package com.example.heojuyeong.foodandroid.model;


import com.example.heojuyeong.foodandroid.model.cart.CartItem;

import java.util.ArrayList;

/**
 * Created by heojuyeong on 2017. 9. 13..
 */


public class OrderItem  {

    public int user_id;
    public int rest_id;
    public int rest_admin_id;
    public ArrayList<CartItem> cartItems;
    public String arrivedTime;
    public String request;
    public int status;
    public String completePrice;

    public OrderItem(int user_id, int rest_id, int rest_admin_id, ArrayList<CartItem> cartItems, String arrivedTime, String request, int status, String completePrice) {
        this.user_id = user_id;
        this.rest_id = rest_id;
        this.rest_admin_id = rest_admin_id;
        this.cartItems = cartItems;
        this.arrivedTime = arrivedTime;
        this.request = request;
        this.status = status;
        this.completePrice = completePrice;
    }
}


