package com.example.fooddeuk.order.model;

import com.example.fooddeuk.user.User;

/**
 * Created by heo on 2018. 1. 22..
 */


public class UserSide{
    public String user_id;
    public String arrivedTime;
    public String request;
    public String completePrice;
    public String user_name;
    public String fcm_token;
    public String phone;


    public UserSide(User user, String arrivedTime, String request, String completePrice) {
        this.user_id=user.user_id;
        this.user_name=user.user_name;
        this.fcm_token=user.fcm_token;
        this.phone=user.phone;
        this.arrivedTime = arrivedTime;
        this.request = request;
        this.completePrice = completePrice;
    }
}
