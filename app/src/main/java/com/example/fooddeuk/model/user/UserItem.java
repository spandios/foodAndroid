package com.example.fooddeuk.model.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by heojuyeong on 2017. 9. 18..
 */


public class UserItem {

    @SerializedName("exist")
    public boolean exist;

    @SerializedName("user")
    public User user;

    public UserItem(){

    }
    public UserItem(boolean exist, User user) {
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
        return "UserItem{" +
                "exist=" + exist +
                ", user=" + user +
                '}';
    }






}
