package com.example.fooddeuk.model.menu;


import com.example.fooddeuk.model.cart.CartItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by heojuyeong on 2017. 9. 13..
 */


public class OrderItem  {

    public int user_id;
    public int rest_id;
    public String rest_admin_id;
    public ArrayList<CartItem> cartItems;
    public String arrivedTime;
    public String request;
    public String status;
    public String completePrice;
    public String create_at;



    public OrderItem(int user_id, int rest_id, String rest_admin_id, ArrayList<CartItem> cartItems, String arrivedTime, String request, String status, String completePrice) {
        this.user_id = user_id;
        this.rest_id = rest_id;
        this.rest_admin_id = rest_admin_id;
        this.cartItems = cartItems;
        this.arrivedTime = arrivedTime;
        this.request = request;
        this.status = status;
        this.completePrice = completePrice;
        this.create_at=new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(new Date(System.currentTimeMillis()));;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public int getRest_id() {
        return rest_id;
    }

    public void setRest_id(int rest_id) {
        this.rest_id = rest_id;
    }

    public String getRest_admin_id() {
        return rest_admin_id;
    }

    public void setRest_admin_id(String rest_admin_id) {
        this.rest_admin_id = rest_admin_id;
    }

    public ArrayList<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(ArrayList<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public String getArrivedTime() {
        return arrivedTime;
    }

    public void setArrivedTime(String arrivedTime) {
        this.arrivedTime = arrivedTime;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompletePrice() {
        return completePrice;
    }

    public void setCompletePrice(String completePrice) {
        this.completePrice = completePrice;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "user_id=" + user_id +
                ", rest_id=" + rest_id +
                ", rest_admin_id='" + rest_admin_id + '\'' +
                ", cartItems=" + cartItems +
                ", arrivedTime='" + arrivedTime + '\'' +
                ", request='" + request + '\'' +
                ", status='" + status + '\'' +
                ", completePrice='" + completePrice + '\'' +
                ", create_at='" + create_at + '\'' +
                '}';
    }
}


