package com.example.heojuyeong.foodandroid.model.cart;

import io.realm.RealmObject;

/**
 * Created by heojuyeong on 2017. 9. 19..
 */

public class CartRestaurant extends RealmObject{

    int rest_id;
    String name;
    public CartRestaurant(){

    }
    public CartRestaurant(int rest_id, String name) {
        this.rest_id = rest_id;
        this.name = name;
    }

    public int getRest_id() {
        return rest_id;
    }

    public void setRest_id(int rest_id) {
        this.rest_id = rest_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
