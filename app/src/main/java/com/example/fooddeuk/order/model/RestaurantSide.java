package com.example.fooddeuk.order.model;

import com.example.fooddeuk.restaurant.model.Restaurant;

/**
 * Created by heo on 2018. 1. 22..
 */

public class RestaurantSide{
    public String _id;
    public String rest_id ;
    public int owner_id;
    public String rest_name;
    public double lat;
    public double lng;
    public String picture;
    public String tel;
    public String address;


    public RestaurantSide(Restaurant restaurant) {
        this._id = restaurant._id;
        this.rest_id = restaurant.rest_id;
        this.owner_id = restaurant.owner_id;
        this.rest_name = restaurant.name;
        this.picture=restaurant.picture;
        this.lat = restaurant.lat;
        this.lng = restaurant.lng;
        this.tel=restaurant.tel;
        this.address=restaurant.address;
    }
}
