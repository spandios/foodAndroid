package com.example.heojuyeong.foodandroid.model;

/**
 * Created by heojuyeong on 2017. 9. 18..
 */


public class UserItem {
    int user_id;
    String user_name;
    String phone;


    public UserItem(int user_id, String user_name, String phone) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.phone = phone;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
