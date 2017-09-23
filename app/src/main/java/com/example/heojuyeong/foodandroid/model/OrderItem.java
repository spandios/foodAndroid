package com.example.heojuyeong.foodandroid.model;


import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.ArrayList;

/**
 * Created by heojuyeong on 2017. 9. 13..
 */

@Parcel
public class OrderItem  {

    MenuItem menuItem;
    CurrentLocationRestaurantItem.Restaurant restaurant;
    ArrayList<MenuItem.Options.Option> optionNecessary;
    ArrayList<MenuItem.Options.Option> optionNotNecessary;
    int status;
    public OrderItem(){

    }

   @ParcelConstructor
    public OrderItem(MenuItem menuItem, CurrentLocationRestaurantItem.Restaurant restaurant, ArrayList<MenuItem.Options.Option> optionNecessary, ArrayList<MenuItem.Options.Option> optionNotNecessary, int status) {

        this.menuItem = menuItem;
        this.restaurant = restaurant;
        this.optionNecessary = optionNecessary;
        this.optionNotNecessary = optionNotNecessary;
        this.status = status;
    }

    public OrderItem(MenuItem menuItem, CurrentLocationRestaurantItem.Restaurant restaurant, int status) {

        this.menuItem = menuItem;
        this.restaurant = restaurant;
        this.status = status;
        optionNecessary=null;
        optionNotNecessary=null;
    }





    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public CurrentLocationRestaurantItem.Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(CurrentLocationRestaurantItem.Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public ArrayList<MenuItem.Options.Option> getOptionNecessary() {
        return optionNecessary;
    }

    public void setOptionNecessary(ArrayList<MenuItem.Options.Option> optionNecessary) {
        this.optionNecessary = optionNecessary;
    }

    public ArrayList<MenuItem.Options.Option> getOptionNotNecessary() {
        return optionNotNecessary;
    }

    public void setOptionNotNecessary(ArrayList<MenuItem.Options.Option> optionNotNecessary) {
        this.optionNotNecessary = optionNotNecessary;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}


