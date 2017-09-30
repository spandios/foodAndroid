package com.example.heojuyeong.foodandroid.model.restaurant;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by heojuyeong on 2017. 9. 30..
 */

public class RestaurantItemRealm extends RealmObject{
    @PrimaryKey
    int id=1;
    public int rest_id;
    public int rest_admin_id;
    public String name;
    public String address;
    public String openhour;
    public String avg_cooking_time;

    public RestaurantItemRealm(){

    }

    public RestaurantItemRealm(int rest_id, int rest_admin_id, String name, String address, String openhour, String avg_cooking_time) {
        this.rest_id = rest_id;
        this.rest_admin_id = rest_admin_id;
        this.name = name;
        this.address = address;
        this.openhour = openhour;
        this.avg_cooking_time = avg_cooking_time;
    }


    public int getRest_id() {
        return rest_id;
    }

    public void setRest_id(int rest_id) {
        this.rest_id = rest_id;
    }

    public int getRest_admin_id() {
        return rest_admin_id;
    }

    public void setRest_admin_id(int rest_admin_id) {
        this.rest_admin_id = rest_admin_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpenhour() {
        return openhour;
    }

    public void setOpenhour(String openhour) {
        this.openhour = openhour;
    }

    public String getAvg_cooking_time() {
        return avg_cooking_time;
    }

    public void setAvg_cooking_time(String avg_cooking_time) {
        this.avg_cooking_time = avg_cooking_time;
    }
}
