package com.example.fooddeuk.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by heojuyeong on 2017. 9. 18..
 */


public class UserResponse {

    @SerializedName("exist")
    public boolean exist;

    @SerializedName("user")
    public User user;

    public UserResponse(){

    }
    public UserResponse(boolean exist, User user) {
        this.exist = exist;
        this.user = user;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "exist=" + exist +
                ", user=" + user +
                '}';
    }






}
