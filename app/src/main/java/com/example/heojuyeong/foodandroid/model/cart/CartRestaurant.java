package com.example.heojuyeong.foodandroid.model.cart;

import io.realm.RealmObject;

/**
 * Created by heojuyeong on 2017. 9. 19..
 */

public class CartRestaurant extends RealmObject{

    int rest_id;
    String name;
    String address;
    int rest_admin_id;
    String distance;
    double lat;
    double lng;

    public CartRestaurant(){

    }

    public CartRestaurant(int rest_id, String name, String address, int rest_admin_id, String distance, double lat, double lng) {
        this.rest_id = rest_id;
        this.name = name;
        this.address = address;
        this.rest_admin_id = rest_admin_id;
        this.distance = distance;
        this.lat=lat;
        this.lng=lng;

    }

    public String getAddress() {
        return address;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getAdress() {
        return address;
    }

    public void setAdress(String adress) {
        this.address = adress;
    }

    public int getRest_admin_id() {
        return rest_admin_id;
    }

    public void setRest_admin_id(int rest_admin_id) {
        this.rest_admin_id = rest_admin_id;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
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
