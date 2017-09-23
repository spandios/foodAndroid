package com.example.heojuyeong.foodandroid.model.cart;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by heojuyeong on 2017. 9. 19..
 */

public class CartItem extends RealmObject {
    @PrimaryKey
    int id;

    CartMenu menu;
    RealmList<CartOption> option;
    CartRestaurant restaurant;

    public CartItem(){

    }
    public CartItem(int id, CartMenu menu,CartRestaurant cartRestaurant,RealmList<CartOption> option) {
        this.id = id;
        this.menu = menu;
        this.restaurant=cartRestaurant;
        this.option=option;
    }

    public CartItem(int id, CartMenu menu, CartRestaurant restaurant) {
        this.id = id;
        this.menu = menu;
        this.restaurant = restaurant;
    }

    public RealmList<CartOption> getOption() {
        return option;
    }

    public void setOption(RealmList<CartOption> option) {
        this.option = option;
    }

    public CartRestaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(CartRestaurant restaurant) {
        this.restaurant = restaurant;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CartMenu getMenu() {
        return menu;
    }

    public void setMenu(CartMenu menu){
        this.menu=menu;
    }
}
